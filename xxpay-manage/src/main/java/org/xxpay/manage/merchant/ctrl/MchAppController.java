package org.xxpay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.MchApp;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/13
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/app")
public class MchAppController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchApp mchApp = getObject( MchApp.class);
        int count = rpcCommonService.rpcMchAppService.count(mchApp);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchApp> mchAppList = rpcCommonService.rpcMchAppService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), mchApp);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAppList, count));
    }

    /**
     * 查询应用信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String appId = getValStringRequired( "appId");
        MchApp mchApp = rpcCommonService.rpcMchAppService.findById(appId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchApp));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "添加商户应用" )
    public ResponseEntity<?> add() {

        JSONObject param = getReqParamJSON();
        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        String appName = getValStringRequired( "appName");
        String remark = param.getString("remark");
        MchApp mchApp = new MchApp();
        mchApp.setMchId(mchId);
//        mchApp.setAppName(mchInfo.getName());  //TODO 修改商户表结构0910
        mchApp.setAppId(MySeq.getUUID());
        mchApp.setAppName(appName);
        mchApp.setStatus(MchConstant.PUB_YES);
        mchApp.setRemark(remark);
        int count = rpcCommonService.rpcMchAppService.add(mchApp);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户应用" )
    public ResponseEntity<?> update() {

        JSONObject param = getReqParamJSON();
        String appId = getValStringRequired( "appId");
        String appName = param.getString("appName");
        String remark = param.getString("remark");
        Byte status = param.getByte("status");
        MchApp mchApp = new MchApp();
        mchApp.setAppId(appId);
        if(StringUtils.isNotBlank(appName)) mchApp.setAppName(appName);
        if(status != null && status.byteValue() == MchConstant.PUB_YES) {
            mchApp.setStatus(MchConstant.PUB_YES);
        }else if(status != null && status.byteValue() == MchConstant.PUB_NO) {
            mchApp.setStatus(MchConstant.PUB_NO);
        }
        if(StringUtils.isNotBlank(remark)) mchApp.setRemark(remark);
        int count = rpcCommonService.rpcMchAppService.update( mchApp);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
