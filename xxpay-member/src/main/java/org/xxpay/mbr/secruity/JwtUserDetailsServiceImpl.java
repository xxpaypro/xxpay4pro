package org.xxpay.mbr.secruity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.Member;
import org.xxpay.mbr.common.service.RpcCommonService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RpcCommonService rpc;

    @Override
    public UserDetails loadUserByUsername(String loginStr) throws UsernameNotFoundException {


        //查找登录用户
        Member member = rpc.rpcMemberService.getById(Long.valueOf(loginStr));
        if(member == null || member.getStatus() != MchConstant.PUB_YES) return null;

        return (UserDetails) member;

    }
}
