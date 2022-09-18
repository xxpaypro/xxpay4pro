package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.core.entity.RefundOrderExample;
import org.xxpay.core.service.IRefundOrderService;
import org.xxpay.service.dao.mapper.RefundOrderMapper;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/10/30
 * @description:
 */
@Service
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements IRefundOrderService {

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    @Override
    public RefundOrder findByRefundOrderId(String refundOrderId) {
        return refundOrderMapper.selectByPrimaryKey(refundOrderId);
    }

    public int createRefundOrder(RefundOrder refundOrder) {
        return refundOrderMapper.insertSelective(refundOrder);
    }

    public RefundOrder selectRefundOrder(String refundOrderId) {
        return refundOrderMapper.selectByPrimaryKey(refundOrderId);
    }

    public RefundOrder selectByMchIdAndRefundOrderId(Long mchId, String refundOrderId) {
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andRefundOrderIdEqualTo(refundOrderId);
        List<RefundOrder> refundOrderList = refundOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(refundOrderList) ? null : refundOrderList.get(0);
    }

    public RefundOrder selectByMchIdAndMchRefundNo(Long mchId, String mchRefundNo) {
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andMchRefundNoEqualTo(mchRefundNo);
        List<RefundOrder> refundOrderList = refundOrderMapper.selectByExample(example);
        return org.springframework.util.CollectionUtils.isEmpty(refundOrderList) ? null : refundOrderList.get(0);
    }

    public int updateStatus4Ing(String refundOrderId, String channelOrderNo) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setStatus(PayConstant.REFUND_STATUS_REFUNDING);
        if(channelOrderNo != null) refundOrder.setChannelOrderNo(channelOrderNo);
        refundOrder.setRefundSuccTime(new Date());
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        criteria.andRefundOrderIdEqualTo(refundOrderId);
        criteria.andStatusEqualTo(PayConstant.REFUND_STATUS_INIT);
        return refundOrderMapper.updateByExampleSelective(refundOrder, example);
    }

    public int updateStatus4Success(String refundOrderId) {
        return updateStatus4Success(refundOrderId, null);
    }

    public int updateStatus4Success(String refundOrderId, String channelOrderNo) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setRefundOrderId(refundOrderId);
        refundOrder.setStatus(PayConstant.REFUND_STATUS_SUCCESS);
        refundOrder.setResult(PayConstant.REFUND_RESULT_SUCCESS);
        refundOrder.setRefundSuccTime(new Date());
        if(StringUtils.isNotBlank(channelOrderNo)) refundOrder.setChannelOrderNo(channelOrderNo);
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        criteria.andRefundOrderIdEqualTo(refundOrderId);
        criteria.andStatusEqualTo(PayConstant.REFUND_STATUS_REFUNDING);
        return refundOrderMapper.updateByExampleSelective(refundOrder, example);
    }


    public int updateStatus4Fail(String refundOrderId, String channelErrCode, String channelErrMsg) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setStatus(PayConstant.REFUND_STATUS_FAIL);
        refundOrder.setResult(PayConstant.REFUND_RESULT_FAIL);
        if(channelErrCode != null) refundOrder.setChannelErrCode(channelErrCode);
        if(channelErrMsg != null) refundOrder.setChannelErrMsg(channelErrMsg);
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        criteria.andRefundOrderIdEqualTo(refundOrderId);
        criteria.andStatusEqualTo(PayConstant.REFUND_STATUS_REFUNDING);
        return refundOrderMapper.updateByExampleSelective(refundOrder, example);
    }

    @Override
    public RefundOrder find(RefundOrder refundOrder) {
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, refundOrder);
        List<RefundOrder> refundOrderList = refundOrderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(refundOrderList)) return null;
        return refundOrderList.get(0);
    }

    @Override
    public List<RefundOrder> select(int offset, int limit, RefundOrder refundOrder, Date createTimeStart, Date createTimeEnd) {
        RefundOrderExample example = new RefundOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        RefundOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, refundOrder, createTimeStart, createTimeEnd);
        return refundOrderMapper.selectByExample(example);
    }

    @Override
    public Integer count(RefundOrder refundOrder, Date createTimeStart, Date createTimeEnd) {
        RefundOrderExample example = new RefundOrderExample();
        RefundOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, refundOrder, createTimeStart, createTimeEnd);
        return refundOrderMapper.countByExample(example);
    }

    void setCriteria(RefundOrderExample.Criteria criteria, RefundOrder refundOrder) {
        setCriteria(criteria, refundOrder, null, null);
    }

    void setCriteria(RefundOrderExample.Criteria criteria, RefundOrder refundOrder, Date createTimeStart, Date createTimeEnd) {
        if(refundOrder != null) {
            if(refundOrder.getMchId() != null) criteria.andMchIdEqualTo(refundOrder.getMchId());
            if(StringUtils.isNotBlank(refundOrder.getRefundOrderId())) criteria.andRefundOrderIdEqualTo(refundOrder.getRefundOrderId());
            if(StringUtils.isNotBlank(refundOrder.getMchRefundNo())) criteria.andMchRefundNoEqualTo(refundOrder.getMchRefundNo());
            if(StringUtils.isNotBlank(refundOrder.getChannelOrderNo())) criteria.andChannelOrderNoEqualTo(refundOrder.getChannelOrderNo());
            if(refundOrder.getStatus() != null && refundOrder.getStatus() != -99) criteria.andStatusEqualTo(refundOrder.getStatus());
        }
        if(createTimeStart != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(createTimeStart);
        }
        if(createTimeEnd != null) {
            criteria.andCreateTimeLessThanOrEqualTo(createTimeEnd);
        }
    }

}
