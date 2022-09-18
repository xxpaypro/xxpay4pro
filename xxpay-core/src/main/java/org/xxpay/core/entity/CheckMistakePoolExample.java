package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CheckMistakePoolExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public CheckMistakePoolExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProductameIsNull() {
            addCriterion("productame is null");
            return (Criteria) this;
        }

        public Criteria andProductameIsNotNull() {
            addCriterion("productame is not null");
            return (Criteria) this;
        }

        public Criteria andProductameEqualTo(String value) {
            addCriterion("productame =", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameNotEqualTo(String value) {
            addCriterion("productame <>", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameGreaterThan(String value) {
            addCriterion("productame >", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameGreaterThanOrEqualTo(String value) {
            addCriterion("productame >=", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameLessThan(String value) {
            addCriterion("productame <", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameLessThanOrEqualTo(String value) {
            addCriterion("productame <=", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameLike(String value) {
            addCriterion("productame like", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameNotLike(String value) {
            addCriterion("productame not like", value, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameIn(List<String> values) {
            addCriterion("productame in", values, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameNotIn(List<String> values) {
            addCriterion("productame not in", values, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameBetween(String value1, String value2) {
            addCriterion("productame between", value1, value2, "productame");
            return (Criteria) this;
        }

        public Criteria andProductameNotBetween(String value1, String value2) {
            addCriterion("productame not between", value1, value2, "productame");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIsNull() {
            addCriterion("mchOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIsNotNull() {
            addCriterion("mchOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoEqualTo(String value) {
            addCriterion("mchOrderNo =", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotEqualTo(String value) {
            addCriterion("mchOrderNo <>", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoGreaterThan(String value) {
            addCriterion("mchOrderNo >", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("mchOrderNo >=", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLessThan(String value) {
            addCriterion("mchOrderNo <", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLessThanOrEqualTo(String value) {
            addCriterion("mchOrderNo <=", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoLike(String value) {
            addCriterion("mchOrderNo like", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotLike(String value) {
            addCriterion("mchOrderNo not like", value, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoIn(List<String> values) {
            addCriterion("mchOrderNo in", values, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotIn(List<String> values) {
            addCriterion("mchOrderNo not in", values, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoBetween(String value1, String value2) {
            addCriterion("mchOrderNo between", value1, value2, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andMchOrderNoNotBetween(String value1, String value2) {
            addCriterion("mchOrderNo not between", value1, value2, "mchOrderNo");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("orderId is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("orderId is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(String value) {
            addCriterion("orderId =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(String value) {
            addCriterion("orderId <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(String value) {
            addCriterion("orderId >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("orderId >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(String value) {
            addCriterion("orderId <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(String value) {
            addCriterion("orderId <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLike(String value) {
            addCriterion("orderId like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotLike(String value) {
            addCriterion("orderId not like", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<String> values) {
            addCriterion("orderId in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<String> values) {
            addCriterion("orderId not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(String value1, String value2) {
            addCriterion("orderId between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(String value1, String value2) {
            addCriterion("orderId not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoIsNull() {
            addCriterion("bankOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoIsNotNull() {
            addCriterion("bankOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoEqualTo(String value) {
            addCriterion("bankOrderNo =", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoNotEqualTo(String value) {
            addCriterion("bankOrderNo <>", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoGreaterThan(String value) {
            addCriterion("bankOrderNo >", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("bankOrderNo >=", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoLessThan(String value) {
            addCriterion("bankOrderNo <", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoLessThanOrEqualTo(String value) {
            addCriterion("bankOrderNo <=", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoLike(String value) {
            addCriterion("bankOrderNo like", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoNotLike(String value) {
            addCriterion("bankOrderNo not like", value, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoIn(List<String> values) {
            addCriterion("bankOrderNo in", values, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoNotIn(List<String> values) {
            addCriterion("bankOrderNo not in", values, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoBetween(String value1, String value2) {
            addCriterion("bankOrderNo between", value1, value2, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andBankOrderNoNotBetween(String value1, String value2) {
            addCriterion("bankOrderNo not between", value1, value2, "bankOrderNo");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNull() {
            addCriterion("orderAmount is null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNotNull() {
            addCriterion("orderAmount is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountEqualTo(Long value) {
            addCriterion("orderAmount =", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotEqualTo(Long value) {
            addCriterion("orderAmount <>", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThan(Long value) {
            addCriterion("orderAmount >", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("orderAmount >=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThan(Long value) {
            addCriterion("orderAmount <", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThanOrEqualTo(Long value) {
            addCriterion("orderAmount <=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIn(List<Long> values) {
            addCriterion("orderAmount in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotIn(List<Long> values) {
            addCriterion("orderAmount not in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountBetween(Long value1, Long value2) {
            addCriterion("orderAmount between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotBetween(Long value1, Long value2) {
            addCriterion("orderAmount not between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeIsNull() {
            addCriterion("platIncome is null");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeIsNotNull() {
            addCriterion("platIncome is not null");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeEqualTo(Long value) {
            addCriterion("platIncome =", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeNotEqualTo(Long value) {
            addCriterion("platIncome <>", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeGreaterThan(Long value) {
            addCriterion("platIncome >", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeGreaterThanOrEqualTo(Long value) {
            addCriterion("platIncome >=", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeLessThan(Long value) {
            addCriterion("platIncome <", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeLessThanOrEqualTo(Long value) {
            addCriterion("platIncome <=", value, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeIn(List<Long> values) {
            addCriterion("platIncome in", values, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeNotIn(List<Long> values) {
            addCriterion("platIncome not in", values, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeBetween(Long value1, Long value2) {
            addCriterion("platIncome between", value1, value2, "platIncome");
            return (Criteria) this;
        }

        public Criteria andPlatIncomeNotBetween(Long value1, Long value2) {
            addCriterion("platIncome not between", value1, value2, "platIncome");
            return (Criteria) this;
        }

        public Criteria andFeeateIsNull() {
            addCriterion("feeate is null");
            return (Criteria) this;
        }

        public Criteria andFeeateIsNotNull() {
            addCriterion("feeate is not null");
            return (Criteria) this;
        }

        public Criteria andFeeateEqualTo(BigDecimal value) {
            addCriterion("feeate =", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateNotEqualTo(BigDecimal value) {
            addCriterion("feeate <>", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateGreaterThan(BigDecimal value) {
            addCriterion("feeate >", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("feeate >=", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateLessThan(BigDecimal value) {
            addCriterion("feeate <", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("feeate <=", value, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateIn(List<BigDecimal> values) {
            addCriterion("feeate in", values, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateNotIn(List<BigDecimal> values) {
            addCriterion("feeate not in", values, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("feeate between", value1, value2, "feeate");
            return (Criteria) this;
        }

        public Criteria andFeeateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("feeate not between", value1, value2, "feeate");
            return (Criteria) this;
        }

        public Criteria andPlatCostIsNull() {
            addCriterion("platCost is null");
            return (Criteria) this;
        }

        public Criteria andPlatCostIsNotNull() {
            addCriterion("platCost is not null");
            return (Criteria) this;
        }

        public Criteria andPlatCostEqualTo(Long value) {
            addCriterion("platCost =", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostNotEqualTo(Long value) {
            addCriterion("platCost <>", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostGreaterThan(Long value) {
            addCriterion("platCost >", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostGreaterThanOrEqualTo(Long value) {
            addCriterion("platCost >=", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostLessThan(Long value) {
            addCriterion("platCost <", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostLessThanOrEqualTo(Long value) {
            addCriterion("platCost <=", value, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostIn(List<Long> values) {
            addCriterion("platCost in", values, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostNotIn(List<Long> values) {
            addCriterion("platCost not in", values, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostBetween(Long value1, Long value2) {
            addCriterion("platCost between", value1, value2, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatCostNotBetween(Long value1, Long value2) {
            addCriterion("platCost not between", value1, value2, "platCost");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIsNull() {
            addCriterion("platProfit is null");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIsNotNull() {
            addCriterion("platProfit is not null");
            return (Criteria) this;
        }

        public Criteria andPlatProfitEqualTo(Long value) {
            addCriterion("platProfit =", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotEqualTo(Long value) {
            addCriterion("platProfit <>", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitGreaterThan(Long value) {
            addCriterion("platProfit >", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitGreaterThanOrEqualTo(Long value) {
            addCriterion("platProfit >=", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitLessThan(Long value) {
            addCriterion("platProfit <", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitLessThanOrEqualTo(Long value) {
            addCriterion("platProfit <=", value, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitIn(List<Long> values) {
            addCriterion("platProfit in", values, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotIn(List<Long> values) {
            addCriterion("platProfit not in", values, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitBetween(Long value1, Long value2) {
            addCriterion("platProfit between", value1, value2, "platProfit");
            return (Criteria) this;
        }

        public Criteria andPlatProfitNotBetween(Long value1, Long value2) {
            addCriterion("platProfit not between", value1, value2, "platProfit");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("channelId is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("channelId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(String value) {
            addCriterion("channelId =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(String value) {
            addCriterion("channelId <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(String value) {
            addCriterion("channelId >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("channelId >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(String value) {
            addCriterion("channelId <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(String value) {
            addCriterion("channelId <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLike(String value) {
            addCriterion("channelId like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotLike(String value) {
            addCriterion("channelId not like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<String> values) {
            addCriterion("channelId in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<String> values) {
            addCriterion("channelId not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(String value1, String value2) {
            addCriterion("channelId between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(String value1, String value2) {
            addCriterion("channelId not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNull() {
            addCriterion("channelType is null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNotNull() {
            addCriterion("channelType is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeEqualTo(String value) {
            addCriterion("channelType =", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotEqualTo(String value) {
            addCriterion("channelType <>", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThan(String value) {
            addCriterion("channelType >", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThanOrEqualTo(String value) {
            addCriterion("channelType >=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThan(String value) {
            addCriterion("channelType <", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThanOrEqualTo(String value) {
            addCriterion("channelType <=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLike(String value) {
            addCriterion("channelType like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotLike(String value) {
            addCriterion("channelType not like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIn(List<String> values) {
            addCriterion("channelType in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotIn(List<String> values) {
            addCriterion("channelType not in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeBetween(String value1, String value2) {
            addCriterion("channelType between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotBetween(String value1, String value2) {
            addCriterion("channelType not between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeIsNull() {
            addCriterion("paySuccessTime is null");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeIsNotNull() {
            addCriterion("paySuccessTime is not null");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeEqualTo(Date value) {
            addCriterion("paySuccessTime =", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeNotEqualTo(Date value) {
            addCriterion("paySuccessTime <>", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeGreaterThan(Date value) {
            addCriterion("paySuccessTime >", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("paySuccessTime >=", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeLessThan(Date value) {
            addCriterion("paySuccessTime <", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeLessThanOrEqualTo(Date value) {
            addCriterion("paySuccessTime <=", value, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeIn(List<Date> values) {
            addCriterion("paySuccessTime in", values, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeNotIn(List<Date> values) {
            addCriterion("paySuccessTime not in", values, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeBetween(Date value1, Date value2) {
            addCriterion("paySuccessTime between", value1, value2, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andPaySuccessTimeNotBetween(Date value1, Date value2) {
            addCriterion("paySuccessTime not between", value1, value2, "paySuccessTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNull() {
            addCriterion("completeTime is null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNotNull() {
            addCriterion("completeTime is not null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeEqualTo(Date value) {
            addCriterion("completeTime =", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotEqualTo(Date value) {
            addCriterion("completeTime <>", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThan(Date value) {
            addCriterion("completeTime >", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("completeTime >=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThan(Date value) {
            addCriterion("completeTime <", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThanOrEqualTo(Date value) {
            addCriterion("completeTime <=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIn(List<Date> values) {
            addCriterion("completeTime in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotIn(List<Date> values) {
            addCriterion("completeTime not in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeBetween(Date value1, Date value2) {
            addCriterion("completeTime between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotBetween(Date value1, Date value2) {
            addCriterion("completeTime not between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andIsRefundIsNull() {
            addCriterion("isRefund is null");
            return (Criteria) this;
        }

        public Criteria andIsRefundIsNotNull() {
            addCriterion("isRefund is not null");
            return (Criteria) this;
        }

        public Criteria andIsRefundEqualTo(Byte value) {
            addCriterion("isRefund =", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundNotEqualTo(Byte value) {
            addCriterion("isRefund <>", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundGreaterThan(Byte value) {
            addCriterion("isRefund >", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundGreaterThanOrEqualTo(Byte value) {
            addCriterion("isRefund >=", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundLessThan(Byte value) {
            addCriterion("isRefund <", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundLessThanOrEqualTo(Byte value) {
            addCriterion("isRefund <=", value, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundIn(List<Byte> values) {
            addCriterion("isRefund in", values, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundNotIn(List<Byte> values) {
            addCriterion("isRefund not in", values, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundBetween(Byte value1, Byte value2) {
            addCriterion("isRefund between", value1, value2, "isRefund");
            return (Criteria) this;
        }

        public Criteria andIsRefundNotBetween(Byte value1, Byte value2) {
            addCriterion("isRefund not between", value1, value2, "isRefund");
            return (Criteria) this;
        }

        public Criteria andRefundTimesIsNull() {
            addCriterion("refundTimes is null");
            return (Criteria) this;
        }

        public Criteria andRefundTimesIsNotNull() {
            addCriterion("refundTimes is not null");
            return (Criteria) this;
        }

        public Criteria andRefundTimesEqualTo(Integer value) {
            addCriterion("refundTimes =", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesNotEqualTo(Integer value) {
            addCriterion("refundTimes <>", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesGreaterThan(Integer value) {
            addCriterion("refundTimes >", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("refundTimes >=", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesLessThan(Integer value) {
            addCriterion("refundTimes <", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesLessThanOrEqualTo(Integer value) {
            addCriterion("refundTimes <=", value, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesIn(List<Integer> values) {
            addCriterion("refundTimes in", values, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesNotIn(List<Integer> values) {
            addCriterion("refundTimes not in", values, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesBetween(Integer value1, Integer value2) {
            addCriterion("refundTimes between", value1, value2, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andRefundTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("refundTimes not between", value1, value2, "refundTimes");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountIsNull() {
            addCriterion("successRefundAmount is null");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountIsNotNull() {
            addCriterion("successRefundAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountEqualTo(Long value) {
            addCriterion("successRefundAmount =", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountNotEqualTo(Long value) {
            addCriterion("successRefundAmount <>", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountGreaterThan(Long value) {
            addCriterion("successRefundAmount >", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("successRefundAmount >=", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountLessThan(Long value) {
            addCriterion("successRefundAmount <", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountLessThanOrEqualTo(Long value) {
            addCriterion("successRefundAmount <=", value, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountIn(List<Long> values) {
            addCriterion("successRefundAmount in", values, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountNotIn(List<Long> values) {
            addCriterion("successRefundAmount not in", values, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountBetween(Long value1, Long value2) {
            addCriterion("successRefundAmount between", value1, value2, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andSuccessRefundAmountNotBetween(Long value1, Long value2) {
            addCriterion("successRefundAmount not between", value1, value2, "successRefundAmount");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andBatchNoIsNull() {
            addCriterion("batchNo is null");
            return (Criteria) this;
        }

        public Criteria andBatchNoIsNotNull() {
            addCriterion("batchNo is not null");
            return (Criteria) this;
        }

        public Criteria andBatchNoEqualTo(String value) {
            addCriterion("batchNo =", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotEqualTo(String value) {
            addCriterion("batchNo <>", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoGreaterThan(String value) {
            addCriterion("batchNo >", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoGreaterThanOrEqualTo(String value) {
            addCriterion("batchNo >=", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLessThan(String value) {
            addCriterion("batchNo <", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLessThanOrEqualTo(String value) {
            addCriterion("batchNo <=", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoLike(String value) {
            addCriterion("batchNo like", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotLike(String value) {
            addCriterion("batchNo not like", value, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoIn(List<String> values) {
            addCriterion("batchNo in", values, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotIn(List<String> values) {
            addCriterion("batchNo not in", values, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoBetween(String value1, String value2) {
            addCriterion("batchNo between", value1, value2, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBatchNoNotBetween(String value1, String value2) {
            addCriterion("batchNo not between", value1, value2, "batchNo");
            return (Criteria) this;
        }

        public Criteria andBillDateIsNull() {
            addCriterion("billDate is null");
            return (Criteria) this;
        }

        public Criteria andBillDateIsNotNull() {
            addCriterion("billDate is not null");
            return (Criteria) this;
        }

        public Criteria andBillDateEqualTo(Date value) {
            addCriterionForJDBCDate("billDate =", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("billDate <>", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateGreaterThan(Date value) {
            addCriterionForJDBCDate("billDate >", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("billDate >=", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateLessThan(Date value) {
            addCriterionForJDBCDate("billDate <", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("billDate <=", value, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateIn(List<Date> values) {
            addCriterionForJDBCDate("billDate in", values, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("billDate not in", values, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("billDate between", value1, value2, "billDate");
            return (Criteria) this;
        }

        public Criteria andBillDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("billDate not between", value1, value2, "billDate");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("updateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("updateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("updateTime =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("updateTime <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("updateTime >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("updateTime >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("updateTime <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("updateTime <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("updateTime in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("updateTime not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("updateTime between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("updateTime not between", value1, value2, "updateTime");
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