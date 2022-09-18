package org.xxpay.pay.channel.sandpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.sandpay.demo.gateway.DemoBase;
import org.xxpay.pay.mq.BaseNotify4MchPay;

import java.io.File;

/**
 * @author: dingzhiwei
 * @date: 18/8/24
 * @description: 杉德支付接口
 */
@Service
public class SandpayPaymentService extends BasePayment {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(SandpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SANDPAY;
    }

    public final static String PAY_CHANNEL_SANDPAY_BANK_B2C = PayConstant.CHANNEL_NAME_SANDPAY + "_bank_b2c";	// 杉德网银支付
    public final static String PAY_CHANNEL_SANDPAY_BANK_B2B = PayConstant.CHANNEL_NAME_SANDPAY + "_bank_b2b";	// 杉德网银支付
    public final static String PAY_CHANNEL_SANDPAY_WXPAY_NATIVE = PayConstant.CHANNEL_NAME_SANDPAY + "_wxpay_native";	    // 杉德微信扫码支付
    public final static String PAY_CHANNEL_SANDPAY_ALIPAY_NATIVE = PayConstant.CHANNEL_NAME_SANDPAY + "_alipay_native";	    // 杉德支付宝扫码支付
    public final static String PAY_CHANNEL_SANDPAY_UNIONPAY_NATIVE = PayConstant.CHANNEL_NAME_SANDPAY + "_unionpay_native";	// 杉德银联扫码支付

    //支付模式定义
    public enum PayMode {
        bank_b2c, bank_b2b
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;
        switch (channelId) {
            case PAY_CHANNEL_SANDPAY_WXPAY_NATIVE :
                retObj = doNativeReq(payOrder, SandpayBase.PAY_TOOL_QR_WXPAY);
                break;
            case PAY_CHANNEL_SANDPAY_ALIPAY_NATIVE :
                retObj = doNativeReq(payOrder, SandpayBase.PAY_TOOL_QR_ALIPAY);
                break;
            case PAY_CHANNEL_SANDPAY_UNIONPAY_NATIVE :
                retObj = doNativeReq(payOrder, SandpayBase.PAY_TOOL_QR_UNIONPAY);
                break;
            case PAY_CHANNEL_SANDPAY_BANK_B2C :
                retObj = doGatewayReq(payOrder, PayMode.bank_b2c);
                break;
            case PAY_CHANNEL_SANDPAY_BANK_B2B :
                retObj = doGatewayReq(payOrder, PayMode.bank_b2b);
                break;
            default: return ApiBuilder.bizError("不支持的杉德渠道[channelId="+channelId+"]");
        }
        return retObj;
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        SandpayConfig sandpayConfig = new SandpayConfig(getPayParam(payOrder));
        String reqAddr="/order/create";   //接口报文规范中获取
        JSONObject retObj = new JSONObject();
        JSONObject head = buildQueryHead(sandpayConfig, payOrder.getChannelId());
        JSONObject body = buildQueryBody(payOrder);

        String mchId = sandpayConfig.getMchId();
        String publicKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + sandpayConfig.getPublicKeyFile();
        String privateKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator +  sandpayConfig.getPrivateKeyFile();
        String keyPassword = sandpayConfig.getPassword();
        String reqUrl = sandpayConfig.getReqUrl() + "/gateway/api" + reqAddr;

        JSONObject resp = SandpayBase.requestServer(head, body,
                mchId, publicKeyPath, privateKeyPath, keyPassword, reqUrl);

        if(resp == null) {
            return QueryRetMsg.unknown();
        }

        String respCode = resp.getJSONObject("head").getString("respCode"); // 响应吗
        String respMsg = resp.getJSONObject("head").getString("respMsg");   // 响应描述
        if("000000".equals(respCode)){
            String orderStatus = resp.getJSONObject("body").getString("orderStatus");
            String oriRespCode = resp.getJSONObject("body").getString("oriRespCode");
            if("000000".equals(oriRespCode) && "00".equals(orderStatus)){
                return QueryRetMsg.confirmSuccess(null);
            }else {
                return QueryRetMsg.waiting();
            }
        }else{
            return QueryRetMsg.unknown();
        }
    }

    /**
     * 杉德网关支付
     * @param payOrder
     * @param mode
     * @return
     */
    public AbstractRes doGatewayReq(PayOrder payOrder, PayMode mode) {
        SandpayConfig sandpayConfig = new SandpayConfig(getPayParam(payOrder));
        String reqAddr="/order/pay";   //接口报文规范中获取

        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        JSONObject head = buildGatewayHead(sandpayConfig, mode);
        JSONObject body = buildGatewayBody(payOrder, mode);

        String mchId = sandpayConfig.getMchId();
        String publicKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + sandpayConfig.getPublicKeyFile();
        String privateKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator +  sandpayConfig.getPrivateKeyFile();
        String keyPassword = sandpayConfig.getPassword();
        String reqUrl = sandpayConfig.getReqUrl() + "/gateway/api" + reqAddr;

        JSONObject resp = SandpayBase.requestServer(head, body,
                mchId, publicKeyPath, privateKeyPath, keyPassword, reqUrl);

        if(resp == null) {
            return ApiBuilder.bizError("操作失败!");
        }

        String respCode = resp.getJSONObject("head").getString("respCode"); // 响应吗
        String respMsg = resp.getJSONObject("head").getString("respMsg");   // 响应描述

        if("000000".equals(respCode)){
            String traceNo = resp.getJSONObject("body").getString("traceNo");

            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), traceNo);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);

            String credential = resp.getJSONObject("body").getString("credential"); // 支付凭证数据

            JSONObject credentialObj = JSONObject.parseObject(credential);
            String submitUrl = credentialObj.getString("submitUrl");
            JSONObject paramsObj = credentialObj.getJSONObject("params");
            StringBuffer payForm = new StringBuffer();
            payForm.append("<form action=\""+submitUrl+"\" method=\"post\" >");
            for(String k : paramsObj.keySet()) {
                payForm.append("<input type=\"text\" name=\""+k+"\" value=\""+paramsObj.get(k)+"\">");
            }
            payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
            payForm.append("</form>");
            payForm.append("<script>document.forms[0].submit();</script>");

            // 支付链接地址
            retObj.setPayOrderId(payOrder.getPayOrderId()); // 设置支付订单ID
            retObj.setJumpInfo(payForm.toString());
            return retObj;
        }else{
            return ApiBuilder.bizError("操作失败!");
        }

    }

    /**
     * 杉德扫码支付
     * @param payOrder
     * @param payTool 支付工具: 0401-支付宝扫码   0402-微信扫码
     * @return
     */
    public AbstractRes doNativeReq(PayOrder payOrder, String payTool) {
        SandpayConfig sandpayConfig = new SandpayConfig(getPayParam(payOrder));
        String reqAddr="/order/create";   //接口报文规范中获取

        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        JSONObject head = buildQrHead(sandpayConfig, payTool);
        JSONObject body = buildQrBody(payOrder, payTool);
        //_log.info("{}发起请求:head={}, body={}", getChannelName(), head, body);

        String mchId = sandpayConfig.getMchId();
        String publicKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + sandpayConfig.getPublicKeyFile();
        String privateKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator +  sandpayConfig.getPrivateKeyFile();
        String keyPassword = sandpayConfig.getPassword();
        String reqUrl = sandpayConfig.getReqUrl() + "/qr/api" + reqAddr;

        JSONObject resp = SandpayBase.requestServer(head, body,
                mchId, publicKeyPath, privateKeyPath, keyPassword, reqUrl);

        if(resp == null) {
            return ApiBuilder.bizError("操作失败!");
        }

        //_log.info("{}请求结果:{}", getChannelName(), resp);
        String respCode = resp.getJSONObject("head").getString("respCode"); // 响应吗
        String respMsg = resp.getJSONObject("head").getString("respMsg");   // 响应描述

        if("000000".equals(respCode)){
            String traceNo = resp.getJSONObject("body").getString("traceNo");
            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), traceNo);
            _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
            String codeUrl = resp.getJSONObject("body").getString("qrCode"); // 二维码支付链接
            retObj.setQrInfo(codeUrl, genCodeImgUrl(codeUrl));

            return retObj;
        }else{
            return ApiBuilder.bizError("操作失败!");
        }

    }

    JSONObject buildQrHead(SandpayConfig sandpayConfig, String payTool) {
        JSONObject header = new JSONObject();
        header.put("version", SandpayBase.version);			            //版本号
        header.put("method", SandpayBase.QR_ORDERCREATE);		        //接口名称:预下单
        if(SandpayBase.PAY_TOOL_QR_WXPAY.equals(payTool)) {
            header.put("productId", SandpayBase.PRODUCTID_WXPAY);	    //产品编码,根据实际支付方式确定
        }else if(SandpayBase.PAY_TOOL_QR_ALIPAY.equals(payTool)) {
            header.put("productId", SandpayBase.PRODUCTID_ALIPAY);
        }else if(SandpayBase.PAY_TOOL_QR_UNIONPAY.equals(payTool)) {
            header.put("productId", SandpayBase.PRODUCTID_UNIONPAY);
        }

        header.put("mid", sandpayConfig.getMchId());	            //商户ID
        String plMid = sandpayConfig.getPlMid();		            //平台商户ID
        if(plMid!=null && org.apache.commons.lang.StringUtils.isNotEmpty(plMid)) {  //平台商户存在时接入
            header.put("accessType", "2");					        //接入类型设置为平台商户接入
            header.put("plMid", plMid);
        }else {
            header.put("accessType", "1");					        //接入类型设置为普通商户接入
        }
        header.put("channelType", "07");					        //渠道类型：07-互联网   08-移动端
        header.put("reqTime", SandpayBase.getCurrentTime());	    //请求时间
        return header;
    }

    JSONObject buildQrBody(PayOrder payOrder, String payTool) {
        JSONObject body = new JSONObject();

        body.put("payTool", payTool);						    //支付工具: 0401-支付宝扫码   0402-微信扫码   (需和产品编码对应)
        body.put("orderCode", payOrder.getPayOrderId());		//商户订单号
        //	body.put("limitPay","1");							//限定支付方式 (只有微信支持)  1-限定不能使用信用卡
        body.put("totalAmount", String.format("%12d", payOrder.getAmount()).replace(" ", "0"));			//订单金额 12位长度，精确到分
        body.put("subject", payOrder.getSubject());				//订单标题
        body.put("body", payOrder.getBody());					//订单描述
        body.put("txnTimeOut", SandpayBase.getNextDayTime());	//订单超时时间
        body.put("storeId", "");							    //商户门店编号
        body.put("terminalId", "");							    //商户终端编号
        body.put("operatorId", "");							    //操作员编号
        body.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));	//异步通知地址
        body.put("bizExtendParams", "");					    //业务扩展参数
        body.put("merchExtendParams", "");					    //商户扩展参数
        body.put("extend", "");								    //扩展域
        return body;
    }

    JSONObject buildGatewayHead(SandpayConfig sandpayConfig, PayMode mode) {
        JSONObject header = new JSONObject();
        header.put("version", SandpayBase.version);			//版本号
        header.put("method", SandpayBase.ORDERPAY);			//接口名称:统一下单
        header.put("mid", sandpayConfig.getMchId());	    //商户ID
        String plMid = sandpayConfig.getPlMid();		    //平台商户ID
        if(plMid!=null && StringUtils.isNotEmpty(plMid)) {  //平台商户存在时接入
            header.put("accessType", "2");					//接入类型设置为平台商户接入
            header.put("plMid", plMid);
        }else {
            header.put("accessType", "1");					//接入类型设置为平台商户接入
        }
        header.put("channelType", "07");					//渠道类型：07-互联网   08-移动端
        header.put("reqTime", SandpayBase.getCurrentTime());	//请求时间
        String productId="";

        switch(mode) {
            case  bank_b2c:    	// 银行网关B2C支付
                productId = SandpayBase.PRODUCTID_B2C;
                break;
            case  bank_b2b:    	// 银行网关B2B支付
                productId = SandpayBase.PRODUCTID_B2B;
                break;
        }

        header.put("productId", productId); //产品编码,详见《杉德线上支付接口规范》 附录
        return header;
    }

    JSONObject buildGatewayBody(PayOrder payOrder, PayMode mode) {
        JSONObject body = new JSONObject();
        body.put("orderCode", payOrder.getPayOrderId());                           //商户订单号
        body.put("totalAmount", String.format("%12d", payOrder.getAmount()).replace(" ", "0")); //订单金额
        body.put("subject", payOrder.getSubject());                               //订单标题
        body.put("body", payOrder.getBody());                                     //订单描述
        body.put("txnTimeOut", DemoBase.getNextDayTime());                        //订单超时时间
        body.put("clientIp", payOrder.getClientIp());                             //客户端IP
        body.put("notifyUrl", payConfig.getNotifyUrl(getChannelName()));          //异步通知地址
        body.put("frontUrl", payConfig.getReturnUrl(getChannelName()));           //前台通知地址
        body.put("storeId", "");                                                  //商户门店编号
        body.put("terminalId", "");                                               //商户终端编号
        body.put("operatorId", "");                                               //操作员编号
        body.put("clearCycle", "");                                               //清算模式
        body.put("royaltyInfo", "");                                              //分账信息
        body.put("riskRateInfo", "");                                             //风控信息域
        body.put("bizExtendParams", "");                                          //业务扩展参数
        body.put("merchExtendParams", "");                                        //商户扩展参数
        body.put("extend", "");                                                   //扩展域

        String payMode = "bank_pc";
        JSONObject payExtra = new JSONObject();
        String payType = "1";   // 支付类型  1-借记卡  2-贷记卡  3-借/贷记卡  4-B2B支付
        switch(mode) {
            case  bank_b2c:
                payType = "1";
                break;
            case  bank_b2b:
                payType = "4";
                break;
        }
        String bankCode = "03080000";   // 银行编码,具体编码见《杉德线上支付接口规范》 附录
        String extra = payOrder.getExtra();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(extra)) {
            try {
                JSONObject extraObj = JSONObject.parseObject(extra);
                payType = extraObj.getString("payType") == null ? payType : extraObj.getString("payType");
                bankCode = extraObj.getString("bankCode") == null ? bankCode : extraObj.getString("bankCode");
            }catch (Exception e) {
                _log.error(e, "");
            }
        }
        payExtra.put("payType",payType);
        payExtra.put("bankCode", bankCode);
        body.put("payMode", payMode);					//支付模式
        body.put("payExtra", payExtra.toJSONString());	//支付扩展域
        return body;
    }

    JSONObject buildQueryHead(SandpayConfig sandpayConfig, String channelId) {
        JSONObject header = new JSONObject();
        header.put("version", SandpayBase.version);			//版本号
        header.put("method", SandpayBase.ORDERQUERY);		//接口名称:订单查询
        switch (channelId) {
            case PAY_CHANNEL_SANDPAY_BANK_B2C :
                header.put("productId", SandpayBase.PRODUCTID_B2C);
                break;
            case PAY_CHANNEL_SANDPAY_BANK_B2B :
                header.put("productId", SandpayBase.PRODUCTID_B2B);
                break;
            case PAY_CHANNEL_SANDPAY_WXPAY_NATIVE :
                header.put("productId", SandpayBase.PRODUCTID_WXPAY);
                break;
            case PAY_CHANNEL_SANDPAY_ALIPAY_NATIVE :
                header.put("productId", SandpayBase.PRODUCTID_ALIPAY);
                break;
        }
        header.put("mid", sandpayConfig.getMchId());	    //商户ID
        String plMid = sandpayConfig.getPlMid();		    //平台商户ID
        if(plMid!=null && StringUtils.isNotEmpty(plMid)) {  //平台商户存在时接入
            header.put("accessType", "2");					//接入类型设置为平台商户接入
            header.put("plMid", plMid);
        }else {
            header.put("accessType", "1");					//接入类型设置为平台商户接入
        }
        header.put("channelType", "07");					//渠道类型：07-互联网   08-移动端
        header.put("reqTime", SandpayBase.getCurrentTime());	//请求时间
        return header;
    }

    JSONObject buildQueryBody(PayOrder payOrder) {
        JSONObject body = new JSONObject();
        body.put("orderCode", payOrder.getPayOrderId());    //商户订单号
        body.put("extend", "");							    //扩展域
        return body;
    }

}
