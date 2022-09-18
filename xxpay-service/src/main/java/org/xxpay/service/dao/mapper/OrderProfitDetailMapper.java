package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.OrderProfitDetailExample;

public interface OrderProfitDetailMapper extends BaseMapper<OrderProfitDetail> {
    long countByExample(OrderProfitDetailExample example);

    int deleteByExample(OrderProfitDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderProfitDetail record);

    int insertSelective(OrderProfitDetail record);

    List<OrderProfitDetail> selectByExample(OrderProfitDetailExample example);

    OrderProfitDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderProfitDetail record, @Param("example") OrderProfitDetailExample example);

    int updateByExample(@Param("record") OrderProfitDetail record, @Param("example") OrderProfitDetailExample example);

    int updateByPrimaryKeySelective(OrderProfitDetail record);

    int updateByPrimaryKey(OrderProfitDetail record);
    
    List<OrderProfitDetail> selectMchAndAgentNotSettProfitDetails();

    List<Map> selectAgentDetailInfoList(@Param("bizOrderId")String bizOrderId, @Param("bizType")Byte bizType, @Param("bizItem")String bizItem);

    List<Map> agentStatistics(Map param);

    Map platStatistics(Map param);

    List<Map> countAgentStatistics(Map param);

    List<Map> count4AgentProfit(Map param);

    /** 查询统计数据包括：订单总数, 支付总金额, 分润总金额, 最早交易时间, 最晚交易时间 **/
    Map selectCountTradeData(@Param("isvId") Long isvId,
                        @Param("referInfoId") Long referInfoId,
                        @Param("referInfoType") Byte referInfoType,
                        @Param("ltBizOrderCreateDate") Date ltBizOrderCreateDate,
                        @Param("settTaskStatus") Byte settTaskStatus,
                        @Param("productIdList") List<Integer> productIdList);


    /** 查询统计数据包括：分润总金额 **/
    BigDecimal selectSumProfitData(@Param("isvId") Long isvId,
                               @Param("referInfoId") Long referInfoId,
                               @Param("referInfoType") Byte referInfoType,
                               @Param("ltBizOrderCreateDate") Date ltBizOrderCreateDate,
                               @Param("settTaskStatus") Byte settTaskStatus,
                               @Param("productIdList") List<Integer> productIdList);


    /** 根据mchTradeOrderId 查询对应角色金额的sum值 **/
    OrderProfitDetail selectSumAmountByTradeOrder(@Param("referInfoId") Long referInfoId,
                                                  @Param("referInfoType") Byte referInfoType,
                                                  @Param("mchTradeOrderId") String mchTradeOrderId);


    /** 查询统计数据, 分润任务完成后的金额 **/
    BigDecimal selectSumProfitDataByFinishTask(
            @Param("bizOrderCreateDateStart") Date bizOrderCreateDateStart, @Param("bizOrderCreateDateEnd") Date bizOrderCreateDateEnd
            ,@Param("referInfoId") Long referInfoId, @Param("referInfoType") Byte referInfoType);


}
