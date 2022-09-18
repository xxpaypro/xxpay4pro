package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayCloseRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.PaymentInterface;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 支付订单关闭
 * @author dingzhiwei jmdhappy@126.com
 * @date 2020-08-03
 * @version V1.0
 * @Copyright: www.xxpay.vip
 */
@RestController
public class ClosePayOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(ClosePayOrderController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 关闭支付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)执行关闭订单逻辑
     * 3)返回结果
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/pay/close_order")
    public AbstractRes closePayOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户关闭支付订单请求 ######");
        String logPrefix = "【商户支付订单关闭】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            // 验证参数有效性
            String errorMessage = validateParams(po, payContext);
            if (!"success".equalsIgnoreCase(errorMessage)) {
                _log.warn(errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            _log.debug("请求参数及签名校验通过");
            Long mchId = po.getLong("mchId"); 			        // 商户ID
            String mchOrderNo = po.getString("mchOrderNo"); 	// 商户订单号
            String payOrderId = po.getString("payOrderId"); 	// 支付订单号

            PayOrder payOrder = payOrderService.query(mchId, payOrderId, mchOrderNo);
            if (payOrder == null) {
                return ApiBuilder.bizError("支付订单不存在");
            }
            if(PayConstant.PAY_STATUS_PAYING != payOrder.getStatus()) { // 非支付中状态不能关闭
                return ApiBuilder.bizError("非支付中订单不能关闭");
            }
            payOrderId = payOrder.getPayOrderId();
            mchOrderNo = payOrder.getMchOrderNo();
            String channelId = payOrder.getChannelId();
            String channelName = channelId.substring(0, channelId.indexOf("_"));
            PaymentInterface paymentInterface;
            try {
                paymentInterface = (PaymentInterface) SpringUtil.getBean(channelName.toLowerCase() +  "PaymentService");
            }catch (BeansException e) {
                _log.error(e, "");
                return ApiBuilder.bizError("调用支付渠道失败");
            }

            // 执行支付
            AbstractRes res = paymentInterface.close(payOrder);
            if(res == null) return ApiBuilder.bizError("调用支付渠道失败");

            PayCloseRes payCloseRes = (PayCloseRes) res;
            if(payCloseRes.getResultCode() == PayCloseRes.ResultCode.SUCCESS) {
                // 关闭订单操作
                int result = rpcCommonService.rpcPayOrderService.closePayOrder(payOrderId);
                if(result != 1) {
                    // 关闭支付系统订单失败,需要人工查看
                    _log.error("{}支付渠道已关闭,支付系统订单关闭失败,需要人工查看.payOrderId={}", logPrefix, payOrderId);
                }
            }
            _log.info("###### 商户关闭订单处理完成 ######");
            payCloseRes.autoGenSign(payContext.getString("key")); //自动签名
            return payCloseRes;
        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("支付中心系统异常");
        }
    }


    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID

        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String payOrderId = params.getString("payOrderId"); 	// 支付订单号

        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        if(StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(payOrderId)) {
            errorMessage = "request params[mchOrderNo or payOrderId] error.";
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "mchInfo not available [mchId="+mchId+"] record in db.";
            return errorMessage;
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "key is null[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        payContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }

        return "success";
    }

}
