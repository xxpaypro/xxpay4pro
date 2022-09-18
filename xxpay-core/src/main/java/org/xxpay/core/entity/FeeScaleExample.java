package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeeScaleExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public FeeScaleExample() {
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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andProductTypeIsNull() {
            addCriterion("ProductType is null");
            return (Criteria) this;
        }

        public Criteria andProductTypeIsNotNull() {
            addCriterion("ProductType is not null");
            return (Criteria) this;
        }

        public Criteria andProductTypeEqualTo(Byte value) {
            addCriterion("ProductType =", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotEqualTo(Byte value) {
            addCriterion("ProductType <>", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThan(Byte value) {
            addCriterion("ProductType >", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ProductType >=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThan(Byte value) {
            addCriterion("ProductType <", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ProductType <=", value, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeIn(List<Byte> values) {
            addCriterion("ProductType in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotIn(List<Byte> values) {
            addCriterion("ProductType not in", values, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeBetween(Byte value1, Byte value2) {
            addCriterion("ProductType between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andProductTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ProductType not between", value1, value2, "productType");
            return (Criteria) this;
        }

        public Criteria andRefProductIdIsNull() {
            addCriterion("refProductId is null");
            return (Criteria) this;
        }

        public Criteria andRefProductIdIsNotNull() {
            addCriterion("refProductId is not null");
            return (Criteria) this;
        }

        public Criteria andRefProductIdEqualTo(Integer value) {
            addCriterion("refProductId =", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdNotEqualTo(Integer value) {
            addCriterion("refProductId <>", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdGreaterThan(Integer value) {
            addCriterion("refProductId >", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("refProductId >=", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdLessThan(Integer value) {
            addCriterion("refProductId <", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdLessThanOrEqualTo(Integer value) {
            addCriterion("refProductId <=", value, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdIn(List<Integer> values) {
            addCriterion("refProductId in", values, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdNotIn(List<Integer> values) {
            addCriterion("refProductId not in", values, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdBetween(Integer value1, Integer value2) {
            addCriterion("refProductId between", value1, value2, "refProductId");
            return (Criteria) this;
        }

        public Criteria andRefProductIdNotBetween(Integer value1, Integer value2) {
            addCriterion("refProductId not between", value1, value2, "refProductId");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIsNull() {
            addCriterion("FeeScale is null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIsNotNull() {
            addCriterion("FeeScale is not null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleEqualTo(Byte value) {
            addCriterion("FeeScale =", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleNotEqualTo(Byte value) {
            addCriterion("FeeScale <>", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleGreaterThan(Byte value) {
            addCriterion("FeeScale >", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleGreaterThanOrEqualTo(Byte value) {
            addCriterion("FeeScale >=", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleLessThan(Byte value) {
            addCriterion("FeeScale <", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleLessThanOrEqualTo(Byte value) {
            addCriterion("FeeScale <=", value, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleIn(List<Byte> values) {
            addCriterion("FeeScale in", values, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleNotIn(List<Byte> values) {
            addCriterion("FeeScale not in", values, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleBetween(Byte value1, Byte value2) {
            addCriterion("FeeScale between", value1, value2, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleNotBetween(Byte value1, Byte value2) {
            addCriterion("FeeScale not between", value1, value2, "feeScale");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepIsNull() {
            addCriterion("FeeScaleStep is null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepIsNotNull() {
            addCriterion("FeeScaleStep is not null");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepEqualTo(Integer value) {
            addCriterion("FeeScaleStep =", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepNotEqualTo(Integer value) {
            addCriterion("FeeScaleStep <>", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepGreaterThan(Integer value) {
            addCriterion("FeeScaleStep >", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepGreaterThanOrEqualTo(Integer value) {
            addCriterion("FeeScaleStep >=", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepLessThan(Integer value) {
            addCriterion("FeeScaleStep <", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepLessThanOrEqualTo(Integer value) {
            addCriterion("FeeScaleStep <=", value, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepIn(List<Integer> values) {
            addCriterion("FeeScaleStep in", values, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepNotIn(List<Integer> values) {
            addCriterion("FeeScaleStep not in", values, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepBetween(Integer value1, Integer value2) {
            addCriterion("FeeScaleStep between", value1, value2, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andFeeScaleStepNotBetween(Integer value1, Integer value2) {
            addCriterion("FeeScaleStep not between", value1, value2, "feeScaleStep");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeIsNull() {
            addCriterion("SingleFeeType is null");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeIsNotNull() {
            addCriterion("SingleFeeType is not null");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeEqualTo(Byte value) {
            addCriterion("SingleFeeType =", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeNotEqualTo(Byte value) {
            addCriterion("SingleFeeType <>", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeGreaterThan(Byte value) {
            addCriterion("SingleFeeType >", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("SingleFeeType >=", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeLessThan(Byte value) {
            addCriterion("SingleFeeType <", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("SingleFeeType <=", value, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeIn(List<Byte> values) {
            addCriterion("SingleFeeType in", values, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeNotIn(List<Byte> values) {
            addCriterion("SingleFeeType not in", values, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeBetween(Byte value1, Byte value2) {
            addCriterion("SingleFeeType between", value1, value2, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("SingleFeeType not between", value1, value2, "singleFeeType");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("Fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("Fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("Fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("Fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("Fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("Fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("Fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("Fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("Fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("Fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("Fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("Fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andExtConfigIsNull() {
            addCriterion("ExtConfig is null");
            return (Criteria) this;
        }

        public Criteria andExtConfigIsNotNull() {
            addCriterion("ExtConfig is not null");
            return (Criteria) this;
        }

        public Criteria andExtConfigEqualTo(String value) {
            addCriterion("ExtConfig =", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigNotEqualTo(String value) {
            addCriterion("ExtConfig <>", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigGreaterThan(String value) {
            addCriterion("ExtConfig >", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigGreaterThanOrEqualTo(String value) {
            addCriterion("ExtConfig >=", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigLessThan(String value) {
            addCriterion("ExtConfig <", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigLessThanOrEqualTo(String value) {
            addCriterion("ExtConfig <=", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigLike(String value) {
            addCriterion("ExtConfig like", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigNotLike(String value) {
            addCriterion("ExtConfig not like", value, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigIn(List<String> values) {
            addCriterion("ExtConfig in", values, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigNotIn(List<String> values) {
            addCriterion("ExtConfig not in", values, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigBetween(String value1, String value2) {
            addCriterion("ExtConfig between", value1, value2, "extConfig");
            return (Criteria) this;
        }

        public Criteria andExtConfigNotBetween(String value1, String value2) {
            addCriterion("ExtConfig not between", value1, value2, "extConfig");
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

        public Criteria andIsDefaultIsNull() {
            addCriterion("IsDefault is null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIsNotNull() {
            addCriterion("IsDefault is not null");
            return (Criteria) this;
        }

        public Criteria andIsDefaultEqualTo(Byte value) {
            addCriterion("IsDefault =", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotEqualTo(Byte value) {
            addCriterion("IsDefault <>", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThan(Byte value) {
            addCriterion("IsDefault >", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultGreaterThanOrEqualTo(Byte value) {
            addCriterion("IsDefault >=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThan(Byte value) {
            addCriterion("IsDefault <", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultLessThanOrEqualTo(Byte value) {
            addCriterion("IsDefault <=", value, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultIn(List<Byte> values) {
            addCriterion("IsDefault in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotIn(List<Byte> values) {
            addCriterion("IsDefault not in", values, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultBetween(Byte value1, Byte value2) {
            addCriterion("IsDefault between", value1, value2, "isDefault");
            return (Criteria) this;
        }

        public Criteria andIsDefaultNotBetween(Byte value1, Byte value2) {
            addCriterion("IsDefault not between", value1, value2, "isDefault");
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