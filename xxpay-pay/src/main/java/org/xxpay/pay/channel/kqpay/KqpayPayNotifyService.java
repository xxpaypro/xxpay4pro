package org.xxpay.pay.channel.kqpay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class KqpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(KqpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_KQPAY;
    }

    String buildRtn(int rtnOk, String rtnUrl) {
        return String.format("<result>%d</result><redirecturl>%s</redirecturl>", rtnOk, rtnUrl);
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        int rtnOK = 0;
        String rtnUrl;
        String logPrefix = "【处理快钱支付回调】";
        _log.info("====== 开始处理快钱支付回调通知 ======");
        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);
        if(!verifyKqPayParams(payContext)) {
            // showUrl 此处得不到
            String showUrl = "";
            rtnUrl= showUrl + "?msg=false";
            retObj.put("resResult", buildRtn(rtnOK, rtnUrl));
            return retObj;
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);
        String payResult = params.get("payResult").toString();		// 交易结果
        payOrder = (PayOrder) payContext.get("payOrder");
        KqpayConfig kqpayConfig = new KqpayConfig(getPayParam(payOrder));
        switch(Integer.parseInt(payResult)) {
            case 10:
                int updatePayOrderRows;
                payOrder = (PayOrder)payContext.get("payOrder");
                byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
                if (payStatus != PayConstant.PAY_STATUS_SUCCESS) {
                    updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), StrUtil.toString(params.get("orderId"), null));
                    if (updatePayOrderRows != 1) {
                        rtnOK = 0;
                        //以下是我们快钱设置的show页面，商户需要自己定义该页面。
                        rtnUrl= kqpayConfig.getShowUrl() + "?msg=false";
                        _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                        _log.info("{}响应给快钱结果：{}", logPrefix, buildRtn(rtnOK, rtnUrl));
                        retObj.put("resResult", buildRtn(rtnOK, rtnUrl));
                        return retObj;
                    }
                    _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                }
                rtnOK = 1;
                rtnUrl = kqpayConfig.getShowUrl() + "?msg=success";
                break;
            default:
                rtnOK = 0;
                rtnUrl= kqpayConfig.getShowUrl() + "?msg=false";
                _log.info("{}支付结果payResult={},不做业务处理", logPrefix, payResult);
                _log.info("{}响应给快钱结果：{}", logPrefix, buildRtn(rtnOK, rtnUrl));
                retObj.put(PayConstant.RESPONSE_RESULT, buildRtn(rtnOK, rtnUrl));
                return retObj;
        }
        baseNotify4MchPay.doNotify(payOrder, true);
        _log.info("====== 完成处理快钱支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, buildRtn(rtnOK, rtnUrl));
        return retObj;
    }

    /**
     * 解析回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;

    }

    /**
     * 验证快钱支付通知参数
     * @return
     */
    public boolean verifyKqPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");

        //人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。
        String merchantAcctId = params.get("merchantAcctId");
        //网关版本，固定值：v2.0,该值与提交时相同。
        String version = params.get("version");
        //语言种类，1代表中文显示，2代表英文显示。默认为1,该值与提交时相同。
        String language = params.get("language");
        //签名类型,该值为4，代表PKI加密方式,该值与提交时相同。
        String signType = params.get("signType");
        //支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10,该值与提交时相同。
        String payType = params.get("payType");
        //银行代码，如果payType为00，该值为空；如果payType为10,该值与提交时相同。
        String bankId = params.get("bankId");
        //商户订单号，该值与提交时相同。
        String orderId = params.get("orderId");
        //订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101,该值与提交时相同。
        String orderTime = params.get("orderTime");
        //订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试,该值与支付时相同。
        String orderAmount = params.get("orderAmount");
        // 快钱交易号，商户每一笔交易都会在快钱生成一个交易号。
        String dealId = params.get("dealId");
        //银行交易号 ，快钱交易在银行支付时对应的交易号，如果不是通过银行卡支付，则为空
        String bankDealId = params.get("bankDealId");
        //快钱交易时间，快钱对交易进行处理的时间,格式：yyyyMMddHHmmss，如：20071117020101
        String dealTime = params.get("dealTime");
        //商户实际支付金额 以分为单位。比方10元，提交时金额应为1000。该金额代表商户快钱账户最终收到的金额。
        String payAmount = params.get("payAmount");
        //费用，快钱收取商户的手续费，单位为分。
        String fee = params.get("fee");
        //扩展字段1，该值与提交时相同。
        String ext1 = params.get("ext1");
        //扩展字段2，该值与提交时相同。
        String ext2 = params.get("ext2");
        //处理结果， 10支付成功，11 支付失败，00订单申请成功，01 订单申请失败
        String payResult = params.get("payResult");
        //错误代码 ，请参照《人民币网关接口文档》最后部分的详细解释。
        String errCode = params.get("errCode");
        //签名字符串
        String signMsg = params.get("signMsg");

        String out_trade_no = orderId;		// 商户订单号
        String total_amount = orderAmount; 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(out_trade_no)) {
            _log.error("KqPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("KqPay Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        // 验证签名
        String merchantSignMsgVal = "";
        merchantSignMsgVal = appendParam(merchantSignMsgVal,"merchantAcctId", merchantAcctId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version",version);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "language",language);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType",signType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType",payType);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId",bankId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId",orderId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime",orderTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount",orderAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId",dealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId",bankDealId);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime",dealTime);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount",payAmount);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult",payResult);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode",errCode);
        KqpayConfig kqpayConfig = new KqpayConfig(getPayParam(payOrder));
        String privateCertFilePath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + kqpayConfig.getPrivateCertPath();
        String publicCertFilePath = payConfig.getUploadIsvCertRootDir() + File.separator + getChannelName() + File.separator + kqpayConfig.getPublicCertPath();
        Pkipair pki = new Pkipair(privateCertFilePath, publicCertFilePath, kqpayConfig.getKeyPwd());
        boolean verify_result = pki.enCodeByCer(merchantSignMsgVal, signMsg);
        // 验证
        if (!verify_result) {
            errorMessage = "KqPay verify sign failed.";
            _log.error("KqPay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long aliPayAmt = new BigDecimal(total_amount).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);

        return true;
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
