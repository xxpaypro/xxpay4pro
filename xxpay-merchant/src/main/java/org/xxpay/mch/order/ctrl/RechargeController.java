package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.MchApp;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 充值ctrl,
 * 原存放在 PaymentController ,
 * 由于spring security 框架设置payment/* 全部不走拦截器，所以无法拿到user信息。
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/recharge")
public class RechargeController extends BaseController {

    private static final MyLog _log = MyLog.getLog(RechargeController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商户充值 发起支付
     * @return
     */
    @RequestMapping("/pay")
    @ResponseBody
    @MethodLog( remark = "发起充值" )
    public ResponseEntity<?> recharge() {
        JSONObject jsonObject = new JSONObject();

        Long mchId = getUser().getBelongInfoId();
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_STOP));
        }

        String productId = request.getParameter("productId");
        String amount = request.getParameter("amount");
        Float amountF = Float.parseFloat(amount) * 100;
        Long amountL = amountF.longValue();

        if(StringUtils.isBlank(productId) || !NumberUtils.isDigits(productId)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        }

        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(Integer.parseInt(productId));
        if(payProduct == null || payProduct.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PAY_PRODUCT_NOT_EXIST));
        }

        // 创建交易订单
        String orderId = MySeq.getTrade();
        MchTradeOrder mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setTradeOrderId(orderId);
        mchTradeOrder.setTradeType(MchConstant.TRADE_TYPE_RECHARGE);    // 充值
        mchTradeOrder.setMchId(mchId);
        mchTradeOrder.setAmount(amountL);
        mchTradeOrder.setProductId(productId != null ? Integer.parseInt(productId) : null);
        mchTradeOrder.setProductType(payProduct.getProductType());
        mchTradeOrder.setSubject("充值" + amountL/100.00 + "元");
        mchTradeOrder.setBody("充值" + amountL/100.00 + "元");
        mchTradeOrder.setClientIp(IPUtility.getClientIp(request));  // 客户端IP

        // 创建支付订单
        try {
            String extra = "{\"bank\":"+productId+"}";
            Map resMap = createCashier(mchInfo, null, mchTradeOrder, extra);
            if(resMap == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_PAY_ORDER_FAIL, "没有返回"));
            }
            int result = rpcCommonService.rpcMchTradeOrderService.add(mchTradeOrder);
            _log.info("创建交易订单, orderId={}, result={}", orderId, result);
            if(result != 1) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL));
            }

            // 更新为处理中
            rpcCommonService.rpcMchTradeOrderService.updateStatus4Ing(mchTradeOrder.getTradeOrderId());

            jsonObject.put("payUrl", resMap.get("payUrl"));
            return ResponseEntity.ok(XxPayResponse.buildSuccess(jsonObject));
        }catch (Exception e) {
            _log.error(e, "创建订单失败");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_PAY_ORDER_FAIL));
        }
    }

    private Map createCashier(MchInfo mchInfo, MchApp mchApp, MchTradeOrder mchTradeOrder, String extra) {
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
        paramMap.put("extra", extra);

        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("xxpay_req:{}", reqData);
        String url = mainConfig.getPayUrl() + "/cashier/pc_build?";
        String result = XXPayUtil.call4Post(url + reqData);
        _log.info("xxpay_res:{}", result);
        Map retMap = JSON.parseObject(result);
        if(retMap != null && PayEnum.OK.getCode().equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, mchInfo.getPrivateKey(), "sign");
            String retSign = (String) retMap.get("sign");
            //if(checkSign.equals(retSign)) return retMap;
            //_log.info("验签失败:retSign={},checkSign={}", retSign, checkSign);
            return retMap;
        }
        return retMap;
    }

}
