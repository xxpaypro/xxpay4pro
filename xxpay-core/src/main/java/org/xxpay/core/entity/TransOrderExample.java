package org.xxpay.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransOrderExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public TransOrderExample() {
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

        public Criteria andAppIdIsNull() {
            addCriterion("AppId is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("AppId is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("AppId =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("AppId <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("AppId >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("AppId >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("AppId <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("AppId <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("AppId like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("AppId not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("AppId in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("AppId not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("AppId between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("AppId not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andMchTransNoIsNull() {
            addCriterion("MchTransNo is null");
            return (Criteria) this;
        }

        public Criteria andMchTransNoIsNotNull() {
            addCriterion("MchTransNo is not null");
            return (Criteria) this;
        }

        public Criteria andMchTransNoEqualTo(String value) {
            addCriterion("MchTransNo =", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoNotEqualTo(String value) {
            addCriterion("MchTransNo <>", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoGreaterThan(String value) {
            addCriterion("MchTransNo >", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoGreaterThanOrEqualTo(String value) {
            addCriterion("MchTransNo >=", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoLessThan(String value) {
            addCriterion("MchTransNo <", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoLessThanOrEqualTo(String value) {
            addCriterion("MchTransNo <=", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoLike(String value) {
            addCriterion("MchTransNo like", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoNotLike(String value) {
            addCriterion("MchTransNo not like", value, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoIn(List<String> values) {
            addCriterion("MchTransNo in", values, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoNotIn(List<String> values) {
            addCriterion("MchTransNo not in", values, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoBetween(String value1, String value2) {
            addCriterion("MchTransNo between", value1, value2, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andMchTransNoNotBetween(String value1, String value2) {
            addCriterion("MchTransNo not between", value1, value2, "mchTransNo");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNull() {
            addCriterion("ChannelType is null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNotNull() {
            addCriterion("ChannelType is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeEqualTo(String value) {
            addCriterion("ChannelType =", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotEqualTo(String value) {
            addCriterion("ChannelType <>", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThan(String value) {
            addCriterion("ChannelType >", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelType >=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThan(String value) {
            addCriterion("ChannelType <", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThanOrEqualTo(String value) {
            addCriterion("ChannelType <=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLike(String value) {
            addCriterion("ChannelType like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotLike(String value) {
            addCriterion("ChannelType not like", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIn(List<String> values) {
            addCriterion("ChannelType in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotIn(List<String> values) {
            addCriterion("ChannelType not in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeBetween(String value1, String value2) {
            addCriterion("ChannelType between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotBetween(String value1, String value2) {
            addCriterion("ChannelType not between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("ChannelId is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("ChannelId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(String value) {
            addCriterion("ChannelId =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(String value) {
            addCriterion("ChannelId <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(String value) {
            addCriterion("ChannelId >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelId >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(String value) {
            addCriterion("ChannelId <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(String value) {
            addCriterion("ChannelId <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLike(String value) {
            addCriterion("ChannelId like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotLike(String value) {
            addCriterion("ChannelId not like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<String> values) {
            addCriterion("ChannelId in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<String> values) {
            addCriterion("ChannelId not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(String value1, String value2) {
            addCriterion("ChannelId between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(String value1, String value2) {
            addCriterion("ChannelId not between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andPassageIdIsNull() {
            addCriterion("PassageId is null");
            return (Criteria) this;
        }

        public Criteria andPassageIdIsNotNull() {
            addCriterion("PassageId is not null");
            return (Criteria) this;
        }

        public Criteria andPassageIdEqualTo(Integer value) {
            addCriterion("PassageId =", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotEqualTo(Integer value) {
            addCriterion("PassageId <>", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdGreaterThan(Integer value) {
            addCriterion("PassageId >", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("PassageId >=", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdLessThan(Integer value) {
            addCriterion("PassageId <", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdLessThanOrEqualTo(Integer value) {
            addCriterion("PassageId <=", value, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdIn(List<Integer> values) {
            addCriterion("PassageId in", values, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotIn(List<Integer> values) {
            addCriterion("PassageId not in", values, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdBetween(Integer value1, Integer value2) {
            addCriterion("PassageId between", value1, value2, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("PassageId not between", value1, value2, "passageId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdIsNull() {
            addCriterion("PassageAccountId is null");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdIsNotNull() {
            addCriterion("PassageAccountId is not null");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdEqualTo(Integer value) {
            addCriterion("PassageAccountId =", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdNotEqualTo(Integer value) {
            addCriterion("PassageAccountId <>", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdGreaterThan(Integer value) {
            addCriterion("PassageAccountId >", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("PassageAccountId >=", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdLessThan(Integer value) {
            addCriterion("PassageAccountId <", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdLessThanOrEqualTo(Integer value) {
            addCriterion("PassageAccountId <=", value, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdIn(List<Integer> values) {
            addCriterion("PassageAccountId in", values, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdNotIn(List<Integer> values) {
            addCriterion("PassageAccountId not in", values, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdBetween(Integer value1, Integer value2) {
            addCriterion("PassageAccountId between", value1, value2, "passageAccountId");
            return (Criteria) this;
        }

        public Criteria andPassageAccountIdNotBetween(Integer value1, Integer value2) {
            addCriterion("PassageAccountId not between", value1, value2, "passageAccountId");
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

        public Criteria andCurrencyIsNull() {
            addCriterion("Currency is null");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNotNull() {
            addCriterion("Currency is not null");
            return (Criteria) this;
        }

        public Criteria andCurrencyEqualTo(String value) {
            addCriterion("Currency =", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotEqualTo(String value) {
            addCriterion("Currency <>", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThan(String value) {
            addCriterion("Currency >", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("Currency >=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThan(String value) {
            addCriterion("Currency <", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThanOrEqualTo(String value) {
            addCriterion("Currency <=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLike(String value) {
            addCriterion("Currency like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotLike(String value) {
            addCriterion("Currency not like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyIn(List<String> values) {
            addCriterion("Currency in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotIn(List<String> values) {
            addCriterion("Currency not in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyBetween(String value1, String value2) {
            addCriterion("Currency between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotBetween(String value1, String value2) {
            addCriterion("Currency not between", value1, value2, "currency");
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

        public Criteria andResultIsNull() {
            addCriterion("Result is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("Result is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(Byte value) {
            addCriterion("Result =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(Byte value) {
            addCriterion("Result <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(Byte value) {
            addCriterion("Result >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(Byte value) {
            addCriterion("Result >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(Byte value) {
            addCriterion("Result <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(Byte value) {
            addCriterion("Result <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<Byte> values) {
            addCriterion("Result in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<Byte> values) {
            addCriterion("Result not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(Byte value1, Byte value2) {
            addCriterion("Result between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(Byte value1, Byte value2) {
            addCriterion("Result not between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNull() {
            addCriterion("ClientIp is null");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNotNull() {
            addCriterion("ClientIp is not null");
            return (Criteria) this;
        }

        public Criteria andClientIpEqualTo(String value) {
            addCriterion("ClientIp =", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotEqualTo(String value) {
            addCriterion("ClientIp <>", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThan(String value) {
            addCriterion("ClientIp >", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThanOrEqualTo(String value) {
            addCriterion("ClientIp >=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThan(String value) {
            addCriterion("ClientIp <", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThanOrEqualTo(String value) {
            addCriterion("ClientIp <=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLike(String value) {
            addCriterion("ClientIp like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotLike(String value) {
            addCriterion("ClientIp not like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpIn(List<String> values) {
            addCriterion("ClientIp in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotIn(List<String> values) {
            addCriterion("ClientIp not in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpBetween(String value1, String value2) {
            addCriterion("ClientIp between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotBetween(String value1, String value2) {
            addCriterion("ClientIp not between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andDeviceIsNull() {
            addCriterion("Device is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIsNotNull() {
            addCriterion("Device is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceEqualTo(String value) {
            addCriterion("Device =", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotEqualTo(String value) {
            addCriterion("Device <>", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceGreaterThan(String value) {
            addCriterion("Device >", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceGreaterThanOrEqualTo(String value) {
            addCriterion("Device >=", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLessThan(String value) {
            addCriterion("Device <", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLessThanOrEqualTo(String value) {
            addCriterion("Device <=", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceLike(String value) {
            addCriterion("Device like", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotLike(String value) {
            addCriterion("Device not like", value, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceIn(List<String> values) {
            addCriterion("Device in", values, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotIn(List<String> values) {
            addCriterion("Device not in", values, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceBetween(String value1, String value2) {
            addCriterion("Device between", value1, value2, "device");
            return (Criteria) this;
        }

        public Criteria andDeviceNotBetween(String value1, String value2) {
            addCriterion("Device not between", value1, value2, "device");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoIsNull() {
            addCriterion("RemarkInfo is null");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoIsNotNull() {
            addCriterion("RemarkInfo is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoEqualTo(String value) {
            addCriterion("RemarkInfo =", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoNotEqualTo(String value) {
            addCriterion("RemarkInfo <>", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoGreaterThan(String value) {
            addCriterion("RemarkInfo >", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoGreaterThanOrEqualTo(String value) {
            addCriterion("RemarkInfo >=", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoLessThan(String value) {
            addCriterion("RemarkInfo <", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoLessThanOrEqualTo(String value) {
            addCriterion("RemarkInfo <=", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoLike(String value) {
            addCriterion("RemarkInfo like", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoNotLike(String value) {
            addCriterion("RemarkInfo not like", value, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoIn(List<String> values) {
            addCriterion("RemarkInfo in", values, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoNotIn(List<String> values) {
            addCriterion("RemarkInfo not in", values, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoBetween(String value1, String value2) {
            addCriterion("RemarkInfo between", value1, value2, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andRemarkInfoNotBetween(String value1, String value2) {
            addCriterion("RemarkInfo not between", value1, value2, "remarkInfo");
            return (Criteria) this;
        }

        public Criteria andChannelUserIsNull() {
            addCriterion("ChannelUser is null");
            return (Criteria) this;
        }

        public Criteria andChannelUserIsNotNull() {
            addCriterion("ChannelUser is not null");
            return (Criteria) this;
        }

        public Criteria andChannelUserEqualTo(String value) {
            addCriterion("ChannelUser =", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserNotEqualTo(String value) {
            addCriterion("ChannelUser <>", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserGreaterThan(String value) {
            addCriterion("ChannelUser >", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelUser >=", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserLessThan(String value) {
            addCriterion("ChannelUser <", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserLessThanOrEqualTo(String value) {
            addCriterion("ChannelUser <=", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserLike(String value) {
            addCriterion("ChannelUser like", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserNotLike(String value) {
            addCriterion("ChannelUser not like", value, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserIn(List<String> values) {
            addCriterion("ChannelUser in", values, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserNotIn(List<String> values) {
            addCriterion("ChannelUser not in", values, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserBetween(String value1, String value2) {
            addCriterion("ChannelUser between", value1, value2, "channelUser");
            return (Criteria) this;
        }

        public Criteria andChannelUserNotBetween(String value1, String value2) {
            addCriterion("ChannelUser not between", value1, value2, "channelUser");
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

        public Criteria andBankTypeIsNull() {
            addCriterion("BankType is null");
            return (Criteria) this;
        }

        public Criteria andBankTypeIsNotNull() {
            addCriterion("BankType is not null");
            return (Criteria) this;
        }

        public Criteria andBankTypeEqualTo(Long value) {
            addCriterion("BankType =", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotEqualTo(Long value) {
            addCriterion("BankType <>", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThan(Long value) {
            addCriterion("BankType >", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeGreaterThanOrEqualTo(Long value) {
            addCriterion("BankType >=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThan(Long value) {
            addCriterion("BankType <", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeLessThanOrEqualTo(Long value) {
            addCriterion("BankType <=", value, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeIn(List<Long> values) {
            addCriterion("BankType in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotIn(List<Long> values) {
            addCriterion("BankType not in", values, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeBetween(Long value1, Long value2) {
            addCriterion("BankType between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankTypeNotBetween(Long value1, Long value2) {
            addCriterion("BankType not between", value1, value2, "bankType");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNull() {
            addCriterion("BankCode is null");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNotNull() {
            addCriterion("BankCode is not null");
            return (Criteria) this;
        }

        public Criteria andBankCodeEqualTo(String value) {
            addCriterion("BankCode =", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotEqualTo(String value) {
            addCriterion("BankCode <>", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThan(String value) {
            addCriterion("BankCode >", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThanOrEqualTo(String value) {
            addCriterion("BankCode >=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThan(String value) {
            addCriterion("BankCode <", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThanOrEqualTo(String value) {
            addCriterion("BankCode <=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLike(String value) {
            addCriterion("BankCode like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotLike(String value) {
            addCriterion("BankCode not like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeIn(List<String> values) {
            addCriterion("BankCode in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotIn(List<String> values) {
            addCriterion("BankCode not in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeBetween(String value1, String value2) {
            addCriterion("BankCode between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotBetween(String value1, String value2) {
            addCriterion("BankCode not between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIsNull() {
            addCriterion("ChannelMchId is null");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIsNotNull() {
            addCriterion("ChannelMchId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdEqualTo(String value) {
            addCriterion("ChannelMchId =", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotEqualTo(String value) {
            addCriterion("ChannelMchId <>", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdGreaterThan(String value) {
            addCriterion("ChannelMchId >", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelMchId >=", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLessThan(String value) {
            addCriterion("ChannelMchId <", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLessThanOrEqualTo(String value) {
            addCriterion("ChannelMchId <=", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdLike(String value) {
            addCriterion("ChannelMchId like", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotLike(String value) {
            addCriterion("ChannelMchId not like", value, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdIn(List<String> values) {
            addCriterion("ChannelMchId in", values, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotIn(List<String> values) {
            addCriterion("ChannelMchId not in", values, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdBetween(String value1, String value2) {
            addCriterion("ChannelMchId between", value1, value2, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelMchIdNotBetween(String value1, String value2) {
            addCriterion("ChannelMchId not between", value1, value2, "channelMchId");
            return (Criteria) this;
        }

        public Criteria andChannelRateIsNull() {
            addCriterion("ChannelRate is null");
            return (Criteria) this;
        }

        public Criteria andChannelRateIsNotNull() {
            addCriterion("ChannelRate is not null");
            return (Criteria) this;
        }

        public Criteria andChannelRateEqualTo(BigDecimal value) {
            addCriterion("ChannelRate =", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateNotEqualTo(BigDecimal value) {
            addCriterion("ChannelRate <>", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateGreaterThan(BigDecimal value) {
            addCriterion("ChannelRate >", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ChannelRate >=", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateLessThan(BigDecimal value) {
            addCriterion("ChannelRate <", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ChannelRate <=", value, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateIn(List<BigDecimal> values) {
            addCriterion("ChannelRate in", values, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateNotIn(List<BigDecimal> values) {
            addCriterion("ChannelRate not in", values, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ChannelRate between", value1, value2, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ChannelRate not between", value1, value2, "channelRate");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryIsNull() {
            addCriterion("channelFeeEvery is null");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryIsNotNull() {
            addCriterion("channelFeeEvery is not null");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryEqualTo(Long value) {
            addCriterion("channelFeeEvery =", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryNotEqualTo(Long value) {
            addCriterion("channelFeeEvery <>", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryGreaterThan(Long value) {
            addCriterion("channelFeeEvery >", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryGreaterThanOrEqualTo(Long value) {
            addCriterion("channelFeeEvery >=", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryLessThan(Long value) {
            addCriterion("channelFeeEvery <", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryLessThanOrEqualTo(Long value) {
            addCriterion("channelFeeEvery <=", value, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryIn(List<Long> values) {
            addCriterion("channelFeeEvery in", values, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryNotIn(List<Long> values) {
            addCriterion("channelFeeEvery not in", values, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryBetween(Long value1, Long value2) {
            addCriterion("channelFeeEvery between", value1, value2, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEveryNotBetween(Long value1, Long value2) {
            addCriterion("channelFeeEvery not between", value1, value2, "channelFeeEvery");
            return (Criteria) this;
        }

        public Criteria andChannelCostIsNull() {
            addCriterion("ChannelCost is null");
            return (Criteria) this;
        }

        public Criteria andChannelCostIsNotNull() {
            addCriterion("ChannelCost is not null");
            return (Criteria) this;
        }

        public Criteria andChannelCostEqualTo(Long value) {
            addCriterion("ChannelCost =", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotEqualTo(Long value) {
            addCriterion("ChannelCost <>", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostGreaterThan(Long value) {
            addCriterion("ChannelCost >", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostGreaterThanOrEqualTo(Long value) {
            addCriterion("ChannelCost >=", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostLessThan(Long value) {
            addCriterion("ChannelCost <", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostLessThanOrEqualTo(Long value) {
            addCriterion("ChannelCost <=", value, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostIn(List<Long> values) {
            addCriterion("ChannelCost in", values, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotIn(List<Long> values) {
            addCriterion("ChannelCost not in", values, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostBetween(Long value1, Long value2) {
            addCriterion("ChannelCost between", value1, value2, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelCostNotBetween(Long value1, Long value2) {
            addCriterion("ChannelCost not between", value1, value2, "channelCost");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIsNull() {
            addCriterion("ChannelOrderNo is null");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIsNotNull() {
            addCriterion("ChannelOrderNo is not null");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoEqualTo(String value) {
            addCriterion("ChannelOrderNo =", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotEqualTo(String value) {
            addCriterion("ChannelOrderNo <>", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoGreaterThan(String value) {
            addCriterion("ChannelOrderNo >", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelOrderNo >=", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLessThan(String value) {
            addCriterion("ChannelOrderNo <", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLessThanOrEqualTo(String value) {
            addCriterion("ChannelOrderNo <=", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoLike(String value) {
            addCriterion("ChannelOrderNo like", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotLike(String value) {
            addCriterion("ChannelOrderNo not like", value, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoIn(List<String> values) {
            addCriterion("ChannelOrderNo in", values, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotIn(List<String> values) {
            addCriterion("ChannelOrderNo not in", values, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoBetween(String value1, String value2) {
            addCriterion("ChannelOrderNo between", value1, value2, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelOrderNoNotBetween(String value1, String value2) {
            addCriterion("ChannelOrderNo not between", value1, value2, "channelOrderNo");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeIsNull() {
            addCriterion("ChannelErrCode is null");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeIsNotNull() {
            addCriterion("ChannelErrCode is not null");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeEqualTo(String value) {
            addCriterion("ChannelErrCode =", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeNotEqualTo(String value) {
            addCriterion("ChannelErrCode <>", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeGreaterThan(String value) {
            addCriterion("ChannelErrCode >", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelErrCode >=", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeLessThan(String value) {
            addCriterion("ChannelErrCode <", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeLessThanOrEqualTo(String value) {
            addCriterion("ChannelErrCode <=", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeLike(String value) {
            addCriterion("ChannelErrCode like", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeNotLike(String value) {
            addCriterion("ChannelErrCode not like", value, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeIn(List<String> values) {
            addCriterion("ChannelErrCode in", values, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeNotIn(List<String> values) {
            addCriterion("ChannelErrCode not in", values, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeBetween(String value1, String value2) {
            addCriterion("ChannelErrCode between", value1, value2, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrCodeNotBetween(String value1, String value2) {
            addCriterion("ChannelErrCode not between", value1, value2, "channelErrCode");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgIsNull() {
            addCriterion("ChannelErrMsg is null");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgIsNotNull() {
            addCriterion("ChannelErrMsg is not null");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgEqualTo(String value) {
            addCriterion("ChannelErrMsg =", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgNotEqualTo(String value) {
            addCriterion("ChannelErrMsg <>", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgGreaterThan(String value) {
            addCriterion("ChannelErrMsg >", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelErrMsg >=", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgLessThan(String value) {
            addCriterion("ChannelErrMsg <", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgLessThanOrEqualTo(String value) {
            addCriterion("ChannelErrMsg <=", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgLike(String value) {
            addCriterion("ChannelErrMsg like", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgNotLike(String value) {
            addCriterion("ChannelErrMsg not like", value, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgIn(List<String> values) {
            addCriterion("ChannelErrMsg in", values, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgNotIn(List<String> values) {
            addCriterion("ChannelErrMsg not in", values, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgBetween(String value1, String value2) {
            addCriterion("ChannelErrMsg between", value1, value2, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andChannelErrMsgNotBetween(String value1, String value2) {
            addCriterion("ChannelErrMsg not between", value1, value2, "channelErrMsg");
            return (Criteria) this;
        }

        public Criteria andExtraIsNull() {
            addCriterion("Extra is null");
            return (Criteria) this;
        }

        public Criteria andExtraIsNotNull() {
            addCriterion("Extra is not null");
            return (Criteria) this;
        }

        public Criteria andExtraEqualTo(String value) {
            addCriterion("Extra =", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotEqualTo(String value) {
            addCriterion("Extra <>", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThan(String value) {
            addCriterion("Extra >", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThanOrEqualTo(String value) {
            addCriterion("Extra >=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThan(String value) {
            addCriterion("Extra <", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThanOrEqualTo(String value) {
            addCriterion("Extra <=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLike(String value) {
            addCriterion("Extra like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotLike(String value) {
            addCriterion("Extra not like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraIn(List<String> values) {
            addCriterion("Extra in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotIn(List<String> values) {
            addCriterion("Extra not in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraBetween(String value1, String value2) {
            addCriterion("Extra between", value1, value2, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotBetween(String value1, String value2) {
            addCriterion("Extra not between", value1, value2, "extra");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIsNull() {
            addCriterion("NotifyUrl is null");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIsNotNull() {
            addCriterion("NotifyUrl is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlEqualTo(String value) {
            addCriterion("NotifyUrl =", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotEqualTo(String value) {
            addCriterion("NotifyUrl <>", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlGreaterThan(String value) {
            addCriterion("NotifyUrl >", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlGreaterThanOrEqualTo(String value) {
            addCriterion("NotifyUrl >=", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLessThan(String value) {
            addCriterion("NotifyUrl <", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLessThanOrEqualTo(String value) {
            addCriterion("NotifyUrl <=", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlLike(String value) {
            addCriterion("NotifyUrl like", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotLike(String value) {
            addCriterion("NotifyUrl not like", value, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlIn(List<String> values) {
            addCriterion("NotifyUrl in", values, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotIn(List<String> values) {
            addCriterion("NotifyUrl not in", values, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlBetween(String value1, String value2) {
            addCriterion("NotifyUrl between", value1, value2, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andNotifyUrlNotBetween(String value1, String value2) {
            addCriterion("NotifyUrl not between", value1, value2, "notifyUrl");
            return (Criteria) this;
        }

        public Criteria andParam1IsNull() {
            addCriterion("Param1 is null");
            return (Criteria) this;
        }

        public Criteria andParam1IsNotNull() {
            addCriterion("Param1 is not null");
            return (Criteria) this;
        }

        public Criteria andParam1EqualTo(String value) {
            addCriterion("Param1 =", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1NotEqualTo(String value) {
            addCriterion("Param1 <>", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1GreaterThan(String value) {
            addCriterion("Param1 >", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1GreaterThanOrEqualTo(String value) {
            addCriterion("Param1 >=", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1LessThan(String value) {
            addCriterion("Param1 <", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1LessThanOrEqualTo(String value) {
            addCriterion("Param1 <=", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1Like(String value) {
            addCriterion("Param1 like", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1NotLike(String value) {
            addCriterion("Param1 not like", value, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1In(List<String> values) {
            addCriterion("Param1 in", values, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1NotIn(List<String> values) {
            addCriterion("Param1 not in", values, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1Between(String value1, String value2) {
            addCriterion("Param1 between", value1, value2, "param1");
            return (Criteria) this;
        }

        public Criteria andParam1NotBetween(String value1, String value2) {
            addCriterion("Param1 not between", value1, value2, "param1");
            return (Criteria) this;
        }

        public Criteria andParam2IsNull() {
            addCriterion("Param2 is null");
            return (Criteria) this;
        }

        public Criteria andParam2IsNotNull() {
            addCriterion("Param2 is not null");
            return (Criteria) this;
        }

        public Criteria andParam2EqualTo(String value) {
            addCriterion("Param2 =", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2NotEqualTo(String value) {
            addCriterion("Param2 <>", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2GreaterThan(String value) {
            addCriterion("Param2 >", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2GreaterThanOrEqualTo(String value) {
            addCriterion("Param2 >=", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2LessThan(String value) {
            addCriterion("Param2 <", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2LessThanOrEqualTo(String value) {
            addCriterion("Param2 <=", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2Like(String value) {
            addCriterion("Param2 like", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2NotLike(String value) {
            addCriterion("Param2 not like", value, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2In(List<String> values) {
            addCriterion("Param2 in", values, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2NotIn(List<String> values) {
            addCriterion("Param2 not in", values, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2Between(String value1, String value2) {
            addCriterion("Param2 between", value1, value2, "param2");
            return (Criteria) this;
        }

        public Criteria andParam2NotBetween(String value1, String value2) {
            addCriterion("Param2 not between", value1, value2, "param2");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNull() {
            addCriterion("ExpireTime is null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNotNull() {
            addCriterion("ExpireTime is not null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeEqualTo(Date value) {
            addCriterion("ExpireTime =", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotEqualTo(Date value) {
            addCriterion("ExpireTime <>", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThan(Date value) {
            addCriterion("ExpireTime >", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ExpireTime >=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThan(Date value) {
            addCriterion("ExpireTime <", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThanOrEqualTo(Date value) {
            addCriterion("ExpireTime <=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIn(List<Date> values) {
            addCriterion("ExpireTime in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotIn(List<Date> values) {
            addCriterion("ExpireTime not in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeBetween(Date value1, Date value2) {
            addCriterion("ExpireTime between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotBetween(Date value1, Date value2) {
            addCriterion("ExpireTime not between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeIsNull() {
            addCriterion("TransSuccTime is null");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeIsNotNull() {
            addCriterion("TransSuccTime is not null");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeEqualTo(Date value) {
            addCriterion("TransSuccTime =", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeNotEqualTo(Date value) {
            addCriterion("TransSuccTime <>", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeGreaterThan(Date value) {
            addCriterion("TransSuccTime >", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("TransSuccTime >=", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeLessThan(Date value) {
            addCriterion("TransSuccTime <", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeLessThanOrEqualTo(Date value) {
            addCriterion("TransSuccTime <=", value, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeIn(List<Date> values) {
            addCriterion("TransSuccTime in", values, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeNotIn(List<Date> values) {
            addCriterion("TransSuccTime not in", values, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeBetween(Date value1, Date value2) {
            addCriterion("TransSuccTime between", value1, value2, "transSuccTime");
            return (Criteria) this;
        }

        public Criteria andTransSuccTimeNotBetween(Date value1, Date value2) {
            addCriterion("TransSuccTime not between", value1, value2, "transSuccTime");
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