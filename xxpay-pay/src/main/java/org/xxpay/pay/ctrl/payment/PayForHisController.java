package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.api.*;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.RpcCommonService;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

@RestController
public class PayForHisController extends BaseController {

    private final MyLog _log = MyLog.getLog(PayForHisController.class);

    //"http://localhost:8201/api";
    //@Value("${config.payUrl}")
    private String payUrl = "http://localhost:8201/api";

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 对接his支付条形码付款
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/pay/his/create_order")
    public AbstractRes payOrder(HttpServletRequest request) {
        try {
            _log.info("###### 开始接收HIS统一下单请求 ######");
            String logPrefix = "【HIS系统统一下单】";
            JSONObject requestParams = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, requestParams);
            String mchId = requestParams.getString("mchId");
            String amount = requestParams.getString("amount");
            String subject = requestParams.getString("subject");
            String extra = requestParams.getString("extra");  //扩展参数, 条形码参数
            //条形码不能为空
            if (StringUtils.isBlank(extra)) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_PARAMETER_EXTRA_BAR_REQUIRED);
            }
            String productId = String.valueOf(XXPayUtil.getProductIdByBarCode(extra));
            String mchOrderNo = requestParams.getString("mchOrderNo");

            if(org.springframework.util.StringUtils.isEmpty(amount) || org.springframework.util.StringUtils.isEmpty(productId) || org.springframework.util.StringUtils.isEmpty(mchOrderNo)){
                return ApiBuilder.bizError(RetEnum.RET_HIS_PARAMETER_AMT_AND_PRODUCTID_AND_MCHORDERNO_REQUIRED);
            }

            //金额转换为  分 为单位
            String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
            Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(Integer.valueOf(productId));  //mchTradeOrder中的 productType
            String clientIp = "39.98.42.221";

            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", mchId);  //商户ID
            paramMap.put("productId", productId);  //支付产品ID
            paramMap.put("mchOrderNo", mchOrderNo);   //商户订单号
            paramMap.put("currency", "cny");   //币种
            paramMap.put("amount", amountParam);   //支付金额
            paramMap.put("notifyUrl", "https://mch.pay.ncmedical.cn/api/payment/notify");   //支付结果后台回调URL
            paramMap.put("subject", subject);  //商品主题
            paramMap.put("body", PayConstant.PAY_PRODUCT_WX_BAR == Integer.parseInt(productId) ? "收款微信条码支付" : "收款支付宝条码支付");   //商品描述信息
            paramMap.put("extra", extra);  //附加参数 条形码
            paramMap.put("clientIp", clientIp);   //客户端IP
            paramMap.put("device", "his");   //客户端设备

            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(Long.parseLong(mchId));
            if(mchInfo == null) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCHID_REQUIRED, mchId);
            }
            if(mchInfo.getStatus() != 1) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_STATE_ERROR, mchId);
            }
            String mchKey = mchInfo.getPrivateKey();
            if (StringUtils.isBlank(mchKey)) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_KEY_REQUIRED, mchId);
            }

            SysUser sysUser = rpcCommonService.rpcSysService.findByUserName(mchInfo.getLoginUserName());

            MchTradeOrder addMchTradeOrder = rpcCommonService.rpcMchTradeOrderService.insertTradeOrderHis(
                    mchInfo, MchConstant.TRADE_TYPE_PAY, Long.valueOf(amountParam), Long.valueOf(amountParam), tradeProductType, Integer.parseInt(productId), clientIp,
                    mchOrderNo,null, sysUser != null ? sysUser.getStoreId() : null,
                    (mchTradeOrder) -> {
                        mchTradeOrder.setUserId(extra); //用户ID
                        mchTradeOrder.setIsvDeviceNo(null); //服务商设备编号
                        mchTradeOrder.setDepositMode((byte)0); //是否押金模式
                    });

            //入库失败
            if (addMchTradeOrder == null) {
                return ApiBuilder.bizError(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);
            }

            //tradeOrderId
            String tradeOrderId = addMchTradeOrder.getTradeOrderId();

            //生成签名数据
            String reqSign = PayDigestUtil.getSign(paramMap, mchKey);
            paramMap.put("sign", reqSign);

            String reqData = "params=" + paramMap.toJSONString();
            String url = payUrl + "/pay/create_order?";
            // 发起Http请求下单
            String result = XXPayUtil.call4Post(url + reqData);
            _log.info("[xxpay] res:{}", result);

            PayRes payRes = ApiBuilder.buildSuccess(PayRes.class);
            if (StringUtils.isNotBlank(result)) {
                payRes = JSONArray.parseObject(result, PayRes.class);
                String channelOrderStatus = String.valueOf(payRes.getOrderStatus());
                String channelPayOrderId = payRes.getPayOrderId();
                //[6.] 更新订单号
                MchTradeOrder updateRecord = new MchTradeOrder();
                updateRecord.setTradeOrderId(tradeOrderId);
                updateRecord.setPayOrderId(channelPayOrderId);
                updateRecord.setStatus(Byte.valueOf(channelOrderStatus));
                rpcCommonService.rpcMchTradeOrderService.updateById(updateRecord);
                if ("0".equals(payRes.getRetCode())) {
                    _log.info("{} 下单成功,结果:{}", logPrefix, JSON.toJSONString(payRes));
                }else {
                    _log.info("{} 下单失败,结果:{}", logPrefix, JSON.toJSONString(payRes));

                }
                return payRes;
            }else {
                return ApiBuilder.bizError("his调用支付中心【条码支付】下单失败");
            }
        }catch (Exception e) {
            _log.error(e, "");
        }
        return ApiBuilder.bizError(PayEnum.ERR_0010);
    }

    @RequestMapping(value = "/api/pay/his/query_order")
    public AbstractRes queryPayOrder(HttpServletRequest request) {
        try {
            _log.info("###### 开始接收HIS查询支付订单请求 ######");
            String logPrefix = "【HIS系统查询支付订单】";
            JSONObject requestParams = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, requestParams);
            String mchId = requestParams.getString("mchId");
            String mchOrderNo = requestParams.getString("mchOrderNo");
            String payOrderId = requestParams.getString("payOrderId");

            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", mchId);
            paramMap.put("mchOrderNo", mchOrderNo);
            paramMap.put("payOrderId", payOrderId);
            //默认true, 如果是true则支付中心会再次向用户发起回调
            paramMap.put("executeNotify", false);

            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(Long.parseLong(mchId));
            if(mchInfo == null) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCHID_REQUIRED, mchId);
            }
            if(mchInfo.getStatus() != 1) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_STATE_ERROR, mchId);
            }
            String mchKey = mchInfo.getPrivateKey();
            if (StringUtils.isBlank(mchKey)) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_KEY_REQUIRED, mchId);
            }

            //生成签名数据
            String reqSign = PayDigestUtil.getSign(paramMap, mchKey);
            paramMap.put("sign", reqSign);
            String reqData = "params=" + paramMap.toJSONString();
            String url = payUrl + "/pay/query_order?";
            // 发起Http请求下单
            String result = XXPayUtil.call4Post(url + reqData);
            _log.info("[xxpay] res:{}", result);

            PayQueryRes payQueryRes = ApiBuilder.buildSuccess(PayQueryRes.class);
            if (StringUtils.isNotBlank(result)) {
                payQueryRes = JSONArray.parseObject(result, PayQueryRes.class);
                if ("0".equals(payQueryRes.getRetCode())) {
                    _log.info("{} 支付订单查询成功,结果:{}", logPrefix, payQueryRes);
                }else {
                    _log.info("{} 支付订单查询失败,结果:{}", logPrefix, payQueryRes);
                }
                return payQueryRes;
            }else {
                return ApiBuilder.bizError("his支付订单查询失败");
            }
        }
        catch(Exception ex) {
            _log.error(ex, "");
        }
        return ApiBuilder.bizError(PayEnum.ERR_0010);
    }

    /**
     * 退款
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/refund/his/create_order")
    public AbstractRes refundOrder(HttpServletRequest request) {
        try {
            _log.info("###### 开始接收HIS退款请求 ######");
            String logPrefix = "【HIS系统发起退款】";
            JSONObject requestParams = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, requestParams);
            String mchId = requestParams.getString("mchId");
            String payOrderId = requestParams.getString("payOrderId");
            String mchOrderNo = requestParams.getString("mchOrderNo");
            String mchRefundNo = requestParams.getString("mchRefundNo");
            String amount = requestParams.getString("amount");

            //金额转换为  分 为单位
            String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();

            MchTradeOrder dbOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchOrderNo);
            if(dbOrder == null){
                return ApiBuilder.bizError(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST);
            }

            //订单状态 不是支付成功， 也不是部分退款状态时，不允许发起退款
            if(dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_SUCCESS && dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_REFUND_PART){ //支付成功状态
                return ApiBuilder.bizError(RetEnum.RET_MCH_REFUND_STATUS_NOT_SUPPORT);
            }

            if(dbOrder.getAmount() <= dbOrder.getRefundTotalAmount()  ) { //订单支付金额 <= 订单总退金额
                return ApiBuilder.bizError(RetEnum.RET_MCH_ALREADY_REFUNDS);
            }

            if(Long.valueOf(amountParam) >  ( dbOrder.getAmount() - dbOrder.getRefundTotalAmount() ) ) { //退款金额 > （订单金额 - 总退款金额）
                return ApiBuilder.bizError(RetEnum.RET_MCH_REFUNDAMOUNT_GT_PAYAMOUNT);
            }

            //判断当前订单 是否存在[退款中] 订单
            int ingRefundOrder = rpcCommonService.rpcMchRefundOrderService.count(
                    new QueryWrapper<MchRefundOrder>().lambda().eq(MchRefundOrder::getTradeOrderId, mchOrderNo)
                            .eq(MchRefundOrder::getStatus, MchConstant.MCH_REFUND_STATUS_ING));
            if(ingRefundOrder > 0){
                return ApiBuilder.bizError(RetEnum.RET_MCH_TRADE_HAS_REFUNDING_ORDER);
            }

            //插入商户退款表
            MchRefundOrder mchRefundOrder = new MchRefundOrder();
            mchRefundOrder.setMchRefundOrderId(mchRefundNo); //商户退款订单号
            mchRefundOrder.setTradeOrderId(dbOrder.getTradeOrderId()); //支付订单号
            mchRefundOrder.setMchId(dbOrder.getMchId());  //商户ID
            mchRefundOrder.setPayAmount(dbOrder.getAmount());  //支付订单号
            mchRefundOrder.setRefundAmount(Long.valueOf(amountParam)); //退款金额
            mchRefundOrder.setCurrency("CNY");  //币种
            mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_ING);  //默认退款单状态： 退款中
            mchRefundOrder.setRefundDesc("his冲销"); //退款原因
            mchRefundOrder.setCreateTime(new Date());
            rpcCommonService.rpcMchRefundOrderService.save(mchRefundOrder);   //插入商户退款表记录

            String clientIp = "39.98.42.221";
            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", mchId);
            paramMap.put("payOrderId", payOrderId);
            paramMap.put("mchOrderNo", mchOrderNo);
            paramMap.put("mchRefundNo", mchRefundNo);
            paramMap.put("amount", amountParam);
            paramMap.put("currency", "cny");
            paramMap.put("notifyUrl", "");
            paramMap.put("clientIp", clientIp);

            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(Long.parseLong(mchId));
            if(mchInfo == null) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCHID_REQUIRED, mchId);
            }
            if(mchInfo.getStatus() != 1) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_STATE_ERROR, mchId);
            }
            String mchKey = mchInfo.getPrivateKey();
            if (StringUtils.isBlank(mchKey)) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_KEY_REQUIRED, mchId);
            }

            //生成签名数据
            String reqSign = PayDigestUtil.getSign(paramMap, mchKey);
            paramMap.put("sign", reqSign);
            String reqData = "params=" + paramMap.toJSONString();
            String url = payUrl + "/refund/create_order?";
            // 发起Http请求下单
            String result = XXPayUtil.call4Post(url + reqData);
            _log.info("[ncpay] res:{}", result);
            JSONObject retMsg = JSON.parseObject(result);

            RefundOrderRes refundOrderRes = ApiBuilder.buildSuccess(RefundOrderRes.class);
            if (StringUtils.isNotBlank(result)) {
                refundOrderRes = JSONArray.parseObject(result, RefundOrderRes.class);
                if ("0".equals(refundOrderRes.getRetCode())) {

                    if(PayConstant.REFUND_STATUS_FAIL == retMsg.getByte("status")){  //退款失败
                        mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_FAIL);
                    }else if(PayConstant.REFUND_STATUS_SUCCESS ==retMsg.getByte("status")){  //退款成功
                        mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_SUCCESS);
                    }

                    //退款成功
                    if(mchRefundOrder.getStatus() == MchConstant.MCH_REFUND_STATUS_SUCCESS){
                        //更新订单状态
                        rpcCommonService.rpcMchRefundOrderService.refundSuccess(mchRefundOrder, dbOrder);

                    }else if(mchRefundOrder.getStatus() == MchConstant.MCH_REFUND_STATUS_FAIL){ //退款失败
                        rpcCommonService.rpcMchRefundOrderService.refundFail(mchRefundOrder.getMchRefundOrderId());
                    }

                    RefundOrder order = rpcCommonService.rpcRefundOrderService.findByRefundOrderId(refundOrderRes.getRefundOrderId());
                    refundOrderRes.setByOrder(order);
                    refundOrderRes.autoGenSign(mchKey);
                    _log.info("{} his发起退款成功,结果:{}", logPrefix, JSON.toJSONString(refundOrderRes));
                }else {
                    _log.info("{} his发起退款失败,结果:{}", logPrefix, JSON.toJSONString(refundOrderRes));
                }
                return refundOrderRes;

            }else {
                return ApiBuilder.bizError("his退款失败");
            }
        }
        catch(Exception ex) {
            _log.error(ex, "");
        }
        return ApiBuilder.bizError(PayEnum.ERR_0010);
    }


    /**
     * 查询支付退款订单
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/refund/his/query_order")
    public AbstractRes queryRefundOrder(HttpServletRequest request) {
        try {
            _log.info("###### 开始接收HIS查询退款订单请求 ######");
            String logPrefix = "【HIS系统查询退款订单】";
            JSONObject requestParams = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, requestParams);
            String mchId = requestParams.getString("mchId");
            String mchRefundNo = requestParams.getString("mchRefundNo");
            String refundOrderId = requestParams.getString("refundOrderId");

            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", mchId);
            paramMap.put("mchRefundNo", mchRefundNo);
            paramMap.put("refundOrderId", refundOrderId);

            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(Long.parseLong(mchId));
            if(mchInfo == null) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCHID_REQUIRED, mchId);
            }
            if(mchInfo.getStatus() != 1) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_STATE_ERROR, mchId);
            }
            String mchKey = mchInfo.getPrivateKey();
            if (StringUtils.isBlank(mchKey)) {
                return ApiBuilder.bizError(RetEnum.RET_HIS_MCH_KEY_REQUIRED, mchId);
            }

            //生成签名数据
            String reqSign = PayDigestUtil.getSign(paramMap, mchKey);
            paramMap.put("sign", reqSign);
            String reqData = "params=" + paramMap.toJSONString();
            String url = payUrl + "/refund/query_order?";
            // 发起Http请求查询退款订单
            String result = XXPayUtil.call4Post(url + reqData);
            _log.info("[xxpay] res:{}", result);

            RefundOrderQueryRes refundOrderQueryRes = ApiBuilder.buildSuccess(RefundOrderQueryRes.class);
            if (StringUtils.isNotBlank(result)) {
                refundOrderQueryRes = JSONArray.parseObject(result, RefundOrderQueryRes.class);
                if ("0".equals(refundOrderQueryRes.getRetCode())) {
                    RefundOrder refundOrder = null;
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(mchRefundNo)) {
                        refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndMchRefundNo(Long.valueOf(mchId), mchRefundNo);
                    }else if(org.apache.commons.lang3.StringUtils.isNotBlank(refundOrderId)) {
                        refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndRefundOrderId(Long.valueOf(mchId), refundOrderId);
                    }

                    refundOrderQueryRes.setByOrder(refundOrder);
                    refundOrderQueryRes.autoGenSign(mchKey); //自动签名
                    _log.info("{} 查询退款订单成功,结果:{}", logPrefix, JSON.toJSONString(refundOrderQueryRes));
                }else {
                    _log.info("{} 查询退款订单失败,结果:{}", logPrefix, JSON.toJSONString(refundOrderQueryRes));
                }
                return refundOrderQueryRes;
            }else {
                return ApiBuilder.bizError("his支付订单查询失败");
            }
        }
        catch(Exception ex) {
            _log.error(ex, "");
        }
        return ApiBuilder.bizError(PayEnum.ERR_0010);
    }
}
