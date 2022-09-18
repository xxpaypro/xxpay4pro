package org.xxpay.mbr.user.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.Member;
import org.xxpay.mbr.common.config.MainConfig;
import org.xxpay.mbr.common.service.RpcCommonService;
import org.xxpay.mbr.secruity.JwtToken;

import java.util.Map;

/**
 * Created by zx
 */
@Component
public class UserService {

    @Autowired
    private RpcCommonService rpcCommonService;
    @Autowired
    private MainConfig mainConfig;

    private static final MyLog _log = MyLog.getLog(UserService.class);

    /**
     * 查询
     * @param memberId
     * @return
     */
    public Member findByMemberId(String memberId) {
        //查找登录用户
        Member member = rpcCommonService.rpcMemberService.getById(Long.valueOf(memberId));
        if(member == null || member.getStatus() != MchConstant.PUB_YES) return null;

        return member;
    }

    public String login(Long memberId) throws ServiceException {

        //获取会员ID
        Member member = rpcCommonService.rpcMemberService.getById(memberId);
        if (member == null && member.getStatus() != MchConstant.PUB_YES) {
            throw new ServiceException(RetEnum.RET_MGR_SUBUSER_NOT_EXIST);
        }

        JSONObject json = (JSONObject) JSONObject.toJSON(member);
        try{
            return JwtToken.generateToken(json.toJavaObject(Map.class), mainConfig.getJwtSecret(), mainConfig.getJwtExpiration());
        }catch (Exception e) {
            throw new ServiceException(RetEnum.RET_MGR_LOGIN_FAIL);
        }
    }

}
