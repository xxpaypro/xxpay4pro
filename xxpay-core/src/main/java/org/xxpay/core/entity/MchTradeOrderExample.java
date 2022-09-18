package org.xxpay.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MchTradeOrderExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public MchTradeOrderExample() {
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

        public Criteria andTradeOrderIdIsNull() {
            addCriterion("TradeOrderId is null");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdIsNotNull() {
            addCriterion("TradeOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdEqualTo(String value) {
            addCriterion("TradeOrderId =", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdNotEqualTo(String value) {
            addCriterion("TradeOrderId <>", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdGreaterThan(String value) {
            addCriterion("TradeOrderId >", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("TradeOrderId >=", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdLessThan(String value) {
            addCriterion("TradeOrderId <", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdLessThanOrEqualTo(String value) {
            addCriterion("TradeOrderId <=", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdLike(String value) {
            addCriterion("TradeOrderId like", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdNotLike(String value) {
            addCriterion("TradeOrderId not like", value, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdIn(List<String> values) {
            addCriterion("TradeOrderId in", values, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdNotIn(List<String> values) {
            addCriterion("TradeOrderId not in", values, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdBetween(String value1, String value2) {
            addCriterion("TradeOrderId between", value1, value2, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeOrderIdNotBetween(String value1, String value2) {
            addCriterion("TradeOrderId not between", value1, value2, "tradeOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeTypeIsNull() {
            addCriterion("TradeType is null");
            return (Criteria) this;
        }

        public Criteria andTradeTypeIsNotNull() {
            addCriterion("TradeType is not null");
            return (Criteria) this;
        }

        public Criteria andTradeTypeEqualTo(Byte value) {
            addCriterion("TradeType =", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeNotEqualTo(Byte value) {
            addCriterion("TradeType <>", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeGreaterThan(Byte value) {
            addCriterion("TradeType >", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("TradeType >=", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeLessThan(Byte value) {
            addCriterion("TradeType <", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("TradeType <=", value, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeIn(List<Byte> values) {
            addCriterion("TradeType in", values, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeNotIn(List<Byte> values) {
            addCriterion("TradeType not in", values, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeBetween(Byte value1, Byte value2) {
            addCriterion("TradeType between", value1, value2, "tradeType");
            return (Criteria) this;
        }

        public Criteria andTradeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("TradeType not between", value1, value2, "tradeType");
            return (Criteria) this;
        }

        public Criteria andDepositModeIsNull() {
            addCriterion("DepositMode is null");
            return (Criteria) this;
        }

        public Criteria andDepositModeIsNotNull() {
            addCriterion("DepositMode is not null");
            return (Criteria) this;
        }

        public Criteria andDepositModeEqualTo(Byte value) {
            addCriterion("DepositMode =", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeNotEqualTo(Byte value) {
            addCriterion("DepositMode <>", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeGreaterThan(Byte value) {
            addCriterion("DepositMode >", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeGreaterThanOrEqualTo(Byte value) {
            addCriterion("DepositMode >=", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeLessThan(Byte value) {
            addCriterion("DepositMode <", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeLessThanOrEqualTo(Byte value) {
            addCriterion("DepositMode <=", value, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeIn(List<Byte> values) {
            addCriterion("DepositMode in", values, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeNotIn(List<Byte> values) {
            addCriterion("DepositMode not in", values, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeBetween(Byte value1, Byte value2) {
            addCriterion("DepositMode between", value1, value2, "depositMode");
            return (Criteria) this;
        }

        public Criteria andDepositModeNotBetween(Byte value1, Byte value2) {
            addCriterion("DepositMode not between", value1, value2, "depositMode");
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

        public Criteria andAgentIdIsNull() {
            addCriterion("AgentId is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNotNull() {
            addCriterion("AgentId is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdEqualTo(Long value) {
            addCriterion("AgentId =", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotEqualTo(Long value) {
            addCriterion("AgentId <>", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThan(Long value) {
            addCriterion("AgentId >", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("AgentId >=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThan(Long value) {
            addCriterion("AgentId <", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThanOrEqualTo(Long value) {
            addCriterion("AgentId <=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdIn(List<Long> values) {
            addCriterion("AgentId in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotIn(List<Long> values) {
            addCriterion("AgentId not in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdBetween(Long value1, Long value2) {
            addCriterion("AgentId between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotBetween(Long value1, Long value2) {
            addCriterion("AgentId not between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andIsvIdIsNull() {
            addCriterion("IsvId is null");
            return (Criteria) this;
        }

        public Criteria andIsvIdIsNotNull() {
            addCriterion("IsvId is not null");
            return (Criteria) this;
        }

        public Criteria andIsvIdEqualTo(Long value) {
            addCriterion("IsvId =", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotEqualTo(Long value) {
            addCriterion("IsvId <>", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdGreaterThan(Long value) {
            addCriterion("IsvId >", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdGreaterThanOrEqualTo(Long value) {
            addCriterion("IsvId >=", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdLessThan(Long value) {
            addCriterion("IsvId <", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdLessThanOrEqualTo(Long value) {
            addCriterion("IsvId <=", value, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdIn(List<Long> values) {
            addCriterion("IsvId in", values, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotIn(List<Long> values) {
            addCriterion("IsvId not in", values, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdBetween(Long value1, Long value2) {
            addCriterion("IsvId between", value1, value2, "isvId");
            return (Criteria) this;
        }

        public Criteria andIsvIdNotBetween(Long value1, Long value2) {
            addCriterion("IsvId not between", value1, value2, "isvId");
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

        public Criteria andGoodsIdIsNull() {
            addCriterion("GoodsId is null");
            return (Criteria) this;
        }

        public Criteria andGoodsIdIsNotNull() {
            addCriterion("GoodsId is not null");
            return (Criteria) this;
        }

        public Criteria andGoodsIdEqualTo(String value) {
            addCriterion("GoodsId =", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotEqualTo(String value) {
            addCriterion("GoodsId <>", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdGreaterThan(String value) {
            addCriterion("GoodsId >", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdGreaterThanOrEqualTo(String value) {
            addCriterion("GoodsId >=", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdLessThan(String value) {
            addCriterion("GoodsId <", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdLessThanOrEqualTo(String value) {
            addCriterion("GoodsId <=", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdLike(String value) {
            addCriterion("GoodsId like", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotLike(String value) {
            addCriterion("GoodsId not like", value, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdIn(List<String> values) {
            addCriterion("GoodsId in", values, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotIn(List<String> values) {
            addCriterion("GoodsId not in", values, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdBetween(String value1, String value2) {
            addCriterion("GoodsId between", value1, value2, "goodsId");
            return (Criteria) this;
        }

        public Criteria andGoodsIdNotBetween(String value1, String value2) {
            addCriterion("GoodsId not between", value1, value2, "goodsId");
            return (Criteria) this;
        }

        public Criteria andSubjectIsNull() {
            addCriterion("Subject is null");
            return (Criteria) this;
        }

        public Criteria andSubjectIsNotNull() {
            addCriterion("Subject is not null");
            return (Criteria) this;
        }

        public Criteria andSubjectEqualTo(String value) {
            addCriterion("Subject =", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotEqualTo(String value) {
            addCriterion("Subject <>", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectGreaterThan(String value) {
            addCriterion("Subject >", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectGreaterThanOrEqualTo(String value) {
            addCriterion("Subject >=", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLessThan(String value) {
            addCriterion("Subject <", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLessThanOrEqualTo(String value) {
            addCriterion("Subject <=", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLike(String value) {
            addCriterion("Subject like", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotLike(String value) {
            addCriterion("Subject not like", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectIn(List<String> values) {
            addCriterion("Subject in", values, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotIn(List<String> values) {
            addCriterion("Subject not in", values, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectBetween(String value1, String value2) {
            addCriterion("Subject between", value1, value2, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotBetween(String value1, String value2) {
            addCriterion("Subject not between", value1, value2, "subject");
            return (Criteria) this;
        }

        public Criteria andBodyIsNull() {
            addCriterion("Body is null");
            return (Criteria) this;
        }

        public Criteria andBodyIsNotNull() {
            addCriterion("Body is not null");
            return (Criteria) this;
        }

        public Criteria andBodyEqualTo(String value) {
            addCriterion("Body =", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotEqualTo(String value) {
            addCriterion("Body <>", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThan(String value) {
            addCriterion("Body >", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThanOrEqualTo(String value) {
            addCriterion("Body >=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThan(String value) {
            addCriterion("Body <", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThanOrEqualTo(String value) {
            addCriterion("Body <=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLike(String value) {
            addCriterion("Body like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotLike(String value) {
            addCriterion("Body not like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyIn(List<String> values) {
            addCriterion("Body in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotIn(List<String> values) {
            addCriterion("Body not in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyBetween(String value1, String value2) {
            addCriterion("Body between", value1, value2, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotBetween(String value1, String value2) {
            addCriterion("Body not between", value1, value2, "body");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNull() {
            addCriterion("OrderAmount is null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIsNotNull() {
            addCriterion("OrderAmount is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAmountEqualTo(Long value) {
            addCriterion("OrderAmount =", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotEqualTo(Long value) {
            addCriterion("OrderAmount <>", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThan(Long value) {
            addCriterion("OrderAmount >", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("OrderAmount >=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThan(Long value) {
            addCriterion("OrderAmount <", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountLessThanOrEqualTo(Long value) {
            addCriterion("OrderAmount <=", value, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountIn(List<Long> values) {
            addCriterion("OrderAmount in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotIn(List<Long> values) {
            addCriterion("OrderAmount not in", values, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountBetween(Long value1, Long value2) {
            addCriterion("OrderAmount between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andOrderAmountNotBetween(Long value1, Long value2) {
            addCriterion("OrderAmount not between", value1, value2, "orderAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNull() {
            addCriterion("DiscountAmount is null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIsNotNull() {
            addCriterion("DiscountAmount is not null");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountEqualTo(Long value) {
            addCriterion("DiscountAmount =", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotEqualTo(Long value) {
            addCriterion("DiscountAmount <>", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThan(Long value) {
            addCriterion("DiscountAmount >", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("DiscountAmount >=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThan(Long value) {
            addCriterion("DiscountAmount <", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountLessThanOrEqualTo(Long value) {
            addCriterion("DiscountAmount <=", value, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountIn(List<Long> values) {
            addCriterion("DiscountAmount in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotIn(List<Long> values) {
            addCriterion("DiscountAmount not in", values, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountBetween(Long value1, Long value2) {
            addCriterion("DiscountAmount between", value1, value2, "discountAmount");
            return (Criteria) this;
        }

        public Criteria andDiscountAmountNotBetween(Long value1, Long value2) {
            addCriterion("DiscountAmount not between", value1, value2, "discountAmount");
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

        public Criteria andDepositAmountIsNull() {
            addCriterion("DepositAmount is null");
            return (Criteria) this;
        }

        public Criteria andDepositAmountIsNotNull() {
            addCriterion("DepositAmount is not null");
            return (Criteria) this;
        }

        public Criteria andDepositAmountEqualTo(Long value) {
            addCriterion("DepositAmount =", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountNotEqualTo(Long value) {
            addCriterion("DepositAmount <>", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountGreaterThan(Long value) {
            addCriterion("DepositAmount >", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("DepositAmount >=", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountLessThan(Long value) {
            addCriterion("DepositAmount <", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountLessThanOrEqualTo(Long value) {
            addCriterion("DepositAmount <=", value, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountIn(List<Long> values) {
            addCriterion("DepositAmount in", values, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountNotIn(List<Long> values) {
            addCriterion("DepositAmount not in", values, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountBetween(Long value1, Long value2) {
            addCriterion("DepositAmount between", value1, value2, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andDepositAmountNotBetween(Long value1, Long value2) {
            addCriterion("DepositAmount not between", value1, value2, "depositAmount");
            return (Criteria) this;
        }

        public Criteria andMchIncomeIsNull() {
            addCriterion("MchIncome is null");
            return (Criteria) this;
        }

        public Criteria andMchIncomeIsNotNull() {
            addCriterion("MchIncome is not null");
            return (Criteria) this;
        }

        public Criteria andMchIncomeEqualTo(Long value) {
            addCriterion("MchIncome =", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeNotEqualTo(Long value) {
            addCriterion("MchIncome <>", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeGreaterThan(Long value) {
            addCriterion("MchIncome >", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeGreaterThanOrEqualTo(Long value) {
            addCriterion("MchIncome >=", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeLessThan(Long value) {
            addCriterion("MchIncome <", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeLessThanOrEqualTo(Long value) {
            addCriterion("MchIncome <=", value, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeIn(List<Long> values) {
            addCriterion("MchIncome in", values, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeNotIn(List<Long> values) {
            addCriterion("MchIncome not in", values, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeBetween(Long value1, Long value2) {
            addCriterion("MchIncome between", value1, value2, "mchIncome");
            return (Criteria) this;
        }

        public Criteria andMchIncomeNotBetween(Long value1, Long value2) {
            addCriterion("MchIncome not between", value1, value2, "mchIncome");
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

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("UserId =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("UserId <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("UserId >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("UserId >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("UserId <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("UserId <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("UserId like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("UserId not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("UserId in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("UserId not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("UserId between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("UserId not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdIsNull() {
            addCriterion("ChannelUserId is null");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdIsNotNull() {
            addCriterion("ChannelUserId is not null");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdEqualTo(String value) {
            addCriterion("ChannelUserId =", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdNotEqualTo(String value) {
            addCriterion("ChannelUserId <>", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdGreaterThan(String value) {
            addCriterion("ChannelUserId >", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("ChannelUserId >=", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdLessThan(String value) {
            addCriterion("ChannelUserId <", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdLessThanOrEqualTo(String value) {
            addCriterion("ChannelUserId <=", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdLike(String value) {
            addCriterion("ChannelUserId like", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdNotLike(String value) {
            addCriterion("ChannelUserId not like", value, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdIn(List<String> values) {
            addCriterion("ChannelUserId in", values, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdNotIn(List<String> values) {
            addCriterion("ChannelUserId not in", values, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdBetween(String value1, String value2) {
            addCriterion("ChannelUserId between", value1, value2, "channelUserId");
            return (Criteria) this;
        }

        public Criteria andChannelUserIdNotBetween(String value1, String value2) {
            addCriterion("ChannelUserId not between", value1, value2, "channelUserId");
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

        public Criteria andPayOrderIdIsNull() {
            addCriterion("PayOrderId is null");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdIsNotNull() {
            addCriterion("PayOrderId is not null");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdEqualTo(String value) {
            addCriterion("PayOrderId =", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotEqualTo(String value) {
            addCriterion("PayOrderId <>", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdGreaterThan(String value) {
            addCriterion("PayOrderId >", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("PayOrderId >=", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLessThan(String value) {
            addCriterion("PayOrderId <", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLessThanOrEqualTo(String value) {
            addCriterion("PayOrderId <=", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdLike(String value) {
            addCriterion("PayOrderId like", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotLike(String value) {
            addCriterion("PayOrderId not like", value, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdIn(List<String> values) {
            addCriterion("PayOrderId in", values, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotIn(List<String> values) {
            addCriterion("PayOrderId not in", values, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdBetween(String value1, String value2) {
            addCriterion("PayOrderId between", value1, value2, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andPayOrderIdNotBetween(String value1, String value2) {
            addCriterion("PayOrderId not between", value1, value2, "payOrderId");
            return (Criteria) this;
        }

        public Criteria andProductIdIsNull() {
            addCriterion("ProductId is null");
            return (Criteria) this;
        }

        public Criteria andProductIdIsNotNull() {
            addCriterion("ProductId is not null");
            return (Criteria) this;
        }

        public Criteria andProductIdEqualTo(Integer value) {
            addCriterion("ProductId =", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotEqualTo(Integer value) {
            addCriterion("ProductId <>", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThan(Integer value) {
            addCriterion("ProductId >", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ProductId >=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThan(Integer value) {
            addCriterion("ProductId <", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdLessThanOrEqualTo(Integer value) {
            addCriterion("ProductId <=", value, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdIn(List<Integer> values) {
            addCriterion("ProductId in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotIn(List<Integer> values) {
            addCriterion("ProductId not in", values, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdBetween(Integer value1, Integer value2) {
            addCriterion("ProductId between", value1, value2, "productId");
            return (Criteria) this;
        }

        public Criteria andProductIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ProductId not between", value1, value2, "productId");
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

        public Criteria andMchCouponIdIsNull() {
            addCriterion("MchCouponId is null");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdIsNotNull() {
            addCriterion("MchCouponId is not null");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdEqualTo(Long value) {
            addCriterion("MchCouponId =", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdNotEqualTo(Long value) {
            addCriterion("MchCouponId <>", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdGreaterThan(Long value) {
            addCriterion("MchCouponId >", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MchCouponId >=", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdLessThan(Long value) {
            addCriterion("MchCouponId <", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdLessThanOrEqualTo(Long value) {
            addCriterion("MchCouponId <=", value, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdIn(List<Long> values) {
            addCriterion("MchCouponId in", values, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdNotIn(List<Long> values) {
            addCriterion("MchCouponId not in", values, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdBetween(Long value1, Long value2) {
            addCriterion("MchCouponId between", value1, value2, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMchCouponIdNotBetween(Long value1, Long value2) {
            addCriterion("MchCouponId not between", value1, value2, "mchCouponId");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoIsNull() {
            addCriterion("MemberCouponNo is null");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoIsNotNull() {
            addCriterion("MemberCouponNo is not null");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoEqualTo(String value) {
            addCriterion("MemberCouponNo =", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoNotEqualTo(String value) {
            addCriterion("MemberCouponNo <>", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoGreaterThan(String value) {
            addCriterion("MemberCouponNo >", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoGreaterThanOrEqualTo(String value) {
            addCriterion("MemberCouponNo >=", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoLessThan(String value) {
            addCriterion("MemberCouponNo <", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoLessThanOrEqualTo(String value) {
            addCriterion("MemberCouponNo <=", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoLike(String value) {
            addCriterion("MemberCouponNo like", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoNotLike(String value) {
            addCriterion("MemberCouponNo not like", value, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoIn(List<String> values) {
            addCriterion("MemberCouponNo in", values, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoNotIn(List<String> values) {
            addCriterion("MemberCouponNo not in", values, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoBetween(String value1, String value2) {
            addCriterion("MemberCouponNo between", value1, value2, "memberCouponNo");
            return (Criteria) this;
        }

        public Criteria andMemberCouponNoNotBetween(String value1, String value2) {
            addCriterion("MemberCouponNo not between", value1, value2, "memberCouponNo");
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

        public Criteria andStoreNoIsNull() {
            addCriterion("StoreNo is null");
            return (Criteria) this;
        }

        public Criteria andStoreNoIsNotNull() {
            addCriterion("StoreNo is not null");
            return (Criteria) this;
        }

        public Criteria andStoreNoEqualTo(String value) {
            addCriterion("StoreNo =", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoNotEqualTo(String value) {
            addCriterion("StoreNo <>", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoGreaterThan(String value) {
            addCriterion("StoreNo >", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoGreaterThanOrEqualTo(String value) {
            addCriterion("StoreNo >=", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoLessThan(String value) {
            addCriterion("StoreNo <", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoLessThanOrEqualTo(String value) {
            addCriterion("StoreNo <=", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoLike(String value) {
            addCriterion("StoreNo like", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoNotLike(String value) {
            addCriterion("StoreNo not like", value, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoIn(List<String> values) {
            addCriterion("StoreNo in", values, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoNotIn(List<String> values) {
            addCriterion("StoreNo not in", values, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoBetween(String value1, String value2) {
            addCriterion("StoreNo between", value1, value2, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNoNotBetween(String value1, String value2) {
            addCriterion("StoreNo not between", value1, value2, "storeNo");
            return (Criteria) this;
        }

        public Criteria andStoreNameIsNull() {
            addCriterion("StoreName is null");
            return (Criteria) this;
        }

        public Criteria andStoreNameIsNotNull() {
            addCriterion("StoreName is not null");
            return (Criteria) this;
        }

        public Criteria andStoreNameEqualTo(String value) {
            addCriterion("StoreName =", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameNotEqualTo(String value) {
            addCriterion("StoreName <>", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameGreaterThan(String value) {
            addCriterion("StoreName >", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameGreaterThanOrEqualTo(String value) {
            addCriterion("StoreName >=", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameLessThan(String value) {
            addCriterion("StoreName <", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameLessThanOrEqualTo(String value) {
            addCriterion("StoreName <=", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameLike(String value) {
            addCriterion("StoreName like", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameNotLike(String value) {
            addCriterion("StoreName not like", value, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameIn(List<String> values) {
            addCriterion("StoreName in", values, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameNotIn(List<String> values) {
            addCriterion("StoreName not in", values, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameBetween(String value1, String value2) {
            addCriterion("StoreName between", value1, value2, "storeName");
            return (Criteria) this;
        }

        public Criteria andStoreNameNotBetween(String value1, String value2) {
            addCriterion("StoreName not between", value1, value2, "storeName");
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

        public Criteria andOperatorIdEqualTo(String value) {
            addCriterion("OperatorId =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(String value) {
            addCriterion("OperatorId <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(String value) {
            addCriterion("OperatorId >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(String value) {
            addCriterion("OperatorId >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(String value) {
            addCriterion("OperatorId <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(String value) {
            addCriterion("OperatorId <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLike(String value) {
            addCriterion("OperatorId like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotLike(String value) {
            addCriterion("OperatorId not like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<String> values) {
            addCriterion("OperatorId in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<String> values) {
            addCriterion("OperatorId not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(String value1, String value2) {
            addCriterion("OperatorId between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(String value1, String value2) {
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

        public Criteria andMemberIdIsNull() {
            addCriterion("MemberId is null");
            return (Criteria) this;
        }

        public Criteria andMemberIdIsNotNull() {
            addCriterion("MemberId is not null");
            return (Criteria) this;
        }

        public Criteria andMemberIdEqualTo(Long value) {
            addCriterion("MemberId =", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotEqualTo(Long value) {
            addCriterion("MemberId <>", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThan(Long value) {
            addCriterion("MemberId >", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThanOrEqualTo(Long value) {
            addCriterion("MemberId >=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThan(Long value) {
            addCriterion("MemberId <", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThanOrEqualTo(Long value) {
            addCriterion("MemberId <=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdIn(List<Long> values) {
            addCriterion("MemberId in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotIn(List<Long> values) {
            addCriterion("MemberId not in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdBetween(Long value1, Long value2) {
            addCriterion("MemberId between", value1, value2, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotBetween(Long value1, Long value2) {
            addCriterion("MemberId not between", value1, value2, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberTelIsNull() {
            addCriterion("MemberTel is null");
            return (Criteria) this;
        }

        public Criteria andMemberTelIsNotNull() {
            addCriterion("MemberTel is not null");
            return (Criteria) this;
        }

        public Criteria andMemberTelEqualTo(String value) {
            addCriterion("MemberTel =", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelNotEqualTo(String value) {
            addCriterion("MemberTel <>", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelGreaterThan(String value) {
            addCriterion("MemberTel >", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelGreaterThanOrEqualTo(String value) {
            addCriterion("MemberTel >=", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelLessThan(String value) {
            addCriterion("MemberTel <", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelLessThanOrEqualTo(String value) {
            addCriterion("MemberTel <=", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelLike(String value) {
            addCriterion("MemberTel like", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelNotLike(String value) {
            addCriterion("MemberTel not like", value, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelIn(List<String> values) {
            addCriterion("MemberTel in", values, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelNotIn(List<String> values) {
            addCriterion("MemberTel not in", values, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelBetween(String value1, String value2) {
            addCriterion("MemberTel between", value1, value2, "memberTel");
            return (Criteria) this;
        }

        public Criteria andMemberTelNotBetween(String value1, String value2) {
            addCriterion("MemberTel not between", value1, value2, "memberTel");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNull() {
            addCriterion("RuleId is null");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNotNull() {
            addCriterion("RuleId is not null");
            return (Criteria) this;
        }

        public Criteria andRuleIdEqualTo(Long value) {
            addCriterion("RuleId =", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotEqualTo(Long value) {
            addCriterion("RuleId <>", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThan(Long value) {
            addCriterion("RuleId >", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("RuleId >=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThan(Long value) {
            addCriterion("RuleId <", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThanOrEqualTo(Long value) {
            addCriterion("RuleId <=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdIn(List<Long> values) {
            addCriterion("RuleId in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotIn(List<Long> values) {
            addCriterion("RuleId not in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdBetween(Long value1, Long value2) {
            addCriterion("RuleId between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotBetween(Long value1, Long value2) {
            addCriterion("RuleId not between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andGivePointsIsNull() {
            addCriterion("GivePoints is null");
            return (Criteria) this;
        }

        public Criteria andGivePointsIsNotNull() {
            addCriterion("GivePoints is not null");
            return (Criteria) this;
        }

        public Criteria andGivePointsEqualTo(Long value) {
            addCriterion("GivePoints =", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsNotEqualTo(Long value) {
            addCriterion("GivePoints <>", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsGreaterThan(Long value) {
            addCriterion("GivePoints >", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsGreaterThanOrEqualTo(Long value) {
            addCriterion("GivePoints >=", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsLessThan(Long value) {
            addCriterion("GivePoints <", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsLessThanOrEqualTo(Long value) {
            addCriterion("GivePoints <=", value, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsIn(List<Long> values) {
            addCriterion("GivePoints in", values, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsNotIn(List<Long> values) {
            addCriterion("GivePoints not in", values, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsBetween(Long value1, Long value2) {
            addCriterion("GivePoints between", value1, value2, "givePoints");
            return (Criteria) this;
        }

        public Criteria andGivePointsNotBetween(Long value1, Long value2) {
            addCriterion("GivePoints not between", value1, value2, "givePoints");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountIsNull() {
            addCriterion("RefundTotalAmount is null");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountIsNotNull() {
            addCriterion("RefundTotalAmount is not null");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountEqualTo(Long value) {
            addCriterion("RefundTotalAmount =", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountNotEqualTo(Long value) {
            addCriterion("RefundTotalAmount <>", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountGreaterThan(Long value) {
            addCriterion("RefundTotalAmount >", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("RefundTotalAmount >=", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountLessThan(Long value) {
            addCriterion("RefundTotalAmount <", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountLessThanOrEqualTo(Long value) {
            addCriterion("RefundTotalAmount <=", value, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountIn(List<Long> values) {
            addCriterion("RefundTotalAmount in", values, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountNotIn(List<Long> values) {
            addCriterion("RefundTotalAmount not in", values, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountBetween(Long value1, Long value2) {
            addCriterion("RefundTotalAmount between", value1, value2, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andRefundTotalAmountNotBetween(Long value1, Long value2) {
            addCriterion("RefundTotalAmount not between", value1, value2, "refundTotalAmount");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeIsNull() {
            addCriterion("TradeSuccTime is null");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeIsNotNull() {
            addCriterion("TradeSuccTime is not null");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeEqualTo(Date value) {
            addCriterion("TradeSuccTime =", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeNotEqualTo(Date value) {
            addCriterion("TradeSuccTime <>", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeGreaterThan(Date value) {
            addCriterion("TradeSuccTime >", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("TradeSuccTime >=", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeLessThan(Date value) {
            addCriterion("TradeSuccTime <", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeLessThanOrEqualTo(Date value) {
            addCriterion("TradeSuccTime <=", value, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeIn(List<Date> values) {
            addCriterion("TradeSuccTime in", values, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeNotIn(List<Date> values) {
            addCriterion("TradeSuccTime not in", values, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeBetween(Date value1, Date value2) {
            addCriterion("TradeSuccTime between", value1, value2, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andTradeSuccTimeNotBetween(Date value1, Date value2) {
            addCriterion("TradeSuccTime not between", value1, value2, "tradeSuccTime");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIsNull() {
            addCriterion("SettTaskStatus is null");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIsNotNull() {
            addCriterion("SettTaskStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusEqualTo(Byte value) {
            addCriterion("SettTaskStatus =", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotEqualTo(Byte value) {
            addCriterion("SettTaskStatus <>", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusGreaterThan(Byte value) {
            addCriterion("SettTaskStatus >", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("SettTaskStatus >=", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusLessThan(Byte value) {
            addCriterion("SettTaskStatus <", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusLessThanOrEqualTo(Byte value) {
            addCriterion("SettTaskStatus <=", value, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusIn(List<Byte> values) {
            addCriterion("SettTaskStatus in", values, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotIn(List<Byte> values) {
            addCriterion("SettTaskStatus not in", values, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusBetween(Byte value1, Byte value2) {
            addCriterion("SettTaskStatus between", value1, value2, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andSettTaskStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("SettTaskStatus not between", value1, value2, "settTaskStatus");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIsNull() {
            addCriterion("ProvinceCode is null");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIsNotNull() {
            addCriterion("ProvinceCode is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeEqualTo(Integer value) {
            addCriterion("ProvinceCode =", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotEqualTo(Integer value) {
            addCriterion("ProvinceCode <>", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeGreaterThan(Integer value) {
            addCriterion("ProvinceCode >", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ProvinceCode >=", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeLessThan(Integer value) {
            addCriterion("ProvinceCode <", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeLessThanOrEqualTo(Integer value) {
            addCriterion("ProvinceCode <=", value, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeIn(List<Integer> values) {
            addCriterion("ProvinceCode in", values, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotIn(List<Integer> values) {
            addCriterion("ProvinceCode not in", values, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeBetween(Integer value1, Integer value2) {
            addCriterion("ProvinceCode between", value1, value2, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andProvinceCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("ProvinceCode not between", value1, value2, "provinceCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeIsNull() {
            addCriterion("CityCode is null");
            return (Criteria) this;
        }

        public Criteria andCityCodeIsNotNull() {
            addCriterion("CityCode is not null");
            return (Criteria) this;
        }

        public Criteria andCityCodeEqualTo(Integer value) {
            addCriterion("CityCode =", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotEqualTo(Integer value) {
            addCriterion("CityCode <>", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThan(Integer value) {
            addCriterion("CityCode >", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("CityCode >=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThan(Integer value) {
            addCriterion("CityCode <", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeLessThanOrEqualTo(Integer value) {
            addCriterion("CityCode <=", value, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeIn(List<Integer> values) {
            addCriterion("CityCode in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotIn(List<Integer> values) {
            addCriterion("CityCode not in", values, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeBetween(Integer value1, Integer value2) {
            addCriterion("CityCode between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andCityCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("CityCode not between", value1, value2, "cityCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIsNull() {
            addCriterion("AreaCode is null");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIsNotNull() {
            addCriterion("AreaCode is not null");
            return (Criteria) this;
        }

        public Criteria andAreaCodeEqualTo(Integer value) {
            addCriterion("AreaCode =", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotEqualTo(Integer value) {
            addCriterion("AreaCode <>", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeGreaterThan(Integer value) {
            addCriterion("AreaCode >", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("AreaCode >=", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeLessThan(Integer value) {
            addCriterion("AreaCode <", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeLessThanOrEqualTo(Integer value) {
            addCriterion("AreaCode <=", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIn(List<Integer> values) {
            addCriterion("AreaCode in", values, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotIn(List<Integer> values) {
            addCriterion("AreaCode not in", values, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeBetween(Integer value1, Integer value2) {
            addCriterion("AreaCode between", value1, value2, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("AreaCode not between", value1, value2, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaInfoIsNull() {
            addCriterion("AreaInfo is null");
            return (Criteria) this;
        }

        public Criteria andAreaInfoIsNotNull() {
            addCriterion("AreaInfo is not null");
            return (Criteria) this;
        }

        public Criteria andAreaInfoEqualTo(String value) {
            addCriterion("AreaInfo =", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoNotEqualTo(String value) {
            addCriterion("AreaInfo <>", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoGreaterThan(String value) {
            addCriterion("AreaInfo >", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoGreaterThanOrEqualTo(String value) {
            addCriterion("AreaInfo >=", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoLessThan(String value) {
            addCriterion("AreaInfo <", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoLessThanOrEqualTo(String value) {
            addCriterion("AreaInfo <=", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoLike(String value) {
            addCriterion("AreaInfo like", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoNotLike(String value) {
            addCriterion("AreaInfo not like", value, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoIn(List<String> values) {
            addCriterion("AreaInfo in", values, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoNotIn(List<String> values) {
            addCriterion("AreaInfo not in", values, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoBetween(String value1, String value2) {
            addCriterion("AreaInfo between", value1, value2, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andAreaInfoNotBetween(String value1, String value2) {
            addCriterion("AreaInfo not between", value1, value2, "areaInfo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoIsNull() {
            addCriterion("IsvDeviceNo is null");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoIsNotNull() {
            addCriterion("IsvDeviceNo is not null");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoEqualTo(String value) {
            addCriterion("IsvDeviceNo =", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoNotEqualTo(String value) {
            addCriterion("IsvDeviceNo <>", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoGreaterThan(String value) {
            addCriterion("IsvDeviceNo >", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoGreaterThanOrEqualTo(String value) {
            addCriterion("IsvDeviceNo >=", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoLessThan(String value) {
            addCriterion("IsvDeviceNo <", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoLessThanOrEqualTo(String value) {
            addCriterion("IsvDeviceNo <=", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoLike(String value) {
            addCriterion("IsvDeviceNo like", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoNotLike(String value) {
            addCriterion("IsvDeviceNo not like", value, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoIn(List<String> values) {
            addCriterion("IsvDeviceNo in", values, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoNotIn(List<String> values) {
            addCriterion("IsvDeviceNo not in", values, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoBetween(String value1, String value2) {
            addCriterion("IsvDeviceNo between", value1, value2, "isvDeviceNo");
            return (Criteria) this;
        }

        public Criteria andIsvDeviceNoNotBetween(String value1, String value2) {
            addCriterion("IsvDeviceNo not between", value1, value2, "isvDeviceNo");
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

        public Criteria andPostTypeIsNull() {
            addCriterion("PostType is null");
            return (Criteria) this;
        }

        public Criteria andPostTypeIsNotNull() {
            addCriterion("PostType is not null");
            return (Criteria) this;
        }

        public Criteria andPostTypeEqualTo(Byte value) {
            addCriterion("PostType =", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotEqualTo(Byte value) {
            addCriterion("PostType <>", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeGreaterThan(Byte value) {
            addCriterion("PostType >", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("PostType >=", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeLessThan(Byte value) {
            addCriterion("PostType <", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeLessThanOrEqualTo(Byte value) {
            addCriterion("PostType <=", value, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeIn(List<Byte> values) {
            addCriterion("PostType in", values, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotIn(List<Byte> values) {
            addCriterion("PostType not in", values, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeBetween(Byte value1, Byte value2) {
            addCriterion("PostType between", value1, value2, "postType");
            return (Criteria) this;
        }

        public Criteria andPostTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("PostType not between", value1, value2, "postType");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdIsNull() {
            addCriterion("StoreAreaId is null");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdIsNotNull() {
            addCriterion("StoreAreaId is not null");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdEqualTo(Long value) {
            addCriterion("StoreAreaId =", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdNotEqualTo(Long value) {
            addCriterion("StoreAreaId <>", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdGreaterThan(Long value) {
            addCriterion("StoreAreaId >", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdGreaterThanOrEqualTo(Long value) {
            addCriterion("StoreAreaId >=", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdLessThan(Long value) {
            addCriterion("StoreAreaId <", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdLessThanOrEqualTo(Long value) {
            addCriterion("StoreAreaId <=", value, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdIn(List<Long> values) {
            addCriterion("StoreAreaId in", values, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdNotIn(List<Long> values) {
            addCriterion("StoreAreaId not in", values, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdBetween(Long value1, Long value2) {
            addCriterion("StoreAreaId between", value1, value2, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andStoreAreaIdNotBetween(Long value1, Long value2) {
            addCriterion("StoreAreaId not between", value1, value2, "storeAreaId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNull() {
            addCriterion("AddressId is null");
            return (Criteria) this;
        }

        public Criteria andAddressIdIsNotNull() {
            addCriterion("AddressId is not null");
            return (Criteria) this;
        }

        public Criteria andAddressIdEqualTo(Long value) {
            addCriterion("AddressId =", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotEqualTo(Long value) {
            addCriterion("AddressId <>", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThan(Long value) {
            addCriterion("AddressId >", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdGreaterThanOrEqualTo(Long value) {
            addCriterion("AddressId >=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThan(Long value) {
            addCriterion("AddressId <", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdLessThanOrEqualTo(Long value) {
            addCriterion("AddressId <=", value, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdIn(List<Long> values) {
            addCriterion("AddressId in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotIn(List<Long> values) {
            addCriterion("AddressId not in", values, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdBetween(Long value1, Long value2) {
            addCriterion("AddressId between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andAddressIdNotBetween(Long value1, Long value2) {
            addCriterion("AddressId not between", value1, value2, "addressId");
            return (Criteria) this;
        }

        public Criteria andPostIdIsNull() {
            addCriterion("PostId is null");
            return (Criteria) this;
        }

        public Criteria andPostIdIsNotNull() {
            addCriterion("PostId is not null");
            return (Criteria) this;
        }

        public Criteria andPostIdEqualTo(Long value) {
            addCriterion("PostId =", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotEqualTo(Long value) {
            addCriterion("PostId <>", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdGreaterThan(Long value) {
            addCriterion("PostId >", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdGreaterThanOrEqualTo(Long value) {
            addCriterion("PostId >=", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdLessThan(Long value) {
            addCriterion("PostId <", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdLessThanOrEqualTo(Long value) {
            addCriterion("PostId <=", value, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdIn(List<Long> values) {
            addCriterion("PostId in", values, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotIn(List<Long> values) {
            addCriterion("PostId not in", values, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdBetween(Long value1, Long value2) {
            addCriterion("PostId between", value1, value2, "postId");
            return (Criteria) this;
        }

        public Criteria andPostIdNotBetween(Long value1, Long value2) {
            addCriterion("PostId not between", value1, value2, "postId");
            return (Criteria) this;
        }

        public Criteria andTransportNoIsNull() {
            addCriterion("TransportNo is null");
            return (Criteria) this;
        }

        public Criteria andTransportNoIsNotNull() {
            addCriterion("TransportNo is not null");
            return (Criteria) this;
        }

        public Criteria andTransportNoEqualTo(String value) {
            addCriterion("TransportNo =", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoNotEqualTo(String value) {
            addCriterion("TransportNo <>", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoGreaterThan(String value) {
            addCriterion("TransportNo >", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoGreaterThanOrEqualTo(String value) {
            addCriterion("TransportNo >=", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoLessThan(String value) {
            addCriterion("TransportNo <", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoLessThanOrEqualTo(String value) {
            addCriterion("TransportNo <=", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoLike(String value) {
            addCriterion("TransportNo like", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoNotLike(String value) {
            addCriterion("TransportNo not like", value, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoIn(List<String> values) {
            addCriterion("TransportNo in", values, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoNotIn(List<String> values) {
            addCriterion("TransportNo not in", values, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoBetween(String value1, String value2) {
            addCriterion("TransportNo between", value1, value2, "transportNo");
            return (Criteria) this;
        }

        public Criteria andTransportNoNotBetween(String value1, String value2) {
            addCriterion("TransportNo not between", value1, value2, "transportNo");
            return (Criteria) this;
        }

        public Criteria andReceiveNameIsNull() {
            addCriterion("ReceiveName is null");
            return (Criteria) this;
        }

        public Criteria andReceiveNameIsNotNull() {
            addCriterion("ReceiveName is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveNameEqualTo(String value) {
            addCriterion("ReceiveName =", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameNotEqualTo(String value) {
            addCriterion("ReceiveName <>", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameGreaterThan(String value) {
            addCriterion("ReceiveName >", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameGreaterThanOrEqualTo(String value) {
            addCriterion("ReceiveName >=", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameLessThan(String value) {
            addCriterion("ReceiveName <", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameLessThanOrEqualTo(String value) {
            addCriterion("ReceiveName <=", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameLike(String value) {
            addCriterion("ReceiveName like", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameNotLike(String value) {
            addCriterion("ReceiveName not like", value, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameIn(List<String> values) {
            addCriterion("ReceiveName in", values, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameNotIn(List<String> values) {
            addCriterion("ReceiveName not in", values, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameBetween(String value1, String value2) {
            addCriterion("ReceiveName between", value1, value2, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveNameNotBetween(String value1, String value2) {
            addCriterion("ReceiveName not between", value1, value2, "receiveName");
            return (Criteria) this;
        }

        public Criteria andReceiveTelIsNull() {
            addCriterion("ReceiveTel is null");
            return (Criteria) this;
        }

        public Criteria andReceiveTelIsNotNull() {
            addCriterion("ReceiveTel is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveTelEqualTo(String value) {
            addCriterion("ReceiveTel =", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelNotEqualTo(String value) {
            addCriterion("ReceiveTel <>", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelGreaterThan(String value) {
            addCriterion("ReceiveTel >", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelGreaterThanOrEqualTo(String value) {
            addCriterion("ReceiveTel >=", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelLessThan(String value) {
            addCriterion("ReceiveTel <", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelLessThanOrEqualTo(String value) {
            addCriterion("ReceiveTel <=", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelLike(String value) {
            addCriterion("ReceiveTel like", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelNotLike(String value) {
            addCriterion("ReceiveTel not like", value, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelIn(List<String> values) {
            addCriterion("ReceiveTel in", values, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelNotIn(List<String> values) {
            addCriterion("ReceiveTel not in", values, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelBetween(String value1, String value2) {
            addCriterion("ReceiveTel between", value1, value2, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andReceiveTelNotBetween(String value1, String value2) {
            addCriterion("ReceiveTel not between", value1, value2, "receiveTel");
            return (Criteria) this;
        }

        public Criteria andPostStatusIsNull() {
            addCriterion("PostStatus is null");
            return (Criteria) this;
        }

        public Criteria andPostStatusIsNotNull() {
            addCriterion("PostStatus is not null");
            return (Criteria) this;
        }

        public Criteria andPostStatusEqualTo(Byte value) {
            addCriterion("PostStatus =", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusNotEqualTo(Byte value) {
            addCriterion("PostStatus <>", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusGreaterThan(Byte value) {
            addCriterion("PostStatus >", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("PostStatus >=", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusLessThan(Byte value) {
            addCriterion("PostStatus <", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusLessThanOrEqualTo(Byte value) {
            addCriterion("PostStatus <=", value, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusIn(List<Byte> values) {
            addCriterion("PostStatus in", values, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusNotIn(List<Byte> values) {
            addCriterion("PostStatus not in", values, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusBetween(Byte value1, Byte value2) {
            addCriterion("PostStatus between", value1, value2, "postStatus");
            return (Criteria) this;
        }

        public Criteria andPostStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("PostStatus not between", value1, value2, "postStatus");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdIsNull() {
            addCriterion("RrturnReasonId is null");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdIsNotNull() {
            addCriterion("RrturnReasonId is not null");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdEqualTo(Long value) {
            addCriterion("RrturnReasonId =", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdNotEqualTo(Long value) {
            addCriterion("RrturnReasonId <>", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdGreaterThan(Long value) {
            addCriterion("RrturnReasonId >", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdGreaterThanOrEqualTo(Long value) {
            addCriterion("RrturnReasonId >=", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdLessThan(Long value) {
            addCriterion("RrturnReasonId <", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdLessThanOrEqualTo(Long value) {
            addCriterion("RrturnReasonId <=", value, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdIn(List<Long> values) {
            addCriterion("RrturnReasonId in", values, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdNotIn(List<Long> values) {
            addCriterion("RrturnReasonId not in", values, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdBetween(Long value1, Long value2) {
            addCriterion("RrturnReasonId between", value1, value2, "rrturnReasonId");
            return (Criteria) this;
        }

        public Criteria andRrturnReasonIdNotBetween(Long value1, Long value2) {
            addCriterion("RrturnReasonId not between", value1, value2, "rrturnReasonId");
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