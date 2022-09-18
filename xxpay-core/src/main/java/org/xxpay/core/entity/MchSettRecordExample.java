package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MchSettRecordExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchSettRecordExample() {
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

        public Criteria andNameIsNull() {
            addCriterion("Name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("Name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("Name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("Name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("Name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("Name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("Name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("Name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("Name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("Name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("Name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("Name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("Name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("Name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andSettModeIsNull() {
            addCriterion("SettMode is null");
            return (Criteria) this;
        }

        public Criteria andSettModeIsNotNull() {
            addCriterion("SettMode is not null");
            return (Criteria) this;
        }

        public Criteria andSettModeEqualTo(Byte value) {
            addCriterion("SettMode =", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotEqualTo(Byte value) {
            addCriterion("SettMode <>", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeGreaterThan(Byte value) {
            addCriterion("SettMode >", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettMode >=", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeLessThan(Byte value) {
            addCriterion("SettMode <", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeLessThanOrEqualTo(Byte value) {
            addCriterion("SettMode <=", value, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeIn(List<Byte> values) {
            addCriterion("SettMode in", values, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotIn(List<Byte> values) {
            addCriterion("SettMode not in", values, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeBetween(Byte value1, Byte value2) {
            addCriterion("SettMode between", value1, value2, "settMode");
            return (Criteria) this;
        }

        public Criteria andSettModeNotBetween(Byte value1, Byte value2) {
            addCriterion("SettMode not between", value1, value2, "settMode");
            return (Criteria) this;
        }

        public Criteria andMchTypeIsNull() {
            addCriterion("MchType is null");
            return (Criteria) this;
        }

        public Criteria andMchTypeIsNotNull() {
            addCriterion("MchType is not null");
            return (Criteria) this;
        }

        public Criteria andMchTypeEqualTo(Byte value) {
            addCriterion("MchType =", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotEqualTo(Byte value) {
            addCriterion("MchType <>", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeGreaterThan(Byte value) {
            addCriterion("MchType >", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("MchType >=", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeLessThan(Byte value) {
            addCriterion("MchType <", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeLessThanOrEqualTo(Byte value) {
            addCriterion("MchType <=", value, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeIn(List<Byte> values) {
            addCriterion("MchType in", values, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotIn(List<Byte> values) {
            addCriterion("MchType not in", values, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeBetween(Byte value1, Byte value2) {
            addCriterion("MchType between", value1, value2, "mchType");
            return (Criteria) this;
        }

        public Criteria andMchTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("MchType not between", value1, value2, "mchType");
            return (Criteria) this;
        }

        public Criteria andSettDateIsNull() {
            addCriterion("SettDate is null");
            return (Criteria) this;
        }

        public Criteria andSettDateIsNotNull() {
            addCriterion("SettDate is not null");
            return (Criteria) this;
        }

        public Criteria andSettDateEqualTo(Date value) {
            addCriterionForJDBCDate("SettDate =", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("SettDate <>", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateGreaterThan(Date value) {
            addCriterionForJDBCDate("SettDate >", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("SettDate >=", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateLessThan(Date value) {
            addCriterionForJDBCDate("SettDate <", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("SettDate <=", value, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateIn(List<Date> values) {
            addCriterionForJDBCDate("SettDate in", values, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("SettDate not in", values, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("SettDate between", value1, value2, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("SettDate not between", value1, value2, "settDate");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNull() {
            addCriterion("SettAmount is null");
            return (Criteria) this;
        }

        public Criteria andSettAmountIsNotNull() {
            addCriterion("SettAmount is not null");
            return (Criteria) this;
        }

        public Criteria andSettAmountEqualTo(Long value) {
            addCriterion("SettAmount =", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotEqualTo(Long value) {
            addCriterion("SettAmount <>", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThan(Long value) {
            addCriterion("SettAmount >", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("SettAmount >=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThan(Long value) {
            addCriterion("SettAmount <", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountLessThanOrEqualTo(Long value) {
            addCriterion("SettAmount <=", value, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountIn(List<Long> values) {
            addCriterion("SettAmount in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotIn(List<Long> values) {
            addCriterion("SettAmount not in", values, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountBetween(Long value1, Long value2) {
            addCriterion("SettAmount between", value1, value2, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettAmountNotBetween(Long value1, Long value2) {
            addCriterion("SettAmount not between", value1, value2, "settAmount");
            return (Criteria) this;
        }

        public Criteria andSettFeeIsNull() {
            addCriterion("SettFee is null");
            return (Criteria) this;
        }

        public Criteria andSettFeeIsNotNull() {
            addCriterion("SettFee is not null");
            return (Criteria) this;
        }

        public Criteria andSettFeeEqualTo(Long value) {
            addCriterion("SettFee =", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeNotEqualTo(Long value) {
            addCriterion("SettFee <>", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeGreaterThan(Long value) {
            addCriterion("SettFee >", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeGreaterThanOrEqualTo(Long value) {
            addCriterion("SettFee >=", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeLessThan(Long value) {
            addCriterion("SettFee <", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeLessThanOrEqualTo(Long value) {
            addCriterion("SettFee <=", value, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeIn(List<Long> values) {
            addCriterion("SettFee in", values, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeNotIn(List<Long> values) {
            addCriterion("SettFee not in", values, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeBetween(Long value1, Long value2) {
            addCriterion("SettFee between", value1, value2, "settFee");
            return (Criteria) this;
        }

        public Criteria andSettFeeNotBetween(Long value1, Long value2) {
            addCriterion("SettFee not between", value1, value2, "settFee");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIsNull() {
            addCriterion("RemitAmount is null");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIsNotNull() {
            addCriterion("RemitAmount is not null");
            return (Criteria) this;
        }

        public Criteria andRemitAmountEqualTo(Long value) {
            addCriterion("RemitAmount =", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotEqualTo(Long value) {
            addCriterion("RemitAmount <>", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountGreaterThan(Long value) {
            addCriterion("RemitAmount >", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("RemitAmount >=", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountLessThan(Long value) {
            addCriterion("RemitAmount <", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountLessThanOrEqualTo(Long value) {
            addCriterion("RemitAmount <=", value, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountIn(List<Long> values) {
            addCriterion("RemitAmount in", values, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotIn(List<Long> values) {
            addCriterion("RemitAmount not in", values, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountBetween(Long value1, Long value2) {
            addCriterion("RemitAmount between", value1, value2, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitAmountNotBetween(Long value1, Long value2) {
            addCriterion("RemitAmount not between", value1, value2, "remitAmount");
            return (Criteria) this;
        }

        public Criteria andRemitTypeIsNull() {
            addCriterion("RemitType is null");
            return (Criteria) this;
        }

        public Criteria andRemitTypeIsNotNull() {
            addCriterion("RemitType is not null");
            return (Criteria) this;
        }

        public Criteria andRemitTypeEqualTo(Byte value) {
            addCriterion("RemitType =", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeNotEqualTo(Byte value) {
            addCriterion("RemitType <>", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeGreaterThan(Byte value) {
            addCriterion("RemitType >", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("RemitType >=", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeLessThan(Byte value) {
            addCriterion("RemitType <", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeLessThanOrEqualTo(Byte value) {
            addCriterion("RemitType <=", value, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeIn(List<Byte> values) {
            addCriterion("RemitType in", values, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeNotIn(List<Byte> values) {
            addCriterion("RemitType not in", values, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeBetween(Byte value1, Byte value2) {
            addCriterion("RemitType between", value1, value2, "remitType");
            return (Criteria) this;
        }

        public Criteria andRemitTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("RemitType not between", value1, value2, "remitType");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoIsNull() {
            addCriterion("MchRemitInfo is null");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoIsNotNull() {
            addCriterion("MchRemitInfo is not null");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoEqualTo(String value) {
            addCriterion("MchRemitInfo =", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoNotEqualTo(String value) {
            addCriterion("MchRemitInfo <>", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoGreaterThan(String value) {
            addCriterion("MchRemitInfo >", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoGreaterThanOrEqualTo(String value) {
            addCriterion("MchRemitInfo >=", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoLessThan(String value) {
            addCriterion("MchRemitInfo <", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoLessThanOrEqualTo(String value) {
            addCriterion("MchRemitInfo <=", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoLike(String value) {
            addCriterion("MchRemitInfo like", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoNotLike(String value) {
            addCriterion("MchRemitInfo not like", value, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoIn(List<String> values) {
            addCriterion("MchRemitInfo in", values, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoNotIn(List<String> values) {
            addCriterion("MchRemitInfo not in", values, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoBetween(String value1, String value2) {
            addCriterion("MchRemitInfo between", value1, value2, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andMchRemitInfoNotBetween(String value1, String value2) {
            addCriterion("MchRemitInfo not between", value1, value2, "mchRemitInfo");
            return (Criteria) this;
        }

        public Criteria andSettStatusIsNull() {
            addCriterion("SettStatus is null");
            return (Criteria) this;
        }

        public Criteria andSettStatusIsNotNull() {
            addCriterion("SettStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSettStatusEqualTo(Byte value) {
            addCriterion("SettStatus =", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotEqualTo(Byte value) {
            addCriterion("SettStatus <>", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusGreaterThan(Byte value) {
            addCriterion("SettStatus >", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettStatus >=", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusLessThan(Byte value) {
            addCriterion("SettStatus <", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusLessThanOrEqualTo(Byte value) {
            addCriterion("SettStatus <=", value, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusIn(List<Byte> values) {
            addCriterion("SettStatus in", values, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotIn(List<Byte> values) {
            addCriterion("SettStatus not in", values, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusBetween(Byte value1, Byte value2) {
            addCriterion("SettStatus between", value1, value2, "settStatus");
            return (Criteria) this;
        }

        public Criteria andSettStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("SettStatus not between", value1, value2, "settStatus");
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

        public Criteria andRemitRemarkIsNull() {
            addCriterion("RemitRemark is null");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkIsNotNull() {
            addCriterion("RemitRemark is not null");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkEqualTo(String value) {
            addCriterion("RemitRemark =", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkNotEqualTo(String value) {
            addCriterion("RemitRemark <>", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkGreaterThan(String value) {
            addCriterion("RemitRemark >", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("RemitRemark >=", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkLessThan(String value) {
            addCriterion("RemitRemark <", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkLessThanOrEqualTo(String value) {
            addCriterion("RemitRemark <=", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkLike(String value) {
            addCriterion("RemitRemark like", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkNotLike(String value) {
            addCriterion("RemitRemark not like", value, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkIn(List<String> values) {
            addCriterion("RemitRemark in", values, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkNotIn(List<String> values) {
            addCriterion("RemitRemark not in", values, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkBetween(String value1, String value2) {
            addCriterion("RemitRemark between", value1, value2, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andRemitRemarkNotBetween(String value1, String value2) {
            addCriterion("RemitRemark not between", value1, value2, "remitRemark");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("OperatorId is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("OperatorId is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(Long value) {
            addCriterion("OperatorId =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(Long value) {
            addCriterion("OperatorId <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(Long value) {
            addCriterion("OperatorId >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(Long value) {
            addCriterion("OperatorId >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(Long value) {
            addCriterion("OperatorId <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(Long value) {
            addCriterion("OperatorId <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<Long> values) {
            addCriterion("OperatorId in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<Long> values) {
            addCriterion("OperatorId not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(Long value1, Long value2) {
            addCriterion("OperatorId between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(Long value1, Long value2) {
            addCriterion("OperatorId not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNull() {
            addCriterion("OperatorName is null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNotNull() {
            addCriterion("OperatorName is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameEqualTo(String value) {
            addCriterion("OperatorName =", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotEqualTo(String value) {
            addCriterion("OperatorName <>", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThan(String value) {
            addCriterion("OperatorName >", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThanOrEqualTo(String value) {
            addCriterion("OperatorName >=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThan(String value) {
            addCriterion("OperatorName <", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThanOrEqualTo(String value) {
            addCriterion("OperatorName <=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLike(String value) {
            addCriterion("OperatorName like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotLike(String value) {
            addCriterion("OperatorName not like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIn(List<String> values) {
            addCriterion("OperatorName in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotIn(List<String> values) {
            addCriterion("OperatorName not in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameBetween(String value1, String value2) {
            addCriterion("OperatorName between", value1, value2, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotBetween(String value1, String value2) {
            addCriterion("OperatorName not between", value1, value2, "operatorName");
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