package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchStoreBanner;

import java.util.Date;

/**
 * <p>
 * 商户小程序轮播图配置表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-27
 */
public interface IMchStoreBannerService extends IService<MchStoreBanner> {

    /**
     * 轮播图配置列表
     * @param mchStoreBanner
     * @param page
     * @param currentDate
     * @return
     */
    IPage<MchStoreBanner> list(MchStoreBanner mchStoreBanner, IPage page, Date currentDate);

    /**
     * 存入轮播图及关联
     * @param storeIds
     * @param mchStoreBanner
     * @return
     */
    boolean saveBanner(String storeIds, MchStoreBanner mchStoreBanner);

    /**
     * 更新轮播图及关联
     * @param storeIds
     * @param mchStoreBanner
     * @return
     */
    boolean updateBanner(String storeIds, MchStoreBanner mchStoreBanner);
}
