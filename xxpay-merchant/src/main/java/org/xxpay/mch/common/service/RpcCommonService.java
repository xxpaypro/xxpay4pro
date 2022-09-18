package org.xxpay.mch.common.service;

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
    public ISettBankAccountService rpcSettBankAccountService;

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
    public IChannelConfigService rpcChannelConfigService;

    @Autowired
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public IMchQrCodeService rpcMchQrCodeService;

    @Autowired
    public ISysMessageService rpcSysMessageService;

    @Autowired
    public IBillService rpcMchBillService;

    @Autowired
    public IUserAccountService rpcUserAccountService;

    @Autowired
    public IAgentpayService rpcAgentpayService;

    @Autowired
    public ISysService rpcSysService;

    @Autowired
    public IAgentpayPassageService rpcAgentpayPassageService;

    @Autowired
    public IPayProductService rpcPayProductService;

    @Autowired
    public ICommonService rpcCommonService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayAgentpayService rpcXxPayAgentpayService;
    
    @Autowired
    public IMsgcodeService rpcMsgcodeService;
    
    @Autowired
    public ISysConfigService rpcSysConfigService;
    
    @Autowired
    public IAgentInfoService rpcAgentInfoService;

    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;

    @Autowired
    public IPayPassageService rpcPayPassageService;

    @Autowired
    public IPayInterfaceService rpcPayInterfaceService;

    @Autowired
    public IPayInterfaceTypeService rpcPayInterfaceTypeService;

    @Autowired
    public IPayTypeService rpcPayTypeService;

    @Autowired
    public IAgentpayPassageAccountService rpcAgentpayPassageAccountService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayTransService rpcXxPayTransService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IFeeScaleService rpcFeeScaleService;

    @Autowired
    public IFeeRiskConfigService rpcFeeRiskConfigService;

    @Autowired
    public ISysLogService rpcSysLogService;

    @Autowired
    public IMchStoreService rpcMchStoreService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayAlipayApiService rpcXxPayAlipayApiService;

    @Reference(version = Constant.XXPAY_SERVICE_VERSION, timeout = Constant.DUBBO_TIMEOUT, retries = Constant.DUBBO_RETRIES, check = false)
    public IXxPayWxpayApiService rpcXxPayWxpayApiService;

    @Autowired
    public IMemberService rpcMemberService;

    @Autowired
    public IMemberBalanceService rpcMemberBalanceService;

    @Autowired
    public IMemberPointsService rpcMemberPointsService;

    @Autowired
    public IMemberBalanceHistoryService rpcMemberBalanceHistoryService;

    @Autowired
    public IMemberPointsHistoryService rpcMemberPointsHistoryService;

    @Autowired
    public IMemberCouponService rpcMemberCouponService;

    @Autowired
    public IMchCouponService rpcMchCouponService;

    @Autowired
    public IMchPointsGoodsService rpcMchPointsGoodsService;

    @Autowired
    public IMchMemberRechargeRuleService rpcMchMemberRechargeRuleService;

    @Autowired
    public IMchMemberConfigService rpcMchMemberConfigService;

    @Autowired
    public IMemberGoodsExchangeService rpcMemberGoodsExchangeService;

    @Autowired
    public IMchPointsGoodsStoreRelaService rpcMchPointsGoodsStoreRelaService;

    @Autowired
    public IMchCouponStoreRelaService rpcMchCouponStoreRelaService;

    @Autowired
    public IMchDeviceService rpcMchDeviceService;
	
	@Autowired
    public IMchRefundOrderService rpcMchRefundOrderService;

	@Autowired
    public IIsvInfoService rpcIsvInfoService;

	@Autowired
    public IIsvSpeakerConfigService rpcIsvSpeakerConfigService;

	@Autowired
    public IMchStoreSpeakerService rpcMchStoreSpeakerService;

    @Autowired
    public IMchStorePrinterService rpcMchStorePrinterService;

    @Autowired
    public IIsvPrinterConfigService rpcIsvPrinterConfigService;

    @Autowired
    public IMchAppConfigService rpcMchAppConfigService;

    @Autowired
    public IIsvAdvertConfigService rpcIsvAdvertConfigService;

    @Autowired
    public ISysClientVersionService rpcSysClientVersionService;

    @Autowired
    public IMchOperatorHandoverService rpcMchOperatorHandoverService;

    @Autowired
    public ISysProvinceCodeService rpcSysProvinceCodeService;

    @Autowired
    public ISysCityCodeService rpcSysCityCodeService;

    @Autowired
    public ISysAreaCodeService rpcSysAreaCodeService;

    @Autowired
    public IMchGoodsService rpcMchGoodsService;

    @Autowired
    public IMchGoodsCategoryService rpcMchGoodsCategoryService;

    @Autowired
    public IMchGoodsPropsCategoryService rpcMchGoodsPropsCategoryService;

    @Autowired
    public IMchGoodsPropsService rpcMchGoodsPropsService;

    @Autowired
    public IMchShoppingCartService rpcMchShoppingCartService;

    @Autowired
    public IMchReceiveAddressService rpcMchReceiveAddressService;

    @Autowired
    public IMchReturnReasonService rpcMchReturnReasonService;

    @Autowired
    public IMchQuestionService rpcMchQuestionService;

    @Autowired
    public IMchGoodsPropsCategoryRelaService rpcMchGoodsPropsCategoryRelaService;

    @Autowired
    public IMchTradeOrderDetailService rpcMchTradeOrderDetailService;

    @Autowired
    public IMchGoodsStoreRelaService rpcMchGoodsStoreRelaService;

    @Autowired
    public IMchStoreTakeoutService rpcMchStoreTakeoutService;

    @Autowired
    public IMchStoreAreaManageService rpcMchStoreAreaManageService;

    @Autowired
    public IMchStoreBannerService rpcMchStoreBannerService;

    @Autowired
    public IMchStoreBannerRelaService rpcMchStoreBannerRelaService;

    @Autowired
    public IMchWxauthInfoService rpcMchWxauthInfoService;

    @Autowired
    public IMchMiniVajraService rpcMchMiniVajraService;

    @Autowired
    public IMchMiniConfigService rpcMchMiniConfigService;

    @Autowired
    public IMchTradeOrderAfterSaleService rpcMchTradeOrderAfterSaleService;

    @Autowired
    public IIsvWx3rdInfoService rpcIsvWx3rdInfoService;

    @Autowired
    public ISysArticleService rpcSysArticleService;

    @Autowired
    public IMchTradeOrderBatchHourService rpcMchTradeOrderBatchHourService;

    @Autowired
    public IMchTradeOrderBatchService rpcMchTradeOrderBatchService;

    @Autowired
    public IMchTradeOrderBatchMonthService rpcMchTradeOrderBatchMonthService;

    @Autowired
    public IMchHospitalService rpcMchHospitalService;

}
