package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayPassageExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayPassageExample() {
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

        public Criteria andBelongInfoIdIsNull() {
            addCriterion("BelongInfoId is null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdIsNotNull() {
            addCriterion("BelongInfoId is not null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdEqualTo(Long value) {
            addCriterion("BelongInfoId =", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotEqualTo(Long value) {
            addCriterion("BelongInfoId <>", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdGreaterThan(Long value) {
            addCriterion("BelongInfoId >", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("BelongInfoId >=", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdLessThan(Long value) {
            addCriterion("BelongInfoId <", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdLessThanOrEqualTo(Long value) {
            addCriterion("BelongInfoId <=", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdIn(List<Long> values) {
            addCriterion("BelongInfoId in", values, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotIn(List<Long> values) {
            addCriterion("BelongInfoId not in", values, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdBetween(Long value1, Long value2) {
            addCriterion("BelongInfoId between", value1, value2, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotBetween(Long value1, Long value2) {
            addCriterion("BelongInfoId not between", value1, value2, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIsNull() {
            addCriterion("BelongInfoType is null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIsNotNull() {
            addCriterion("BelongInfoType is not null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeEqualTo(Byte value) {
            addCriterion("BelongInfoType =", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotEqualTo(Byte value) {
            addCriterion("BelongInfoType <>", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeGreaterThan(Byte value) {
            addCriterion("BelongInfoType >", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("BelongInfoType >=", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeLessThan(Byte value) {
            addCriterion("BelongInfoType <", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeLessThanOrEqualTo(Byte value) {
            addCriterion("BelongInfoType <=", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIn(List<Byte> values) {
            addCriterion("BelongInfoType in", values, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotIn(List<Byte> values) {
            addCriterion("BelongInfoType not in", values, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeBetween(Byte value1, Byte value2) {
            addCriterion("BelongInfoType between", value1, value2, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("BelongInfoType not between", value1, value2, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andPassageNameIsNull() {
            addCriterion("PassageName is null");
            return (Criteria) this;
        }

        public Criteria andPassageNameIsNotNull() {
            addCriterion("PassageName is not null");
            return (Criteria) this;
        }

        public Criteria andPassageNameEqualTo(String value) {
            addCriterion("PassageName =", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameNotEqualTo(String value) {
            addCriterion("PassageName <>", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameGreaterThan(String value) {
            addCriterion("PassageName >", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameGreaterThanOrEqualTo(String value) {
            addCriterion("PassageName >=", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameLessThan(String value) {
            addCriterion("PassageName <", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameLessThanOrEqualTo(String value) {
            addCriterion("PassageName <=", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameLike(String value) {
            addCriterion("PassageName like", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameNotLike(String value) {
            addCriterion("PassageName not like", value, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameIn(List<String> values) {
            addCriterion("PassageName in", values, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameNotIn(List<String> values) {
            addCriterion("PassageName not in", values, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameBetween(String value1, String value2) {
            addCriterion("PassageName between", value1, value2, "passageName");
            return (Criteria) this;
        }

        public Criteria andPassageNameNotBetween(String value1, String value2) {
            addCriterion("PassageName not between", value1, value2, "passageName");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIsNull() {
            addCriterion("IfTypeCode is null");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIsNotNull() {
            addCriterion("IfTypeCode is not null");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeEqualTo(String value) {
            addCriterion("IfTypeCode =", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotEqualTo(String value) {
            addCriterion("IfTypeCode <>", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeGreaterThan(String value) {
            addCriterion("IfTypeCode >", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("IfTypeCode >=", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLessThan(String value) {
            addCriterion("IfTypeCode <", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLessThanOrEqualTo(String value) {
            addCriterion("IfTypeCode <=", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLike(String value) {
            addCriterion("IfTypeCode like", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotLike(String value) {
            addCriterion("IfTypeCode not like", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIn(List<String> values) {
            addCriterion("IfTypeCode in", values, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotIn(List<String> values) {
            addCriterion("IfTypeCode not in", values, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeBetween(String value1, String value2) {
            addCriterion("IfTypeCode between", value1, value2, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotBetween(String value1, String value2) {
            addCriterion("IfTypeCode not between", value1, value2, "ifTypeCode");
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

        public Criteria andPassageRateIsNull() {
            addCriterion("PassageRate is null");
            return (Criteria) this;
        }

        public Criteria andPassageRateIsNotNull() {
            addCriterion("PassageRate is not null");
            return (Criteria) this;
        }

        public Criteria andPassageRateEqualTo(BigDecimal value) {
            addCriterion("PassageRate =", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateNotEqualTo(BigDecimal value) {
            addCriterion("PassageRate <>", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateGreaterThan(BigDecimal value) {
            addCriterion("PassageRate >", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PassageRate >=", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateLessThan(BigDecimal value) {
            addCriterion("PassageRate <", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PassageRate <=", value, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateIn(List<BigDecimal> values) {
            addCriterion("PassageRate in", values, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateNotIn(List<BigDecimal> values) {
            addCriterion("PassageRate not in", values, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PassageRate between", value1, value2, "passageRate");
            return (Criteria) this;
        }

        public Criteria andPassageRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PassageRate not between", value1, value2, "passageRate");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountIsNull() {
            addCriterion("MaxDayAmount is null");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountIsNotNull() {
            addCriterion("MaxDayAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountEqualTo(Long value) {
            addCriterion("MaxDayAmount =", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountNotEqualTo(Long value) {
            addCriterion("MaxDayAmount <>", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountGreaterThan(Long value) {
            addCriterion("MaxDayAmount >", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MaxDayAmount >=", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountLessThan(Long value) {
            addCriterion("MaxDayAmount <", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountLessThanOrEqualTo(Long value) {
            addCriterion("MaxDayAmount <=", value, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountIn(List<Long> values) {
            addCriterion("MaxDayAmount in", values, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountNotIn(List<Long> values) {
            addCriterion("MaxDayAmount not in", values, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountBetween(Long value1, Long value2) {
            addCriterion("MaxDayAmount between", value1, value2, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxDayAmountNotBetween(Long value1, Long value2) {
            addCriterion("MaxDayAmount not between", value1, value2, "maxDayAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountIsNull() {
            addCriterion("MaxEveryAmount is null");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountIsNotNull() {
            addCriterion("MaxEveryAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountEqualTo(Long value) {
            addCriterion("MaxEveryAmount =", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotEqualTo(Long value) {
            addCriterion("MaxEveryAmount <>", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountGreaterThan(Long value) {
            addCriterion("MaxEveryAmount >", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MaxEveryAmount >=", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountLessThan(Long value) {
            addCriterion("MaxEveryAmount <", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountLessThanOrEqualTo(Long value) {
            addCriterion("MaxEveryAmount <=", value, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountIn(List<Long> values) {
            addCriterion("MaxEveryAmount in", values, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotIn(List<Long> values) {
            addCriterion("MaxEveryAmount not in", values, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountBetween(Long value1, Long value2) {
            addCriterion("MaxEveryAmount between", value1, value2, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMaxEveryAmountNotBetween(Long value1, Long value2) {
            addCriterion("MaxEveryAmount not between", value1, value2, "maxEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountIsNull() {
            addCriterion("MinEveryAmount is null");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountIsNotNull() {
            addCriterion("MinEveryAmount is not null");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountEqualTo(Long value) {
            addCriterion("MinEveryAmount =", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountNotEqualTo(Long value) {
            addCriterion("MinEveryAmount <>", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountGreaterThan(Long value) {
            addCriterion("MinEveryAmount >", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("MinEveryAmount >=", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountLessThan(Long value) {
            addCriterion("MinEveryAmount <", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountLessThanOrEqualTo(Long value) {
            addCriterion("MinEveryAmount <=", value, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountIn(List<Long> values) {
            addCriterion("MinEveryAmount in", values, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountNotIn(List<Long> values) {
            addCriterion("MinEveryAmount not in", values, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountBetween(Long value1, Long value2) {
            addCriterion("MinEveryAmount between", value1, value2, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andMinEveryAmountNotBetween(Long value1, Long value2) {
            addCriterion("MinEveryAmount not between", value1, value2, "minEveryAmount");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeIsNull() {
            addCriterion("TradeStartTime is null");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeIsNotNull() {
            addCriterion("TradeStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeEqualTo(String value) {
            addCriterion("TradeStartTime =", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeNotEqualTo(String value) {
            addCriterion("TradeStartTime <>", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeGreaterThan(String value) {
            addCriterion("TradeStartTime >", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeGreaterThanOrEqualTo(String value) {
            addCriterion("TradeStartTime >=", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeLessThan(String value) {
            addCriterion("TradeStartTime <", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeLessThanOrEqualTo(String value) {
            addCriterion("TradeStartTime <=", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeLike(String value) {
            addCriterion("TradeStartTime like", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeNotLike(String value) {
            addCriterion("TradeStartTime not like", value, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeIn(List<String> values) {
            addCriterion("TradeStartTime in", values, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeNotIn(List<String> values) {
            addCriterion("TradeStartTime not in", values, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeBetween(String value1, String value2) {
            addCriterion("TradeStartTime between", value1, value2, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeStartTimeNotBetween(String value1, String value2) {
            addCriterion("TradeStartTime not between", value1, value2, "tradeStartTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeIsNull() {
            addCriterion("TradeEndTime is null");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeIsNotNull() {
            addCriterion("TradeEndTime is not null");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeEqualTo(String value) {
            addCriterion("TradeEndTime =", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeNotEqualTo(String value) {
            addCriterion("TradeEndTime <>", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeGreaterThan(String value) {
            addCriterion("TradeEndTime >", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeGreaterThanOrEqualTo(String value) {
            addCriterion("TradeEndTime >=", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeLessThan(String value) {
            addCriterion("TradeEndTime <", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeLessThanOrEqualTo(String value) {
            addCriterion("TradeEndTime <=", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeLike(String value) {
            addCriterion("TradeEndTime like", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeNotLike(String value) {
            addCriterion("TradeEndTime not like", value, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeIn(List<String> values) {
            addCriterion("TradeEndTime in", values, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeNotIn(List<String> values) {
            addCriterion("TradeEndTime not in", values, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeBetween(String value1, String value2) {
            addCriterion("TradeEndTime between", value1, value2, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andTradeEndTimeNotBetween(String value1, String value2) {
            addCriterion("TradeEndTime not between", value1, value2, "tradeEndTime");
            return (Criteria) this;
        }

        public Criteria andRiskStatusIsNull() {
            addCriterion("RiskStatus is null");
            return (Criteria) this;
        }

        public Criteria andRiskStatusIsNotNull() {
            addCriterion("RiskStatus is not null");
            return (Criteria) this;
        }

        public Criteria andRiskStatusEqualTo(Byte value) {
            addCriterion("RiskStatus =", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusNotEqualTo(Byte value) {
            addCriterion("RiskStatus <>", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusGreaterThan(Byte value) {
            addCriterion("RiskStatus >", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("RiskStatus >=", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusLessThan(Byte value) {
            addCriterion("RiskStatus <", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusLessThanOrEqualTo(Byte value) {
            addCriterion("RiskStatus <=", value, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusIn(List<Byte> values) {
            addCriterion("RiskStatus in", values, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusNotIn(List<Byte> values) {
            addCriterion("RiskStatus not in", values, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusBetween(Byte value1, Byte value2) {
            addCriterion("RiskStatus between", value1, value2, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andRiskStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("RiskStatus not between", value1, value2, "riskStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusIsNull() {
            addCriterion("ContractStatus is null");
            return (Criteria) this;
        }

        public Criteria andContractStatusIsNotNull() {
            addCriterion("ContractStatus is not null");
            return (Criteria) this;
        }

        public Criteria andContractStatusEqualTo(Byte value) {
            addCriterion("ContractStatus =", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusNotEqualTo(Byte value) {
            addCriterion("ContractStatus <>", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusGreaterThan(Byte value) {
            addCriterion("ContractStatus >", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("ContractStatus >=", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusLessThan(Byte value) {
            addCriterion("ContractStatus <", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusLessThanOrEqualTo(Byte value) {
            addCriterion("ContractStatus <=", value, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusIn(List<Byte> values) {
            addCriterion("ContractStatus in", values, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusNotIn(List<Byte> values) {
            addCriterion("ContractStatus not in", values, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusBetween(Byte value1, Byte value2) {
            addCriterion("ContractStatus between", value1, value2, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andContractStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("ContractStatus not between", value1, value2, "contractStatus");
            return (Criteria) this;
        }

        public Criteria andIsvParamIsNull() {
            addCriterion("IsvParam is null");
            return (Criteria) this;
        }

        public Criteria andIsvParamIsNotNull() {
            addCriterion("IsvParam is not null");
            return (Criteria) this;
        }

        public Criteria andIsvParamEqualTo(String value) {
            addCriterion("IsvParam =", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamNotEqualTo(String value) {
            addCriterion("IsvParam <>", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamGreaterThan(String value) {
            addCriterion("IsvParam >", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamGreaterThanOrEqualTo(String value) {
            addCriterion("IsvParam >=", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamLessThan(String value) {
            addCriterion("IsvParam <", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamLessThanOrEqualTo(String value) {
            addCriterion("IsvParam <=", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamLike(String value) {
            addCriterion("IsvParam like", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamNotLike(String value) {
            addCriterion("IsvParam not like", value, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamIn(List<String> values) {
            addCriterion("IsvParam in", values, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamNotIn(List<String> values) {
            addCriterion("IsvParam not in", values, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamBetween(String value1, String value2) {
            addCriterion("IsvParam between", value1, value2, "isvParam");
            return (Criteria) this;
        }

        public Criteria andIsvParamNotBetween(String value1, String value2) {
            addCriterion("IsvParam not between", value1, value2, "isvParam");
            return (Criteria) this;
        }

        public Criteria andMchParamIsNull() {
            addCriterion("MchParam is null");
            return (Criteria) this;
        }

        public Criteria andMchParamIsNotNull() {
            addCriterion("MchParam is not null");
            return (Criteria) this;
        }

        public Criteria andMchParamEqualTo(String value) {
            addCriterion("MchParam =", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamNotEqualTo(String value) {
            addCriterion("MchParam <>", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamGreaterThan(String value) {
            addCriterion("MchParam >", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamGreaterThanOrEqualTo(String value) {
            addCriterion("MchParam >=", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamLessThan(String value) {
            addCriterion("MchParam <", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamLessThanOrEqualTo(String value) {
            addCriterion("MchParam <=", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamLike(String value) {
            addCriterion("MchParam like", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamNotLike(String value) {
            addCriterion("MchParam not like", value, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamIn(List<String> values) {
            addCriterion("MchParam in", values, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamNotIn(List<String> values) {
            addCriterion("MchParam not in", values, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamBetween(String value1, String value2) {
            addCriterion("MchParam between", value1, value2, "mchParam");
            return (Criteria) this;
        }

        public Criteria andMchParamNotBetween(String value1, String value2) {
            addCriterion("MchParam not between", value1, value2, "mchParam");
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