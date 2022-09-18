package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchShoppingCart;
import org.xxpay.core.service.IMchShoppingCartService;
import org.xxpay.service.dao.mapper.MchShoppingCartMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchShoppingCartServiceImpl extends ServiceImpl<MchShoppingCartMapper, MchShoppingCart> implements IMchShoppingCartService {

    @Override
    public List<Map> selectCartList(Integer offset, Integer limit, Byte industryType, Long memberId, Long mchId) {
        Map param = new HashMap<>();
        param.put("offset",offset);
        param.put("limit",limit);
        if(industryType != null) param.put("industryType", industryType);
        if(memberId != null) param.put("memberId", memberId);
        if(mchId != null) param.put("mchId", mchId);
        return baseMapper.selectCartList(param);
    }

    @Override
    public int count(Byte industryType, Long memberId, Long mchId) {
        Map param = new HashMap<>();
        if(industryType != null) param.put("industryType", industryType);
        if(memberId != null) param.put("memberId", memberId);
        if(mchId != null) param.put("mchId", mchId);
        return baseMapper.count(param);
    }
}
