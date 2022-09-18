package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 地区编码表（区/县 级别）
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@TableName("t_sys_area_code")
public class SysAreaCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 地区编码
     */
    @TableId("AreaCode")
    private Integer areaCode;

    /**
     * 地区名称
     */
    @TableField("AreaName")
    private String areaName;

    /**
     * 省编码
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    /**
     * 市编码
     */
    @TableField("CityCode")
    private Integer cityCode;

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    @Override
    public String toString() {
        return "SysAreaCode{" +
            "AreaCode=" + areaCode +
            ", areaName=" + areaName +
            ", provinceCode=" + provinceCode +
            ", cityCode=" + cityCode +
        "}";
    }
}
