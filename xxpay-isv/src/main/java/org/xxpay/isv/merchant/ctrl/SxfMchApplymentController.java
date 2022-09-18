package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SxfMchSnapshot;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

/** 随行付商户入驻接口  **/
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/sxf_mch_applyment")
public class SxfMchApplymentController extends BaseController {

    private static final MyLog logger = MyLog.getLog(SxfMchApplymentController.class);

    @Autowired
    private RpcCommonService rpc;

    /** 查询随行付进件信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {

        Long mchId = getValLongRequired("mchId");
        Long currentIsvId = getUser().getBelongInfoId();

        SxfMchSnapshot record = rpc.rpcSxfMchSnapshotService.findByMchAndIsv(mchId, currentIsvId);
        return XxPayResponse.buildSuccess(record);
    }



    /** 随行付商户进件申请 **/
    @RequestMapping("/applyment4sub")
    public XxPayResponse applyment4sub() {
        try {
            SxfMchSnapshot sxfMchSnapshot = getObject(SxfMchSnapshot.class);
            sxfMchSnapshot.setIsvId(getUser().getBelongInfoId());
            rpc.rpcXxPaySxfpayApiService.sxfMicroApply(sxfMchSnapshot);
            return XxPayResponse.buildSuccess();
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询商户申请审核状态 **/
    @RequestMapping("/querySubApplyStatus")
    public XxPayResponse querySubApplyStatus() {
        try {
            Long applyNo = getValLongRequired("applyNo");
            JSONObject result = rpc.rpcXxPaySxfpayApiService.sxfQueryMicroApply(applyNo);
            return XxPayResponse.buildSuccess(result);
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}