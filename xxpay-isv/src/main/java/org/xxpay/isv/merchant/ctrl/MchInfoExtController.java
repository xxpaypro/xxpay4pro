package org.xxpay.isv.merchant.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchInfoExt;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/mch_info_ext")
public class MchInfoExtController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired("mchId");
        MchInfoExt dbRecord = rpc.rpcMchInfoExtService.getOneMchExt(mchId, getUser().getBelongInfoId(), null);

        if(dbRecord != null){
            dbRecord.setPsVal("industryIds", rpc.rpcSysIndustryCodeService.getAllIndustryCode(dbRecord.getIndustryCode()));
            String industryDetail = rpc.rpcSysIndustryCodeService.formatIndustryInfo(dbRecord.getIndustryCode());
            dbRecord.setPsVal("industryDetail", industryDetail);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(dbRecord));
    }

    /** 补充进件资料 */
    @RequestMapping("/saveOrUpdate")
    @MethodLog( remark = "补充进件资料" )
    public XxPayResponse saveOrUpdate() {

        //请求数据
        MchInfoExt record = getObject(MchInfoExt.class);

        MchInfo mchInfo = rpc.rpcMchInfoService.getOneMch(record.getMchId(), getUser().getBelongInfoId(), null);
        if(mchInfo == null){
            return XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
        }

        //填充基本信息
        record.setAgentId(mchInfo.getAgentId());
        record.setIsvId(mchInfo.getIsvId());

        //更新 or 新增 操作
        rpc.rpcMchInfoExtService.saveOrUpdate(record);

        //返回success
        return XxPayResponse.buildSuccess();
    }
}