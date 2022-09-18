package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CheckBatchExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public CheckBatchExample() {
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

        public Criteria andMistakeCountIsNull() {
            addCriterion("mistakeCount is null");
            return (Criteria) this;
        }

        public Criteria andMistakeCountIsNotNull() {
            addCriterion("mistakeCount is not null");
            return (Criteria) this;
        }

        public Criteria andMistakeCountEqualTo(Integer value) {
            addCriterion("mistakeCount =", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountNotEqualTo(Integer value) {
            addCriterion("mistakeCount <>", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountGreaterThan(Integer value) {
            addCriterion("mistakeCount >", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("mistakeCount >=", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountLessThan(Integer value) {
            addCriterion("mistakeCount <", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountLessThanOrEqualTo(Integer value) {
            addCriterion("mistakeCount <=", value, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountIn(List<Integer> values) {
            addCriterion("mistakeCount in", values, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountNotIn(List<Integer> values) {
            addCriterion("mistakeCount not in", values, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountBetween(Integer value1, Integer value2) {
            addCriterion("mistakeCount between", value1, value2, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andMistakeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("mistakeCount not between", value1, value2, "mistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountIsNull() {
            addCriterion("unhandleMistakeCount is null");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountIsNotNull() {
            addCriterion("unhandleMistakeCount is not null");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountEqualTo(Integer value) {
            addCriterion("unhandleMistakeCount =", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountNotEqualTo(Integer value) {
            addCriterion("unhandleMistakeCount <>", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountGreaterThan(Integer value) {
            addCriterion("unhandleMistakeCount >", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("unhandleMistakeCount >=", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountLessThan(Integer value) {
            addCriterion("unhandleMistakeCount <", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountLessThanOrEqualTo(Integer value) {
            addCriterion("unhandleMistakeCount <=", value, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountIn(List<Integer> values) {
            addCriterion("unhandleMistakeCount in", values, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountNotIn(List<Integer> values) {
            addCriterion("unhandleMistakeCount not in", values, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountBetween(Integer value1, Integer value2) {
            addCriterion("unhandleMistakeCount between", value1, value2, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andUnhandleMistakeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("unhandleMistakeCount not between", value1, value2, "unhandleMistakeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountIsNull() {
            addCriterion("tradeCount is null");
            return (Criteria) this;
        }

        public Criteria andTradeCountIsNotNull() {
            addCriterion("tradeCount is not null");
            return (Criteria) this;
        }

        public Criteria andTradeCountEqualTo(Integer value) {
            addCriterion("tradeCount =", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountNotEqualTo(Integer value) {
            addCriterion("tradeCount <>", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountGreaterThan(Integer value) {
            addCriterion("tradeCount >", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("tradeCount >=", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountLessThan(Integer value) {
            addCriterion("tradeCount <", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountLessThanOrEqualTo(Integer value) {
            addCriterion("tradeCount <=", value, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountIn(List<Integer> values) {
            addCriterion("tradeCount in", values, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountNotIn(List<Integer> values) {
            addCriterion("tradeCount not in", values, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountBetween(Integer value1, Integer value2) {
            addCriterion("tradeCount between", value1, value2, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("tradeCount not between", value1, value2, "tradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountIsNull() {
            addCriterion("bankTradeCount is null");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountIsNotNull() {
            addCriterion("bankTradeCount is not null");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountEqualTo(Integer value) {
            addCriterion("bankTradeCount =", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountNotEqualTo(Integer value) {
            addCriterion("bankTradeCount <>", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountGreaterThan(Integer value) {
            addCriterion("bankTradeCount >", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("bankTradeCount >=", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountLessThan(Integer value) {
            addCriterion("bankTradeCount <", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountLessThanOrEqualTo(Integer value) {
            addCriterion("bankTradeCount <=", value, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountIn(List<Integer> values) {
            addCriterion("bankTradeCount in", values, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountNotIn(List<Integer> values) {
            addCriterion("bankTradeCount not in", values, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountBetween(Integer value1, Integer value2) {
            addCriterion("bankTradeCount between", value1, value2, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andBankTradeCountNotBetween(Integer value1, Integer value2) {
            addCriterion("bankTradeCount not between", value1, value2, "bankTradeCount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountIsNull() {
            addCriterion("tradeAmount is null");
            return (Criteria) this;
        }

        public Criteria andTradeAmountIsNotNull() {
            addCriterion("tradeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andTradeAmountEqualTo(Long value) {
            addCriterion("tradeAmount =", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountNotEqualTo(Long value) {
            addCriterion("tradeAmount <>", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountGreaterThan(Long value) {
            addCriterion("tradeAmount >", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("tradeAmount >=", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountLessThan(Long value) {
            addCriterion("tradeAmount <", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountLessThanOrEqualTo(Long value) {
            addCriterion("tradeAmount <=", value, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountIn(List<Long> values) {
            addCriterion("tradeAmount in", values, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountNotIn(List<Long> values) {
            addCriterion("tradeAmount not in", values, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountBetween(Long value1, Long value2) {
            addCriterion("tradeAmount between", value1, value2, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andTradeAmountNotBetween(Long value1, Long value2) {
            addCriterion("tradeAmount not between", value1, value2, "tradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountIsNull() {
            addCriterion("bankTradeAmount is null");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountIsNotNull() {
            addCriterion("bankTradeAmount is not null");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountEqualTo(Long value) {
            addCriterion("bankTradeAmount =", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountNotEqualTo(Long value) {
            addCriterion("bankTradeAmount <>", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountGreaterThan(Long value) {
            addCriterion("bankTradeAmount >", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("bankTradeAmount >=", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountLessThan(Long value) {
            addCriterion("bankTradeAmount <", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountLessThanOrEqualTo(Long value) {
            addCriterion("bankTradeAmount <=", value, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountIn(List<Long> values) {
            addCriterion("bankTradeAmount in", values, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountNotIn(List<Long> values) {
            addCriterion("bankTradeAmount not in", values, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountBetween(Long value1, Long value2) {
            addCriterion("bankTradeAmount between", value1, value2, "bankTradeAmount");
            return (Criteria) this;
        }

        public Criteria andBankTradeAmountNotBetween(Long value1, Long value2) {
            addCriterion("bankTradeAmount not between", value1, value2, "bankTradeAmount");
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

        public Criteria andOrgCheckFilePathIsNull() {
            addCriterion("orgCheckFilePath is null");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathIsNotNull() {
            addCriterion("orgCheckFilePath is not null");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathEqualTo(String value) {
            addCriterion("orgCheckFilePath =", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathNotEqualTo(String value) {
            addCriterion("orgCheckFilePath <>", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathGreaterThan(String value) {
            addCriterion("orgCheckFilePath >", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("orgCheckFilePath >=", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathLessThan(String value) {
            addCriterion("orgCheckFilePath <", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathLessThanOrEqualTo(String value) {
            addCriterion("orgCheckFilePath <=", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathLike(String value) {
            addCriterion("orgCheckFilePath like", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathNotLike(String value) {
            addCriterion("orgCheckFilePath not like", value, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathIn(List<String> values) {
            addCriterion("orgCheckFilePath in", values, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathNotIn(List<String> values) {
            addCriterion("orgCheckFilePath not in", values, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathBetween(String value1, String value2) {
            addCriterion("orgCheckFilePath between", value1, value2, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andOrgCheckFilePathNotBetween(String value1, String value2) {
            addCriterion("orgCheckFilePath not between", value1, value2, "orgCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathIsNull() {
            addCriterion("releaseCheckFilePath is null");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathIsNotNull() {
            addCriterion("releaseCheckFilePath is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathEqualTo(String value) {
            addCriterion("releaseCheckFilePath =", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathNotEqualTo(String value) {
            addCriterion("releaseCheckFilePath <>", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathGreaterThan(String value) {
            addCriterion("releaseCheckFilePath >", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("releaseCheckFilePath >=", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathLessThan(String value) {
            addCriterion("releaseCheckFilePath <", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathLessThanOrEqualTo(String value) {
            addCriterion("releaseCheckFilePath <=", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathLike(String value) {
            addCriterion("releaseCheckFilePath like", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathNotLike(String value) {
            addCriterion("releaseCheckFilePath not like", value, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathIn(List<String> values) {
            addCriterion("releaseCheckFilePath in", values, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathNotIn(List<String> values) {
            addCriterion("releaseCheckFilePath not in", values, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathBetween(String value1, String value2) {
            addCriterion("releaseCheckFilePath between", value1, value2, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseCheckFilePathNotBetween(String value1, String value2) {
            addCriterion("releaseCheckFilePath not between", value1, value2, "releaseCheckFilePath");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusIsNull() {
            addCriterion("releaseStatus is null");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusIsNotNull() {
            addCriterion("releaseStatus is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusEqualTo(Byte value) {
            addCriterion("releaseStatus =", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusNotEqualTo(Byte value) {
            addCriterion("releaseStatus <>", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusGreaterThan(Byte value) {
            addCriterion("releaseStatus >", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("releaseStatus >=", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusLessThan(Byte value) {
            addCriterion("releaseStatus <", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusLessThanOrEqualTo(Byte value) {
            addCriterion("releaseStatus <=", value, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusIn(List<Byte> values) {
            addCriterion("releaseStatus in", values, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusNotIn(List<Byte> values) {
            addCriterion("releaseStatus not in", values, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusBetween(Byte value1, Byte value2) {
            addCriterion("releaseStatus between", value1, value2, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andReleaseStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("releaseStatus not between", value1, value2, "releaseStatus");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgIsNull() {
            addCriterion("checkFailMsg is null");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgIsNotNull() {
            addCriterion("checkFailMsg is not null");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgEqualTo(String value) {
            addCriterion("checkFailMsg =", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgNotEqualTo(String value) {
            addCriterion("checkFailMsg <>", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgGreaterThan(String value) {
            addCriterion("checkFailMsg >", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgGreaterThanOrEqualTo(String value) {
            addCriterion("checkFailMsg >=", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgLessThan(String value) {
            addCriterion("checkFailMsg <", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgLessThanOrEqualTo(String value) {
            addCriterion("checkFailMsg <=", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgLike(String value) {
            addCriterion("checkFailMsg like", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgNotLike(String value) {
            addCriterion("checkFailMsg not like", value, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgIn(List<String> values) {
            addCriterion("checkFailMsg in", values, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgNotIn(List<String> values) {
            addCriterion("checkFailMsg not in", values, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgBetween(String value1, String value2) {
            addCriterion("checkFailMsg between", value1, value2, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andCheckFailMsgNotBetween(String value1, String value2) {
            addCriterion("checkFailMsg not between", value1, value2, "checkFailMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgIsNull() {
            addCriterion("bankErrMsg is null");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgIsNotNull() {
            addCriterion("bankErrMsg is not null");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgEqualTo(String value) {
            addCriterion("bankErrMsg =", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgNotEqualTo(String value) {
            addCriterion("bankErrMsg <>", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgGreaterThan(String value) {
            addCriterion("bankErrMsg >", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgGreaterThanOrEqualTo(String value) {
            addCriterion("bankErrMsg >=", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgLessThan(String value) {
            addCriterion("bankErrMsg <", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgLessThanOrEqualTo(String value) {
            addCriterion("bankErrMsg <=", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgLike(String value) {
            addCriterion("bankErrMsg like", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgNotLike(String value) {
            addCriterion("bankErrMsg not like", value, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgIn(List<String> values) {
            addCriterion("bankErrMsg in", values, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgNotIn(List<String> values) {
            addCriterion("bankErrMsg not in", values, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgBetween(String value1, String value2) {
            addCriterion("bankErrMsg between", value1, value2, "bankErrMsg");
            return (Criteria) this;
        }

        public Criteria andBankErrMsgNotBetween(String value1, String value2) {
            addCriterion("bankErrMsg not between", value1, value2, "bankErrMsg");
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