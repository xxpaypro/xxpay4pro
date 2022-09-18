package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchTradeOrderAfterSale;

/**
 * <p>
 * 商城订单售后表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-19
 */
public interface IMchTradeOrderAfterSaleService extends IService<MchTradeOrderAfterSale> {

    /**
     * 申请售后
     * @param afterSale
     * @return
     */
    boolean apply(MchTradeOrderAfterSale afterSale);

    /**
     * 更新售后为完成状态
     * @param afterOrderId
     * @return
     */
    boolean updateComplete(Long afterOrderId);

    /**
     * 正在进行售后服务的订单数量
     * @param memberId
     * @param authFrom
     * @return
     */
    int countAfterSaleIng(Long memberId, Byte authFrom);
}
