package org.xxpay.pay.service.impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.core.service.IXxPayTransService;
import org.xxpay.pay.channel.TransInterface;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

/**
 * @author: dingzhiwei
 * @date: 2018/5/29
 * @description:
 */
@Service(interfaceName = "org.xxpay.core.service.IXxPayTransService", version = Constant.XXPAY_SERVICE_VERSION, retries = Constant.DUBBO_RETRIES)
public class XxPayTransServiceImpl implements IXxPayTransService {

    private static final MyLog _log = MyLog.getLog(XxPayTransServiceImpl.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Override
    public String executeTrans(TransOrder transOrder) {
        // 生成转账ID
        String transOrderId = MySeq.getTrans();
        transOrder.setTransOrderId(transOrderId);
        _log.info("调取rpc转账接口,transOrder={}", transOrder);
        int result = rpcCommonService.rpcTransOrderService.createTransOrder(transOrder);
        _log.info("创建转账订单,结果:{}", result);

        result = rpcCommonService.rpcTransOrderService.updateStatus4Ing(transOrderId, "");
        if(result != 1) {
            _log.info("更改转账为转账中({})失败,不能转账.transOrderId={}", PayConstant.TRANS_STATUS_TRANING, transOrderId);
            return transOrderId;
        }

        QueryRetMsg retObj = null;
        try{
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(transOrder.getChannelType() + "TransService");
            retObj = transInterface.trans(transOrder);
        }catch (BeansException e) {
            _log.warn("不支持的转账渠道,停止转账处理.transOrderId={},channelType={}", transOrder.getTransOrderId(), transOrder.getChannelType());
        }

        if(retObj != null) {
            // 判断业务结果
            if(QueryRetMsg.ChannelState.CONFIRM_SUCCESS.equals(retObj.getChannelState())) {
                // 更新转账状态为成功
                String channelOrderNo = retObj.getChannelOrderId();
                result = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, result);
            }else if(QueryRetMsg.ChannelState.CONFIRM_FAIL.equals(retObj.getChannelState())) {
                // 更新转账状态为失败
                String channelErrCode = retObj.getChannelErrCode();
                String channelErrMsg = retObj.getChannelErrMsg();
                result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
                _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
            }
        }else {
            // 更新转账状态为失败
            String channelErrCode = null;
            String channelErrMsg = null;
            result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
            _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
        }
        return transOrderId;
    }

    @Override
    public AgentPayBalanceRetMsg queryBalance(String channelType, String payParam) {
        try{
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            return transInterface.balance(payParam);
        }catch (BeansException e) {
            _log.warn("不支持的查询余额渠道.channelType={}", channelType);
            return AgentPayBalanceRetMsg.fail();
        }
    }

    @Override
    public QueryRetMsg queryTrans(String transOrderId) {
        String channelType= "";
        try{
            TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            if(transOrder == null) return null;
            channelType = transOrder.getChannelType();
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            return transInterface.query(transOrder);
        }catch (BeansException e) {
            _log.warn("不支持的代付查询渠道.channelType={}", channelType);
        }
        return null;
    }

}
