package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayInterfaceTypeTemplateExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public PayInterfaceTypeTemplateExample() {
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

        public Criteria andTemplateNameIsNull() {
            addCriterion("TemplateName is null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIsNotNull() {
            addCriterion("TemplateName is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameEqualTo(String value) {
            addCriterion("TemplateName =", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotEqualTo(String value) {
            addCriterion("TemplateName <>", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThan(String value) {
            addCriterion("TemplateName >", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThanOrEqualTo(String value) {
            addCriterion("TemplateName >=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThan(String value) {
            addCriterion("TemplateName <", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThanOrEqualTo(String value) {
            addCriterion("TemplateName <=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLike(String value) {
            addCriterion("TemplateName like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotLike(String value) {
            addCriterion("TemplateName not like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIn(List<String> values) {
            addCriterion("TemplateName in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotIn(List<String> values) {
            addCriterion("TemplateName not in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameBetween(String value1, String value2) {
            addCriterion("TemplateName between", value1, value2, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotBetween(String value1, String value2) {
            addCriterion("TemplateName not between", value1, value2, "templateName");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIsNull() {
            addCriterion("IfTypeCode is null");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIsNotNull() {
            addCriterion("IfTypeCode is not null");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeEqualTo(String value) {
            addCriterion("IfTypeCode =", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotEqualTo(String value) {
            addCriterion("IfTypeCode <>", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeGreaterThan(String value) {
            addCriterion("IfTypeCode >", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("IfTypeCode >=", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLessThan(String value) {
            addCriterion("IfTypeCode <", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLessThanOrEqualTo(String value) {
            addCriterion("IfTypeCode <=", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeLike(String value) {
            addCriterion("IfTypeCode like", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotLike(String value) {
            addCriterion("IfTypeCode not like", value, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeIn(List<String> values) {
            addCriterion("IfTypeCode in", values, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotIn(List<String> values) {
            addCriterion("IfTypeCode not in", values, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeBetween(String value1, String value2) {
            addCriterion("IfTypeCode between", value1, value2, "ifTypeCode");
            return (Criteria) this;
        }

        public Criteria andIfTypeCodeNotBetween(String value1, String value2) {
            addCriterion("IfTypeCode not between", value1, value2, "ifTypeCode");
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

        public Criteria andPassageMchIdTemplateIsNull() {
            addCriterion("PassageMchIdTemplate is null");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateIsNotNull() {
            addCriterion("PassageMchIdTemplate is not null");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateEqualTo(String value) {
            addCriterion("PassageMchIdTemplate =", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateNotEqualTo(String value) {
            addCriterion("PassageMchIdTemplate <>", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateGreaterThan(String value) {
            addCriterion("PassageMchIdTemplate >", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateGreaterThanOrEqualTo(String value) {
            addCriterion("PassageMchIdTemplate >=", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateLessThan(String value) {
            addCriterion("PassageMchIdTemplate <", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateLessThanOrEqualTo(String value) {
            addCriterion("PassageMchIdTemplate <=", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateLike(String value) {
            addCriterion("PassageMchIdTemplate like", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateNotLike(String value) {
            addCriterion("PassageMchIdTemplate not like", value, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateIn(List<String> values) {
            addCriterion("PassageMchIdTemplate in", values, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateNotIn(List<String> values) {
            addCriterion("PassageMchIdTemplate not in", values, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateBetween(String value1, String value2) {
            addCriterion("PassageMchIdTemplate between", value1, value2, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andPassageMchIdTemplateNotBetween(String value1, String value2) {
            addCriterion("PassageMchIdTemplate not between", value1, value2, "passageMchIdTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateIsNull() {
            addCriterion("ParamTemplate is null");
            return (Criteria) this;
        }

        public Criteria andParamTemplateIsNotNull() {
            addCriterion("ParamTemplate is not null");
            return (Criteria) this;
        }

        public Criteria andParamTemplateEqualTo(String value) {
            addCriterion("ParamTemplate =", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateNotEqualTo(String value) {
            addCriterion("ParamTemplate <>", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateGreaterThan(String value) {
            addCriterion("ParamTemplate >", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateGreaterThanOrEqualTo(String value) {
            addCriterion("ParamTemplate >=", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateLessThan(String value) {
            addCriterion("ParamTemplate <", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateLessThanOrEqualTo(String value) {
            addCriterion("ParamTemplate <=", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateLike(String value) {
            addCriterion("ParamTemplate like", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateNotLike(String value) {
            addCriterion("ParamTemplate not like", value, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateIn(List<String> values) {
            addCriterion("ParamTemplate in", values, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateNotIn(List<String> values) {
            addCriterion("ParamTemplate not in", values, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateBetween(String value1, String value2) {
            addCriterion("ParamTemplate between", value1, value2, "paramTemplate");
            return (Criteria) this;
        }

        public Criteria andParamTemplateNotBetween(String value1, String value2) {
            addCriterion("ParamTemplate not between", value1, value2, "paramTemplate");
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