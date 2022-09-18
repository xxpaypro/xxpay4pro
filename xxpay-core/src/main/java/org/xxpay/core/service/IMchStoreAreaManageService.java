package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchStoreAreaManage;

/**
 * <p>
 * 商户餐饮店区域管理表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchStoreAreaManageService extends IService<MchStoreAreaManage> {

    IPage<MchStoreAreaManage> list(MchStoreAreaManage mchStoreAreaManage, IPage iPage);
}
