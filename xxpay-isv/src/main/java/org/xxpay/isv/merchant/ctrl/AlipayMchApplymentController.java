package org.xxpay.isv.merchant.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.AlipayMchSnapshot;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;

/** 支付宝商户入驻接口  **/
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/alipay_mch_applyment")
public class AlipayMchApplymentController extends BaseController {

    private static final MyLog logger = MyLog.getLog(AlipayMchApplymentController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;


    /** 查询支付宝进件信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {

        Long mchId = getValLongRequired("mchId");
        Long currentIsvId = getUser().getBelongInfoId();

        AlipayMchSnapshot record = rpc.rpcAlipayMchSnapshotService.findByMchAndIsv(mchId, currentIsvId);
        return XxPayResponse.buildSuccess(record);
    }



    /** 商户进件 **/
    @RequestMapping("/apply")
    public XxPayResponse apply() {
        try {

            AlipayMchSnapshot record = getObject(AlipayMchSnapshot.class);
            record.setIsvId(getUser().getBelongInfoId());
            rpc.rpcXxPayAlipaypayApiService.mchApply(record);
            return XxPayResponse.buildSuccess();
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询支付宝商户申请审核状态 **/
    @RequestMapping("/queryApplyStatus")
    public XxPayResponse queryApplyStatus() {
        try {
            Long applyNo = getValLongRequired("applyNo");
            rpc.rpcXxPayAlipaypayApiService.queryMchApply(applyNo);
            return XxPayResponse.buildSuccess();
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }
}