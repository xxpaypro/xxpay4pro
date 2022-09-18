package org.xxpay.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.PayInterface;
import org.xxpay.core.entity.PayInterfaceExample;
import org.xxpay.core.service.IPayInterfaceService;
import org.xxpay.service.dao.mapper.PayInterfaceMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/5/3
 * @description: 支付接口
 */
@Service
public class PayInterfaceServiceImpl extends ServiceImpl<PayInterfaceMapper, PayInterface> implements IPayInterfaceService {

    @Autowired
    private PayInterfaceMapper payInterfaceMapper;

    @Override
    public int add(PayInterface payInterface) {
        return payInterfaceMapper.insertSelective(payInterface);
    }

    @Override
    public int update(PayInterface payInterface) {
        return payInterfaceMapper.updateByPrimaryKeySelective(payInterface);
    }

    @Override
    public PayInterface findByCode(String ifCode) {
        return payInterfaceMapper.selectByPrimaryKey(ifCode);
    }

    @Override
    public PayInterface findByTypeCodeAndPayType(String ifTypeCode, String payType) {
        return this.getOne(new QueryWrapper<PayInterface>().lambda()
                .eq(PayInterface::getIfTypeCode, ifTypeCode)
                .eq(PayInterface::getPayType, payType)
        );
    }

    @Override
    public List<PayInterface> select(int offset, int limit, PayInterface payInterface) {
        PayInterfaceExample example = new PayInterfaceExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayInterfaceExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterface);
        return payInterfaceMapper.selectByExample(example);
    }

    @Override
    public Integer count(PayInterface payInterface) {
        PayInterfaceExample example = new PayInterfaceExample();
        PayInterfaceExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterface);
        return payInterfaceMapper.countByExample(example);
    }

    @Override
    public List<PayInterface> selectAll(PayInterface payInterface) {
        PayInterfaceExample example = new PayInterfaceExample();
        example.setOrderByClause("createTime DESC");
        PayInterfaceExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payInterface);
        return payInterfaceMapper.selectByExample(example);
    }

    @Override
    public List<PayInterface> selectAllByTypeCode(String ifTypeCode) {
        PayInterface payInterface = new PayInterface();
        payInterface.setIfTypeCode(ifTypeCode);
        return selectAll(payInterface);
    }

    void setCriteria(PayInterfaceExample.Criteria criteria, PayInterface obj) {
        if(obj != null) {
            if(obj.getIfTypeCode() != null) criteria.andIfTypeCodeEqualTo(obj.getIfTypeCode());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
        }
    }
}
