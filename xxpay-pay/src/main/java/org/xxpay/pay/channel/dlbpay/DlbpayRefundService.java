package org.xxpay.pay.channel.dlbpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.BaseRefund;

@Service
public class DlbpayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(DlbpayRefundService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_DLBPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {

        String logPrefix = "【哆啦宝退款】";
        JSONObject retObj = buildRetObj();

        try {

            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(refundOrder.getPayOrderId());

            String payOrderId = payOrder.getPayOrderId();

            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("refundRequestNum", refundOrder.getRefundOrderId());//必须是 18--32 位的纯数字

            reqData.put("refundPartAmount", AmountUtil.convertCent2Dollar(refundOrder.getRefundAmount())); //金额， 单位元

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/order/refund/part", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                _log.info("{} >>> 退款操作成功", logPrefix);
                retObj.put("isSuccess", true);
                retObj.put("refundOrderId", refundOrder.getRefundOrderId());
                return retObj;


            }else{ //支付失败

                JSONObject errObject = respData.getJSONObject("error");

                retObj.put("isSuccess", false);
                retObj.put("channelErrCode", errObject.getString("errorCode"));
                retObj.put("channelErrMsg", errObject.getString("errorMsg"));
                return retObj;
            }

        } catch (Exception e) {
            _log.error("{}退款异常", logPrefix, e);
            retObj.put("isSuccess", false);
            retObj.put("channelErrCode", e.getMessage());
            retObj.put("channelErrMsg", e.getMessage());
            return retObj;
        }

    }

}
