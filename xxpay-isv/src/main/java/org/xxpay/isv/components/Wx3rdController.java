package org.xxpay.isv.components;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/** 查询微信第三方平台配置 */
@Controller
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx3rd")
public class Wx3rdController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /** 获取微信三方配置信息 */
    @ResponseBody
    @RequestMapping("/get")
    public XxPayResponse get() {
        IsvWx3rdInfo isvWx3rdInfo = rpc.rpcIsvWx3rdInfoService.getById(getUser().getBelongInfoId());
        return XxPayResponse.buildSuccess(isvWx3rdInfo);
    }

    /** 更新配置信息 */
    @ResponseBody
    @RequestMapping("/update")
    public XxPayResponse update() {

        IsvWx3rdInfo paramRecord = getObject(IsvWx3rdInfo.class);
        paramRecord.setIsvId(getUser().getBelongInfoId());

        boolean isTrue;
        IsvWx3rdInfo dbRecord = rpc.rpcIsvWx3rdInfoService.getById(getUser().getBelongInfoId());
        if(dbRecord == null){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        paramRecord.setStatus(MchConstant.ISV3RD_STATUS_WAIT_SIGN);  //待验证
        isTrue = rpc.rpcIsvWx3rdInfoService.updateById(paramRecord);
        if(isTrue){
            return XxPayResponse.buildSuccess();
        }

        throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //插入失败
    }


}