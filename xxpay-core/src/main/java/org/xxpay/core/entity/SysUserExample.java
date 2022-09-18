package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysUserExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public SysUserExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("UserId is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("UserId is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("UserId =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("UserId <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("UserId >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("UserId >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("UserId <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("UserId <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("UserId in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("UserId not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("UserId between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("UserId not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNull() {
            addCriterion("NickName is null");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNotNull() {
            addCriterion("NickName is not null");
            return (Criteria) this;
        }

        public Criteria andNickNameEqualTo(String value) {
            addCriterion("NickName =", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotEqualTo(String value) {
            addCriterion("NickName <>", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThan(String value) {
            addCriterion("NickName >", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("NickName >=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThan(String value) {
            addCriterion("NickName <", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThanOrEqualTo(String value) {
            addCriterion("NickName <=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLike(String value) {
            addCriterion("NickName like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotLike(String value) {
            addCriterion("NickName not like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameIn(List<String> values) {
            addCriterion("NickName in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotIn(List<String> values) {
            addCriterion("NickName not in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameBetween(String value1, String value2) {
            addCriterion("NickName between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotBetween(String value1, String value2) {
            addCriterion("NickName not between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIsNull() {
            addCriterion("LoginUserName is null");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIsNotNull() {
            addCriterion("LoginUserName is not null");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameEqualTo(String value) {
            addCriterion("LoginUserName =", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotEqualTo(String value) {
            addCriterion("LoginUserName <>", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameGreaterThan(String value) {
            addCriterion("LoginUserName >", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("LoginUserName >=", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLessThan(String value) {
            addCriterion("LoginUserName <", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLessThanOrEqualTo(String value) {
            addCriterion("LoginUserName <=", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameLike(String value) {
            addCriterion("LoginUserName like", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotLike(String value) {
            addCriterion("LoginUserName not like", value, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameIn(List<String> values) {
            addCriterion("LoginUserName in", values, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotIn(List<String> values) {
            addCriterion("LoginUserName not in", values, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameBetween(String value1, String value2) {
            addCriterion("LoginUserName between", value1, value2, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andLoginUserNameNotBetween(String value1, String value2) {
            addCriterion("LoginUserName not between", value1, value2, "loginUserName");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("Email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("Email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("Email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("Email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("Email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("Email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("Email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("Email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("Email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("Email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("Email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("Email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("Email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("Email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("Mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("Mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("Mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("Mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("Mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("Mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("Mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("Mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("Mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("Mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("Mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("Mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("Mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("Mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordIsNull() {
            addCriterion("LoginPassword is null");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordIsNotNull() {
            addCriterion("LoginPassword is not null");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordEqualTo(String value) {
            addCriterion("LoginPassword =", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordNotEqualTo(String value) {
            addCriterion("LoginPassword <>", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordGreaterThan(String value) {
            addCriterion("LoginPassword >", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("LoginPassword >=", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordLessThan(String value) {
            addCriterion("LoginPassword <", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordLessThanOrEqualTo(String value) {
            addCriterion("LoginPassword <=", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordLike(String value) {
            addCriterion("LoginPassword like", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordNotLike(String value) {
            addCriterion("LoginPassword not like", value, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordIn(List<String> values) {
            addCriterion("LoginPassword in", values, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordNotIn(List<String> values) {
            addCriterion("LoginPassword not in", values, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordBetween(String value1, String value2) {
            addCriterion("LoginPassword between", value1, value2, "loginPassword");
            return (Criteria) this;
        }

        public Criteria andLoginPasswordNotBetween(String value1, String value2) {
            addCriterion("LoginPassword not between", value1, value2, "loginPassword");
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

        public Criteria andIsSuperAdminIsNull() {
            addCriterion("IsSuperAdmin is null");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminIsNotNull() {
            addCriterion("IsSuperAdmin is not null");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminEqualTo(Byte value) {
            addCriterion("IsSuperAdmin =", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminNotEqualTo(Byte value) {
            addCriterion("IsSuperAdmin <>", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminGreaterThan(Byte value) {
            addCriterion("IsSuperAdmin >", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminGreaterThanOrEqualTo(Byte value) {
            addCriterion("IsSuperAdmin >=", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminLessThan(Byte value) {
            addCriterion("IsSuperAdmin <", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminLessThanOrEqualTo(Byte value) {
            addCriterion("IsSuperAdmin <=", value, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminIn(List<Byte> values) {
            addCriterion("IsSuperAdmin in", values, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminNotIn(List<Byte> values) {
            addCriterion("IsSuperAdmin not in", values, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminBetween(Byte value1, Byte value2) {
            addCriterion("IsSuperAdmin between", value1, value2, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andIsSuperAdminNotBetween(Byte value1, Byte value2) {
            addCriterion("IsSuperAdmin not between", value1, value2, "isSuperAdmin");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdIsNull() {
            addCriterion("BelongInfoId is null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdIsNotNull() {
            addCriterion("BelongInfoId is not null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdEqualTo(Long value) {
            addCriterion("BelongInfoId =", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotEqualTo(Long value) {
            addCriterion("BelongInfoId <>", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdGreaterThan(Long value) {
            addCriterion("BelongInfoId >", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("BelongInfoId >=", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdLessThan(Long value) {
            addCriterion("BelongInfoId <", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdLessThanOrEqualTo(Long value) {
            addCriterion("BelongInfoId <=", value, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdIn(List<Long> values) {
            addCriterion("BelongInfoId in", values, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotIn(List<Long> values) {
            addCriterion("BelongInfoId not in", values, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdBetween(Long value1, Long value2) {
            addCriterion("BelongInfoId between", value1, value2, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoIdNotBetween(Long value1, Long value2) {
            addCriterion("BelongInfoId not between", value1, value2, "belongInfoId");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIsNull() {
            addCriterion("BelongInfoType is null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIsNotNull() {
            addCriterion("BelongInfoType is not null");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeEqualTo(Byte value) {
            addCriterion("BelongInfoType =", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotEqualTo(Byte value) {
            addCriterion("BelongInfoType <>", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeGreaterThan(Byte value) {
            addCriterion("BelongInfoType >", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("BelongInfoType >=", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeLessThan(Byte value) {
            addCriterion("BelongInfoType <", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeLessThanOrEqualTo(Byte value) {
            addCriterion("BelongInfoType <=", value, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeIn(List<Byte> values) {
            addCriterion("BelongInfoType in", values, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotIn(List<Byte> values) {
            addCriterion("BelongInfoType not in", values, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeBetween(Byte value1, Byte value2) {
            addCriterion("BelongInfoType between", value1, value2, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andBelongInfoTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("BelongInfoType not between", value1, value2, "belongInfoType");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNull() {
            addCriterion("Avatar is null");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNotNull() {
            addCriterion("Avatar is not null");
            return (Criteria) this;
        }

        public Criteria andAvatarEqualTo(String value) {
            addCriterion("Avatar =", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotEqualTo(String value) {
            addCriterion("Avatar <>", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThan(String value) {
            addCriterion("Avatar >", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThanOrEqualTo(String value) {
            addCriterion("Avatar >=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThan(String value) {
            addCriterion("Avatar <", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThanOrEqualTo(String value) {
            addCriterion("Avatar <=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLike(String value) {
            addCriterion("Avatar like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotLike(String value) {
            addCriterion("Avatar not like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarIn(List<String> values) {
            addCriterion("Avatar in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotIn(List<String> values) {
            addCriterion("Avatar not in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarBetween(String value1, String value2) {
            addCriterion("Avatar between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotBetween(String value1, String value2) {
            addCriterion("Avatar not between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("Sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("Sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Byte value) {
            addCriterion("Sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Byte value) {
            addCriterion("Sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Byte value) {
            addCriterion("Sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Byte value) {
            addCriterion("Sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Byte value) {
            addCriterion("Sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Byte value) {
            addCriterion("Sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Byte> values) {
            addCriterion("Sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Byte> values) {
            addCriterion("Sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Byte value1, Byte value2) {
            addCriterion("Sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Byte value1, Byte value2) {
            addCriterion("Sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andStoreIdIsNull() {
            addCriterion("StoreId is null");
            return (Criteria) this;
        }

        public Criteria andStoreIdIsNotNull() {
            addCriterion("StoreId is not null");
            return (Criteria) this;
        }

        public Criteria andStoreIdEqualTo(Long value) {
            addCriterion("StoreId =", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotEqualTo(Long value) {
            addCriterion("StoreId <>", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdGreaterThan(Long value) {
            addCriterion("StoreId >", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdGreaterThanOrEqualTo(Long value) {
            addCriterion("StoreId >=", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdLessThan(Long value) {
            addCriterion("StoreId <", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdLessThanOrEqualTo(Long value) {
            addCriterion("StoreId <=", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdIn(List<Long> values) {
            addCriterion("StoreId in", values, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotIn(List<Long> values) {
            addCriterion("StoreId not in", values, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdBetween(Long value1, Long value2) {
            addCriterion("StoreId between", value1, value2, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotBetween(Long value1, Long value2) {
            addCriterion("StoreId not between", value1, value2, "storeId");
            return (Criteria) this;
        }

        public Criteria andWorkStatusIsNull() {
            addCriterion("WorkStatus is null");
            return (Criteria) this;
        }

        public Criteria andWorkStatusIsNotNull() {
            addCriterion("WorkStatus is not null");
            return (Criteria) this;
        }

        public Criteria andWorkStatusEqualTo(Byte value) {
            addCriterion("WorkStatus =", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusNotEqualTo(Byte value) {
            addCriterion("WorkStatus <>", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusGreaterThan(Byte value) {
            addCriterion("WorkStatus >", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("WorkStatus >=", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusLessThan(Byte value) {
            addCriterion("WorkStatus <", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusLessThanOrEqualTo(Byte value) {
            addCriterion("WorkStatus <=", value, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusIn(List<Byte> values) {
            addCriterion("WorkStatus in", values, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusNotIn(List<Byte> values) {
            addCriterion("WorkStatus not in", values, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusBetween(Byte value1, Byte value2) {
            addCriterion("WorkStatus between", value1, value2, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("WorkStatus not between", value1, value2, "workStatus");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeIsNull() {
            addCriterion("WorkStartTime is null");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeIsNotNull() {
            addCriterion("WorkStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeEqualTo(Date value) {
            addCriterion("WorkStartTime =", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeNotEqualTo(Date value) {
            addCriterion("WorkStartTime <>", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeGreaterThan(Date value) {
            addCriterion("WorkStartTime >", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("WorkStartTime >=", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeLessThan(Date value) {
            addCriterion("WorkStartTime <", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("WorkStartTime <=", value, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeIn(List<Date> values) {
            addCriterion("WorkStartTime in", values, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeNotIn(List<Date> values) {
            addCriterion("WorkStartTime not in", values, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeBetween(Date value1, Date value2) {
            addCriterion("WorkStartTime between", value1, value2, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andWorkStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("WorkStartTime not between", value1, value2, "workStartTime");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNull() {
            addCriterion("CreateUserId is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("CreateUserId is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(Long value) {
            addCriterion("CreateUserId =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(Long value) {
            addCriterion("CreateUserId <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(Long value) {
            addCriterion("CreateUserId >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("CreateUserId >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(Long value) {
            addCriterion("CreateUserId <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(Long value) {
            addCriterion("CreateUserId <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<Long> values) {
            addCriterion("CreateUserId in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<Long> values) {
            addCriterion("CreateUserId not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(Long value1, Long value2) {
            addCriterion("CreateUserId between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(Long value1, Long value2) {
            addCriterion("CreateUserId not between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("LastLoginTime is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("LastLoginTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("LastLoginTime =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("LastLoginTime <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("LastLoginTime >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastLoginTime >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("LastLoginTime <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastLoginTime <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("LastLoginTime in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("LastLoginTime not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("LastLoginTime between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastLoginTime not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNull() {
            addCriterion("LastLoginIp is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNotNull() {
            addCriterion("LastLoginIp is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpEqualTo(String value) {
            addCriterion("LastLoginIp =", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotEqualTo(String value) {
            addCriterion("LastLoginIp <>", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThan(String value) {
            addCriterion("LastLoginIp >", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThanOrEqualTo(String value) {
            addCriterion("LastLoginIp >=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThan(String value) {
            addCriterion("LastLoginIp <", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThanOrEqualTo(String value) {
            addCriterion("LastLoginIp <=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLike(String value) {
            addCriterion("LastLoginIp like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotLike(String value) {
            addCriterion("LastLoginIp not like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIn(List<String> values) {
            addCriterion("LastLoginIp in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotIn(List<String> values) {
            addCriterion("LastLoginIp not in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpBetween(String value1, String value2) {
            addCriterion("LastLoginIp between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotBetween(String value1, String value2) {
            addCriterion("LastLoginIp not between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeIsNull() {
            addCriterion("LastPasswordResetTime is null");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeIsNotNull() {
            addCriterion("LastPasswordResetTime is not null");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeEqualTo(Date value) {
            addCriterion("LastPasswordResetTime =", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotEqualTo(Date value) {
            addCriterion("LastPasswordResetTime <>", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeGreaterThan(Date value) {
            addCriterion("LastPasswordResetTime >", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastPasswordResetTime >=", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeLessThan(Date value) {
            addCriterion("LastPasswordResetTime <", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeLessThanOrEqualTo(Date value) {
            addCriterion("LastPasswordResetTime <=", value, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeIn(List<Date> values) {
            addCriterion("LastPasswordResetTime in", values, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotIn(List<Date> values) {
            addCriterion("LastPasswordResetTime not in", values, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeBetween(Date value1, Date value2) {
            addCriterion("LastPasswordResetTime between", value1, value2, "lastPasswordResetTime");
            return (Criteria) this;
        }

        public Criteria andLastPasswordResetTimeNotBetween(Date value1, Date value2) {
            addCriterion("LastPasswordResetTime not between", value1, value2, "lastPasswordResetTime");
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