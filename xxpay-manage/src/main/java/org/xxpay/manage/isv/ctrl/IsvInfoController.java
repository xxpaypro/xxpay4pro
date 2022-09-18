package org.xxpay.manage.isv.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/isv_info")
public class IsvInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**  查询服务商信息（单条） **/
    @RequestMapping("/get")
    public XxPayResponse get() {
        Long isvId = getValLongRequired( "isvId");
        IsvInfo isvInfo = rpc.rpcIsvInfoService.getById(isvId);
        return XxPayResponse.buildSuccess(isvInfo);
    }

    /** 新增服务商信息 **/
    @RequestMapping("/add")
    @MethodLog( remark = "新增服务商" )
    public BizResponse add() {
        IsvInfo isvInfo = getObject( IsvInfo.class);

        //登录用户名不能是纯数字
        if(!XXPayUtil.checkLoginUserName(isvInfo.getLoginUserName())){
            throw ServiceException.build(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR);
        }

//        // 校验手机号是否重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getMobile, isvInfo.getMobile())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_MOBILE_EXISTS);
        }
        // 校验用户登录名是否重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getLoginUserName, isvInfo.getLoginUserName())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_USERNAME_EXISTS);
        }

        // 校验邮箱是否重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getEmail, isvInfo.getEmail())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_EMAIL_EXISTS);
        }

        rpc.rpcIsvInfoService.addIsv(isvInfo);
        return BizResponse.buildSuccess();
    }

    /** 修改服务商信息 **/
    @RequestMapping("/update")
    @MethodLog( remark = "修改服务商" )
    public BizResponse update() {
        JSONObject param = getReqParamJSON();
        Long isvId = getValLongRequired( "isvId");
        IsvInfo isvInfo = getObject( IsvInfo.class);

        //登录用户名不能是纯数字
        if(isvInfo.getLoginUserName() != null && !XXPayUtil.checkLoginUserName(isvInfo.getLoginUserName())){
            throw ServiceException.build(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR);
        }

        // 确认手机不能重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().ne(IsvInfo::getIsvId, isvId).eq(IsvInfo::getMobile, isvInfo.getMobile())) > 0) {
            return BizResponse.build(RetEnum.RET_MCH_MOBILE_USED);
        }

        // 确认邮箱不能重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().ne(IsvInfo::getIsvId, isvId).eq(IsvInfo::getEmail, isvInfo.getEmail())) > 0) {
            return BizResponse.build(RetEnum.RET_MCH_EMAIL_USED);
        }

        // 确认用户名不能重复
        if(rpc.rpcIsvInfoService.count(isvInfo.lambda().ne(IsvInfo::getIsvId, isvId).eq(IsvInfo::getLoginUserName, isvInfo.getLoginUserName())) > 0) {
            return BizResponse.build(RetEnum.RET_MCH_USERNAME_USED);
        }

        IsvInfo dbIsvInfo = rpc.rpcIsvInfoService.getById(isvId);
        if(dbIsvInfo == null) throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        SysUser isvUser = null;
        String password = getValString("password"); //登录密码

        //如果密码有变更 | 登录认证信息发生变化
        if(StringUtils.isNotEmpty(password)
                || (StringUtils.isNotEmpty(isvInfo.getLoginUserName()) && !dbIsvInfo.getLoginUserName().equals(isvInfo.getLoginUserName()))
                || (StringUtils.isNotEmpty(isvInfo.getMobile()) && !dbIsvInfo.getMobile().equals(isvInfo.getMobile()))
                || (StringUtils.isNotEmpty(isvInfo.getEmail()) && !dbIsvInfo.getEmail().equals(isvInfo.getEmail()))
                || (StringUtils.isNotEmpty(isvInfo.getIsvName()) && !dbIsvInfo.getIsvName().equals(isvInfo.getIsvName()))
        ){

            isvUser = new SysUser();
            if(StringUtils.isNotEmpty(password)){
                isvUser.setLoginPassword(SpringSecurityUtil.generateSSPassword(password));
                isvUser.setLastPasswordResetTime(new Date());
            }
            isvUser.setLoginUserName(StringUtils.isNotEmpty(isvInfo.getLoginUserName()) ? isvInfo.getLoginUserName(): null);
            isvUser.setMobile(StringUtils.isNotEmpty(isvInfo.getMobile()) ? isvInfo.getMobile(): null);
            isvUser.setEmail(StringUtils.isNotEmpty(isvInfo.getEmail()) ? isvInfo.getEmail(): null);
            isvUser.setNickName(StringUtils.isNotEmpty(isvInfo.getIsvName()) ? isvInfo.getIsvName(): null);
        }

        rpc.rpcIsvInfoService.updateIsv(isvInfo, isvUser);
        return BizResponse.buildSuccess();
    }

    /** 服务商列表 **/
    @RequestMapping("/list")
    public XxPayResponse list() {
        IsvInfo isvInfo = getObject( IsvInfo.class);
        QueryWrapper<IsvInfo> wrapper = new QueryWrapper<>(isvInfo);
        wrapper.lambda().orderByDesc(IsvInfo::getCreateTime);
        IPage<IsvInfo> records = rpc.rpcIsvInfoService.page(getIPage(), wrapper);
        return PageRes.buildSuccess(records);
    }

   /** 生成登录信息 **/
    @RequestMapping("/genLoginUrl")
    public XxPayResponse genLoginUrl() {

        Long isvId = getValLongRequired( "isvId");

        IsvInfo isvInfo = rpc.rpcIsvInfoService.getById(isvId);
        // 将服务商ID+服务商登录名+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String secret = "Abc%$G&!!!128G";
        String signTime = DateUtil.date2Str(new Date(), "ddHHmm");
        String rawToken = isvId + isvInfo.getLoginUserName() + secret + signTime;
        String tokenMD5 = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginAgentUrl = mainConfig.getLoginIsvUrl();

        JSONObject json = new JSONObject();
        json.put("loginUrl", String.format(loginAgentUrl, isvId, tokenMD5));
        return XxPayResponse.buildSuccess(json);
    }

    /** 重置登录密码 **/
    @MethodLog( remark = "重置服务商登录密码" )
    @RequestMapping("/resetLoginPwd")
    public XxPayResponse resetLoginPwd() {
        if(!rpc.rpcSysService.resetLoginPwd(MchConstant.INFO_TYPE_ISV, getValLongRequired( "isvId"))){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }
        return XxPayResponse.buildSuccess();
    }

    /** 审核服务商信息  */
    @RequestMapping("/audit")
    @MethodLog( remark = "审核服务商信息" )
    public XxPayResponse audit() {

        Long isvId = getValLong("isvId");
        Byte status = getValByte("status");
        IsvInfo isvInfo  = rpc.rpcIsvInfoService.getById(isvId);

        if(isvInfo == null){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        if(isvInfo.getStatus() != MchConstant.STATUS_AUDIT_ING){ ///状态非待审核状态
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        rpc.rpcIsvInfoService.auditMch(isvId, status);
        return XxPayResponse.buildSuccess();
    }

}