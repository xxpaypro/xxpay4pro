package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商结算跑批任务表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-24
 */
@TableName("t_order_profit_task")
public class OrderProfitTask extends BaseModel<OrderProfitTask> {

    private static final long serialVersionUID = 1L;

    /**
     * BatchId, 跑批任务ID
     */
    @TableId(value =" TaskId", type = IdType.AUTO)
    private Long taskId;

    /**
     * isvId
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 结算代理商ID, null为服务商本身分佣统计
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 结算代理商父ID, null为服务商本身分佣统计
     */
    @TableField("AgentPid")
    private Long agentPid;

    /**
     * 代理商名称
     */
    @TableField("AgentName")
    private String agentName;

    /**
     * 交易最早时间
     */
    @TableField("TradeFirstDate")
    private Date tradeFirstDate;

    /**
     * 交易最晚时间
     */
    @TableField("TradeLastDate")
    private Date tradeLastDate;

    /**
     * 服务商结算配置快照
     */
    @TableField("SettConfigSnapshot")
    private String settConfigSnapshot;

    /**
     * 全部交易总笔数
     */
    @TableField("AllTradeCount")
    private Long allTradeCount;

    /**
     * 全部交易总金额,单位分
     */
    @TableField("AllTradeAmount")
    private Long allTradeAmount;

    /**
     * 全部交易总返佣金额,单位分
     */
    @TableField("AllTradeProfitAmount")
    private Long allTradeProfitAmount;

    /**
     * 微信交易总笔数
     */
    @TableField("WxTradeCount")
    private Long wxTradeCount;

    /**
     * 微信交易总金额,单位分
     */
    @TableField("WxTradeAmount")
    private Long wxTradeAmount;

    /**
     * 微信交易总返佣金额,单位分
     */
    @TableField("WxTradeProfitAmount")
    private Long wxTradeProfitAmount;

    /**
     * 支付宝交易总笔数
     */
    @TableField("AlipayTradeCount")
    private Long alipayTradeCount;

    /**
     * 支付宝交易总金额,单位分
     */
    @TableField("AlipayTradeAmount")
    private Long alipayTradeAmount;

    /**
     * 支付宝交易总返佣金额,单位分
     */
    @TableField("AlipayTradeProfitAmount")
    private Long alipayTradeProfitAmount;

    /**
     * 结算状态 -1 无需结算 0-待结算 1-已结算
     */
    @TableField("SettStatus")
    private Byte settStatus;

    /**
     * 结算时间
     */
    @TableField("SettDate")
    private Date settDate;

    /**
     * 结算支付凭证图片
     */
    @TableField("SettImg")
    private String settImg;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Long getAgentPid() {
        return agentPid;
    }

    public void setAgentPid(Long agentPid) {
        this.agentPid = agentPid;
    }
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    public Date getTradeFirstDate() {
        return tradeFirstDate;
    }

    public void setTradeFirstDate(Date tradeFirstDate) {
        this.tradeFirstDate = tradeFirstDate;
    }
    public Date getTradeLastDate() {
        return tradeLastDate;
    }

    public void setTradeLastDate(Date tradeLastDate) {
        this.tradeLastDate = tradeLastDate;
    }
    public String getSettConfigSnapshot() {
        return settConfigSnapshot;
    }

    public void setSettConfigSnapshot(String settConfigSnapshot) {
        this.settConfigSnapshot = settConfigSnapshot;
    }
    public Long getAllTradeCount() {
        return allTradeCount;
    }

    public void setAllTradeCount(Long allTradeCount) {
        this.allTradeCount = allTradeCount;
    }
    public Long getAllTradeAmount() {
        return allTradeAmount;
    }

    public void setAllTradeAmount(Long allTradeAmount) {
        this.allTradeAmount = allTradeAmount;
    }
    public Long getAllTradeProfitAmount() {
        return allTradeProfitAmount;
    }

    public void setAllTradeProfitAmount(Long allTradeProfitAmount) {
        this.allTradeProfitAmount = allTradeProfitAmount;
    }
    public Long getWxTradeCount() {
        return wxTradeCount;
    }

    public void setWxTradeCount(Long wxTradeCount) {
        this.wxTradeCount = wxTradeCount;
    }
    public Long getWxTradeAmount() {
        return wxTradeAmount;
    }

    public void setWxTradeAmount(Long wxTradeAmount) {
        this.wxTradeAmount = wxTradeAmount;
    }
    public Long getWxTradeProfitAmount() {
        return wxTradeProfitAmount;
    }

    public void setWxTradeProfitAmount(Long wxTradeProfitAmount) {
        this.wxTradeProfitAmount = wxTradeProfitAmount;
    }
    public Long getAlipayTradeCount() {
        return alipayTradeCount;
    }

    public void setAlipayTradeCount(Long alipayTradeCount) {
        this.alipayTradeCount = alipayTradeCount;
    }
    public Long getAlipayTradeAmount() {
        return alipayTradeAmount;
    }

    public void setAlipayTradeAmount(Long alipayTradeAmount) {
        this.alipayTradeAmount = alipayTradeAmount;
    }
    public Long getAlipayTradeProfitAmount() {
        return alipayTradeProfitAmount;
    }

    public void setAlipayTradeProfitAmount(Long alipayTradeProfitAmount) {
        this.alipayTradeProfitAmount = alipayTradeProfitAmount;
    }
    public Byte getSettStatus() {
        return settStatus;
    }

    public void setSettStatus(Byte settStatus) {
        this.settStatus = settStatus;
    }
    public Date getSettDate() {
        return settDate;
    }

    public void setSettDate(Date settDate) {
        this.settDate = settDate;
    }
    public String getSettImg() {
        return settImg;
    }

    public void setSettImg(String settImg) {
        this.settImg = settImg;
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

    @Override
    public String toString() {
        return "OrderProfitTask{" +
                "TaskId=" + taskId +
                ", isvId=" + isvId +
                ", agentId=" + agentId +
                ", agentPid=" + agentPid +
                ", agentName=" + agentName +
                ", tradeFirstDate=" + tradeFirstDate +
                ", tradeLastDate=" + tradeLastDate +
                ", settConfigSnapshot=" + settConfigSnapshot +
                ", allTradeCount=" + allTradeCount +
                ", allTradeAmount=" + allTradeAmount +
                ", allTradeProfitAmount=" + allTradeProfitAmount +
                ", wxTradeCount=" + wxTradeCount +
                ", wxTradeAmount=" + wxTradeAmount +
                ", wxTradeProfitAmount=" + wxTradeProfitAmount +
                ", alipayTradeCount=" + alipayTradeCount +
                ", alipayTradeAmount=" + alipayTradeAmount +
                ", alipayTradeProfitAmount=" + alipayTradeProfitAmount +
                ", settStatus=" + settStatus +
                ", settDate=" + settDate +
                ", settImg=" + settImg +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
