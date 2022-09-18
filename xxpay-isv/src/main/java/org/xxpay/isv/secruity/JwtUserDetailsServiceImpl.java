package org.xxpay.isv.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RpcCommonService rpc;

    @Override
    public JWTBaseUser loadUserByUsername(String loginStr) throws UsernameNotFoundException {

        //1. 查找登录用户
        SysUser isvUser = rpc.rpcSysService.findByLoginStr(loginStr, MchConstant.INFO_TYPE_ISV);
        if(isvUser == null) return null;

        //2. 查询服务商信息
        IsvInfo isvInfo = rpc.rpcIsvInfoService.getById(isvUser.getBelongInfoId());
        if(isvInfo == null)  return null;

        //3. 查询用户拥有权限集合
        List<String> perms = rpc.rpcSysService.getEffectivePerms(MchConstant.INFO_TYPE_ISV, isvUser);
        perms.add(MchConstant.ISV_ROLE_NORMAL); ///添加默认角色

        //转换为GrantedAuthority格式
        List<GrantedAuthority> grantedAuthorities = perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        //4. 创建JWTBaseUser
        return new JWTBaseUser(isvUser, grantedAuthorities);
    }
}
