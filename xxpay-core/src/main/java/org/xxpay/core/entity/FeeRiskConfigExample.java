package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeeRiskConfigExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public FeeRiskConfigExample() {
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

        public Criteria andFeeScaleIdIsNull() {
            addCriterion("FeeScaleId is null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdIsNotNull() {
            addCriterion("FeeScaleId is not null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdEqualTo(Integer value) {
            addCriterion("FeeScaleId =", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdNotEqualTo(Integer value) {
            addCriterion("FeeScaleId <>", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdGreaterThan(Integer value) {
            addCriterion("FeeScaleId >", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("FeeScaleId >=", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdLessThan(Integer value) {
            addCriterion("FeeScaleId <", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdLessThanOrEqualTo(Integer value) {
            addCriterion("FeeScaleId <=", value, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdIn(List<Integer> values) {
            addCriterion("FeeScaleId in", values, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdNotIn(List<Integer> values) {
            addCriterion("FeeScaleId not in", values, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdBetween(Integer value1, Integer value2) {
            addCriterion("FeeScaleId between", value1, value2, "feeScaleId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("FeeScaleId not between", value1, value2, "feeScaleId");
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