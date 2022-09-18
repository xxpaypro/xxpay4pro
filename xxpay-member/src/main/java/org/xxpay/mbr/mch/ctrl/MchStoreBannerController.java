package org.xxpay.mbr.mch.ctrl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.entity.MchStoreBanner;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

/**
 * <p>
 * 商户小程序轮播图配置表 前端控制器
 * </p>
 *
 * @since 2020-04-29
 */
@Controller
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/store_banner")
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
        mchStoreBanner.setMchId(getUser().getMchId());
        IPage<MchStoreBanner> mchCouponList = rpcCommonService.rpcMchStoreBannerService.list(mchStoreBanner, getIPage(), null);
        return ResponseEntity.ok(PageRes.buildSuccess(mchCouponList));
    }

}
