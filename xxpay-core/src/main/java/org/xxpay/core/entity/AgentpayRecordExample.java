package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentpayRecordExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AgentpayRecordExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andAgentpayOrderIdIsNull() {
            addCriterion("AgentpayOrderId is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdIsNotNull() {
            addCriterion("AgentpayOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdEqualTo(String value) {
            addCriterion("AgentpayOrderId =", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdNotEqualTo(String value) {
            addCriterion("AgentpayOrderId <>", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdGreaterThan(String value) {
            addCriterion("AgentpayOrderId >", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("AgentpayOrderId >=", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdLessThan(String value) {
            addCriterion("AgentpayOrderId <", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdLessThanOrEqualTo(String value) {
            addCriterion("AgentpayOrderId <=", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdLike(String value) {
            addCriterion("AgentpayOrderId like", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdNotLike(String value) {
            addCriterion("AgentpayOrderId not like", value, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdIn(List<String> values) {
            addCriterion("AgentpayOrderId in", values, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdNotIn(List<String> values) {
            addCriterion("AgentpayOrderId not in", values, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdBetween(String value1, String value2) {
            addCriterion("AgentpayOrderId between", value1, value2, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andAgentpayOrderIdNotBetween(String value1, String value2) {
            addCriterion("AgentpayOrderId not between", value1, value2, "agentpayOrderId");
            return (Criteria) this;
        }

        public Criteria andInfoIdIsNull() {
            addCriterion("InfoId is null");
            return (Criteria) this;
        }

        public Criteria andInfoIdIsNotNull() {
            addCriterion("InfoId is not null");
            return (Criteria) this;
        }

        public Criteria andInfoIdEqualTo(Long value) {
            addCriterion("InfoId =", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdNotEqualTo(Long value) {
            addCriterion("InfoId <>", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdGreaterThan(Long value) {
            addCriterion("InfoId >", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("InfoId >=", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdLessThan(Long value) {
            addCriterion("InfoId <", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdLessThanOrEqualTo(Long value) {
            addCriterion("InfoId <=", value, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdIn(List<Long> values) {
            addCriterion("InfoId in", values, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdNotIn(List<Long> values) {
            addCriterion("InfoId not in", values, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdBetween(Long value1, Long value2) {
            addCriterion("InfoId between", value1, value2, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoIdNotBetween(Long value1, Long value2) {
            addCriterion("InfoId not between", value1, value2, "infoId");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNull() {
            addCriterion("InfoType is null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNotNull() {
            addCriterion("InfoType is not null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeEqualTo(Byte value) {
            addCriterion("InfoType =", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotEqualTo(Byte value) {
            addCriterion("InfoType <>", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThan(Byte value) {
            addCriterion("InfoType >", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("InfoType >=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThan(Byte value) {
            addCriterion("InfoType <", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThanOrEqualTo(Byte value) {
            addCriterion("InfoType <=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIn(List<Byte> values) {
            addCriterion("InfoType in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotIn(List<Byte> values) {
            addCriterion("InfoType not in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeBetween(Byte value1, Byte value2) {
            addCriterion("InfoType between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("InfoType not between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andMchTypeIsNull() {
            addCriterion("MchType is null");
            return (Criteria) this;
        }

        public Criteria andMchTypeIsNotNull() {
            addCriterion("MchType is not null");
            return (Criteria) this;
        }

        public Criteria andMchTypeEqualTo(Byte value) {
            addCriterion("MchType =", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotEqualTo(Byte value) {
            addCriterion("MchType <>", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeGreaterThan(Byte value) {
            addCriterion("MchType >", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("MchType >=", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeLessThan(Byte value) {
            addCriterion("MchType <", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeLessThanOrEqualTo(Byte value) {
            addCriterion("MchType <=", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeIn(List<Byte> values) {
            addCriterion("MchType in", values, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotIn(List<Byte> values) {
            addCriterion("MchType not in", values, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeBetween(Byte value1, Byte value2) {
            addCriterion("MchType between", value1, value2, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("MchType not between", value1, value2, "mchType");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("Amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("Amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("Amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("Amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("Amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("Amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("Amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("Amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("Amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("Amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("Amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("Amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("Fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("Fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(Long value) {
            addCriterion("Fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(Long value) {
            addCriterion("Fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(Long value) {
            addCriterion("Fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("Fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(Long value) {
            addCriterion("Fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(Long value) {
            addCriterion("Fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<Long> values) {
            addCriterion("Fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<Long> values) {
            addCriterion("Fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(Long value1, Long value2) {
            addCriterion("Fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(Long value1, Long value2) {
            addCriterion("Fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIsNull() {
            addCriterion("RemitAmount is null");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIsNotNull() {
            addCriterion("RemitAmount is not null");
            return (Criteria) this;
        }

        public Criteria andRemitAmountEqualTo(Long value) {
            addCriterion("RemitAmount =", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotEqualTo(Long value) {
            addCriterion("RemitAmount <>", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountGreaterThan(Long value) {
            addCriterion("RemitAmount >", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("RemitAmount >=", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountLessThan(Long value) {
            addCriterion("RemitAmount <", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountLessThanOrEqualTo(Long value) {
            addCriterion("RemitAmount <=", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIn(List<Long> values) {
            addCriterion("RemitAmount in", values, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotIn(List<Long> values) {
            addCriterion("RemitAmount not in", values, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountBetween(Long value1, Long value2) {
            addCriterion("RemitAmount between", value1, value2, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotBetween(Long value1, Long value2) {
            addCriterion("RemitAmount not between", value1, value2, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountIsNull() {
            addCriterion("SubAmount is null");
            return (Criteria) this;
        }

        public Criteria andSubAmountIsNotNull() {
            addCriterion("SubAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSubAmountEqualTo(Long value) {
            addCriterion("SubAmount =", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountNotEqualTo(Long value) {
            addCriterion("SubAmount <>", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountGreaterThan(Long value) {
            addCriterion("SubAmount >", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("SubAmount >=", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountLessThan(Long value) {
            addCriterion("SubAmount <", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountLessThanOrEqualTo(Long value) {
            addCriterion("SubAmount <=", value, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountIn(List<Long> values) {
            addCriterion("SubAmount in", values, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountNotIn(List<Long> values) {
            addCriterion("SubAmount not in", values, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountBetween(Long value1, Long value2) {
            addCriterion("SubAmount between", value1, value2, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountNotBetween(Long value1, Long value2) {
            addCriterion("SubAmount not between", value1, value2, "subAmount");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromIsNull() {
            addCriterion("SubAmountFrom is null");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromIsNotNull() {
            addCriterion("SubAmountFrom is not null");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromEqualTo(Byte value) {
            addCriterion("SubAmountFrom =", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromNotEqualTo(Byte value) {
            addCriterion("SubAmountFrom <>", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromGreaterThan(Byte value) {
            addCriterion("SubAmountFrom >", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromGreaterThanOrEqualTo(Byte value) {
            addCriterion("SubAmountFrom >=", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromLessThan(Byte value) {
            addCriterion("SubAmountFrom <", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromLessThanOrEqualTo(Byte value) {
            addCriterion("SubAmountFrom <=", value, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromIn(List<Byte> values) {
            addCriterion("SubAmountFrom in", values, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromNotIn(List<Byte> values) {
            addCriterion("SubAmountFrom not in", values, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromBetween(Byte value1, Byte value2) {
            addCriterion("SubAmountFrom between", value1, value2, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andSubAmountFromNotBetween(Byte value1, Byte value2) {
            addCriterion("SubAmountFrom not between", value1, value2, "subAmountFrom");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIsNull() {
            addCriterion("AccountAttr is null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIsNotNull() {
            addCriterion("AccountAttr is not null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrEqualTo(Byte value) {
            addCriterion("AccountAttr =", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotEqualTo(Byte value) {
            addCriterion("AccountAttr <>", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThan(Byte value) {
            addCriterion("AccountAttr >", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr >=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThan(Byte value) {
            addCriterion("AccountAttr <", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr <=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIn(List<Byte> values) {
            addCriterion("AccountAttr in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotIn(List<Byte> values) {
            addCriterion("AccountAttr not in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr not between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNull() {
            addCriterion("AccountType is null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNotNull() {
            addCriterion("AccountType is not null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeEqualTo(Byte value) {
            addCriterion("AccountType =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(Byte value) {
            addCriterion("AccountType <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(Byte value) {
            addCriterion("AccountType >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountType >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(Byte value) {
            addCriterion("AccountType <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(Byte value) {
            addCriterion("AccountType <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<Byte> values) {
            addCriterion("AccountType in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<Byte> values) {
            addCriterion("AccountType not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(Byte value1, Byte value2) {
            addCriterion("AccountType between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountType not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNull() {
            addCriterion("AccountName is null");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNotNull() {
            addCriterion("AccountName is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNameEqualTo(String value) {
            addCriterion("AccountName =", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotEqualTo(String value) {
            addCriterion("AccountName <>", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThan(String value) {
            addCriterion("AccountName >", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("AccountName >=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThan(String value) {
            addCriterion("AccountName <", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThanOrEqualTo(String value) {
            addCriterion("AccountName <=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLike(String value) {
            addCriterion("AccountName like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotLike(String value) {
            addCriterion("AccountName not like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameIn(List<String> values) {
            addCriterion("AccountName in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotIn(List<String> values) {
            addCriterion("AccountName not in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameBetween(String value1, String value2) {
            addCriterion("AccountName between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotBetween(String value1, String value2) {
            addCriterion("AccountName not between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNull() {
            addCriterion("AccountNo is null");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNotNull() {
            addCriterion("AccountNo is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNoEqualTo(String value) {
            addCriterion("AccountNo =", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotEqualTo(String value) {
            addCriterion("AccountNo <>", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThan(String value) {
            addCriterion("AccountNo >", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThanOrEqualTo(String value) {
            addCriterion("AccountNo >=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThan(String value) {
            addCriterion("AccountNo <", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThanOrEqualTo(String value) {
            addCriterion("AccountNo <=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLike(String value) {
            addCriterion("AccountNo like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotLike(String value) {
            addCriterion("AccountNo not like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoIn(List<String> values) {
            addCriterion("AccountNo in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotIn(List<String> values) {
            addCriterion("AccountNo not in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoBetween(String value1, String value2) {
            addCriterion("AccountNo between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotBetween(String value1, String value2) {
            addCriterion("AccountNo not between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("Province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("Province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("Province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("Province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("Province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("Province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("Province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("Province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("Province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("Province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("Province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("Province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("Province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("Province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("City is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("City is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("City =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("City <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("City >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("City >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("City <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("City <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("City like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("City not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("City in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("City not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("City between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("City not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNull() {
            addCriterion("BankName is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("BankName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("BankName =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("BankName <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("BankName >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankName >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("BankName <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("BankName <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("BankName like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("BankName not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("BankName in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("BankName not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("BankName between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("BankName not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNull() {
            addCriterion("BankNetName is null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNotNull() {
            addCriterion("BankNetName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameEqualTo(String value) {
            addCriterion("BankNetName =", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotEqualTo(String value) {
            addCriterion("BankNetName <>", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThan(String value) {
            addCriterion("BankNetName >", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankNetName >=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThan(String value) {
            addCriterion("BankNetName <", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThanOrEqualTo(String value) {
            addCriterion("BankNetName <=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLike(String value) {
            addCriterion("BankNetName like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotLike(String value) {
            addCriterion("BankNetName not like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIn(List<String> values) {
            addCriterion("BankNetName in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotIn(List<String> values) {
            addCriterion("BankNetName not in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameBetween(String value1, String value2) {
            addCriterion("BankNetName between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotBetween(String value1, String value2) {
            addCriterion("BankNetName not between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNumberIsNull() {
            addCriterion("BankNumber is null");
            return (Criteria) this;
        }

        public Criteria andBankNumberIsNotNull() {
            addCriterion("BankNumber is not null");
            return (Criteria) this;
        }

        public Criteria andBankNumberEqualTo(String value) {
            addCriterion("BankNumber =", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberNotEqualTo(String value) {
            addCriterion("BankNumber <>", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberGreaterThan(String value) {
            addCriterion("BankNumber >", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberGreaterThanOrEqualTo(String value) {
            addCriterion("BankNumber >=", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberLessThan(String value) {
            addCriterion("BankNumber <", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberLessThanOrEqualTo(String value) {
            addCriterion("BankNumber <=", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberLike(String value) {
            addCriterion("BankNumber like", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberNotLike(String value) {
            addCriterion("BankNumber not like", value, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberIn(List<String> values) {
            addCriterion("BankNumber in", values, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberNotIn(List<String> values) {
            addCriterion("BankNumber not in", values, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberBetween(String value1, String value2) {
            addCriterion("BankNumber between", value1, value2, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankNumberNotBetween(String value1, String value2) {
            addCriterion("BankNumber not between", value1, value2, "bankNumber");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNull() {
            addCriterion("BankCode is null");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNotNull() {
            addCriterion("BankCode is not null");
            return (Criteria) this;
        }

        public Criteria andBankCodeEqualTo(String value) {
            addCriterion("BankCode =", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotEqualTo(String value) {
            addCriterion("BankCode <>", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThan(String value) {
            addCriterion("BankCode >", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThanOrEqualTo(String value) {
            addCriterion("BankCode >=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThan(String value) {
            addCriterion("BankCode <", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThanOrEqualTo(String value) {
            addCriterion("BankCode <=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLike(String value) {
            addCriterion("BankCode like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotLike(String value) {
            addCriterion("BankCode not like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeIn(List<String> values) {
            addCriterion("BankCode in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotIn(List<String> values) {
            addCriterion("BankCode not in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeBetween(String value1, String value2) {
            addCriterion("BankCode between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotBetween(String value1, String value2) {
            addCriterion("BankCode not between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("Status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("Status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("Status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("Status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("Status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("Status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("Status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("Status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("Status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("Status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("Status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("Status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIsNull() {
            addCriterion("TransOrderId is null");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIsNotNull() {
            addCriterion("TransOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdEqualTo(String value) {
            addCriterion("TransOrderId =", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotEqualTo(String value) {
            addCriterion("TransOrderId <>", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdGreaterThan(String value) {
            addCriterion("TransOrderId >", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("TransOrderId >=", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLessThan(String value) {
            addCriterion("TransOrderId <", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLessThanOrEqualTo(String value) {
            addCriterion("TransOrderId <=", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLike(String value) {
            addCriterion("TransOrderId like", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotLike(String value) {
            addCriterion("TransOrderId not like", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIn(List<String> values) {
            addCriterion("TransOrderId in", values, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotIn(List<String> values) {
            addCriterion("TransOrderId not in", values, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdBetween(String value1, String value2) {
            addCriterion("TransOrderId between", value1, value2, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotBetween(String value1, String value2) {
            addCriterion("TransOrderId not between", value1, value2, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransMsgIsNull() {
            addCriterion("TransMsg is null");
            return (Criteria) this;
        }

        public Criteria andTransMsgIsNotNull() {
            addCriterion("TransMsg is not null");
            return (Criteria) this;
        }

        public Criteria andTransMsgEqualTo(String value) {
            addCriterion("TransMsg =", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotEqualTo(String value) {
            addCriterion("TransMsg <>", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgGreaterThan(String value) {
            addCriterion("TransMsg >", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgGreaterThanOrEqualTo(String value) {
            addCriterion("TransMsg >=", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLessThan(String value) {
            addCriterion("TransMsg <", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLessThanOrEqualTo(String value) {
            addCriterion("TransMsg <=", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLike(String value) {
            addCriterion("TransMsg like", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotLike(String value) {
            addCriterion("TransMsg not like", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgIn(List<String> values) {
            addCriterion("TransMsg in", values, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotIn(List<String> values) {
            addCriterion("TransMsg not in", values, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgBetween(String value1, String value2) {
            addCriterion("TransMsg between", value1, value2, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotBetween(String value1, String value2) {
            addCriterion("TransMsg not between", value1, value2, "transMsg");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("ChannelId is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("ChannelId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(String value) {
            addCriterion("ChannelId =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(String value) {
            addCriterion("ChannelId <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(String value) {
            addCriterion("ChannelId >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelId >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(String value) {
            addCriterion("ChannelId <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(String value) {
            addCriterion("ChannelId <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLike(String value) {
            addCriterion("ChannelId like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotLike(String value) {
            addCriterion("ChannelId not like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<String> values) {
            addCriterion("ChannelId in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<String> values) {
            addCriterion("ChannelId not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(String value1, String value2) {
            addCriterion("ChannelId between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(String value1, String value2) {
            addCriterion("ChannelId not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andPassageIdIsNull() {
            addCriterion("PassageId is null");
            return (Criteria) this;
        }

        public Criteria andPassageIdIsNotNull() {
            addCriterion("PassageId is not null");
            return (Criteria) this;
        }

        public Criteria andPassageIdEqualTo(Integer value) {
            addCriterion("PassageId =", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotEqualTo(Integer value) {
            addCriterion("PassageId <>", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdGreaterThan(Integer value) {
            addCriterion("PassageId >", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("PassageId >=", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdLessThan(Integer value) {
            addCriterion("PassageId <", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdLessThanOrEqualTo(Integer value) {
            addCriterion("PassageId <=", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdIn(List<Integer> values) {
            addCriterion("PassageId in", values, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotIn(List<Integer> values) {
            addCriterion("PassageId not in", values, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdBetween(Integer value1, Integer value2) {
            addCriterion("PassageId between", value1, value2, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("PassageId not between", value1, value2, "passageId");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("Remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("Remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("Remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("Remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("Remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("Remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("Remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("Remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("Remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("Remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("Remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("Remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("Remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("Remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CreateTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CreateTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CreateTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CreateTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNull() {
            addCriterion("ClientIp is null");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNotNull() {
            addCriterion("ClientIp is not null");
            return (Criteria) this;
        }

        public Criteria andClientIpEqualTo(String value) {
            addCriterion("ClientIp =", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotEqualTo(String value) {
            addCriterion("ClientIp <>", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThan(String value) {
            addCriterion("ClientIp >", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThanOrEqualTo(String value) {
            addCriterion("ClientIp >=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThan(String value) {
            addCriterion("ClientIp <", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThanOrEqualTo(String value) {
            addCriterion("ClientIp <=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLike(String value) {
            addCriterion("ClientIp like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotLike(String value) {
            addCriterion("ClientIp not like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpIn(List<String> values) {
            addCriterion("ClientIp in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotIn(List<String> values) {
            addCriterion("ClientIp not in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpBetween(String value1, String value2) {
            addCriterion("ClientIp between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotBetween(String value1, String value2) {
            addCriterion("ClientIp not between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andDeviceIsNull() {
            addCriterion("Device is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIsNotNull() {
            addCriterion("Device is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceEqualTo(String value) {
            addCriterion("Device =", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotEqualTo(String value) {
            addCriterion("Device <>", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceGreaterThan(String value) {
            addCriterion("Device >", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceGreaterThanOrEqualTo(String value) {
            addCriterion("Device >=", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLessThan(String value) {
            addCriterion("Device <", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLessThanOrEqualTo(String value) {
            addCriterion("Device <=", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLike(String value) {
            addCriterion("Device like", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotLike(String value) {
            addCriterion("Device not like", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceIn(List<String> values) {
            addCriterion("Device in", values, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotIn(List<String> values) {
            addCriterion("Device not in", values, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceBetween(String value1, String value2) {
            addCriterion("Device between", value1, value2, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotBetween(String value1, String value2) {
            addCriterion("Device not between", value1, value2, "device");
            return (Criteria) this;
        }

        public Criteria andBatchNoIsNull() {
            addCriterion("BatchNo is null");
            return (Criteria) this;
        }

        public Criteria andBatchNoIsNotNull() {
            addCriterion("BatchNo is not null");
            return (Criteria) this;
        }

        public Criteria andBatchNoEqualTo(String value) {
            addCriterion("BatchNo =", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotEqualTo(String value) {
            addCriterion("BatchNo <>", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoGreaterThan(String value) {
            addCriterion("BatchNo >", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoGreaterThanOrEqualTo(String value) {
            addCriterion("BatchNo >=", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLessThan(String value) {
            addCriterion("BatchNo <", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLessThanOrEqualTo(String value) {
            addCriterion("BatchNo <=", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLike(String value) {
            addCriterion("BatchNo like", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotLike(String value) {
            addCriterion("BatchNo not like", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoIn(List<String> values) {
            addCriterion("BatchNo in", values, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotIn(List<String> values) {
            addCriterion("BatchNo not in", values, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoBetween(String value1, String value2) {
            addCriterion("BatchNo between", value1, value2, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotBetween(String value1, String value2) {
            addCriterion("BatchNo not between", value1, value2, "batchNo");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelIsNull() {
            addCriterion("AgentpayChannel is null");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelIsNotNull() {
            addCriterion("AgentpayChannel is not null");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelEqualTo(Byte value) {
            addCriterion("AgentpayChannel =", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelNotEqualTo(Byte value) {
            addCriterion("AgentpayChannel <>", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelGreaterThan(Byte value) {
            addCriterion("AgentpayChannel >", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelGreaterThanOrEqualTo(Byte value) {
            addCriterion("AgentpayChannel >=", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelLessThan(Byte value) {
            addCriterion("AgentpayChannel <", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelLessThanOrEqualTo(Byte value) {
            addCriterion("AgentpayChannel <=", value, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelIn(List<Byte> values) {
            addCriterion("AgentpayChannel in", values, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelNotIn(List<Byte> values) {
            addCriterion("AgentpayChannel not in", values, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelBetween(Byte value1, Byte value2) {
            addCriterion("AgentpayChannel between", value1, value2, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andAgentpayChannelNotBetween(Byte value1, Byte value2) {
            addCriterion("AgentpayChannel not between", value1, value2, "agentpayChannel");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIsNull() {
            addCriterion("MchOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIsNotNull() {
            addCriterion("MchOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoEqualTo(String value) {
            addCriterion("MchOrderNo =", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotEqualTo(String value) {
            addCriterion("MchOrderNo <>", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoGreaterThan(String value) {
            addCriterion("MchOrderNo >", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("MchOrderNo >=", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLessThan(String value) {
            addCriterion("MchOrderNo <", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLessThanOrEqualTo(String value) {
            addCriterion("MchOrderNo <=", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLike(String value) {
            addCriterion("MchOrderNo like", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotLike(String value) {
            addCriterion("MchOrderNo not like", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIn(List<String> values) {
            addCriterion("MchOrderNo in", values, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotIn(List<String> values) {
            addCriterion("MchOrderNo not in", values, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoBetween(String value1, String value2) {
            addCriterion("MchOrderNo between", value1, value2, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotBetween(String value1, String value2) {
            addCriterion("MchOrderNo not between", value1, value2, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIsNull() {
            addCriterion("NotifyUrl is null");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIsNotNull() {
            addCriterion("NotifyUrl is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlEqualTo(String value) {
            addCriterion("NotifyUrl =", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotEqualTo(String value) {
            addCriterion("NotifyUrl <>", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlGreaterThan(String value) {
            addCriterion("NotifyUrl >", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlGreaterThanOrEqualTo(String value) {
            addCriterion("NotifyUrl >=", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLessThan(String value) {
            addCriterion("NotifyUrl <", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLessThanOrEqualTo(String value) {
            addCriterion("NotifyUrl <=", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLike(String value) {
            addCriterion("NotifyUrl like", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotLike(String value) {
            addCriterion("NotifyUrl not like", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIn(List<String> values) {
            addCriterion("NotifyUrl in", values, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotIn(List<String> values) {
            addCriterion("NotifyUrl not in", values, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlBetween(String value1, String value2) {
            addCriterion("NotifyUrl between", value1, value2, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotBetween(String value1, String value2) {
            addCriterion("NotifyUrl not between", value1, value2, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andExtraIsNull() {
            addCriterion("Extra is null");
            return (Criteria) this;
        }

        public Criteria andExtraIsNotNull() {
            addCriterion("Extra is not null");
            return (Criteria) this;
        }

        public Criteria andExtraEqualTo(String value) {
            addCriterion("Extra =", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotEqualTo(String value) {
            addCriterion("Extra <>", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThan(String value) {
            addCriterion("Extra >", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThanOrEqualTo(String value) {
            addCriterion("Extra >=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThan(String value) {
            addCriterion("Extra <", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThanOrEqualTo(String value) {
            addCriterion("Extra <=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLike(String value) {
            addCriterion("Extra like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotLike(String value) {
            addCriterion("Extra not like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraIn(List<String> values) {
            addCriterion("Extra in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotIn(List<String> values) {
            addCriterion("Extra not in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraBetween(String value1, String value2) {
            addCriterion("Extra between", value1, value2, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotBetween(String value1, String value2) {
            addCriterion("Extra not between", value1, value2, "extra");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}