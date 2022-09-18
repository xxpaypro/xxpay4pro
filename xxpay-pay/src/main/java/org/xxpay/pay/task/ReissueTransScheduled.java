package org.xxpay.pay.task;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.DateUtils;
import org.xxpay.core.common.util.IPUtility;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.TransInterface;
import org.xxpay.pay.mq.BaseNotify4MchTrans;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/09/09
 * @description: 转账补单任务
 */
@Component
public class ReissueTransScheduled extends ReissuceBase {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(ReissueTransScheduled.class);

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    /**
     * 转账订单批量补单任务
     */
    //@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void transReissueTask() {
        String logPrefix = "【转账补单】";
        // 代付补单开关
        if(!isExcuteReissueTask(reissueTransSwitch, reissueTransIP)) {
            _log.info("{}当前机器不执行补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        TransOrder queryTransOrder = new TransOrder();
        queryTransOrder.setStatus(PayConstant.TRANS_STATUS_TRANING);
        // 查询比当前时间小60秒,状态为处理中的订单
        Date createTimeEnd = new Date(System.currentTimeMillis() - 60 * 1000);
        boolean flag = true;
        _log.info("{}开始查询需要处理的转账订单,查询订单创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有
        while (flag) {
            List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((pageIndex - 1) * limit, limit, queryTransOrder, null, createTimeEnd);
            _log.info("{}查询需要处理的转账订单,共:{}条", logPrefix, transOrderList == null ? 0 : transOrderList.size());
            for(TransOrder transOrder : transOrderList) {
                long startTime = System.currentTimeMillis();
                String transOrderId = transOrder.getTransOrderId();
                _log.info("{}开始处理transOrderId={}", logPrefix, transOrderId);
                // 渠道类型
                String channelType = transOrder.getChannelType();
                QueryRetMsg retObj;
                try{
                    TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
                    retObj = transInterface.query(transOrder);
                }catch (BeansException e) {
                    _log.warn("{}不支持的转账渠道,停止处理.transOrderId={},channelType={}", logPrefix, transOrderId, channelType);
                    continue;
                }
                if(retObj == null) {
                    _log.warn("{}查询上游返回空,停止处理.transOrderId={},channelType={}", logPrefix, transOrderId, channelType);
                    continue;
                }
                // 查询成功
                if(retObj != null) {
                    // 1-处理中 2-成功 3-失败
                    if(QueryRetMsg.ChannelState.CONFIRM_SUCCESS.equals(retObj.getChannelState())) {
                        String channelOrderNo = retObj.getChannelOrderId();
                        int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                        _log.info("{}更新转账订单状态为成功({}),transOrderId={},返回结果:{}", logPrefix, PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                        if (updateTransOrderRows == 1) {
                            transOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
                            // 通知业务系统
                            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                            baseNotify4MchTrans.doNotify(transOrder, true);
                        }
                    }else if(QueryRetMsg.ChannelState.CONFIRM_FAIL.equals(retObj.getChannelState())) {
                        String channelOrderNo = retObj.getChannelOrderId();
                        String channelErrCode = retObj.getChannelErrCode();
                        String channelErrMsg = retObj.getChannelErrMsg();
                        int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg, channelOrderNo);
                        _log.info("{}更新转账订单状态为失败({}),transOrderId={},返回结果:{}", logPrefix, PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                        if (updateTransOrderRows == 1) {
                            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                            // 通知业务系统
                            baseNotify4MchTrans.doNotify(transOrder, true);
                        }
                    }
                }
                _log.info("{}处理完毕transOrderId={}.耗时:{} ms", logPrefix, transOrderId, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(transOrderList) || transOrderList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次查询处理完成,", logPrefix);
    }

    /**
     * 转账订单Notify补发通知
     */
    //@Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    /*@Scheduled(cron="0 20 0 ? * *")  // 每日零点
    public void transTask() {
        String logPrefix = "【转账订单补发通知】";
        int pageIndex = 1;
        int limit = 100;
        TransOrder queryTransOrder = new TransOrder();
        queryTransOrder.setStatus(PayConstant.TRANS_STATUS_COMPLETE);
        // 查询比当前时间小60秒,状态为处理中的订单
        Date createTimeEnd = new Date(System.currentTimeMillis() - 60 * 1000);
        Date createTimeStart = new Date(System.currentTimeMillis() - 2 *  24 * 60 * 60 * 1000);
        boolean flag = true;
        _log.info("{}开始查询需要处理的转账订单,查询订单创建时间<={},>={}", logPrefix, DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"), DateUtils.getTimeStr(createTimeStart, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有商户账户
        while (flag) {
            List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((pageIndex - 1) * limit, limit, queryTransOrder, null, createTimeEnd);
            _log.info("{}查询需要处理的转账订单,共:{}条", logPrefix, transOrderList == null ? 0 : transOrderList.size());
            for(TransOrder transOrder : transOrderList) {
                long startTime = System.currentTimeMillis();
                String transOrderId = transOrder.getTransOrderId();
                Byte status = transOrder.getStatus();
                if(status == PayConstant.TRANS_STATUS_SUCCESS || status == PayConstant.TRANS_STATUS_FAIL || status == PayConstant.TRANS_STATUS_COMPLETE) {
                    baseNotify4MchTrans.doNotify(transOrder, false);
                }
                _log.info("{}transOrderId={},处理完毕.耗时:{} ms", logPrefix, transOrderId, System.currentTimeMillis() - startTime);
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(transOrderList) || transOrderList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}处理完成,", logPrefix);
    }*/


}
