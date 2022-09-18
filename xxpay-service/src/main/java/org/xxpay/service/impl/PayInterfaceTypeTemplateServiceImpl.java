package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.PayInterfaceTypeTemplate;
import org.xxpay.core.entity.PayInterfaceTypeTemplateExample;
import org.xxpay.core.service.IPayInterfaceTypeTemplateService;
import org.xxpay.service.dao.mapper.PayInterfaceTypeTemplateMapper;

import java.util.List;


@Service
public class PayInterfaceTypeTemplateServiceImpl extends ServiceImpl<PayInterfaceTypeTemplateMapper, PayInterfaceTypeTemplate> implements IPayInterfaceTypeTemplateService {
	
	@Autowired
	private PayInterfaceTypeTemplateMapper recordMapper;

	@Override
	public List<PayInterfaceTypeTemplate> select(int offset, int limit, PayInterfaceTypeTemplate record) {
		PayInterfaceTypeTemplateExample example = new PayInterfaceTypeTemplateExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayInterfaceTypeTemplateExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, record);
        return recordMapper.selectByExample(example);
	}

	@Override
	public int count(PayInterfaceTypeTemplate record) {
		PayInterfaceTypeTemplateExample example = new PayInterfaceTypeTemplateExample();
		PayInterfaceTypeTemplateExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, record);
        return recordMapper.countByExample(example);
	}

	@Override
	public PayInterfaceTypeTemplate findById(Long id) {
		return recordMapper.selectByPrimaryKey(id);
	}

	@Override
	public int update(PayInterfaceTypeTemplate record) {
		return recordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int add(PayInterfaceTypeTemplate record) {
		return recordMapper.insertSelective(record);
	}
	
	
	 void setCriteria(PayInterfaceTypeTemplateExample.Criteria criteria, PayInterfaceTypeTemplate obj) {
	        if(obj != null) {
	            if(obj.getIfTypeCode() != null) criteria.andIfTypeCodeEqualTo(obj.getIfTypeCode());
	            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
	        }
	    }

}
