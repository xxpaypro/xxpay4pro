package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.entity.MchReturnReason;
import org.xxpay.mbr.common.ctrl.BaseController;

/**
 * <p>
 * 商户退货原因表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchReturnReason")
public class MchReturnReasonController extends BaseController {

    /**
     * 退户原因列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(Integer page, Integer limit) {
        MchReturnReason mchReturnReason = new MchReturnReason();
        mchReturnReason.setMchId(getUser().getMchId());
        IPage<MchReturnReason> mchReturnReasonList = rpcCommonService.rpcMchReturnReasonService.list(mchReturnReason, new Page<>(page, limit));
        return ResponseEntity.ok(PageRes.buildSuccess(mchReturnReasonList));
    }

}
