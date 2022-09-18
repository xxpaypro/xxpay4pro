package org.xxpay.service.common.annotation;

import org.xxpay.service.common.enumm.DataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切换数据源 注解
 * 在方法层面上添加 @DataSourceSwitch(DataSourceType.MASTER) 即可切换数据源
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceSwitch {
    /**
     * 默认使用Master
     * @return
     */
    DataSourceType value() default DataSourceType.MASTER;
}
