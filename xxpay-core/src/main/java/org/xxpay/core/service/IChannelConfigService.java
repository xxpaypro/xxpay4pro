package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.ChannelConfig;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/14
 * @description:
 */
public interface IChannelConfigService extends IService<ChannelConfig> {

    int add(ChannelConfig channelConfig);

    int update(ChannelConfig channelConfig);

    List<ChannelConfig> select(int pageIndex, int pageSize, ChannelConfig channelConfig);

    Integer count(ChannelConfig channelConfig);

    ChannelConfig findById(int id);

    ChannelConfig findByChannelId(String channelId);

}
