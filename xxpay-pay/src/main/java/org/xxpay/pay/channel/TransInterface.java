package org.xxpay.pay.channel;

import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.entity.TransOrder;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
public interface TransInterface {

    /**
     * 发起转账(代付)
     * @param transOrder
     * @return
     */
    QueryRetMsg trans(TransOrder transOrder);

    /**
     * 查询结果
     * @param transOrder
     * @return
     */
    QueryRetMsg query(TransOrder transOrder);

    /**
     * 查询账户余额
     * @param payParam
     * @return
     */
    AgentPayBalanceRetMsg balance(String payParam);

}
