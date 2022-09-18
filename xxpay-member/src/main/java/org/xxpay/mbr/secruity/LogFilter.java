package org.xxpay.mbr.secruity;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.util.IPUtility;
import org.xxpay.core.common.util.MyLog;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用注解标注过滤器
 * @WebFilter将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 * 
 */
@Component
@WebFilter(filterName="LogFilter",urlPatterns= Constant.MBR_CONTROLLER_ROOT_PATH + "/*")
public class LogFilter implements Filter {

    private static final MyLog _log = MyLog.getLog(LogFilter.class);

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) {

        try {
            long startTime = System.currentTimeMillis();
            WrappedHttpServletRequest requestWrapper = new WrappedHttpServletRequest((HttpServletRequest) request);
            HttpServletResponse response1 = (HttpServletResponse) response;
            String uri = requestWrapper.getRequestURI();
            String remoteAddr = IPUtility.getClientIp(requestWrapper);
            String method = requestWrapper.getMethod().toUpperCase();
            String params = "";
            if ("POST".equals(method)) {
                //params = requestWrapper.getRequestParams();
                params = JSON.toJSONString(requestWrapper.getParameterMap());
            } else if("GET".equals(method)) {
                params = JSON.toJSONString(requestWrapper.getParameterMap());
            }
            _log.info("[request] [uri:{}, method:{}, remoteAddr:{}, params:{}]", uri, method, remoteAddr, params);
            // 这里doFilter传入我们实现的子类
            chain.doFilter(requestWrapper, response1);
            _log.info("[response] [uri:{}, status:{}, cost:{} ms]", uri, response1.getStatus(), System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            _log.error(e, "");
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}