package org.xxpay.manage.secruity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.common.vo.JWTPayload;
import org.xxpay.core.common.vo.JWTUtils;
import org.xxpay.manage.common.config.MainConfig;
import org.xxpay.manage.utils.SpringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>Title: </b>JwtAuthenticationTokenFilter.java
 * <p><b>Description: </b>
 * spring security框架中验证组件的前置过滤器；
 * 用于验证token有效期，并放置ContextAuthentication信息,为后续spring security框架验证提供数据；
 * 避免使用@Component等bean自动装配注解：@Component会将filter被spring实例化为web容器的全局filter，导致重复过滤。
 * @modify terrfly
 * @version V1.0
 * <p>
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final MyLog _log = MyLog.getLog(JwtAuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);
        if(StringUtils.isEmpty(authToken)){
            authToken = request.getParameter(MchConstant.USER_TOKEN_KEY);
        }
        if(StringUtils.isEmpty(authToken)){
            chain.doFilter(request, response); return ; //放行,并交给UsernamePasswordAuthenticationFilter进行验证,返回公共错误信息.
        }

        JWTPayload jwtPayload = JWTUtils.parseToken(authToken, getMainConfig().getJwtSecret());  //反解析token信息
        //token字符串解析失败 || redis不存在（token已失效）
        if( jwtPayload == null ||
                StringUtils.isEmpty(getStringRedisTemplate().opsForValue().get(MchConstant.CACHEKEY_TOKEN_PREFIX_MGR + authToken))
        ){
            chain.doFilter(request, response); return ;
        }

        //根据用户名查找数据库
        JWTBaseUser jwtBaseUser = (JWTBaseUser)getUserDetailsServiceBean().loadUserByUsername(jwtPayload.getLoginUserName());
        if(jwtBaseUser == null || !jwtPayload.validateToken(jwtBaseUser.getUserId(), jwtBaseUser.getLastPasswordResetTime())){
            getStringRedisTemplate().delete(MchConstant.CACHEKEY_TOKEN_PREFIX_MGR + authToken);
            chain.doFilter(request, response); return ; //数据库查询失败，或用户名/密码有修改，需重新 签token
        }
        jwtBaseUser.setLoginTypeAndVersion(jwtPayload.getLoginType(), jwtPayload.getVersion(), jwtPayload.getLoginDeviceNo()); //设置其他必要信息

        //redis put authToken
        getStringRedisTemplate().opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MGR + authToken, "1",
                MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

        //将信息放置到Spring-security context中
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtBaseUser, null, jwtBaseUser.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        _log.debug("authenticated user " + jwtBaseUser.getLoginUserName() + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private static MainConfig getMainConfig(){
        return SpringUtil.getBean(MainConfig.class);

    }

    private static UserDetailsService getUserDetailsServiceBean(){
        return SpringUtil.getBean(UserDetailsService.class);
    }

    private static StringRedisTemplate getStringRedisTemplate(){
        return SpringUtil.getBean(StringRedisTemplate.class);
    }

}
