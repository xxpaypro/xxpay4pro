package org.xxpay.mch.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RpcCommonService rpc;

    @Override
    public UserDetails loadUserByUsername(String loginStr) throws UsernameNotFoundException {


        //1. 查找登录用户
        SysUser mchUser = rpc.rpcSysService.findByLoginStr(loginStr, MchConstant.INFO_TYPE_MCH);
        if(mchUser == null) return null;

        //2. 查询商户信息
        MchInfo mchInfo = rpc.rpcMchInfoService.getById(mchUser.getBelongInfoId());
        if(mchInfo == null)  return null;

        //3. 查询用户拥有权限集合
        List<String> perms = rpc.rpcSysService.getEffectivePerms(MchConstant.INFO_TYPE_MCH, mchUser);
        perms.add(MchConstant.MCH_ROLE_NORMAL); ///添加默认角色

        //转换为GrantedAuthority格式
        List<GrantedAuthority> grantedAuthorities = perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        //4. 创建JWTBaseUser
        return new JWTBaseUser(mchUser, grantedAuthorities);

    }
}
