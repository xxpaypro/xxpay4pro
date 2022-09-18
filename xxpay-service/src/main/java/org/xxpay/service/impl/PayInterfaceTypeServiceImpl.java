package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayInterfaceTypeExample;
import org.xxpay.core.service.IPayInterfaceTypeService;
import org.xxpay.service.dao.mapper.PayInterfaceTypeMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/5/3
 * @description: 支付接口类型
 */
@Service
public class PayInterfaceTypeServiceImpl extends ServiceImpl<PayInterfaceTypeMapper, PayInterfaceType> implements IPayInterfaceTypeService {

    @Autowired
    private PayInterfaceTypeMapper payInterfaceTypeMapper;

    @Override
    public int add(PayInterfaceType payInterfaceType) {
        return payInterfaceTypeMapper.insertSelective(payInterfaceType);
    }

    @Override
    public int update(PayInterfaceType payInterfaceType) {
        return payInterfaceTypeMapper.updateByPrimaryKeySelective(payInterfaceType);
    }

    @Override
    public PayInterfaceType findByCode(String ifTypeCode) {
        return payInterfaceTypeMapper.selectByPrimaryKey(ifTypeCode);
    }

    @Override
    public List<PayInterfaceType> select(int offset, int limit, PayInterfaceType payInterfaceType) {
        PayInterfaceTypeExample example = new PayInterfaceTypeExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayInterfaceTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterfaceType);
        return payInterfaceTypeMapper.selectByExample(example);
    }

    @Override
    public Integer count(PayInterfaceType payInterfaceType) {
        PayInterfaceTypeExample example = new PayInterfaceTypeExample();
        PayInterfaceTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterfaceType);
        return payInterfaceTypeMapper.countByExample(example);
    }

    @Override
    public List<PayInterfaceType> selectAll(PayInterfaceType payInterfaceType) {
        PayInterfaceTypeExample example = new PayInterfaceTypeExample();
        example.setOrderByClause("createTime DESC");
        PayInterfaceTypeExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterfaceType);
        return payInterfaceTypeMapper.selectByExample(example);
    }

    void setCriteria(PayInterfaceTypeExample.Criteria criteria, PayInterfaceType obj) {
        if(obj != null) {
            if(obj.getIfTypeCode() != null) criteria.andIfTypeCodeEqualTo(obj.getIfTypeCode());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
        }
    }
}
