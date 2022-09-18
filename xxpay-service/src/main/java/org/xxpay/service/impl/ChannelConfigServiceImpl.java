package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.ChannelConfig;
import org.xxpay.core.entity.ChannelConfigExample;
import org.xxpay.core.service.IChannelConfigService;
import org.xxpay.service.dao.mapper.ChannelConfigMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/14
 * @description:
 */
@Service
public class ChannelConfigServiceImpl extends ServiceImpl<ChannelConfigMapper, ChannelConfig> implements IChannelConfigService {

    @Autowired
    private ChannelConfigMapper channelConfigMapper;

    @Override
    public int add(ChannelConfig channelConfig) {
        return channelConfigMapper.insertSelective(channelConfig);
    }

    @Override
    public int update(ChannelConfig channelConfig) {
        return channelConfigMapper.updateByPrimaryKeySelective(channelConfig);
    }

    @Override
    public List<ChannelConfig> select(int pageIndex, int pageSize, ChannelConfig channelConfig) {
        ChannelConfigExample example = new ChannelConfigExample();
        example.setOrderByClause("channelId ASC, createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        ChannelConfigExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, channelConfig);
        return channelConfigMapper.selectByExample(example);
    }

    @Override
    public Integer count(ChannelConfig channelConfig) {
        ChannelConfigExample example = new ChannelConfigExample();
        ChannelConfigExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, channelConfig);
        return channelConfigMapper.countByExample(example);
    }

    @Override
    public ChannelConfig findById(int id) {
        return channelConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public ChannelConfig findByChannelId(String channelId) {
        ChannelConfigExample example = new ChannelConfigExample();
        ChannelConfigExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        criteria.andStatusEqualTo(MchConstant.PUB_YES);
        List<ChannelConfig> channelConfigList = channelConfigMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(channelConfigList)) return null;
        return channelConfigList.get(0);
    }

    void setCriteria(ChannelConfigExample.Criteria criteria, ChannelConfig channelConfig) {
        if(channelConfig != null) {
            if(StringUtils.isNotBlank(channelConfig.getChannelId())) criteria.andChannelIdEqualTo(channelConfig.getChannelId());
            if(StringUtils.isNotBlank(channelConfig.getChannelType())) criteria.andChannelTypeEqualTo(channelConfig.getChannelType());
        }
    }

}
