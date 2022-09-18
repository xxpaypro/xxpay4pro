package org.xxpay.manage.settlement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.manage.common.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
public class SettService {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 同步转账
     * @param mchInfo
     * @param settRecord
     * @return
     */
    /*public int excuteTrans(MchInfo mchInfo, SettRecord settRecord) {
        // 先更新为打款中

        int count = rpcCommonService.rpcSettRecordService.remit(settRecord.getId(), MchConstant.SETT_STATUS_REMIT_ING, "", "");
        // 向支付中心发起转账申请
        JSONObject resultObj = createTransOrder(mchInfo,  mchAgentpayRecord);
        if(resultObj == null) {
            _log.info("发起转账失败,支付中心没有返回结果.agentpayOrderId={}", mchAgentpayRecord.getAgentpayOrderId());
            rpcCommonService.rpcMchAgentpayService.updateStatus4Fail(mchAgentpayRecord.getAgentpayOrderId(), null);
            // 解冻金额
            rpcCommonService.rpcMchAccountService.unFreezeSettAmount(mchInfo.getMchId(), mchAgentpayRecord.getAmount());
            return 0;
        }
        String transOrderId = resultObj.getString("transOrderId");
        Byte transStatus = resultObj.getByte("status");
        if(transStatus == null || PayConstant.AGENTPAY_STATUS_FAIL == transStatus) {
            rpcCommonService.rpcMchAgentpayService.updateStatus4Fail(mchAgentpayRecord.getAgentpayOrderId(), transOrderId);
            // 解冻金额
            rpcCommonService.rpcMchAccountService.unFreezeSettAmount(mchInfo.getMchId(), mchAgentpayRecord.getAmount());
            return 0;
        }else if(PayConstant.AGENTPAY_STATUS_SUCCESS == transStatus) {
            result = rpcCommonService.rpcMchAgentpayService.updateStatus4Success(mchAgentpayRecord.getAgentpayOrderId(), transOrderId);
            // 解冻+减款
            rpcCommonService.rpcMchAccountService.unFreezeAmount(mchInfo.getMchId(), mchAgentpayRecord.getAmount(), mchAgentpayRecord.getAgentpayOrderId(), MchConstant.BIZ_TYPE_REMIT);
        }
        return result;
    }*/

    /**
     * 创建转账订单
     * @param mchInfo
     * @param settRecord
     * @return
     */
    /*private JSONObject createTransOrder(MchInfo mchInfo, SettRecord settRecord) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchInfo.getMchId());                      // 商户ID
        paramMap.put("mchTransNo", mchAgentpayRecord.getAgentpayOrderId());      // 商户代付单号
        paramMap.put("channelId", mchAgentpayRecord.getChannelId());    // 代付渠道
        paramMap.put("passageId", mchAgentpayRecord.getPassageId());    // 支付通道,平台账户需要传递
        paramMap.put("amount", mchAgentpayRecord.getRemitAmount());     // 转账金额,单位分
        paramMap.put("currency", "cny");                                // 币种, cny-人民币
        paramMap.put("clientIp", IPUtility.getLocalIP());               // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                                  // 设备
        //paramMap.put("notifyUrl", notifyUrl);                         // 回调URL
        paramMap.put("param1", "");                                     // 扩展参数1
        paramMap.put("param2", "");                                     // 扩展参数2
        paramMap.put("remarkInfo", mchAgentpayRecord.getRemark());
        paramMap.put("accountName", mchAgentpayRecord.getAccountName());
        paramMap.put("accountNo", mchAgentpayRecord.getAccountNo());
        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("xxpay_req:{}", reqData);
        String url = mainConfig.getPayUrl() + "/trans/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        _log.info("xxpay_res:{}", result);
        JSONObject retObj = JSON.parseObject(result);
        if(XXPayUtil.isSuccess(retObj)) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retObj, mchInfo.getPrivateKey(), "sign");
            String retSign = (String) retObj.get("sign");
            if(checkSign.equals(retSign)) return retObj;
            _log.info("验签失败:retSign={},checkSign={}", retSign, checkSign);
            return retObj;
        }
        return null;
    }*/

}
