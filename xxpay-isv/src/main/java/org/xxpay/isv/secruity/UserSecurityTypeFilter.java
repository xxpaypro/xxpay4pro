package org.xxpay.isv.secruity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.vo.JWTBaseUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

/**
 * <p><b>Title: </b>UserSecurityTypeFilter.java
 * <p><b>Description: 用户安全认证方式过滤器</b>
 * @modify terrfly
 * @version V1.0
 * <p>
 */
public class UserSecurityTypeFilter extends OncePerRequestFilter {

    private static final MyLog _log = MyLog.getLog(UserSecurityTypeFilter.class);

    //放行url
    private static final HashSet<String> passUriSet = new HashSet<>();
    static{
        passUriSet.add("/api/isv/get"); //获取服务商信息 否则页面出现错误信息
        passUriSet.add("/api/isv/menu_get"); //获取菜单
        passUriSet.add("/api/data/count4Account"); //首页统计数据
//        passUriSet.add("/api/data/count4agent"); // 首页统计数据
        passUriSet.add("/api/security/google_qrcode"); // 获取谷歌验证码图片
        passUriSet.add("/api/security/set_security_auth_type"); // 设置安全认证方式
        passUriSet.add("/api/agent/sms_send"); // 发送验证码
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
    							    FilterChain chain) throws ServletException, IOException {


        if(passUriSet.contains(request.getRequestURI())){
            chain.doFilter(request, response);
            return ;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            chain.doFilter(request, response);
            return ;
        }

        JWTBaseUser jwtUser = (JWTBaseUser) authentication.getPrincipal();
        if(jwtUser == null) { //无用户信息(返回错误信息)
            chain.doFilter(request, response);
            return ;
        }

        //返回json形式的错误信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("{\"code\":1002, \"msg\":\"not set secutiryType\"}");
        response.getWriter().flush();
    }
    
}
