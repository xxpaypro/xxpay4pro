package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayProductExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayProductExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNull() {
            addCriterion("ProductName is null");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNotNull() {
            addCriterion("ProductName is not null");
            return (Criteria) this;
        }

        public Criteria andProductNameEqualTo(String value) {
            addCriterion("ProductName =", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotEqualTo(String value) {
            addCriterion("ProductName <>", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThan(String value) {
            addCriterion("ProductName >", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("ProductName >=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThan(String value) {
            addCriterion("ProductName <", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThanOrEqualTo(String value) {
            addCriterion("ProductName <=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLike(String value) {
            addCriterion("ProductName like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotLike(String value) {
            addCriterion("ProductName not like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameIn(List<String> values) {
            addCriterion("ProductName in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotIn(List<String> values) {
            addCriterion("ProductName not in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameBetween(String value1, String value2) {
            addCriterion("ProductName between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotBetween(String value1, String value2) {
            addCriterion("ProductName not between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("PayType is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("PayType is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(String value) {
            addCriterion("PayType =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(String value) {
            addCriterion("PayType <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(String value) {
            addCriterion("PayType >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(String value) {
            addCriterion("PayType >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(String value) {
            addCriterion("PayType <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(String value) {
            addCriterion("PayType <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLike(String value) {
            addCriterion("PayType like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotLike(String value) {
            addCriterion("PayType not like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<String> values) {
            addCriterion("PayType in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<String> values) {
            addCriterion("PayType not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(String value1, String value2) {
            addCriterion("PayType between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(String value1, String value2) {
            addCriterion("PayType not between", value1, value2, "payType");
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

        public Criteria andAgentRateIsNull() {
            addCriterion("AgentRate is null");
            return (Criteria) this;
        }

        public Criteria andAgentRateIsNotNull() {
            addCriterion("AgentRate is not null");
            return (Criteria) this;
        }

        public Criteria andAgentRateEqualTo(BigDecimal value) {
            addCriterion("AgentRate =", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateNotEqualTo(BigDecimal value) {
            addCriterion("AgentRate <>", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateGreaterThan(BigDecimal value) {
            addCriterion("AgentRate >", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("AgentRate >=", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateLessThan(BigDecimal value) {
            addCriterion("AgentRate <", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("AgentRate <=", value, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateIn(List<BigDecimal> values) {
            addCriterion("AgentRate in", values, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateNotIn(List<BigDecimal> values) {
            addCriterion("AgentRate not in", values, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("AgentRate between", value1, value2, "agentRate");
            return (Criteria) this;
        }

        public Criteria andAgentRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("AgentRate not between", value1, value2, "agentRate");
            return (Criteria) this;
        }

        public Criteria andMchRateIsNull() {
            addCriterion("MchRate is null");
            return (Criteria) this;
        }

        public Criteria andMchRateIsNotNull() {
            addCriterion("MchRate is not null");
            return (Criteria) this;
        }

        public Criteria andMchRateEqualTo(BigDecimal value) {
            addCriterion("MchRate =", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotEqualTo(BigDecimal value) {
            addCriterion("MchRate <>", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateGreaterThan(BigDecimal value) {
            addCriterion("MchRate >", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MchRate >=", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateLessThan(BigDecimal value) {
            addCriterion("MchRate <", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MchRate <=", value, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateIn(List<BigDecimal> values) {
            addCriterion("MchRate in", values, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotIn(List<BigDecimal> values) {
            addCriterion("MchRate not in", values, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchRate between", value1, value2, "mchRate");
            return (Criteria) this;
        }

        public Criteria andMchRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MchRate not between", value1, value2, "mchRate");
            return (Criteria) this;
        }

        public Criteria andIfModeIsNull() {
            addCriterion("IfMode is null");
            return (Criteria) this;
        }

        public Criteria andIfModeIsNotNull() {
            addCriterion("IfMode is not null");
            return (Criteria) this;
        }

        public Criteria andIfModeEqualTo(Byte value) {
            addCriterion("IfMode =", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeNotEqualTo(Byte value) {
            addCriterion("IfMode <>", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeGreaterThan(Byte value) {
            addCriterion("IfMode >", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("IfMode >=", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeLessThan(Byte value) {
            addCriterion("IfMode <", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeLessThanOrEqualTo(Byte value) {
            addCriterion("IfMode <=", value, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeIn(List<Byte> values) {
            addCriterion("IfMode in", values, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeNotIn(List<Byte> values) {
            addCriterion("IfMode not in", values, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeBetween(Byte value1, Byte value2) {
            addCriterion("IfMode between", value1, value2, "ifMode");
            return (Criteria) this;
        }

        public Criteria andIfModeNotBetween(Byte value1, Byte value2) {
            addCriterion("IfMode not between", value1, value2, "ifMode");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdIsNull() {
            addCriterion("PayPassageId is null");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdIsNotNull() {
            addCriterion("PayPassageId is not null");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdEqualTo(Integer value) {
            addCriterion("PayPassageId =", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdNotEqualTo(Integer value) {
            addCriterion("PayPassageId <>", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdGreaterThan(Integer value) {
            addCriterion("PayPassageId >", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("PayPassageId >=", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdLessThan(Integer value) {
            addCriterion("PayPassageId <", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdLessThanOrEqualTo(Integer value) {
            addCriterion("PayPassageId <=", value, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdIn(List<Integer> values) {
            addCriterion("PayPassageId in", values, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdNotIn(List<Integer> values) {
            addCriterion("PayPassageId not in", values, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdBetween(Integer value1, Integer value2) {
            addCriterion("PayPassageId between", value1, value2, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("PayPassageId not between", value1, value2, "payPassageId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdIsNull() {
            addCriterion("PayPassageAccountId is null");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdIsNotNull() {
            addCriterion("PayPassageAccountId is not null");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdEqualTo(Integer value) {
            addCriterion("PayPassageAccountId =", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdNotEqualTo(Integer value) {
            addCriterion("PayPassageAccountId <>", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdGreaterThan(Integer value) {
            addCriterion("PayPassageAccountId >", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("PayPassageAccountId >=", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdLessThan(Integer value) {
            addCriterion("PayPassageAccountId <", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdLessThanOrEqualTo(Integer value) {
            addCriterion("PayPassageAccountId <=", value, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdIn(List<Integer> values) {
            addCriterion("PayPassageAccountId in", values, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdNotIn(List<Integer> values) {
            addCriterion("PayPassageAccountId not in", values, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdBetween(Integer value1, Integer value2) {
            addCriterion("PayPassageAccountId between", value1, value2, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPayPassageAccountIdNotBetween(Integer value1, Integer value2) {
            addCriterion("PayPassageAccountId not between", value1, value2, "payPassageAccountId");
            return (Criteria) this;
        }

        public Criteria andPollParamIsNull() {
            addCriterion("PollParam is null");
            return (Criteria) this;
        }

        public Criteria andPollParamIsNotNull() {
            addCriterion("PollParam is not null");
            return (Criteria) this;
        }

        public Criteria andPollParamEqualTo(String value) {
            addCriterion("PollParam =", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamNotEqualTo(String value) {
            addCriterion("PollParam <>", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamGreaterThan(String value) {
            addCriterion("PollParam >", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamGreaterThanOrEqualTo(String value) {
            addCriterion("PollParam >=", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamLessThan(String value) {
            addCriterion("PollParam <", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamLessThanOrEqualTo(String value) {
            addCriterion("PollParam <=", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamLike(String value) {
            addCriterion("PollParam like", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamNotLike(String value) {
            addCriterion("PollParam not like", value, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamIn(List<String> values) {
            addCriterion("PollParam in", values, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamNotIn(List<String> values) {
            addCriterion("PollParam not in", values, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamBetween(String value1, String value2) {
            addCriterion("PollParam between", value1, value2, "pollParam");
            return (Criteria) this;
        }

        public Criteria andPollParamNotBetween(String value1, String value2) {
            addCriterion("PollParam not between", value1, value2, "pollParam");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNull() {
            addCriterion("ProductType is null");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNotNull() {
            addCriterion("ProductType is not null");
            return (Criteria) this;
        }

        public Criteria andProductTypeEqualTo(Byte value) {
            addCriterion("ProductType =", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotEqualTo(Byte value) {
            addCriterion("ProductType <>", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThan(Byte value) {
            addCriterion("ProductType >", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ProductType >=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThan(Byte value) {
            addCriterion("ProductType <", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ProductType <=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeIn(List<Byte> values) {
            addCriterion("ProductType in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotIn(List<Byte> values) {
            addCriterion("ProductType not in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeBetween(Byte value1, Byte value2) {
            addCriterion("ProductType between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ProductType not between", value1, value2, "productType");
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