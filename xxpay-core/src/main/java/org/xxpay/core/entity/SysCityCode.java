package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 地区编码表（市级别）
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@TableName("t_sys_city_code")
public class SysCityCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 市编码
     */
    @TableId("CityCode")
    private Integer cityCode;

    /**
     * 市名称
     */
    @TableField("CityName")
    private String cityName;

    /**
     * 省编码
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "SysCityCode{" +
            "CityCode=" + cityCode +
            ", cityName=" + cityName +
            ", provinceCode=" + provinceCode +
        "}";
    }
}
