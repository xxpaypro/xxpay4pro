package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MemberPointsHistory;

import java.util.Date;

/**
 * <p>
 * 商户会员积分流水表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MemberPointsHistoryMapper extends BaseMapper<MemberPointsHistory> {

    /* 会员赠送积分 */
    Long sumGivePoints(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mchId") Long mchId);

    /* 会员消费积分 */
    Long sumConsumePoints(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("mchId") Long mchId);
}
