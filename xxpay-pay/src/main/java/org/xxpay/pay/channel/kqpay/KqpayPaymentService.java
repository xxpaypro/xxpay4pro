package org.xxpay.pay.channel.kqpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;

import java.io.File;

/**
 * @author: dingzhiwei
 * @date: 18/1/29
 * @description:
 */
@Service
public class KqpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(KqpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_KQPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        KqpayConfig kqpayConfig = new KqpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        //人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = kqpayConfig.getMerchantAcctId();
        //编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        //接收支付结果的页面地址，该参数一般置为空即可。
        String pageUrl = payConfig.getReturnUrl(getChannelName());
        //服务器接收支付结果的后台地址，该参数务必填写，不能为空。
        String bgUrl = payConfig.getNotifyUrl(getChannelName());
        //网关版本，固定值：v2.0,该参数必填。
        String version =  "v2.0";
        //语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        String language =  "1";
        //签名类型,该值为4，代表PKI加密方式,该参数必填。
        String signType =  "4";
        //支付人姓名,可以为空。
        String payerName= "";
        //支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
        String payerContactType =  "";
        //支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
        String payerContact =  "";
        //商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = payOrder.getPayOrderId();
        //订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
        String orderAmount = payOrder.getAmount()+"";
        //订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        //商品名称，可以为空。
        String productName= payOrder.getSubject();
        //商品数量，可以为空。
        String productNum = "";
        //商品代码，可以为空。
        String productId = "";
        //商品描述，可以为空。
        String productDesc = payOrder.getBody();
        //扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext1 = "";
        //扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext2 = "";
        //支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
        String payType = "00";
        //银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
        String bankId = "";
        //同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        String redoFlag = "";
        //快钱合作伙伴的帐户号，即商户编号，可为空。
        String pid = "";

        // 生成需要传递参数对象
        JSONObject object = new JSONObject();
        object.put("inputCharset", inputCharset);
        object.put("pageUrl", pageUrl);
        object.put("bgUrl", bgUrl);
        object.put("version", version);
        object.put("language", language);
        object.put("signType", signType);
        object.put("merchantAcctId",merchantAcctId);
        object.put("payerName", payerName);
        object.put("payerContactType",payerContactType);
        object.put("payerContact", payerContact);
        object.put("orderId", orderId);
        object.put("orderAmount", orderAmount);
        object.put("orderTime", orderTime);
        object.put("productName", productName);
        object.put("productNum", productNum);
        object.put("productId", productId);
        object.put("productDesc", productDesc);
        object.put("ext1", ext1);
        object.put("ext2", ext2);
        object.put("payType", payType);
        object.put("bankId", bankId);
        object.put("redoFlag", redoFlag);
        object.put("pid", pid);

        // signMsg 签名字符串 不可空，生成加密签名串
        String signMsgVal = "";
        signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset);
        signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl);
        signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl);
        signMsgVal = appendParam(signMsgVal, "version", version);
        signMsgVal = appendParam(signMsgVal, "language", language);
        signMsgVal = appendParam(signMsgVal, "signType", signType);
        signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId);
        signMsgVal = appendParam(signMsgVal, "payerName", payerName);
        signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType);
        signMsgVal = appendParam(signMsgVal, "payerContact", payerContact);
        signMsgVal = appendParam(signMsgVal, "orderId", orderId);
        signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount);
        signMsgVal = appendParam(signMsgVal, "orderTime", orderTime);
        signMsgVal = appendParam(signMsgVal, "productName", productName);
        signMsgVal = appendParam(signMsgVal, "productNum", productNum);
        signMsgVal = appendParam(signMsgVal, "productId", productId);
        signMsgVal = appendParam(signMsgVal, "productDesc", productDesc);
        signMsgVal = appendParam(signMsgVal, "ext1", ext1);
        signMsgVal = appendParam(signMsgVal, "ext2", ext2);
        signMsgVal = appendParam(signMsgVal, "payType", payType);
        signMsgVal = appendParam(signMsgVal, "bankId", bankId);
        signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag);
        signMsgVal = appendParam(signMsgVal, "pid", pid);

        String privateCertFilePath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + kqpayConfig.getPrivateCertPath();
        String publicCertFilePath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + kqpayConfig.getPublicCertPath();
        Pkipair pki = new Pkipair(privateCertFilePath, publicCertFilePath, kqpayConfig.getKeyPwd());
        String signMsg = pki.signMsg(signMsgVal);
        // 签名后存入
        object.put("signMsg", signMsg);

        retObj.setJumpInfo(kqpayConfig.getReqUrl(), null, "POST");
        retObj.setPayParams(object); //自定义参数

        retObj.setPayOrderId(payOrder.getPayOrderId()); // 设置支付订单ID
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
        return retObj;
    }

    public String appendParam(String returns, String paramId, String paramValue) {
        if (!returns.equals("")) {
            if (!paramValue.equals("")) {
                returns += "&" + paramId + "=" + paramValue;
            }
        } else {
            if (!paramValue.equals("")) {
                returns = paramId + "=" + paramValue;
            }
        }
        return returns;
    }

}
