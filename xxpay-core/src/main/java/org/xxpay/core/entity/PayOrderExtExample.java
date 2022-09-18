package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayOrderExtExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayOrderExtExample() {
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

        public Criteria andPayOrderIdIsNull() {
            addCriterion("PayOrderId is null");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdIsNotNull() {
            addCriterion("PayOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdEqualTo(String value) {
            addCriterion("PayOrderId =", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotEqualTo(String value) {
            addCriterion("PayOrderId <>", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdGreaterThan(String value) {
            addCriterion("PayOrderId >", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("PayOrderId >=", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLessThan(String value) {
            addCriterion("PayOrderId <", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLessThanOrEqualTo(String value) {
            addCriterion("PayOrderId <=", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLike(String value) {
            addCriterion("PayOrderId like", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotLike(String value) {
            addCriterion("PayOrderId not like", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdIn(List<String> values) {
            addCriterion("PayOrderId in", values, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotIn(List<String> values) {
            addCriterion("PayOrderId not in", values, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdBetween(String value1, String value2) {
            addCriterion("PayOrderId between", value1, value2, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotBetween(String value1, String value2) {
            addCriterion("PayOrderId not between", value1, value2, "payOrderId");
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

        public Criteria andRetDataIsNull() {
            addCriterion("retData is null");
            return (Criteria) this;
        }

        public Criteria andRetDataIsNotNull() {
            addCriterion("retData is not null");
            return (Criteria) this;
        }

        public Criteria andRetDataEqualTo(String value) {
            addCriterion("retData =", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataNotEqualTo(String value) {
            addCriterion("retData <>", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataGreaterThan(String value) {
            addCriterion("retData >", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataGreaterThanOrEqualTo(String value) {
            addCriterion("retData >=", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataLessThan(String value) {
            addCriterion("retData <", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataLessThanOrEqualTo(String value) {
            addCriterion("retData <=", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataLike(String value) {
            addCriterion("retData like", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataNotLike(String value) {
            addCriterion("retData not like", value, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataIn(List<String> values) {
            addCriterion("retData in", values, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataNotIn(List<String> values) {
            addCriterion("retData not in", values, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataBetween(String value1, String value2) {
            addCriterion("retData between", value1, value2, "retData");
            return (Criteria) this;
        }

        public Criteria andRetDataNotBetween(String value1, String value2) {
            addCriterion("retData not between", value1, value2, "retData");
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