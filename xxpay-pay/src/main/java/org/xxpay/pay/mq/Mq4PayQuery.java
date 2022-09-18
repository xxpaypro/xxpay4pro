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
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.PaymentInterface;
import org.xxpay.pay.channel.suixingpay.SuixingpayApiService;
import org.xxpay.pay.channel.wxpay.WxpayApiService;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.jms.*;

/**
 * @Description: 支付渠道订单查询MQ
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-3-6
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Mq4PayQuery {

    @Autowired
    private Queue payQueryQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private WxpayApiService wxpayApiService;

    @Autowired
    private SuixingpayApiService suixingpayApiService;

    private static final MyLog _log = MyLog.getLog(Mq4PayQuery.class);

    public void send(String msg) {
        _log.info("发送MQ消息:msg={}", msg);
        this.jmsTemplate.convertAndSend(this.payQueryQueue, msg);
    }

    /**
     * 发送延迟消息
     * @param msg
     * @param delay
     */
    public void send(String msg, long delay) {
        _log.info("发送MQ延时消息:msg={},delay={}", msg, delay);
        jmsTemplate.send(this.payQueryQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(msg);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 1*1000);
                tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 1);
                return tm;
            }
        });
    }

    @JmsListener(destination = MqConfig.PAY_QUERY_QUEUE_NAME)
    public void receive(String msg) {
        _log.info("处理支付订单查询任务.msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        int count = msgObj.getIntValue("count");
        String payOrderId = msgObj.getString("payOrderId");
        String channelName = msgObj.getString("channelName");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if(payOrder == null) {
            _log.warn("查询支付订单为空,payOrderId={}", payOrderId);
            return;
        }
        if(payOrder.getStatus() != PayConstant.PAY_STATUS_PAYING) {
            _log.warn("订单状态不是支付中({}),不需查询渠道.payOrderId={}", PayConstant.PAY_STATUS_PAYING, payOrderId);
            return;
        }

        PaymentInterface paymentInterface = (PaymentInterface) SpringUtil.getBean(channelName.toLowerCase() +  "PaymentService");

        QueryRetMsg retObj = paymentInterface.query(payOrder);

        // 订单为成功
        if(retObj != null && retObj.getChannelState() == QueryRetMsg.ChannelState.CONFIRM_SUCCESS ) { //明确成功
            String channelOrderId = retObj.getChannelOrderId();
            String channelAttach = retObj.getChannelAttach();
            int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), channelOrderId,channelAttach);
            _log.error("将payOrderId={}订单状态更新为支付成功,result={}", payOrderId, updatePayOrderRows);
            if (updatePayOrderRows == 1) {
                // 通知业务系统
                baseNotify4MchPay.doNotify(payOrder, true);
                return;
            }
        }

        if(count >= 6){ //第6次 已经到了30s， 如果还没有支付成功，需要调用撤销接口


            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(payOrder.getMchId());
            String mainPayParam = payOrderService.getMainPayParam(mchInfo, PayConstant.PAY_PRODUCT_WX_BAR);
            String subPayParam = payOrderService.getSubPayParam(mchInfo, PayConstant.PAY_PRODUCT_WX_BAR);

            boolean isSuccess = false;

            if(PayConstant.PAY_CHANNEL_WX_BAR.equals(payOrder.getChannelId())){ //微信条码支付

                isSuccess = wxpayApiService.reverse(mainPayParam, subPayParam, payOrderId);

            }else if(PayConstant.PAY_CHANNEL_SUIXINGPAY_WX_BAR.equals(payOrder.getChannelId())){ //随行付微信条码支付

                isSuccess = suixingpayApiService.reverse(mainPayParam, subPayParam, payOrderId);
            }


            if(isSuccess){ //更改订单状态为 失败！

                //1. 更新支付订单表为失败状态
                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);

                //2. 更新mchTradeOrder为失败状态 (如果在商户中心写补单任务任务, 也是通过接口查询payOrder表的逻辑)
                rpcCommonService.rpcMchTradeOrderService.updateStatus4Fail(payOrder.getMchOrderNo());

                return ;
            }
        }

        //当count不满足撤销接口时， 当撤销接口调用失败时， 重复上述操作。
        count++;
        if(count < 15){  //最多重复15次
            msgObj.put("count", count);
            send(msgObj.toJSONString(), 5 * 1000);   // 延迟5秒查询
        }
    }
}
