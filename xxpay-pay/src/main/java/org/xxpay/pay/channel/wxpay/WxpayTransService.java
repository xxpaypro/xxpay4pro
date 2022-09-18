package org.xxpay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.BaseTrans;

import java.io.File;

/**
 * @author: dingzhiwei
 * @date: 17/12/25
 * @description:
 */
@Service
public class WxpayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(WxpayTransService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @Override
    public QueryRetMsg trans(TransOrder transOrder) {
        String logPrefix = "【微信企业付款】";
        try{
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(getTransParam(transOrder), "", payConfig.getUploadIsvCertRootDir(), payConfig.getNotifyUrl(getChannelName()));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            EntPayService entPayService = new EntPayServiceImpl(wxPayService);
            EntPayRequest entPayRequest = buildWxEntPayRequest(transOrder, wxPayConfig);

            EntPayResult result;
            try {
                result = entPayService.entPay(entPayRequest);
                _log.info("{} >>> 转账成功", logPrefix);
                return QueryRetMsg.confirmSuccess(result.getPaymentNo());
            } catch (WxPayException e) {
                _log.error(e, "转账失败");
                //出现业务错误
                _log.info("{}转账返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                return QueryRetMsg.confirmFail(null, e.getErrCode(), e.getErrCodeDes());
            }
        }catch (Exception e) {
            _log.error(e, "微信转账异常");
            return QueryRetMsg.sysError();
        }
    }

    public QueryRetMsg query(TransOrder transOrder) {
        String logPrefix = "【微信企业付款查询】";
        QueryRetMsg retObj;
        try{
            WxPayConfig wxPayConfig = WxPayUtil.getWxPayConfig(getTransParam(transOrder), "", payConfig.getUploadIsvCertRootDir(), payConfig.getNotifyUrl(getChannelName()));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            EntPayService entPayService = new EntPayServiceImpl(wxPayService);
            String transOrderId = transOrder.getTransOrderId();
            EntPayQueryResult result;
            try {
                result = entPayService.queryEntPay(transOrderId);
                _log.info("{} >>> 成功", logPrefix);
                retObj = QueryRetMsg.confirmSuccess(null);
                retObj.setChannelOriginResponse(result.toString());
                return retObj;

            } catch (WxPayException e) {
                _log.error(e, "失败");
                //出现业务错误
                _log.info("{}返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                return QueryRetMsg.confirmFail(null, e.getErrCode(), e.getErrCodeDes());
            }
        }catch (Exception e) {
            _log.error(e, "微信企业付款查询异常");
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AgentPayBalanceRetMsg balance(String payParam) {
        return AgentPayBalanceRetMsg.unsupported();
    }

    /**
     * 构建微信企业付款请求数据
     * @param transOrder
     * @param wxPayConfig
     * @return
     */
    EntPayRequest buildWxEntPayRequest(TransOrder transOrder, WxPayConfig wxPayConfig) {
        // 微信企业付款请求对象
        EntPayRequest request = new EntPayRequest();
        request.setAmount(transOrder.getAmount().intValue()); // 金额,单位分
        String checkName = "NO_CHECK";
        if(transOrder.getExtra() != null) checkName = JSON.parseObject(transOrder.getExtra()).getString("checkName");
        request.setCheckName(checkName);
        request.setDescription(transOrder.getRemarkInfo());
        request.setReUserName(transOrder.getAccountName());
        request.setPartnerTradeNo(transOrder.getTransOrderId());
        request.setDeviceInfo(transOrder.getDevice());
        request.setSpbillCreateIp(transOrder.getClientIp());
        request.setOpenid(transOrder.getChannelUser());
        return request;
    }

}
