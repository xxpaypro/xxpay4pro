package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchReceiveAddress;
import org.xxpay.core.service.IMchReceiveAddressService;
import org.xxpay.service.dao.mapper.MchReceiveAddressMapper;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchReceiveAddressServiceImpl extends ServiceImpl<MchReceiveAddressMapper, MchReceiveAddress> implements IMchReceiveAddressService {

    @Override
    public IPage<MchReceiveAddress> list(MchReceiveAddress mchReceiveAddress, IPage page) {
        LambdaQueryWrapper<MchReceiveAddress> lambda = new QueryWrapper<MchReceiveAddress>().lambda();
        lambda.orderByDesc(MchReceiveAddress::getCreateTime);
        setCriteria(lambda, mchReceiveAddress);
        return page(page, lambda);
    }

    void setCriteria(LambdaQueryWrapper<MchReceiveAddress> lambda, MchReceiveAddress mchReceiveAddress) {
        if(mchReceiveAddress != null) {
            if (mchReceiveAddress.getMemberId() != null) lambda.eq(MchReceiveAddress::getMemberId, mchReceiveAddress.getMemberId());
            if (mchReceiveAddress.getAddressId() != null) lambda.eq(MchReceiveAddress::getAddressId, mchReceiveAddress.getAddressId());
            if (mchReceiveAddress.getIsDefaultAddress() != null) lambda.eq(MchReceiveAddress::getIsDefaultAddress, mchReceiveAddress.getIsDefaultAddress());
            if (mchReceiveAddress.getMchId() != null) lambda.eq(MchReceiveAddress::getMchId, mchReceiveAddress.getMchId());
            if (mchReceiveAddress.getIsvId() != null) lambda.eq(MchReceiveAddress::getIsvId, mchReceiveAddress.getIsvId());
        }
    }

}
