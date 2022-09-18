package org.xxpay.manage.common.service;

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
    public ISettRecordService rpcSettRecordService;

    @Autowired
    public IMchSettBatchRecordService rpcMchSettBatchRecordService;

    @Autowired
    public IPayOrderService rpcPayOrderService;

    @Autowired
    public ITransOrderService rpcTransOrderService;

    @Autowired
    public IRefundOrderService rpcRefundOrderService;

    @Autowired
    public IMchAppService rpcMchAppService;

    @Autowired
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public IMchQrCodeService rpcMchQrCodeService;

    @Autowired
    public ISysService rpcSysService;

    @Autowired
    public IMchNotifyService rpcMchNotifyService;

    @Autowired
    public IChannelConfigService rpcChannelConfigService;

    @Autowired
    public ISysMessageService rpcSysMessageService;

    @Autowired
    public ICheckService rpcCheckService;

    @Autowired
    public IBillService rpcMchBillService;

    @Autowired
    public IAgentInfoService rpcAgentInfoService;

    @Autowired
    public IPayInterfaceTypeService rpcPayInterfaceTypeService;

    @Autowired
    public IPayInterfaceService rpcPayInterfaceService;

    @Autowired
    public IAgentpayPassageAccountService rpcAgentpayPassageAccountService;

    @Autowired
    public IAgentpayPassageService rpcAgentpayPassageService;

    @Autowired
    public IPayPassageService rpcPayPassageService;

    @Autowired
    public IPayProductService rpcPayProductService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayTransService rpcXxPayTransService;

    @Autowired
    public IAgentpayService rpcAgentpayService;

    @Autowired
    public IBankCardBinService rpcBankCardBinService;

    @Autowired
    public ISysConfigService rpcSysConfigService;

    @Autowired
    public IPayTypeService rpcPayTypeService;

    @Autowired
    public ICommonService rpcCommonService;
    
    @Autowired
    public ISettBankAccountService rpcSettBankAccountService;
    
    @Autowired
    public IPayInterfaceTypeTemplateService rpcPayInterfaceTypeTemplateService;

    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IFeeScaleService rpcFeeScaleService;

    @Autowired
    public IFeeRiskConfigService rpcFeeRiskConfigService;

    @Autowired
    public ISysLogService rpcSysLogService;

    @Autowired
    public IIsvInfoService rpcIsvInfoService;

    @Autowired
    public ISysClientVersionService sysClientVersionService;

    @Autowired
    public IMemberService rpcMemberService;

}
