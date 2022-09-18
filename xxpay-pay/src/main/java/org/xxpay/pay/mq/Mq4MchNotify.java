package org.xxpay.pay.mq;


import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.pay.service.RpcCommonService;

import javax.jms.*;

/**
 * @Description: 商户通知MQ统一处理
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-10-31
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Mq4MchNotify {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4MchNotify.class);

    public void send(Queue queue, String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(queue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(Queue queue, String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(queue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    public String httpPost(String url) {
        return XXPayUtil.call4Post(url, 15);
    }

}
