package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MemberBalanceHistory;

/**
 * <p>
 * 商户会员储值流水表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberBalanceHistoryService extends IService<MemberBalanceHistory> {

    /**
     * 会员储值流水列表
     * @param page
     * @param balanceHistory
     * @return
     */
    IPage<MemberBalanceHistory> selectPage(Page page, MemberBalanceHistory balanceHistory);
}
