package org.xxpay.pay.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.mq.BaseNotify4MchRefund;
import org.xxpay.pay.mq.Mq4RefundNotify;

/**
 * @author: dingzhiwei
 * @date: 17/10/30
 * @description:
 */
@Service
public class RefundOrderService {

    private static final MyLog _log = MyLog.getLog(RefundOrderService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private Mq4RefundNotify mq4RefundNotify;

    @Autowired
    private BaseNotify4MchRefund baseNotify4MchRefund;

    public void sendRefundNotify(String refundOrderId, String channelType) {
        JSONObject object = new JSONObject();
        object.put("refundOrderId", refundOrderId);
        object.put("channelType", channelType);
        mq4RefundNotify.send(object.toJSONString());
    }

    public JSONObject query(Long mchId, String refundOrderId, String mchRefundNo, String executeNotify) {
        RefundOrder refundOrder;
        if(StringUtils.isNotBlank(refundOrderId)) {
            refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndRefundOrderId(mchId, refundOrderId);

        }else {
            refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndMchRefundNo(mchId, mchRefundNo);
        }
        if(refundOrder == null) return null;
        boolean isNotify = Boolean.parseBoolean(executeNotify);
        if(isNotify) {
            baseNotify4MchRefund.doNotify(refundOrder, false);
            _log.info("业务查单完成,并再次发送业务退款通知.发送完成");

        }
        return (JSONObject) JSONObject.toJSON(refundOrder);
    }

}
