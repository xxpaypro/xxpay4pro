package org.xxpay.mch.shoppingMall.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchStoreTakeout;
import org.xxpay.mch.common.ctrl.BaseController;

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
    @MethodLog( remark = "配送信息" )
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        MchStoreTakeout mchGoodsProps = rpcCommonService.rpcMchStoreTakeoutService.getOne(
                new QueryWrapper<MchStoreTakeout>().lambda()
                        .eq(MchStoreTakeout::getMchId, getUser().getBelongInfoId())
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchGoodsProps));
    }

    /**
     * 新增
     */
    @MethodLog( remark = "新增" )
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {
        MchStoreTakeout mchStoreTakeout = getObject( MchStoreTakeout.class);
        mchStoreTakeout.setMchId(getUser().getBelongInfoId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        mchStoreTakeout.setIsvId(mchInfo.getIsvId());
        boolean result = rpcCommonService.rpcMchStoreTakeoutService.saveOrUpdate(mchStoreTakeout);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }
}
