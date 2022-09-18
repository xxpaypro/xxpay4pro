package org.xxpay.core.service;

import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.entity.TransOrder;

/**
 * @author: dingzhiwei
 * @date: 2018/5/29
 * @description:
 */
public interface IXxPayTransService {

    /**
     * 发起转账
     * @param transOrder
     * @return
     */
    String executeTrans(TransOrder transOrder);

    /**
     * 查询余额
     * @param channelType
     * @param payParam
     * @return
     */
    AgentPayBalanceRetMsg queryBalance(String channelType, String payParam);

    /**
     * 查询余额
     * @param transOrderId
     * @return
     */
    QueryRetMsg queryTrans(String transOrderId);

}
