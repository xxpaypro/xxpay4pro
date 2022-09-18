package org.xxpay.pay.channel;

import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.entity.PayOrder;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
public interface PaymentInterface {

    AbstractRes pay(PayOrder payOrder);

    QueryRetMsg query(PayOrder payOrder);

    AbstractRes close(PayOrder payOrder);

}
