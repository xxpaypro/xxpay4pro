package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysLog;
import org.xxpay.core.entity.SysLogExample;
import org.xxpay.core.service.ISysLogService;
import org.xxpay.service.dao.mapper.SysLogMapper;

import java.util.List;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    
    @Autowired
    private SysLogMapper recordMapper;

    void setCriteria(SysLogExample.Criteria criteria, SysLog obj) {
        if(obj != null) {
            if(obj.getUserId() != null) criteria.andUserIdEqualTo(obj.getUserId());
            if(obj.getSystem() != null) criteria.andSystemEqualTo(obj.getSystem());
        }
    }

    @Override
    public int add(SysLog record) {
        return recordMapper.insertSelective(record);
    }

    @Override
    public List<SysLog> select(int offset, int limit, SysLog record) {
        SysLogExample example = new SysLogExample();
        example.setOrderByClause("CreateTime desc");
        example.setOffset(offset);
        example.setLimit(limit);
        SysLogExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, record);
        return recordMapper.selectByExample(example);
    }

    @Override
    public Integer count(SysLog record) {
        SysLogExample example = new SysLogExample();
        SysLogExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, record);
        return recordMapper.countByExample(example);
    }
}
