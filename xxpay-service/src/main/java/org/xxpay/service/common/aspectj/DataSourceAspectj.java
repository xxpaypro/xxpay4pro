package org.xxpay.service.common.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.xxpay.service.common.annotation.DataSourceSwitch;
import org.xxpay.service.common.datasource.DataSourceManager;

import java.lang.reflect.Method;

/**
 * 数据源切面
 */
@Component
@Aspect
@Order(0)
public class DataSourceAspectj {

    /**
     * 切入点
     */
    @Pointcut("@annotation(org.xxpay.service.common.annotation.DataSourceSwitch)")
    public void dsPointCut() { }

    /**
     * 围绕事件
     */
    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //获取方法注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSourceSwitch dataSource = method.getAnnotation(DataSourceSwitch.class);
        if (dataSource != null) {
            DataSourceManager.setDataSource(dataSource.value());
        }

        try {
            return point.proceed();  //处理函数
        }finally {
            // 销毁数据源 在执行方法之后
            DataSourceManager.clear();
        }
    }

}
