package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.core.entity.AccountHistoryExample;

import java.util.List;
import java.util.Map;

public interface AccountHistoryMapper extends BaseMapper<AccountHistory> {
    int countByExample(AccountHistoryExample example);

    int deleteByExample(AccountHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountHistory record);

    int insertSelective(AccountHistory record);

    List<AccountHistory> selectByExample(AccountHistoryExample example);

    AccountHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountHistory record, @Param("example") AccountHistoryExample example);

    int updateByExample(@Param("record") AccountHistory record, @Param("example") AccountHistoryExample example);

    int updateByPrimaryKeySelective(AccountHistory record);

    int updateByPrimaryKey(AccountHistory record);

    //以下为 mchHistoryMapper 方法
    /**
     * 查询商户待结算汇总数据
     * @param param
     * @return
     */
    Map selectSettDailyCollect4Mch(Map param);

    /**
     * 查询代理商待结算汇总数据
     * @param param
     * @return
     */
    Map selectSettDailyCollect4Agent(Map param);

    /**
     * 更新商户结算状态
     * @param param
     */
    void updateCompleteSett4Mch(Map param);

    /**
     * 更新代理商结算状态
     * @param param
     */
    void updateCompleteSett4Agent(Map param);

    /**
     * 查询代理商风险预存期内的待结算记录
     * @param param
     * @return
     */
    List<AccountHistory> selectNotSettCollect4Agent(Map param);

    /**
     * 统计数据
     * @return
     */
    Map count4Data(Map param);

    /**
     * 统计数据
     * @param param
     * @return
     */
    Map count4Data2(Map param);

    /**
     * 代理商分润排行
     * @param param
     * @return
     */
    List<Map> count4AgentProfitTop(Map param);

    List<Map> count4Data3();


    /**
     * 统计代理商分润数据
     * @param param
     * @return
     */
    List<Map> count4AgentProfit(Map param);


}
