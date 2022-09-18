package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.entity.MchReturnReason;
import org.xxpay.core.service.IMchReturnReasonService;
import org.xxpay.service.dao.mapper.MchReturnReasonMapper;

/**
 * <p>
 * 商户退货原因表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchReturnReasonServiceImpl extends ServiceImpl<MchReturnReasonMapper, MchReturnReason> implements IMchReturnReasonService {

    @Override
    public IPage<MchReturnReason> list(MchReturnReason mchReturnReason, Page page) {
        LambdaQueryWrapper<MchReturnReason> lambda = new QueryWrapper<MchReturnReason>().lambda();
        lambda.orderByDesc(MchReturnReason::getCreateTime);
        lambda.eq(MchReturnReason::getMchId, mchReturnReason.getMchId());
        lambda.eq(MchReturnReason::getStatus, MchConstant.PUB_YES);
        return page(page, lambda);
    }

}
