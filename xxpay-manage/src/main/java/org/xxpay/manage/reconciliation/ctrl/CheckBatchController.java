package org.xxpay.manage.reconciliation.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.CheckBatch;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bill/check_batch")
public class CheckBatchController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        CheckBatch checkBatch = rpcCommonService.rpcCheckService.findByBatchId(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(checkBatch));
    }

    /**
     * 记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        CheckBatch checkBatch = getObject( CheckBatch.class);
        int count = rpcCommonService.rpcCheckService.countCheckBatch(checkBatch);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<CheckBatch> checkBatchList = rpcCommonService.rpcCheckService.selectCheckBatch(
                (getPageIndex() -1) * getPageSize(), getPageSize(), checkBatch);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(checkBatchList, count));
    }

}
