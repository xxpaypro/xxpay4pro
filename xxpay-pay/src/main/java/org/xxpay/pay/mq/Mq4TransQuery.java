package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.TransInterface;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.jms.*;

/**
 * @Description: 转账订单查询MQ
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-9-6
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Mq4TransQuery {

    @Autowired
    private Queue transQueryQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(Mq4TransQuery.class);

    public void send(String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(this.transQueryQueue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(this.transQueryQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    @JmsListener(destination = MqConfig.TRANS_QUERY_QUEUE_NAME)
    public void receive(String msg) {
        _log.info("处理支付订单查询任务.msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        int count = msgObj.getIntValue("count");
        String transOrderId = msgObj.getString("transOrderId");
        String channelName = msgObj.getString("channelName");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        if(transOrder == null) {
            _log.warn("查询转账订单为空,transOrderId={}", transOrderId);
            return;
        }
        if(transOrder.getStatus() != PayConstant.TRANS_STATUS_TRANING) {
            _log.warn("订单状态不是转账中({}),不需查询上游渠道.transOrderId={}", PayConstant.TRANS_STATUS_TRANING, transOrderId);
            return;
        }

        TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelName.toLowerCase() +  "TransService");
        QueryRetMsg retObj = transInterface.query(transOrder);
        // 查询成功
        if(retObj != null) {
            // 1-处理中 2-成功 3-失败
            if(QueryRetMsg.ChannelState.CONFIRM_SUCCESS.equals(retObj.getChannelState())) {
                String channelOrderNo = retObj.getChannelOrderId();
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows == 1) {
                    // 通知业务系统
                    baseNotify4MchTrans.doNotify(transOrder, true);
                    return;
                }
            }else if(QueryRetMsg.ChannelState.CONFIRM_FAIL.equals(retObj.getChannelState())) {
                String channelOrderNo = retObj.getChannelOrderId();
                String channelErrCode = retObj.getChannelErrCode();
                String channelErrMsg = retObj.getChannelErrMsg();
                int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg, channelOrderNo);
                _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                if (updateTransOrderRows == 1) {
                    // 通知业务系统
                    baseNotify4MchTrans.doNotify(transOrder, true);
                    return;
                }
            }
        }

        // 发送延迟消息,继续查询
        int cnt = count + 1;
        if(cnt <= 10) {
            // 通知频率为15/15/30/180/1800/1800/1800/1800/3600/36000
            // 通知失败，延时再通知
            msgObj.put("count", cnt);
            long delay = getNotifyDelay(cnt);
            _log.info("[转账查询]{}次: msg={}", cnt, msgObj);
            this.send(msgObj.toJSONString(), delay * 1000);
        }

    }


    /**
     * 获取延迟秒数
     * @param cnt
     * @return
     */
    long getNotifyDelay(int cnt) {
        switch (cnt) {
            case 1:
                return 30;
            case 2:
                return 60;
            case 3:
                return 120;
            case 4:
                return 180;
            case 5:
                return 1800;
            case 6:
                return 1800;
            case 7:
                return 1800;
            case 8:
                return 1800;
            case 9:
                return 3600;
            case 10:
                return 36000;
            default:
                return 180;
        }
    }

}
