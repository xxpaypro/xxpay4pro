package org.xxpay.mch.user.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchStoreBanner;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

/**
 * <p>
 * 商户小程序轮播图配置表 前端控制器
 * </p>
 *
 * @since 2020-04-29
 */
@Controller
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/store_banner")
public class MchStoreBannerController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商户优小程序轮播图配置列表
     * @return
     */
    @MethodLog( remark = "轮播图配置列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        // 轮播图列表
        MchStoreBanner mchStoreBanner = getObject(MchStoreBanner.class);
        mchStoreBanner.setMchId(getUser().getBelongInfoId());
        IPage<MchStoreBanner> mchCouponList = rpcCommonService.rpcMchStoreBannerService.list(mchStoreBanner, getIPage(), null);
        return ResponseEntity.ok(PageRes.buildSuccess(mchCouponList));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增小程序轮播图" )
    public ResponseEntity<?> add() {
        // 获取门店ID
        String storeIds = getValString("storeIds");
        MchStoreBanner mchStoreBanner = getObject(MchStoreBanner.class);
        mchStoreBanner.setMchId(getUser().getBelongInfoId());
        if (StringUtils.isBlank(mchStoreBanner.getBannerName())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_BANNER_NULL));
        }
        boolean result = rpcCommonService.rpcMchStoreBannerService.saveBanner(storeIds, mchStoreBanner);
        if(result){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "编辑小程序轮播图" )
    public ResponseEntity<?> update() {
        // 获取门店ID
        String storeIds = getValString("storeIds");
        MchStoreBanner mchStoreBanner = getObject(MchStoreBanner.class);
        mchStoreBanner.setMchId(getUser().getBelongInfoId());
        if (StringUtils.isBlank(mchStoreBanner.getBannerName())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STORE_BANNER_NULL));
        }
        boolean result = rpcCommonService.rpcMchStoreBannerService.updateBanner(storeIds, mchStoreBanner);
        if(result){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 商户优惠券信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long bannerId = getValLong("bannerId");
        MchStoreBanner mchStoreBanner = rpcCommonService.rpcMchStoreBannerService.getOne(
                new QueryWrapper<MchStoreBanner>().lambda()
                .eq(MchStoreBanner::getBannerId, bannerId)
                .eq(MchStoreBanner::getMchId, getUser().getBelongInfoId())
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchStoreBanner));
    }

    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除小程序轮播图" )
    public ResponseEntity<?> delete() {
        // 获取轮播图ID
        String bannerId = getValString("bannerId");
        // 删除轮播图配置
        boolean removeBanner = rpcCommonService.rpcMchStoreBannerService.removeById(bannerId);
        // 删除轮播图关联
        boolean removeRela = rpcCommonService.rpcMchStoreBannerRelaService.removeById(bannerId);
        if(removeBanner && removeRela){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
