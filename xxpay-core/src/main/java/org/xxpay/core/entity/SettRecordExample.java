package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SettRecordExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public SettRecordExample() {
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

        public Criteria andSettTypeIsNull() {
            addCriterion("SettType is null");
            return (Criteria) this;
        }

        public Criteria andSettTypeIsNotNull() {
            addCriterion("SettType is not null");
            return (Criteria) this;
        }

        public Criteria andSettTypeEqualTo(Byte value) {
            addCriterion("SettType =", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotEqualTo(Byte value) {
            addCriterion("SettType <>", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeGreaterThan(Byte value) {
            addCriterion("SettType >", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettType >=", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeLessThan(Byte value) {
            addCriterion("SettType <", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeLessThanOrEqualTo(Byte value) {
            addCriterion("SettType <=", value, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeIn(List<Byte> values) {
            addCriterion("SettType in", values, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotIn(List<Byte> values) {
            addCriterion("SettType not in", values, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeBetween(Byte value1, Byte value2) {
            addCriterion("SettType between", value1, value2, "settType");
            return (Criteria) this;
        }

        public Criteria andSettTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("SettType not between", value1, value2, "settType");
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

        public Criteria andAccountAttrIsNull() {
            addCriterion("AccountAttr is null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIsNotNull() {
            addCriterion("AccountAttr is not null");
            return (Criteria) this;
        }

        public Criteria andAccountAttrEqualTo(Byte value) {
            addCriterion("AccountAttr =", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotEqualTo(Byte value) {
            addCriterion("AccountAttr <>", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThan(Byte value) {
            addCriterion("AccountAttr >", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr >=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThan(Byte value) {
            addCriterion("AccountAttr <", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrLessThanOrEqualTo(Byte value) {
            addCriterion("AccountAttr <=", value, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrIn(List<Byte> values) {
            addCriterion("AccountAttr in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotIn(List<Byte> values) {
            addCriterion("AccountAttr not in", values, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountAttrNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountAttr not between", value1, value2, "accountAttr");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNull() {
            addCriterion("AccountType is null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIsNotNull() {
            addCriterion("AccountType is not null");
            return (Criteria) this;
        }

        public Criteria andAccountTypeEqualTo(Byte value) {
            addCriterion("AccountType =", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotEqualTo(Byte value) {
            addCriterion("AccountType <>", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThan(Byte value) {
            addCriterion("AccountType >", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("AccountType >=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThan(Byte value) {
            addCriterion("AccountType <", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeLessThanOrEqualTo(Byte value) {
            addCriterion("AccountType <=", value, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeIn(List<Byte> values) {
            addCriterion("AccountType in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotIn(List<Byte> values) {
            addCriterion("AccountType not in", values, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeBetween(Byte value1, Byte value2) {
            addCriterion("AccountType between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andAccountTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("AccountType not between", value1, value2, "accountType");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNull() {
            addCriterion("BankName is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("BankName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("BankName =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("BankName <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("BankName >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankName >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("BankName <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("BankName <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("BankName like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("BankName not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("BankName in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("BankName not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("BankName between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("BankName not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNull() {
            addCriterion("BankNetName is null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIsNotNull() {
            addCriterion("BankNetName is not null");
            return (Criteria) this;
        }

        public Criteria andBankNetNameEqualTo(String value) {
            addCriterion("BankNetName =", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotEqualTo(String value) {
            addCriterion("BankNetName <>", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThan(String value) {
            addCriterion("BankNetName >", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankNetName >=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThan(String value) {
            addCriterion("BankNetName <", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLessThanOrEqualTo(String value) {
            addCriterion("BankNetName <=", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameLike(String value) {
            addCriterion("BankNetName like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotLike(String value) {
            addCriterion("BankNetName not like", value, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameIn(List<String> values) {
            addCriterion("BankNetName in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotIn(List<String> values) {
            addCriterion("BankNetName not in", values, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameBetween(String value1, String value2) {
            addCriterion("BankNetName between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andBankNetNameNotBetween(String value1, String value2) {
            addCriterion("BankNetName not between", value1, value2, "bankNetName");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNull() {
            addCriterion("AccountName is null");
            return (Criteria) this;
        }

        public Criteria andAccountNameIsNotNull() {
            addCriterion("AccountName is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNameEqualTo(String value) {
            addCriterion("AccountName =", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotEqualTo(String value) {
            addCriterion("AccountName <>", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThan(String value) {
            addCriterion("AccountName >", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("AccountName >=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThan(String value) {
            addCriterion("AccountName <", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLessThanOrEqualTo(String value) {
            addCriterion("AccountName <=", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameLike(String value) {
            addCriterion("AccountName like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotLike(String value) {
            addCriterion("AccountName not like", value, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameIn(List<String> values) {
            addCriterion("AccountName in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotIn(List<String> values) {
            addCriterion("AccountName not in", values, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameBetween(String value1, String value2) {
            addCriterion("AccountName between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNameNotBetween(String value1, String value2) {
            addCriterion("AccountName not between", value1, value2, "accountName");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNull() {
            addCriterion("AccountNo is null");
            return (Criteria) this;
        }

        public Criteria andAccountNoIsNotNull() {
            addCriterion("AccountNo is not null");
            return (Criteria) this;
        }

        public Criteria andAccountNoEqualTo(String value) {
            addCriterion("AccountNo =", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotEqualTo(String value) {
            addCriterion("AccountNo <>", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThan(String value) {
            addCriterion("AccountNo >", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoGreaterThanOrEqualTo(String value) {
            addCriterion("AccountNo >=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThan(String value) {
            addCriterion("AccountNo <", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLessThanOrEqualTo(String value) {
            addCriterion("AccountNo <=", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoLike(String value) {
            addCriterion("AccountNo like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotLike(String value) {
            addCriterion("AccountNo not like", value, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoIn(List<String> values) {
            addCriterion("AccountNo in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotIn(List<String> values) {
            addCriterion("AccountNo not in", values, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoBetween(String value1, String value2) {
            addCriterion("AccountNo between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andAccountNoNotBetween(String value1, String value2) {
            addCriterion("AccountNo not between", value1, value2, "accountNo");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("Province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("Province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("Province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("Province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("Province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("Province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("Province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("Province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("Province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("Province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("Province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("Province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("Province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("Province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("City is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("City is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("City =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("City <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("City >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("City >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("City <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("City <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("City like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("City not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("City in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("City not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("City between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("City not between", value1, value2, "city");
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

        public Criteria andSettOrderIdIsNull() {
            addCriterion("SettOrderId is null");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdIsNotNull() {
            addCriterion("SettOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdEqualTo(String value) {
            addCriterion("SettOrderId =", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdNotEqualTo(String value) {
            addCriterion("SettOrderId <>", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdGreaterThan(String value) {
            addCriterion("SettOrderId >", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("SettOrderId >=", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdLessThan(String value) {
            addCriterion("SettOrderId <", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdLessThanOrEqualTo(String value) {
            addCriterion("SettOrderId <=", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdLike(String value) {
            addCriterion("SettOrderId like", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdNotLike(String value) {
            addCriterion("SettOrderId not like", value, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdIn(List<String> values) {
            addCriterion("SettOrderId in", values, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdNotIn(List<String> values) {
            addCriterion("SettOrderId not in", values, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdBetween(String value1, String value2) {
            addCriterion("SettOrderId between", value1, value2, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andSettOrderIdNotBetween(String value1, String value2) {
            addCriterion("SettOrderId not between", value1, value2, "settOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIsNull() {
            addCriterion("TransOrderId is null");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIsNotNull() {
            addCriterion("TransOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdEqualTo(String value) {
            addCriterion("TransOrderId =", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotEqualTo(String value) {
            addCriterion("TransOrderId <>", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdGreaterThan(String value) {
            addCriterion("TransOrderId >", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("TransOrderId >=", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLessThan(String value) {
            addCriterion("TransOrderId <", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLessThanOrEqualTo(String value) {
            addCriterion("TransOrderId <=", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdLike(String value) {
            addCriterion("TransOrderId like", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotLike(String value) {
            addCriterion("TransOrderId not like", value, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdIn(List<String> values) {
            addCriterion("TransOrderId in", values, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotIn(List<String> values) {
            addCriterion("TransOrderId not in", values, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdBetween(String value1, String value2) {
            addCriterion("TransOrderId between", value1, value2, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransOrderIdNotBetween(String value1, String value2) {
            addCriterion("TransOrderId not between", value1, value2, "transOrderId");
            return (Criteria) this;
        }

        public Criteria andTransMsgIsNull() {
            addCriterion("TransMsg is null");
            return (Criteria) this;
        }

        public Criteria andTransMsgIsNotNull() {
            addCriterion("TransMsg is not null");
            return (Criteria) this;
        }

        public Criteria andTransMsgEqualTo(String value) {
            addCriterion("TransMsg =", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotEqualTo(String value) {
            addCriterion("TransMsg <>", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgGreaterThan(String value) {
            addCriterion("TransMsg >", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgGreaterThanOrEqualTo(String value) {
            addCriterion("TransMsg >=", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLessThan(String value) {
            addCriterion("TransMsg <", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLessThanOrEqualTo(String value) {
            addCriterion("TransMsg <=", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgLike(String value) {
            addCriterion("TransMsg like", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotLike(String value) {
            addCriterion("TransMsg not like", value, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgIn(List<String> values) {
            addCriterion("TransMsg in", values, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotIn(List<String> values) {
            addCriterion("TransMsg not in", values, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgBetween(String value1, String value2) {
            addCriterion("TransMsg between", value1, value2, "transMsg");
            return (Criteria) this;
        }

        public Criteria andTransMsgNotBetween(String value1, String value2) {
            addCriterion("TransMsg not between", value1, value2, "transMsg");
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