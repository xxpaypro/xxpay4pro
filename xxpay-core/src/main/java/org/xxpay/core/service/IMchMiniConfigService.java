package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchMiniConfig;

/**
 * <p>
 * 商户小程序配置表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-18
 */
public interface IMchMiniConfigService extends IService<MchMiniConfig> {

    /**
     * 小程序可视化保存
     * @param mchId
     * @param componentList
     * @param componentIndex
     * @param authFrom
     * @param isDraft
     * @return
     */
    int visualableSaveOrUpdate(Long mchId, String componentList, String componentIndex, Byte authFrom, Byte isDraft);
}
