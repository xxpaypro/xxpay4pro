package org.xxpay.pay.channel.gomepay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.gomepay.util.DSDES;
import org.xxpay.pay.channel.swiftpay.SwiftpayConfig;
import org.xxpay.pay.channel.swiftpay.util.SignUtils;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;
import org.xxpay.pay.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class GomepayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(GomepayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_GOMEPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理银盈通支付回调】";
        _log.info("====== 开始处理银盈通支付回调通知 ======");
        HttpServletRequest request = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        Map<String, String> parameters = new HashMap<>();
        PayOrder payOrder;
        String respString = "处理失败"; // 返回00为成功,其他失败
        try {

            // 明文参数
            String dstbdata = request.getParameter("dstbdata");
            parameters.put("dstbdata", dstbdata);
            // 签名数据
            String dstbdatasign = request.getParameter("dstbdatasign");
            parameters.put("dstbdatasign", dstbdatasign);

            parameters.putAll(handleDate(dstbdata));

            /*// 交易状态,00成功，其他错误
            String returncode = request.getParameter("returncode");
            parameters.put("returncode", returncode);
            // 商户号
            String merchno = request.getParameter("merchno");
            parameters.put("merchno", merchno);
            // 电商订单
            String dsorderid = request.getParameter("dsorderid");
            parameters.put("dsorderid", dsorderid);
            // 交易总金额
            String amount = request.getParameter("amount");
            parameters.put("amount", amount);
            // 系统流水号
            String orderid = request.getParameter("orderid");
            parameters.put("orderid", orderid);
            // 扣款日期 YYYYMMDD
            String transdate = request.getParameter("transdate");
            parameters.put("transdate", transdate);
            // 扣款时间 Hhmmss
            String transtime = request.getParameter("transtime");
            parameters.put("transtime", transtime);
            //
            String syssn = request.getParameter("syssn");
            parameters.put("syssn", syssn);*/
            _log.info("通知参数parameters = {}", parameters);
            // 交易成功
            if(parameters.get("returncode") != null && "00".equals(parameters.get("returncode"))){
                payContext.put("parameters", parameters);
                if(!verifyPayParams(payContext)) {
                    retObj.put(PayConstant.RESPONSE_RESULT, "验证订单数据不通过");
                    return retObj;
                }
                payOrder = (PayOrder) payContext.get("payOrder");
                // 处理订单
                byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
                String transaction_id = parameters.get("orderid");
                if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transaction_id);
                    if (updatePayOrderRows != 1) {
                        _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                        retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                        return retObj;
                    }
                    _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                }
                // 业务系统后端通知
                baseNotify4MchPay.doNotify(payOrder, true);
                _log.info("====== 完成处理银盈通支付回调通知 ======");
                respString = "00";
            }
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;
    }

    Map handleDate(String param) {
        Map paramMap = new HashMap<>();
        //param = "returncode=&merchno=611200000102017&dsorderid=P01201803220704122380000&amount=0.01&orderid=201803221904222712000142&transdate=20180322&transtime=190735";
        String[] params = param.split("&");
        for(int i=0; i<params.length; i++) {
            String p = params[i];
            String[] ps = p.split("=");
            if(ps.length > 1) {
                paramMap.put(ps[0], ps[1]);
            }
        }
        return paramMap;
    }

    /**
     * 验证银盈通支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");

        //校验结果是否成功
        String returncode = params.get("returncode");
        if(returncode == null || !"00".equals(returncode)) {
            _log.error("returncode={}", returncode);
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        String dsorderid = params.get("dsorderid");		// 商户订单号
        String amount = params.get("amount"); 		    // 支付金额
        if (StringUtils.isBlank(dsorderid)) {
            _log.error("GomePay Notify parameter dsorderid is empty. dsorderid={}", dsorderid);
            payContext.put("retMsg", "dsorderid is empty");
            return false;
        }
        if (StringUtils.isBlank(amount)) {
            _log.error("GomePay Notify parameter amount is empty. amount={}", amount);
            payContext.put("retMsg", "amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = dsorderid;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }
        GomepayConfig gomepayConfig = new GomepayConfig(getPayParam(payOrder));
        String dstbdata = params.get("dstbdata");           // 明文数据
        String dstbdatasign = params.get("dstbdatasign");   // 签名数据
        String myDstbdatasign = ""; //
        try {
            myDstbdatasign = DSDES.getBlackData(gomepayConfig.getMediumkey().getBytes(), dstbdata.getBytes("utf-8"));
        } catch (Exception e) {
            _log.error(e, "");
        }
        // 验证签名
        if (!myDstbdatasign.equals(dstbdatasign)) {
            errorMessage = "check sign failed.";
            _log.error("Gomepay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(amount).multiply(new BigDecimal(100)).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
