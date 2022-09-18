package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 地区编码表（省级别）
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@TableName("t_sys_province_code")
public class SysProvinceCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 省编码
     */
    @TableId("ProvinceCode")
    private Integer provinceCode;

    /**
     * 省名称
     */
    @TableField("provinceName")
    private String provinceName;

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "SysProvinceCode{" +
            "ProvinceCode=" + provinceCode +
            ", provinceName=" + provinceName +
        "}";
    }
}
