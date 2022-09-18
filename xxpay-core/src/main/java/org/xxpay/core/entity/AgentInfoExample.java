package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentInfoExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AgentInfoExample() {
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

        public Criteria andAgentIdIsNull() {
            addCriterion("AgentId is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNotNull() {
            addCriterion("AgentId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdEqualTo(Long value) {
            addCriterion("AgentId =", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotEqualTo(Long value) {
            addCriterion("AgentId <>", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThan(Long value) {
            addCriterion("AgentId >", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("AgentId >=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThan(Long value) {
            addCriterion("AgentId <", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThanOrEqualTo(Long value) {
            addCriterion("AgentId <=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdIn(List<Long> values) {
            addCriterion("AgentId in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotIn(List<Long> values) {
            addCriterion("AgentId not in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdBetween(Long value1, Long value2) {
            addCriterion("AgentId between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotBetween(Long value1, Long value2) {
            addCriterion("AgentId not between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentNameIsNull() {
            addCriterion("AgentName is null");
            return (Criteria) this;
        }

        public Criteria andAgentNameIsNotNull() {
            addCriterion("AgentName is not null");
            return (Criteria) this;
        }

        public Criteria andAgentNameEqualTo(String value) {
            addCriterion("AgentName =", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameNotEqualTo(String value) {
            addCriterion("AgentName <>", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameGreaterThan(String value) {
            addCriterion("AgentName >", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameGreaterThanOrEqualTo(String value) {
            addCriterion("AgentName >=", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameLessThan(String value) {
            addCriterion("AgentName <", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameLessThanOrEqualTo(String value) {
            addCriterion("AgentName <=", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameLike(String value) {
            addCriterion("AgentName like", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameNotLike(String value) {
            addCriterion("AgentName not like", value, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameIn(List<String> values) {
            addCriterion("AgentName in", values, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameNotIn(List<String> values) {
            addCriterion("AgentName not in", values, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameBetween(String value1, String value2) {
            addCriterion("AgentName between", value1, value2, "agentName");
            return (Criteria) this;
        }

        public Criteria andAgentNameNotBetween(String value1, String value2) {
            addCriterion("AgentName not between", value1, value2, "agentName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIsNull() {
            addCriterion("LoginUserName is null");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIsNotNull() {
            addCriterion("LoginUserName is not null");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameEqualTo(String value) {
            addCriterion("LoginUserName =", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotEqualTo(String value) {
            addCriterion("LoginUserName <>", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameGreaterThan(String value) {
            addCriterion("LoginUserName >", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("LoginUserName >=", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLessThan(String value) {
            addCriterion("LoginUserName <", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLessThanOrEqualTo(String value) {
            addCriterion("LoginUserName <=", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLike(String value) {
            addCriterion("LoginUserName like", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotLike(String value) {
            addCriterion("LoginUserName not like", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIn(List<String> values) {
            addCriterion("LoginUserName in", values, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotIn(List<String> values) {
            addCriterion("LoginUserName not in", values, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameBetween(String value1, String value2) {
            addCriterion("LoginUserName between", value1, value2, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotBetween(String value1, String value2) {
            addCriterion("LoginUserName not between", value1, value2, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("Mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("Mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("Mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("Mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("Mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("Mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("Mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("Mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("Mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("Mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("Mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("Mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("Mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("Mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("Email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("Email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("Email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("Email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("Email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("Email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("Email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("Email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("Email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("Email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("Email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("Email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("Email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("Email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNull() {
            addCriterion("RealName is null");
            return (Criteria) this;
        }

        public Criteria andRealNameIsNotNull() {
            addCriterion("RealName is not null");
            return (Criteria) this;
        }

        public Criteria andRealNameEqualTo(String value) {
            addCriterion("RealName =", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotEqualTo(String value) {
            addCriterion("RealName <>", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThan(String value) {
            addCriterion("RealName >", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameGreaterThanOrEqualTo(String value) {
            addCriterion("RealName >=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThan(String value) {
            addCriterion("RealName <", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLessThanOrEqualTo(String value) {
            addCriterion("RealName <=", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameLike(String value) {
            addCriterion("RealName like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotLike(String value) {
            addCriterion("RealName not like", value, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameIn(List<String> values) {
            addCriterion("RealName in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotIn(List<String> values) {
            addCriterion("RealName not in", values, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameBetween(String value1, String value2) {
            addCriterion("RealName between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andRealNameNotBetween(String value1, String value2) {
            addCriterion("RealName not between", value1, value2, "realName");
            return (Criteria) this;
        }

        public Criteria andIdCardIsNull() {
            addCriterion("IdCard is null");
            return (Criteria) this;
        }

        public Criteria andIdCardIsNotNull() {
            addCriterion("IdCard is not null");
            return (Criteria) this;
        }

        public Criteria andIdCardEqualTo(String value) {
            addCriterion("IdCard =", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotEqualTo(String value) {
            addCriterion("IdCard <>", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardGreaterThan(String value) {
            addCriterion("IdCard >", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardGreaterThanOrEqualTo(String value) {
            addCriterion("IdCard >=", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLessThan(String value) {
            addCriterion("IdCard <", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLessThanOrEqualTo(String value) {
            addCriterion("IdCard <=", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardLike(String value) {
            addCriterion("IdCard like", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotLike(String value) {
            addCriterion("IdCard not like", value, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardIn(List<String> values) {
            addCriterion("IdCard in", values, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotIn(List<String> values) {
            addCriterion("IdCard not in", values, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardBetween(String value1, String value2) {
            addCriterion("IdCard between", value1, value2, "idCard");
            return (Criteria) this;
        }

        public Criteria andIdCardNotBetween(String value1, String value2) {
            addCriterion("IdCard not between", value1, value2, "idCard");
            return (Criteria) this;
        }

        public Criteria andQqIsNull() {
            addCriterion("Qq is null");
            return (Criteria) this;
        }

        public Criteria andQqIsNotNull() {
            addCriterion("Qq is not null");
            return (Criteria) this;
        }

        public Criteria andQqEqualTo(String value) {
            addCriterion("Qq =", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotEqualTo(String value) {
            addCriterion("Qq <>", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqGreaterThan(String value) {
            addCriterion("Qq >", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqGreaterThanOrEqualTo(String value) {
            addCriterion("Qq >=", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLessThan(String value) {
            addCriterion("Qq <", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLessThanOrEqualTo(String value) {
            addCriterion("Qq <=", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqLike(String value) {
            addCriterion("Qq like", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotLike(String value) {
            addCriterion("Qq not like", value, "qq");
            return (Criteria) this;
        }

        public Criteria andQqIn(List<String> values) {
            addCriterion("Qq in", values, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotIn(List<String> values) {
            addCriterion("Qq not in", values, "qq");
            return (Criteria) this;
        }

        public Criteria andQqBetween(String value1, String value2) {
            addCriterion("Qq between", value1, value2, "qq");
            return (Criteria) this;
        }

        public Criteria andQqNotBetween(String value1, String value2) {
            addCriterion("Qq not between", value1, value2, "qq");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("Address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("Address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("Address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("Address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("Address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("Address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("Address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("Address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("Address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("Address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("Address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("Address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("Address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("Address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andProfitRateIsNull() {
            addCriterion("ProfitRate is null");
            return (Criteria) this;
        }

        public Criteria andProfitRateIsNotNull() {
            addCriterion("ProfitRate is not null");
            return (Criteria) this;
        }

        public Criteria andProfitRateEqualTo(BigDecimal value) {
            addCriterion("ProfitRate =", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateNotEqualTo(BigDecimal value) {
            addCriterion("ProfitRate <>", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateGreaterThan(BigDecimal value) {
            addCriterion("ProfitRate >", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ProfitRate >=", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateLessThan(BigDecimal value) {
            addCriterion("ProfitRate <", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ProfitRate <=", value, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateIn(List<BigDecimal> values) {
            addCriterion("ProfitRate in", values, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateNotIn(List<BigDecimal> values) {
            addCriterion("ProfitRate not in", values, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ProfitRate between", value1, value2, "profitRate");
            return (Criteria) this;
        }

        public Criteria andProfitRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ProfitRate not between", value1, value2, "profitRate");
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

        public Criteria andIsvIdIsNull() {
            addCriterion("IsvId is null");
            return (Criteria) this;
        }

        public Criteria andIsvIdIsNotNull() {
            addCriterion("IsvId is not null");
            return (Criteria) this;
        }

        public Criteria andIsvIdEqualTo(Long value) {
            addCriterion("IsvId =", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotEqualTo(Long value) {
            addCriterion("IsvId <>", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdGreaterThan(Long value) {
            addCriterion("IsvId >", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdGreaterThanOrEqualTo(Long value) {
            addCriterion("IsvId >=", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdLessThan(Long value) {
            addCriterion("IsvId <", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdLessThanOrEqualTo(Long value) {
            addCriterion("IsvId <=", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdIn(List<Long> values) {
            addCriterion("IsvId in", values, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotIn(List<Long> values) {
            addCriterion("IsvId not in", values, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdBetween(Long value1, Long value2) {
            addCriterion("IsvId between", value1, value2, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotBetween(Long value1, Long value2) {
            addCriterion("IsvId not between", value1, value2, "isvId");
            return (Criteria) this;
        }

        public Criteria andPidIsNull() {
            addCriterion("Pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("Pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Long value) {
            addCriterion("Pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Long value) {
            addCriterion("Pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Long value) {
            addCriterion("Pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Long value) {
            addCriterion("Pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Long value) {
            addCriterion("Pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Long value) {
            addCriterion("Pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Long> values) {
            addCriterion("Pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Long> values) {
            addCriterion("Pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Long value1, Long value2) {
            addCriterion("Pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Long value1, Long value2) {
            addCriterion("Pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andAgentLevelIsNull() {
            addCriterion("AgentLevel is null");
            return (Criteria) this;
        }

        public Criteria andAgentLevelIsNotNull() {
            addCriterion("AgentLevel is not null");
            return (Criteria) this;
        }

        public Criteria andAgentLevelEqualTo(Integer value) {
            addCriterion("AgentLevel =", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelNotEqualTo(Integer value) {
            addCriterion("AgentLevel <>", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelGreaterThan(Integer value) {
            addCriterion("AgentLevel >", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("AgentLevel >=", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelLessThan(Integer value) {
            addCriterion("AgentLevel <", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelLessThanOrEqualTo(Integer value) {
            addCriterion("AgentLevel <=", value, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelIn(List<Integer> values) {
            addCriterion("AgentLevel in", values, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelNotIn(List<Integer> values) {
            addCriterion("AgentLevel not in", values, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelBetween(Integer value1, Integer value2) {
            addCriterion("AgentLevel between", value1, value2, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAgentLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("AgentLevel not between", value1, value2, "agentLevel");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentIsNull() {
            addCriterion("AllowAddSubAgent is null");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentIsNotNull() {
            addCriterion("AllowAddSubAgent is not null");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentEqualTo(Byte value) {
            addCriterion("AllowAddSubAgent =", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentNotEqualTo(Byte value) {
            addCriterion("AllowAddSubAgent <>", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentGreaterThan(Byte value) {
            addCriterion("AllowAddSubAgent >", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentGreaterThanOrEqualTo(Byte value) {
            addCriterion("AllowAddSubAgent >=", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentLessThan(Byte value) {
            addCriterion("AllowAddSubAgent <", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentLessThanOrEqualTo(Byte value) {
            addCriterion("AllowAddSubAgent <=", value, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentIn(List<Byte> values) {
            addCriterion("AllowAddSubAgent in", values, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentNotIn(List<Byte> values) {
            addCriterion("AllowAddSubAgent not in", values, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentBetween(Byte value1, Byte value2) {
            addCriterion("AllowAddSubAgent between", value1, value2, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddSubAgentNotBetween(Byte value1, Byte value2) {
            addCriterion("AllowAddSubAgent not between", value1, value2, "allowAddSubAgent");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchIsNull() {
            addCriterion("AllowAddMch is null");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchIsNotNull() {
            addCriterion("AllowAddMch is not null");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchEqualTo(Byte value) {
            addCriterion("AllowAddMch =", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchNotEqualTo(Byte value) {
            addCriterion("AllowAddMch <>", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchGreaterThan(Byte value) {
            addCriterion("AllowAddMch >", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchGreaterThanOrEqualTo(Byte value) {
            addCriterion("AllowAddMch >=", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchLessThan(Byte value) {
            addCriterion("AllowAddMch <", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchLessThanOrEqualTo(Byte value) {
            addCriterion("AllowAddMch <=", value, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchIn(List<Byte> values) {
            addCriterion("AllowAddMch in", values, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchNotIn(List<Byte> values) {
            addCriterion("AllowAddMch not in", values, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchBetween(Byte value1, Byte value2) {
            addCriterion("AllowAddMch between", value1, value2, "allowAddMch");
            return (Criteria) this;
        }

        public Criteria andAllowAddMchNotBetween(Byte value1, Byte value2) {
            addCriterion("AllowAddMch not between", value1, value2, "allowAddMch");
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