package org.xxpay.isv.sys.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.IPUtility;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.RequestUtils;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.SysLog;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.utils.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description 方法级日志切面组件
 **/

@Component
@Aspect
public class MethodLogAop {

    private static final MyLog logger = MyLog.getLog(MethodLogAop.class);

    /**
     * 异步处理线程池
     */
    private final static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    /**
     * 切点
     */
    @Pointcut("@annotation(org.xxpay.core.common.annotation.MethodLog)")
    public void methodCachePointcut() { }

    /**
     * 切面
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("methodCachePointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        final SysLog sysLog = new SysLog();
        sysLog.setSystem(MchConstant.INFO_TYPE_ISV); //服务商系统

        // 使用point.getArgs()可获取request，仅限于spring MVC参数包含request，改为通过contextHolder获取。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //请求参数
        sysLog.setOptReqParam( RequestUtils.getJsonParam(request, true).toJSONString() );

        //注解备注
        sysLog.setMethodRemark( getAnnotationRemark(point) );

        //包名 方法名
        String methodName = point.getSignature().getName();
        String packageName = point.getThis().getClass().getName();
        if (packageName.indexOf("$$EnhancerByCGLIB$$") > -1 || packageName.indexOf("$$EnhancerBySpringCGLIB$$") > -1) { // 如果是CGLIB动态生成的类
            packageName = packageName.substring(0, packageName.indexOf("$$"));
        }
        sysLog.setMethodName(packageName + "." + methodName);

        //处理切面任务 发生异常将向外抛出， 不记录日志
        Object result = point.proceed();

        try {

            Long currentUserId = 0L;
            String currentUserName = "";

            JWTBaseUser jwtBaseUser = SpringSecurityUtil.getCurrentJWTUser();
            if(jwtBaseUser != null){
                currentUserId = jwtBaseUser.getUserId();
                currentUserName = jwtBaseUser.getNickName();
            }

            sysLog.setUserId(currentUserId);
            sysLog.setUserName(currentUserName);
            sysLog.setUserIp(IPUtility.getClientIp(request));
            sysLog.setOptResInfo(result.toString());
            sysLog.setCreateTime(new Date());

            scheduledThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    RpcCommonService rpcCommonService = (RpcCommonService) SpringUtil.getBean("rpcCommonService");
                    rpcCommonService.rpcSysLogService.add(sysLog);
                }
            });
        } catch (Exception e) {
            logger.error("methodLogError", e);
        }

        return result;
    }

    /**
     * 获取方法中的中文备注
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getAnnotationRemark(ProceedingJoinPoint joinPoint) throws Exception {

        Signature sig = joinPoint.getSignature();
        Method m = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(),  ((MethodSignature) sig).getParameterTypes());

        MethodLog methodCache = m.getAnnotation(MethodLog.class);
        if (methodCache != null) {
            return methodCache.remark();
        }
        return "";
    }
}
