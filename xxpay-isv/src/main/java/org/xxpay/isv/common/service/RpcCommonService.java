package org.xxpay.isv.common.service;

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
    public ISysConfigService rpcSysConfigService;
    
    @Autowired
    public IPayPassageService rpcPayPassageService;

    @Autowired
    public IMsgcodeService rpcMsgcodeService;
    
    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;

    @Autowired
    public IAgentpayService rpcAgentpayService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IFeeScaleService rpcFeeScaleService;

    @Autowired
    public ISysLogService rpcSysLogService;

    @Autowired
    public IIsvInfoService rpcIsvInfoService;

    @Autowired
    public IPayInterfaceTypeService rpcPayInterfaceTypeService;

    @Autowired
    public IIsvSpeakerConfigService rpcIsvSpeakerConfigService;

    @Autowired
    public IIsvPrinterConfigService rpcIsvPrinterConfigService;

    @Autowired
    public IMchNotifyService rpcMchNotifyService;

    @Autowired
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public ICommonService rpcCommonService;

    @Autowired
    public ISettBankAccountService rpcSettBankAccountService;

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
    public IIsvAdvertConfigService rpcIsvAdvertConfigService;

    @Autowired
    public IPayInterfaceService rpcPayInterfaceService;

    @Autowired
    public IIsvSettConfigService rpcIsvSettConfigService;

    @Autowired
    public IIsvWx3rdInfoService rpcIsvWx3rdInfoService;

    @Autowired
    public IMchWxauthInfoService rpcMchWxauthInfoService;

    @Autowired
    public IMemberOpenidRelaService rpcMemberOpenidRelaService;

    @Autowired
    public IIsvDeviceService rpcIsvDeviceService;

    @Autowired
    public IMchStoreService rpcMchStoreService;

    @Autowired
    public IMchInfoExtService rpcMchInfoExtService;

    @Autowired
    public IWxMchSnapshotService rpcWxMchSnapshotService;

    @Autowired
    public IWxMchUpgradeSnapshotService rpcWxMchUpgradeSnapshotService;

    @Autowired
    public IAlipayMchSnapshotService rpcAlipayMchSnapshotService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT_LONG, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayWxpayApiService rpcXxPayWxpayApiService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT_LONG, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPaySxfpayApiService rpcXxPaySxfpayApiService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT_LONG, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayAlipayApiService rpcXxPayAlipaypayApiService;

    @Autowired
    public ISysAlipayIndustryCodeService rpcSysAlipayIndustryCodeService;

    @Autowired
    public ISysWxpayIndustryCodeService rpcSysWxpayIndustryCodeService;

    @Autowired
    public IMchMiniVersionService rpcMchMiniVersionService;

    @Autowired
    public ISxfMchSnapshotService rpcSxfMchSnapshotService;

}
