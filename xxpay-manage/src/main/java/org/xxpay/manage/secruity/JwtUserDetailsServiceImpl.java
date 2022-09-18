package org.xxpay.manage.secruity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.SysResource;
import org.xxpay.core.entity.SysUser;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.sys.service.SysUserService;

import java.util.LinkedList;
import java.util.List;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.findByUserName(username);
        if (sysUser == null) {
            //throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
            return null;
        } else {
            List<SysResource> sysResourceList;
            if(MchConstant.PUB_YES == sysUser.getIsSuperAdmin()) {
                // 如果是超级管理用户,则得到所有权限资源
                sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_PLAT);
            }else {
                // 得到用户的权限资源
                sysResourceList = rpcCommonService.rpcSysService.selectResourceByUserId(sysUser.getUserId());
            }

            List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
            if(CollectionUtils.isNotEmpty(sysResourceList)) {
                for(SysResource sysResource : sysResourceList) {
                    if(sysResource == null) continue;
                    if(StringUtils.isBlank(sysResource.getPermName())) continue;
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysResource.getPermName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }

            // 本系统的权限规则是配置到资源中的权限是受保护的,没有配置的是不受保护的
            // 那么对于一些特殊接口,可以同给指定用户,或者指定角色增加特有的权限
            // 可以在controller中增加 @PreAuthorize("hasRole('"+ MchConstant.MGR_ROLE_NORMAL+"')")来限制
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(MchConstant.MGR_ROLE_NORMAL);
            grantedAuthorities.add(grantedAuthority);

            return new JWTBaseUser(sysUser, grantedAuthorities);
        }
    }
}
