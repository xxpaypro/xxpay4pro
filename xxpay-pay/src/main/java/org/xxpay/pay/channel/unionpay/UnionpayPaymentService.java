package org.xxpay.pay.channel.unionpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.unionpay.utils.UniHttpUtils;
import org.xxpay.pay.channel.unionpay.utils.UniResultUtils;
import org.xxpay.pay.channel.unionpay.utils.UniSignUtils;

import java.io.File;

/** 银联支付 **/
@Service
public class UnionpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(UnionpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_UNIONPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;
        switch (channelId) {
            case PayConstant.PAY_CHANNEL_UNIONPAY_BAR :
                retObj = bar(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_UNIONPAY_JSAPI :
                retObj = jsapi(payOrder);
                break;
            default:
                retObj = ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    @Override
    public QueryRetMsg query(PayOrder payOrder) {

        String logPrefix = "【云闪付条码支付】";
        String payOrderId = payOrder.getPayOrderId();

        try {

            _log.info("{}开始查询通道订单,payOrderId={}", logPrefix, payOrderId);
            JSONObject isvParam = getMainPayParam(payOrder);  //主调起方的支付配置参数

            JSONObject reqParams = new JSONObject();
            reqParams.put("version", "5.1.0"); //版本号 固定值 5.1.0
            reqParams.put("encoding", "UTF-8"); //编码方式 默认取值：UTF-8
            reqParams.put("bizType", "000000"); //产品类型  填写000000
            reqParams.put("txnType", "00"); //交易类型 00
            reqParams.put("txnSubType", "00"); //交易子类 流水号查询：00
            reqParams.put("accessType", "0"); //接入类型 商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
            reqParams.put("signMethod", "01"); //签名方法 非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
            reqParams.put("merId", isvParam.getString("merId")); //商户代码
            reqParams.put("txnTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS)); //订单发送时间 YYYYMMDDhhmmss
            reqParams.put("orderId", payOrder.getPayOrderId()); //商户订单号

            String mchPrivateCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("mchPrivateCertFile");
            String mchPrivateCertPwd = isvParam.getString("mchPrivateCertPwd");

            //签名 & 填充字段
            UniSignUtils.signAndFillField(reqParams, mchPrivateCertFile, mchPrivateCertPwd);

            _log.info("{}reqJSON={}", logPrefix, reqParams);
            String resText = UniHttpUtils.post(reqParams, XXPayUtil.getUnionpayHost4env(isvParam) + "/gateway/api/queryTrans.do");
            if(StringUtils.isEmpty(resText)){
                _log.error("{}请求返回数据为空！", logPrefix);
                return QueryRetMsg.waiting(); //支付中
            }

            JSONObject resJSON = UniResultUtils.convertResultStringToJSON(resText);
            _log.info("{} resJSON={}:", logPrefix, resJSON);

            //验签
            String unionpayRootCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayRootCertFile");
            String unionpayMiddleCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayMiddleCertFile");

            //验签失败
            if(!UniSignUtils.validate(resJSON, unionpayRootCertFile, unionpayMiddleCertFile)){

                _log.info("{}验签失败！");  //做失败处理
                return QueryRetMsg.waiting(); //支付中
            }

            //请求 & 响应成功， 判断业务逻辑
            String respCode = resJSON.getString("respCode"); //应答码
            String respMsg = resJSON.getString("respMsg"); //应答信息
            String origRespCode = resJSON.getString("origRespCode"); //原始应答信息
            String queryId = resJSON.getString("queryId"); //上游订单号


            if(("00").equals(respCode)){//如果查询交易成功

                if(("00").equals(origRespCode)){

                    //交易成功，更新商户订单状态
                    return QueryRetMsg.confirmSuccess(queryId);  //支付成功

                }else if(("03").equals(origRespCode)|| ("04").equals(origRespCode)|| ("05").equals(origRespCode)){

                    //订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】

                    return QueryRetMsg.waiting(); //支付中

                }else{

                    //其他应答码为交易失败
                    return QueryRetMsg.confirmFail(queryId); //失败

                }

            }else if(("34").equals(respCode)){

                //订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
                return QueryRetMsg.waiting(); //支付中


            }else{//查询交易本身失败，如应答码10/11检查查询报文是否正确

                _log.error("{} 应答码={} [{}], 原始响应码为={}", logPrefix, respCode, respMsg, origRespCode);
                return QueryRetMsg.waiting(); //支付中
            }

        }catch (Exception e) {
            _log.error("{}查询异常", logPrefix, e);
            return QueryRetMsg.waiting(); //支付中
        }
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {
        return null;
    }

    /** 二维码支付（被扫） **/
    private AbstractRes bar(PayOrder payOrder){

        String logPrefix = "【云闪付条码支付】";
        String payOrderId = payOrder.getPayOrderId();

        try {

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
            JSONObject isvParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            JSONObject reqParams = new JSONObject();
            reqParams.put("merId", isvParam.getString("merId")); //商户代码
            reqParams.put("orderId", payOrder.getPayOrderId()); //商户订单号
            reqParams.put("backUrl", payConfig.getNotifyUrl(getChannelName())); //后台通知地址
            reqParams.put("qrNo", payOrder.getExtra()); //C2B码,1-20位数字
            reqParams.put("txnAmt", payOrder.getAmount()); //交易金额 单位为分
            reqParams.put("version", "5.1.0"); //版本号 固定值 5.1.0
            reqParams.put("encoding", "UTF-8"); //编码方式 默认取值：UTF-8
            reqParams.put("bizType", "000000"); //产品类型  填写000000
            reqParams.put("txnTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS)); //订单发送时间 YYYYMMDDhhmmss
            reqParams.put("currencyCode", "156"); //交易币种 156 人民币
            reqParams.put("txnType", "01"); //交易类型 01:消费
            reqParams.put("txnSubType", "06"); //交易子类 06：二维码消费
            reqParams.put("accessType", "0"); //接入类型 商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
            reqParams.put("signMethod", "01"); //签名方法 非对称签名： 01（表示采用RSA签名） HASH表示散列算法 11：支持散列方式验证SHA-256 12：支持散列方式验证SM3
            reqParams.put("channelType", "07"); //渠道类型 08手机  07为测试案例提供值

            String mchPrivateCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("mchPrivateCertFile");
            String mchPrivateCertPwd = isvParam.getString("mchPrivateCertPwd");

            //签名 & 填充字段
            UniSignUtils.signAndFillField(reqParams, mchPrivateCertFile, mchPrivateCertPwd);

            _log.info("{}reqJSON={}", logPrefix, reqParams);
            String resText = UniHttpUtils.post(reqParams, XXPayUtil.getUnionpayHost4env(isvParam) + "/gateway/api/backTransReq.do");
            if(StringUtils.isEmpty(resText)){
                return ApiBuilder.bizError("下单失败[http请求失败]");
            }

            JSONObject resJSON = UniResultUtils.convertResultStringToJSON(resText);
            _log.info("{} resJSON={}:", logPrefix, resJSON);

            //http Post 请求
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, resJSON.getString("queryId"));  //更新订单为支付中

            //验签
            String unionpayRootCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayRootCertFile");
            String unionpayMiddleCertFile = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("unionpayMiddleCertFile");

            //验签失败
            if(!UniSignUtils.validate(resJSON, unionpayRootCertFile, unionpayMiddleCertFile)){

                _log.info("{}验签失败！");  //做失败处理
                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);  //更新为订单失败
                return ApiBuilder.bizError("下单失败[验签失败]");
            }

            //请求 & 响应成功， 判断业务逻辑
            String respCode = resJSON.getString("respCode"); //应答码
            String respMsg = resJSON.getString("respMsg"); //应答信息

            //00-交易已受理成功， 03-通讯超时或状态不明 , 12-交易重复， 需要发起查询处理
            if("00".equals(respCode) || "03".equals(respCode) ||"12".equals(respCode)){
                //做处理中处理， 等待异步回调 | 补单操作
                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING);
            }else{
                //做失败处理
                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);  //更新为订单失败
                return ApiBuilder.bizError("下单失败[" + respCode + "|" +respMsg + "]");
            }

            _log.info("{}生成银联数据,req={}", logPrefix, reqParams);
            _log.info("###### 商户统一下单处理完成 ######");

            retObj.setPayOrderId(payOrderId);
            return retObj;

        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
    }


    /** 主扫支付模式 **/
    private AbstractRes jsapi(PayOrder payOrder){

        return null;
    }





}
