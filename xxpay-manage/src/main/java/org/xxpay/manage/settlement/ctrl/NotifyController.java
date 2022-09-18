package org.xxpay.manage.settlement.ctrl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettRecord;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/04/21
 * @description: 代付操作
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/notify")
public class NotifyController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(NotifyController.class);

    /**
     * 接收支付中心转账通知
     * @param response
     * @throws Exception
     */
    @RequestMapping("/sett")
    @ResponseBody
    public String payNotify( HttpServletResponse response) throws Exception {
        _log.info("====== 开始处理支付中心转账通知 ======");
        Map<String,Object> paramMap = request2payResponseMap(request, new String[]{
                "transOrderId", "mchId", "appId", "mchOrderNo", "amount", "status",
                "result", "channelOrderNo", "channelErrMsg", "param1",
                "param2","transSuccTime", "sign"
        });
        String resStr;
        _log.info("[XxPay_Trans_Notify], paramMap={}", paramMap);
        String transOrderId = (String) paramMap.get("transOrderId");
        String mchOrderNo = (String) paramMap.get("mchOrderNo");
        String channelErrMsg = (String) paramMap.get("channelErrMsg");
        Object object = verifyPayResponse(paramMap);
        if(object instanceof String) {
            String errorMessage = "verify request param failed. " + object;
            _log.warn(errorMessage);
            resStr = object.toString();
        }else {
            SettRecord settRecord = (SettRecord) object;
            try {
                if(settRecord.getSettStatus() == MchConstant.SETT_STATUS_REMIT_SUCCESS) {
                    return "success";
                }
                String transStatusStr = paramMap.get("status").toString();
                Byte transStatus = Byte.parseByte(transStatusStr);
                long id = settRecord.getId();
                int result = 0;
                if(transStatus == null || PayConstant.REFUND_STATUS_FAIL == transStatus) {  // 明确转账失败
                    // 更新打款失败
                    result = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_FAIL, null, null, null, channelErrMsg);
                }else if(PayConstant.TRANS_STATUS_SUCCESS == transStatus ) { // 明确转成成功
                    // 更新打款成功
                    result = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_SUCCESS, null, null, null, null);
                }
                resStr = result == 1 ? "success" : "fail";
            }catch (Exception e) {
                resStr = "fail";
                _log.error(e, "执行业务异常,transOrderId=%s.mchOrderNo=%s", transOrderId, mchOrderNo);
            }
        }
        _log.info("[XxPay_Trans_Notify]: response={}, transOrderId={}, mchOrderNo={}", resStr, transOrderId, mchOrderNo);
        _log.info("====== 支付中心转账通知处理完成 ======");
        return resStr;
    }

    public Object verifyPayResponse(Map<String,Object> map) {
        String mchId = (String) map.get("mchId");
        String transOrderId = (String) map.get("transOrderId");
        String mchOrderNo = (String) map.get("mchOrderNo");
        String amount = (String) map.get("amount");
        String sign = (String) map.get("sign");

        String errMsg;
        if (StringUtils.isEmpty(mchId)) {
            errMsg = "参数mchId不能为空";
            _log.warn("{}. mchId={}", errMsg, mchId);
            return errMsg;
        }
        if (StringUtils.isEmpty(transOrderId)) {
            errMsg = "参数transOrderId不能为空";
            _log.warn("{}. transOrderId={}", errMsg, transOrderId);
            return errMsg;
        }
        if (StringUtils.isEmpty(amount) || !NumberUtils.isDigits(amount)) {
            errMsg = "参数amount不正确";
            _log.warn("{}. amount={}", amount, amount);
            return errMsg;
        }
        if (StringUtils.isEmpty(sign)) {
            errMsg = "参数sign不能为空";
            _log.warn("{}. sign={}", errMsg, sign);
            return errMsg;
        }

        // 验证签名
        if (!verifySign(map)) {
            errMsg = "验签失败";
            _log.warn("{}. transOrderId={}", errMsg, transOrderId);
            return errMsg;
        }

        // 查询业务订单, 验证订单是否存在
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.findBySettOrderId(mchOrderNo);
        if(settRecord == null) {
            errMsg = "业务订单不存在";
            _log.warn("{},transOrderId={},mchOrderNo={}", errMsg, transOrderId, mchOrderNo);
            return errMsg;
        }
        // 核对金额
        if(settRecord.getSettAmount() != Long.parseLong(amount)) {
            errMsg = "金额不一致";
            _log.warn("{},dbTransPrice={},payPrice={}", errMsg, settRecord.getSettAmount(), amount);
            return errMsg;
        }
        return settRecord;
    }

    public boolean verifySign(Map<String, Object> map) {
        Long mchId = Long.parseLong(map.get("mchId").toString());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);

        String key = null;
        if(mchInfo != null) {
            key = mchInfo.getPrivateKey();
        }else if(mchId.toString().startsWith("1")) { // TODO 临时处理,判断为代理商
            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(mchId);
        }
        if(key == null) {
            _log.warn("privateKey not exist. mchId={}", mchId);
            return false;
        }

        String localSign = PayDigestUtil.getSign(map, key, "sign");
        String sign = (String) map.get("sign");
        return localSign.equalsIgnoreCase(sign);
    }

}
