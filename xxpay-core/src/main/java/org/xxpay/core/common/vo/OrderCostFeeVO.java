package org.xxpay.core.common.vo;

/**
 * <p><b>Title: </b>OrderCostFeeVO.java
 * <p><b>Description: </b>订单分润 对象
 * @author terrfly
 * @version V1.0
 * <p>
 */
public class OrderCostFeeVO {
	
	
	public OrderCostFeeVO() {
		super();
	}

	public OrderCostFeeVO(Long channelCostFee, Long agentCostFee, Long mchCostFee, Long platProfit, Long agentProfit, Long mchIncome) {
		super();
		this.channelCostFee = channelCostFee;
		this.agentCostFee = agentCostFee;
		this.mchCostFee = mchCostFee;
		this.platProfit = platProfit;
		this.agentProfit = agentProfit;
		this.mchIncome = mchIncome;
	}

	/**
	 * 通道手续费
	 */
	private Long channelCostFee;
	
	/**
	 * 代理商成本费用   即  ：代理商需要支付给平台的费用
	 */
	private Long agentCostFee;
	
	/**
	 * 商家成本费用  即 ： 商家需要支付代理商的费用
	 */
	private Long mchCostFee;
	
	/**
	 * 平台利润 
	 */
	private Long platProfit;
	
	/**
	 * 代理商利润 
	 */
	private Long agentProfit;
	
	/**
	 * 商户入账金额
	 */
	private Long mchIncome;

	public Long getChannelCostFee() {
		return channelCostFee;
	}

	public void setChannelCostFee(Long channelCostFee) {
		this.channelCostFee = channelCostFee;
	}

	public Long getAgentCostFee() {
		return agentCostFee;
	}

	public void setAgentCostFee(Long agentCostFee) {
		this.agentCostFee = agentCostFee;
	}

	public Long getMchCostFee() {
		return mchCostFee;
	}

	public void setMchCostFee(Long mchCostFee) {
		this.mchCostFee = mchCostFee;
	}

	public Long getPlatProfit() {
		return platProfit;
	}

	public void setPlatProfit(Long platProfit) {
		this.platProfit = platProfit;
	}

	public Long getAgentProfit() {
		return agentProfit;
	}

	public void setAgentProfit(Long agentProfit) {
		this.agentProfit = agentProfit;
	}

	public Long getMchIncome() {
		return mchIncome;
	}

	public void setMchIncome(Long mchIncome) {
		this.mchIncome = mchIncome;
	}

}
