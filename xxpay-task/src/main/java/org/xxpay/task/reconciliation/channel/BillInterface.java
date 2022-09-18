package org.xxpay.task.reconciliation.channel;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.CheckBatch;

/**
 * @author: dingzhiwei
 * @date: 18/1/18
 * @description:
 */
public interface BillInterface {

    JSONObject downloadBill(JSONObject param, CheckBatch batch);

}
