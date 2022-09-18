package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MemberPoints;

/**
 * <p>
 * 商户会员积分表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MemberPointsMapper extends BaseMapper<MemberPoints> {


    /**
     * 更新会员积分信息， 保证积分信息数据准确
     * @param memberPoints
     * @return
     */
    int updatePointByMemberId(MemberPoints memberPoints);

}
