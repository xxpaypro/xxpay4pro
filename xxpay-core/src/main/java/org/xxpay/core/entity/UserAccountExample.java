package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAccountExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public UserAccountExample() {
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

        public Criteria andBalanceIsNull() {
            addCriterion("Balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("Balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(Long value) {
            addCriterion("Balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(Long value) {
            addCriterion("Balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(Long value) {
            addCriterion("Balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("Balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(Long value) {
            addCriterion("Balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(Long value) {
            addCriterion("Balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<Long> values) {
            addCriterion("Balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<Long> values) {
            addCriterion("Balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(Long value1, Long value2) {
            addCriterion("Balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(Long value1, Long value2) {
            addCriterion("Balance not between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andCheckSumIsNull() {
            addCriterion("CheckSum is null");
            return (Criteria) this;
        }

        public Criteria andCheckSumIsNotNull() {
            addCriterion("CheckSum is not null");
            return (Criteria) this;
        }

        public Criteria andCheckSumEqualTo(String value) {
            addCriterion("CheckSum =", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumNotEqualTo(String value) {
            addCriterion("CheckSum <>", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumGreaterThan(String value) {
            addCriterion("CheckSum >", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumGreaterThanOrEqualTo(String value) {
            addCriterion("CheckSum >=", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumLessThan(String value) {
            addCriterion("CheckSum <", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumLessThanOrEqualTo(String value) {
            addCriterion("CheckSum <=", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumLike(String value) {
            addCriterion("CheckSum like", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumNotLike(String value) {
            addCriterion("CheckSum not like", value, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumIn(List<String> values) {
            addCriterion("CheckSum in", values, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumNotIn(List<String> values) {
            addCriterion("CheckSum not in", values, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumBetween(String value1, String value2) {
            addCriterion("CheckSum between", value1, value2, "checkSum");
            return (Criteria) this;
        }

        public Criteria andCheckSumNotBetween(String value1, String value2) {
            addCriterion("CheckSum not between", value1, value2, "checkSum");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaIsNull() {
            addCriterion("UpdateTimeJava is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaIsNotNull() {
            addCriterion("UpdateTimeJava is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaEqualTo(Long value) {
            addCriterion("UpdateTimeJava =", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaNotEqualTo(Long value) {
            addCriterion("UpdateTimeJava <>", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaGreaterThan(Long value) {
            addCriterion("UpdateTimeJava >", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaGreaterThanOrEqualTo(Long value) {
            addCriterion("UpdateTimeJava >=", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaLessThan(Long value) {
            addCriterion("UpdateTimeJava <", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaLessThanOrEqualTo(Long value) {
            addCriterion("UpdateTimeJava <=", value, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaIn(List<Long> values) {
            addCriterion("UpdateTimeJava in", values, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaNotIn(List<Long> values) {
            addCriterion("UpdateTimeJava not in", values, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaBetween(Long value1, Long value2) {
            addCriterion("UpdateTimeJava between", value1, value2, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeJavaNotBetween(Long value1, Long value2) {
            addCriterion("UpdateTimeJava not between", value1, value2, "updateTimeJava");
            return (Criteria) this;
        }

        public Criteria andTotalRollInIsNull() {
            addCriterion("TotalRollIn is null");
            return (Criteria) this;
        }

        public Criteria andTotalRollInIsNotNull() {
            addCriterion("TotalRollIn is not null");
            return (Criteria) this;
        }

        public Criteria andTotalRollInEqualTo(Long value) {
            addCriterion("TotalRollIn =", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInNotEqualTo(Long value) {
            addCriterion("TotalRollIn <>", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInGreaterThan(Long value) {
            addCriterion("TotalRollIn >", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalRollIn >=", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInLessThan(Long value) {
            addCriterion("TotalRollIn <", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInLessThanOrEqualTo(Long value) {
            addCriterion("TotalRollIn <=", value, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInIn(List<Long> values) {
            addCriterion("TotalRollIn in", values, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInNotIn(List<Long> values) {
            addCriterion("TotalRollIn not in", values, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInBetween(Long value1, Long value2) {
            addCriterion("TotalRollIn between", value1, value2, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollInNotBetween(Long value1, Long value2) {
            addCriterion("TotalRollIn not between", value1, value2, "totalRollIn");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutIsNull() {
            addCriterion("TotalRollOut is null");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutIsNotNull() {
            addCriterion("TotalRollOut is not null");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutEqualTo(Long value) {
            addCriterion("TotalRollOut =", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutNotEqualTo(Long value) {
            addCriterion("TotalRollOut <>", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutGreaterThan(Long value) {
            addCriterion("TotalRollOut >", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutGreaterThanOrEqualTo(Long value) {
            addCriterion("TotalRollOut >=", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutLessThan(Long value) {
            addCriterion("TotalRollOut <", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutLessThanOrEqualTo(Long value) {
            addCriterion("TotalRollOut <=", value, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutIn(List<Long> values) {
            addCriterion("TotalRollOut in", values, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutNotIn(List<Long> values) {
            addCriterion("TotalRollOut not in", values, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutBetween(Long value1, Long value2) {
            addCriterion("TotalRollOut between", value1, value2, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andTotalRollOutNotBetween(Long value1, Long value2) {
            addCriterion("TotalRollOut not between", value1, value2, "totalRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceIsNull() {
            addCriterion("UseableBalance is null");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceIsNotNull() {
            addCriterion("UseableBalance is not null");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceEqualTo(Long value) {
            addCriterion("UseableBalance =", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceNotEqualTo(Long value) {
            addCriterion("UseableBalance <>", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceGreaterThan(Long value) {
            addCriterion("UseableBalance >", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("UseableBalance >=", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceLessThan(Long value) {
            addCriterion("UseableBalance <", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceLessThanOrEqualTo(Long value) {
            addCriterion("UseableBalance <=", value, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceIn(List<Long> values) {
            addCriterion("UseableBalance in", values, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceNotIn(List<Long> values) {
            addCriterion("UseableBalance not in", values, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceBetween(Long value1, Long value2) {
            addCriterion("UseableBalance between", value1, value2, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableBalanceNotBetween(Long value1, Long value2) {
            addCriterion("UseableBalance not between", value1, value2, "useableBalance");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumIsNull() {
            addCriterion("UseableCheckSum is null");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumIsNotNull() {
            addCriterion("UseableCheckSum is not null");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumEqualTo(String value) {
            addCriterion("UseableCheckSum =", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumNotEqualTo(String value) {
            addCriterion("UseableCheckSum <>", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumGreaterThan(String value) {
            addCriterion("UseableCheckSum >", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumGreaterThanOrEqualTo(String value) {
            addCriterion("UseableCheckSum >=", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumLessThan(String value) {
            addCriterion("UseableCheckSum <", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumLessThanOrEqualTo(String value) {
            addCriterion("UseableCheckSum <=", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumLike(String value) {
            addCriterion("UseableCheckSum like", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumNotLike(String value) {
            addCriterion("UseableCheckSum not like", value, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumIn(List<String> values) {
            addCriterion("UseableCheckSum in", values, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumNotIn(List<String> values) {
            addCriterion("UseableCheckSum not in", values, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumBetween(String value1, String value2) {
            addCriterion("UseableCheckSum between", value1, value2, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableCheckSumNotBetween(String value1, String value2) {
            addCriterion("UseableCheckSum not between", value1, value2, "useableCheckSum");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaIsNull() {
            addCriterion("UseableUpdateTimeJava is null");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaIsNotNull() {
            addCriterion("UseableUpdateTimeJava is not null");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaEqualTo(Long value) {
            addCriterion("UseableUpdateTimeJava =", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaNotEqualTo(Long value) {
            addCriterion("UseableUpdateTimeJava <>", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaGreaterThan(Long value) {
            addCriterion("UseableUpdateTimeJava >", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaGreaterThanOrEqualTo(Long value) {
            addCriterion("UseableUpdateTimeJava >=", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaLessThan(Long value) {
            addCriterion("UseableUpdateTimeJava <", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaLessThanOrEqualTo(Long value) {
            addCriterion("UseableUpdateTimeJava <=", value, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaIn(List<Long> values) {
            addCriterion("UseableUpdateTimeJava in", values, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaNotIn(List<Long> values) {
            addCriterion("UseableUpdateTimeJava not in", values, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaBetween(Long value1, Long value2) {
            addCriterion("UseableUpdateTimeJava between", value1, value2, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableUpdateTimeJavaNotBetween(Long value1, Long value2) {
            addCriterion("UseableUpdateTimeJava not between", value1, value2, "useableUpdateTimeJava");
            return (Criteria) this;
        }

        public Criteria andUseableRollInIsNull() {
            addCriterion("UseableRollIn is null");
            return (Criteria) this;
        }

        public Criteria andUseableRollInIsNotNull() {
            addCriterion("UseableRollIn is not null");
            return (Criteria) this;
        }

        public Criteria andUseableRollInEqualTo(Long value) {
            addCriterion("UseableRollIn =", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInNotEqualTo(Long value) {
            addCriterion("UseableRollIn <>", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInGreaterThan(Long value) {
            addCriterion("UseableRollIn >", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInGreaterThanOrEqualTo(Long value) {
            addCriterion("UseableRollIn >=", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInLessThan(Long value) {
            addCriterion("UseableRollIn <", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInLessThanOrEqualTo(Long value) {
            addCriterion("UseableRollIn <=", value, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInIn(List<Long> values) {
            addCriterion("UseableRollIn in", values, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInNotIn(List<Long> values) {
            addCriterion("UseableRollIn not in", values, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInBetween(Long value1, Long value2) {
            addCriterion("UseableRollIn between", value1, value2, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollInNotBetween(Long value1, Long value2) {
            addCriterion("UseableRollIn not between", value1, value2, "useableRollIn");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutIsNull() {
            addCriterion("UseableRollOut is null");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutIsNotNull() {
            addCriterion("UseableRollOut is not null");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutEqualTo(Long value) {
            addCriterion("UseableRollOut =", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutNotEqualTo(Long value) {
            addCriterion("UseableRollOut <>", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutGreaterThan(Long value) {
            addCriterion("UseableRollOut >", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutGreaterThanOrEqualTo(Long value) {
            addCriterion("UseableRollOut >=", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutLessThan(Long value) {
            addCriterion("UseableRollOut <", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutLessThanOrEqualTo(Long value) {
            addCriterion("UseableRollOut <=", value, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutIn(List<Long> values) {
            addCriterion("UseableRollOut in", values, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutNotIn(List<Long> values) {
            addCriterion("UseableRollOut not in", values, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutBetween(Long value1, Long value2) {
            addCriterion("UseableRollOut between", value1, value2, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andUseableRollOutNotBetween(Long value1, Long value2) {
            addCriterion("UseableRollOut not between", value1, value2, "useableRollOut");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("State is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("State is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Short value) {
            addCriterion("State =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Short value) {
            addCriterion("State <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Short value) {
            addCriterion("State >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Short value) {
            addCriterion("State >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Short value) {
            addCriterion("State <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Short value) {
            addCriterion("State <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Short> values) {
            addCriterion("State in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Short> values) {
            addCriterion("State not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Short value1, Short value2) {
            addCriterion("State between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Short value1, Short value2) {
            addCriterion("State not between", value1, value2, "state");
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