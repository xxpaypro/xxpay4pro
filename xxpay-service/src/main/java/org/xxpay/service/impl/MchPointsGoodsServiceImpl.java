package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MchPointsGoodsStoreRela;
import org.xxpay.core.entity.MemberGoodsExchange;
import org.xxpay.core.service.IMchPointsGoodsService;
import org.xxpay.service.dao.mapper.MchPointsGoodsMapper;
import org.xxpay.service.dao.mapper.MchPointsGoodsStoreRelaMapper;
import org.xxpay.service.dao.mapper.MemberGoodsExchangeMapper;

import java.util.Date;

/**
 * <p>
 * 商户积分商品表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MchPointsGoodsServiceImpl extends ServiceImpl<MchPointsGoodsMapper, MchPointsGoods> implements IMchPointsGoodsService {

    @Autowired
    private MemberGoodsExchangeMapper memberGoodsExchangeMapper;

    @Autowired
    private MchPointsGoodsMapper mchPointsGoodsMapper;

    @Autowired
    private MchPointsGoodsStoreRelaMapper mchPointsGoodsStoreRelaMapper;

    private static final MyLog _log = MyLog.getLog(MchPointsGoodsServiceImpl.class);

    @Override
    public IPage<MchPointsGoods> list(MchPointsGoods mchPointsGoods, Page page) {
        LambdaQueryWrapper<MchPointsGoods> lambda = new QueryWrapper<MchPointsGoods>().lambda();
        lambda.orderByDesc(MchPointsGoods::getCreateTime);
        setCriteria(lambda, mchPointsGoods);
        return page(page, lambda);
    }

    /**
     * 核销积分商品
     * @param exchange
     * @param operatorId
     * @param operatorName
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public boolean checkPointGoods(MemberGoodsExchange exchange, Long operatorId, String operatorName) {

        //积分商品兑换表修改状态-已兑换、兑换时间，并修改
        exchange.setStatus(MchConstant.INTEGRAL_COMMODITY_ALREADY_EXCHANGE);
        exchange.setExchangeTime(new Date());
        exchange.setOperatorId(operatorId+"");
        exchange.setOperatorName(operatorName);
        memberGoodsExchangeMapper.updateById(exchange);
        _log.info("积分商品核销成功");
        return true;
    }

    /**
     * 更新积分商品信息
     * @param mchPointsGoods
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public boolean updatePointsGoods(MchPointsGoods mchPointsGoods, String storeIds) {
        //如果限制门店，删除商品下关联并重新关联商品与门店ID
        mchPointsGoodsStoreRelaMapper.deleteById(mchPointsGoods.getGoodsId());
        String[] split = storeIds.split(",");
        MchPointsGoodsStoreRela rela = new MchPointsGoodsStoreRela();
        rela.setGoodsId(mchPointsGoods.getGoodsId());
        for (String s: split) {
            rela.setStoreId(Long.valueOf(s));
            mchPointsGoodsStoreRelaMapper.insert(rela);
        }
        //更新积分商品信息
        int i = mchPointsGoodsMapper.updateById(mchPointsGoods);
        if (i != 1) return false;
        return true;
    }

    void setCriteria(LambdaQueryWrapper<MchPointsGoods> lambda, MchPointsGoods mchPointsGoods) {
        if(mchPointsGoods != null) {
            if (mchPointsGoods.getGoodsId() != null) lambda.eq(MchPointsGoods::getGoodsId, mchPointsGoods.getGoodsId());
            if (mchPointsGoods.getMchId() != null) lambda.eq(MchPointsGoods::getMchId, mchPointsGoods.getMchId());
            if (StringUtils.isNotEmpty(mchPointsGoods.getGoodsName())) lambda.like(MchPointsGoods::getGoodsName, "%" + mchPointsGoods.getGoodsName() + "%");
            if (mchPointsGoods.getPoints() != null) lambda.eq(MchPointsGoods::getPoints, mchPointsGoods.getPoints());
            if (mchPointsGoods.getStatus() != null) lambda.eq(MchPointsGoods::getStatus, mchPointsGoods.getStatus());
            if (mchPointsGoods.getStockLimitType() != null) lambda.eq(MchPointsGoods::getStockLimitType, mchPointsGoods.getStockLimitType());
            if (mchPointsGoods.getStockNum() != null) lambda.eq(MchPointsGoods::getStockNum, mchPointsGoods.getStockNum());
            if (mchPointsGoods.getSingleMemberLimit() != null) lambda.eq(MchPointsGoods::getSingleMemberLimit, mchPointsGoods.getSingleMemberLimit());
            if (mchPointsGoods.getStoreLimitType() != null) lambda.eq(MchPointsGoods::getStockLimitType, mchPointsGoods.getStoreLimitType());
            if (mchPointsGoods.getBeginTime() != null) lambda.ge(MchPointsGoods::getBeginTime, mchPointsGoods.getBeginTime());
            if (mchPointsGoods.getEndTime() != null) lambda.le(MchPointsGoods::getEndTime, mchPointsGoods.getEndTime());
        }
    }

}
