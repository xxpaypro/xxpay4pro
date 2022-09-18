package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayInterfaceTypeExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayInterfaceTypeExample() {
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

        public Criteria andIfTypeNameIsNull() {
            addCriterion("IfTypeName is null");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameIsNotNull() {
            addCriterion("IfTypeName is not null");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameEqualTo(String value) {
            addCriterion("IfTypeName =", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameNotEqualTo(String value) {
            addCriterion("IfTypeName <>", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameGreaterThan(String value) {
            addCriterion("IfTypeName >", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameGreaterThanOrEqualTo(String value) {
            addCriterion("IfTypeName >=", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameLessThan(String value) {
            addCriterion("IfTypeName <", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameLessThanOrEqualTo(String value) {
            addCriterion("IfTypeName <=", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameLike(String value) {
            addCriterion("IfTypeName like", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameNotLike(String value) {
            addCriterion("IfTypeName not like", value, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameIn(List<String> values) {
            addCriterion("IfTypeName in", values, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameNotIn(List<String> values) {
            addCriterion("IfTypeName not in", values, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameBetween(String value1, String value2) {
            addCriterion("IfTypeName between", value1, value2, "ifTypeName");
            return (Criteria) this;
        }

        public Criteria andIfTypeNameNotBetween(String value1, String value2) {
            addCriterion("IfTypeName not between", value1, value2, "ifTypeName");
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

        public Criteria andPrivateMchParamIsNull() {
            addCriterion("PrivateMchParam is null");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamIsNotNull() {
            addCriterion("PrivateMchParam is not null");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamEqualTo(String value) {
            addCriterion("PrivateMchParam =", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamNotEqualTo(String value) {
            addCriterion("PrivateMchParam <>", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamGreaterThan(String value) {
            addCriterion("PrivateMchParam >", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamGreaterThanOrEqualTo(String value) {
            addCriterion("PrivateMchParam >=", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamLessThan(String value) {
            addCriterion("PrivateMchParam <", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamLessThanOrEqualTo(String value) {
            addCriterion("PrivateMchParam <=", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamLike(String value) {
            addCriterion("PrivateMchParam like", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamNotLike(String value) {
            addCriterion("PrivateMchParam not like", value, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamIn(List<String> values) {
            addCriterion("PrivateMchParam in", values, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamNotIn(List<String> values) {
            addCriterion("PrivateMchParam not in", values, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamBetween(String value1, String value2) {
            addCriterion("PrivateMchParam between", value1, value2, "privateMchParam");
            return (Criteria) this;
        }

        public Criteria andPrivateMchParamNotBetween(String value1, String value2) {
            addCriterion("PrivateMchParam not between", value1, value2, "privateMchParam");
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