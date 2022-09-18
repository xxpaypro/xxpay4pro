package org.xxpay.pay.channel.suixingpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.BaseRefund;
import org.xxpay.pay.channel.suixingpay.utils.SuixingpayUtil;
import org.xxpay.pay.channel.wxpay.WxPayUtil;

import java.util.Map;

@Service
public class SuixingpayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(SuixingpayRefundService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SUIXINGPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {

        String logPrefix = "【随行付退款】";
        JSONObject retObj = buildRetObj();

        try {

            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(refundOrder.getPayOrderId());

            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("ordNo", refundOrder.getRefundOrderId()); //订单号
            reqData.put("mno", mainParam.getString("mno")); //商户编号
            reqData.put("origOrderNo", payOrder.getPayOrderId()); //商户订单号
            reqData.put("amt", AmountUtil.convertCent2Dollar(refundOrder.getRefundAmount()));
            reqData.put("notifyUrl", "");
            reqData.put("refundReason", refundOrder.getRemarkInfo());

            //请求接口
            JSONObject resData = SuixingpayUtil.req("/order/refund", payOrder, mainParam, reqData);

            //退款操作成功   2002退款中也定做退款成功
            if("0000".equals(resData.getString("bizCode")) || "2002".equals(resData.getString("bizCode"))){
                _log.info("{} >>> 退款操作成功", logPrefix);
                retObj.put("isSuccess", true);
                retObj.put("refundOrderId", refundOrder.getRefundOrderId());
//                retObj.put("channelOrderNo", result.getRefundId());
                return retObj;
            }

        } catch (Exception e) {

            _log.error("{}退款异常", logPrefix, e);
            retObj.put("isSuccess", false);
            retObj.put("channelErrCode", e.getMessage());
            retObj.put("channelErrMsg", e.getMessage());
        }

        return retObj;
    }

    public JSONObject query(RefundOrder refundOrder) {

        String logPrefix = "【随行付退款查询】";
        JSONObject retObj = buildRetObj();
        try{

            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(refundOrder.getPayOrderId());

            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("ordNo", refundOrder.getRefundOrderId()); //订单号
            reqData.put("mno", mainParam.getString("mno")); //商户编号

            //请求接口
            JSONObject resData = SuixingpayUtil.req("/query/refundQuery", payOrder, mainParam, reqData);

            //退款状态：
            //REFUNDSUC 退款成功， REFUNDFAIL退款失败， REFUNDING 退款中
            String tranSts = resData.getString("tranSts");

            if("REFUNDSUC".equals(tranSts)){ //退款成功
                _log.info("{} >>> 成功", logPrefix);
                retObj.put("isSuccess", true);
                return retObj;

            }else if("REFUNDFAIL".equals(tranSts)){ //退款失败
                retObj.put("channelErrCode", resData.getString("bizCode"));
                retObj.put("channelErrMsg", resData.getString("bizMsg"));
                retObj.put("isSuccess", false);
                return retObj;
            }

            return retObj;
        }catch (Exception e) {

            _log.error(e, "随行付退款查询异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

}
