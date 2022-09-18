package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户交易表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-19
 */
@TableName("t_mch_trade_order")
public class MchTradeOrder extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 交易单号
     */
    @TableId("TradeOrderId")
    private String tradeOrderId;

    /**
     * 交易类型:1-收款,2-充值
     */
    @TableField("TradeType")
    private Byte tradeType;

    /**
     * 是否押金模式:0-否,1-是
     */
    @TableField("DepositMode")
    private Byte depositMode;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 代理商ID
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 应用ID
     */
    @TableField("AppId")
    private String appId;

    /**
     * 客户端IP
     */
    @TableField("ClientIp")
    private String clientIp;

    /**
     * 设备
     */
    @TableField("Device")
    private String device;

    /**
     * 商品ID
     */
    @TableField("GoodsId")
    private String goodsId;

    /**
     * 商品标题
     */
    @TableField("Subject")
    private String subject;

    /**
     * 商品描述信息
     */
    @TableField("Body")
    private String body;

    /**
     * 订单金额,单位分
     */
    @TableField("OrderAmount")
    private Long orderAmount;

    /**
     * 优惠金额,单位分
     */
    @TableField("DiscountAmount")
    private Long discountAmount;

    /**
     * 实际支付金额,单位分
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 押金金额,单位分
     */
    @TableField("DepositAmount")
    private Long depositAmount;

    /**
     * 商户入账,单位分
     */
    @TableField("MchIncome")
    private Long mchIncome;

    /**
     * 用户ID
     */
    @TableField("UserId")
    private String userId;

    /**
     * 支付渠道用户ID(微信openID或支付宝账号等第三方支付账号)
     */
    @TableField("ChannelUserId")
    private String channelUserId;

    /**
     * 订单状态,生成(0),处理中(1),支付成功(2),失败(-1),部分退款(4),全额退款(5),押金未结算(6),押金退还(7)
     */
    @TableField("Status")
    private Byte status;

    /**
     * 支付订单号
     */
    @TableField("PayOrderId")
    private String payOrderId;

    /**
     * 产品ID
     */
    @TableField("ProductId")
    private Integer productId;

    /**
     * 支付产品类型: 1-现金收款, 2-会员卡支付, 3-微信支付, 4-支付宝支付
     */
    @TableField("ProductType")
    private Byte productType;

    /**
     * 商户优惠券ID
     */
    @TableField("MchCouponId")
    private Long mchCouponId;

    /**
     * 会员优惠券核销码
     */
    @TableField("MemberCouponNo")
    private String memberCouponNo;

    /**
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 门店编号
     */
    @TableField("StoreNo")
    private String storeNo;

    /**
     * 门店名称
     */
    @TableField("StoreName")
    private String storeName;

    /**
     * 操作员ID
     */
    @TableField("OperatorId")
    private String operatorId;

    /**
     * 操作员名称
     */
    @TableField("OperatorName")
    private String operatorName;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 会员手机号
     */
    @TableField("MemberTel")
    private String memberTel;

    /**
     * 充值赠送规则ID
     */
    @TableField("RuleId")
    private Long ruleId;

    /**
     * 消费赠送积分
     */
    @TableField("GivePoints")
    private Long givePoints;

    /**
     * 退款总金额
     */
    @TableField("RefundTotalAmount")
    private Long refundTotalAmount;

    /**
     * 交易成功时间
     */
    @TableField("TradeSuccTime")
    private Date tradeSuccTime;

    /**
     * 结算任务状态: -1无需执行结算任务, 0-待执行结算任务, 1-已完成结算任务
     */
    @TableField("SettTaskStatus")
    private Byte settTaskStatus;

    /**
     * 硬件设备编号
     */
    @TableField("IsvDeviceNo")
    private String isvDeviceNo;

    /**
     * 所属行业 1-餐饮 2-电商
     */
    @TableField("IndustryType")
    private Byte industryType;

    /**
     * 1-堂食 2-外卖
     */
    @TableField("PostType")
    private Byte postType;

    /**
     * 餐桌号ID
     */
    @TableField("StoreAreaId")
    private Long storeAreaId;

    /**
     * 区域名
     */
    @TableField("AreaName")
    private String areaName;

    /**
     * 预约时间
     */
    @TableField("AppointmentTime")
    private Date appointmentTime;

    /**
     * 收货地址ID
     */
    @TableField("AddressId")
    private Long addressId;

    /**
     * 行政地区编号，省
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    /**
     * 行政地区编号， 市
     */
    @TableField("CityCode")
    private Integer cityCode;

    /**
     * 行政地区编号， 县
     */
    @TableField("AreaCode")
    private Integer areaCode;

    /**
     * 省市县名称描述
     */
    @TableField("AreaInfo")
    private String areaInfo;

    /**
     * 详细地址
     */
    @TableField("Address")
    private String address;

    /**
     * 收货人手机号
     */
    @TableField("ReceiveTel")
    private String receiveTel;

    /**
     * 收货人姓名
     */
    @TableField("ReceiveName")
    private String receiveName;

    /**
     * 发货快递ID
     */
    @TableField("PostId")
    private Long postId;

    /**
     * 运单号
     */
    @TableField("TransportNo")
    private String transportNo;

    /**
     * 发货状态：0-待发货 1-已发货 2-确认收货 3-评价完成 4-退款审核 5-已退款 6-退款拒绝 7-退货审核 8-已退货 9-退货拒绝
     */
    @TableField("PostStatus")
    private Byte postStatus;

    /**
     * 订单评价
     */
    @TableField("Evaluation")
    private String evaluation;

    /**
     * 订单评价图片
     */
    @TableField("EvaluationImage")
    private String evaluationImage;

    /**
     * 会员端订单删除标识：0-未删除 1-删除
     */
    @TableField("DeleteFlag")
    private Byte deleteFlag;

    /**
     * 退货原因ID
     */
    @TableField("ReturnReasonId")
    private Long returnReasonId;

    /**
     * 退货详情
     */
    @TableField("ReturnDesc")
    private String returnDesc;

    /**
     * 商品信息，商品ID、对应数量和属性
     */
    @TableField("GoodsDesc")
    private String goodsDesc;

    /**
     * 主图图片路径
     */
    @TableField("ImgPathMain")
    private String imgPathMain;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

    /**
     * 运费
     */
    @TableField("PostPrice")
    private Long postPrice;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UpdateTime")
    private Date updateTime;

    private String channelOrderNo;

    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }
    public Byte getTradeType() {
        return tradeType;
    }

    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }
    public Byte getDepositMode() {
        return depositMode;
    }

    public void setDepositMode(Byte depositMode) {
        this.depositMode = depositMode;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }
    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public Long getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Long depositAmount) {
        this.depositAmount = depositAmount;
    }
    public Long getMchIncome() {
        return mchIncome;
    }

    public void setMchIncome(Long mchIncome) {
        this.mchIncome = mchIncome;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }
    public Long getMchCouponId() {
        return mchCouponId;
    }

    public void setMchCouponId(Long mchCouponId) {
        this.mchCouponId = mchCouponId;
    }
    public String getMemberCouponNo() {
        return memberCouponNo;
    }

    public void setMemberCouponNo(String memberCouponNo) {
        this.memberCouponNo = memberCouponNo;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    public Long getGivePoints() {
        return givePoints;
    }

    public void setGivePoints(Long givePoints) {
        this.givePoints = givePoints;
    }
    public Long getRefundTotalAmount() {
        return refundTotalAmount;
    }

    public void setRefundTotalAmount(Long refundTotalAmount) {
        this.refundTotalAmount = refundTotalAmount;
    }
    public Date getTradeSuccTime() {
        return tradeSuccTime;
    }

    public void setTradeSuccTime(Date tradeSuccTime) {
        this.tradeSuccTime = tradeSuccTime;
    }
    public Byte getSettTaskStatus() {
        return settTaskStatus;
    }

    public void setSettTaskStatus(Byte settTaskStatus) {
        this.settTaskStatus = settTaskStatus;
    }
    public String getIsvDeviceNo() {
        return isvDeviceNo;
    }

    public void setIsvDeviceNo(String isvDeviceNo) {
        this.isvDeviceNo = isvDeviceNo;
    }
    public Byte getIndustryType() {
        return industryType;
    }

    public void setIndustryType(Byte industryType) {
        this.industryType = industryType;
    }
    public Byte getPostType() {
        return postType;
    }

    public void setPostType(Byte postType) {
        this.postType = postType;
    }
    public Long getStoreAreaId() {
        return storeAreaId;
    }

    public void setStoreAreaId(Long storeAreaId) {
        this.storeAreaId = storeAreaId;
    }
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }
    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getReceiveTel() {
        return receiveTel;
    }

    public void setReceiveTel(String receiveTel) {
        this.receiveTel = receiveTel;
    }
    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }
    public Byte getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(Byte postStatus) {
        this.postStatus = postStatus;
    }
    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
    public String getEvaluationImage() {
        return evaluationImage;
    }

    public void setEvaluationImage(String evaluationImage) {
        this.evaluationImage = evaluationImage;
    }
    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public Long getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(Long returnReasonId) {
        this.returnReasonId = returnReasonId;
    }
    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }
    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }
    public String getImgPathMain() {
        return imgPathMain;
    }

    public void setImgPathMain(String imgPathMain) {
        this.imgPathMain = imgPathMain;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(Long postPrice) {
        this.postPrice = postPrice;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    @Override
    public String toString() {
        return "MchTradeOrder{" +
                "TradeOrderId=" + tradeOrderId +
                ", tradeType=" + tradeType +
                ", depositMode=" + depositMode +
                ", mchId=" + mchId +
                ", agentId=" + agentId +
                ", isvId=" + isvId +
                ", appId=" + appId +
                ", clientIp=" + clientIp +
                ", device=" + device +
                ", goodsId=" + goodsId +
                ", subject=" + subject +
                ", body=" + body +
                ", orderAmount=" + orderAmount +
                ", discountAmount=" + discountAmount +
                ", amount=" + amount +
                ", depositAmount=" + depositAmount +
                ", mchIncome=" + mchIncome +
                ", userId=" + userId +
                ", channelUserId=" + channelUserId +
                ", status=" + status +
                ", payOrderId=" + payOrderId +
                ", productId=" + productId +
                ", productType=" + productType +
                ", mchCouponId=" + mchCouponId +
                ", memberCouponNo=" + memberCouponNo +
                ", storeId=" + storeId +
                ", storeNo=" + storeNo +
                ", storeName=" + storeName +
                ", operatorId=" + operatorId +
                ", operatorName=" + operatorName +
                ", memberId=" + memberId +
                ", memberTel=" + memberTel +
                ", ruleId=" + ruleId +
                ", givePoints=" + givePoints +
                ", refundTotalAmount=" + refundTotalAmount +
                ", tradeSuccTime=" + tradeSuccTime +
                ", settTaskStatus=" + settTaskStatus +
                ", isvDeviceNo=" + isvDeviceNo +
                ", industryType=" + industryType +
                ", postType=" + postType +
                ", storeAreaId=" + storeAreaId +
                ", areaName=" + areaName +
                ", appointmentTime=" + appointmentTime +
                ", addressId=" + addressId +
                ", provinceCode=" + provinceCode +
                ", cityCode=" + cityCode +
                ", areaCode=" + areaCode +
                ", areaInfo=" + areaInfo +
                ", address=" + address +
                ", receiveTel=" + receiveTel +
                ", receiveName=" + receiveName +
                ", postId=" + postId +
                ", transportNo=" + transportNo +
                ", postStatus=" + postStatus +
                ", evaluation=" + evaluation +
                ", evaluationImage=" + evaluationImage +
                ", deleteFlag=" + deleteFlag +
                ", returnReasonId=" + returnReasonId +
                ", returnDesc=" + returnDesc +
                ", goodsDesc=" + goodsDesc +
                ", imgPathMain=" + imgPathMain +
                ", remark=" + remark +
                ", postPrice=" + postPrice +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
