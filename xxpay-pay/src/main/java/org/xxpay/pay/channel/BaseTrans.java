package org.xxpay.pay.channel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.entity.*;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Component
public abstract class BaseTrans extends BaseService implements TransInterface {

    @Autowired
    protected RpcCommonService rpcCommonService;

    @Autowired
    protected PayConfig payConfig;

    public abstract String getChannelName();

    public QueryRetMsg trans(TransOrder transOrder) {
        return null;
    }

    public QueryRetMsg query(TransOrder transOrder) {
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
