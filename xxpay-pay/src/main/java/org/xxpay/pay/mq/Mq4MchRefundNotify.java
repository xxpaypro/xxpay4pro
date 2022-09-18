package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xxpay.core.common.util.MyLog;

import javax.jms.Queue;

/**
 * @Description: 商户通知MQ统一处理
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-10-31
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Mq4MchRefundNotify extends Mq4MchNotify {

    @Autowired
    private Queue mchRefundNotifyQueue;

    private static final MyLog _log = MyLog.getLog(Mq4MchRefundNotify.class);

    public void send(String msg) {
        super.send(mchRefundNotifyQueue, msg);
    }

    @JmsListener(destination = MqConfig.MCH_REFUND_NOTIFY_QUEUE_NAME)
    @Async("mqExecutor")
    public void receive(String msg) {
        String logPrefix = "【商户退款通知】";
        _log.info("{}接收消息:msg={}", logPrefix, msg);
        JSONObject msgObj = JSON.parseObject(msg);
        String respUrl = msgObj.getString("url");
        String orderId = msgObj.getString("orderId");
        int count = msgObj.getInteger("count");
        if(StringUtils.isEmpty(respUrl)) {
            _log.warn("{}商户通知URL为空,respUrl={}", logPrefix, respUrl);
            return;
        }
        String httpResult = "";
        try{
            httpResult = httpPost(respUrl);
        }catch (Exception e) {
            _log.error(e, "发起通知请求异常");
        }
        int cnt = count + 1;
        _log.info("{}notifyCount={}", logPrefix, cnt);
        if("success".equalsIgnoreCase(httpResult)){

            // 修改通知
            try {
                int result = rpcCommonService.rpcMchNotifyService.updateMchNotifySuccess(orderId, httpResult, (byte) cnt);
                _log.info("{}修改商户通知,orderId={},result={},notifyCount={},结果:{}", logPrefix, orderId, httpResult, cnt, result == 1 ? "成功" : "失败");
            }catch (Exception e) {
                _log.error(e, "修改商户支付通知异常");
            }
            return ; // 通知成功结束
        }else {
            // 修改通知次数
            try {
                int result = rpcCommonService.rpcMchNotifyService.updateMchNotifyFail(orderId, httpResult, (byte) cnt);
                _log.info("{}修改商户通知,orderId={},result={},notifyCount={},结果:{}", logPrefix, orderId, httpResult, cnt, result == 1 ? "成功" : "失败");
            }catch (Exception e) {
                _log.error(e, "修改商户支付通知异常");
            }
            if (cnt > 5) {
                _log.info("{}orderId={},通知次数notifyCount({})>5,停止通知", logPrefix, orderId, cnt);
                return ;
            }
            // 通知失败，延时再通知
            msgObj.put("count", cnt);
            this.send(mchRefundNotifyQueue, msgObj.toJSONString(), cnt * 60 * 1000);
            _log.info("{}orderId={},发送延时通知完成,通知次数:{},{}秒后执行通知", logPrefix, orderId, cnt, cnt * 60);
        }
    }
}
