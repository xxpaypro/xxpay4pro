package org.xxpay.pay.channel.sandpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.sandpay.sdk.CertUtil;
import org.xxpay.pay.channel.sandpay.sdk.CryptoUtil;
import org.xxpay.pay.channel.swiftpay.SwiftpayConfig;
import org.xxpay.pay.channel.swiftpay.util.SignUtils;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;
import org.xxpay.pay.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/8/27
 * @description: 杉德支付回调
 */
@Service
public class SandpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(SandpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SANDPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理杉德支付回调】";
        _log.info("====== 开始处理杉德支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = PayConstant.RETURN_SWIFTPAY_VALUE_FAIL;
        try {
            String data = req.getParameter("data");
            String sign  = req.getParameter("sign");
            _log.info("[{}]回调通知参数,data={},sign={}", getChannelName(), data, sign);
            if(data != null && !"".equals(data)){
                JSONObject dataObj = JSON.parseObject(data);

                JSONObject headObj = dataObj.getJSONObject("head");
                JSONObject bodyObj = dataObj.getJSONObject("body");

                payContext.put("parameters", dataObj);
                if(!verifyPayParams(payContext, data, headObj, bodyObj, sign)) {
                    retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_SWIFTPAY_VALUE_FAIL);
                    return retObj;
                }
                payOrder = (PayOrder) payContext.get("payOrder");
                // 处理订单
                byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
                String traceNo = dataObj.getString("traceNo");
                if (payStatus != PayConstant.PAY_STATUS_SUCCESS) {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), traceNo);
                    if (updatePayOrderRows != 1) {
                        _log.warn("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                        retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                        return retObj;
                    }
                    _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                }
                // 业务系统后端通知
                baseNotify4MchPay.doNotify(payOrder, true);
                _log.info("====== 完成处理杉德支付回调通知 ======");
                respString = "respCode=000000";
            }
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;

    }

    /**
     * 验证杉德支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext, String data, JSONObject headObj, JSONObject bodyObj, String sign) {
        //Map<String,String> params = (Map<String,String>)payContext.get("parameters");

        //校验结果是否成功
        String status = bodyObj.getString("orderStatus");
        if(status == null || !"1".equals(status)) {
            _log.error("status={}", status);
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        String out_trade_no = bodyObj.getString("orderCode");		            // 商户订单号
        String total_amount = bodyObj.getString("buyerPayAmount"); 		    // 支付金额
        if (StringUtils.isBlank(out_trade_no)) {
            _log.error("Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (StringUtils.isBlank(total_amount)) {
            _log.error("Notify parameter total_amount is empty. total_amount={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        SandpayConfig sandpayConfig = new SandpayConfig(getPayParam(payOrder));
        String mchId = sandpayConfig.getMchId();
        String publicKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + sandpayConfig.getPublicKeyFile();
        String privateKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator +  sandpayConfig.getPrivateKeyFile();
        String keyPassword = sandpayConfig.getPassword();
        try {
            CertUtil.init(mchId, publicKeyPath, privateKeyPath, keyPassword);
            boolean valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicKey(), "SHA1WithRSA");
            // 验证签名
            if (!valid) {
                errorMessage = "check sign failed.";
                _log.info("{} {}", getChannelName(), errorMessage);
                payContext.put("retMsg", errorMessage);
                return false;
            }
        } catch (Exception e) {
            _log.error(e, "初始化杉德证书失败");
        }

        // 核对金额
        long outPayAmt = new BigDecimal(total_amount).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
