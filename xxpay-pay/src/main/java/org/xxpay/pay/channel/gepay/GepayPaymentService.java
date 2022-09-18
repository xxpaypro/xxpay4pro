package org.xxpay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.mq.Mq4PayQuery;

/**
 * @author: dingzhiwei
 * @date: 18/3/1
 * @description: 个付通支付接口
 */
@Service
public class GepayPaymentService extends BasePayment {

    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(GepayPaymentService.class);

    @Override
    public String getChannelName() {
        return GepayConfig.CHANNEL_NAME;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        switch (channelId) {
            case GepayConfig.CHANNEL_NAME_WXPAY :
                return doPayReq(payOrder, "wechat");
            case GepayConfig.CHANNEL_NAME_ALIPAY :
                return doPayReq(payOrder, "alipay");
            default:
                return ApiBuilder.bizError("不支持的渠道[channelId="+channelId+"]");
        }
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        return null;
    }

    /**
     * 微信扫码支付
     * @param payOrder
     * @return
     */
    public AbstractRes doPayReq(PayOrder payOrder, String channel) {
        GepayConfig gepayConfig = new GepayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", gepayConfig.getMchId());
        paramMap.put("command", "applyqr");
        paramMap.put("money", payOrder.getAmount());
        paramMap.put("channel", channel);
        paramMap.put("user_name", payOrder.getPayOrderId());
        try {
            String reqData = XXPayUtil.genUrlParams(paramMap);
            _log.info("{}请求数据:{}", getChannelName(), reqData);
            String url = gepayConfig.getReqUrl() + "?";
            String result = XXPayUtil.call4Post(url + reqData);
            _log.info("{}返回数据:{}", getChannelName(), result);
            if(StringUtils.isBlank(result)) {
                return ApiBuilder.bizError("上游通道返回空!");
            }
            //{"message":"ok","data":{"url":"wxp://f2f14TiGjC0Iy5WiqNbmB-8gz3RNsEhQ0HLY","mark_sell":"编号：1538115871304"},"status":1}
            JSONObject resObj = JSONObject.parseObject(result);
            if("ok".equals(resObj.getString("message")) && resObj.getIntValue("status") == 1) {
                JSONObject dataObj = resObj.getJSONObject("data");

                String codeUrl = dataObj.getString("url");
                String markSell = dataObj.getString("mark_sell");
                int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", updateCount);

                retObj.setQrInfo(codeUrl, payConfig.getPayUrl() + "/qrcode_img_get?url=" + codeUrl + "&widht=200&height=200");
                return retObj;
            }else {
                return ApiBuilder.bizError("调起上游接口响应失败!");
            }
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("操作失败!");
        }
    }

}
