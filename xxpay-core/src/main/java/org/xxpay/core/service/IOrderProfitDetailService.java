package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.OrderProfitDetailExample;


public interface IOrderProfitDetailService extends IService<OrderProfitDetail> {

   /**
    * <p><b>Description: </b>根据record 查询记录
    * <p>2018年10月12日 下午2:24:40
    * @author terrfly
    * @param record
    * @return
    */
   public List<OrderProfitDetail> selectOrderDetail(OrderProfitDetail record);
   
   public int insert(OrderProfitDetail detail);
   
   /**
    * <p><b>Description: </b>查询所有未结算 商户/所有代理商分润数据
    * <p>2018年10月12日 下午6:25:53
    * @author terrfly
    * @return
    */
   public List<OrderProfitDetail> selectMchAndAgentNotSettProfitDetails();


   
	public OrderProfitDetail findOne(OrderProfitDetail record);
		
	public OrderProfitDetail findOne(Byte bizType, String bizOrderId, Byte referInfoType);
	
	public OrderProfitDetail findMchDetailForPayOrderId(String orderId);

	public OrderProfitDetail findCurrentAgentDetailForPayOrderId(String orderId, Long currentAgentId);
	
	public OrderProfitDetail findMchDetailForRechargeOrderId(String orderId);

	/**
	 * 代理商统计
	 * @param agentId
	 * @param bizType
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	List<Map> agentStatistics(int offset, int limit, Long agentId, String bizType, String createTimeStart, String createTimeEnd);

	/**
	 * 平台统计
	 * @param createTimeStart
	 * @param createTimeEnd
	 * @return
	 */
	Map platStatistics(String createTimeStart, String createTimeEnd);

	List<Map> countAgentStatistics(Long agentId, String bizType, String createTimeStart, String createTimeEnd);

    List<Map> count4AgentProfit(Long agentId);

	List<Map> selectAgentDetailInfoList(String bizOrderId, Byte bizType, String bizItem);


	/**
	 * 根据最新 [交易订单数据]， 重新计算各方的分佣情况
	 * @param mchTradeOrder 最新交易订单数据， 包含退款总金额
	 * @param mchFeeRate 商户费率， 一般为0.6
	 * @param isvFeeRate  服务商费率基数， 一般为 0.2
	 * @param agentsList 各级代理商的返佣比例, 顺序按照代理商等级降序  index=0 为远离商户、远离服务商的等级最高代理商
	 * @return  key = INFO_TYPE + infoId,  val 佣金详情
	 */
	Map<String, OrderProfitDetail> recalculateTradeOrderProfit(MchTradeOrder mchTradeOrder, BigDecimal mchFeeRate,
																	  BigDecimal isvFeeRate, List<AgentInfo> agentsList);


	/** 查询统计数据, 分润任务完成后的金额 **/
	BigDecimal selectSumProfitDataByFinishTask(Date bizOrderCreateDateStart, Date bizOrderCreateDateEnd, Long referInfoId, Byte referInfoType);



}
