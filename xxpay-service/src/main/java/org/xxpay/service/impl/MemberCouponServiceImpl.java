package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MemberSeq;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.core.service.IMchCouponStoreRelaService;
import org.xxpay.core.service.IMemberCouponService;
import org.xxpay.service.dao.mapper.MchCouponMapper;
import org.xxpay.service.dao.mapper.MemberCouponMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员优惠券领取记录表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberCouponServiceImpl extends ServiceImpl<MemberCouponMapper, MemberCoupon> implements IMemberCouponService {

    @Autowired
    private MemberCouponMapper memberCouponMapper;

    @Autowired
    private MchCouponServiceImpl mchCouponService;

    @Autowired
    private MchCouponMapper mchCouponMapper;

    @Autowired
    private IMchCouponStoreRelaService mchCouponStoreRelaService;

    private static final MyLog _log = MyLog.getLog(MemberCouponServiceImpl.class);

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public Boolean checkCoupon(MemberCoupon memberCoupon) {
        Date validateEnd = memberCoupon.getValidateEnd();
        if (validateEnd.compareTo(new Date()) < 0){
            _log.info("优惠券超过有效期, validateEnd={}", validateEnd);
            return false;
        }
        memberCoupon.setStatus(MchConstant.MEMBER_COUPON_USED);
        int result = memberCouponMapper.updateById(memberCoupon);
        if (result == 1) {
            _log.info("优惠券核销成功");
            return true;
        }
        return false;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int addMbrCoupon(MchCoupon mchCoupon, Long memberId, Long mchId) {

        //商户优惠券数量减1，行锁
        MchCoupon updateRecord = new MchCoupon();
        updateRecord.setCouponId(mchCoupon.getCouponId());
        updateRecord.setReceiveNum(1);//领取一张
        if(mchCouponMapper.updateReceiveNumByCouponId(updateRecord) <= 0){
            throw new ServiceException(RetEnum.RET_MBR_COUPON_GET_OVER);
        }
        MchCoupon dbMchCoupon = mchCouponMapper.selectById(mchCoupon.getCouponId());
        if (dbMchCoupon.getTotalNum() - dbMchCoupon.getReceiveNum() < 0) {
            throw new ServiceException(RetEnum.RET_MBR_COUPON_GET_OVER);
        }

        int count = 0;
        //如果剩余张数为0，更新状态为已领完
        if (dbMchCoupon.getTotalNum() - dbMchCoupon.getReceiveNum() == 0) {
            MchCoupon updateRecord2 = new MchCoupon();
            updateRecord2.setCouponId(mchCoupon.getCouponId());
            updateRecord2.setStatus(MchConstant.MCH_COUPON_STATUS_END);
            count = mchCouponMapper.updateById(updateRecord2);
            if (count != 1) return 0;
        }

        //计算有效期
        Date validateEnd = null;
        if (mchCoupon.getValidateType() == MchConstant.MCH_COUPON_VALIDATE_TYPE_DAY) {
            String validateEndStr = DateUtil.date2Str(DateUtil.addDay(new Date(), mchCoupon.getValidateDay()), "yyyy-MM-dd") + " 23:59:59";
            validateEnd = DateUtil.str2date(validateEndStr);
        } else if (mchCoupon.getValidateType() == MchConstant.MCH_COUPON_VALIDATE_TYPE_TIME) {
            validateEnd = mchCoupon.getEndTime();
        }

        //判断是否过期
        if (validateEnd.getTime() < new Date().getTime()){
            throw new ServiceException(RetEnum.RET_MBR_COUPON_OUT_TIME);
        }

        String couponNo = MemberSeq.getCouponNo();  //优惠券核销码

        MemberCoupon record = new MemberCoupon();
        record.setCouponId(mchCoupon.getCouponId());
        record.setMemberId(memberId);
        record.setMchId(mchId);
        record.setStatus(MchConstant.MEMBER_COUPON_NOT_USED);
        record.setCouponNo(couponNo);
        record.setValidateEnd(validateEnd);
        count = baseMapper.insert(record);
        if (count != 1) return 0;
        return 1;
    }

    @Override
    public JSONArray selectMemberReceiveCouponList(Long memberId, Long currentMchId, Long currentStoreId, Long payAmount){

        JSONArray result = new JSONArray();

        //查询到该会员领取的所有优惠券列表
        List<MchCoupon> allList = mchCouponMapper.selectMemberCanUseCoupon(memberId, currentMchId);
        for (MchCoupon itemCoupon : allList) {

            JSONObject itemCouponJSON = new JSONObject();
            itemCouponJSON.put("couponNo", itemCoupon.getPsStringVal("couponNo"));  //优惠券核销码
            itemCouponJSON.put("validateEnd", itemCoupon.getPsDateVal("validateEnd"));  //优惠券有效期至
            itemCouponJSON.put("couponId", itemCoupon.getCouponId());  //优惠券ID
            itemCouponJSON.put("couponName", itemCoupon.getCouponName());  //优惠券名称
            itemCouponJSON.put("couponColor", itemCoupon.getCouponColor());  //优惠券配色
            itemCouponJSON.put("logoImgPath", itemCoupon.getLogoImgPath());  //优惠券logo
            itemCouponJSON.put("couponAmount", itemCoupon.getCouponAmount());  //优惠券面值
            itemCouponJSON.put("payAmountLimit", itemCoupon.getPayAmountLimit());  //最低消费金额
            itemCouponJSON.put("couponDesc", "满"+ AmountUtil.convertCent2Dollar(itemCoupon.getPayAmountLimit()) +"元减"+AmountUtil.convertCent2Dollar(itemCoupon.getCouponAmount())+"元");  //优惠券描述信息
            itemCouponJSON.put("usableFlag", true); //可用

            //不可用描述信息
            String unUsableMsg = this.useCouponPreCheck(itemCoupon, payAmount, currentStoreId);
            if(unUsableMsg != null){
                itemCouponJSON.put("usableFlag", false); //不可用
                itemCouponJSON.put("unUsableMsg", unUsableMsg); //不可用描述信息
            }

            result.add(itemCouponJSON);
        }

        return result;
    }

    @Override
    public boolean use(String couponNo) {

        return this.update(new UpdateWrapper<MemberCoupon>().lambda()
                .eq(MemberCoupon::getCouponNo, couponNo)
//                .eq(MemberCoupon::getStatus, MchConstant.PUB_NO)
                .set(MemberCoupon::getStatus, MchConstant.PUB_YES)
        );
    }


    //使用限制
//            {
//                weekLimit: [1,2,3]        //限制使用周， 仅周几可用
//                timeLimit: ["10:00:00_12:00:00", "15:00:00_16:00:00"]  // 限制使用时间段， 仅[10点到12点， 15点到16点] 可用
//                dayLimit:["2019-01-01", "2019-02-02", "2019-03-03"] //限制使用日期， 仅2019-01-01和2019-03-04可用
//            }
    @Override
    public String useCouponPreCheck(MchCoupon useCoupon, Long payAmount, Long currentStoreId){

        //金额不满足
        if(payAmount != null && useCoupon.getPayAmountLimit() > payAmount){
            return "支付金额不满足条件";
        }

        //使用限制JSON格式
        JSONObject configJSON = JSONObject.parseObject(useCoupon.getUseTimeConfig());

        // [周] 限制
        JSONArray weekLimit = configJSON.getJSONArray("weekLimit");
        if(weekLimit != null && !weekLimit.isEmpty()){ //限制日期
            int currentWeek = DateUtil.getCurrentWeek();
            if(! weekLimit.toJavaList(Integer.class).contains(currentWeek)){
                return "使用日期不满足条件";
            }
        }

        // [ 时间段] 限制
        JSONArray timeLimit = configJSON.getJSONArray("timeLimit");
        if(timeLimit != null && !timeLimit.isEmpty()){ //限制日期
            Long currentTimestamp = DateUtil.str2date(DateUtil.getCurrentTimeStr("1970-01-01 HH-mm-ss")).getTime();

            for(String str: timeLimit.toJavaList(String.class)){
                String[] arr = str.split("_");
                Long beginTime = DateUtil.str2date(DateUtil.getCurrentTimeStr("1970-01-01 " + arr[0])).getTime();
                Long endTime = DateUtil.str2date(DateUtil.getCurrentTimeStr("1970-01-01 " + arr[0])).getTime();
                if(currentTimestamp < beginTime || currentTimestamp > endTime){
                    return "使用日期不满足条件";
                }
            }
        }

        // [ 指定日期] 限制
        JSONArray dayLimit = configJSON.getJSONArray("dayLimit");
        if(dayLimit != null && !dayLimit.isEmpty()){ //限制日期
            String currentDay = DateUtil.getCurrentTimeStr("yyyy-MM-dd");
            if(! weekLimit.toJavaList(String.class).contains(currentDay)){
                return "使用日期不满足条件";
            }
        }

        //查询门店限制
        if(useCoupon.getStoreLimitType() == MchConstant.PUB_YES){ //限制门店

            if(currentStoreId == null){  //当前门店ID为null
                return "适用门店不满足条件";
            }

            if( !mchCouponStoreRelaService.canUseByStoreId(useCoupon.getCouponId(), currentStoreId)) {
                return "适用门店不满足条件";
            }
        }

        return null;
    }

}
