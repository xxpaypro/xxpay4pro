package org.xxpay.agent.common.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.service.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/05
 * @description:
 */
@Service
public class RpcCommonService {

    @Autowired
    public IMchInfoService rpcMchInfoService;

    @Autowired
    public IAccountHistoryService rpcAccountHistoryService;

    @Autowired
    public ISysMessageService rpcSysMessageService;

    @Autowired
    public ISysService rpcSysService;

    @Autowired
    public IAgentInfoService rpcAgentInfoService;

    @Autowired
    public IPayProductService rpcPayProductService;

    @Autowired
    public IPayOrderService rpcPayOrderService;

    @Autowired
    public ISettRecordService rpcSettRecordService;

    @Autowired
    public IAgentpayPassageService rpcAgentpayPassageService;
    
    @Autowired
    public ISysConfigService rpcSysConfigService;
    
    @Autowired
    public IPayPassageService rpcPayPassageService;
    
    @Autowired
    public IAgentpayPassageAccountService rpcAgentpayPassageAccountService;

    @Autowired
    public IMsgcodeService rpcMsgcodeService;
    
    @Autowired
    public ISettBankAccountService rpcSettBankAccountService;

    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;

    @Autowired
    public IAgentpayService rpcAgentpayService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayTransService rpcXxPayTransService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayAgentpayService rpcXxPayAgentpayService;

    @Autowired
    public IBillService rpcBillService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IFeeScaleService rpcFeeScaleService;

    @Autowired
    public ISysLogService rpcSysLogService;

    @Autowired
    public IOrderProfitTaskService rpcOrderProfitTaskService;

    @Autowired
    public ISysProvinceCodeService rpcSysProvinceCodeService;

    @Autowired
    public ISysCityCodeService rpcSysCityCodeService;

    @Autowired
    public ISysAreaCodeService rpcSysAreaCodeService;

    @Autowired
    public ISysIndustryCodeService rpcSysIndustryCodeService;

    @Autowired
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public IIsvSettConfigService rpcIsvSettConfigService;

    @Autowired
    public IMchInfoExtService rpcMchInfoExtService;

}
