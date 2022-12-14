package org.xxpay.mbr.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyAES;
import org.xxpay.core.common.util.MyBase64;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.*;
import org.xxpay.mbr.common.config.MainConfig;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;
import org.xxpay.mbr.secruity.JwtToken;
import org.xxpay.mbr.user.service.UserService;
import org.xxpay.mbr.user.service.WxAuthService;
import org.xxpay.mbr.wx.service.XxPayWxComponentService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    @Autowired
    private MainConfig mainConfig;

    @Autowired
    private WxAuthService wxAuthService;

    @Value("${config.wxOauth2CodeUrl}")
    private String wxOauth2CodeUrl;

    @Value("${config.mbrIndex}")
    private String mbrIndex;

    private static final MyLog _log = MyLog.getLog(AuthController.class);

    private String appId;
    private String appSecret;

    /**
     * ?????????????????????
     */
    @RequestMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<?> authLogin() {
        //??????
        String token;
        try {
            token = userService.login(1L);
        }catch (ServiceException e) {
            throw new ServiceException(e.getRetEnum());
        }

        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

        JSONObject data = new JSONObject();
        data.put("access_token", token);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * ??????H5????????????openId
     */
    @RequestMapping("/auth")
    @ResponseBody
    public void getWxOpenId() {

        JSONObject param = getReqParamJSON(); //????????????

        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.indexOf("micromessenger") == -1) {      //???????????????????????????
            throw new ServiceException(RetEnum.RET_MBR_USE_WECHAT);
        }

        //????????????????????????ID
        Long mchId = getValLongRequired("mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        if (mchInfo == null) {
            _log.error("=================?????????{}?????????=================", mchId);
            throw new ServiceException(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
        }

        //?????????ID
        Long isvId = mchInfo.getIsvId();
        PayPassage isvPassage = rpcCommonService.rpcPaypassageService.selectByIsv(isvId, PayConstant.CHANNEL_NAME_WXPAY);
        if (isvPassage != null && StringUtils.isNotBlank(isvPassage.getIsvParam())) {
            JSONObject isvParam = JSON.parseObject(isvPassage.getIsvParam());
            this.appId = isvParam.getString("appId");
            this.appSecret = isvParam.getString("appSecret");
        }else {
            _log.error("=================????????????????????????????????????????????????appId=================");
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        }

        //??????????????????
        String returnParamStr = MyBase64.encode(param.toJSONString().getBytes()); //base64
        String wxReturnUrl = String.format("%s/auth/wxPlatFormNotify/%s", mainConfig.getMbrApiUrl(), returnParamStr);

        //??????????????????
        String url = String.format(wxOauth2CodeUrl + "?appid=%s&scope=snsapi_base&state=&response_type=code&redirect_uri=%s", appId, StrUtil.urlEnodeUTF8(wxReturnUrl));
        _log.info("??????????????????code??????url???{}", url);

        try {
            response.sendRedirect(url); //????????????oauth??????
            _log.info("=================??????????????????code????????????=================");
        } catch (IOException e) {
            _log.error("", e);
        }
    }

    /**
     * ????????????oauth????????????????????????????????????????????????code?????????
     * @param returnParamStr ?????????????????????????????????base64?????????
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/auth/wxPlatFormNotify/{returnParamStr}")
    public void wxRedirectUrl(@PathVariable("returnParamStr") String returnParamStr) throws Exception {

        _log.info("===========??????????????????openId??????============");
        String wxReturnCode = request.getParameter("code"); //????????????????????????code, ????????????code??????accessToken
        JSONObject returnParamObj = JSONObject.parseObject(new String(MyBase64.decode(returnParamStr)));

        Long mchId = returnParamObj.getLong("mchId");

        //??????code ??????openId
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(appId);
        wxMpConfigStorage.setSecret(appSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(wxReturnCode);
        String openId = accessToken.getOpenId();

        //??????mchId???openId??????????????????????????????????????????????????????????????????????????????????????????
        Member member = rpcCommonService.rpcMemberService.getOne(
                new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getWxOpenId, openId));

        String redirectUrl;
        if (member != null && member.getStatus() == MchConstant.PUB_YES) {
            //??????
            String token;
            try {
                token = userService.login(member.getMemberId());
            }catch (ServiceException e) {
                throw new ServiceException(e.getRetEnum());
            }

            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                    MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
            //???????????????h5??????
            redirectUrl = String.format(mbrIndex + "#/member?mchId=%s&openId=%s&memberId=%s&access_token=%s"
                    ,mchId, openId, member.getMemberId(), URLEncoder.encode(token));
        } else {
            //???????????????h5??????
            redirectUrl = String.format(mbrIndex + "#/?mchId=%s&openId=%s", mchId, openId);
        }
        _log.info("==========????????????openId????????????, ??????url???{}===========", redirectUrl);

        response.sendRedirect(redirectUrl); //???????????????????????????
    }


    /**
     * ?????????????????????????????????????????????
     */
    @RequestMapping("/auth/getMemberCard")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long mchId = getValLongRequired("mchId");
        MchMemberConfig config = rpcCommonService.rpcMchMemberConfigService.getById(mchId);
        if(config == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_NO_CARD));
        }
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        JSONObject object = (JSONObject) JSON.toJSON(config);
        object.put("mchName", mchInfo.getMchName());
        object.put("mobile", mchInfo.getLoginMobile());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * ????????????????????????????????????
     */
    @RequestMapping("/auth/getMember")
    @ResponseBody
    public ResponseEntity<?> getMember() {

        //????????????
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(getUser().getMchId());

        //????????????
        JSONObject object = (JSONObject) JSON.toJSON(mchInfo);
        object.put("mchName", mchInfo.getMchName());//????????????

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * ????????????????????????
     */
    @RequestMapping("/auth/getStore")
    @ResponseBody
    public ResponseEntity<?> getStore() {
        Long mchId = getUser().getMchId();
        Long storeId = getValLong("storeId");
        String storeAreaId = getValString("storeAreaId");

        if (StringUtils.isNotBlank(storeAreaId)) {
            Long areaId = Long.valueOf(storeAreaId);
            MchStoreAreaManage storeAreaManage = rpcCommonService.rpcMchStoreAreaManageService.getById(areaId);
            if (storeAreaManage != null){
                storeId = storeAreaManage.getStoreId();
            }
        }

        LambdaQueryWrapper<MchStore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchStore::getMchId, mchId);
        if (storeId != null) queryWrapper.eq(MchStore::getStoreId, storeId);
        queryWrapper.orderByAsc(MchStore::getCreateTime);
        List<MchStore> list = rpcCommonService.rpcMchStoreService.list(queryWrapper);

        MchStore mchStore = list.get(0);
        mchStore.setStoreImgPath(StringUtils.isNotBlank(mchStore.getStoreImgPath()) ? mchStore.getStoreImgPath() : MchConstant.MCH_STORE_DEFAULT_AVATAR);
        mchStore.setMiniImgPath(StringUtils.isNotBlank(mchStore.getMiniImgPath()) ? mchStore.getMiniImgPath() : MchConstant.MCH_STORE_MINI_TOP_IMAGEPATH);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchStore));
    }

    /**
     * ???????????????
     */
    @RequestMapping("/auth/member/register")
    @ResponseBody
    public ResponseEntity<?> memberRegister() {

        Member member = getObject( Member.class);
        //??????????????? ?????????
        member.setStatus(MchConstant.PUB_YES);
        //?????????  ???????????????
        if (member.getSex() == null)member.setSex(MchConstant.SEX_UNKNOWN);

        //???????????????????????????????????????
        if (member.getTel() != null && rpcCommonService.rpcMemberService.getByMchIdAndTel(member) != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        int updateCount = rpcCommonService.rpcMemberService.initMember(member, 0L, 0L, null, null);
        if (updateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        //??????
        String token;
        try {
            token = userService.login(member.getMemberId());
        }catch (ServiceException e) {
            throw new ServiceException(e.getRetEnum());
        }

        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

        JSONObject data = new JSONObject();
        data.put("access_token", token);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * ????????????????????????openId???session_key
     */
    @RequestMapping("/auth/code2Session")
    @ResponseBody
    public ResponseEntity<?> code2Session() {

        String code = getValStringRequired("code");
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");//openid?????????1-???????????????
        String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);//????????????token???????????????

        JSONObject respJSON = new JSONObject();
        try {
            //authToken????????????????????????
            if(StringUtils.isBlank(authToken) || StringUtils.isBlank(stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + authToken))
                || JwtToken.parseToken(authToken, mainConfig.getJwtSecret()) == null){

                //??????session_key???openid
                JSONObject sessionJSON = xxPayWxComponentService.getSessionKey(mchId, authFrom, code);
                String openid = sessionJSON.getString("openid");
                String sessionKey = sessionJSON.getString("session_key");

                //??????openid???authFrom????????????
                MemberOpenidRela openidRela = rpcCommonService.rpcMemberOpenidRelaService.getOne(new QueryWrapper<MemberOpenidRela>().lambda()
                        .eq(MemberOpenidRela::getWxOpenId, openid)
                        .eq(MemberOpenidRela::getWxOpenIdFrom, authFrom)
                        .eq(MemberOpenidRela::getMchId, mchId)
                );

                //?????????????????????session_key??????redis????????????????????????????????????
                if (openidRela == null) {
                    //session_key??????redis??????????????????  key: $prefix + authFrom + openid
                    stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid, sessionKey,
                            MchConstant.CACHEKEY_SESSIONKEY_TIMEOUT, TimeUnit.MINUTES);

                    respJSON.put("openidData", MyAES.getInstance().encrypt(openid));
                }else {
                    //????????????????????????????????????????????????
                    Member member = rpcCommonService.rpcMemberService.getById(openidRela.getMemberId());
                    if (member != null && member.getStatus() == MchConstant.PUB_YES){
                        //??????token?????????redis
                        String token = userService.login(member.getMemberId());

                        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                                MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

                        respJSON.put("access_token", token);
                    }
                }
            }else {
                //authToken??????????????????????????????????????????????????????????????????openId??????????????????????????????????????????openId?????????
                Map<String, Object> map = JwtToken.parseToken(authToken, mainConfig.getJwtSecret());  //?????????token??????
                if (map == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

                //??????openId?????????????????????????????? ????????????
                Long memberId = (Long) map.get("memberId");
                //???????????????openId
                MemberOpenidRela openidRela = rpcCommonService.rpcMemberOpenidRelaService.getOne(new QueryWrapper<MemberOpenidRela>().lambda()
                        .eq(MemberOpenidRela::getMemberId, memberId)
                        .eq(MemberOpenidRela::getWxOpenIdFrom, authFrom)
                        .eq(MemberOpenidRela::getMchId, mchId)
                );

                //openId????????????
                if (openidRela == null || StringUtils.isBlank(openidRela.getWxOpenId())) {
                    //??????session_key???openid
                    JSONObject sessionJSON = xxPayWxComponentService.getSessionKey(mchId, authFrom, code);
                    String openid = sessionJSON.getString("openid");
                    String sessionKey = sessionJSON.getString("session_key");

                    //session_key??????redis??????????????????  key: $prefix + authFrom + openid
                    stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid, sessionKey,
                            MchConstant.CACHEKEY_SESSIONKEY_TIMEOUT, TimeUnit.MINUTES);

                    respJSON.put("openidData", MyAES.getInstance().encrypt(openid));
                }else {
                    respJSON.put("access_token", authToken);
                }
            }

            return ResponseEntity.ok(XxPayResponse.buildSuccess(respJSON));
        } catch (Exception e) {
            _log.error("??????session_key??????", e);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
    }

    /**
     * ?????????????????????????????????
     */
    @RequestMapping("/auth/getPhoneNum")
    @ResponseBody
    public ResponseEntity<?> getPhoneNum() {

        Long mchId = getValLongRequired("mchId");
        String openidData = getValStringRequired("openidData");
        String encrypdata = getValStringRequired("encrypdata");
        String ivdata = getValStringRequired("ivdata");
        Byte authFrom = getValByteRequired("authFrom");//openid?????????1-???????????????

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        if (mchInfo == null) {
            _log.error("=================?????????{}?????????=================", mchId);
            throw new ServiceException(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
        }

        try {
            //??????openid?????????sessionkey??????sessionkey??????????????????wx.login
            String openid = MyAES.getInstance().decrypt(openidData);
            String sessionKey = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid);
            if (StringUtils.isBlank(sessionKey)) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
            }

            //??????????????????????????????
            String result = xxPayWxComponentService.decrypt(encrypdata, ivdata, sessionKey);
            _log.info("=================????????????????????????{}", result);

            JSONObject resultJSON = (JSONObject) JSONObject.parse(result);
            if (StringUtils.isBlank(resultJSON.getString("purePhoneNumber"))) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
            }

            //??????
            String token;

            //????????????mchId????????????????????????????????????????????????????????????
            Member member = new Member();
            member.setMchId(mchId);
            member.setTel(resultJSON.getString("purePhoneNumber"));

            //????????????mchId????????????????????????
            Member hasMember = rpcCommonService.rpcMemberService.getByMchIdAndTel(member);

            //???????????????????????????????????????????????????????????????????????????openId??????????????????
            if (hasMember != null && hasMember.getStatus() == MchConstant.PUB_YES) {
                int saveOrUpdateCount = rpcCommonService.rpcMemberOpenidRelaService.saveOrUpdateRecord(hasMember.getMemberId(), openid, authFrom, mchId);
                if (saveOrUpdateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

                token = userService.login(hasMember.getMemberId());
            } else if (hasMember == null) {
                //????????????
                member.setMemberName(resultJSON.getString("purePhoneNumber"));
                //??????????????? ?????????
                member.setStatus(MchConstant.PUB_YES);
                //?????????  ???????????????
                if (member.getSex() == null) member.setSex(MchConstant.SEX_UNKNOWN);

                int updateCount = rpcCommonService.rpcMemberService.initMemberWithOpenid(member, 0L, 0L, null, null, authFrom, openid);
                if (updateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

                token = userService.login(member.getMemberId());
            }else {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
            }

            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                    MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

            JSONObject data = new JSONObject();
            data.put("access_token", token);

            return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
        }
    }


    /**
     * ???????????????access_token
     */
    @RequestMapping("/auth/getAccessToken")
    @ResponseBody
    public ResponseEntity<?> getAccessToken() {

        try {
            JSONArray roomInfo = wxAuthService.getLiveInfo(getPageIndex(), getPageSize());

            return ResponseEntity.ok(XxPayPageRes.buildSuccess(roomInfo, roomInfo.size()));
        } catch (Exception e) {
            logger.error(e, "???????????????{}");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
    }

}
