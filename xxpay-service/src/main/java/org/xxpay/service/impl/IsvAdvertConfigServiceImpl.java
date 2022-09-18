package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.IsvAdvertConfig;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.service.IIsvAdvertConfigService;
import org.xxpay.service.dao.mapper.IsvAdvertConfigMapper;

import java.util.List;

/**
 * <p>
 * 服务商广告配置表 服务实现类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-26
 */
@Service
public class IsvAdvertConfigServiceImpl extends ServiceImpl<IsvAdvertConfigMapper, IsvAdvertConfig> implements IIsvAdvertConfigService {

    @Autowired
    private IsvAdvertConfigMapper configMapper;

    @Override
    public IPage<IsvAdvertConfig> getList(IsvAdvertConfig config, Page page) {
        LambdaQueryWrapper<IsvAdvertConfig> lambda = new QueryWrapper<IsvAdvertConfig>().lambda();
        lambda.orderByDesc(IsvAdvertConfig::getCreateTime);
        setCriteria(lambda, config);
        return page(page, lambda);
    }

    @Override
    public JSONArray selectAdListByTradeOrder(Byte showType, MchTradeOrder mchTradeOrder) {

        if(mchTradeOrder == null) return null;
        List<IsvAdvertConfig> result = baseMapper.selectAdList(showType, mchTradeOrder.getIsvId(), mchTradeOrder.getAgentId(),
                mchTradeOrder.getProvinceCode(), mchTradeOrder.getCityCode(), mchTradeOrder.getAreaCode());
        return packageAdList(result);
    }

    @Override
    public JSONArray selectAdListByMch(Byte showType, MchInfo mchInfo) {

        if(mchInfo == null) return null;
        List<IsvAdvertConfig> result = baseMapper.selectAdList(showType, mchInfo.getIsvId(), mchInfo.getAgentId(),
                mchInfo.getProvinceCode(), mchInfo.getCityCode(), mchInfo.getAreaCode());
        return packageAdList(result);
    }


    /** 封装查询结果 */
    private JSONArray packageAdList(List<IsvAdvertConfig> list){
        JSONArray resultJSON = new JSONArray();
        list.stream().forEach(ad -> {
            JSONObject json = new JSONObject();
            json.put("adId", ad.getId());
            json.put("advertName", ad.getAdvertName());
            json.put("advertUrl", ad.getAdvertUrl());
            json.put("mediaType", ad.getMediaType());
            json.put("mediaPath", ad.getMediaPath());
            resultJSON.add(json);
        });
        return resultJSON;
    }

    void setCriteria(LambdaQueryWrapper<IsvAdvertConfig> lambda, IsvAdvertConfig config) {
        if(config != null) {
            if (config.getIsvId() != null) lambda.eq(IsvAdvertConfig::getIsvId, config.getIsvId());
            if (StringUtils.isNotEmpty(config.getAdvertName())) lambda.like(IsvAdvertConfig::getAdvertName, "%" + config.getAdvertName() + "%");
            if (config.getShowType() != null) lambda.eq(IsvAdvertConfig::getShowType, config.getShowType());
            if (config.getPutMch() != null && config.getPutMch() != -99) lambda.eq(IsvAdvertConfig::getPutMch, config.getPutMch());
            if (config.getStatus() != null) lambda.eq(IsvAdvertConfig::getStatus, config.getStatus());
            if (config.getEndTime() != null) lambda.ge(IsvAdvertConfig::getEndTime, config.getEndTime());
        }
    }

}
