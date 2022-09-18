package org.xxpay.task.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.service.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/04
 * @description:
 */
@Service
public class RpcCommonService {

    @Autowired
    public IMchInfoService rpcMchInfoService;

    @Autowired
    public IAgentInfoService rpcAgentInfoService;

    @Autowired
    public IPayOrderService rpcPayOrderService;

    @Autowired
    public ICheckService rpcCheckService;

    @Autowired
    public IBillService rpcMchBillService;

    @Autowired
    public IPayProductService rpcPayProductService;
    
    @Autowired
    public IOrderProfitDetailService rpcOrderProfitDetailService;
    
    @Autowired
    public IAgentpayService rpcAgentpayService;

    @Autowired
    public IAccountBalanceService rpcAccountBalanceService;

    @Autowired
    public IIsvSettConfigService rpcIsvSettConfigService;

    @Autowired
    public IOrderProfitTaskService rpcOrderProfitTaskService;

}
