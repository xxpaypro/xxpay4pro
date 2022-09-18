package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.entity.MchNotify;
import org.xxpay.core.entity.MchNotifyExample;
import org.xxpay.core.service.IMchNotifyService;
import org.xxpay.service.dao.mapper.MchNotifyMapper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/5
 * @description:
 */
@Service
public class MchNotifyServiceImpl extends ServiceImpl<MchNotifyMapper, MchNotify> implements IMchNotifyService {

    @Autowired
    private MchNotifyMapper mchNotifyMapper;

    @Override
    public MchNotify findByOrderId(String orderId) {
        return mchNotifyMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public List<MchNotify> select(int offset, int limit, MchNotify mchNotify) {
        MchNotifyExample example = new MchNotifyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchNotifyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchNotify);
        return mchNotifyMapper.selectByExample(example);
    }

    @Override
    public Integer count(MchNotify mchNotify) {
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchNotify);
        return mchNotifyMapper.countByExample(example);
    }

    @Override
    public int insertSelectiveOnDuplicateKeyUpdate(String orderId, Long mchId, Long isvId, String appId, String mchOrderNo, String orderType, String notifyUrl) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setOrderId(orderId);
        mchNotify.setMchId(mchId);
        mchNotify.setIsvId(isvId);
        mchNotify.setAppId(appId);
        mchNotify.setMchOrderNo(mchOrderNo);
        mchNotify.setOrderType(orderType);
        mchNotify.setNotifyUrl(notifyUrl);
        return mchNotifyMapper.insertSelectiveOnDuplicateKeyUpdate(mchNotify);
    }

    @Override
    public int updateMchNotifySuccess(String orderId, String result, byte notifyCount) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setStatus(PayConstant.MCH_NOTIFY_STATUS_SUCCESS);
        mchNotify.setResult(result);
        mchNotify.setNotifyCount(notifyCount);
        mchNotify.setLastNotifyTime(new Date());
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List values = new LinkedList<>();
        values.add(PayConstant.MCH_NOTIFY_STATUS_NOTIFYING);
        values.add(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        criteria.andStatusIn(values);
        return mchNotifyMapper.updateByExampleSelective(mchNotify, example);
    }

    @Override
    public int updateMchNotifyFail(String orderId, String result, byte notifyCount) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setStatus(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        if(result != null && result.length() > 2000) {
            mchNotify.setResult(result.substring(0, 2000));
        }else {
            mchNotify.setResult(result);
        }
        mchNotify.setNotifyCount(notifyCount);
        mchNotify.setLastNotifyTime(new Date());
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List values = new LinkedList<>();
        values.add(PayConstant.MCH_NOTIFY_STATUS_NOTIFYING);
        values.add(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        return mchNotifyMapper.updateByExampleSelective(mchNotify, example);
    }

    void setCriteria(MchNotifyExample.Criteria criteria, MchNotify mchNotify) {
        if(mchNotify != null) {
            if(mchNotify.getMchId() != null) criteria.andMchIdEqualTo(mchNotify.getMchId());
            if(mchNotify.getIsvId() != null) criteria.andIsvIdEqualTo(mchNotify.getIsvId());
            if(StringUtils.isNotBlank(mchNotify.getOrderId())) criteria.andOrderIdEqualTo(mchNotify.getOrderId());
            if(StringUtils.isNotBlank(mchNotify.getOrderType())) criteria.andOrderTypeEqualTo(mchNotify.getOrderType());
            if(StringUtils.isNotBlank(mchNotify.getMchOrderNo())) criteria.andMchOrderNoEqualTo(mchNotify.getMchOrderNo());
            if(mchNotify.getStatus() != null && mchNotify.getStatus() != -99) criteria.andStatusEqualTo(mchNotify.getStatus());
        }
    }

}
