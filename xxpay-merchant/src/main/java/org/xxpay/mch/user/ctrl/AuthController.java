package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.xxpay.core.entity.MchDevice;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SysClientVersion;
import org.xxpay.core.entity.SysUser;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.common.util.AliSmsUtil;
import org.xxpay.mch.user.service.UserService;
import org.xxpay.mch.utils.BaiduAuthToken;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final MyLog _log = MyLog.getLog(AuthController.class);

    /**
     * 登录鉴权
     * 商户登录所需参数： username（商户登录名）  password（密码）  vercode（图形验证码）  vercodeRandomStr（图片验证码key）
     * 当需要安全验证时 所需参数添加 smsOrGoogleCode（短信/谷歌验证码）
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "登录" )
    public ResponseEntity<?> authToken() throws AuthenticationException{

        String loginType = getValStringRequired( "loginType");  //pc：收银插件,  uni-app：手机客户端,  web：浏览器网页访问, face-app:  刷脸app
        String version = getValString( "version");
        String loginDeviceNo = getValString("deviceNo");  //登录设备编号

        if (!MchConstant.LOGIN_TYPE_PC.equals(loginType) && !MchConstant.LOGIN_TYPE_WEB.equals(loginType)
                    && !MchConstant.LOGIN_TYPE_APP.equals(loginType) && !MchConstant.LOGIN_TYPE_FACEAPP.equals(loginType)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_LOGIN_TYPE_ERROR));
        }

        String vercodeRandomStr = "";
        if (MchConstant.LOGIN_TYPE_WEB.equals(loginType)) {
            String vercode = getValStringRequired( "vercode"); //图形验证码
            vercodeRandomStr = getValStringRequired( "vercodeRandomStr"); //图形验证码 缓存key

            String vercodeCacheValue = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_VERCODE_PREFIX_MCH + vercodeRandomStr);

            if(!vercode.equals(vercodeCacheValue)){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_VERCODE_ERROR));
            }
        }

        String username = getValStringRequired( "username");
        String password = getValStringRequired( "password");

        String token;
        try {
           token = userService.login(username, password, loginType, version, loginDeviceNo);
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }

        //验证通过，如果cid存在，则新增或更新设备、用户绑定信息
        String cid = getValString( "cid");
        if (StringUtils.isNotBlank(cid)) {
            MchDevice device = new MchDevice();
            device.setCid(cid);
            device.setUserId(getUser().getUserId());
            device.setLoginTime(new Date());
            rpcCommonService.rpcMchDeviceService.saveOrUpdate(device);
        }

        //获取店铺名称
        Long storeId = getUser().getStoreId();
        String storeName = null;
        if (storeId != null) {
            storeName = rpcCommonService.rpcMchStoreService.getById(storeId).getStoreName();
        }
        _log.info("当前用户所属门店：{}", storeId);

        //判断是app或pc登录，延长token有效时间
        if (StringUtils.isNotBlank(loginType) && (MchConstant.LOGIN_TYPE_APP.equals(loginType) || MchConstant.LOGIN_TYPE_PC.equals(loginType) || MchConstant.LOGIN_TYPE_FACEAPP.equals(loginType))) {
            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + token, "1",
                    MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
            JSONObject data = new JSONObject();
            data.put("access_token", token);
            if (storeId != null) {
                data.put("storeId", storeId);
                data.put("storeName", storeName);
            }

            //小程序返回自定义角色
            if ("uni-app".equals(loginType)) {
                MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByLoginName(username);
                data.put("miniRole", mchInfo != null ? mchInfo.getMiniRole() : null);
            }

            //记录首次工作时间
            rpcCommonService.rpcSysService.startWork(getUser().getUserId());
            return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
        }

        //web登录,删除图形验证码缓存
        if (MchConstant.LOGIN_TYPE_WEB.equals(loginType)) {
            stringRedisTemplate.delete(MchConstant.CACHEKEY_VERCODE_PREFIX_MCH + vercodeRandomStr);
        }
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + token, "1",
                MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
        JSONObject data = new JSONObject();
        data.put("access_token", token);
        if (storeId != null) {
            data.put("storeId", storeId);
            data.put("storeName", storeName);
        }
        //记录首次工作时间
        rpcCommonService.rpcSysService.startWork(getUser().getUserId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 商户注册
     * @return
     */
    @RequestMapping(value = "/auth/register")
    public ResponseEntity<?> register() {

        String mchName = getValStringRequired("mchName");
        String mobile = getValStringRequired("mobile");
        String verifyCode = getValStringRequired("vercode");
        Long isvId = getValLongRequired("isvId");
        String password = getValStringRequired("password");

        if(rpcCommonService.rpcIsvInfoService.getById(isvId) == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_ISV_NOT_EXIST));
        }

        //验证手机号是否可用
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if (sysUser != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        //校验手机验证码
        if(!verifyMobileCode(mobile, verifyCode, MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_VERCODE_ERROR));
        }

        MchInfo saveMchInfo = new MchInfo();
        saveMchInfo.setMchName(mchName);
        saveMchInfo.setMchShortName(mchName);
        saveMchInfo.setLoginUserName("m" + mobile);
        saveMchInfo.setLoginMobile(mobile);
        saveMchInfo.setIsvId(isvId);
        saveMchInfo.setProvinceCode(11);
        saveMchInfo.setCityCode(1101);
        saveMchInfo.setAreaCode(110101);
        saveMchInfo.setAreaInfo("北京 市辖区 东城区");
        saveMchInfo.setAddress(mchName);
        saveMchInfo.setRegisterPassword(SpringSecurityUtil.generateSSPassword(password));
        saveMchInfo.setStatus(MchConstant.STATUS_AUDIT_ING); //商户状态： 新增状态默认开启
        rpcCommonService.rpcMchInfoService.add(saveMchInfo);

        //删除缓存的短信验证码
        stringRedisTemplate.delete(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER + mobile);

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 登录鉴权(运营平台登录商户系统鉴权)
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/mgr_auth")
    public ResponseEntity<?> mgrAuthToken() throws AuthenticationException{

        Long mchId = getValLongRequired( "mchId");
        String token = getValStringRequired( "token");

        MchInfo mchInfo = userService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        if(MchConstant.PUB_YES != mchInfo.getStatus()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_STATUS_STOP));
        }

        // 先校验运营平台传过来的token,是否合法
        // 将商户ID+商户登录名+密钥 做32位MD5加密转大写
        String loginUserName = mchInfo.getLoginUserName();
        String secret = "Abc%$G&!!!128GMCH";
        
        String signNowTime = DateUtil.date2Str(new Date(), "ddHHmm"); //当前时间
		String rawTokenByNow = mchId + loginUserName + secret + signNowTime;
		String md5Now = MD5Util.string2MD5(rawTokenByNow).toUpperCase();
		
		String sign1Time = DateUtil.date2Str(DateUtil.minusDateByMinute(new Date(), 1 ), "ddHHmm"); //当前时间减去1min
		String rawTokenBySub1 = mchId + loginUserName + secret + sign1Time;
        String md5BySub1 = MD5Util.string2MD5(rawTokenBySub1).toUpperCase();
        
        //验证当前时间 和 当前时间减1分钟是否满足（避免59s 跳转后失效的问题）
        if(!md5Now.equals(token) && !md5BySub1.equals(token)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_ILLEGAL_LOGIN));
        }

        //查询用户
        JWTBaseUser jwtBaseUser = (JWTBaseUser) userDetailsService.loadUserByUsername(mchId.toString());
        jwtBaseUser.setLoginTypeAndVersion(MchConstant.LOGIN_TYPE_WEB, null, null); //设置其他信息
        String authToken = JWTUtils.generateToken(new JWTPayload(jwtBaseUser), mainConfig.getJwtSecret(), mainConfig.getJwtExpiration());
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + authToken, "1",
                MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

        JSONObject data = new JSONObject();
        data.put("access_token", authToken);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 退出接口
     */
    @RequestMapping(value = "/logout")
    public ResponseEntity<?>  logout() {

        String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);
        stringRedisTemplate.delete(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + authToken);
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
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
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
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
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + bizType + mobile, verifyCode,
                MchConstant.CACHEKEY_SMSCODE_TIMEOUT, TimeUnit.MINUTES);

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    /**
     * 找回密码   发送验证短信
     * @return
     */
    @RequestMapping(value = "/auth/forget/sms_send")
    public ResponseEntity<?> forgetSendSms() {


        // 验证参数
        String mobile = getValStringRequired( "mobile");

        if(!StrUtil.checkMobileNumber(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }

        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if(sysUser == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUBUSER_NOT_EXIST));
        }

        // 发送短信服务
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);

        boolean addFlag = rpcCommonService.rpcMsgcodeService.addCode(mobile, verifyCode, MchConstant.MSGCODE_BIZTPYE_MCH_LOGIN, MchConstant.MSGCODE_EXP_TIME, null);
        if (!addFlag) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        String sysConfigStr = rpcCommonService.rpcSysConfigService.getVal("smsConfig");
        if(!AliSmsUtil.sendSms(Long.parseLong(mobile), verifyCode, MchConstant.MSGCODE_BIZTPYE_MCH_LOGIN, sysConfigStr)){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 找回密码   修改密码
     * @return
     */
    @RequestMapping(value = "/auth/forget/pwd_update")
    public ResponseEntity<?> forgetPwdUpdate() {

        // 参数
        String mobile = getValStringRequired( "mobile");
        String smsCode = getValStringRequired( "smsCode");
        String password = getValStringRequired( "password");

        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if(sysUser == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUBUSER_NOT_EXIST));
        }

        // 判断密码
        if(StringUtils.isEmpty(password) || !StrUtil.checkPassword(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }

        //判断短信验证码
        if(StringUtils.isEmpty(smsCode)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        boolean verFlag = rpcCommonService.rpcMsgcodeService.verifyCode(mobile, smsCode, MchConstant.MSGCODE_BIZTPYE_MCH_LOGIN);
        if(!verFlag){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        //修改密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        sysUser.setLoginPassword(encoder.encode(password));
        sysUser.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 获取图形验证码
     */
    @RequestMapping(value = "/auth/vercode")
    public void getAuthCode( HttpServletResponse response, String vercodeRandomStr) throws IOException {
    	
    	if(StringUtils.isEmpty(vercodeRandomStr))return ;
        Map<String, Object> randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        String vercodeValue = randomMap.get("randomString").toString(); //码值
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_VERCODE_PREFIX_MCH + vercodeRandomStr,
        									  vercodeValue, MchConstant.CACHEKEY_VERCODE_TIMEOUT, TimeUnit.MINUTES);
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }

    /**
     * 获取百度语音token
     */
    @RequestMapping(value = "/getBaiduToken")
    public ResponseEntity<?>  getBaiduToken() {

        String loginType = getUser().getLoginType();//登录端

        String token = null;
        if (StringUtils.isNotBlank(loginType) && loginType.equals(MchConstant.LOGIN_TYPE_APP)){
            token = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_UNIAPP_BAIDU_TOKEN);
            //redis里token为空，则重新获取token
            if (StringUtils.isEmpty(token)) {

                Map<String, String> baiduBCEConfig = rpcCommonService.rpcSysConfigService.selectByCodes("baiduBceAppKey2Mch", "baiduBceAppSecret2Mch");
                token = BaiduAuthToken.getAccessToken(baiduBCEConfig.get("baiduBceAppKey2Mch"), baiduBCEConfig.get("baiduBceAppSecret2Mch"));
                if (StringUtils.isBlank(token)) {
                    return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_GET_BAIDU_TOKEN));
                }
                stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_UNIAPP_BAIDU_TOKEN,
                        token, MchConstant.CACHEKEY_BAIDU_TOKEN_TIMEOUT, TimeUnit.DAYS);
            }
        } else if (StringUtils.isNotBlank(loginType) && loginType.equals(MchConstant.LOGIN_TYPE_FACEAPP)){
            token = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_FACEAPP_BAIDU_TOKEN);
            //redis里token为空，则重新获取token
            if (StringUtils.isEmpty(token)) {
                Map<String, String> baiduBCEConfig = rpcCommonService.rpcSysConfigService.selectByCodes("baiduBceAppKey2Face", "baiduBceAppSecret2Face");
                token = BaiduAuthToken.getAccessToken(baiduBCEConfig.get("baiduBceAppKey2Face"), baiduBCEConfig.get("baiduBceAppSecret2Face"));
                if (StringUtils.isBlank(token)) {
                    return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_GET_BAIDU_TOKEN));
                }
                stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_FACEAPP_BAIDU_TOKEN,
                        token, MchConstant.CACHEKEY_BAIDU_TOKEN_TIMEOUT, TimeUnit.DAYS);
            }
        }

        JSONObject data = new JSONObject();
        data.put("baidu_token", token);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));

    }

    /** 查询更新版本 **/
    @RequestMapping(value = "/clientVersion")
    public XxPayResponse clientVersion() {

        String clientType = getValStringRequired("clientType");  //客户端类型
        String currentVersionSN = getValStringRequired("currentVersionSN");  //当前客户端版本

        //查询当前版本
        SysClientVersion currentVersionRecord = rpcCommonService.rpcSysClientVersionService.getOne(
                new QueryWrapper<SysClientVersion>().lambda()
                .eq(SysClientVersion:: getClientType, clientType)
                .eq(SysClientVersion:: getVersionSN, currentVersionSN)
        );

        if(currentVersionRecord == null){ //未查询到当前版本信息,  查询最新版本作为 需[强制更新] 的版本
            List<SysClientVersion> newVersionList = rpcCommonService.rpcSysClientVersionService.page(
                    new Page<>(1, 1),
                    new QueryWrapper<SysClientVersion>().lambda().eq(SysClientVersion:: getClientType, clientType).orderByDesc(SysClientVersion:: getVid)
            ).getRecords();
            if(newVersionList != null && !newVersionList.isEmpty()){
                SysClientVersion newVersion = newVersionList.get(0);
                newVersion.setForceUpdate(MchConstant.PUB_YES);
                return XxPayResponse.buildSuccess(newVersion);
            }else{
                return XxPayResponse.buildSuccess(new JSONObject());  //未查询到 任何版本信息
            }
        }

        // 查询到当前版本信息， 需查询当前版本之后的最新版本信息
        List<SysClientVersion> newVersionList = rpcCommonService.rpcSysClientVersionService.page(
                new Page<>(1, 1),
                new QueryWrapper<SysClientVersion>().lambda()
                .eq(SysClientVersion:: getClientType, clientType)
                .gt(SysClientVersion:: getVid, currentVersionRecord.getVid())
                .orderByDesc(SysClientVersion:: getVid)
        ).getRecords();

        if(newVersionList == null || newVersionList.isEmpty()){ //未查询到 最新版本
            return XxPayResponse.buildSuccess(new JSONObject());
        }

        //最新版本信息
        SysClientVersion newVersion = newVersionList.get(0);

        //如果当前版本前，包含强制更新， 则更改当前版本为强制更新版本
        Integer count = rpcCommonService.rpcSysClientVersionService.count(
                new QueryWrapper<SysClientVersion>().lambda()
                        .eq(SysClientVersion:: getClientType, clientType)
                        .eq(SysClientVersion:: getForceUpdate, MchConstant.PUB_YES)
                        .gt(SysClientVersion:: getVid, currentVersionRecord.getVid())
        );
        if(count > 0){
            newVersion.setForceUpdate(MchConstant.PUB_YES);
        }

        return XxPayResponse.buildSuccess(newVersion);
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
        String redisCode = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + bizType + mobile);
        if (verifyCode.equals(redisCode)) {
            return true;
        }

        return false;
    }

}
