package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchStoreAreaManage;
import org.xxpay.mbr.common.ctrl.BaseController;

/**
 * <p>
 * 商户门店区域信息表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchStoreArea")
public class MchStoreAreaManageController extends BaseController {

    /**
     * 获取区域信息
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired("storeAreaId");

        MchStoreAreaManage mchStoreAreaManage = rpcCommonService.rpcMchStoreAreaManageService.getOne(
                new QueryWrapper<MchStoreAreaManage>().lambda()
                        .eq(MchStoreAreaManage::getMchId, getUser().getMchId())
                        .eq(MchStoreAreaManage::getId, id)
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchStoreAreaManage));
    }

}
