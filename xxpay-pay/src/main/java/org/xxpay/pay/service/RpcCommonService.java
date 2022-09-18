package org.xxpay.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.service.*;

/**
 * @author: dingzhiwei
 * @date: 17/9/10
 * @description:
 */
@Service
public class RpcCommonService {

    @Autowired
    public IMchInfoService rpcMchInfoService;

    @Autowired
    public IPayOrderService rpcPayOrderService;

    @Autowired
    public ITransOrderService rpcTransOrderService;

    @Autowired
    public IRefundOrderService rpcRefundOrderService;

    @Autowired
    public IMchAppService rpcMchAppService;

    @Autowired
    public IMchNotifyService rpcMchNotifyService;

    @Autowired
    public IUserAccountService rpcUserAccountService;

    @Autowired
    public IAgentpayPassageAccountService rpcAgentpayPassageAccountService;

    @Autowired
    public IPayPassageService rpcPayPassageService;

    @Autowired
    public IPayProductService rpcPayProductService;

    @Autowired
    public IAgentpayPassageService rpcAgentpayPassageService;

    @Autowired
    public IAccountHistoryService rpcAccountHistoryService;

    @Autowired
    public ISettBankAccountService rpcSettBankAccountService;

    @Autowired
    public IBankCardBinService rpcBankCardBinService;

    @Autowired
    public IAgentInfoService rpcAgentInfoService;

    @Autowired
    public IPayInterfaceService rpcPayInterfaceService;

    @Autowired
    public IAgentpayService rpcAgentpayService;
    
    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;
    
    @Autowired
    public IPayOrderExtService rpcPayOrderExtService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IFeeScaleService rpcFeeScaleService;

    @Autowired
    public IMchQrCodeService rpcMchQrCodeService;

    @Autowired
    public IMchTradeOrderService rpcMchTradeOrderService;

    @Autowired
    public IIsvInfoService rpcIsvInfoService;

    @Autowired
    public IPayInterfaceTypeService rpcPayInterfaceTypeService;

    @Autowired
    public IMemberService rpcMemberService;

    @Autowired
    public IMemberBalanceService rpcMemberBalanceService;

    @Autowired
    public IMemberCouponService rpcMemberCouponService;

    @Autowired
    public IMchCouponService rpcMchCouponService;

    @Autowired
    public IMchStoreService rpcMchStoreService;

    @Autowired
    public ISysService rpcSysService;

    @Autowired
    public IMchInfoExtService rpvMchInfoExtService;

    @Autowired
    public IWxMchSnapshotService rpcWxMchSnapshotService;

    @Autowired
    public IWxMchUpgradeSnapshotService rpcWxMchUpgradeSnapshotService;

    @Autowired
    public IAlipayMchSnapshotService rpcAlipayMchSnapshotService;

    @Autowired
    public ISxfMchSnapshotService rpcSxfMchSnapshotService;

    @Autowired
    public IMchRefundOrderService rpcMchRefundOrderService;


}
