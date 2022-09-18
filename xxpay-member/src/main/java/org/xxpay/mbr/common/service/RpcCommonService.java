package org.xxpay.mbr.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public ISysService rpcSysService;

    @Autowired
    public ISysLogService rpcSysLogService;

    @Autowired
    public ICommonService rpcCommonService;

    @Autowired
    public IMchStoreService rpcMchStoreService;

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
    public IMchRefundOrderService rpcMchRefundOrderService;

    @Autowired
    public IPayPassageService rpcPaypassageService;

    @Autowired
    public ISysConfigService rpcSysConfigService;

    @Autowired
    public IMchGoodsService rpcMchGoodsService;

    @Autowired
    public IMchGoodsCategoryService rpcMchGoodsCategoryService;

    @Autowired
    public IMchShoppingCartService rpcMchShoppingCartService;

    @Autowired
    public IMchReceiveAddressService rpcMchReceiveAddressService;

    @Autowired
    public IMchReturnReasonService rpcMchReturnReasonService;

    @Autowired
    public IMchQuestionService rpcMchQuestionService;

    @Autowired
    public IMchGoodsPropsService rpcMchGoodsPropsService;

    @Autowired
    public IMchGoodsPropsCategoryRelaService rpcMchGoodsPropsCategoryRelaService;

    @Autowired
    public IMchTradeOrderDetailService rpcMchTradeOrderDetailService;

    @Autowired
    public ISysProvinceCodeService rpcSysProvinceCodeService;

    @Autowired
    public ISysCityCodeService rpcSysCityCodeService;

    @Autowired
    public ISysAreaCodeService rpcSysAreaCodeService;

    @Autowired
    public IMchStoreTakeoutService rpcMchStoreTakeoutService;

    @Autowired
    public IIsvWx3rdInfoService rpcIsvWx3rdInfoService;

    @Autowired
    public IMchWxauthInfoService rpcMchWxauthInfoService;

    @Autowired
    public IMemberOpenidRelaService rpcMemberOpenidRelaService;

    @Autowired
    public IMchStoreBannerService rpcMchStoreBannerService;

    @Autowired
    public IMchStoreAreaManageService rpcMchStoreAreaManageService;

    @Autowired
    public IMchMiniVajraService rpcMchMiniVajraService;

    @Autowired
    public IMchMiniConfigService rpcMchMiniConfigService;

    @Autowired
    public IMchTradeOrderAfterSaleService rpcMchTradeOrderAfterSaleService;

    @Autowired
    public ISysArticleService rpcSysArticleService;

}
