package org.xxpay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.PayOrder;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
public interface PayNotifyInterface {

    JSONObject doNotify(Object notifyData);

    JSONObject doReturn(Object notifyData);

}
