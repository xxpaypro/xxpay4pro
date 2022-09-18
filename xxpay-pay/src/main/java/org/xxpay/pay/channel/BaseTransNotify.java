package org.xxpay.pay.channel;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.AgentpayPassageAccount;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.mq.BaseNotify4MchTrans;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 18/08/16
 * @description:
 */
@Component
public abstract class BaseTransNotify extends BaseService implements TransNotifyInterface {

    @Autowired
    public RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    public abstract String getChannelName();

    public JSONObject doNotify(Object notifyData) {
        return null;
    }

    /**
     * 获取三方支付配置信息
     * 如果是平台账户,则使用平台对应的配置,否则使用商户自己配置的渠道
     * @param transOrder
     * @return
     */
    public String getTransParam(TransOrder transOrder) {
        String payParam = "";
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(transOrder.getPassageAccountId());
        if(agentpayPassageAccount != null && agentpayPassageAccount.getStatus() == MchConstant.PUB_YES) {
            payParam = agentpayPassageAccount.getParam();
        }
        if(StringUtils.isBlank(payParam)) {
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_ACCOUNT_NOT_EXIST);
        }
        return payParam;
    }

}
