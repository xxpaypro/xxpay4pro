package org.xxpay.pay.channel.unionpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.BaseRefund;
import org.xxpay.pay.channel.unionpay.utils.UniHttpUtils;
import org.xxpay.pay.channel.unionpay.utils.UniResultUtils;
import org.xxpay.pay.channel.unionpay.utils.UniSignUtils;

import java.io.File;

@Service
public class UnionpayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(UnionpayRefundService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_UNIONPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {
        String logPrefix = "【云闪付退款】";
        String refundOrderId = refundOrder.getRefundOrderId();

        try {

            JSONObject retObj = buildRetObj();
            retObj.put("refundOrderId", refundOrderId);
            retObj.put("isSuccess", false);

            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(refundOrder.getPayOrderId());

            JSONObject isvParam = getMainPayParam(payOrder);  //主调起方的支付配置参数

            JSONObject reqParams = new JSONObject();
            reqParams.put("version", "5.1.0"); //版本号 固定值 5.1.0
            reqParams.put("bizType", "000000"); //产品类型  填写000000
            reqParams.put("txnTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS)); //订单发送时间 YYYYMMDDhhmmss
            reqParams.put("backUrl", "http://www.specialUrl.com"); //异步通知
            reqParams.put("txnAmt", refundOrder.getRefundAmount()); //退款金额
            reqParams.put("txnType", "04"); //交易类型 04
            reqParams.put("txnSubType", "00"); //交易子类 00
            reqParams.put("accessType", "0"); //接入类型 商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
            reqParams.put("signMethod", "01"); //签名方法 非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
            reqParams.put("channelType", "07"); //渠道类型 08手机  07为测试案例提供值
            reqParams.put("merId", isvParam.getString("merId")); //商户代码
            reqParams.put("orderId", refundOrder.getRefundOrderId()); //商户订单号
            reqParams.put("origQryId", payOrder.getChannelOrderNo()); //原始消费交易的queryId
            reqParams.put("encoding", "UTF-8"); //编码方式 默认取值：UTF-8

            String mchPrivateCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("mchPrivateCertFile");
            String mchPrivateCertPwd = isvParam.getString("mchPrivateCertPwd");

            //签名 & 填充字段
            UniSignUtils.signAndFillField(reqParams, mchPrivateCertFile, mchPrivateCertPwd);

            _log.info("{}reqJSON={}", logPrefix, reqParams);
            String resText = UniHttpUtils.post(reqParams, XXPayUtil.getUnionpayHost4env(isvParam) + "/gateway/api/backTransReq.do");
            if(StringUtils.isEmpty(resText)){
                _log.error("{}请求返回数据为空！", logPrefix);
            }

            JSONObject resJSON = UniResultUtils.convertResultStringToJSON(resText);
            _log.info("{} resJSON={}:", logPrefix, resJSON);

            //验签
            String unionpayRootCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayRootCertFile");
            String unionpayMiddleCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayMiddleCertFile");

            //验签失败
            if(!UniSignUtils.validate(resJSON, unionpayRootCertFile, unionpayMiddleCertFile)){

                _log.info("{}验签失败！");  //做失败处理
                retObj.put("isSuccess", false);
                retObj.put("channelErrCode", "");
                retObj.put("channelErrMsg", "验签失败！");
                return retObj;
            }

            //请求 & 响应成功， 判断业务逻辑
            String respCode = resJSON.getString("respCode"); //应答码
            String respMsg = resJSON.getString("respMsg"); //应答信息
            String origRespCode = resJSON.getString("origRespCode"); //原始应答信息
            String queryId = resJSON.getString("queryId"); //上游订单号

            if(("00").equals(respCode)){
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", queryId);
                return retObj;

            }else if(("03").equals(respCode)|| ("04").equals(respCode)|| ("05").equals(respCode)){

                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", queryId);
                return retObj;

            }else{
                //其他应答码为失败请排查原因
                _log.info("{}返回失败", logPrefix);
                _log.info("respCode:{},respMsg:{}", respCode, respMsg);
                retObj.put("channelErrCode", respCode);
                retObj.put("channelErrMsg", respMsg);
                return retObj;
            }

        } catch (Exception e) {
            _log.error(e, "");
            return buildFailRetObj();
        }
    }

}
