package org.xxpay.pay.channel.transfarpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.channel.BaseTransNotify;
import org.xxpay.pay.channel.transfarpay.util.MD5Util;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class TransfarpayTransNotifyService extends BaseTransNotify {

    private static final MyLog _log = MyLog.getLog(TransfarpayTransNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_TRANSFARPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理传化代付回调】";
        _log.info("====== 开始处理传化代付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        TransOrder transOrder;
        // 获取通知请求参数
        String subject = req.getParameter("subject");                                   // 商品名称
        String businessnumber = req.getParameter("businessnumber");                     // 业务流水号
        String businessrecordnumber = req.getParameter("businessrecordnumber");         // 支付订单号
        String status = req.getParameter("status");                                     // 交易状态:失败/成功/已退票
        String transactiondate = req.getParameter("transactiondate");                   // 交易时间,格式为:yyyy-MM-dd HH:mm:ss
        String billamount = req.getParameter("billamount");                             // 订单金额
        String transactionamount = req.getParameter("transactionamount");               // 交易金额
        String transactiontype = req.getParameter("transactiontype");                   // 交易类型
        String inputdate = req.getParameter("inputdate");                               // 交易创建时间,格式为:yyyy-MM-dd HH:mm:ss
        String frompartyid = req.getParameter("frompartyid");                           // 付款方会员id
        String fromaccountnumber = req.getParameter("fromaccountnumber");               // 付款方会员账号
        String remark = req.getParameter("remark");                                     // 结果说明
        String tf_sign = req.getParameter("tf_sign");                                   // 签名串字符串

        // 这些参数验证签名也需要,按照传化的demo使用
        Map<String, String> params = new HashMap<>();
        params.put("subject", subject);
        params.put("businessnumber", businessnumber);
        params.put("businessrecordnumber", businessrecordnumber);
        params.put("status", status);
        params.put("transactiondate", transactiondate);
        params.put("billamount", billamount);
        params.put("transactionamount", transactionamount);
        params.put("transactiontype", transactiontype);
        params.put("inputdate", inputdate);
        params.put("frompartyid", frompartyid);
        params.put("fromaccountnumber", fromaccountnumber);
        params.put("remark", remark);
        params.put("tf_sign", tf_sign);

        _log.info("{}通知参数:{}", logPrefix, params);
        String respString = "请求失败";
        try {
            payContext.put("parameters", params);
            if(!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, buildRet("验证数据没有通过"));
                return retObj;
            }
            transOrder = (TransOrder) payContext.get("transOrder");
            // 处理订单
            byte transStatus = transOrder.getStatus(); // 转账状态: 0-订单生成,1-转账中,2-转账成功,3-转账失败
            if(transStatus == PayConstant.TRANS_STATUS_INIT || transStatus == PayConstant.TRANS_STATUS_TRANING) {   // 只有转账状态为初始或处理中时再处理
                // 交易状态:失败/成功/已退票
                if("成功".equals(status)) {   // 成功
                    _log.info("{} >>> 转账成功", logPrefix);
                    int updateRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrder.getTransOrderId(), businessrecordnumber);
                    if (updateRows != 1) {
                        _log.error("{}更新转账状态失败,将transOrderId={},更新transStatus={}失败", logPrefix, transOrder.getTransOrderId(), PayConstant.TRANS_STATUS_SUCCESS);
                        retObj.put(PayConstant.RESPONSE_RESULT, buildRet("处理订单失败"));
                        return retObj;
                    }
                    _log.error("{}更新转账状态成功,将transOrderId={},更新transStatus={}成功", logPrefix, transOrder.getTransOrderId(), PayConstant.TRANS_STATUS_SUCCESS);
                    transOrder.setStatus(PayConstant.TRANS_STATUS_SUCCESS);
                }else if("失败".equals(status) || "已退票".equals(status)){ // 失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    int updateRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrder.getTransOrderId(), "", remark, businessrecordnumber);
                    if (updateRows != 1) {
                        _log.error("{}更新转账状态失败,将transOrderId={},更新transStatus={}失败", logPrefix, transOrder.getTransOrderId(), PayConstant.TRANS_STATUS_FAIL);
                        retObj.put(PayConstant.RESPONSE_RESULT, buildRet("处理订单失败"));
                        return retObj;
                    }
                    _log.error("{}更新转账状态成功,将transOrderId={},更新transStatus={}成功", logPrefix, transOrder.getTransOrderId(), PayConstant.TRANS_STATUS_FAIL);
                    transOrder.setStatus(PayConstant.TRANS_STATUS_FAIL);
                }
                // 业务系统异步通知
                baseNotify4MchTrans.doNotify(transOrder, true);
            }
            _log.info("====== 完成处理传化代付回调通知 ======");
            respString = "请求成功";
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, buildRet(respString));
        return retObj;
    }

    /**
     * 验证传化代付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext) {
        Map<String, String> params = (Map<String, String>)payContext.get("parameters");
        String errorMessage;
        // 查询payOrder记录
        String transOrderId = params.get("businessnumber");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        if (transOrder == null) {
            _log.error("[{}数据校验]金额不一致. transOrderId={}, db中无此订单", getChannelName(), transOrderId);
            payContext.put("retMsg", "订单不存在");
            return false;
        }
        TransfarpayConfig transfarpayConfig = new TransfarpayConfig(getTransParam(transOrder));

        params.put("appid", transfarpayConfig.getAppId());
        params.put("backurl", payConfig.getNotifyTransUrl(getChannelName()));

        // 验证签名
        String sign = params.get("tf_sign");
        try {
            boolean verify = MD5Util.verify(params, sign, transfarpayConfig.getDogSk(), "UTF-8");
            if(!verify) {
                errorMessage = "验证签名失败.";
                _log.error("[{}数据校验]{}", getChannelName(), errorMessage);
                payContext.put("retMsg", errorMessage);
                return false;
            }
        } catch (Exception e) {
            _log.error(e, "");
        }

        // 核对金额
        String billamount = params.get("billamount");
        long outTransAmt = new BigDecimal(billamount).multiply(new BigDecimal(100)).longValue();
        long dbTransAmt = transOrder.getAmount().longValue();
        if (outTransAmt != dbTransAmt) {
            _log.error("[{}数据校验]金额不一致. transOrderId={}, outTransAmt={}, dbTransAmt={}", getChannelName(), transOrderId, outTransAmt, dbTransAmt);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("transOrder", transOrder);
        return true;
    }

    /**
     * 生成回调同步响应数据
     * @param msg
     * @return
     */
    String buildRet(String msg) {
        JSONObject object = new JSONObject();
        if("请求成功".equals(msg)) {
            object.put("result", "success");
        }else {
            object.put("result", "error");
        }
        object.put("msg", msg);
        return object.toJSONString();
    }
}
