package org.xxpay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MD5Util;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.core.entity.SysUser;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_info")
public class MchInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        mchInfo.setPsVal("offRechargeFee", rpcCommonService.rpcFeeScaleService.getOffRechargeFeeByMch(mchId));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchInfo));
    }

    /**
     * 修改商户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户信息" )
    public ResponseEntity<?> update() {

        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = getObject( MchInfo.class);
        MchInfo dbMchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(dbMchInfo == null) {
            ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        // 确认手机不能重复
        if(rpcCommonService.rpcMchInfoService.count(mchInfo.lambda().ne(MchInfo::getMchId, mchId).eq(MchInfo::getLoginMobile, mchInfo.getLoginMobile())) > 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        // 确认邮箱不能重复
        if(rpcCommonService.rpcMchInfoService.count(mchInfo.lambda().ne(MchInfo::getMchId, mchId).eq(MchInfo::getLoginEmail, mchInfo.getLoginEmail())) > 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }

        // 确认用户名不能重复
        if(rpcCommonService.rpcMchInfoService.count(mchInfo.lambda().ne(MchInfo::getMchId, mchId).eq(MchInfo::getLoginUserName, mchInfo.getLoginUserName())) > 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_USERNAME_USED));
        }

        SysUser mchUser = null;
        String password = getValString("password"); //登录密码

        //如果密码有变更 | 登录认证信息发生变化
        if(StringUtils.isNotEmpty(password)
           || (StringUtils.isNotEmpty(mchInfo.getLoginUserName()) && !dbMchInfo.getLoginUserName().equals(mchInfo.getLoginUserName()))
           || (StringUtils.isNotEmpty(mchInfo.getLoginMobile()) && !dbMchInfo.getLoginMobile().equals(mchInfo.getLoginMobile()))
           || (StringUtils.isNotEmpty(mchInfo.getLoginEmail()) && !dbMchInfo.getLoginEmail().equals(mchInfo.getLoginEmail()))
           || (StringUtils.isNotEmpty(mchInfo.getMchName()) && !dbMchInfo.getMchName().equals(mchInfo.getMchName()))
        ){

            mchUser = new SysUser();
            if(StringUtils.isNotEmpty(password)){
                mchUser.setLoginPassword(SpringSecurityUtil.generateSSPassword(password));
                mchUser.setLastPasswordResetTime(new Date());
            }
            mchUser.setLoginUserName(StringUtils.isNotEmpty(mchInfo.getLoginUserName()) ? mchInfo.getLoginUserName(): null);
            mchUser.setMobile(StringUtils.isNotEmpty(mchInfo.getLoginMobile()) ? mchInfo.getLoginMobile(): null);
            mchUser.setEmail(StringUtils.isNotEmpty(mchInfo.getLoginEmail()) ? mchInfo.getLoginEmail(): null);
            mchUser.setNickName(StringUtils.isNotEmpty(mchInfo.getMchName()) ? mchInfo.getMchName(): null);
        }

        JSONObject param = getReqParamJSON();
        SettBankAccount mchSettBankAccount = new SettBankAccount();
        mchSettBankAccount.setName(param.getString("settAccountName"));
        mchSettBankAccount.setAccountType((byte)1); //账号类型 1-银行账号
        mchSettBankAccount.setBankName(param.getString("settBankName")); //银行名称
        mchSettBankAccount.setBankNetName(param.getString("settBankNetName")); //银行网点名称
        mchSettBankAccount.setProvince(param.getString("settCity")); //开户所在省
        mchSettBankAccount.setCity(param.getString("settCity")); //开户行所在市
        mchSettBankAccount.setAccountName(param.getString("settAccountName")); //开户人
        mchSettBankAccount.setAccountNo(param.getString("settAccountNo")); //银行账号
        mchSettBankAccount.setAccountAttr(param.getByte("settAccountAttr")); //账户属性
        mchSettBankAccount.setIsDefault(MchConstant.PUB_YES); //设置默认结算账号

        int count = rpcCommonService.rpcMchInfoService.updateMch(mchInfo, mchSettBankAccount, mchUser);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        return null;
    }


    /**
     * <p><b>Description: </b>生成商户登录url
     * <p>2018年10月26日 上午10:22:56
     * @author terrfly
     * @return
     */
    @RequestMapping("/genLoginUrl")
    @ResponseBody
    public ResponseEntity<?> genLoginUrl() {
    	
        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        // 将商户ID+商户登录名+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String secret = "Abc%$G&!!!128GMCH";
        
		String signTime = DateUtil.date2Str(new Date(), "ddHHmm");
        String rawToken = mchId + mchInfo.getLoginUserName() + secret + signTime;
        String tokenMD5 = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginMchUrl = mainConfig.getLoginMchUrl();
        
        JSONObject json = new JSONObject();
        json.put("loginUrl", String.format(loginMchUrl, mchInfo.getMchId(), tokenMD5));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));
    }

}