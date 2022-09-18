package org.xxpay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.core.entity.TransOrder;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description: 退款接口
 */
public interface RefundInterface {

    /**
     * 申请退款
     * @param refundOrder
     * @return
     */
    JSONObject refund(RefundOrder refundOrder);

    /**
     * 查询退款
     * @param refundOrder
     * @return
     */
    JSONObject query(RefundOrder refundOrder);

}
