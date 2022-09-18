package org.xxpay.isv.merchant.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchStore;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;

/**
 * 门店Ctrl
 */
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/mch_store")
public class MchStoreController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchStoreController.class);

    /**  门店列表  **/
    @RequestMapping("/listAll")
    public XxPayResponse listAll() {

        Long mchId = getValLongRequired("mchId");
        LambdaQueryWrapper<MchStore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchStore::getMchId, mchId);
        queryWrapper.select(MchStore::getStoreId, MchStore::getStoreName);

        List<MchStore> mchStoreList = rpcCommonService.rpcMchStoreService.list(queryWrapper);
        return XxPayResponse.buildSuccess(mchStoreList);
    }

}
