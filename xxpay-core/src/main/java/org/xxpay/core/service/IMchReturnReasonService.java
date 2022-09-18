package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchReturnReason;

/**
 * <p>
 * 商户退货原因表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchReturnReasonService extends IService<MchReturnReason> {

    /**
     * 退货原因列表
     * @param mchReturnReason
     * @param page
     * @return
     */
    IPage<MchReturnReason> list(MchReturnReason mchReturnReason, Page page);
}
