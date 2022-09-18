package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAccountLogExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public UserAccountLogExample() {
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

        public Criteria andLogIdIsNull() {
            addCriterion("LogId is null");
            return (Criteria) this;
        }

        public Criteria andLogIdIsNotNull() {
            addCriterion("LogId is not null");
            return (Criteria) this;
        }

        public Criteria andLogIdEqualTo(Long value) {
            addCriterion("LogId =", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotEqualTo(Long value) {
            addCriterion("LogId <>", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThan(Long value) {
            addCriterion("LogId >", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdGreaterThanOrEqualTo(Long value) {
            addCriterion("LogId >=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThan(Long value) {
            addCriterion("LogId <", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdLessThanOrEqualTo(Long value) {
            addCriterion("LogId <=", value, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdIn(List<Long> values) {
            addCriterion("LogId in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotIn(List<Long> values) {
            addCriterion("LogId not in", values, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdBetween(Long value1, Long value2) {
            addCriterion("LogId between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andLogIdNotBetween(Long value1, Long value2) {
            addCriterion("LogId not between", value1, value2, "logId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("UserId is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("UserId is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("UserId =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("UserId <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("UserId >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("UserId >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("UserId <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("UserId <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("UserId like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("UserId not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("UserId in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("UserId not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("UserId between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("UserId not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andMchIdIsNull() {
            addCriterion("MchId is null");
            return (Criteria) this;
        }

        public Criteria andMchIdIsNotNull() {
            addCriterion("MchId is not null");
            return (Criteria) this;
        }

        public Criteria andMchIdEqualTo(Long value) {
            addCriterion("MchId =", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotEqualTo(Long value) {
            addCriterion("MchId <>", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThan(Long value) {
            addCriterion("MchId >", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MchId >=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThan(Long value) {
            addCriterion("MchId <", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThanOrEqualTo(Long value) {
            addCriterion("MchId <=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdIn(List<Long> values) {
            addCriterion("MchId in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotIn(List<Long> values) {
            addCriterion("MchId not in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdBetween(Long value1, Long value2) {
            addCriterion("MchId between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotBetween(Long value1, Long value2) {
            addCriterion("MchId not between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andChangeAmountIsNull() {
            addCriterion("ChangeAmount is null");
            return (Criteria) this;
        }

        public Criteria andChangeAmountIsNotNull() {
            addCriterion("ChangeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andChangeAmountEqualTo(Long value) {
            addCriterion("ChangeAmount =", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountNotEqualTo(Long value) {
            addCriterion("ChangeAmount <>", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountGreaterThan(Long value) {
            addCriterion("ChangeAmount >", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("ChangeAmount >=", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountLessThan(Long value) {
            addCriterion("ChangeAmount <", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountLessThanOrEqualTo(Long value) {
            addCriterion("ChangeAmount <=", value, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountIn(List<Long> values) {
            addCriterion("ChangeAmount in", values, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountNotIn(List<Long> values) {
            addCriterion("ChangeAmount not in", values, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountBetween(Long value1, Long value2) {
            addCriterion("ChangeAmount between", value1, value2, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andChangeAmountNotBetween(Long value1, Long value2) {
            addCriterion("ChangeAmount not between", value1, value2, "changeAmount");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("Type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("Type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Short value) {
            addCriterion("Type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Short value) {
            addCriterion("Type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Short value) {
            addCriterion("Type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("Type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Short value) {
            addCriterion("Type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Short value) {
            addCriterion("Type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Short> values) {
            addCriterion("Type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Short> values) {
            addCriterion("Type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Short value1, Short value2) {
            addCriterion("Type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Short value1, Short value2) {
            addCriterion("Type not between", value1, value2, "type");
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

        public Criteria andAccountTypeEqualTo(Short value) {
            addCriterion("AccountType =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(Short value) {
            addCriterion("AccountType <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(Short value) {
            addCriterion("AccountType >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("AccountType >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(Short value) {
            addCriterion("AccountType <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(Short value) {
            addCriterion("AccountType <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<Short> values) {
            addCriterion("AccountType in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<Short> values) {
            addCriterion("AccountType not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(Short value1, Short value2) {
            addCriterion("AccountType between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(Short value1, Short value2) {
            addCriterion("AccountType not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andOldBalanceIsNull() {
            addCriterion("OldBalance is null");
            return (Criteria) this;
        }

        public Criteria andOldBalanceIsNotNull() {
            addCriterion("OldBalance is not null");
            return (Criteria) this;
        }

        public Criteria andOldBalanceEqualTo(Long value) {
            addCriterion("OldBalance =", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceNotEqualTo(Long value) {
            addCriterion("OldBalance <>", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceGreaterThan(Long value) {
            addCriterion("OldBalance >", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("OldBalance >=", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceLessThan(Long value) {
            addCriterion("OldBalance <", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceLessThanOrEqualTo(Long value) {
            addCriterion("OldBalance <=", value, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceIn(List<Long> values) {
            addCriterion("OldBalance in", values, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceNotIn(List<Long> values) {
            addCriterion("OldBalance not in", values, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceBetween(Long value1, Long value2) {
            addCriterion("OldBalance between", value1, value2, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldBalanceNotBetween(Long value1, Long value2) {
            addCriterion("OldBalance not between", value1, value2, "oldBalance");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaIsNull() {
            addCriterion("OldUpdateTimeJava is null");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaIsNotNull() {
            addCriterion("OldUpdateTimeJava is not null");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaEqualTo(Long value) {
            addCriterion("OldUpdateTimeJava =", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaNotEqualTo(Long value) {
            addCriterion("OldUpdateTimeJava <>", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaGreaterThan(Long value) {
            addCriterion("OldUpdateTimeJava >", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaGreaterThanOrEqualTo(Long value) {
            addCriterion("OldUpdateTimeJava >=", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaLessThan(Long value) {
            addCriterion("OldUpdateTimeJava <", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaLessThanOrEqualTo(Long value) {
            addCriterion("OldUpdateTimeJava <=", value, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaIn(List<Long> values) {
            addCriterion("OldUpdateTimeJava in", values, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaNotIn(List<Long> values) {
            addCriterion("OldUpdateTimeJava not in", values, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaBetween(Long value1, Long value2) {
            addCriterion("OldUpdateTimeJava between", value1, value2, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldUpdateTimeJavaNotBetween(Long value1, Long value2) {
            addCriterion("OldUpdateTimeJava not between", value1, value2, "oldUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumIsNull() {
            addCriterion("OldCheckSum is null");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumIsNotNull() {
            addCriterion("OldCheckSum is not null");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumEqualTo(String value) {
            addCriterion("OldCheckSum =", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumNotEqualTo(String value) {
            addCriterion("OldCheckSum <>", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumGreaterThan(String value) {
            addCriterion("OldCheckSum >", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumGreaterThanOrEqualTo(String value) {
            addCriterion("OldCheckSum >=", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumLessThan(String value) {
            addCriterion("OldCheckSum <", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumLessThanOrEqualTo(String value) {
            addCriterion("OldCheckSum <=", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumLike(String value) {
            addCriterion("OldCheckSum like", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumNotLike(String value) {
            addCriterion("OldCheckSum not like", value, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumIn(List<String> values) {
            addCriterion("OldCheckSum in", values, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumNotIn(List<String> values) {
            addCriterion("OldCheckSum not in", values, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumBetween(String value1, String value2) {
            addCriterion("OldCheckSum between", value1, value2, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andOldCheckSumNotBetween(String value1, String value2) {
            addCriterion("OldCheckSum not between", value1, value2, "oldCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewBalanceIsNull() {
            addCriterion("NewBalance is null");
            return (Criteria) this;
        }

        public Criteria andNewBalanceIsNotNull() {
            addCriterion("NewBalance is not null");
            return (Criteria) this;
        }

        public Criteria andNewBalanceEqualTo(Long value) {
            addCriterion("NewBalance =", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceNotEqualTo(Long value) {
            addCriterion("NewBalance <>", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceGreaterThan(Long value) {
            addCriterion("NewBalance >", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("NewBalance >=", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceLessThan(Long value) {
            addCriterion("NewBalance <", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceLessThanOrEqualTo(Long value) {
            addCriterion("NewBalance <=", value, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceIn(List<Long> values) {
            addCriterion("NewBalance in", values, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceNotIn(List<Long> values) {
            addCriterion("NewBalance not in", values, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceBetween(Long value1, Long value2) {
            addCriterion("NewBalance between", value1, value2, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewBalanceNotBetween(Long value1, Long value2) {
            addCriterion("NewBalance not between", value1, value2, "newBalance");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaIsNull() {
            addCriterion("NewUpdateTimeJava is null");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaIsNotNull() {
            addCriterion("NewUpdateTimeJava is not null");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaEqualTo(Long value) {
            addCriterion("NewUpdateTimeJava =", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaNotEqualTo(Long value) {
            addCriterion("NewUpdateTimeJava <>", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaGreaterThan(Long value) {
            addCriterion("NewUpdateTimeJava >", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaGreaterThanOrEqualTo(Long value) {
            addCriterion("NewUpdateTimeJava >=", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaLessThan(Long value) {
            addCriterion("NewUpdateTimeJava <", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaLessThanOrEqualTo(Long value) {
            addCriterion("NewUpdateTimeJava <=", value, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaIn(List<Long> values) {
            addCriterion("NewUpdateTimeJava in", values, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaNotIn(List<Long> values) {
            addCriterion("NewUpdateTimeJava not in", values, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaBetween(Long value1, Long value2) {
            addCriterion("NewUpdateTimeJava between", value1, value2, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewUpdateTimeJavaNotBetween(Long value1, Long value2) {
            addCriterion("NewUpdateTimeJava not between", value1, value2, "newUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumIsNull() {
            addCriterion("NewCheckSum is null");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumIsNotNull() {
            addCriterion("NewCheckSum is not null");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumEqualTo(String value) {
            addCriterion("NewCheckSum =", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumNotEqualTo(String value) {
            addCriterion("NewCheckSum <>", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumGreaterThan(String value) {
            addCriterion("NewCheckSum >", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumGreaterThanOrEqualTo(String value) {
            addCriterion("NewCheckSum >=", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumLessThan(String value) {
            addCriterion("NewCheckSum <", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumLessThanOrEqualTo(String value) {
            addCriterion("NewCheckSum <=", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumLike(String value) {
            addCriterion("NewCheckSum like", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumNotLike(String value) {
            addCriterion("NewCheckSum not like", value, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumIn(List<String> values) {
            addCriterion("NewCheckSum in", values, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumNotIn(List<String> values) {
            addCriterion("NewCheckSum not in", values, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumBetween(String value1, String value2) {
            addCriterion("NewCheckSum between", value1, value2, "newCheckSum");
            return (Criteria) this;
        }

        public Criteria andNewCheckSumNotBetween(String value1, String value2) {
            addCriterion("NewCheckSum not between", value1, value2, "newCheckSum");
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