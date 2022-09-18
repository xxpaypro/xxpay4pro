package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CheckMistakeExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public CheckMistakeExample() {
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

        public Criteria andBillTypeIsNull() {
            addCriterion("billType is null");
            return (Criteria) this;
        }

        public Criteria andBillTypeIsNotNull() {
            addCriterion("billType is not null");
            return (Criteria) this;
        }

        public Criteria andBillTypeEqualTo(Byte value) {
            addCriterion("billType =", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeNotEqualTo(Byte value) {
            addCriterion("billType <>", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeGreaterThan(Byte value) {
            addCriterion("billType >", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("billType >=", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeLessThan(Byte value) {
            addCriterion("billType <", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeLessThanOrEqualTo(Byte value) {
            addCriterion("billType <=", value, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeIn(List<Byte> values) {
            addCriterion("billType in", values, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeNotIn(List<Byte> values) {
            addCriterion("billType not in", values, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeBetween(Byte value1, Byte value2) {
            addCriterion("billType between", value1, value2, "billType");
            return (Criteria) this;
        }

        public Criteria andBillTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("billType not between", value1, value2, "billType");
            return (Criteria) this;
        }

        public Criteria andBankTypeIsNull() {
            addCriterion("bankType is null");
            return (Criteria) this;
        }

        public Criteria andBankTypeIsNotNull() {
            addCriterion("bankType is not null");
            return (Criteria) this;
        }

        public Criteria andBankTypeEqualTo(String value) {
            addCriterion("bankType =", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotEqualTo(String value) {
            addCriterion("bankType <>", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThan(String value) {
            addCriterion("bankType >", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThanOrEqualTo(String value) {
            addCriterion("bankType >=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThan(String value) {
            addCriterion("bankType <", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThanOrEqualTo(String value) {
            addCriterion("bankType <=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLike(String value) {
            addCriterion("bankType like", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotLike(String value) {
            addCriterion("bankType not like", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeIn(List<String> values) {
            addCriterion("bankType in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotIn(List<String> values) {
            addCriterion("bankType not in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeBetween(String value1, String value2) {
            addCriterion("bankType between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotBetween(String value1, String value2) {
            addCriterion("bankType not between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIsNull() {
            addCriterion("channelMchId is null");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIsNotNull() {
            addCriterion("channelMchId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdEqualTo(String value) {
            addCriterion("channelMchId =", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotEqualTo(String value) {
            addCriterion("channelMchId <>", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdGreaterThan(String value) {
            addCriterion("channelMchId >", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdGreaterThanOrEqualTo(String value) {
            addCriterion("channelMchId >=", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLessThan(String value) {
            addCriterion("channelMchId <", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLessThanOrEqualTo(String value) {
            addCriterion("channelMchId <=", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLike(String value) {
            addCriterion("channelMchId like", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotLike(String value) {
            addCriterion("channelMchId not like", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIn(List<String> values) {
            addCriterion("channelMchId in", values, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotIn(List<String> values) {
            addCriterion("channelMchId not in", values, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdBetween(String value1, String value2) {
            addCriterion("channelMchId between", value1, value2, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotBetween(String value1, String value2) {
            addCriterion("channelMchId not between", value1, value2, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNull() {
            addCriterion("orderTime is null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIsNotNull() {
            addCriterion("orderTime is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTimeEqualTo(Date value) {
            addCriterion("orderTime =", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotEqualTo(Date value) {
            addCriterion("orderTime <>", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThan(Date value) {
            addCriterion("orderTime >", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("orderTime >=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThan(Date value) {
            addCriterion("orderTime <", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeLessThanOrEqualTo(Date value) {
            addCriterion("orderTime <=", value, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeIn(List<Date> values) {
            addCriterion("orderTime in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotIn(List<Date> values) {
            addCriterion("orderTime not in", values, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeBetween(Date value1, Date value2) {
            addCriterion("orderTime between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andOrderTimeNotBetween(Date value1, Date value2) {
            addCriterion("orderTime not between", value1, value2, "orderTime");
            return (Criteria) this;
        }

        public Criteria andMchIdIsNull() {
            addCriterion("mchId is null");
            return (Criteria) this;
        }

        public Criteria andMchIdIsNotNull() {
            addCriterion("mchId is not null");
            return (Criteria) this;
        }

        public Criteria andMchIdEqualTo(Long value) {
            addCriterion("mchId =", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotEqualTo(Long value) {
            addCriterion("mchId <>", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThan(Long value) {
            addCriterion("mchId >", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdGreaterThanOrEqualTo(Long value) {
            addCriterion("mchId >=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThan(Long value) {
            addCriterion("mchId <", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdLessThanOrEqualTo(Long value) {
            addCriterion("mchId <=", value, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdIn(List<Long> values) {
            addCriterion("mchId in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotIn(List<Long> values) {
            addCriterion("mchId not in", values, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdBetween(Long value1, Long value2) {
            addCriterion("mchId between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchIdNotBetween(Long value1, Long value2) {
            addCriterion("mchId not between", value1, value2, "mchId");
            return (Criteria) this;
        }

        public Criteria andMchNameIsNull() {
            addCriterion("mchName is null");
            return (Criteria) this;
        }

        public Criteria andMchNameIsNotNull() {
            addCriterion("mchName is not null");
            return (Criteria) this;
        }

        public Criteria andMchNameEqualTo(String value) {
            addCriterion("mchName =", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameNotEqualTo(String value) {
            addCriterion("mchName <>", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameGreaterThan(String value) {
            addCriterion("mchName >", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameGreaterThanOrEqualTo(String value) {
            addCriterion("mchName >=", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameLessThan(String value) {
            addCriterion("mchName <", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameLessThanOrEqualTo(String value) {
            addCriterion("mchName <=", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameLike(String value) {
            addCriterion("mchName like", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameNotLike(String value) {
            addCriterion("mchName not like", value, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameIn(List<String> values) {
            addCriterion("mchName in", values, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameNotIn(List<String> values) {
            addCriterion("mchName not in", values, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameBetween(String value1, String value2) {
            addCriterion("mchName between", value1, value2, "mchName");
            return (Criteria) this;
        }

        public Criteria andMchNameNotBetween(String value1, String value2) {
            addCriterion("mchName not between", value1, value2, "mchName");
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

        public Criteria andTradeTimeIsNull() {
            addCriterion("tradeTime is null");
            return (Criteria) this;
        }

        public Criteria andTradeTimeIsNotNull() {
            addCriterion("tradeTime is not null");
            return (Criteria) this;
        }

        public Criteria andTradeTimeEqualTo(Date value) {
            addCriterion("tradeTime =", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotEqualTo(Date value) {
            addCriterion("tradeTime <>", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeGreaterThan(Date value) {
            addCriterion("tradeTime >", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("tradeTime >=", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeLessThan(Date value) {
            addCriterion("tradeTime <", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeLessThanOrEqualTo(Date value) {
            addCriterion("tradeTime <=", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeIn(List<Date> values) {
            addCriterion("tradeTime in", values, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotIn(List<Date> values) {
            addCriterion("tradeTime not in", values, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeBetween(Date value1, Date value2) {
            addCriterion("tradeTime between", value1, value2, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotBetween(Date value1, Date value2) {
            addCriterion("tradeTime not between", value1, value2, "tradeTime");
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

        public Criteria andRefundAmountIsNull() {
            addCriterion("refundAmount is null");
            return (Criteria) this;
        }

        public Criteria andRefundAmountIsNotNull() {
            addCriterion("refundAmount is not null");
            return (Criteria) this;
        }

        public Criteria andRefundAmountEqualTo(Long value) {
            addCriterion("refundAmount =", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotEqualTo(Long value) {
            addCriterion("refundAmount <>", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountGreaterThan(Long value) {
            addCriterion("refundAmount >", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("refundAmount >=", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountLessThan(Long value) {
            addCriterion("refundAmount <", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountLessThanOrEqualTo(Long value) {
            addCriterion("refundAmount <=", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountIn(List<Long> values) {
            addCriterion("refundAmount in", values, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotIn(List<Long> values) {
            addCriterion("refundAmount not in", values, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountBetween(Long value1, Long value2) {
            addCriterion("refundAmount between", value1, value2, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotBetween(Long value1, Long value2) {
            addCriterion("refundAmount not between", value1, value2, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("orderStatus is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("orderStatus is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(Byte value) {
            addCriterion("orderStatus =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(Byte value) {
            addCriterion("orderStatus <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(Byte value) {
            addCriterion("orderStatus >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("orderStatus >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(Byte value) {
            addCriterion("orderStatus <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(Byte value) {
            addCriterion("orderStatus <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<Byte> values) {
            addCriterion("orderStatus in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<Byte> values) {
            addCriterion("orderStatus not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(Byte value1, Byte value2) {
            addCriterion("orderStatus between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("orderStatus not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(Long value) {
            addCriterion("fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(Long value) {
            addCriterion("fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(Long value) {
            addCriterion("fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(Long value) {
            addCriterion("fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(Long value) {
            addCriterion("fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<Long> values) {
            addCriterion("fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<Long> values) {
            addCriterion("fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(Long value1, Long value2) {
            addCriterion("fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(Long value1, Long value2) {
            addCriterion("fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeIsNull() {
            addCriterion("bankTradeTime is null");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeIsNotNull() {
            addCriterion("bankTradeTime is not null");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeEqualTo(Date value) {
            addCriterion("bankTradeTime =", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeNotEqualTo(Date value) {
            addCriterion("bankTradeTime <>", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeGreaterThan(Date value) {
            addCriterion("bankTradeTime >", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("bankTradeTime >=", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeLessThan(Date value) {
            addCriterion("bankTradeTime <", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeLessThanOrEqualTo(Date value) {
            addCriterion("bankTradeTime <=", value, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeIn(List<Date> values) {
            addCriterion("bankTradeTime in", values, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeNotIn(List<Date> values) {
            addCriterion("bankTradeTime not in", values, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeBetween(Date value1, Date value2) {
            addCriterion("bankTradeTime between", value1, value2, "bankTradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTradeTimeNotBetween(Date value1, Date value2) {
            addCriterion("bankTradeTime not between", value1, value2, "bankTradeTime");
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

        public Criteria andBankOrderStatusIsNull() {
            addCriterion("bankOrderStatus is null");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusIsNotNull() {
            addCriterion("bankOrderStatus is not null");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusEqualTo(String value) {
            addCriterion("bankOrderStatus =", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusNotEqualTo(String value) {
            addCriterion("bankOrderStatus <>", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusGreaterThan(String value) {
            addCriterion("bankOrderStatus >", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusGreaterThanOrEqualTo(String value) {
            addCriterion("bankOrderStatus >=", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusLessThan(String value) {
            addCriterion("bankOrderStatus <", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusLessThanOrEqualTo(String value) {
            addCriterion("bankOrderStatus <=", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusLike(String value) {
            addCriterion("bankOrderStatus like", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusNotLike(String value) {
            addCriterion("bankOrderStatus not like", value, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusIn(List<String> values) {
            addCriterion("bankOrderStatus in", values, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusNotIn(List<String> values) {
            addCriterion("bankOrderStatus not in", values, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusBetween(String value1, String value2) {
            addCriterion("bankOrderStatus between", value1, value2, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankOrderStatusNotBetween(String value1, String value2) {
            addCriterion("bankOrderStatus not between", value1, value2, "bankOrderStatus");
            return (Criteria) this;
        }

        public Criteria andBankAmountIsNull() {
            addCriterion("bankAmount is null");
            return (Criteria) this;
        }

        public Criteria andBankAmountIsNotNull() {
            addCriterion("bankAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBankAmountEqualTo(Long value) {
            addCriterion("bankAmount =", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotEqualTo(Long value) {
            addCriterion("bankAmount <>", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountGreaterThan(Long value) {
            addCriterion("bankAmount >", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("bankAmount >=", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountLessThan(Long value) {
            addCriterion("bankAmount <", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountLessThanOrEqualTo(Long value) {
            addCriterion("bankAmount <=", value, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountIn(List<Long> values) {
            addCriterion("bankAmount in", values, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotIn(List<Long> values) {
            addCriterion("bankAmount not in", values, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountBetween(Long value1, Long value2) {
            addCriterion("bankAmount between", value1, value2, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankAmountNotBetween(Long value1, Long value2) {
            addCriterion("bankAmount not between", value1, value2, "bankAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountIsNull() {
            addCriterion("bankRefundAmount is null");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountIsNotNull() {
            addCriterion("bankRefundAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountEqualTo(Long value) {
            addCriterion("bankRefundAmount =", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountNotEqualTo(Long value) {
            addCriterion("bankRefundAmount <>", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountGreaterThan(Long value) {
            addCriterion("bankRefundAmount >", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("bankRefundAmount >=", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountLessThan(Long value) {
            addCriterion("bankRefundAmount <", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountLessThanOrEqualTo(Long value) {
            addCriterion("bankRefundAmount <=", value, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountIn(List<Long> values) {
            addCriterion("bankRefundAmount in", values, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountNotIn(List<Long> values) {
            addCriterion("bankRefundAmount not in", values, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountBetween(Long value1, Long value2) {
            addCriterion("bankRefundAmount between", value1, value2, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankRefundAmountNotBetween(Long value1, Long value2) {
            addCriterion("bankRefundAmount not between", value1, value2, "bankRefundAmount");
            return (Criteria) this;
        }

        public Criteria andBankFeeIsNull() {
            addCriterion("bankFee is null");
            return (Criteria) this;
        }

        public Criteria andBankFeeIsNotNull() {
            addCriterion("bankFee is not null");
            return (Criteria) this;
        }

        public Criteria andBankFeeEqualTo(Long value) {
            addCriterion("bankFee =", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeNotEqualTo(Long value) {
            addCriterion("bankFee <>", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeGreaterThan(Long value) {
            addCriterion("bankFee >", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("bankFee >=", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeLessThan(Long value) {
            addCriterion("bankFee <", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeLessThanOrEqualTo(Long value) {
            addCriterion("bankFee <=", value, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeIn(List<Long> values) {
            addCriterion("bankFee in", values, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeNotIn(List<Long> values) {
            addCriterion("bankFee not in", values, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeBetween(Long value1, Long value2) {
            addCriterion("bankFee between", value1, value2, "bankFee");
            return (Criteria) this;
        }

        public Criteria andBankFeeNotBetween(Long value1, Long value2) {
            addCriterion("bankFee not between", value1, value2, "bankFee");
            return (Criteria) this;
        }

        public Criteria andErrTypeIsNull() {
            addCriterion("errType is null");
            return (Criteria) this;
        }

        public Criteria andErrTypeIsNotNull() {
            addCriterion("errType is not null");
            return (Criteria) this;
        }

        public Criteria andErrTypeEqualTo(Byte value) {
            addCriterion("errType =", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeNotEqualTo(Byte value) {
            addCriterion("errType <>", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeGreaterThan(Byte value) {
            addCriterion("errType >", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("errType >=", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeLessThan(Byte value) {
            addCriterion("errType <", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeLessThanOrEqualTo(Byte value) {
            addCriterion("errType <=", value, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeIn(List<Byte> values) {
            addCriterion("errType in", values, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeNotIn(List<Byte> values) {
            addCriterion("errType not in", values, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeBetween(Byte value1, Byte value2) {
            addCriterion("errType between", value1, value2, "errType");
            return (Criteria) this;
        }

        public Criteria andErrTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("errType not between", value1, value2, "errType");
            return (Criteria) this;
        }

        public Criteria andHandleStatusIsNull() {
            addCriterion("handleStatus is null");
            return (Criteria) this;
        }

        public Criteria andHandleStatusIsNotNull() {
            addCriterion("handleStatus is not null");
            return (Criteria) this;
        }

        public Criteria andHandleStatusEqualTo(Byte value) {
            addCriterion("handleStatus =", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusNotEqualTo(Byte value) {
            addCriterion("handleStatus <>", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusGreaterThan(Byte value) {
            addCriterion("handleStatus >", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("handleStatus >=", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusLessThan(Byte value) {
            addCriterion("handleStatus <", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusLessThanOrEqualTo(Byte value) {
            addCriterion("handleStatus <=", value, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusIn(List<Byte> values) {
            addCriterion("handleStatus in", values, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusNotIn(List<Byte> values) {
            addCriterion("handleStatus not in", values, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusBetween(Byte value1, Byte value2) {
            addCriterion("handleStatus between", value1, value2, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("handleStatus not between", value1, value2, "handleStatus");
            return (Criteria) this;
        }

        public Criteria andHandleValueIsNull() {
            addCriterion("handleValue is null");
            return (Criteria) this;
        }

        public Criteria andHandleValueIsNotNull() {
            addCriterion("handleValue is not null");
            return (Criteria) this;
        }

        public Criteria andHandleValueEqualTo(String value) {
            addCriterion("handleValue =", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueNotEqualTo(String value) {
            addCriterion("handleValue <>", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueGreaterThan(String value) {
            addCriterion("handleValue >", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueGreaterThanOrEqualTo(String value) {
            addCriterion("handleValue >=", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueLessThan(String value) {
            addCriterion("handleValue <", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueLessThanOrEqualTo(String value) {
            addCriterion("handleValue <=", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueLike(String value) {
            addCriterion("handleValue like", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueNotLike(String value) {
            addCriterion("handleValue not like", value, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueIn(List<String> values) {
            addCriterion("handleValue in", values, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueNotIn(List<String> values) {
            addCriterion("handleValue not in", values, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueBetween(String value1, String value2) {
            addCriterion("handleValue between", value1, value2, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleValueNotBetween(String value1, String value2) {
            addCriterion("handleValue not between", value1, value2, "handleValue");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkIsNull() {
            addCriterion("handleRemark is null");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkIsNotNull() {
            addCriterion("handleRemark is not null");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkEqualTo(String value) {
            addCriterion("handleRemark =", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkNotEqualTo(String value) {
            addCriterion("handleRemark <>", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkGreaterThan(String value) {
            addCriterion("handleRemark >", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("handleRemark >=", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkLessThan(String value) {
            addCriterion("handleRemark <", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkLessThanOrEqualTo(String value) {
            addCriterion("handleRemark <=", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkLike(String value) {
            addCriterion("handleRemark like", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkNotLike(String value) {
            addCriterion("handleRemark not like", value, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkIn(List<String> values) {
            addCriterion("handleRemark in", values, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkNotIn(List<String> values) {
            addCriterion("handleRemark not in", values, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkBetween(String value1, String value2) {
            addCriterion("handleRemark between", value1, value2, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andHandleRemarkNotBetween(String value1, String value2) {
            addCriterion("handleRemark not between", value1, value2, "handleRemark");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNull() {
            addCriterion("operatorName is null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNotNull() {
            addCriterion("operatorName is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameEqualTo(String value) {
            addCriterion("operatorName =", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotEqualTo(String value) {
            addCriterion("operatorName <>", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThan(String value) {
            addCriterion("operatorName >", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThanOrEqualTo(String value) {
            addCriterion("operatorName >=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThan(String value) {
            addCriterion("operatorName <", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThanOrEqualTo(String value) {
            addCriterion("operatorName <=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLike(String value) {
            addCriterion("operatorName like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotLike(String value) {
            addCriterion("operatorName not like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIn(List<String> values) {
            addCriterion("operatorName in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotIn(List<String> values) {
            addCriterion("operatorName not in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameBetween(String value1, String value2) {
            addCriterion("operatorName between", value1, value2, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotBetween(String value1, String value2) {
            addCriterion("operatorName not between", value1, value2, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdIsNull() {
            addCriterion("operatorUserId is null");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdIsNotNull() {
            addCriterion("operatorUserId is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdEqualTo(String value) {
            addCriterion("operatorUserId =", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdNotEqualTo(String value) {
            addCriterion("operatorUserId <>", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdGreaterThan(String value) {
            addCriterion("operatorUserId >", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("operatorUserId >=", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdLessThan(String value) {
            addCriterion("operatorUserId <", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdLessThanOrEqualTo(String value) {
            addCriterion("operatorUserId <=", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdLike(String value) {
            addCriterion("operatorUserId like", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdNotLike(String value) {
            addCriterion("operatorUserId not like", value, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdIn(List<String> values) {
            addCriterion("operatorUserId in", values, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdNotIn(List<String> values) {
            addCriterion("operatorUserId not in", values, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdBetween(String value1, String value2) {
            addCriterion("operatorUserId between", value1, value2, "operatorUserId");
            return (Criteria) this;
        }

        public Criteria andOperatorUserIdNotBetween(String value1, String value2) {
            addCriterion("operatorUserId not between", value1, value2, "operatorUserId");
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