package org.xxpay.manage.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.common.vo.JWTPayload;
import org.xxpay.core.common.vo.JWTUtils;
import org.xxpay.core.entity.SysUser;
import org.xxpay.manage.common.config.MainConfig;
import org.xxpay.manage.common.service.RpcCommonService;

/**
 * Created by dingzhiwei on 17/11/28.
 */
@Component
public class SysUserService {

    @Autowired
    private RpcCommonService rpcCommonService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MainConfig mainConfig;

    private static final MyLog _log = MyLog.getLog(SysUserService.class);

    /**
     * 查询
     * @param userId
     * @return
     */
    public SysUser findByUserId(Long userId) {
        return rpcCommonService.rpcSysService.findByUserId(userId);
    }

    public SysUser findByUserName(String userName) {
        return rpcCommonService.rpcSysService.findByUserName(userName);
    }

    public String login(String loginStr, String password) throws ServiceException {

        //1. 生成spring-security usernamePassword类型对象
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(loginStr, password);
        try{

            //spring-security 自动认证过程；
            // 1. 进入JwtUserDetailsServiceImpl.loadUserByUsername 获取用户基本信息；
            //2. SS根据UserDetails接口验证是否用户可用；
            //3. 最后返回loadUserByUsername 封装的对象信息；
            Authentication authentication = authenticationManager.authenticate(upToken);
            JWTBaseUser jwtBaseUser = (JWTBaseUser)authentication.getPrincipal();
            jwtBaseUser.setLoginType(MchConstant.LOGIN_TYPE_WEB);  //设置其他必要信息

            SecurityContextHolder.getContext().setAuthentication(authentication);

            JWTPayload jwtPayload = new JWTPayload(jwtBaseUser);
            return JWTUtils.generateToken(jwtPayload, mainConfig.getJwtSecret(), mainConfig.getJwtExpiration());

        }catch (Exception e) {
            _log.warn("鉴权失败:loginStr={},password={}", loginStr, password);
            throw new ServiceException(RetEnum.RET_MCH_AUTH_FAIL);
        }
    }

}
