package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.Member;

import java.util.Map;

/**
 * <p>
 * 商户会员表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MemberMapper extends BaseMapper<Member> {

    /** 查询会员所有信息, 包含会员注册信息, 会员余额， 会员积分数据 （一般用于会员支付时显示） **/
    Map selectMemberAllInfo(@Param("mchId") Long mchId, @Param("memberId")Long memberId, @Param("wxOpenId")String wxOpenId);

}
