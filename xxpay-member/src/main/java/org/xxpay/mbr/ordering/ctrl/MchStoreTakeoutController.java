package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchStoreTakeout;
import org.xxpay.mbr.common.ctrl.BaseController;

/**
 * <p>
 * 商户餐饮店营业及配送信息表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchStoreTakeout")
public class MchStoreTakeoutController extends BaseController {

    /**
     * 获取配送信息
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        MchStoreTakeout mchStoreTakeout = rpcCommonService.rpcMchStoreTakeoutService.getOne(
                new QueryWrapper<MchStoreTakeout>().lambda()
                        .eq(MchStoreTakeout::getMchId, getUser().getMchId())
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchStoreTakeout));
    }

}
