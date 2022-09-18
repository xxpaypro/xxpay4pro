package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderProfitDetailExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public OrderProfitDetailExample() {
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

        public Criteria andBizTypeIsNull() {
            addCriterion("BizType is null");
            return (Criteria) this;
        }

        public Criteria andBizTypeIsNotNull() {
            addCriterion("BizType is not null");
            return (Criteria) this;
        }

        public Criteria andBizTypeEqualTo(Byte value) {
            addCriterion("BizType =", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotEqualTo(Byte value) {
            addCriterion("BizType <>", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeGreaterThan(Byte value) {
            addCriterion("BizType >", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("BizType >=", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeLessThan(Byte value) {
            addCriterion("BizType <", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeLessThanOrEqualTo(Byte value) {
            addCriterion("BizType <=", value, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeIn(List<Byte> values) {
            addCriterion("BizType in", values, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotIn(List<Byte> values) {
            addCriterion("BizType not in", values, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeBetween(Byte value1, Byte value2) {
            addCriterion("BizType between", value1, value2, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("BizType not between", value1, value2, "bizType");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdIsNull() {
            addCriterion("BizOrderId is null");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdIsNotNull() {
            addCriterion("BizOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdEqualTo(String value) {
            addCriterion("BizOrderId =", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdNotEqualTo(String value) {
            addCriterion("BizOrderId <>", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdGreaterThan(String value) {
            addCriterion("BizOrderId >", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("BizOrderId >=", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdLessThan(String value) {
            addCriterion("BizOrderId <", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdLessThanOrEqualTo(String value) {
            addCriterion("BizOrderId <=", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdLike(String value) {
            addCriterion("BizOrderId like", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdNotLike(String value) {
            addCriterion("BizOrderId not like", value, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdIn(List<String> values) {
            addCriterion("BizOrderId in", values, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdNotIn(List<String> values) {
            addCriterion("BizOrderId not in", values, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdBetween(String value1, String value2) {
            addCriterion("BizOrderId between", value1, value2, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderIdNotBetween(String value1, String value2) {
            addCriterion("BizOrderId not between", value1, value2, "bizOrderId");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountIsNull() {
            addCriterion("BizOrderPayAmount is null");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountIsNotNull() {
            addCriterion("BizOrderPayAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountEqualTo(Long value) {
            addCriterion("BizOrderPayAmount =", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountNotEqualTo(Long value) {
            addCriterion("BizOrderPayAmount <>", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountGreaterThan(Long value) {
            addCriterion("BizOrderPayAmount >", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("BizOrderPayAmount >=", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountLessThan(Long value) {
            addCriterion("BizOrderPayAmount <", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountLessThanOrEqualTo(Long value) {
            addCriterion("BizOrderPayAmount <=", value, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountIn(List<Long> values) {
            addCriterion("BizOrderPayAmount in", values, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountNotIn(List<Long> values) {
            addCriterion("BizOrderPayAmount not in", values, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountBetween(Long value1, Long value2) {
            addCriterion("BizOrderPayAmount between", value1, value2, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderPayAmountNotBetween(Long value1, Long value2) {
            addCriterion("BizOrderPayAmount not between", value1, value2, "bizOrderPayAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateIsNull() {
            addCriterion("BizOrderCreateDate is null");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateIsNotNull() {
            addCriterion("BizOrderCreateDate is not null");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateEqualTo(Date value) {
            addCriterion("BizOrderCreateDate =", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateNotEqualTo(Date value) {
            addCriterion("BizOrderCreateDate <>", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateGreaterThan(Date value) {
            addCriterion("BizOrderCreateDate >", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("BizOrderCreateDate >=", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateLessThan(Date value) {
            addCriterion("BizOrderCreateDate <", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("BizOrderCreateDate <=", value, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateIn(List<Date> values) {
            addCriterion("BizOrderCreateDate in", values, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateNotIn(List<Date> values) {
            addCriterion("BizOrderCreateDate not in", values, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateBetween(Date value1, Date value2) {
            addCriterion("BizOrderCreateDate between", value1, value2, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andBizOrderCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("BizOrderCreateDate not between", value1, value2, "bizOrderCreateDate");
            return (Criteria) this;
        }

        public Criteria andProductIdIsNull() {
            addCriterion("ProductId is null");
            return (Criteria) this;
        }

        public Criteria andProductIdIsNotNull() {
            addCriterion("ProductId is not null");
            return (Criteria) this;
        }

        public Criteria andProductIdEqualTo(Integer value) {
            addCriterion("ProductId =", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotEqualTo(Integer value) {
            addCriterion("ProductId <>", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThan(Integer value) {
            addCriterion("ProductId >", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ProductId >=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThan(Integer value) {
            addCriterion("ProductId <", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThanOrEqualTo(Integer value) {
            addCriterion("ProductId <=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdIn(List<Integer> values) {
            addCriterion("ProductId in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotIn(List<Integer> values) {
            addCriterion("ProductId not in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdBetween(Integer value1, Integer value2) {
            addCriterion("ProductId between", value1, value2, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ProductId not between", value1, value2, "productId");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeIsNull() {
            addCriterion("ReferInfoType is null");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeIsNotNull() {
            addCriterion("ReferInfoType is not null");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeEqualTo(Byte value) {
            addCriterion("ReferInfoType =", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeNotEqualTo(Byte value) {
            addCriterion("ReferInfoType <>", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeGreaterThan(Byte value) {
            addCriterion("ReferInfoType >", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ReferInfoType >=", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeLessThan(Byte value) {
            addCriterion("ReferInfoType <", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ReferInfoType <=", value, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeIn(List<Byte> values) {
            addCriterion("ReferInfoType in", values, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeNotIn(List<Byte> values) {
            addCriterion("ReferInfoType not in", values, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeBetween(Byte value1, Byte value2) {
            addCriterion("ReferInfoType between", value1, value2, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ReferInfoType not between", value1, value2, "referInfoType");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdIsNull() {
            addCriterion("ReferInfoId is null");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdIsNotNull() {
            addCriterion("ReferInfoId is not null");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdEqualTo(Long value) {
            addCriterion("ReferInfoId =", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdNotEqualTo(Long value) {
            addCriterion("ReferInfoId <>", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdGreaterThan(Long value) {
            addCriterion("ReferInfoId >", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ReferInfoId >=", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdLessThan(Long value) {
            addCriterion("ReferInfoId <", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdLessThanOrEqualTo(Long value) {
            addCriterion("ReferInfoId <=", value, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdIn(List<Long> values) {
            addCriterion("ReferInfoId in", values, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdNotIn(List<Long> values) {
            addCriterion("ReferInfoId not in", values, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdBetween(Long value1, Long value2) {
            addCriterion("ReferInfoId between", value1, value2, "referInfoId");
            return (Criteria) this;
        }

        public Criteria andReferInfoIdNotBetween(Long value1, Long value2) {
            addCriterion("ReferInfoId not between", value1, value2, "referInfoId");
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

        public Criteria andIncomeAmountIsNull() {
            addCriterion("IncomeAmount is null");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountIsNotNull() {
            addCriterion("IncomeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountEqualTo(Long value) {
            addCriterion("IncomeAmount =", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotEqualTo(Long value) {
            addCriterion("IncomeAmount <>", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountGreaterThan(Long value) {
            addCriterion("IncomeAmount >", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("IncomeAmount >=", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountLessThan(Long value) {
            addCriterion("IncomeAmount <", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountLessThanOrEqualTo(Long value) {
            addCriterion("IncomeAmount <=", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountIn(List<Long> values) {
            addCriterion("IncomeAmount in", values, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotIn(List<Long> values) {
            addCriterion("IncomeAmount not in", values, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountBetween(Long value1, Long value2) {
            addCriterion("IncomeAmount between", value1, value2, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotBetween(Long value1, Long value2) {
            addCriterion("IncomeAmount not between", value1, value2, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountIsNull() {
            addCriterion("FeeAmount is null");
            return (Criteria) this;
        }

        public Criteria andFeeAmountIsNotNull() {
            addCriterion("FeeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andFeeAmountEqualTo(Long value) {
            addCriterion("FeeAmount =", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountNotEqualTo(Long value) {
            addCriterion("FeeAmount <>", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountGreaterThan(Long value) {
            addCriterion("FeeAmount >", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("FeeAmount >=", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountLessThan(Long value) {
            addCriterion("FeeAmount <", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountLessThanOrEqualTo(Long value) {
            addCriterion("FeeAmount <=", value, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountIn(List<Long> values) {
            addCriterion("FeeAmount in", values, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountNotIn(List<Long> values) {
            addCriterion("FeeAmount not in", values, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountBetween(Long value1, Long value2) {
            addCriterion("FeeAmount between", value1, value2, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeAmountNotBetween(Long value1, Long value2) {
            addCriterion("FeeAmount not between", value1, value2, "feeAmount");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotIsNull() {
            addCriterion("FeeRateSnapshot is null");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotIsNotNull() {
            addCriterion("FeeRateSnapshot is not null");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotEqualTo(String value) {
            addCriterion("FeeRateSnapshot =", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotNotEqualTo(String value) {
            addCriterion("FeeRateSnapshot <>", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotGreaterThan(String value) {
            addCriterion("FeeRateSnapshot >", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotGreaterThanOrEqualTo(String value) {
            addCriterion("FeeRateSnapshot >=", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotLessThan(String value) {
            addCriterion("FeeRateSnapshot <", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotLessThanOrEqualTo(String value) {
            addCriterion("FeeRateSnapshot <=", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotLike(String value) {
            addCriterion("FeeRateSnapshot like", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotNotLike(String value) {
            addCriterion("FeeRateSnapshot not like", value, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotIn(List<String> values) {
            addCriterion("FeeRateSnapshot in", values, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotNotIn(List<String> values) {
            addCriterion("FeeRateSnapshot not in", values, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotBetween(String value1, String value2) {
            addCriterion("FeeRateSnapshot between", value1, value2, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andFeeRateSnapshotNotBetween(String value1, String value2) {
            addCriterion("FeeRateSnapshot not between", value1, value2, "feeRateSnapshot");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIsNull() {
            addCriterion("SettTaskStatus is null");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIsNotNull() {
            addCriterion("SettTaskStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusEqualTo(Byte value) {
            addCriterion("SettTaskStatus =", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotEqualTo(Byte value) {
            addCriterion("SettTaskStatus <>", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusGreaterThan(Byte value) {
            addCriterion("SettTaskStatus >", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettTaskStatus >=", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusLessThan(Byte value) {
            addCriterion("SettTaskStatus <", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusLessThanOrEqualTo(Byte value) {
            addCriterion("SettTaskStatus <=", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIn(List<Byte> values) {
            addCriterion("SettTaskStatus in", values, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotIn(List<Byte> values) {
            addCriterion("SettTaskStatus not in", values, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusBetween(Byte value1, Byte value2) {
            addCriterion("SettTaskStatus between", value1, value2, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("SettTaskStatus not between", value1, value2, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdIsNull() {
            addCriterion("SettTaskId is null");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdIsNotNull() {
            addCriterion("SettTaskId is not null");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdEqualTo(Long value) {
            addCriterion("SettTaskId =", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdNotEqualTo(Long value) {
            addCriterion("SettTaskId <>", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdGreaterThan(Long value) {
            addCriterion("SettTaskId >", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SettTaskId >=", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdLessThan(Long value) {
            addCriterion("SettTaskId <", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("SettTaskId <=", value, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdIn(List<Long> values) {
            addCriterion("SettTaskId in", values, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdNotIn(List<Long> values) {
            addCriterion("SettTaskId not in", values, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdBetween(Long value1, Long value2) {
            addCriterion("SettTaskId between", value1, value2, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("SettTaskId not between", value1, value2, "settTaskId");
            return (Criteria) this;
        }

        public Criteria andSettTimeIsNull() {
            addCriterion("SettTime is null");
            return (Criteria) this;
        }

        public Criteria andSettTimeIsNotNull() {
            addCriterion("SettTime is not null");
            return (Criteria) this;
        }

        public Criteria andSettTimeEqualTo(Date value) {
            addCriterion("SettTime =", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeNotEqualTo(Date value) {
            addCriterion("SettTime <>", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeGreaterThan(Date value) {
            addCriterion("SettTime >", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("SettTime >=", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeLessThan(Date value) {
            addCriterion("SettTime <", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeLessThanOrEqualTo(Date value) {
            addCriterion("SettTime <=", value, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeIn(List<Date> values) {
            addCriterion("SettTime in", values, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeNotIn(List<Date> values) {
            addCriterion("SettTime not in", values, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeBetween(Date value1, Date value2) {
            addCriterion("SettTime between", value1, value2, "settTime");
            return (Criteria) this;
        }

        public Criteria andSettTimeNotBetween(Date value1, Date value2) {
            addCriterion("SettTime not between", value1, value2, "settTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("CreatedTime is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("CreatedTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Date value) {
            addCriterion("CreatedTime =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Date value) {
            addCriterion("CreatedTime <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Date value) {
            addCriterion("CreatedTime >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreatedTime >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Date value) {
            addCriterion("CreatedTime <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Date value) {
            addCriterion("CreatedTime <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Date> values) {
            addCriterion("CreatedTime in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Date> values) {
            addCriterion("CreatedTime not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Date value1, Date value2) {
            addCriterion("CreatedTime between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Date value1, Date value2) {
            addCriterion("CreatedTime not between", value1, value2, "createdTime");
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