package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: pangxiaoyu
 * @date: 19/08/22
 * @description: 商户优惠券
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/coupon")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchCouponController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchCouponController.class);

    /**
     * 商户优惠券列表
     * @return
     */
    @MethodLog( remark = "商户优惠券列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        // 优惠券列表
        MchCoupon coupon = getObject( MchCoupon.class);
        coupon.setMchId(getUser().getBelongInfoId());
        IPage<MchCoupon> mchCouponList = rpcCommonService.rpcMchCouponService.list(coupon, getIPage(), null);

        // 统计核销张数
        List checkList = new LinkedList();
        for (MchCoupon mchCoupon : mchCouponList.getRecords()) {
            LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
            lambda.eq(MemberCoupon::getMchId, getUser().getBelongInfoId());
            lambda.eq(MemberCoupon::getStatus, MchConstant.PUB_YES);
            lambda.eq(MemberCoupon::getCouponId, mchCoupon.getCouponId());
            int count = rpcCommonService.rpcMemberCouponService.count(lambda);
            JSONObject json = (JSONObject) JSONObject.toJSON(mchCoupon);
            json.put("checkCount", count);
            checkList.add(json);
        }
        return ResponseEntity.ok(PageRes.buildSuccess(checkList));
    }

    /**
     * 商户优惠券信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long couponId = getValLongRequired( "couponId");
        MchCoupon coupon = rpcCommonService.rpcMchCouponService.getOne(
                new QueryWrapper<MchCoupon>().lambda()
                .eq(MchCoupon::getCouponId, couponId)
                .eq(MchCoupon::getMchId, getUser().getBelongInfoId())
        );
        // 转换成json
        JSONObject json = JsonUtil.getJSONObjectFromObj(coupon);
        //获取设置门店对应的门店ID
        ArrayList<Long> list = new ArrayList<>();
        list.add(couponId);
        String storeIds = "";
        Collection<MchCouponStoreRela> listByIds = rpcCommonService.rpcMchCouponStoreRelaService.listByIds(list);
        if (listByIds.size() != 0){
            for (MchCouponStoreRela rela: listByIds) {
                storeIds += rela.getStoreId().toString() + ",";
            }
            json.put("storeIds", storeIds.substring(0, storeIds.length() - 1));
        }
        String useTimeConfig = coupon.getUseTimeConfig();
        JSONObject configJson = JsonUtil.getJSONObjectFromJson(useTimeConfig);
        json.put("weekDays", configJson.getString("week"));
        json.put("useTime", configJson.getString("time"));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));
    }

    /**
     * 新增商户优惠券
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {
        MchCoupon mchCoupon = getObject(MchCoupon.class);
        String weekDays = getValString("weekDays");
        String useTime = getValString("useTime");
        JSONObject json = new JSONObject();
        json.put("week", weekDays);
        json.put("time", useTime);
        mchCoupon.setUseTimeConfig(json.toString());
        // 判断有效期类型是否为领取后天数
        if (mchCoupon.getValidateType() == MchConstant.MEMBER_COUPON_VALIDATE_DAY) {
            if (mchCoupon.getValidateDay() == null){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_VALIDATE_DAY_ERROR));
            }
        }
        // 判断领取数量是否为空
        if (mchCoupon.getSingleUserLimit() == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_SINGLE_USER_LIMIT_ERROR));
        }
        // 判断过期提醒天数是否为空
        if (mchCoupon.getExpiredWarningTime() == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_EXPIRED_WARNING_TIME_ERROR));
        }
        // 判断优惠券面额和最低消费金额是否合理
        if(mchCoupon.getCouponAmount().longValue() >= mchCoupon.getPayAmountLimit()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_AMOUNT_ERROR));
        }
        mchCoupon.setMchId(getUser().getBelongInfoId());
        mchCoupon.setReceiveNum(0); //已领取数量初始化
        // 保存
        boolean result = rpcCommonService.rpcMchCouponService.save(mchCoupon);

        // 如果限制门店增加门店与优惠券关联
        if (mchCoupon.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            String storeIds = getValStringRequired( "storeIds");
            String[] split = storeIds.split(",");
            MchCouponStoreRela rela = new MchCouponStoreRela();
            rela.setCouponId(mchCoupon.getCouponId());
            for (String s : split) {
                rela.setStoreId(Long.valueOf(s));
                rpcCommonService.rpcMchCouponStoreRelaService.save(rela);
            }
        }
        if(result) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 商户优惠券信息修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchCoupon mchCoupon = getObject(MchCoupon.class);
        // 查看数据库所属商户ID与当前操作ID是否相同
        MchCoupon coupon = rpcCommonService.rpcMchCouponService.getById(mchCoupon.getCouponId());
        if (coupon == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        if (!getUser().getBelongInfoId().equals(coupon.getMchId())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        // 判断有效期类型是否为领取后天数
        if (mchCoupon.getValidateType() == MchConstant.MEMBER_COUPON_VALIDATE_DAY) {
            if (mchCoupon.getValidateDay() == null){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_VALIDATE_DAY_ERROR));
            }
        }else {
            mchCoupon.setValidateDay(-1);
        }
        // 判断领取数量是否为空
        if (mchCoupon.getSingleUserLimit() == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_SINGLE_USER_LIMIT_ERROR));
        }
        // 判断过期提醒天数是否为空
        if (mchCoupon.getExpiredWarningTime() == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_EXPIRED_WARNING_TIME_ERROR));
        }
        // 面值与最低消费不可更改
        mchCoupon.setCouponAmount(coupon.getCouponAmount());
        mchCoupon.setPayAmountLimit(coupon.getPayAmountLimit());
        // 如果限制门店则执行门店与商品关联；如果不限制则移除关联执行更新
        Boolean result;
        if (mchCoupon.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            String storeIds = getValStringRequired( "storeIds");
            result = rpcCommonService.rpcMchCouponService.updateCoupon(mchCoupon, storeIds);
        }else {
            rpcCommonService.rpcMchCouponStoreRelaService.removeById(coupon.getCouponId());
            result = rpcCommonService.rpcMchCouponService.updateById(mchCoupon);
        }

        if(result) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
