package org.xxpay.pay.channel.silverspay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/6/29
 * @description: 睿联支付接口
 */
@Service
public class SilverspayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(SilverspayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SILVERSPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;
        switch (channelId) {
            case PayConstant.PAY_CHANNEL_SILVERSPAY_GATEWAY :
                retObj = doGatewayPayReq(payOrder);
                break;
            default: return ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
        }
        return retObj;
    }

    /**
     * B2C网关支付(有收银台)
     * @param payOrder
     * @return
     */
    public AbstractRes doGatewayPayReq(PayOrder payOrder) {
        SilverspayConfig silverspayConfig = new SilverspayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        String key = silverspayConfig.getKey();
        String url = silverspayConfig.getReqUrl();
        String mchId = silverspayConfig.getMchId();
        // 支付参数
        Map<String, String> params = new HashMap<>();
        params.put("pay_memberid", mchId);//商户id
        params.put("pay_orderid", payOrder.getPayOrderId());    //订单号
        params.put("pay_applydate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//下单时间
        params.put("pay_bankcode", "913");//通道编码
        params.put("pay_notifyurl", payConfig.getNotifyUrl(getChannelName()));//回调地址
        params.put("pay_callbackurl", payConfig.getReturnUrl(getChannelName()));//页面跳转返回地址
        // 交易金额
        String amount = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
        params.put("pay_amount", amount);
        String payResult = "";
        try {
            String sign = Util.generateSignature(params, "MD5", key);
            params.put("pay_md5sign", sign);
            payResult = XXPayUtil.call4Post(url + "?" + XXPayUtil.genUrlParams2(params));
        } catch (Exception e) {
            _log.error(e, "");
        }
        _log.info("[{}]下单返回结果:{}", getChannelName(), payResult);
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
        // 支付链接地址
        retObj.setPayOrderId(payOrder.getPayOrderId()); // 设置支付订单ID
        Document doc = Jsoup.parse(payResult);
        Elements formElements = doc.getElementsByTag("form");
        formElements.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");

        retObj.setJumpInfo(formElements.toString() + "<script>document.forms[0].submit();</script>");
        return retObj;
    }

    public static void main(String[] args) {

        String htmlForm = "<html>\\r\\n<head>\\r\\n    <meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=utf-8\\\" />\\r\\n</head>\\r\\n<body onload=\\\"javascript:document.pay_form.submit();\\\">\\r\\n    <form id=\\\"pay_form\\\" name=\\\"pay_form\\\" action=\\\"http://www.cixiangshangmao.com/gotopay/ys/bankpay.php\\\" method=\\\"post\\\">\\r\\n\\t    <input type=\\\"hidden\\\" name=\\\"version\\\" id=\\\"version\\\" value=\\\"5.1.0\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"encoding\\\" id=\\\"encoding\\\" value=\\\"utf-8\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"txnType\\\" id=\\\"txnType\\\" value=\\\"01\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"txnSubType\\\" id=\\\"txnSubType\\\" value=\\\"01\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"bizType\\\" id=\\\"bizType\\\" value=\\\"000201\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"frontUrl\\\" id=\\\"frontUrl\\\" value=\\\"http://www.cixiangshangmao.com/gotopay/ys/bankback.php\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"backUrl\\\" id=\\\"backUrl\\\" value=\\\"http://www.cixiangshangmao.com/gotopay/ys/banknotify.php\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"signMethod\\\" id=\\\"signMethod\\\" value=\\\"01\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"channelType\\\" id=\\\"channelType\\\" value=\\\"07\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"accessType\\\" id=\\\"accessType\\\" value=\\\"0\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"currencyCode\\\" id=\\\"currencyCode\\\" value=\\\"156\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"merId\\\" id=\\\"merId\\\" value=\\\"898319873920571\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"orderId\\\" id=\\\"orderId\\\" value=\\\"20180703110046101535\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"txnTime\\\" id=\\\"txnTime\\\" value=\\\"20180703110046\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"txnAmt\\\" id=\\\"txnAmt\\\" value=\\\"1\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"payTimeout\\\" id=\\\"payTimeout\\\" value=\\\"20180703133046\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"certId\\\" id=\\\"certId\\\" value=\\\"75018744402\\\" />\\n    <input type=\\\"hidden\\\" name=\\\"signature\\\" id=\\\"signature\\\" value=\\\"F27nubgHaM+VGyQfsi2WdPG/lbHIxlYDL6WnEYB/hkFGY5Kknj+rSkZeXjqEHlK7+JCzZBURGS6K/CxDaQOPedtoHzPNgAQAhgVcuqU233N8EVwFuztJAsH2oMIEGwV79Yd42rWr2S9Y8Rcfo/Lf2o1cpw2d2Ld9fsaDRS/1AVEVfFr1kahq75VN8powMSpSNRDhexWEQMTuHZqAgDnC0jewCPakWLslAZfIamOeqWXHtbyAeh1qfvi4HApmOz5f3pI1m2fu0vZ1L1OtPqe5c0P4SqpWlbYW08FWEtvJ8eBpH3m3Z+pGhl0npJG1wAvaU6NBXxM1gIJuIoOY9TbZVg==\\\" />\\n   <!-- <input type=\\\"submit\\\" type=\\\"hidden\\\">-->\\r\\n    </form>\\r\\n</body>\\r\\n</html>";
        Document doc = Jsoup.parse(htmlForm);
        Elements formElements = doc.getElementsByTag("form");
        formElements.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");

        //Element element = new Element("input type=\\\"submit\\\" value=\\\"立即支付\\\" style=\\\"display:none\\\" ");
        //formElements.add(element);

// <input type=\"submit\" value=\"立即支付\" style=\"display:none\" >

        //<script>document.forms[0].submit();</script>

//        formElements.parents().append("<script>document.forms[0].submit();</script>");
        //Elements elements = formElements.parents();
        // doc.getElementsByTag("body").append("<script>document.forms[0].submit();</script>");
        //formElements.after("<script>document.forms[0].submit();</script>");
        System.out.println("(3)body=" + formElements.toString() + "<script>document.forms[0].submit();</script>");

    }

}
