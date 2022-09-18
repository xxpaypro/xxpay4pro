package org.xxpay.pay.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.BaseRefund;

import java.io.File;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/25
 * @description:
 */
@Service
public class AlipayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(AlipayRefundService.class);

    @Autowired
    private AlipayApiService alipayApiService;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {
        String logPrefix = "【支付宝退款】";
        String refundOrderId = refundOrder.getRefundOrderId();

        try {

            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(refundOrder.getPayOrderId());

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            if(subMchParam != null) {

                Long expireTime = subMchParam.getLong("expireTimestamp"); //过期日期（13位时间戳）
                if(expireTime != null && StringUtils.isNotBlank(subMchParam.getString("refreshToken")) &&
                        expireTime - System.currentTimeMillis()  < (30 * 24 * 3600 * 1000) ){ //默认30天前更新appAuthToken

                    //查询最新token并更新到数据库中
                    subMchParam = alipayApiService.getOrRefreshToken(alipayConfig, null, subMchParam.getString("refreshToken"));
                    PayPassage updateRecord = new PayPassage();
                    updateRecord.setId(payOrder.getMchPassageId());
                    updateRecord.setMchParam(subMchParam.toJSONString());
                    rpcCommonService.rpcPayPassageService.updateById(updateRecord);
                }
                request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }


            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(refundOrder.getPayOrderId());
            model.setTradeNo(refundOrder.getChannelPayOrderNo());
            model.setOutRequestNo(refundOrderId);
            model.setRefundAmount(AmountUtil.convertCent2Dollar(refundOrder.getRefundAmount().toString()));
            model.setRefundReason("正常退款");
            request.setBizModel(model);
            JSONObject retObj = buildRetObj();
            retObj.put("refundOrderId", refundOrderId);
            retObj.put("isSuccess", false);

            AlipayTradeRefundResponse response = alipayExecute(alipayConfig, request);
            if(response.isSuccess()){
                retObj.put("isSuccess", true);
                retObj.put("channelOrderNo", response.getTradeNo());
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
            }

            return retObj;
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return buildFailRetObj();
        }
    }

    public JSONObject query(RefundOrder refundOrder) {
        String logPrefix = "【支付宝退款查询】";
        String refundOrderId = refundOrder.getRefundOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getRefundParam(refundOrder));
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(refundOrder.getPayOrderId());
        model.setTradeNo(refundOrder.getChannelPayOrderNo());
        model.setOutRequestNo(refundOrderId);
        request.setBizModel(model);
        JSONObject retObj = buildRetObj();
        retObj.put("refundOrderId", refundOrderId);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayExecute(alipayConfig, request);
            if(response.isSuccess()){
                retObj.putAll((Map) JSON.toJSON(response));
                retObj.put("isSuccess", true);
            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj.put("channelErrCode", response.getSubCode());
                retObj.put("channelErrMsg", response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            retObj = buildFailRetObj();
        }
        return retObj;
    }

    /** 获取支付宝接口调用类 **/
    private DefaultAlipayClient getAlipayClient(AlipayConfig alipayConfig) throws AlipayApiException {

        String serverUrl = XXPayUtil.getAlipayUrl4env(1, alipayConfig.getConvertJSON());

        if("cert".equals(alipayConfig.getEncryptionType())){ //证书加密方式
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl(serverUrl);
            certAlipayRequest.setAppId(alipayConfig.getAppId());
            certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
            certAlipayRequest.setFormat(AlipayConfig.FORMAT);
            certAlipayRequest.setCharset(AlipayConfig.CHARSET);
            certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);

            certAlipayRequest.setCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAppPublicCert());
            certAlipayRequest.setAlipayPublicCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAlipayPublicCert());
            certAlipayRequest.setRootCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAlipayRootCert());
            return new DefaultAlipayClient(certAlipayRequest);

        }else{ //key 或者 空都为默认普通加密方式

            return new DefaultAlipayClient(XXPayUtil.getAlipayUrl4env(1, alipayConfig.getConvertJSON()), alipayConfig.getAppId(),
                    alipayConfig.getPrivateKey(), AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                    alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
        }
    }

    /** 封装支付宝接口调用函数 **/
    private <T extends AlipayResponse> T alipayExecute(AlipayConfig alipayConfig, AlipayRequest<T> request) throws AlipayApiException {

        if("cert".equals(alipayConfig.getEncryptionType())){ //证书加密方式
            return getAlipayClient(alipayConfig).certificateExecute(request);

        }else{ //key 或者 空都为默认普通加密方式
            return getAlipayClient(alipayConfig).execute(request);
        }
    }


}
