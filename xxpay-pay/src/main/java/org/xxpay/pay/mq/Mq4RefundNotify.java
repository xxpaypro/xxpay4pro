package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ScheduledMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.RefundInterface;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 业务通知MQ实现
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-10-30
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Mq4RefundNotify {

    @Autowired
    private Queue refundNotifyQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BaseNotify4MchRefund baseNotify4MchRefund;

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4RefundNotify.class);

    public void send(String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(this.refundNotifyQueue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(this.refundNotifyQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    @JmsListener(destination = MqConfig.REFUND_NOTIFY_QUEUE_NAME)
    public void receive(String msg) {
        _log.info("处理退款任务.msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        String refundOrderId = msgObj.getString("refundOrderId");
        String channelType = msgObj.getString("channelType");
        RefundOrder refundOrder = rpcCommonService.rpcRefundOrderService.findByRefundOrderId(refundOrderId);
        if(refundOrder == null) {
            _log.warn("查询退款订单为空,不能退款.refundOrderId={}", refundOrderId);
            return;
        }
        if(refundOrder.getStatus() != PayConstant.REFUND_STATUS_INIT) {
            _log.warn("退款状态不是初始({})或失败({}),不能退款.refundOrderId={}", PayConstant.REFUND_STATUS_INIT, PayConstant.REFUND_STATUS_FAIL, refundOrderId);
            return;
        }
        int result = rpcCommonService.rpcRefundOrderService.updateStatus4Ing(refundOrderId, "");
        if(result != 1) {
            _log.warn("更改退款为退款中({})失败,不能退款.refundOrderId={}", PayConstant.REFUND_STATUS_REFUNDING, refundOrderId);
            return;
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("refundOrder", refundOrder);

        JSONObject retObj;
        try{
            RefundInterface refundInterface = (RefundInterface) SpringUtil.getBean(channelType + "RefundService");
            retObj = refundInterface.refund(refundOrder);
        }catch (BeansException e) {
            _log.warn("不支持的退款渠道,停止退款处理.refundOrderId={},channelType={}", refundOrderId, channelType);
            return;
        }
        if(!PayConstant.retIsSuccess(retObj)) {
            _log.warn("发起退款返回异常,停止退款处理.refundOrderId={}", refundOrderId);
            return;
        }
        Boolean isSuccess = retObj.getBooleanValue("isSuccess");
        if(isSuccess) {
            // 更新退款状态为成功
            // TODO 考虑事务内完成
            String channelOrderNo = retObj.getString("channelOrderNo");
            result = rpcCommonService.rpcRefundOrderService.updateStatus4Success(refundOrderId, channelOrderNo);
            _log.info("更新退款订单状态为成功({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_SUCCESS, refundOrderId, result);

            PayOrder updatePayOrder = new PayOrder();
            updatePayOrder.setPayOrderId(refundOrder.getPayOrderId());
            updatePayOrder.setIsRefund(MchConstant.PUB_YES);
            updatePayOrder.setRefundTimes(1);
            updatePayOrder.setSuccessRefundAmount(refundOrder.getRefundAmount());
            result = rpcCommonService.rpcPayOrderService.updateByPayOrderId(refundOrder.getPayOrderId(), updatePayOrder);
            _log.info("更新支付订单退款信息,payOrderId={},返回结果:{}", refundOrder.getPayOrderId(), result);

            // 发送商户通知
            if(StringUtils.isNotBlank(refundOrder.getNotifyUrl())) {
                baseNotify4MchRefund.doNotify(refundOrder, true);
            }
        }else {
            // 更新退款状态为失败
            String channelErrCode = retObj.getString("channelErrCode");
            String channelErrMsg = retObj.getString("channelErrMsg");
            result = rpcCommonService.rpcRefundOrderService.updateStatus4Fail(refundOrderId, channelErrCode, channelErrMsg);
            _log.info("更新退款订单状态为失败({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_FAIL, refundOrderId, result);
            // 发送商户通知
            if(StringUtils.isNotBlank(refundOrder.getNotifyUrl())) {
                baseNotify4MchRefund.doNotify(refundOrder, true);
            }
        }
    }
}
