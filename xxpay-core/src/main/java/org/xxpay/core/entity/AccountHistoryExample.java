package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountHistoryExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public AccountHistoryExample() {
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

        public Criteria andAfterBalanceIsNull() {
            addCriterion("AfterBalance is null");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceIsNotNull() {
            addCriterion("AfterBalance is not null");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceEqualTo(Long value) {
            addCriterion("AfterBalance =", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotEqualTo(Long value) {
            addCriterion("AfterBalance <>", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceGreaterThan(Long value) {
            addCriterion("AfterBalance >", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceGreaterThanOrEqualTo(Long value) {
            addCriterion("AfterBalance >=", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceLessThan(Long value) {
            addCriterion("AfterBalance <", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceLessThanOrEqualTo(Long value) {
            addCriterion("AfterBalance <=", value, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceIn(List<Long> values) {
            addCriterion("AfterBalance in", values, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotIn(List<Long> values) {
            addCriterion("AfterBalance not in", values, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceBetween(Long value1, Long value2) {
            addCriterion("AfterBalance between", value1, value2, "afterBalance");
            return (Criteria) this;
        }

        public Criteria andAfterBalanceNotBetween(Long value1, Long value2) {
            addCriterion("AfterBalance not between", value1, value2, "afterBalance");
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

        public Criteria andBizChannelOrderNoIsNull() {
            addCriterion("BizChannelOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoIsNotNull() {
            addCriterion("BizChannelOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoEqualTo(String value) {
            addCriterion("BizChannelOrderNo =", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoNotEqualTo(String value) {
            addCriterion("BizChannelOrderNo <>", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoGreaterThan(String value) {
            addCriterion("BizChannelOrderNo >", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("BizChannelOrderNo >=", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoLessThan(String value) {
            addCriterion("BizChannelOrderNo <", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoLessThanOrEqualTo(String value) {
            addCriterion("BizChannelOrderNo <=", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoLike(String value) {
            addCriterion("BizChannelOrderNo like", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoNotLike(String value) {
            addCriterion("BizChannelOrderNo not like", value, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoIn(List<String> values) {
            addCriterion("BizChannelOrderNo in", values, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoNotIn(List<String> values) {
            addCriterion("BizChannelOrderNo not in", values, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoBetween(String value1, String value2) {
            addCriterion("BizChannelOrderNo between", value1, value2, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizChannelOrderNoNotBetween(String value1, String value2) {
            addCriterion("BizChannelOrderNo not between", value1, value2, "bizChannelOrderNo");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountIsNull() {
            addCriterion("BizOrderAmount is null");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountIsNotNull() {
            addCriterion("BizOrderAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountEqualTo(Long value) {
            addCriterion("BizOrderAmount =", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountNotEqualTo(Long value) {
            addCriterion("BizOrderAmount <>", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountGreaterThan(Long value) {
            addCriterion("BizOrderAmount >", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("BizOrderAmount >=", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountLessThan(Long value) {
            addCriterion("BizOrderAmount <", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountLessThanOrEqualTo(Long value) {
            addCriterion("BizOrderAmount <=", value, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountIn(List<Long> values) {
            addCriterion("BizOrderAmount in", values, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountNotIn(List<Long> values) {
            addCriterion("BizOrderAmount not in", values, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountBetween(Long value1, Long value2) {
            addCriterion("BizOrderAmount between", value1, value2, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderAmountNotBetween(Long value1, Long value2) {
            addCriterion("BizOrderAmount not between", value1, value2, "bizOrderAmount");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeIsNull() {
            addCriterion("BizOrderFee is null");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeIsNotNull() {
            addCriterion("BizOrderFee is not null");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeEqualTo(Long value) {
            addCriterion("BizOrderFee =", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeNotEqualTo(Long value) {
            addCriterion("BizOrderFee <>", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeGreaterThan(Long value) {
            addCriterion("BizOrderFee >", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("BizOrderFee >=", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeLessThan(Long value) {
            addCriterion("BizOrderFee <", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeLessThanOrEqualTo(Long value) {
            addCriterion("BizOrderFee <=", value, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeIn(List<Long> values) {
            addCriterion("BizOrderFee in", values, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeNotIn(List<Long> values) {
            addCriterion("BizOrderFee not in", values, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeBetween(Long value1, Long value2) {
            addCriterion("BizOrderFee between", value1, value2, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andBizOrderFeeNotBetween(Long value1, Long value2) {
            addCriterion("BizOrderFee not between", value1, value2, "bizOrderFee");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIsNull() {
            addCriterion("FundDirection is null");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIsNotNull() {
            addCriterion("FundDirection is not null");
            return (Criteria) this;
        }

        public Criteria andFundDirectionEqualTo(Byte value) {
            addCriterion("FundDirection =", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotEqualTo(Byte value) {
            addCriterion("FundDirection <>", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionGreaterThan(Byte value) {
            addCriterion("FundDirection >", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionGreaterThanOrEqualTo(Byte value) {
            addCriterion("FundDirection >=", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionLessThan(Byte value) {
            addCriterion("FundDirection <", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionLessThanOrEqualTo(Byte value) {
            addCriterion("FundDirection <=", value, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionIn(List<Byte> values) {
            addCriterion("FundDirection in", values, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotIn(List<Byte> values) {
            addCriterion("FundDirection not in", values, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionBetween(Byte value1, Byte value2) {
            addCriterion("FundDirection between", value1, value2, "fundDirection");
            return (Criteria) this;
        }

        public Criteria andFundDirectionNotBetween(Byte value1, Byte value2) {
            addCriterion("FundDirection not between", value1, value2, "fundDirection");
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

        public Criteria andBizItemIsNull() {
            addCriterion("BizItem is null");
            return (Criteria) this;
        }

        public Criteria andBizItemIsNotNull() {
            addCriterion("BizItem is not null");
            return (Criteria) this;
        }

        public Criteria andBizItemEqualTo(String value) {
            addCriterion("BizItem =", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotEqualTo(String value) {
            addCriterion("BizItem <>", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemGreaterThan(String value) {
            addCriterion("BizItem >", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemGreaterThanOrEqualTo(String value) {
            addCriterion("BizItem >=", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLessThan(String value) {
            addCriterion("BizItem <", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLessThanOrEqualTo(String value) {
            addCriterion("BizItem <=", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemLike(String value) {
            addCriterion("BizItem like", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotLike(String value) {
            addCriterion("BizItem not like", value, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemIn(List<String> values) {
            addCriterion("BizItem in", values, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotIn(List<String> values) {
            addCriterion("BizItem not in", values, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemBetween(String value1, String value2) {
            addCriterion("BizItem between", value1, value2, "bizItem");
            return (Criteria) this;
        }

        public Criteria andBizItemNotBetween(String value1, String value2) {
            addCriterion("BizItem not between", value1, value2, "bizItem");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeIsNull() {
            addCriterion("ChangeAccountType is null");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeIsNotNull() {
            addCriterion("ChangeAccountType is not null");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeEqualTo(Byte value) {
            addCriterion("ChangeAccountType =", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeNotEqualTo(Byte value) {
            addCriterion("ChangeAccountType <>", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeGreaterThan(Byte value) {
            addCriterion("ChangeAccountType >", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ChangeAccountType >=", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeLessThan(Byte value) {
            addCriterion("ChangeAccountType <", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ChangeAccountType <=", value, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeIn(List<Byte> values) {
            addCriterion("ChangeAccountType in", values, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeNotIn(List<Byte> values) {
            addCriterion("ChangeAccountType not in", values, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeBetween(Byte value1, Byte value2) {
            addCriterion("ChangeAccountType between", value1, value2, "changeAccountType");
            return (Criteria) this;
        }

        public Criteria andChangeAccountTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ChangeAccountType not between", value1, value2, "changeAccountType");
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