package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.zxing.WriterException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.common.util.CodeImgUtil;
import org.xxpay.mch.utils.AppPush;
import org.xxpay.mch.utils.SpeakerUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/27
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/payment")
public class PaymentController extends BaseController {

    private static final MyLog _log = MyLog.getLog(PaymentController.class);

    @Autowired
    private RpcCommonService rpcCommonService;


    /**
     * 充值第三方跳转页面
     * @return
     */
    @RequestMapping("/recharge_redirect")
    public String redirectRecharge( HttpServletResponse response, ModelMap model) throws IOException {
        String logPrefix = "[wx_openid_redirect]";
        _log.info("{} start", logPrefix);
        String payUrl = request.getParameter("payUrl");
        if(StringUtils.isBlank(payUrl)) {
            model.put("errMsg", RetEnum.RET_COMM_PARAM_ERROR);
            return PAGE_COMMON_ERROR;
        }
        model.put("payUrl", payUrl);
        return "payment/redirect";
    }

    /**
     * 交易查询
     * @return
     */
    @RequestMapping("/trade_query")
    @ResponseBody
    public ResponseEntity<?> queryTrade() {
        JSONObject jsonObject = new JSONObject();
        String tradeOrderId = request.getParameter("tradeOrderId");
        if(StringUtils.isBlank(tradeOrderId)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(tradeOrderId);
        if(mchTradeOrder == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        byte status = mchTradeOrder.getStatus();
        jsonObject.put("status", status);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(jsonObject));
    }

    /**
     * 接收支付中心通知
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify")
    @ResponseBody
    public String payNotify( HttpServletResponse response) throws Exception {
        _log.info("====== 开始处理支付中心通知 ======");
        Map<String,Object> paramMap = request2payResponseMap(request, new String[]{
                "payOrderId", "mchId", "appId", "productId", "mchOrderNo", "amount", "income", "status",
                "channelOrderNo", "channelAttach", "param1",
                "param2","paySuccTime","backType", "sign"
        });
        String resStr;
        _log.info("[XxPay_Notify], paramMap={}", paramMap);
        String payOrderId = (String) paramMap.get("payOrderId");
        String mchOrderNo = (String) paramMap.get("mchOrderNo");
        Long mchIncome = paramMap.get("income") == null ? 0l : Long.parseLong(paramMap.get("income").toString());
        if (!verifyPayResponse(paramMap)) {
            String errorMessage = "verify request param failed.";
            _log.warn(errorMessage);
            resStr = "fail";
        }else {
            try {
                MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchOrderNo);
                if(mchTradeOrder != null && mchTradeOrder.getStatus() == MchConstant.TRADE_ORDER_STATUS_SUCCESS) {
                    return "success";
                }

                int ret = 0;

                if(
                        MchConstant.TRADE_TYPE_PAY == mchTradeOrder.getTradeType()
                         &&
                        ( "8020".equals(mchTradeOrder.getProductId()) || "8004".equals(mchTradeOrder.getProductId()) )
                ){ //交易类型为收款 ，且产品ID为8020表示微信被扫支付，8004表示js支付，  需要判断会员逻辑；

                    JSONObject openIdJSON = (JSONObject) JSON.parse(paramMap.get("channelAttach").toString());

                    ret = rpcCommonService.rpcMchTradeOrderService.updateSuccess4Member(mchOrderNo, payOrderId, mchIncome,openIdJSON.getString("openId"));

                }else if (MchConstant.TRADE_TYPE_RECHARGE == mchTradeOrder.getTradeType()){//交易类型为充值，根据充值规则赠送积分等

                    ret = rpcCommonService.rpcMchTradeOrderService.updateSuccess4MemberRecharge(mchOrderNo, payOrderId, mchIncome);

                }else{ //保持原有逻辑不变
                    ret = rpcCommonService.rpcMchTradeOrderService.updateStatus4Success(mchOrderNo, payOrderId, mchIncome, 0L);
                }

                // ret返回结果
                // 等于1表示处理成功,返回支付中心success
                // 其他值,返回支付中心fail,让稍后再通知
                if(ret == 1) {
                    resStr = "success";
                    try {
                        //推送消息到App
                        pushMsgToApp(mchTradeOrder);
                        Boolean reslut = pushMsgToSpeaker(mchTradeOrder);
                        if (!reslut) _log.info("云喇叭播报失败！");
                    }catch (Exception e) {
                        _log.error(e, "消息推送异常,tradeOrderId=%s,exception:%s", mchTradeOrder.getTradeOrderId(), e);
                    }
                }else {
                    resStr = "fail";
                }
            }catch (Exception e) {
                resStr = "fail";
                _log.error(e, "执行业务异常,payOrderId=%s.mchOrderNo=%s", payOrderId, mchOrderNo);
            }
        }

        _log.info("[XxPay_Notify]: response={},payOrderId={},mchOrderNo={}", resStr, payOrderId, mchOrderNo);
        _log.info("====== 支付中心通知处理完成 ======");
        return resStr;
    }



    public Map<String, Object> request2payResponseMap( String[] paramArray) {
        Map<String, Object> responseMap = new HashMap<>();
        for (int i = 0;i < paramArray.length; i++) {
            String key = paramArray[i];
            String v = request.getParameter(key);
            if (v != null) {
                responseMap.put(key, v);
            }
        }
        return responseMap;
    }


    /**
     * 获取二维码图片流
     */
    @RequestMapping("/qrcode_img_get")
    public void getQrCodeImg( HttpServletResponse response) throws IOException, WriterException {

        String url = getValStringRequired( "url");
        int width = getValIntegerDefault("width", 200);
        int height = getValIntegerDefault( "height", 200);
        CodeImgUtil.writeQrCode(response.getOutputStream(), url, width, height);
    }

    public boolean verifyPayResponse(Map<String,Object> map) {
        String mchId = (String) map.get("mchId");
        String appId = (String) map.get("appId");
        String payOrderId = (String) map.get("payOrderId");
        String mchOrderNo = (String) map.get("mchOrderNo");
        String amount = (String) map.get("amount");
        String income = (String) map.get("income");
        String sign = (String) map.get("sign");

        if (StringUtils.isEmpty(mchId)) {
            _log.warn("Params error. mchId={}", mchId);
            return false;
        }
        if (StringUtils.isEmpty(payOrderId)) {
            _log.warn("Params error. payOrderId={}", payOrderId);
            return false;
        }
        if (StringUtils.isEmpty(amount) || !NumberUtils.isDigits(amount)) {
            _log.warn("Params error. amount={}", amount);
            return false;
        }
        if (StringUtils.isEmpty(income) || !NumberUtils.isDigits(income)) {
            _log.warn("Params error. income={}", income);
            return false;
        }

        if (StringUtils.isEmpty(sign)) {
            _log.warn("Params error. sign={}", sign);
            return false;
        }

        // 验证签名
        if (!verifySign(map)) {
            _log.warn("verify params sign failed. payOrderId={}", payOrderId);
            return false;
        }

        // 查询业务订单,验证订单是否存在
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchOrderNo);
        if(mchTradeOrder == null) {
            _log.warn("业务订单不存在,payOrderId={},mchOrderNo={}", payOrderId, mchOrderNo);
            return false;
        }
        // 核对金额
        if(mchTradeOrder.getAmount() != Long.parseLong(amount)) {
            _log.warn("支付金额不一致,dbPayPrice={},payPrice={}", mchTradeOrder.getAmount(), amount);
            return false;
        }
        return true;
    }

    public boolean verifySign(Map<String, Object> map) {
        Long mchId = Long.parseLong(map.get("mchId").toString());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            _log.warn("mchInfo not exist. mchId={}", mchId);
            return false;
        }
        String key = mchInfo.getPrivateKey();
        if(StringUtils.isBlank(key)) {
            _log.warn("key is null. mchId={}", mchId);
            return false;
        }
        String localSign = PayDigestUtil.getSign(map, key, "sign");
        String sign = (String) map.get("sign");
        return localSign.equalsIgnoreCase(sign);
    }

    /**
     * 创建支付订单
     * @param mchInfo
     * @param mchApp
     * @param mchTradeOrder
     * @return
     */
    private Map createPayOrder(MchInfo mchInfo, MchApp mchApp, MchTradeOrder mchTradeOrder) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchInfo.getMchId());                          // 商户ID
        if(mchApp != null) paramMap.put("appId", mchApp.getAppId());                           // 应用ID
        paramMap.put("mchOrderNo", mchTradeOrder.getTradeOrderId());        // 商户交易单号
        paramMap.put("productId", mchTradeOrder.getProductId());            // 支付产品ID
        paramMap.put("amount", mchTradeOrder.getAmount());                  // 支付金额,单位分
        paramMap.put("currency", "cny");                                    // 币种, cny-人民币
        paramMap.put("clientIp", mchTradeOrder.getClientIp());              // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                                      // 设备
        paramMap.put("subject", mchTradeOrder.getSubject());
        paramMap.put("body", mchTradeOrder.getBody());
        paramMap.put("notifyUrl", mainConfig.getNotifyUrl());               // 回调URL
        paramMap.put("param1", "");                                         // 扩展参数1
        paramMap.put("param2", "");                                         // 扩展参数2
        paramMap.put("reqTime", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));  // 请求时间
        paramMap.put("version", "1.0");                               // 版本号， 固定参数1.0
        // 如果是微信公众号支付需要传openId
        if(8004 == mchTradeOrder.getProductId()) {
            JSONObject extra = new JSONObject();
            extra.put("openId", mchTradeOrder.getChannelUserId());              // 用户openId
            paramMap.put("extra", extra.toJSONString());                        // 附加参数
        }
        // 支付宝条码
        if(8021 == mchTradeOrder.getProductId())  {
            paramMap.put("extra", mchTradeOrder.getChannelUserId());            // 附加参数
        }

        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("xxpay_req:{}", reqData);

        String url = mainConfig.getPayUrl() + "/pay/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        _log.info("xxpay_res:{}", result);
        Map retMap = JSON.parseObject(result);
        if(XXPayUtil.isSuccess(retMap)) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, mchInfo.getPrivateKey(), "sign");
            String retSign = (String) retMap.get("sign");
            //if(checkSign.equals(retSign)) return retMap;
            //_log.info("验签失败:retSign={},checkSign={}", retSign, checkSign);
            return retMap;
        }
        return retMap;
    }

    //异步消息通知
    @Async("appPushExecutor")
    public void pushMsgToApp(MchTradeOrder mchTradeOrder) {
        _log.info("待推送订单：{}",mchTradeOrder);
        //推送消息，根据订单操作员查找设备cid
        String operatorId = mchTradeOrder.getOperatorId();
        _log.info("订单operatorId：{}", operatorId);
        if (StringUtils.isBlank(operatorId)) {
            Long mchId = mchTradeOrder.getMchId();
            SysUser user = rpcCommonService.rpcSysService.getOne(new QueryWrapper<SysUser>().lambda()
                                        .eq(SysUser::getBelongInfoId, mchId)
                                        .eq(SysUser::getIsSuperAdmin, MchConstant.PUB_YES));
            operatorId = String.valueOf(user.getUserId());
        }

        if (StringUtils.isNotBlank(operatorId)) {
            Long userId = Long.valueOf(operatorId);
            MchAppConfig config = rpcCommonService.rpcMchAppConfigService.getById(userId);
            if (config != null && config.getAppVoice() == MchConstant.PUB_YES) {
                LambdaQueryWrapper<MchDevice> lambda = new QueryWrapper<MchDevice>().lambda();
                lambda.eq(MchDevice::getUserId, userId);
                List<MchDevice> list = rpcCommonService.rpcMchDeviceService.list(lambda);
                _log.info("推送设备列表：{}", list);

                String amount = AmountUtil.convertCent2DollarShort(String.valueOf(mchTradeOrder.getAmount()));
                String content = "小新支付收款" + amount + "元";

                //获取app推送配置
                Map<String, String> uniPushConfig = rpcCommonService.rpcSysConfigService.selectByCodes("uniPushAppId", "uniPushAppKey", "uniPushMasterSecret");

                for (MchDevice mchDevice : list) {
                    String result = AppPush.pushMessageToSingle(content, mchDevice.getCid(), uniPushConfig.get("uniPushAppId"),
                            uniPushConfig.get("uniPushAppKey"), uniPushConfig.get("uniPushMasterSecret"));
                    _log.info("订单{}, App消息推送到用户{}, 结果：{}", mchTradeOrder.getTradeOrderId(), userId, result);
                }
            }
        }
    }

    /**
     * 云喇叭播放
     * @param mchTradeOrder
     */
    public Boolean pushMsgToSpeaker(MchTradeOrder mchTradeOrder) {
        _log.info("待播放订单：{}",mchTradeOrder);
        //获取商户ID
        Long mchId = mchTradeOrder.getMchId();
        //获取商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        //根据所属代理商ID获取配置信息
        IsvSpeakerConfig config = rpcCommonService.rpcIsvSpeakerConfigService.getOne(
                new QueryWrapper<IsvSpeakerConfig>().lambda()
                        .eq(IsvSpeakerConfig::getIsvId, mchInfo.getIsvId())
        );

        if (config == null) {
            _log.info("服务商未配置云喇叭");
            return false;
        }

        if (config.getStatus() == MchConstant.PUB_NO) {
            _log.info("服务商配置已关闭");
            return false;
        }

        //根据门店ID获取门店配置信息
        MchStoreSpeaker storeSpeaker = rpcCommonService.rpcMchStoreSpeakerService.getById(mchTradeOrder.getStoreId());
        if (storeSpeaker == null) {
            _log.info("门店配置不存在");
            return false;
        }
        //判断状态是否正确
        if (storeSpeaker.getStatus() != MchConstant.MCH_STORE_SPEAKER_STATUS_USED) {
            _log.info("门店配置已关闭");
            return false;
        }
        Integer productId = mchTradeOrder.getProductId();
        //获取订单支付类型
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if (payProduct == null) {
            _log.info("支付产品类型不存在{}", mchTradeOrder.getProductId());
            return false;
        }
        int payType = 0;

        Byte type = mchTradeOrder.getProductType();
        if (MchConstant.MCH_TRADE_PRODUCT_TYPE_WX == type) {
            payType = 2;
        }else if (MchConstant.MCH_TRADE_PRODUCT_TYPE_ALIPAY == type) {
            payType = 1;
        } else {
            payType = 0;
        }

        String signature = null;
        JSONObject result = null;
        String code = null;
        //获取云喇叭服务器token
        if (config.getToken() == null) {
            //获取token并存入失效时间
            getSpeakerJson(config);
        }else {
            Integer expireTime = config.getTokenExpire();
            if (expireTime < 10) {
                //获取token并存入失效时间
                getSpeakerJson(config);
            }
            signature = config.getToken();
        }
        //发送消息
        result = SpeakerUtil.postMsg(signature, storeSpeaker.getSpeakerId(), mchTradeOrder.getAmount(), payType);
        code = result.getString("code");
        //如果为验签失败
        if ("WG10005".equals(code)) {
            getSpeakerJson(config);
            signature = config.getToken();
            //发送消息
            result = SpeakerUtil.postMsg(signature, storeSpeaker.getSpeakerId(), mchTradeOrder.getAmount(), payType);
            code = result.getString("code");
        }
        if (!"0".equals(code)){
            _log.info("发送消息失败{}", result.getString("msg"));
            return false;
        }
        return true;
    }

    private void getSpeakerJson(IsvSpeakerConfig config){
        JSONObject json = SpeakerUtil.getSignature(config.getUserId(), config.getUserPassword());
        config.setToken(json.getString("signature"));
        config.setTokenExpire(json.getInteger("remainTime"));
        rpcCommonService.rpcIsvSpeakerConfigService.updateById(config);
    }
}
