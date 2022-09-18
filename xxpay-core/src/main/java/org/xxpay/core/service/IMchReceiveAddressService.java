package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchReceiveAddress;

/**
 * <p>
 * 收货地址表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchReceiveAddressService extends IService<MchReceiveAddress> {

    /**
     * 收货地址列表
     * @param mchReceiveAddress
     * @param page
     * @return
     */
    IPage<MchReceiveAddress> list(MchReceiveAddress mchReceiveAddress, IPage page);

}
