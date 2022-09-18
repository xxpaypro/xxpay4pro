package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.PayType;
import org.xxpay.core.entity.PayTypeExample;
import org.xxpay.core.service.IPayTypeService;
import org.xxpay.service.dao.mapper.PayTypeMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/8/28
 * @description: 支付类型
 */
@Service
public class PayTypeServiceImpl extends ServiceImpl<PayTypeMapper, PayType> implements IPayTypeService {

    @Autowired
    private PayTypeMapper payTypeMapper;

    @Override
    public int add(PayType payType) {
        return payTypeMapper.insertSelective(payType);
    }

    @Override
    public int update(PayType payType) {
        return payTypeMapper.updateByPrimaryKeySelective(payType);
    }

    @Override
    public PayType findByPayTypeCode(String payTypeCode) {
        return payTypeMapper.selectByPrimaryKey(payTypeCode);
    }

    @Override
    public List<PayType> select(int offset, int limit, PayType payType) {
        PayTypeExample example = new PayTypeExample();
        example.setOrderByClause("payTypeCode ASC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payType);
        return payTypeMapper.selectByExample(example);
    }

    @Override
    public Integer count(PayType payType) {
        PayTypeExample example = new PayTypeExample();
        PayTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payType);
        return payTypeMapper.countByExample(example);
    }

    @Override
    public List<PayType> selectAll(PayType payType) {
        PayTypeExample example = new PayTypeExample();
        example.setOrderByClause("payTypeCode ASC");
        PayTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payType);
        return payTypeMapper.selectByExample(example);
    }

    @Override
    public List<PayType> selectAllByType(String type) {
        PayType payType = new PayType();
        payType.setType(type);
        return selectAll(payType);
    }

    void setCriteria(PayTypeExample.Criteria criteria, PayType obj) {
        if(obj != null) {
            if(StringUtils.isNotBlank(obj.getPayTypeCode())) criteria.andPayTypeCodeEqualTo(obj.getPayTypeCode());
            if(StringUtils.isNotBlank(obj.getType())) criteria.andTypeEqualTo(obj.getType());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
        }
    }

}
