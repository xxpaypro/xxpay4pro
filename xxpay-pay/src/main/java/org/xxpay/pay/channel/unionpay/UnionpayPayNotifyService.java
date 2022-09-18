package org.xxpay.pay.channel.unionpay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.unionpay.utils.UniSignUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class UnionpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(UnionpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_UNIONPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理银联支付回调】";
        _log.info("====== 开始处理银联支付回调通知 ======");


        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }

        _log.info("{}请求数据:{}", logPrefix, params);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);
        if(!verifyAliPayParams(payContext)) {
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
            return retObj;
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);

        payOrder = (PayOrder)payContext.get("payOrder");
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期

        if (payStatus == PayConstant.PAY_STATUS_PAYING) { //仅判断支付中， 当前可能为已退款状态

            // https://open.unionpay.com/tjweb/acproduct/list?apiSvcId=468&index=2
            String respCode = params.get("respCode").toString();		//应答码  00/A6 表示支付成功
            if("00".equals(respCode) || "A6".equals(respCode)){

                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(
                                        payOrder.getPayOrderId(), StrUtil.toString(params.get("queryId"), null));
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    _log.info("{}响应结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    retObj.put("resResult", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);


            }else{ //支付失败


            }
        }

        baseNotify4MchPay.doNotify(payOrder, true);
        _log.info("====== 完成处理支付宝支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_UNION_VALUE_SUCCESS);

        return retObj;
    }

    @Override
    public JSONObject doReturn(Object notifyData) {
        String logPrefix = "【处理支付宝同步跳转】";
        _log.info("====== 开始处理支付宝同步跳转 ======");

        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);

        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();

        payContext.put("parameters", params);
        if(!verifyAliPayParams(payContext)) {
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
            return retObj;
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);

        PayOrder payOrder = (PayOrder)payContext.get("payOrder");

        _log.info("====== 完成处理支付宝同步跳转 ======");
        String url = baseNotify4MchPay.createNotifyUrl(payOrder, "1");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_UNION_VALUE_SUCCESS);
        retObj.put(PayConstant.JUMP_URL, url);
        return retObj;
    }

    /**
     * 解析支付宝回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 验证支付宝支付通知参数
     * @return
     */
    public boolean verifyAliPayParams(Map<String, Object> payContext) {

        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        String orderId = params.get("orderId");		// 商户订单号
        String txnAmt = params.get("txnAmt"); 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(orderId)) {
            _log.error(". orderId is empty orderId={}", orderId);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(txnAmt)) {
            _log.error("txnAmt is empty :{}", txnAmt);
            payContext.put("retMsg", "txnAmt is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(orderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", orderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        JSONObject isvParam = getMainPayParam(payOrder);

        //验签
        String unionpayRootCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayRootCertFile");
        String unionpayMiddleCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayMiddleCertFile");

        //验签失败
        if(!UniSignUtils.validate((JSONObject) JSONObject.toJSON(params), unionpayRootCertFile, unionpayMiddleCertFile)) {
            errorMessage = "validate failed.";
            _log.error("Notify parameter {}", params);
            payContext.put("retMsg", errorMessage);
            return false;
        }


        // 核对金额
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != Long.parseLong(txnAmt)) {
            _log.error("db payOrder record payPrice not equals total_amount. txnAmt={},payOrderId={}", txnAmt, orderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }


}
