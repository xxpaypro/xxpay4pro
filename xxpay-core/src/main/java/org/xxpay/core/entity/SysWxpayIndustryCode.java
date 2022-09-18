package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 微信行业费率表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-02-25
 */
@TableName("t_sys_wxpay_industry_code")
public class SysWxpayIndustryCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("Id")
    private Integer id;

    /**
     * 行业名称
     */
    @TableField("IndustryName")
    private String industryName;

    /**
     * 特殊资质要求
     */
    @TableField("IndustryDesc")
    private String industryDesc;

    /**
     * 费率
     */
    @TableField("Rate")
    private String rate;

    /**
     * 结算规则
     */
    @TableField("SettRule")
    private String settRule;

    /**
     * 上级规则ID
     */
    @TableField("ParentCode")
    private Long parentCode;

    /**
     * 结算规则ID
     */
    @TableField("CategoryCode")
    private String categoryCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
    public String getIndustryDesc() {
        return industryDesc;
    }

    public void setIndustryDesc(String industryDesc) {
        this.industryDesc = industryDesc;
    }
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getSettRule() {
        return settRule;
    }

    public void setSettRule(String settRule) {
        this.settRule = settRule;
    }
    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "SysWxpayIndustryCode{" +
            "Id=" + id +
            ", industryName=" + industryName +
            ", industryDesc=" + industryDesc +
            ", rate=" + rate +
            ", settRule=" + settRule +
            ", parentCode=" + parentCode +
            ", categoryCode=" + categoryCode +
        "}";
    }
}
