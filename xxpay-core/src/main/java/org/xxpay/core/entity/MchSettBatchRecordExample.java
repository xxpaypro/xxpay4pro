package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchSettBatchRecordExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchSettBatchRecordExample() {
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

        public Criteria andSettRecordIdIsNull() {
            addCriterion("SettRecordId is null");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdIsNotNull() {
            addCriterion("SettRecordId is not null");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdEqualTo(Long value) {
            addCriterion("SettRecordId =", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdNotEqualTo(Long value) {
            addCriterion("SettRecordId <>", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdGreaterThan(Long value) {
            addCriterion("SettRecordId >", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SettRecordId >=", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdLessThan(Long value) {
            addCriterion("SettRecordId <", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdLessThanOrEqualTo(Long value) {
            addCriterion("SettRecordId <=", value, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdIn(List<Long> values) {
            addCriterion("SettRecordId in", values, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdNotIn(List<Long> values) {
            addCriterion("SettRecordId not in", values, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdBetween(Long value1, Long value2) {
            addCriterion("SettRecordId between", value1, value2, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andSettRecordIdNotBetween(Long value1, Long value2) {
            addCriterion("SettRecordId not between", value1, value2, "settRecordId");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameIsNull() {
            addCriterion("BankAccountName is null");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameIsNotNull() {
            addCriterion("BankAccountName is not null");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameEqualTo(String value) {
            addCriterion("BankAccountName =", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameNotEqualTo(String value) {
            addCriterion("BankAccountName <>", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameGreaterThan(String value) {
            addCriterion("BankAccountName >", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("BankAccountName >=", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameLessThan(String value) {
            addCriterion("BankAccountName <", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameLessThanOrEqualTo(String value) {
            addCriterion("BankAccountName <=", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameLike(String value) {
            addCriterion("BankAccountName like", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameNotLike(String value) {
            addCriterion("BankAccountName not like", value, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameIn(List<String> values) {
            addCriterion("BankAccountName in", values, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameNotIn(List<String> values) {
            addCriterion("BankAccountName not in", values, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameBetween(String value1, String value2) {
            addCriterion("BankAccountName between", value1, value2, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNameNotBetween(String value1, String value2) {
            addCriterion("BankAccountName not between", value1, value2, "bankAccountName");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberIsNull() {
            addCriterion("BankAccountNumber is null");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberIsNotNull() {
            addCriterion("BankAccountNumber is not null");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberEqualTo(String value) {
            addCriterion("BankAccountNumber =", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberNotEqualTo(String value) {
            addCriterion("BankAccountNumber <>", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberGreaterThan(String value) {
            addCriterion("BankAccountNumber >", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberGreaterThanOrEqualTo(String value) {
            addCriterion("BankAccountNumber >=", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberLessThan(String value) {
            addCriterion("BankAccountNumber <", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberLessThanOrEqualTo(String value) {
            addCriterion("BankAccountNumber <=", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberLike(String value) {
            addCriterion("BankAccountNumber like", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberNotLike(String value) {
            addCriterion("BankAccountNumber not like", value, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberIn(List<String> values) {
            addCriterion("BankAccountNumber in", values, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberNotIn(List<String> values) {
            addCriterion("BankAccountNumber not in", values, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberBetween(String value1, String value2) {
            addCriterion("BankAccountNumber between", value1, value2, "bankAccountNumber");
            return (Criteria) this;
        }

        public Criteria andBankAccountNumberNotBetween(String value1, String value2) {
            addCriterion("BankAccountNumber not between", value1, value2, "bankAccountNumber");
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

        public Criteria andBankProvinceIsNull() {
            addCriterion("BankProvince is null");
            return (Criteria) this;
        }

        public Criteria andBankProvinceIsNotNull() {
            addCriterion("BankProvince is not null");
            return (Criteria) this;
        }

        public Criteria andBankProvinceEqualTo(String value) {
            addCriterion("BankProvince =", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceNotEqualTo(String value) {
            addCriterion("BankProvince <>", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceGreaterThan(String value) {
            addCriterion("BankProvince >", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("BankProvince >=", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceLessThan(String value) {
            addCriterion("BankProvince <", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceLessThanOrEqualTo(String value) {
            addCriterion("BankProvince <=", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceLike(String value) {
            addCriterion("BankProvince like", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceNotLike(String value) {
            addCriterion("BankProvince not like", value, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceIn(List<String> values) {
            addCriterion("BankProvince in", values, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceNotIn(List<String> values) {
            addCriterion("BankProvince not in", values, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceBetween(String value1, String value2) {
            addCriterion("BankProvince between", value1, value2, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankProvinceNotBetween(String value1, String value2) {
            addCriterion("BankProvince not between", value1, value2, "bankProvince");
            return (Criteria) this;
        }

        public Criteria andBankCityIsNull() {
            addCriterion("BankCity is null");
            return (Criteria) this;
        }

        public Criteria andBankCityIsNotNull() {
            addCriterion("BankCity is not null");
            return (Criteria) this;
        }

        public Criteria andBankCityEqualTo(String value) {
            addCriterion("BankCity =", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityNotEqualTo(String value) {
            addCriterion("BankCity <>", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityGreaterThan(String value) {
            addCriterion("BankCity >", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityGreaterThanOrEqualTo(String value) {
            addCriterion("BankCity >=", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityLessThan(String value) {
            addCriterion("BankCity <", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityLessThanOrEqualTo(String value) {
            addCriterion("BankCity <=", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityLike(String value) {
            addCriterion("BankCity like", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityNotLike(String value) {
            addCriterion("BankCity not like", value, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityIn(List<String> values) {
            addCriterion("BankCity in", values, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityNotIn(List<String> values) {
            addCriterion("BankCity not in", values, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityBetween(String value1, String value2) {
            addCriterion("BankCity between", value1, value2, "bankCity");
            return (Criteria) this;
        }

        public Criteria andBankCityNotBetween(String value1, String value2) {
            addCriterion("BankCity not between", value1, value2, "bankCity");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("Amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("Amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("Amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("Amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("Amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("Amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("Amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("Amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("Amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("Amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("Amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("Amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andPublicFlagIsNull() {
            addCriterion("PublicFlag is null");
            return (Criteria) this;
        }

        public Criteria andPublicFlagIsNotNull() {
            addCriterion("PublicFlag is not null");
            return (Criteria) this;
        }

        public Criteria andPublicFlagEqualTo(Byte value) {
            addCriterion("PublicFlag =", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagNotEqualTo(Byte value) {
            addCriterion("PublicFlag <>", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagGreaterThan(Byte value) {
            addCriterion("PublicFlag >", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("PublicFlag >=", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagLessThan(Byte value) {
            addCriterion("PublicFlag <", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagLessThanOrEqualTo(Byte value) {
            addCriterion("PublicFlag <=", value, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagIn(List<Byte> values) {
            addCriterion("PublicFlag in", values, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagNotIn(List<Byte> values) {
            addCriterion("PublicFlag not in", values, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagBetween(Byte value1, Byte value2) {
            addCriterion("PublicFlag between", value1, value2, "publicFlag");
            return (Criteria) this;
        }

        public Criteria andPublicFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("PublicFlag not between", value1, value2, "publicFlag");
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