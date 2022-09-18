package org.xxpay.pay.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.*;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.BaseTrans;

import java.io.File;

/**
 * @author: dingzhiwei
 * @date: 17/12/25
 * @description:
 */
@Service
public class AlipayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(AlipayTransService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    public QueryRetMsg trans(TransOrder transOrder) {
        String logPrefix = "【支付宝转账】";
        String transOrderId = transOrder.getTransOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(transOrderId);
        model.setPayeeType("ALIPAY_LOGONID");                            // 收款方账户类型
        model.setPayeeAccount(transOrder.getChannelUser());              // 收款方账户
        model.setAmount(AmountUtil.convertCent2Dollar(transOrder.getAmount().toString()));
        model.setPayerShowName("支付转账");
        model.setPayeeRealName(transOrder.getAccountName());
        model.setRemark(transOrder.getRemarkInfo());
        request.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse response = alipayExecute(alipayConfig, request);
            if(response.isSuccess()) {
                return QueryRetMsg.confirmSuccess(response.getOrderId());

            }else {
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                return QueryRetMsg.confirmFail(response.getOrderId(), response.getSubCode(), response.getSubMsg());

            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return QueryRetMsg.sysError();
        }
    }

    public QueryRetMsg query(TransOrder transOrder) {
        QueryRetMsg retObj ;
        String logPrefix = "【支付宝转账查询】";
        String transOrderId = transOrder.getTransOrderId();
        AlipayConfig alipayConfig = new AlipayConfig(getTransParam(transOrder));
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
        model.setOutBizNo(transOrderId);
        model.setOrderId(transOrder.getChannelOrderNo());
        request.setBizModel(model);
        try {
            AlipayFundTransOrderQueryResponse response = alipayExecute(alipayConfig, request);
            if(response.isSuccess()){
                retObj = QueryRetMsg.confirmSuccess(response.getOrderId());
                retObj.setChannelOriginResponse(JSON.toJSON(response).toString());
                return retObj;

            }else {
                _log.info("{}返回失败", logPrefix);
                _log.info("sub_code:{},sub_msg:{}", response.getSubCode(), response.getSubMsg());
                retObj = QueryRetMsg.confirmFail(response.getOrderId(), response.getSubCode(), response.getSubMsg());
                retObj.setChannelOriginResponse(JSON.toJSON(response).toString());
                return retObj;

            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AgentPayBalanceRetMsg balance(String payParam) {
        return AgentPayBalanceRetMsg.unsupported();
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
