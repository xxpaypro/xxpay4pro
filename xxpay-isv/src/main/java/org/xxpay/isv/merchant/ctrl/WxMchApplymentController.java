package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SysWxpayIndustryCode;
import org.xxpay.core.entity.WxMchSnapshot;
import org.xxpay.core.entity.WxMchUpgradeSnapshot;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;

import java.util.List;

/** 微信商户入驻接口  **/
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx_mch_applyment")
public class WxMchApplymentController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxMchApplymentController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;


    /** 查询微信进件信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {

        Long mchId = getValLongRequired("mchId");
        Long currentIsvId = getUser().getBelongInfoId();

        WxMchSnapshot record = rpc.rpcWxMchSnapshotService.findByMchAndIsv(mchId, currentIsvId);
        return XxPayResponse.buildSuccess(record);
    }



    /** 微信商户进件申请 **/
    @RequestMapping("/applyment4sub")
    public XxPayResponse applyment4sub() {
        try {

            WxMchSnapshot record = getObject(WxMchSnapshot.class);
            record.setIsvId(getUser().getBelongInfoId());
            rpc.rpcXxPayWxpayApiService.wxMicroApply(record);
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
            JSONObject result = rpc.rpcXxPayWxpayApiService.wxQueryMicroApply(applyNo);
            return XxPayResponse.buildSuccess(result);
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }




    /** 查询微信进件信息 - 升级信息 **/
    @RequestMapping("/getUpgrade")
    public XxPayResponse getUpgrade() {

        Long mchId = getValLongRequired("mchId");
        Long currentIsvId = getUser().getBelongInfoId();

        WxMchUpgradeSnapshot record = rpc.rpcWxMchUpgradeSnapshotService.findByMchAndIsv(mchId, currentIsvId);
        return XxPayResponse.buildSuccess(record);
    }



    /** 小微升级申请 **/
    @RequestMapping("/microApplyUpgrade")
    public XxPayResponse microApplyUpgrade() {
        try {

            WxMchUpgradeSnapshot record = getObject(WxMchUpgradeSnapshot.class);
            record.setIsvId(getUser().getBelongInfoId());
            rpc.rpcXxPayWxpayApiService.wxMicroApplyUpgrade(record);
            return XxPayResponse.buildSuccess();
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询小微商户 升级审核状态 **/
    @RequestMapping("/queryMicroApplyStatusUpgrade")
    public XxPayResponse queryMicroApplyStatusUpgrade() {
        try {

            Long applyNo = getValLongRequired("applyNo");
            JSONObject result = rpc.rpcXxPayWxpayApiService.wxQueryMicroApplyUpgrade(applyNo);
            return XxPayResponse.buildSuccess(result);
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询小微商户 费率规则 **/
    @RequestMapping("/wxIndustry")
    public XxPayResponse wxIndustry() {

        Long parentCode = getValLongRequired("parentCode");
        LambdaQueryWrapper<SysWxpayIndustryCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysWxpayIndustryCode::getParentCode, parentCode);
        List<SysWxpayIndustryCode> result = rpc.rpcSysWxpayIndustryCodeService.list(queryWrapper);
        return XxPayResponse.buildSuccess(result);
    }

    /** 小微商户申请 **/
    @RequestMapping("/microApply")
    public XxPayResponse microApply() {
        try {

            WxMchSnapshot record = getObject(WxMchSnapshot.class);
            record.setIsvId(getUser().getBelongInfoId());
            rpc.rpcXxPayWxpayApiService.microApply(record);
            return XxPayResponse.buildSuccess();
        }catch (ServiceException e) {
            logger.error("error", e);
            return XxPayResponse.buildErr(e.getErrMsg());

        }catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询小微商户申请审核状态 **/
    @RequestMapping("/queryMicroApplyStatus")
    public XxPayResponse queryMicroApplyStatus() {
        try {

            Long applyNo = getValLongRequired("applyNo");
            JSONObject result = rpc.rpcXxPayWxpayApiService.queryMicroApply(applyNo);
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