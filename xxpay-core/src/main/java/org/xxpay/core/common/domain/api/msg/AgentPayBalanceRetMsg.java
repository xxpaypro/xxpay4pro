package org.xxpay.core.common.domain.api.msg;

import java.io.Serializable;

/**
 * 封装对象 代付余额查询返回信息
 **/
public class AgentPayBalanceRetMsg implements Serializable {

    /** 上游渠道返回状态 **/
    private QueryState queryState;

    /** 余额 **/
    private Long cashBalance;

    /** 可用余额 **/
    private Long usableBalance;

    public AgentPayBalanceRetMsg() {}
    public AgentPayBalanceRetMsg(QueryState queryState) {
        this.queryState = queryState;
    }

    public AgentPayBalanceRetMsg(QueryState queryState, Long cashBalance, Long usableBalance) {
        this.queryState = queryState;
        this.cashBalance = cashBalance;
        this.usableBalance = usableBalance;
    }

    public QueryState getQueryState() {
        return queryState;
    }

    public void setQueryState(QueryState queryState) {
        this.queryState = queryState;
    }

    public Long getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Long cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Long getUsableBalance() {
        return usableBalance;
    }

    public void setUsableBalance(Long usableBalance) {
        this.usableBalance = usableBalance;
    }

    //渠道状态枚举值
    public enum QueryState {
        UNSUPPORTED, //该渠道不支持代付余额查询
        SUCCESS, //查询成功
        FAIL //查询失败
    }


    //静态初始函数

    /** 查询成功 **/
    public static AgentPayBalanceRetMsg success(Long cashBalance, Long usableBalance){
        return new AgentPayBalanceRetMsg(QueryState.SUCCESS, cashBalance, usableBalance);
    }

    /** 查询失败, 一般出现异常的情况 **/
    public static AgentPayBalanceRetMsg fail(){
        return new AgentPayBalanceRetMsg(QueryState.FAIL);
    }

    /** 该渠道不支持查询接口 **/
    public static AgentPayBalanceRetMsg unsupported(){
        return new AgentPayBalanceRetMsg(QueryState.UNSUPPORTED);
    }

}