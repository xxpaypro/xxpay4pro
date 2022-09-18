package org.xxpay.pay.channel.maxpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.pay.channel.BaseTrans;
import org.xxpay.pay.channel.maxpay.config.PaymaxConfig;
import org.xxpay.pay.channel.maxpay.config.SignConfig;
import org.xxpay.pay.channel.maxpay.model.*;

import java.math.BigDecimal;

/**
 * @author: dingzhiwei
 * @date: 18/05/27
 * @description: 拉卡拉代付接口
 */
@Service
public class MaxpayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(MaxpayTransService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_MAXPAY;
    }
    @Override
    public QueryRetMsg trans(TransOrder transOrder) {
        String logPrefix = "【拉卡拉实时代付】";
        try {
            String transOrderId = transOrder.getTransOrderId();
            MaxpayConfig maxpayConfig = new MaxpayConfig(getTransParam(transOrder));
            PayRealTimeRequest requestData = new PayRealTimeRequest();
            requestData.setOrderNo(transOrderId);
            requestData.setMobileNo("");
            requestData.setAccountType("1");
            requestData.setBankAccountNo(transOrder.getAccountNo());
            requestData.setBankAccountName(transOrder.getAccountName());
            requestData.setAmount(new BigDecimal(AmountUtil.convertCent2Dollar(transOrder.getAmount()+"")));
            requestData.setComment(transOrder.getRemarkInfo());

            SignConfig signConfig = new SignConfig(maxpayConfig.getPrivateKey(),
                    maxpayConfig.getSecretKey(),
                    maxpayConfig.getPaymaxPublicKey());
            String reqData = JSONObject.toJSONString(requestData);
            _log.info("{}发起转账,请求参数:{}", logPrefix, reqData);
            PayRealTimeResponse responseData = Paymax.request(signConfig,
                    PaymaxConfig.API_BASE_URL + PaymaxConfig.PAY_REAL_TIME,
                    reqData, PayRealTimeResponse.class);

            if(responseData == null) {
                return QueryRetMsg.confirmFail();
            }
            _log.info("{}转账完成,返回结果:{}", logPrefix, JSONObject.toJSONString(responseData));

            String code = responseData.getFailureCode();
            OrderStatus orderStatus =responseData.getStatus();

            if("success".equalsIgnoreCase(code) && (orderStatus != null && orderStatus.equals(OrderStatus.SUCCEED))) {

                if(orderStatus.equals(OrderStatus.SUCCEED) || orderStatus.equals(OrderStatus.PROCESSING_TO_SUCCESS)) {   // 成功
                    _log.info("{} >>> 转账成功", logPrefix);
                    return QueryRetMsg.confirmSuccess(responseData.getId());

                }else if(orderStatus.equals(OrderStatus.PROCESSING)) {  // 处理中
                    // 处理中,需要主动去查询
                    return QueryRetMsg.waiting();

                }else if(orderStatus.equals(OrderStatus.FAILED) || orderStatus.equals(OrderStatus.PROCESSING_TO_FAIL)){ // 失败
                    // 处理失败
                    return QueryRetMsg.confirmFail(responseData.getId());

                }else {
                    // 失败
                    return QueryRetMsg.confirmFail(responseData.getId());
                }

            }else { // 失败
                return QueryRetMsg.confirmFail(responseData.getId(), responseData.getFailureCode(), responseData.getFailureMsg());
            }

        }catch (Exception e) {
            _log.error(e, "转账异常");
            return QueryRetMsg.sysError();
        }
    }

    public QueryRetMsg query(TransOrder transOrder) {
        String logPrefix = "【拉卡拉实时代付】";
        String transCode = "ODQU";	//订单查询
        QueryRetMsg retObj = QueryRetMsg.unknown();
        try{
            /*SandpayConfig sandpayConfig = new SandpayConfig(getTransParam(transOrder));
            String merchId = sandpayConfig.getMchId();
            String url = sandpayConfig.getReqUrl();
            url += "queryOrder";

            String result;
            String transOrderId = transOrder.getTransOrderId();
            try {
                result = this.post(sandpayConfig, url, transCode, buildQueryOrderRequest(transOrder, sandpayConfig));
                System.out.println("retData:" + result);

                _log.info("{} >>> 成功", logPrefix);
                retObj.putAll((Map) JSON.toJSON(result));
                retObj.put("isSuccess", true);
                retObj.put("transOrderId", transOrderId);
            } catch (WxPayException e) {
                _log.error(e, "失败");
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                retObj.put("channelErrCode", e.getErrCode());
                retObj.put("channelErrMsg", e.getErrCodeDes());
                retObj.put("isSuccess", false);
            }*/
            return retObj;
        }catch (Exception e) {
            _log.error(e, "拉卡拉实时代付");
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AgentPayBalanceRetMsg balance(String payParam) {
        String logPrefix = "【拉卡拉头寸查询】";
        try {
            MaxpayConfig maxpayConfig = new MaxpayConfig(payParam);
            SignConfig signConfig = new SignConfig(maxpayConfig.getPrivateKey(),
                    maxpayConfig.getSecretKey(),
                    maxpayConfig.getPaymaxPublicKey());
            _log.info("{}发起余额查询", logPrefix);
            PayBalanceResponse responseData = Paymax.request(signConfig,
                    PaymaxConfig.API_BASE_URL + PaymaxConfig.PAY_REAL_TIME_BALANCE ,
                    null,PayBalanceResponse.class);
            if(responseData == null) {
                return AgentPayBalanceRetMsg.fail();
            }
            _log.info("{}查询余额完成,返回结果:{}", logPrefix, JSONObject.toJSONString(responseData));
            String code = responseData.getFailureCode();
            if("success".equalsIgnoreCase(code)) {

                return AgentPayBalanceRetMsg.success(
                        Long.parseLong(AmountUtil.convertDollar2Cent(responseData.getCashBalance().toString())), // 现金余额
                        Long.parseLong(AmountUtil.convertDollar2Cent(responseData.getPayBalance().toString())) // 代付可用余额
                );

            }else { // 失败
                return AgentPayBalanceRetMsg.fail();
            }
        }catch (Exception e) {
            _log.error(e, "查询头寸异常");
            return AgentPayBalanceRetMsg.fail();
        }
    }

    public static void main(String[] args) {

        System.out.println(String.format("%12d", 600).replace(" ", "0"));


    }

}
