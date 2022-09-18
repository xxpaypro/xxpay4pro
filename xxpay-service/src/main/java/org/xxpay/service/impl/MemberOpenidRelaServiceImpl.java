package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MemberOpenidRela;
import org.xxpay.core.service.IMemberOpenidRelaService;
import org.xxpay.service.dao.mapper.MemberOpenidRelaMapper;

/**
 * <p>
 * 会员与微信openid关联表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-16
 */
@Service
public class MemberOpenidRelaServiceImpl extends ServiceImpl<MemberOpenidRelaMapper, MemberOpenidRela> implements IMemberOpenidRelaService {

    @Override
    public int saveOrUpdateRecord(Long memberId, String openid, Byte authFrom, Long mchId) {

        LambdaQueryWrapper<MemberOpenidRela> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberOpenidRela::getMemberId, memberId);
        queryWrapper.eq(MemberOpenidRela::getWxOpenId, openid);
        queryWrapper.eq(MemberOpenidRela::getWxOpenIdFrom, authFrom);
        queryWrapper.eq(MemberOpenidRela::getMchId, mchId);
        MemberOpenidRela dbRecord = baseMapper.selectOne(queryWrapper);

        int count;
        //未在当前小程序中注册
        if (dbRecord == null) {
            //保存openid
            MemberOpenidRela openidRela = new MemberOpenidRela();
            openidRela.setMemberId(memberId);
            openidRela.setWxOpenId(openid);
            openidRela.setWxOpenIdFrom(authFrom);
            openidRela.setMchId(mchId);
            count = baseMapper.insert(openidRela);
            if(count != 1) return 0;
        }

        return 1;
    }
}
