package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;


import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.core.entity.OrderProfitDetail;

import java.util.List;
import java.util.Map;

public interface IAccountHistoryService extends IService<AccountHistory> {

    AccountHistory findById(Long id);

    AccountHistory findById(byte infoType, Long infoId, Long id);

    AccountHistory findByOrderId(byte infoType, String bizOrderId);


    List<AccountHistory> select(int offset, int limit, AccountHistory accountHistory);

    int count(AccountHistory accountHistory);



    //以下为mch方法

    Map selectSettDailyCollect4Mch(Long mchId, String collDate, byte fundDirection, int riskDay);

    void updateCompleteSett4Mch(Long mchId, String collDate, int riskDay);

    Map selectSettDailyCollect4Agent(Long agentId, String collDate, byte fundDirection, int riskDay, Byte bizType, String bizItem);

    void updateCompleteSett4Agent(Long agentId, String collDate, int riskDay, Byte bizType, String bizItem);

    /**
     * 查询代理商在风险预存期内未结算的记录
     */
    List<AccountHistory> selectNotSettCollect4Agent(Long agentId, String collDate);

    Map count4Data2(Long mchId, Long agentId, String orderId, Byte bizType, String createTimeStart,
                    String createTimeEnd);

    List<Map> count4AgentTop(Long agentId, String bizType, String createTimeStart, String createTimeEnd);


    Map count4Data(Byte bizType);

    List<Map> count4Data3();







    //以下为agent 方法


    /**
     * 统计代理商分润
     * @return
     */
    List<Map> count4AgentProfit(Long agentId);
}
