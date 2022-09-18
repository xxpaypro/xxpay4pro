package org.xxpay.task.reconciliation.entity;

import java.util.Date;

/**
 *
 */
public class ReconciliationEntity {
	@Override
	public String toString() {
		return "ReconciliationEntity{" +
				"batchNo='" + batchNo + '\'' +
				", billDate=" + billDate +
				", bankType='" + bankType + '\'' +
				", orderTime=" + orderTime +
				", bankTradeTime=" + bankTradeTime +
				", bankOrderNo='" + bankOrderNo + '\'' +
				", bankTrxNo='" + bankTrxNo + '\'' +
				", bankTradeStatus='" + bankTradeStatus + '\'' +
				", bankAmount=" + bankAmount +
				", bankRefundAmount=" + bankRefundAmount +
				", bankFee=" + bankFee +
				'}';
	}

	/** 对账批次号 **/
	private String batchNo;

	/** 账单日期 **/
	private Date billDate;

	/** 银行类型 wxpay, alipay, jdpay等 **/
	private String bankType;

	/** 下单时间 **/
	private Date orderTime;

	/** 银行交易时间 **/
	private Date bankTradeTime;

	/** 银行订单号 **/
	private String bankOrderNo;

	/** 银行流水号 **/
	private String bankTrxNo;

	/** 银行交易状态 **/
	private String bankTradeStatus;

	/** 银行交易金额 **/
	private Long bankAmount;

	/** 银行退款金额 **/
	private Long bankRefundAmount;

	/** 银行手续费 **/
	private Long bankFee;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getBankTradeTime() {
		return bankTradeTime;
	}

	public void setBankTradeTime(Date bankTradeTime) {
		this.bankTradeTime = bankTradeTime;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankTrxNo() {
		return bankTrxNo;
	}

	public void setBankTrxNo(String bankTrxNo) {
		this.bankTrxNo = bankTrxNo;
	}

	public String getBankTradeStatus() {
		return bankTradeStatus;
	}

	public void setBankTradeStatus(String bankTradeStatus) {
		this.bankTradeStatus = bankTradeStatus;
	}

	public Long getBankAmount() {
		return bankAmount;
	}

	public void setBankAmount(Long bankAmount) {
		this.bankAmount = bankAmount;
	}

	public Long getBankRefundAmount() {
		return bankRefundAmount;
	}

	public void setBankRefundAmount(Long bankRefundAmount) {
		this.bankRefundAmount = bankRefundAmount;
	}

	public Long getBankFee() {
		return bankFee;
	}

	public void setBankFee(Long bankFee) {
		this.bankFee = bankFee;
	}
}
