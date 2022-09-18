package org.xxpay.pay.task;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.DateUtils;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.PaymentInterface;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/10/06
 * @description: 支付补单任务(待完善)
 */
@Component
public class ReissuePayScheduled extends ReissuceBase {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(ReissuePayScheduled.class);

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    /**
     * 支付订单批量补单任务
     */
    //@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void payReissueTask() {
        String logPrefix = "【支付补单】";
        // 支付补单开关
        if(!isExcuteReissueTask(reissuePayTaskSwitch, reissuePayTaskIp)) {
            _log.info("{}当前机器不执行支付补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        PayOrder queryPayOrder = new PayOrder();
        queryPayOrder.setStatus(PayConstant.PAY_STATUS_PAYING);   // 支付中
        // 查询比当前时间小10分钟,状态为处理中的订单
        Date createTimeEnd = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
        boolean flag = true;
        _log.info("{}开始查询需要处理的支付订单,查询订单创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有
        while (flag) {
            List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select((pageIndex - 1) * limit, limit, queryPayOrder, null, createTimeEnd);
            _log.info("{}查询需要处理的支付订单,共:{}条", logPrefix, payOrderList == null ? 0 : payOrderList.size());
            for(PayOrder payOrder : payOrderList) {
                String payOrderId = payOrder.getPayOrderId();
                _log.info("{}开始处理payOrderId={}", logPrefix, payOrderId);

                Date expireDate = payOrder.getExpireTime() ;
                if(expireDate != null && expireDate.before(new Date())){ //如果过期时间 在 当前时间 之前

                    rpcCommonService.rpcPayOrderService.updateStatus4Expired(payOrderId);
                    _log.info("{}payOrderId={}, 订单超时", logPrefix, payOrderId);
                    continue;
                }

                long startTime = System.currentTimeMillis();
                // 渠道类型
                String channelType = payOrder.getChannelType();
                QueryRetMsg retObj;
                try{
                    PaymentInterface paymentInterface = (PaymentInterface) SpringUtil.getBean(channelType + "PaymentService");
                    retObj = paymentInterface.query(payOrder);
                }catch (BeansException e) {
                    _log.warn("{}不支持的支付渠道,停止处理.payOrderId={},channelType={}", logPrefix, payOrderId, channelType);
                    continue;
                }catch (Exception e) {
                    _log.warn("{}查询上游订单异常", logPrefix, payOrderId);
                    _log.error(e, "");
                    continue;
                }
                if(retObj == null) {
                    _log.warn("{}查询上游返回空,停止处理.payOrderId={},channelType={}", logPrefix, payOrderId, channelType);
                    continue;
                }
                // 查询成功
                if(retObj.getChannelState() == QueryRetMsg.ChannelState.CONFIRM_SUCCESS) {
                    String channelOrderNo = retObj.getChannelOrderId();
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrderId, channelOrderNo);
                    _log.info("{}更新支付订单状态为成功({}),payOrderId={},返回结果:{}", logPrefix, PayConstant.PAY_STATUS_SUCCESS, payOrderId, updatePayOrderRows);
                    if (updatePayOrderRows == 1) {
                        payOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
                        // 通知业务系统
                        payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
                        baseNotify4MchPay.doNotify(payOrder, true);
                    }
                }else if(retObj.getChannelState() == QueryRetMsg.ChannelState.CONFIRM_FAIL){  //确认失败

                    //1. 更新支付订单表为失败状态
                    rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);

                    //2. 更新mchTradeOrder为失败状态 (如果在商户中心写补单任务任务, 也是通过接口查询payOrder表的逻辑)
                    rpcCommonService.rpcMchTradeOrderService.updateStatus4Fail(payOrder.getMchOrderNo());

                }
                _log.info("{}处理完毕payOrderId={}.耗时:{} ms", logPrefix, payOrderId, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(payOrderList) || payOrderList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次查询处理完成,", logPrefix);
    }

}
