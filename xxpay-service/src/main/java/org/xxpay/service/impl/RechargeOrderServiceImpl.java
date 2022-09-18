package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.RechargeOrder;
import org.xxpay.core.service.IRechargeOrderService;
import org.xxpay.service.dao.mapper.RechargeOrderMapper;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
@Service
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderMapper, RechargeOrder> implements IRechargeOrderService {

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;

    @Override
    public RechargeOrder find(RechargeOrder rechargeOrder) {
        return null;
    }

    @Override
    public RechargeOrder findByRechargeOrderId(String rechargeOrderId) {
        return null;
    }

    @Override
    public RechargeOrder findByMchIdAndRechargeOrderId(Long mchId, String rechargeOrderId) {
        return null;
    }

    @Override
    public List<RechargeOrder> select(int offset, int limit, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd) {
        return null;
    }

    @Override
    public List<RechargeOrder> select(Long mchId, int offset, int limit, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd) {
        return null;
    }

    @Override
    public Integer count(Long mchId, RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd) {
        return null;
    }

    @Override
    public Integer count(RechargeOrder rechargeOrder, Date createTimeStart, Date createTimeEnd) {
        return null;
    }

    @Override
    public Integer count(RechargeOrder rechargeOrder, List<Byte> statusList) {
        return null;
    }

    @Override
    public int updateByRechargeOrderId(String rechargeOrderId, RechargeOrder rechargeOrder) {
        return 0;
    }

    @Override
    public int updateStatus4Ing(String rechargeOrderId, String channelOrderNo) {
        return 0;
    }

    @Override
    public int updateStatus4Success(String rechargeOrderId) {
        return 0;
    }

    @Override
    public int updateStatus4Fail(String rechargeOrderId) {
        return 0;
    }

    @Override
    public int updateStatus4Success(String rechargeOrderId, String channelOrderNo) {
        return 0;
    }

    @Override
    public int updateStatus4Complete(String rechargeOrderId) {
        return 0;
    }

    @Override
    public int createRechargeOrder(RechargeOrder rechargeOrder) {
        return 0;
    }
}
