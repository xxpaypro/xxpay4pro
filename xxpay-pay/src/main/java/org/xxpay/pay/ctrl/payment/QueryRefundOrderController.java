package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.RefundOrderQueryRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchApp;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.mq.BaseNotify4MchRefund;
import org.xxpay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 退款订单查询
 * @author dingzhiwei jmdhappy@126.com
 * @date 2014-04-16
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class QueryRefundOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryRefundOrderController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private BaseNotify4MchRefund baseNotify4MchRefund;

    /**
     * 查询退款订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/refund/query_order")
    public AbstractRes queryPayOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户查询退款订单请求 ######");
        String logPrefix = "【商户退款订单查询】";
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
            String mchRefundNo = po.getString("mchRefundNo"); 	    // 商户退款单号
            String refundOrderId = po.getString("refundOrderId"); 	// 退款单号
            Boolean executeNotify = po.getBooleanValue("executeNotify");   // 是否执行回调

            RefundOrder refundOrder = null;
            if(StringUtils.isNotBlank(mchRefundNo)) {
                refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndMchRefundNo(mchId, mchRefundNo);
            }else if(StringUtils.isNotBlank(refundOrderId)) {
                refundOrder = rpcCommonService.rpcRefundOrderService.selectByMchIdAndRefundOrderId(mchId, refundOrderId);
            }
            _log.info("{}查询退款订单,结果:{}", logPrefix, refundOrder);
            if (refundOrder == null) {
                return ApiBuilder.bizError("退款订单不存在");
            }
            if(executeNotify &&
                    StringUtils.isNotBlank(refundOrder.getNotifyUrl()) && PayConstant.REFUND_RESULT_SUCCESS == refundOrder.getStatus()  ) {
                baseNotify4MchRefund.doNotify(refundOrder, false);
                _log.info("业务查单完成,并再次发送业务退款通知.发送完成");
            }

            RefundOrderQueryRes refundOrderQueryRes = ApiBuilder.buildSuccess(RefundOrderQueryRes.class);
            refundOrderQueryRes.setByOrder(refundOrder);
            refundOrderQueryRes.autoGenSign(payContext.getString("key")); //自动签名
            _log.info("###### 商户查询订单处理完成 ######");

            return refundOrderQueryRes;


        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("支付中心系统异常");
        }
    }

    /**
     * 验证请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			        // 商户ID
        String mchRefundNo = params.getString("mchRefundNo"); 	    // 商户退款单号
        String refundOrderId = params.getString("refundOrderId"); 	// 退款订单号

        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(mchRefundNo) && StringUtils.isBlank(refundOrderId)) {
            errorMessage = "request params[mchRefundNo or refundOrderId] error.";
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
