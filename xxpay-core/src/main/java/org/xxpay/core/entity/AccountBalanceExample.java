package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountBalanceExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AccountBalanceExample() {
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
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("Id not between", value1, value2, "id");
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

        public Criteria andInfoNameIsNull() {
            addCriterion("InfoName is null");
            return (Criteria) this;
        }

        public Criteria andInfoNameIsNotNull() {
            addCriterion("InfoName is not null");
            return (Criteria) this;
        }

        public Criteria andInfoNameEqualTo(String value) {
            addCriterion("InfoName =", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameNotEqualTo(String value) {
            addCriterion("InfoName <>", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameGreaterThan(String value) {
            addCriterion("InfoName >", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameGreaterThanOrEqualTo(String value) {
            addCriterion("InfoName >=", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameLessThan(String value) {
            addCriterion("InfoName <", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameLessThanOrEqualTo(String value) {
            addCriterion("InfoName <=", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameLike(String value) {
            addCriterion("InfoName like", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameNotLike(String value) {
            addCriterion("InfoName not like", value, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameIn(List<String> values) {
            addCriterion("InfoName in", values, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameNotIn(List<String> values) {
            addCriterion("InfoName not in", values, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameBetween(String value1, String value2) {
            addCriterion("InfoName between", value1, value2, "infoName");
            return (Criteria) this;
        }

        public Criteria andInfoNameNotBetween(String value1, String value2) {
            addCriterion("InfoName not between", value1, value2, "infoName");
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

        public Criteria andUnAmountIsNull() {
            addCriterion("UnAmount is null");
            return (Criteria) this;
        }

        public Criteria andUnAmountIsNotNull() {
            addCriterion("UnAmount is not null");
            return (Criteria) this;
        }

        public Criteria andUnAmountEqualTo(Long value) {
            addCriterion("UnAmount =", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountNotEqualTo(Long value) {
            addCriterion("UnAmount <>", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountGreaterThan(Long value) {
            addCriterion("UnAmount >", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("UnAmount >=", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountLessThan(Long value) {
            addCriterion("UnAmount <", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountLessThanOrEqualTo(Long value) {
            addCriterion("UnAmount <=", value, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountIn(List<Long> values) {
            addCriterion("UnAmount in", values, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountNotIn(List<Long> values) {
            addCriterion("UnAmount not in", values, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountBetween(Long value1, Long value2) {
            addCriterion("UnAmount between", value1, value2, "unAmount");
            return (Criteria) this;
        }

        public Criteria andUnAmountNotBetween(Long value1, Long value2) {
            addCriterion("UnAmount not between", value1, value2, "unAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountIsNull() {
            addCriterion("FrozenAmount is null");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountIsNotNull() {
            addCriterion("FrozenAmount is not null");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountEqualTo(Long value) {
            addCriterion("FrozenAmount =", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountNotEqualTo(Long value) {
            addCriterion("FrozenAmount <>", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountGreaterThan(Long value) {
            addCriterion("FrozenAmount >", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("FrozenAmount >=", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountLessThan(Long value) {
            addCriterion("FrozenAmount <", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountLessThanOrEqualTo(Long value) {
            addCriterion("FrozenAmount <=", value, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountIn(List<Long> values) {
            addCriterion("FrozenAmount in", values, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountNotIn(List<Long> values) {
            addCriterion("FrozenAmount not in", values, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountBetween(Long value1, Long value2) {
            addCriterion("FrozenAmount between", value1, value2, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andFrozenAmountNotBetween(Long value1, Long value2) {
            addCriterion("FrozenAmount not between", value1, value2, "frozenAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNull() {
            addCriterion("SettAmount is null");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNotNull() {
            addCriterion("SettAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSettAmountEqualTo(Long value) {
            addCriterion("SettAmount =", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotEqualTo(Long value) {
            addCriterion("SettAmount <>", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThan(Long value) {
            addCriterion("SettAmount >", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("SettAmount >=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThan(Long value) {
            addCriterion("SettAmount <", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThanOrEqualTo(Long value) {
            addCriterion("SettAmount <=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountIn(List<Long> values) {
            addCriterion("SettAmount in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotIn(List<Long> values) {
            addCriterion("SettAmount not in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountBetween(Long value1, Long value2) {
            addCriterion("SettAmount between", value1, value2, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotBetween(Long value1, Long value2) {
            addCriterion("SettAmount not between", value1, value2, "settAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountIsNull() {
            addCriterion("TotalAddAmount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountIsNotNull() {
            addCriterion("TotalAddAmount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountEqualTo(Long value) {
            addCriterion("TotalAddAmount =", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountNotEqualTo(Long value) {
            addCriterion("TotalAddAmount <>", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountGreaterThan(Long value) {
            addCriterion("TotalAddAmount >", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalAddAmount >=", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountLessThan(Long value) {
            addCriterion("TotalAddAmount <", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountLessThanOrEqualTo(Long value) {
            addCriterion("TotalAddAmount <=", value, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountIn(List<Long> values) {
            addCriterion("TotalAddAmount in", values, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountNotIn(List<Long> values) {
            addCriterion("TotalAddAmount not in", values, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountBetween(Long value1, Long value2) {
            addCriterion("TotalAddAmount between", value1, value2, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAddAmountNotBetween(Long value1, Long value2) {
            addCriterion("TotalAddAmount not between", value1, value2, "totalAddAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountIsNull() {
            addCriterion("TotalSubAmount is null");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountIsNotNull() {
            addCriterion("TotalSubAmount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountEqualTo(Long value) {
            addCriterion("TotalSubAmount =", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountNotEqualTo(Long value) {
            addCriterion("TotalSubAmount <>", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountGreaterThan(Long value) {
            addCriterion("TotalSubAmount >", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalSubAmount >=", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountLessThan(Long value) {
            addCriterion("TotalSubAmount <", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountLessThanOrEqualTo(Long value) {
            addCriterion("TotalSubAmount <=", value, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountIn(List<Long> values) {
            addCriterion("TotalSubAmount in", values, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountNotIn(List<Long> values) {
            addCriterion("TotalSubAmount not in", values, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountBetween(Long value1, Long value2) {
            addCriterion("TotalSubAmount between", value1, value2, "totalSubAmount");
            return (Criteria) this;
        }

        public Criteria andTotalSubAmountNotBetween(Long value1, Long value2) {
            addCriterion("TotalSubAmount not between", value1, value2, "totalSubAmount");
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

        public Criteria andSafeKeyIsNull() {
            addCriterion("SafeKey is null");
            return (Criteria) this;
        }

        public Criteria andSafeKeyIsNotNull() {
            addCriterion("SafeKey is not null");
            return (Criteria) this;
        }

        public Criteria andSafeKeyEqualTo(String value) {
            addCriterion("SafeKey =", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyNotEqualTo(String value) {
            addCriterion("SafeKey <>", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyGreaterThan(String value) {
            addCriterion("SafeKey >", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyGreaterThanOrEqualTo(String value) {
            addCriterion("SafeKey >=", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyLessThan(String value) {
            addCriterion("SafeKey <", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyLessThanOrEqualTo(String value) {
            addCriterion("SafeKey <=", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyLike(String value) {
            addCriterion("SafeKey like", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyNotLike(String value) {
            addCriterion("SafeKey not like", value, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyIn(List<String> values) {
            addCriterion("SafeKey in", values, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyNotIn(List<String> values) {
            addCriterion("SafeKey not in", values, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyBetween(String value1, String value2) {
            addCriterion("SafeKey between", value1, value2, "safeKey");
            return (Criteria) this;
        }

        public Criteria andSafeKeyNotBetween(String value1, String value2) {
            addCriterion("SafeKey not between", value1, value2, "safeKey");
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