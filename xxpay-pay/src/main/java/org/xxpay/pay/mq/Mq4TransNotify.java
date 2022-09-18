package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.TransInterface;
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
public class Mq4TransNotify {

    @Autowired
    private Queue transNotifyQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BaseNotify4MchTrans baseNotify4MchTrans;

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4TransNotify.class);

    public void send(String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(this.transNotifyQueue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(this.transNotifyQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    @JmsListener(destination = MqConfig.TRANS_NOTIFY_QUEUE_NAME)
    public void receive(String msg) {
        _log.info("处理转账任务.msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        String transOrderId = msgObj.getString("transOrderId");
        String channelType = msgObj.getString("channelType");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        if(transOrder == null) {
            _log.warn("查询转账订单为空,不能转账.transOrderId={}", transOrderId);
            return;
        }
        if(transOrder.getStatus() != PayConstant.TRANS_STATUS_INIT) {
            _log.warn("转账状态不是初始({})或失败({}),不能转账.transOrderId={}", PayConstant.TRANS_STATUS_INIT, PayConstant.TRANS_STATUS_FAIL, transOrderId);
            return;
        }
        int result = rpcCommonService.rpcTransOrderService.updateStatus4Ing(transOrderId, "");
        if(result != 1) {
            _log.warn("更改转账为转账中({})失败,不能转账.transOrderId={}", PayConstant.TRANS_STATUS_TRANING, transOrderId);
            return;
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("transOrder", transOrder);
        QueryRetMsg retObj;
        try{
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            retObj = transInterface.trans(transOrder);
        }catch (BeansException e) {
            _log.warn("不支持的转账渠道,停止转账处理.transOrderId={},channelType={}", transOrderId, channelType);
            return;
        }
        if(retObj == null) {
            _log.warn("发起转账返回异常,停止转账处理.transOrderId={}", transOrderId);
            return;
        }
        if(QueryRetMsg.ChannelState.CONFIRM_SUCCESS.equals(retObj.getChannelState())) {
            // 更新转账状态为成功
            String channelOrderNo = retObj.getChannelOrderId();
            result = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
            _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, result);
            // 发送商户通知
            baseNotify4MchTrans.doNotify(transOrder, true);
        }else if(QueryRetMsg.ChannelState.CONFIRM_FAIL.equals(retObj.getChannelState())) {
            // 更新转账状态为失败
            String channelErrCode = retObj.getChannelErrCode();
            String channelErrMsg = retObj.getChannelErrMsg();
            result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
            _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
            // 发送商户通知
            baseNotify4MchTrans.doNotify(transOrder, true);
        }

    }
}
