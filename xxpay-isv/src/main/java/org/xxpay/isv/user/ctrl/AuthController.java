package org.xxpay.isv.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.common.vo.JWTPayload;
import org.xxpay.core.common.vo.JWTUtils;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.util.AliSmsUtil;
import org.xxpay.isv.user.service.UserService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RpcCommonService rpcCommonService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final MyLog _log = MyLog.getLog(AuthController.class);

    /**
     * 登录鉴权
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "登录" )
    public ResponseEntity<?> authToken() throws AuthenticationException{

        String loginType = getValString("loginType");
        String vercodeRandomStr = null;
        if (MchConstant.LOGIN_TYPE_WEB.equals(loginType)) {
            String vercode = getValStringRequired( "vercode"); //图形验证码
            vercodeRandomStr = getValStringRequired( "vercodeRandomStr"); //图形验证码 缓存key

            String vercodeCacheValue = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_VERCODE_PREFIX_ISV + vercodeRandomStr);

            if(!vercode.equals(vercodeCacheValue)){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_VERCODE_ERROR));
            }
        }

        String username = getValStringRequired( "username");
        String password = getValStringRequired( "password");

        try {
            String token = userService.login(username, password, loginType, null);

            //判断是app登录，延长token有效时间
            if (StringUtils.isNotBlank(loginType) && (MchConstant.LOGIN_TYPE_APP.equals(loginType))) {
                stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_ISV + token, "1",
                        MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
                JSONObject data = new JSONObject();
                data.put("access_token", token);
                return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
            }

            //web登录,删除图形验证码缓存
            if (MchConstant.LOGIN_TYPE_WEB.equals(loginType)) {
                stringRedisTemplate.delete(MchConstant.CACHEKEY_TOKEN_PREFIX_ISV + vercodeRandomStr);
            }
            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_ISV + token, "1",
                    MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
            JSONObject data = new JSONObject();
            data.put("access_token", token);
            return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
    }

    /**
     * 商户注册
     * @return
     */
    @RequestMapping(value = "/auth/register")
    public ResponseEntity<?> register() {

        IsvInfo isvInfo = getObject(IsvInfo.class);
        String mobile = getValStringRequired("mobile");
        String verifyCode = getValStringRequired("vercode");
        String password = getValStringRequired("password");

        //登录用户名不能是纯数字
        if(!XXPayUtil.checkLoginUserName(isvInfo.getLoginUserName())){
            throw ServiceException.build(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR);
        }

        // 校验手机号是否重复
        if(rpcCommonService.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getMobile, isvInfo.getMobile())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_MOBILE_EXISTS);
        }
        // 校验用户登录名是否重复
        if(rpcCommonService.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getLoginUserName, isvInfo.getLoginUserName())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_USERNAME_EXISTS);
        }

        // 校验邮箱是否重复
        if(rpcCommonService.rpcIsvInfoService.count(isvInfo.lambda().eq(IsvInfo::getEmail, isvInfo.getEmail())) > 0){
            throw ServiceException.build(RetEnum.RET_MGR_EMAIL_EXISTS);
        }

        //验证手机号是否可用
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_ISV, mobile);
        if (sysUser != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        //校验手机验证码
        if(!verifyMobileCode(mobile, verifyCode, MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_VERCODE_ERROR));
        }

        isvInfo.setRegisterPassword(SpringSecurityUtil.generateSSPassword(password));
        isvInfo.setStatus(MchConstant.STATUS_AUDIT_ING); //状态：待审核
        rpcCommonService.rpcIsvInfoService.addIsv(isvInfo);

        //删除缓存的短信验证码
        stringRedisTemplate.delete(MchConstant.CACHEKEY_SMSCODE_PREFIX_ISV + MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER + mobile);

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 登录鉴权(运营平台登录服务商系统鉴权)
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/mgr_auth")
    public ResponseEntity<?> mgrAuthToken() throws AuthenticationException{
        Long isvId = getValLongRequired( "isvId");
        String token = getValStringRequired( "token");

        IsvInfo isvInfo = userService.findByIsvId(isvId);
        if(isvInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_ISV_NOT_EXIST));
        }
        if(MchConstant.PUB_YES != isvInfo.getStatus()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_ISV_STATUS_STOP));
        }

        // 先校验运营平台传过来的token,是否合法
        // 将商户ID+服务商登录用户名+密钥 做32位MD5加密转大写
        String loginUserName = isvInfo.getLoginUserName();
        String secret = "Abc%$G&!!!128G";
        
		String signNowTime = DateUtil.date2Str(new Date(), "ddHHmm"); //当前时间
		String rawTokenByNow = isvId + loginUserName + secret + signNowTime;
		String md5Now = MD5Util.string2MD5(rawTokenByNow).toUpperCase();
		
		String sign1Time = DateUtil.date2Str(DateUtil.minusDateByMinute(new Date(), 1 ), "ddHHmm"); //当前时间减去1min
		String rawTokenBySub1 = isvId + loginUserName + secret + sign1Time;
        String md5BySub1 = MD5Util.string2MD5(rawTokenBySub1).toUpperCase();
        
        //验证当前时间 和 当前时间减1分钟是否满足（避免59s 跳转后失效的问题）
        if(!md5Now.equals(token) && !md5BySub1.equals(token)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_ILLEGAL_LOGIN));
        }

        //查询用户
        JWTBaseUser jwtBaseUser = (JWTBaseUser) userDetailsService.loadUserByUsername(isvId.toString());
        jwtBaseUser.setLoginTypeAndVersion(MchConstant.LOGIN_TYPE_WEB, null, null); //设置其他信息
        String authToken = JWTUtils.generateToken(new JWTPayload(jwtBaseUser), mainConfig.getJwtSecret(), mainConfig.getJwtExpiration());
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_ISV + authToken, "1",
										MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
        
        JSONObject data = new JSONObject();
        data.put("access_token", authToken);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }


    /**
     * 获取图形验证码
     */
    @RequestMapping(value = "/auth/vercode")
    public void getAuthCode(HttpServletResponse response, String vercodeRandomStr) throws IOException {
    	
    	if(StringUtils.isEmpty(vercodeRandomStr))return ;
        Map<String, Object> randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        String vercodeValue = randomMap.get("randomString").toString(); //码值
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_VERCODE_PREFIX_ISV + vercodeRandomStr,
        									  vercodeValue, MchConstant.CACHEKEY_VERCODE_TIMEOUT, TimeUnit.MINUTES);
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }


    /**
     * 发送验证短信
     * @return
     */
    @RequestMapping(value = "/auth/sms_send")
    public ResponseEntity<?> sendSms(){

        Byte bizType = getValByteRequired("bizType");//业务场景 22-注册

        // 验证参数
        if (bizType != MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }

        String mobile = getValStringRequired("mobile");//手机号
        if(!StrUtil.checkMobileNumber(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }

        //根据手机号查询用户
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_ISV, mobile);
        // 注册和绑定新手机，验证手机号是否被使用
        if (sysUser != null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        // 发送短信服务
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        logger.info("手机号：{}，短信验证码：{}", mobile, verifyCode);

        String sysConfigStr = rpcCommonService.rpcSysConfigService.getVal("smsConfig");
        if(!AliSmsUtil.sendSms(Long.parseLong(mobile), verifyCode, bizType, sysConfigStr)){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }

        // 将短信验证码存在redis
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SMSCODE_PREFIX_ISV + bizType + mobile, verifyCode,
                MchConstant.CACHEKEY_SMSCODE_TIMEOUT, TimeUnit.MINUTES);

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    /**
     * 校验手机验证码
     * @param mobile
     * @param verifyCode
     * @return
     */
    public boolean verifyMobileCode(String mobile, String verifyCode, Byte bizType) {

        if (StringUtils.isAnyBlank(mobile, verifyCode)) {
            return false;
        }

        //校验手机验证码
        String redisCode = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_SMSCODE_PREFIX_ISV + bizType + mobile);
        if (verifyCode.equals(redisCode)) {
            return true;
        }

        return false;
    }

}
