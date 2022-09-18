package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysLogExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public SysLogExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
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

        public Criteria andUserNameIsNull() {
            addCriterion("UserName is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("UserName is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("UserName =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("UserName <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("UserName >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("UserName >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("UserName <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("UserName <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("UserName like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("UserName not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("UserName in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("UserName not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("UserName between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("UserName not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserIpIsNull() {
            addCriterion("UserIp is null");
            return (Criteria) this;
        }

        public Criteria andUserIpIsNotNull() {
            addCriterion("UserIp is not null");
            return (Criteria) this;
        }

        public Criteria andUserIpEqualTo(String value) {
            addCriterion("UserIp =", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpNotEqualTo(String value) {
            addCriterion("UserIp <>", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpGreaterThan(String value) {
            addCriterion("UserIp >", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpGreaterThanOrEqualTo(String value) {
            addCriterion("UserIp >=", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpLessThan(String value) {
            addCriterion("UserIp <", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpLessThanOrEqualTo(String value) {
            addCriterion("UserIp <=", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpLike(String value) {
            addCriterion("UserIp like", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpNotLike(String value) {
            addCriterion("UserIp not like", value, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpIn(List<String> values) {
            addCriterion("UserIp in", values, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpNotIn(List<String> values) {
            addCriterion("UserIp not in", values, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpBetween(String value1, String value2) {
            addCriterion("UserIp between", value1, value2, "userIp");
            return (Criteria) this;
        }

        public Criteria andUserIpNotBetween(String value1, String value2) {
            addCriterion("UserIp not between", value1, value2, "userIp");
            return (Criteria) this;
        }

        public Criteria andSystemIsNull() {
            addCriterion("System is null");
            return (Criteria) this;
        }

        public Criteria andSystemIsNotNull() {
            addCriterion("System is not null");
            return (Criteria) this;
        }

        public Criteria andSystemEqualTo(Byte value) {
            addCriterion("System =", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotEqualTo(Byte value) {
            addCriterion("System <>", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThan(Byte value) {
            addCriterion("System >", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemGreaterThanOrEqualTo(Byte value) {
            addCriterion("System >=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThan(Byte value) {
            addCriterion("System <", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemLessThanOrEqualTo(Byte value) {
            addCriterion("System <=", value, "system");
            return (Criteria) this;
        }

        public Criteria andSystemIn(List<Byte> values) {
            addCriterion("System in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotIn(List<Byte> values) {
            addCriterion("System not in", values, "system");
            return (Criteria) this;
        }

        public Criteria andSystemBetween(Byte value1, Byte value2) {
            addCriterion("System between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andSystemNotBetween(Byte value1, Byte value2) {
            addCriterion("System not between", value1, value2, "system");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNull() {
            addCriterion("MethodName is null");
            return (Criteria) this;
        }

        public Criteria andMethodNameIsNotNull() {
            addCriterion("MethodName is not null");
            return (Criteria) this;
        }

        public Criteria andMethodNameEqualTo(String value) {
            addCriterion("MethodName =", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotEqualTo(String value) {
            addCriterion("MethodName <>", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThan(String value) {
            addCriterion("MethodName >", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameGreaterThanOrEqualTo(String value) {
            addCriterion("MethodName >=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThan(String value) {
            addCriterion("MethodName <", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLessThanOrEqualTo(String value) {
            addCriterion("MethodName <=", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameLike(String value) {
            addCriterion("MethodName like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotLike(String value) {
            addCriterion("MethodName not like", value, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameIn(List<String> values) {
            addCriterion("MethodName in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotIn(List<String> values) {
            addCriterion("MethodName not in", values, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameBetween(String value1, String value2) {
            addCriterion("MethodName between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodNameNotBetween(String value1, String value2) {
            addCriterion("MethodName not between", value1, value2, "methodName");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkIsNull() {
            addCriterion("MethodRemark is null");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkIsNotNull() {
            addCriterion("MethodRemark is not null");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkEqualTo(String value) {
            addCriterion("MethodRemark =", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkNotEqualTo(String value) {
            addCriterion("MethodRemark <>", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkGreaterThan(String value) {
            addCriterion("MethodRemark >", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("MethodRemark >=", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkLessThan(String value) {
            addCriterion("MethodRemark <", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkLessThanOrEqualTo(String value) {
            addCriterion("MethodRemark <=", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkLike(String value) {
            addCriterion("MethodRemark like", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkNotLike(String value) {
            addCriterion("MethodRemark not like", value, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkIn(List<String> values) {
            addCriterion("MethodRemark in", values, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkNotIn(List<String> values) {
            addCriterion("MethodRemark not in", values, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkBetween(String value1, String value2) {
            addCriterion("MethodRemark between", value1, value2, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andMethodRemarkNotBetween(String value1, String value2) {
            addCriterion("MethodRemark not between", value1, value2, "methodRemark");
            return (Criteria) this;
        }

        public Criteria andOptReqParamIsNull() {
            addCriterion("OptReqParam is null");
            return (Criteria) this;
        }

        public Criteria andOptReqParamIsNotNull() {
            addCriterion("OptReqParam is not null");
            return (Criteria) this;
        }

        public Criteria andOptReqParamEqualTo(String value) {
            addCriterion("OptReqParam =", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamNotEqualTo(String value) {
            addCriterion("OptReqParam <>", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamGreaterThan(String value) {
            addCriterion("OptReqParam >", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamGreaterThanOrEqualTo(String value) {
            addCriterion("OptReqParam >=", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamLessThan(String value) {
            addCriterion("OptReqParam <", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamLessThanOrEqualTo(String value) {
            addCriterion("OptReqParam <=", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamLike(String value) {
            addCriterion("OptReqParam like", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamNotLike(String value) {
            addCriterion("OptReqParam not like", value, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamIn(List<String> values) {
            addCriterion("OptReqParam in", values, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamNotIn(List<String> values) {
            addCriterion("OptReqParam not in", values, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamBetween(String value1, String value2) {
            addCriterion("OptReqParam between", value1, value2, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptReqParamNotBetween(String value1, String value2) {
            addCriterion("OptReqParam not between", value1, value2, "optReqParam");
            return (Criteria) this;
        }

        public Criteria andOptResInfoIsNull() {
            addCriterion("OptResInfo is null");
            return (Criteria) this;
        }

        public Criteria andOptResInfoIsNotNull() {
            addCriterion("OptResInfo is not null");
            return (Criteria) this;
        }

        public Criteria andOptResInfoEqualTo(String value) {
            addCriterion("OptResInfo =", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoNotEqualTo(String value) {
            addCriterion("OptResInfo <>", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoGreaterThan(String value) {
            addCriterion("OptResInfo >", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoGreaterThanOrEqualTo(String value) {
            addCriterion("OptResInfo >=", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoLessThan(String value) {
            addCriterion("OptResInfo <", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoLessThanOrEqualTo(String value) {
            addCriterion("OptResInfo <=", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoLike(String value) {
            addCriterion("OptResInfo like", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoNotLike(String value) {
            addCriterion("OptResInfo not like", value, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoIn(List<String> values) {
            addCriterion("OptResInfo in", values, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoNotIn(List<String> values) {
            addCriterion("OptResInfo not in", values, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoBetween(String value1, String value2) {
            addCriterion("OptResInfo between", value1, value2, "optResInfo");
            return (Criteria) this;
        }

        public Criteria andOptResInfoNotBetween(String value1, String value2) {
            addCriterion("OptResInfo not between", value1, value2, "optResInfo");
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