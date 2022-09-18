package org.xxpay.manage.isv.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

/** isv通道配置ctrl **/
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/isv_passage_config")
public class IsvPassageConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    @RequestMapping("/list")
    public XxPayResponse list() {
        Long isvId = getValLongRequired( "isvId");

        //所有接口类型List
        IPage<PayInterfaceType> ifTypeList =
                rpc.rpcPayInterfaceTypeService.page(getIPage(),
                new LambdaQueryWrapper<PayInterfaceType>().eq(PayInterfaceType::getStatus, MchConstant.PUB_YES)); //仅查询状态可用的接口类型

        //将当前isv已配置的接口放置到对象中  用于列表显示状态等信息
        for (PayInterfaceType record : ifTypeList.getRecords()) {

            //根据ifTypeCode 与 isvId 查询 服务商通道表
            PayPassage isvPassage = rpc.rpcPayPassageService.selectByIsv(isvId, record.getIfTypeCode());
            if(isvPassage != null){
                record.setPsVal("isvStatus", isvPassage.getStatus());
            }
        }
        return PageRes.buildSuccess(ifTypeList);
    }


    /** 查询详情， 根据isvId 与 ifTypeCode 查询服务商的配置信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {
        Long isvId = getValLongRequired( "isvId");
        String ifTypeCode = getValStringRequired( "ifTypeCode");

        PayPassage isvPassage = rpc.rpcPayPassageService.selectByIsv(isvId, ifTypeCode);
        return XxPayResponse.buildSuccess(isvPassage);
    }


    /** 更新or新增 记录 **/
    @RequestMapping("/saveOrUpdate")
    public XxPayResponse saveOrUpdate() {
        PayPassage isvPassage = getObject( PayPassage.class); //更新的服务商信息

        if(isvPassage.getId() == null){ //新增
            isvPassage.setRiskStatus(MchConstant.PUB_NO); //风控状态： 0-关闭
            isvPassage.setContractStatus(MchConstant.PUB_NO); //签约状态： 0-未开通
        }

        boolean isTrue = rpc.rpcPayPassageService.saveOrUpdate(isvPassage);
        return XxPayResponse.buildSuccess(isTrue);
    }

}