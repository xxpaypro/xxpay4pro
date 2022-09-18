package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 支付宝类目编码表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2020-01-18
 */
@TableName("t_sys_alipay_industry_code")
public class SysAlipayIndustryCode extends BaseModel {

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
     * 行业描述
     */
    @TableField("IndustryDesc")
    private String industryDesc;

    /**
     * 上级编码Code, 等级编码父ID为0
     */
    @TableField("ParentCode")
    private Integer parentCode;

    /**
     * 经营类目编码
     */
    @TableField("MccCode")
    private String mccCode;

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
    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    public String getMccCode() {
        return mccCode;
    }

    public void setMccCode(String mccCode) {
        this.mccCode = mccCode;
    }

    @Override
    public String toString() {
        return "SysAlipayIndustryCode{" +
            "Id=" + id +
            ", industryName=" + industryName +
            ", industryDesc=" + industryDesc +
            ", parentCode=" + parentCode +
            ", mccCode=" + mccCode +
        "}";
    }
}
