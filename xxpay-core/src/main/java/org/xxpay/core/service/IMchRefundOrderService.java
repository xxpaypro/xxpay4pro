package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchRefundOrder;
import org.xxpay.core.entity.MchTradeOrder;

/**
 * <p>
 * 商户退款表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMchRefundOrderService extends IService<MchRefundOrder> {


    /** 发起退款申请， 所有验证通过并调起上游返回退款成功后的收银系统业务处理 **/
    void refundSuccess(MchRefundOrder mchRefundOrder, MchTradeOrder mchTradeOrder);

    /** 退款失败 **/
    void refundFail(String mchRefundId);

}
