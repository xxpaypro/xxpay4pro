package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.core.service.IMchCouponService;
import org.xxpay.service.dao.mapper.MchCouponMapper;
import org.xxpay.service.dao.mapper.MchCouponStoreRelaMapper;
import org.xxpay.service.dao.mapper.MchStoreMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 商户优惠券表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MchCouponServiceImpl extends ServiceImpl<MchCouponMapper, MchCoupon> implements IMchCouponService {

    @Autowired
    private MchCouponMapper mchCouponMapper;

    @Autowired
    private MchCouponStoreRelaMapper mchCouponStoreRelaMapper;

    @Autowired
    private MchStoreMapper mchStoreMapper;

    public static final MyLog logger = MyLog.getLog(MchCouponServiceImpl.class);

    @Override
    public IPage<MchCoupon> list(MchCoupon mchCoupon, IPage page, Date currentDate) {
        LambdaQueryWrapper<MchCoupon> lambda = new QueryWrapper<MchCoupon>().lambda();
        lambda.orderByDesc(MchCoupon::getCreateTime);
        setCriteria(lambda, mchCoupon, currentDate);
        return page(page, lambda);
    }

    /**
     * 更新优惠券并关联门店
     * @param mchCoupon
     * @param storeIds
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public Boolean updateCoupon(MchCoupon mchCoupon, String storeIds) {
        //更新优惠券与门店关联
        mchCouponStoreRelaMapper.deleteById(mchCoupon.getCouponId());
        String[] split = storeIds.split(",");
        MchCouponStoreRela rela = new MchCouponStoreRela();
        rela.setCouponId(mchCoupon.getCouponId());
        for (String s: split) {
            rela.setStoreId(Long.valueOf(s));
            mchCouponStoreRelaMapper.insert(rela);
        }
        //更新优惠券信息
        int i = mchCouponMapper.updateById(mchCoupon);
        if (i != 1) return false;
        return true;
    }

    @Override
    public int checkCoupon(MchCoupon mchCoupon, List<MemberCoupon> memberCouponList, List<MchCouponStoreRela> relaList) {
        //判断库存数量限制
        if (mchCoupon.getTotalNum() - mchCoupon.getReceiveNum() <= 0) {
            return 0;
        }

        //单用户领取数量限制
        if (mchCoupon.getSingleUserLimit() != MchConstant.MCH_COUPON_NO_SINGLEUSERLIMIT
                && memberCouponList != null && memberCouponList.size() > 0) {

            //用户已领取数量
            int mbrCouponCount = 0;
            for (int i = 0; i<memberCouponList.size(); i++){
                if (memberCouponList.get(i).getCouponId().equals(mchCoupon.getCouponId())){
                    mbrCouponCount++;
                }
            }
            //限制数量大于等于已领取数量，不能再领
            logger.info("限制数量：{}，已领取数量：{}", mchCoupon.getSingleUserLimit(), mbrCouponCount);
            if (mchCoupon.getSingleUserLimit() <= mbrCouponCount) {
                return 0;
            }
        }

        //门店限制
        if(mchCoupon.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            //根据门店查询的关联表
            if (relaList == null && relaList.size() <= 0) {
                return 0;
            }

            //关联表couponId集合
            List list = new ArrayList();
            for (int i = 0; i<relaList.size(); i++){
                list.add(relaList.get(i).getCouponId());
            }
            if(!list.contains(mchCoupon.getCouponId())){
                return 0;
            }
        }

        return 1;
    }

    void setCriteria(LambdaQueryWrapper<MchCoupon> lambda, MchCoupon mchCoupon, Date currentDate) {
        if(mchCoupon != null) {
            if (mchCoupon.getCouponId() != null) lambda.eq(MchCoupon::getCouponId, mchCoupon.getCouponId());
            if (StringUtils.isNotEmpty(mchCoupon.getCouponName())) lambda.like(MchCoupon::getCouponName, "%" + mchCoupon.getCouponName() + "%");
            if (mchCoupon.getStatus() != null && mchCoupon.getStatus() != -99) lambda.eq(MchCoupon::getStatus, mchCoupon.getStatus());
            if (mchCoupon.getValidateType() != null) lambda.eq(MchCoupon::getValidateType, mchCoupon.getValidateType());
            if (mchCoupon.getStoreLimitType() != null) lambda.eq(MchCoupon::getStoreLimitType, mchCoupon.getStoreLimitType());
            if (mchCoupon.getSyncWX() != null) lambda.eq(MchCoupon::getSyncWX, mchCoupon.getSyncWX());
            if (mchCoupon.getMchId() != null) lambda.eq(MchCoupon::getMchId, mchCoupon.getMchId());
            if (currentDate != null){
                lambda.le(MchCoupon::getBeginTime, currentDate);
                lambda.ge(MchCoupon::getEndTime, currentDate);
            }
        }
    }
}
