package org.xxpay.pay.channel.accountpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.mq.BaseNotify4MchPay;

/**
 * @author: dingzhiwei
 * @date: 2018/4/5
 * @description: 账户支付
 */
@Service
public class AccountpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(AccountpayPaymentService.class);

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ACCOUNTPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String logPrefix = "【账户支付】";
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        // 将订单更改为支付中
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);

        String userId = payOrder.getChannelUser();
        Long mchId = payOrder.getMchId();
        Long amount = payOrder.getAmount();
        // 扣账户余额
        result = rpcCommonService.rpcUserAccountService.userAccountRollOut(mchId, userId, amount);
        _log.info("{}userId={},mchId={},amount={},结果:{}", logPrefix, userId, mchId, amount, result);

        // 扣款成功
        if(result == 1){
            result = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
            _log.info("[{}]更新订单状态为支付成功:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), result);
            // 发送商户支付成功通知
            if (result == 1) {
                // 通知业务系统
                baseNotify4MchPay.doNotify(payOrder, true);
            }
        }else {
            // 明确为失败,可修改订单状态为失败
            return ApiBuilder.bizError("扣余额失败");
        }

        retObj.setPayMethod(PayConstant.PAY_METHOD_OTHER); //其他支付方式
        return retObj;

    }

    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        return null;
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {
        return null;
    }
}
