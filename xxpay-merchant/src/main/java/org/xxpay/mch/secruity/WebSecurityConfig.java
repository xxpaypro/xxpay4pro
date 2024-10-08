package org.xxpay.mch.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
		     	// 由于使用的是JWT，我们这里不需要csrf
		        .csrf().disable().cors().and()
		
		        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		
		        // 基于token，所以不需要session
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		
		        .authorizeRequests()
		        
		        // 除上面外的所有请求全部需要鉴权认证
		        .anyRequest().authenticated();
		
		        // 添加JWT filter
		        httpSecurity.addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

                //添加用户安全类型方式 filter
                // httpSecurity.addFilterAfter(new UserSecurityTypeFilter(), UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore文件 ： 无需进入spring security 框架
    	// 1.允许对于网站静态资源的无授权访问
        // 2.对于获取token的rest api要允许匿名访问
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.svg",
                "/**/*.webp",
                "/*.txt",
                "/**/*.xls",
                "/**/*.mp4"   //支持mp4格式的文件匿名访问
        )
        .antMatchers(
        		"/api/auth/**",
        	    "/api/google_auth/**",
        	    "/api/mgr_auth/**",
        	    "/api/payment/**",
                "/api/paydemo/notify*",
        	    "/api/notify/**",
        	    "/api/order_advert/**",
        	    "/html/**",
        		"/x_mch/**/*.*",
                "/xxv/**",
                "/cash/**",
                "/api/clientVersion/**",
                "/update/download/**"  //更新文件下载 接口
        );
    }
    
}
