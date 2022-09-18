package org.xxpay.core.common.callback;

import org.xxpay.core.entity.MchTradeOrder;

/** 新增订单回调callback */
public interface AddTradeOrderCallBack {

    /** 入库前前的操作 **/
    void setRecordBeforeInsert(MchTradeOrder mchTradeOrder);
}
