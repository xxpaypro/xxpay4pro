package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.CashierRes;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.*;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 支付收银台
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-06-07
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Controller
@RequestMapping("/api/cashier/")
public class CashierController extends BaseController {

    private static final MyLog _log = MyLog.getLog(CashierController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private PayOrderService payOrderService;

    @RequestMapping(value = "/{scene}_build")
    @ResponseBody
    public AbstractRes buildPaymentUrl(HttpServletRequest request, @PathVariable("scene") String scene) {
        _log.info("###### 开始接收商户生成{}收银台地址请求 ######", scene);
        String logPrefix = "【生成收银台地址】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            String mchId = po.getString("mchId"); 			    // 商户ID
            String appId = po.getString("appId");               // 应用ID
            String extra = po.getString("extra");               // 支付扩展参数,格式为{"plat":[8001,8002], "bank":8012}
            String mchOrderNo = po.getString("mchOrderNo"); 	// 商户订单号
            String amount = po.getString("amount"); 		    // 支付金额（单位分）
            String notifyUrl = po.getString("notifyUrl"); 		// 支付结果回调URL
            String subject = po.getString("subject");	        // 商品主题
            String body = po.getString("body");	                // 商品描述信息
            String sign = po.getString("sign");	                // 签名数据

            String errorMessage = "";
            // 验证请求参数有效性（必选项）
            if(StringUtils.isBlank(sign)) {
                errorMessage += "请求参数[sign]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            Long mchIdL;
            if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
                errorMessage += "请求参数[mchId]不能为空且为数值类型.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);

            }
            mchIdL = Long.parseLong(mchId);

            if(StringUtils.isBlank(extra)) {
                errorMessage += "请求参数[extra]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            if(StringUtils.isBlank(mchOrderNo)) {
                errorMessage += "请求参数[mchOrderNo]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            if(!NumberUtils.isDigits(amount)) {
                errorMessage += "请求参数[amount]应为数值类型.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            if(StringUtils.isBlank(notifyUrl)) {
                errorMessage += "请求参数[notifyUrl]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            if(StringUtils.isBlank(subject)) {
                errorMessage += "请求参数[subject]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            if(StringUtils.isBlank(body)) {
                errorMessage += "请求参数[body]不能为空.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            // 查询商户信息
            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
            if(mchInfo == null) {
                errorMessage += "商户不存在[mchId="+mchId+"].";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            if(mchInfo.getStatus() != MchConstant.PUB_YES) {
                errorMessage += "商户状态不可用[mchId="+mchId+"].";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            // 查询应用信息
            if(StringUtils.isNotBlank(appId)) {
                MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchIdL, appId);
                if (mchApp == null) {
                    errorMessage += "应用不存在[appId=" + appId + "].";
                    _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                    return ApiBuilder.bizError(errorMessage);
                }
                if (mchApp.getStatus() != MchConstant.PUB_YES) {
                    errorMessage += "应用状态不可用[appId=" + appId + "].";
                    _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                    return ApiBuilder.bizError(errorMessage);
                }
            }

            String key = mchInfo.getPrivateKey();
            if (StringUtils.isBlank(key)) {
                errorMessage += "商户私钥为空,请配置商户私钥[mchId="+mchId+"].";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            // 验证签名数据
            boolean verifyFlag = XXPayUtil.verifyPaySign(po, key);
            if(!verifyFlag) {
                errorMessage = "验证签名失败.";
                _log.info("{}参数校验不通过:{}", logPrefix, errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }

            // 做签名(和收银台验签保持一致)
            extra = MyBase64.encode(extra.getBytes());
            Map desMap = new HashMap<>();
            desMap.put("mchId", mchId);
            if(appId != null) desMap.put("appId", appId);
            desMap.put("extra", extra);
            desMap.put("amount", amount);
            desMap.put("mchOrderNo", mchOrderNo);
            desMap.put("subject", subject);
            desMap.put("body", body);
            desMap.put("notifyUrl", notifyUrl);
            sign = PayDigestUtil.getSign(desMap, key);
            String payUrl;
            if(appId != null) {
                payUrl = String.format("%s/cashier/%s?mchId=%s&appId=%s&extra=%s&amount=%s&mchOrderNo=%s&subject=%s&body=%s&notifyUrl=%s&sign=%s",
                        super.payUrl, scene, mchId, appId, extra, amount,
                        mchOrderNo, URLEncoder.encode(subject), URLEncoder.encode(body), URLEncoder.encode(notifyUrl), sign);
            }else {
                payUrl = String.format("%s/cashier/%s?mchId=%s&&extra=%s&amount=%s&mchOrderNo=%s&subject=%s&body=%s&notifyUrl=%s&sign=%s",
                        super.payUrl, scene, mchId, extra, amount,
                        mchOrderNo, URLEncoder.encode(subject), URLEncoder.encode(body), URLEncoder.encode(notifyUrl), sign);
            }

            CashierRes cashierRes = ApiBuilder.buildSuccess(CashierRes.class);
            cashierRes.setPayUrl(payUrl);
            cashierRes.autoGenSign(key); //自动签名
            return cashierRes;

        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("支付中心系统异常");
        }
    }

    /**
     * 打开PC收银台页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/pc")
    public String pc(HttpServletRequest request, ModelMap model) {

        String mchIdStr = request.getParameter("mchId");
        String appId = request.getParameter("appId");
        String extra = request.getParameter("extra");
        String amount = request.getParameter("amount");
        String mchOrderNo = request.getParameter("mchOrderNo");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        String notifyUrl = request.getParameter("notifyUrl");
        String sign = request.getParameter("sign");

        // 校验参数
        if(!NumberUtils.isDigits(mchIdStr) || StringUtils.isBlank(extra) || !NumberUtils.isDigits(amount)) {
            model.put("errMsg", RetEnum.RET_COMM_PARAM_ERROR);
            return PAGE_COMMON_PC_ERROR;
        }
        Long mchId = Long.parseLong(mchIdStr);

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            model.put("errMsg", RetEnum.RET_MCH_CONFIG_NOT_EXIST);
            return PAGE_COMMON_PC_ERROR;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            model.put("errMsg", RetEnum.RET_MCH_STATUS_CLOSE);
            return PAGE_COMMON_PC_ERROR;
        }

        // 查询应用信息
        if(StringUtils.isNotBlank(appId)) {
            MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchId, appId);
            if (mchApp == null) {
                model.put("errMsg", RetEnum.RET_MCH_CONFIG_NOT_EXIST);
                return PAGE_COMMON_PC_ERROR;
            }

            if (mchApp.getStatus() != MchConstant.PUB_YES) {
                model.put("errMsg", RetEnum.RET_MCH_STATUS_CLOSE);
                return PAGE_COMMON_PC_ERROR;
            }
        }

        // 验证签名
        Map desMap = new HashMap<>();
        desMap.put("mchId", mchId);
        if(appId != null) desMap.put("appId", appId);
        desMap.put("extra", extra);
        desMap.put("amount", amount);
        desMap.put("mchOrderNo", mchOrderNo);
        desMap.put("subject", subject);
        desMap.put("body", body);
        desMap.put("notifyUrl", notifyUrl);

        String key = mchInfo.getPrivateKey();
        if(StringUtils.isBlank(key)) {
            model.put("errMsg", buildRetMap(11902, "商户没有配置私钥"));
            return PAGE_COMMON_PC_ERROR;
        }

        String validateSign = PayDigestUtil.getSign(desMap, key);
        if(!sign.equals(validateSign)) {
            model.put("errMsg", buildRetMap(11903, "签名错误"));
            return PAGE_COMMON_PC_ERROR;
        }


        // extra格式
        // {"plat":[8001,8002], "bank":8012}
        // plat 表示平台支付有哪些产品ID,支持多个
        // bank 表示网银支付有哪些产品,支持一个
        JSONObject extraObj = JSONObject.parseObject(new String(MyBase64.decode(extra)));
        JSONArray platArray = extraObj.getJSONArray("plat");
        if(platArray != null && platArray.size() > 0) {
            List productIds = new LinkedList<>();
            for(int i = 0; i<platArray.size(); i++) {
                productIds.add(platArray.getInteger(i));
            }
            List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(productIds);
            if(CollectionUtils.isEmpty(payProductList)) {
                model.put("errMsg", buildRetMap(11904, "支付产品不存在,producIds=" + productIds));
                return PAGE_COMMON_PC_ERROR;
            }
            model.put("plat", payProductList);
        }
        Integer bankProdcutId = extraObj.getInteger("bank");
        if(bankProdcutId != null) {
            PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(bankProdcutId);
            if(payProduct == null) {
                model.put("errMsg", buildRetMap(11905, "支付产品不存在,producId=" + bankProdcutId));
                return PAGE_COMMON_PC_ERROR;
            }
            model.put("bank", payProduct);
            FeeScale mchPayPassage = rpcCommonService.rpcFeeScaleService.findOne(MchConstant.INFO_TYPE_MCH, mchId, MchConstant.FEE_SCALE_PTYPE_PAY, bankProdcutId);
            if(mchPayPassage == null || mchPayPassage.getStatus() != MchConstant.PUB_YES) {
                model.put("errMsg", buildRetMap(11906, "商户通道不可用,producId=" + bankProdcutId));
                return PAGE_COMMON_PC_ERROR;
            }
            //TODO 删除获取支付通道子账号
            Object obj = null;
            if(obj instanceof String) {
                model.put("errMsg", buildRetMap(11907, "商户通道不可用,producId=" + bankProdcutId + "(" + obj + ")"));
                return PAGE_COMMON_PC_ERROR;
            }
            String ifCode = "";  //TODO 删除获取支付通道子账号
            PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
            String bankExtra = payInterface.getExtra();
            if(StringUtils.isNotBlank(bankExtra)) {
                model.put("bankExtra", JSONArray.parseArray(bankExtra));
            }
        }

        if(model.get("plat") == null && model.get("bankExtra") == null) {
            model.put("errMsg", buildRetMap(11908, "收银台没有可用的支付产品"));
            return PAGE_COMMON_PC_ERROR;
        }

        // 设置支付页面所需参数
        model.put("mchId", mchId);
        model.put("mchName", mchInfo.getMchName());
        model.put("appId", appId);
        model.put("mchOrderNo", mchOrderNo);
        model.put("subject", subject);
        model.put("body", body);
        model.put("amount", amount);
        model.put("amountStr", AmountUtil.convertCent2Dollar(amount+""));
        model.put("notifyUrl", notifyUrl);
        return "cashier/pc";
    }

    /**
     * 打开PC订单完成页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/pc_complete")
    public String pcComplete(HttpServletRequest request, ModelMap model) {

        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        String payOrderId = getString(param, "payOrderId");
        // 校验参数

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            model.put("errMsg", RetEnum.RET_SERVICE_MCH_NOT_EXIST);
            return PAGE_COMMON_PC_ERROR;
        }
        if(MchConstant.PUB_NO == mchInfo.getStatus()) {
            model.put("errMsg", RetEnum.RET_MCH_STATUS_STOP);
            return PAGE_COMMON_PC_ERROR;
        }

        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByMchIdAndPayOrderId(mchId, payOrderId);
        // 判断订单是否存在
        if(payOrder == null) {
            model.put("errMsg", RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST);
            return PAGE_COMMON_PC_ERROR;
        }

        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(payOrder.getProductId());

        // 设置页面所需参数
        model.put("subject", payOrder.getSubject());
        model.put("payType", payProduct == null ? "" : payProduct.getPayType());
        model.put("amountStr", AmountUtil.convertCent2Dollar(payOrder.getAmount()+""));
        model.put("mchOrderNo", payOrder.getMchOrderNo());
        model.put("mchId", mchId);
        model.put("mchName", mchInfo.getMchName());
        model.put("status", payOrder.getStatus());
        return "cashier/pc_complete";
    }

    /**
     * PC支付
     * @param request
     * @return
     */
    @RequestMapping("/pc_pay")
    @ResponseBody
    public ResponseEntity<?> pcPay(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String mchIdStr = request.getParameter("mchId");
        String appId = request.getParameter("appId");
        String amount = request.getParameter("amount");
        String mchOrderNo = request.getParameter("mchOrderNo");
        String productId = request.getParameter("productId");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        String notifyUrl = request.getParameter("notifyUrl");
        String payPassageAccountId = request.getParameter("payPassageAccountId");
        String bankCode = request.getParameter("bankCode");

        if(!NumberUtils.isDigits(mchIdStr) || !NumberUtils.isDigits(amount)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        }
        if(StringUtils.isNotBlank(productId) && !NumberUtils.isDigits(productId)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        }
        Long mchId = Long.parseLong(mchIdStr);
        Long amountL = Long.parseLong(amount);

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CONFIG_NOT_EXIST));
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_CLOSE));
        }

        // 查询应用信息
        if(StringUtils.isNotBlank(appId)) {
            MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchId, appId);
            if (mchApp == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CONFIG_NOT_EXIST));
            }

            if (mchApp.getStatus() != MchConstant.PUB_YES) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_CLOSE));
            }
        }

        PayOrder queryPayOrder = new PayOrder();
        queryPayOrder.setMchOrderNo(mchOrderNo);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.find(queryPayOrder);
        if(payOrder != null) {
            _log.info("商户订单已经存在,不能重复使用.mchOrderNo={}", mchOrderNo);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PAY_ORDER_EXIST));
        }
        String extra = "";
        if(StringUtils.isNotBlank(bankCode)) {
            extra = "{\"bankCode\":\""+bankCode+"\"}";
        }

        // 创建支付订单
        try {

            Map resMap = createPayOrder(mchInfo, appId, mchOrderNo, productId, amountL, subject, body, notifyUrl, payPassageAccountId, extra);
            String payOrderId = resMap.get("payOrderId").toString();

            jsonObject.put("productId", productId);
            jsonObject.put("payUrl", resMap.get("payUrl"));
            jsonObject.put("payAction", resMap.get("payAction"));
            jsonObject.put("payParams", resMap.get("payParams"));
            jsonObject.put("payOrderId", payOrderId);
            return ResponseEntity.ok(XxPayResponse.buildSuccess(jsonObject));
        }catch (Exception e) {
            _log.error(e, "创建订单失败");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_PAY_ORDER_FAIL));
        }
    }

    /**
     * 交易查询
     * @param request
     * @return
     */
    @RequestMapping("/pay_query")
    @ResponseBody
    public ResponseEntity<?> queryPay(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String payOrderId = request.getParameter("payOrderId");
        if(StringUtils.isBlank(payOrderId)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if(payOrder == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        byte status = payOrder.getStatus();
        jsonObject.put("status", status);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(jsonObject));
    }

    /**
     * 创建支付订单
     * @param mchInfo
     * @param appId
     * @return
     */
    private Map createPayOrder(MchInfo mchInfo, String appId, String mchOrderNo, String productId, long amount, String subject, String body, String notifyUrl, String payPassageAccountId, String extra) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchInfo.getMchId());                          // 商户ID
        paramMap.put("appId", appId);                                       // 应用ID
        paramMap.put("mchOrderNo",  mchOrderNo);                            // 商户交易单号
        paramMap.put("productId", productId);                               // 支付产品ID
        paramMap.put("amount", amount);                                     // 支付金额,单位分
        paramMap.put("currency", "cny");                                    // 币种, cny-人民币
        paramMap.put("clientIp", IPUtility.getLocalIP());                   // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                                      // 设备
        paramMap.put("subject",subject);
        paramMap.put("body", body);
        paramMap.put("notifyUrl", notifyUrl);                               // 回调URL
        paramMap.put("param1", "");                                         // 扩展参数1
        paramMap.put("param2", "");                                         // 扩展参数2
        paramMap.put("payPassageAccountId", payPassageAccountId);           // 子账户
        paramMap.put("extra", extra);

        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("xxpay_req:{}", reqData);
        String url = payUrl + "/pay/create_order?";
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
        return null;
    }

    Map buildRetMap(int code, String message) {
        Map retMap = new HashMap<>();
        retMap.put("code", code);
        retMap.put("message", message);
        return retMap;
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

}
