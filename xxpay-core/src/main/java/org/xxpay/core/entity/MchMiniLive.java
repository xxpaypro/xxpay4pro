package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 小程序直播表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-26
 */
@TableName("t_mch_mini_live")
public class MchMiniLive extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情ID
     */
    @TableId("RoomId")
    private Long roomId;

    /**
     * 直播间名字
     */
    @TableField("Name")
    private String name;

    /**
     * 直播间背景图mediaID
     */
    @TableField("CoverImg")
    private String coverImg;

    /**
     * 直播计划开始时间，时间戳
     */
    @TableField("StartTime")
    private String startTime;

    /**
     * 直播计划结束时间，时间戳
     */
    @TableField("EndTime")
    private String endTime;

    /**
     * 主播昵称，最短2个汉字，最长15个汉字
     */
    @TableField("AnchorName")
    private String anchorName;

    /**
     * 主播微信号，需要通过“小程序直播”小程序的实名验证
     */
    @TableField("AnchorWechat")
    private String anchorWechat;

    /**
     * 直播间分享图mediaID，图片规则：建议像素1080*1920，大小不超过2M
     */
    @TableField("AnchorImg")
    private String anchorImg;

    /**
     * 直播间类型，1: 推流，0：手机直播
     */
    @TableField("Type")
    private Byte type;

    /**
     * 横屏、竖屏，1：横屏，0：竖屏
     */
    @TableField("ScreenType")
    private Byte screenType;

    /**
     * 是否关闭点赞，0：开启，1：关闭
     */
    @TableField("CloseLike")
    private Byte closeLike;

    /**
     * 是否关闭货架，0：开启，1：关闭
     */
    @TableField("CloseGoods")
    private Byte closeGoods;

    /**
     * 是否关闭评论，0：开启，1：关闭
     */
    @TableField("CloseComment")
    private Byte closeComment;

    /**
     * 所属小程序：1-点餐 2-商城
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }
    public String getAnchorWechat() {
        return anchorWechat;
    }

    public void setAnchorWechat(String anchorWechat) {
        this.anchorWechat = anchorWechat;
    }
    public String getAnchorImg() {
        return anchorImg;
    }

    public void setAnchorImg(String anchorImg) {
        this.anchorImg = anchorImg;
    }
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
    public Byte getScreenType() {
        return screenType;
    }

    public void setScreenType(Byte screenType) {
        this.screenType = screenType;
    }
    public Byte getCloseLike() {
        return closeLike;
    }

    public void setCloseLike(Byte closeLike) {
        this.closeLike = closeLike;
    }
    public Byte getCloseGoods() {
        return closeGoods;
    }

    public void setCloseGoods(Byte closeGoods) {
        this.closeGoods = closeGoods;
    }
    public Byte getCloseComment() {
        return closeComment;
    }

    public void setCloseComment(Byte closeComment) {
        this.closeComment = closeComment;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
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
        return "MchMiniLive{" +
            "RoomId=" + roomId +
            ", name=" + name +
            ", coverImg=" + coverImg +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", anchorName=" + anchorName +
            ", anchorWechat=" + anchorWechat +
            ", anchorImg=" + anchorImg +
            ", type=" + type +
            ", screenType=" + screenType +
            ", closeLike=" + closeLike +
            ", closeGoods=" + closeGoods +
            ", closeComment=" + closeComment +
            ", authFrom=" + authFrom +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
