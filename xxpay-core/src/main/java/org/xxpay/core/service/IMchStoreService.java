package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchStore;

/**
 * <p>
 * 商户门店表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMchStoreService extends IService<MchStore> {

    /**
     * 当前登录商户下门店列表
     * @param mchStore
     * @param page
     * @return
     */
    IPage<MchStore> list(MchStore mchStore, IPage page);

}
