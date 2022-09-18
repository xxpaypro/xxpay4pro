package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.xxpay.core.common.domain.api.RefundOrderRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.RefundInterface;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RefundOrderService;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

/**
 * @Description: 退款
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-10-30
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class RefundOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(RefundOrderController.class);

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /** 统一退款接口 */
    @RequestMapping(value = "/api/refund/create_order")
    public AbstractRes refundOrder() {
        _log.info("###### 开始接收商户统一退款请求 ######");
        String logPrefix = "【商户统一退款】";
        try {
            JSONObject po = getJsonParam();
            _log.info("{}请求参数:{}", logPrefix, po);

            JSONObject refundContext = new JSONObject();   //参数校验 上下文对象 包括  key, channelType, async
            RefundOrder refundOrder = null;
            // 验证参数有效性
            Object object = validateParams(po, refundContext);


            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return ApiBuilder.bizError(object.toString());
            }
            if (object instanceof RefundOrder) refundOrder = (RefundOrder) object;
            if(refundOrder == null) return ApiBuilder.bizError("支付中心退款失败");
            int result = rpcCommonService.rpcRefundOrderService.createRefundOrder(refundOrder);
            _log.info("{}创建退款订单,结果:{}", logPrefix, result);
            if(result != 1) {
                return ApiBuilder.bizError("创建退款订单失败");
            }

            String refundOrderId = refundOrder.getRefundOrderId();
            String channelType = refundContext.getString("channelType");

            // 是否异步退款
            boolean async = refundContext.getBooleanValue("async");

            if(async) {
                // 发送异步退款消息
                refundOrderService.sendRefundNotify(refundOrderId, channelType);
                _log.info("{}发送退款任务完成,refundOrderId={}", logPrefix, refundOrderId);
                // 返回的参数
                RefundOrderRes refundOrderRes = ApiBuilder.buildSuccess(RefundOrderRes.class);
                refundOrderRes.setByAsync(refundOrder);
                refundOrderRes.autoGenSign(refundContext.getString("key"));
                return refundOrderRes;
            }else {

                // 修改转账状态为退款中
                result = rpcCommonService.rpcRefundOrderService.updateStatus4Ing(refundOrderId, "");
                if(result != 1) {
                    _log.warn("更改退款为退款中({})失败,不能退款.refundOrderId={}", PayConstant.REFUND_STATUS_REFUNDING, refundOrderId);
                    return ApiBuilder.bizError("支付中心退款失败");
                }

                JSONObject retObj;
                try{
                    RefundInterface refundInterface = (RefundInterface) SpringUtil.getBean(channelType + "RefundService");
                    retObj = refundInterface.refund(refundOrder);
                }catch (BeansException e) {
                    _log.warn("不支持的退款渠道,停止退款处理.refundOrderId={},channelType={}", refundOrderId, channelType);
                    return ApiBuilder.bizError("支付中心退款失败");
                }

                if(PayConstant.retIsSuccess(retObj) && retObj.getBooleanValue("isSuccess")) {
                    // 更新退款状态为成功
                    // TODO 考虑事务内完成
                    String channelOrderNo = retObj.getString("channelOrderNo");
                    result = rpcCommonService.rpcRefundOrderService.updateStatus4Success(refundOrderId, channelOrderNo);
                    _log.info("更新退款订单状态为成功({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_SUCCESS, refundOrderId, result);

                    PayOrder updatePayOrder = new PayOrder();
                    updatePayOrder.setPayOrderId(refundOrder.getPayOrderId());
                    updatePayOrder.setStatus(PayConstant.PAY_STATUS_REFUND);    // 状态修改为已退款
                    updatePayOrder.setIsRefund(MchConstant.PUB_YES);
                    updatePayOrder.setRefundTimes(1);
                    updatePayOrder.setSuccessRefundAmount(refundOrder.getRefundAmount());
                    result = rpcCommonService.rpcPayOrderService.updateByPayOrderId(refundOrder.getPayOrderId(), updatePayOrder);
                    _log.info("更新支付订单退款信息,payOrderId={},返回结果:{}", refundOrder.getPayOrderId(), result);
                }else {
                    // 更新退款状态为失败
                    String channelErrCode = retObj.getString("channelErrCode");
                    String channelErrMsg = retObj.getString("channelErrMsg");
                    result = rpcCommonService.rpcRefundOrderService.updateStatus4Fail(refundOrderId, channelErrCode, channelErrMsg);
                    _log.info("更新退款订单状态为失败({}),refundOrderId={},返回结果:{}", PayConstant.REFUND_STATUS_FAIL, refundOrderId, result);
                }

                RefundOrderRes refundOrderRes = ApiBuilder.buildSuccess(RefundOrderRes.class);
                RefundOrder order = rpcCommonService.rpcRefundOrderService.findByRefundOrderId(refundOrderId);
                if (order == null) {
                    return ApiBuilder.bizError("退货订单ID" + refundOrderId + "没有找到对应的退货订单记录");
                }
                refundOrderRes.setByOrder(order);
                refundOrderRes.autoGenSign(refundContext.getString("key"));
                return refundOrderRes;

            }
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
    private Object validateParams(JSONObject params, JSONObject refundContext) {
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String payOrderId = params.getString("payOrderId");     // 支付订单号
        String mchOrderNo = params.getString("mchOrderNo");     // 商户支付单号
        String mchRefundNo = params.getString("mchRefundNo"); 	// 商户退款单号
        String amount = params.getString("amount"); 		    // 退款金额（单位分）
        String currency = params.getString("currency");         // 币种
        String clientIp = params.getString("clientIp");	        // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 退款结果回调URL,如果填写则退款结果会通过该url通知
        String sign = params.getString("sign"); 				// 签名
        String channelUser = params.getString("channelUser");	// 渠道用户标识,如微信openId,支付宝账号
        String userName = params.getString("userName");	        // 用户姓名
        String remarkInfo = params.getString("remarkInfo");	    // 备注

        // 验证请求参数有效性（必选项）
        if(!NumberUtils.isDigits(mchId)) {
            return "request params[mchId] error.";
        }
        Long mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(payOrderId) && StringUtils.isBlank(mchOrderNo)) {
            return "request params[payOrderId,mchOrderNo] error.";
        }

        if(StringUtils.isBlank(mchRefundNo)) {
            return "request params[mchRefundNo] error.";
        }
        if(!NumberUtils.isDigits(amount)) {
            return "request params[amount] error.";
        }
        Long refundAmount = Long.parseLong(amount); // 退款金额
        if(StringUtils.isBlank(currency)) {
            return "request params[currency] error.";
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            return "request params[sign] error.";
        }

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            return "Can't found mchInfo[mchId="+mchId+"] record in db.";
        }

        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            return "mchInfo not available [mchId="+mchId+"] record in db.";
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            return "key is null[mchId="+mchId+"] record in db.";
        }
        refundContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            return "Verify XX refund sign failed.";
        }

        //验证订单号是否重复
        Integer mchRefundOrderCount = rpcCommonService.rpcRefundOrderService.count(new LambdaQueryWrapper<RefundOrder>()
        .eq(RefundOrder::getMchId, mchIdL).eq(RefundOrder::getMchRefundNo, mchRefundNo));
        if(mchRefundOrderCount > 0){
            return "商户退款单号["+mchRefundNo+"]已存在.";
        }

        // 验证支付订单是否存在
        PayOrder payOrder = payOrderService.query(Long.parseLong(mchId), payOrderId, mchOrderNo);
        if(payOrder == null) {
            return "payOrder is not exist.";
        }
        // 订单必须是成功或已退款 处理完成状态才可以退款
        if(payOrder.getStatus() != PayConstant.PAY_STATUS_SUCCESS && payOrder.getStatus() != PayConstant.PAY_STATUS_REFUND ) {
            return "payOrder can not refund.";
        }

        refundContext.put("channelType", payOrder.getChannelType());   //ifType
        // 如果已经退款,不能再次发起
        if(MchConstant.PUB_YES == payOrder.getIsRefund() &&
                (payOrder.getSuccessRefundAmount() != null && payOrder.getSuccessRefundAmount() >= payOrder.getAmount())) {
            return "payOrder already refunds.";
        }

        String channelPayOrderNo = payOrder.getChannelOrderNo();    // 渠道测支付单号
        Long payAmount = payOrder.getAmount();  // 全额退款

        // 如果通知地址不为空,则为异步退款
        if(StringUtils.isNotBlank(notifyUrl)){
            refundContext.put("async", true);
        }

        // 验证参数通过,返回退款对象
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setRefundOrderId(MySeq.getRefund());
        refundOrder.setPayOrderId(payOrder.getPayOrderId());
        refundOrder.setChannelPayOrderNo(channelPayOrderNo);
        refundOrder.setMchId(mchIdL);
        refundOrder.setMchType(mchInfo.getType());
        refundOrder.setMchRefundNo(mchRefundNo);
        refundOrder.setChannelType(payOrder.getChannelType());
        refundOrder.setChannelId(payOrder.getChannelId());
        refundOrder.setPayAmount(payAmount);
        refundOrder.setRefundAmount(refundAmount);
        refundOrder.setCurrency(currency);
        refundOrder.setClientIp(clientIp);
        refundOrder.setDevice(device);
        refundOrder.setChannelUser(channelUser);
        refundOrder.setUserName(userName);
        refundOrder.setRemarkInfo(remarkInfo);
        refundOrder.setExtra(extra);
        refundOrder.setChannelMchId(payOrder.getChannelMchId());
        refundOrder.setParam1(param1);
        refundOrder.setParam2(param2);
        refundOrder.setNotifyUrl(notifyUrl);
        refundOrder.setAppId(""); //数据库表结构不支持null
        return refundOrder;
    }

}
