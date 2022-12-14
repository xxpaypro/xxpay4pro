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
     * ????????????
     * ??????????????????????????? username?????????????????????  password????????????  vercode?????????????????????  vercodeRandomStr??????????????????key???
     * ???????????????????????? ?????????????????? smsOrGoogleCode?????????/??????????????????
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "??????" )
    public ResponseEntity<?> authToken() throws AuthenticationException{

        String loginType = getValStringRequired( "loginType");  //pc???????????????,  uni-app??????????????????,  web????????????????????????, face-app:  ??????app
        String version = getValString( "version");
        String loginDeviceNo = getValString("deviceNo");  //??????????????????

        if (!MchConstant.LOGIN_TYPE_PC.equals(loginType) && !MchConstant.LOGIN_TYPE_WEB.equals(loginType)
                    && !MchConstant.LOGIN_TYPE_APP.equals(loginType) && !MchConstant.LOGIN_TYPE_FACEAPP.equals(loginType)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_LOGIN_TYPE_ERROR));
        }

        String vercodeRandomStr = "";
        if (MchConstant.LOGIN_TYPE_WEB.equals(loginType)) {
            String vercode = getValStringRequired( "vercode"); //???????????????
            vercodeRandomStr = getValStringRequired( "vercodeRandomStr"); //??????????????? ??????key

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

        //?????????????????????cid??????????????????????????????????????????????????????
        String cid = getValString( "cid");
        if (StringUtils.isNotBlank(cid)) {
            MchDevice device = new MchDevice();
            device.setCid(cid);
            device.setUserId(getUser().getUserId());
            device.setLoginTime(new Date());
            rpcCommonService.rpcMchDeviceService.saveOrUpdate(device);
        }

        //??????????????????
        Long storeId = getUser().getStoreId();
        String storeName = null;
        if (storeId != null) {
            storeName = rpcCommonService.rpcMchStoreService.getById(storeId).getStoreName();
        }
        _log.info("???????????????????????????{}", storeId);

        //?????????app???pc???????????????token????????????
        if (StringUtils.isNotBlank(loginType) && (MchConstant.LOGIN_TYPE_APP.equals(loginType) || MchConstant.LOGIN_TYPE_PC.equals(loginType) || MchConstant.LOGIN_TYPE_FACEAPP.equals(loginType))) {
            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + token, "1",
                    MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
            JSONObject data = new JSONObject();
            data.put("access_token", token);
            if (storeId != null) {
                data.put("storeId", storeId);
                data.put("storeName", storeName);
            }

            //??????????????????????????????
            if ("uni-app".equals(loginType)) {
                MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByLoginName(username);
                data.put("miniRole", mchInfo != null ? mchInfo.getMiniRole() : null);
            }

            //????????????????????????
            rpcCommonService.rpcSysService.startWork(getUser().getUserId());
            return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
        }

        //web??????,???????????????????????????
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
        //????????????????????????
        rpcCommonService.rpcSysService.startWork(getUser().getUserId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * ????????????
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

        //???????????????????????????
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if (sysUser != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        //?????????????????????
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
        saveMchInfo.setAreaInfo("?????? ????????? ?????????");
        saveMchInfo.setAddress(mchName);
        saveMchInfo.setRegisterPassword(SpringSecurityUtil.generateSSPassword(password));
        saveMchInfo.setStatus(MchConstant.STATUS_AUDIT_ING); //??????????????? ????????????????????????
        rpcCommonService.rpcMchInfoService.add(saveMchInfo);

        //??????????????????????????????
        stringRedisTemplate.delete(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER + mobile);

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * ????????????(????????????????????????????????????)
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

        // ?????????????????????????????????token,????????????
        // ?????????ID+???????????????+?????? ???32???MD5???????????????
        String loginUserName = mchInfo.getLoginUserName();
        String secret = "Abc%$G&!!!128GMCH";
        
        String signNowTime = DateUtil.date2Str(new Date(), "ddHHmm"); //????????????
		String rawTokenByNow = mchId + loginUserName + secret + signNowTime;
		String md5Now = MD5Util.string2MD5(rawTokenByNow).toUpperCase();
		
		String sign1Time = DateUtil.date2Str(DateUtil.minusDateByMinute(new Date(), 1 ), "ddHHmm"); //??????????????????1min
		String rawTokenBySub1 = mchId + loginUserName + secret + sign1Time;
        String md5BySub1 = MD5Util.string2MD5(rawTokenBySub1).toUpperCase();
        
        //?????????????????? ??? ???????????????1???????????????????????????59s ???????????????????????????
        if(!md5Now.equals(token) && !md5BySub1.equals(token)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_ILLEGAL_LOGIN));
        }

        //????????????
        JWTBaseUser jwtBaseUser = (JWTBaseUser) userDetailsService.loadUserByUsername(mchId.toString());
        jwtBaseUser.setLoginTypeAndVersion(MchConstant.LOGIN_TYPE_WEB, null, null); //??????????????????
        String authToken = JWTUtils.generateToken(new JWTPayload(jwtBaseUser), mainConfig.getJwtSecret(), mainConfig.getJwtExpiration());
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + authToken, "1",
                MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

        JSONObject data = new JSONObject();
        data.put("access_token", authToken);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/logout")
    public ResponseEntity<?>  logout() {

        String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);
        stringRedisTemplate.delete(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + authToken);
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


    /**
     * ??????????????????
     * @return
     */
    @RequestMapping(value = "/auth/sms_send")
    public ResponseEntity<?> sendSms(){

        Byte bizType = getValByteRequired("bizType");//???????????? 22-??????

        // ????????????
        if (bizType != MchConstant.MSGCODE_BIZTPYE_MCH_REGISTER) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }

        String mobile = getValStringRequired("mobile");//?????????
        if(!StrUtil.checkMobileNumber(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }

        //???????????????????????????
        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        // ?????????????????????????????????????????????????????????
        if (sysUser != null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        // ??????????????????
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        logger.info("????????????{}?????????????????????{}", mobile, verifyCode);

        String sysConfigStr = rpcCommonService.rpcSysConfigService.getVal("smsConfig");
        if(!AliSmsUtil.sendSms(Long.parseLong(mobile), verifyCode, bizType, sysConfigStr)){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_SEND_ERROR));
        }

        // ????????????????????????redis
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + bizType + mobile, verifyCode,
                MchConstant.CACHEKEY_SMSCODE_TIMEOUT, TimeUnit.MINUTES);

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    /**
     * ????????????   ??????????????????
     * @return
     */
    @RequestMapping(value = "/auth/forget/sms_send")
    public ResponseEntity<?> forgetSendSms() {


        // ????????????
        String mobile = getValStringRequired( "mobile");

        if(!StrUtil.checkMobileNumber(mobile)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }

        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if(sysUser == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUBUSER_NOT_EXIST));
        }

        // ??????????????????
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
     * ????????????   ????????????
     * @return
     */
    @RequestMapping(value = "/auth/forget/pwd_update")
    public ResponseEntity<?> forgetPwdUpdate() {

        // ??????
        String mobile = getValStringRequired( "mobile");
        String smsCode = getValStringRequired( "smsCode");
        String password = getValStringRequired( "password");

        SysUser sysUser = rpcCommonService.rpcSysService.findByMchIdAndMobile(MchConstant.INFO_TYPE_MCH, mobile);
        if(sysUser == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUBUSER_NOT_EXIST));
        }

        // ????????????
        if(StringUtils.isEmpty(password) || !StrUtil.checkPassword(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }

        //?????????????????????
        if(StringUtils.isEmpty(smsCode)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        boolean verFlag = rpcCommonService.rpcMsgcodeService.verifyCode(mobile, smsCode, MchConstant.MSGCODE_BIZTPYE_MCH_LOGIN);
        if(!verFlag){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SMS_VERIFY_FAIL));
        }

        //????????????
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        sysUser.setLoginPassword(encoder.encode(password));
        sysUser.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * ?????????????????????
     */
    @RequestMapping(value = "/auth/vercode")
    public void getAuthCode( HttpServletResponse response, String vercodeRandomStr) throws IOException {
    	
    	if(StringUtils.isEmpty(vercodeRandomStr))return ;
        Map<String, Object> randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        String vercodeValue = randomMap.get("randomString").toString(); //??????
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_VERCODE_PREFIX_MCH + vercodeRandomStr,
        									  vercodeValue, MchConstant.CACHEKEY_VERCODE_TIMEOUT, TimeUnit.MINUTES);
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }

    /**
     * ??????????????????token
     */
    @RequestMapping(value = "/getBaiduToken")
    public ResponseEntity<?>  getBaiduToken() {

        String loginType = getUser().getLoginType();//?????????

        String token = null;
        if (StringUtils.isNotBlank(loginType) && loginType.equals(MchConstant.LOGIN_TYPE_APP)){
            token = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_UNIAPP_BAIDU_TOKEN);
            //redis???token????????????????????????token
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
            //redis???token????????????????????????token
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

    /** ?????????????????? **/
    @RequestMapping(value = "/clientVersion")
    public XxPayResponse clientVersion() {

        String clientType = getValStringRequired("clientType");  //???????????????
        String currentVersionSN = getValStringRequired("currentVersionSN");  //?????????????????????

        //??????????????????
        SysClientVersion currentVersionRecord = rpcCommonService.rpcSysClientVersionService.getOne(
                new QueryWrapper<SysClientVersion>().lambda()
                .eq(SysClientVersion:: getClientType, clientType)
                .eq(SysClientVersion:: getVersionSN, currentVersionSN)
        );

        if(currentVersionRecord == null){ //??????????????????????????????,  ???????????????????????? ???[????????????] ?????????
            List<SysClientVersion> newVersionList = rpcCommonService.rpcSysClientVersionService.page(
                    new Page<>(1, 1),
                    new QueryWrapper<SysClientVersion>().lambda().eq(SysClientVersion:: getClientType, clientType).orderByDesc(SysClientVersion:: getVid)
            ).getRecords();
            if(newVersionList != null && !newVersionList.isEmpty()){
                SysClientVersion newVersion = newVersionList.get(0);
                newVersion.setForceUpdate(MchConstant.PUB_YES);
                return XxPayResponse.buildSuccess(newVersion);
            }else{
                return XxPayResponse.buildSuccess(new JSONObject());  //???????????? ??????????????????
            }
        }

        // ?????????????????????????????? ????????????????????????????????????????????????
        List<SysClientVersion> newVersionList = rpcCommonService.rpcSysClientVersionService.page(
                new Page<>(1, 1),
                new QueryWrapper<SysClientVersion>().lambda()
                .eq(SysClientVersion:: getClientType, clientType)
                .gt(SysClientVersion:: getVid, currentVersionRecord.getVid())
                .orderByDesc(SysClientVersion:: getVid)
        ).getRecords();

        if(newVersionList == null || newVersionList.isEmpty()){ //???????????? ????????????
            return XxPayResponse.buildSuccess(new JSONObject());
        }

        //??????????????????
        SysClientVersion newVersion = newVersionList.get(0);

        //????????????????????????????????????????????? ??????????????????????????????????????????
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
     * ?????????????????????
     * @param mobile
     * @param verifyCode
     * @return
     */
    public boolean verifyMobileCode(String mobile, String verifyCode, Byte bizType) {

        if (StringUtils.isAnyBlank(mobile, verifyCode)) {
            return false;
        }

        //?????????????????????
        String redisCode = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_SMSCODE_PREFIX_MCH + bizType + mobile);
        if (verifyCode.equals(redisCode)) {
            return true;
        }

        return false;
    }

}
