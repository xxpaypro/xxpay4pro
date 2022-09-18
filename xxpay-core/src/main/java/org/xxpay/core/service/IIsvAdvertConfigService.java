package org.xxpay.core.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.IsvAdvertConfig;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchTradeOrder;

/**
 * <p>
 * 服务商广告配置表 服务类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-26
 */
public interface IIsvAdvertConfigService extends IService<IsvAdvertConfig> {

    IPage<IsvAdvertConfig> getList(IsvAdvertConfig config, Page<Object> objectPage);

    /** 根据订单查询广告列表 **/
    JSONArray selectAdListByTradeOrder(Byte showType, MchTradeOrder mchTradeOrder);

    /** 根据商户查询广告列表 **/
    JSONArray selectAdListByMch(Byte showType, MchInfo mchInfo);

}
