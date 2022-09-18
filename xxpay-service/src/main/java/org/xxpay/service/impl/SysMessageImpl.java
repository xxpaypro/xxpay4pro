package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysMessage;
import org.xxpay.core.entity.SysMessageExample;
import org.xxpay.core.service.ISysMessageService;
import org.xxpay.service.dao.mapper.SysMessageMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/19
 * @description:
 */
@Service
public class SysMessageImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public List<SysMessage> select(int pageIndex, int pageSize, SysMessage sysMessage) {
        SysMessageExample example = new SysMessageExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        SysMessageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, sysMessage);
        return sysMessageMapper.selectByExample(example);
    }

    @Override
    public int count(SysMessage sysMessage) {
        SysMessageExample example = new SysMessageExample();
        example.setOrderByClause("createTime DESC");
        SysMessageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, sysMessage);
        return sysMessageMapper.countByExample(example);
    }

    @Override
    public SysMessage findById(Long id) {
        return sysMessageMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysMessage find(SysMessage sysMessage) {
        SysMessageExample example = new SysMessageExample();
        SysMessageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, sysMessage);
        List<SysMessage> sysMessageList = sysMessageMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(sysMessageList)) return null;
        return sysMessageList.get(0);
    }

    @Override
    public int add(SysMessage sysMessage) {
        return sysMessageMapper.insertSelective(sysMessage);
    }

    @Override
    public int update(SysMessage sysMessage) {
        return sysMessageMapper.updateById(sysMessage);
    }

    @Override
    public int delete(Long id) {
        return sysMessageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer batchDelete(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) return 0;
        int count = 0;
        for(Long id : ids) {
            count += delete(id);
        }
        return count;
    }


    void setCriteria(SysMessageExample.Criteria criteria, SysMessage sysMessage) {
        if(sysMessage != null) {
            if(sysMessage.getId() != null) criteria.andIdEqualTo(sysMessage.getId());
            if(StringUtils.isNotBlank(sysMessage.getTitle())) criteria.andTitleLike("%" + sysMessage.getTitle() + "%");
            if(sysMessage.getStatus() != null) criteria.andStatusEqualTo(sysMessage.getStatus());
            if(sysMessage.getMchStatus() != null) criteria.andMchStatusEqualTo(sysMessage.getMchStatus());
            if(sysMessage.getAgentStatus() != null) criteria.andAgentStatusEqualTo(sysMessage.getAgentStatus());
            if(sysMessage.getIsvStatus() != null) criteria.andIsvStatusEqualTo(sysMessage.getIsvStatus());
        }
    }
}
