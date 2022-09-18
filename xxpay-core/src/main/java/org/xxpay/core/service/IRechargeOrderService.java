package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.RechargeOrder;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/9/7
 * @description: 商户充值
 */
public interface IRechargeOrderService extends IService<RechargeOrder> {

    RechargeOrder find(RechargeOrder rechargeOrder);

    RechargeOrder findByRechargeOrderId(String rechargeOrderId);

    RechargeOrder findByMchIdAndRechargeOrderId(Long mchId, String rechargeOrderId);

    List<RechargeOrder> select(int offset, int limit, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd);

    List<RechargeOrder> select(Long mchId, int offset, int limit, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd);

    Integer count(Long mchId, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd);

    Integer count(RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd);

    Integer count(RechargeOrder rechargeOrder, List<Byte> statusList);

    int updateByRechargeOrderId(String rechargeOrderId, RechargeOrder rechargeOrder);

    int updateStatus4Ing(String rechargeOrderId, String channelOrderNo);

    int updateStatus4Success(String rechargeOrderId);

    int updateStatus4Fail(String rechargeOrderId);

    int updateStatus4Success(String rechargeOrderId, String channelOrderNo);

    int updateStatus4Complete(String rechargeOrderId);

    int createRechargeOrder(RechargeOrder rechargeOrder);

}
