package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.OrderProfitDetailExample;
import org.xxpay.core.service.IAccountBalanceService;
import org.xxpay.core.service.IAgentpayPassageService;
import org.xxpay.core.service.IFeeScaleService;
import org.xxpay.core.service.IOrderProfitDetailService;
import org.xxpay.service.dao.mapper.FeeScaleMapper;
import org.xxpay.service.dao.mapper.MchInfoMapper;
import org.xxpay.service.dao.mapper.OrderProfitDetailMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Title: </b>OrderProfitDetailServiceImpl.java
 * <p><b>Description: </b>订单分润相关service
 * @author terrfly
 * @version V1.0
 * <p>
 */
@Service
public class OrderProfitDetailServiceImpl extends ServiceImpl<OrderProfitDetailMapper, OrderProfitDetail> implements IOrderProfitDetailService {

	@Autowired
	private MchInfoMapper mchInfoMapper;

	@Autowired
	private OrderProfitDetailMapper orderProfitDetailMapper;

	@Autowired
	private IAccountBalanceService accountBalanceService;

	@Autowired
	private IAgentpayPassageService agentpayPassageService;

	@Autowired
	private IFeeScaleService feeScaleService;

	@Autowired
	private FeeScaleMapper feeScaleMapper;
	
	@Override
	public List<OrderProfitDetail> selectOrderDetail(OrderProfitDetail record) {
		
		OrderProfitDetailExample exa = new OrderProfitDetailExample();
		org.xxpay.core.entity.OrderProfitDetailExample.Criteria c = exa.createCriteria();
		if(record != null){
			
			if(record.getBizType() != null) c.andBizTypeEqualTo(record.getBizType());
			if(record.getBizOrderId() != null) c.andBizOrderIdEqualTo(record.getBizOrderId());
			if(record.getReferInfoType() != null) c.andReferInfoTypeEqualTo(record.getReferInfoType());
			if(record.getReferInfoId() != null) c.andReferInfoIdEqualTo(record.getReferInfoId());
		}
		return orderProfitDetailMapper.selectByExample(exa);
		
	}



	public List<OrderProfitDetail> selectMchAndAgentNotSettProfitDetails(){
		return orderProfitDetailMapper.selectMchAndAgentNotSettProfitDetails();
	}

	public int insert(OrderProfitDetail detail){
		return orderProfitDetailMapper.insertSelective(detail);
	}
	
	@Override
	public OrderProfitDetail findOne(OrderProfitDetail record){
		
		OrderProfitDetailExample exa = new OrderProfitDetailExample();
		OrderProfitDetailExample.Criteria criteria = exa.createCriteria();
		setCriteria(criteria, record);
		
		List<OrderProfitDetail> result = orderProfitDetailMapper.selectByExample(exa);
		if(result.isEmpty()){
			return null;
		}
		return result.get(0);
	}
	
	@Override
	public OrderProfitDetail findOne(Byte bizType, String bizOrderId, Byte referInfoType){
		
		OrderProfitDetail record = new OrderProfitDetail();
		record.setBizOrderId(bizOrderId);
		record.setBizType(bizType);
		record.setReferInfoType(referInfoType);
		return findOne(record);
	}
	
	
	@Override
	public OrderProfitDetail findMchDetailForPayOrderId(String orderId){
		
		OrderProfitDetail record = new OrderProfitDetail();
		record.setBizOrderId(orderId);
		record.setBizType(MchConstant.BIZ_TYPE_TRANSACT);
		record.setReferInfoType(MchConstant.INFO_TYPE_MCH);
		return findOne(record);
		
	}

	@Override
	public OrderProfitDetail findCurrentAgentDetailForPayOrderId(String orderId, Long currentAgentId){

		OrderProfitDetail record = new OrderProfitDetail();
		record.setBizOrderId(orderId);
		record.setBizType(MchConstant.BIZ_TYPE_TRANSACT);
		record.setReferInfoType(MchConstant.INFO_TYPE_AGENT);
		record.setReferInfoId(currentAgentId);
		return findOne(record);

	}

	
	@Override
	public OrderProfitDetail findMchDetailForRechargeOrderId(String orderId){
		
		OrderProfitDetail record = new OrderProfitDetail();
		record.setBizOrderId(orderId);
		record.setBizType(MchConstant.BIZ_TYPE_RECHARGE);
		record.setReferInfoType(MchConstant.INFO_TYPE_MCH);
		return findOne(record);
		
	}

	@Override
	public List<Map> agentStatistics(int offset, int limit, Long agentId, String bizType, String createTimeStart, String createTimeEnd) {
		Map param = new HashMap();
		param.put("offset",offset);
		param.put("limit",limit);
		if (agentId != null) param.put("agentId",agentId);
		if (StringUtils.isNotBlank(bizType)) param.put("bizType",bizType);
		if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
		if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
		return orderProfitDetailMapper.agentStatistics(param);
	}

	@Override
	public List<Map> countAgentStatistics(Long agentId, String bizType, String createTimeStart, String createTimeEnd) {
		Map param = new HashMap();
		if (agentId != null) param.put("agentId",agentId);
		if (StringUtils.isNotBlank(bizType)) param.put("bizType",bizType);
		if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
		if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
		return orderProfitDetailMapper.countAgentStatistics(param);
	}

    @Override
    public List<Map> count4AgentProfit(Long agentId) {
	    Map param = new HashMap<>();
	    if (agentId != null) param.put("agentId",agentId);
        return orderProfitDetailMapper.count4AgentProfit(param);
    }

	@Override
	public List<Map> selectAgentDetailInfoList(String bizOrderId, Byte bizType, String bizItem) {
		return orderProfitDetailMapper.selectAgentDetailInfoList(bizOrderId, bizType, bizItem);
	}

	@Override
    public Map platStatistics(String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if (StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if (StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return orderProfitDetailMapper.platStatistics(param);
    }

    @Override
	public Map<String, OrderProfitDetail> recalculateTradeOrderProfit(MchTradeOrder mchTradeOrder, BigDecimal mchFeeRate,
																	  BigDecimal isvFeeRate, List<AgentInfo> agentsList){

		//函数返回结果集合
		Map<String, OrderProfitDetail> calResult = new HashMap<>();

		Long mchId = mchTradeOrder.getMchId();  //商户ID
		Long isvId = mchTradeOrder.getIsvId();  //服务商ID
		Integer productId = mchTradeOrder.getProductId(); //产品ID

		//业务类型， 目前与商户交易表对应， 1-支付, 2-充值
		Byte bizType = mchTradeOrder.getTradeType() == MchConstant.TRADE_TYPE_PAY ? MchConstant.PROFIT_BIZ_TYPE_PAY : MchConstant.PRODUCT_TYPE_RECHARGE;

		//计算金额基数 = 用户实际支付金额 - 退款总金额
		Long calBaseAmount = mchTradeOrder.getAmount() - mchTradeOrder.getRefundTotalAmount();

		//商户所需支付手续费
		Long mchFee = XXPayUtil.calOrderMultiplyRate(calBaseAmount, mchFeeRate);

		//保存商户数据集合
		OrderProfitDetail mchProfitDetail = new OrderProfitDetail();
		mchProfitDetail.setBizType(bizType); //业务类型
		mchProfitDetail.setBizOrderId(mchTradeOrder.getTradeOrderId());  //交易单号
		mchProfitDetail.setBizOrderPayAmount(mchTradeOrder.getAmount());  //支付金额
		mchProfitDetail.setBizOrderCreateDate(mchTradeOrder.getCreateTime());  //交易时间
		mchProfitDetail.setProductId(productId);    //支付产品ID
		mchProfitDetail.setReferInfoType(MchConstant.INFO_TYPE_MCH);
		mchProfitDetail.setReferInfoId(mchId);
		mchProfitDetail.setIsvId(isvId);
		mchProfitDetail.setIncomeAmount(calBaseAmount - mchFee);  //商户入账金额
		mchProfitDetail.setFeeAmount(mchFee);  //商户费率费用
		mchProfitDetail.setFeeRateSnapshot(mchFeeRate.toString() + "%");  //商户费率快照
		mchProfitDetail.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_NO_NEED); //结算状态： 无需结算

		calResult.put(MchConstant.INFO_TYPE_MCH  + "_" + mchId, mchProfitDetail);

		//上游所有返佣金额 = 商户费率费用 - 服务商费率费用
		Long allProfit = mchFee - XXPayUtil.calOrderMultiplyRate(calBaseAmount, isvFeeRate);

		Long allSubProfit = 0L;  //初始值务必等于0 ， index = 0 的为最远端代理商， 无任何减少金额
		for (int i = 0; i < agentsList.size(); i++) {

			//当前代理商信息
			AgentInfo currentAgent = agentsList.get(i);

			//当前代理商ID
			Long currentAgentId = currentAgent.getAgentId();

			//当前代理商的返佣比例
			BigDecimal currentAgentProfitRate = currentAgent.getProfitRate();

			//当前代理商实际返佣金额
			Long currentAgentProfit = XXPayUtil.calOrderMultiplyRate(allProfit, currentAgentProfitRate) - allSubProfit;

			//累加下级代理商所有返佣
			allSubProfit += currentAgentProfit;

			//记录返佣
			OrderProfitDetail currentAgentProfitDetail = new OrderProfitDetail();
			currentAgentProfitDetail.setBizType(bizType); //业务类型
			currentAgentProfitDetail.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务订单号
			currentAgentProfitDetail.setBizOrderPayAmount(mchTradeOrder.getAmount()); //支付金额
			currentAgentProfitDetail.setBizOrderCreateDate(mchTradeOrder.getCreateTime()); //订单创建时间
			currentAgentProfitDetail.setProductId(productId);    //支付产品ID
			currentAgentProfitDetail.setReferInfoType(MchConstant.INFO_TYPE_AGENT);  //信息类型： 代理商
			currentAgentProfitDetail.setReferInfoId(currentAgentId);
			currentAgentProfitDetail.setIsvId(isvId);
			currentAgentProfitDetail.setIncomeAmount(currentAgentProfit);  //代理商返佣
			currentAgentProfitDetail.setFeeAmount(0L); //无费用
			currentAgentProfitDetail.setFeeRateSnapshot(currentAgentProfitRate.toString() + "%");  //代理商返佣比例快照
			currentAgentProfitDetail.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT); //结算状态： 未结算
			calResult.put(MchConstant.INFO_TYPE_AGENT  + "_" + currentAgentId, currentAgentProfitDetail);
		}

		//计算服务商的分润结果
		Long isvProfit = allProfit - allSubProfit; //服务商的分润结果 = 所有分佣金额 - 所有子代理商的分佣金额

		OrderProfitDetail isvProfitDetail = new OrderProfitDetail();
		isvProfitDetail.setBizType(bizType); //业务类型
		isvProfitDetail.setBizOrderId(mchTradeOrder.getTradeOrderId());  //业务订单号
		isvProfitDetail.setBizOrderPayAmount(mchTradeOrder.getAmount()); //支付金额
		isvProfitDetail.setBizOrderCreateDate(mchTradeOrder.getCreateTime()); //订单创建时间
		isvProfitDetail.setProductId(productId);  //支付产品ID
		isvProfitDetail.setReferInfoType(MchConstant.INFO_TYPE_ISV);  //信息类型： 服务商
		isvProfitDetail.setReferInfoId(isvId);
		isvProfitDetail.setIsvId(isvId);
		isvProfitDetail.setIncomeAmount(isvProfit);  //代理商返佣
		isvProfitDetail.setFeeAmount(0L); //无费用
		isvProfitDetail.setFeeRateSnapshot(isvFeeRate.toString() + "%");  //服务商基准费率快照
		isvProfitDetail.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_WAIT); //结算状态： 服务商也需要跑结算任务
		calResult.put(MchConstant.INFO_TYPE_ISV + "_" + isvId, isvProfitDetail);

		return calResult;
	}


	@Override
	public BigDecimal selectSumProfitDataByFinishTask(Date bizOrderCreateDateStart, Date bizOrderCreateDateEnd, Long referInfoId, Byte referInfoType){
		return baseMapper.selectSumProfitDataByFinishTask(bizOrderCreateDateStart, bizOrderCreateDateEnd, referInfoId, referInfoType);
	}

	void setCriteria(OrderProfitDetailExample.Criteria criteria, OrderProfitDetail record) {
        if(record != null) {
        	if(record.getBizType() != null) criteria.andBizTypeEqualTo(record.getBizType());
        	if(record.getBizOrderId() != null) criteria.andBizOrderIdEqualTo(record.getBizOrderId());
        	if(record.getReferInfoType() != null) criteria.andReferInfoTypeEqualTo(record.getReferInfoType());
        }
    }
	
	

}
