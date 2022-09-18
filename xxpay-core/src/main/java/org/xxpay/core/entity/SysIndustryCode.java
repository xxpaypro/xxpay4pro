package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 行业编码表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@TableName("t_sys_industry_code")
public class SysIndustryCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 行业编码
     */
    @TableId("IndustryCode")
    private Integer industryCode;

    /**
     * 行业名称
     */
    @TableField("IndustryName")
    private String industryName;

    /**
     * 行业描述
     */
    @TableField("IndustryDesc")
    private String industryDesc;

    /**
     * 上级编码Code, 等级编码父ID为0
     */
    @TableField("ParentCode")
    private Integer parentCode;

    public Integer getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(Integer industryCode) {
        this.industryCode = industryCode;
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
    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public String toString() {
        return "SysIndustryCode{" +
            "IndustryCode=" + industryCode +
            ", industryName=" + industryName +
            ", industryDesc=" + industryDesc +
            ", parentCode=" + parentCode +
        "}";
    }
}
