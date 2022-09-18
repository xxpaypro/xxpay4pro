package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MemberPointsHistory;

import java.util.Date;

/**
 * <p>
 * 商户会员积分流水表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberPointsHistoryService extends IService<MemberPointsHistory> {

    /**
     * 会员储值流水列表
     * @param page
     * @param pointsHistory
     * @return
     */
    IPage<MemberPointsHistory> selectPage(Page page, MemberPointsHistory pointsHistory);

    /**
     * 会员赠送积分
     * @return
     */
    Long sumGivePoints(Date startTime, Date endTime, Long mchId);

    /**
     * 会员消费积分
     * @return
     */
    Long sumConsumePoints(Date startTime, Date endTime, Long mchId);
}
