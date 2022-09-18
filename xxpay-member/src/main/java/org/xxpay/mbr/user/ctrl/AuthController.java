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
     * 小程序模拟登录
     */
    @RequestMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<?> authLogin() {
        //登录
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
     * 会员H5获取微信openId
     */
    @RequestMapping("/auth")
    @ResponseBody
    public void getWxOpenId() {

        JSONObject param = getReqParamJSON(); //请求参数

        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.indexOf("micromessenger") == -1) {      //不是来自微信客户端
            throw new ServiceException(RetEnum.RET_MBR_USE_WECHAT);
        }

        //从地址中获取商户ID
        Long mchId = getValLongRequired("mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        if (mchInfo == null) {
            _log.error("=================商户：{}不存在=================", mchId);
            throw new ServiceException(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
        }

        //服务商ID
        Long isvId = mchInfo.getIsvId();
        PayPassage isvPassage = rpcCommonService.rpcPaypassageService.selectByIsv(isvId, PayConstant.CHANNEL_NAME_WXPAY);
        if (isvPassage != null && StringUtils.isNotBlank(isvPassage.getIsvParam())) {
            JSONObject isvParam = JSON.parseObject(isvPassage.getIsvParam());
            this.appId = isvParam.getString("appId");
            this.appSecret = isvParam.getString("appSecret");
        }else {
            _log.error("=================服务商尚未配置微信通道，无法获取appId=================");
            throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        }

        //微信返回地址
        String returnParamStr = MyBase64.encode(param.toJSONString().getBytes()); //base64
        String wxReturnUrl = String.format("%s/auth/wxPlatFormNotify/%s", mainConfig.getMbrApiUrl(), returnParamStr);

        //本次跳转地址
        String url = String.format(wxOauth2CodeUrl + "?appid=%s&scope=snsapi_base&state=&response_type=code&redirect_uri=%s", appId, StrUtil.urlEnodeUTF8(wxReturnUrl));
        _log.info("获取会员微信code跳转url：{}", url);

        try {
            response.sendRedirect(url); //请求微信oauth接口
            _log.info("=================获取会员微信code跳转完成=================");
        } catch (IOException e) {
            _log.error("", e);
        }
    }

    /**
     * 请求微信oauth接口获取用户信息的回调地址（包含code参数）
     * @param returnParamStr 上一步封装的原样参数（base64格式）
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/auth/wxPlatFormNotify/{returnParamStr}")
    public void wxRedirectUrl(@PathVariable("returnParamStr") String returnParamStr) throws Exception {

        _log.info("===========进入获取微信openId回调============");
        String wxReturnCode = request.getParameter("code"); //微信服务器返回的code, 可以通过code换取accessToken
        JSONObject returnParamObj = JSONObject.parseObject(new String(MyBase64.decode(returnParamStr)));

        Long mchId = returnParamObj.getLong("mchId");

        //通过code 换取openId
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(appId);
        wxMpConfigStorage.setSecret(appSecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(wxReturnCode);
        String openId = accessToken.getOpenId();

        //根据mchId和openId获取会员，存在则登录并跳转会员主页，不存在则跳转领卡注册界面
        Member member = rpcCommonService.rpcMemberService.getOne(
                new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getWxOpenId, openId));

        String redirectUrl;
        if (member != null && member.getStatus() == MchConstant.PUB_YES) {
            //登录
            String token;
            try {
                token = userService.login(member.getMemberId());
            }catch (ServiceException e) {
                throw new ServiceException(e.getRetEnum());
            }

            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                    MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
            //封装跳转到h5参数
            redirectUrl = String.format(mbrIndex + "#/member?mchId=%s&openId=%s&memberId=%s&access_token=%s"
                    ,mchId, openId, member.getMemberId(), URLEncoder.encode(token));
        } else {
            //封装跳转到h5参数
            redirectUrl = String.format(mbrIndex + "#/?mchId=%s&openId=%s", mchId, openId);
        }
        _log.info("==========会员微信openId获取完成, 跳转url：{}===========", redirectUrl);

        response.sendRedirect(redirectUrl); //携带参数，跳转页面
    }


    /**
     * 未登录时，获取会员卡和商户信息
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
     * 未登录时，仅获取商户信息
     */
    @RequestMapping("/auth/getMember")
    @ResponseBody
    public ResponseEntity<?> getMember() {

        //商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(getUser().getMchId());

        //前端返回
        JSONObject object = (JSONObject) JSON.toJSON(mchInfo);
        object.put("mchName", mchInfo.getMchName());//商户名称

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 获取商户门店信息
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
     * 领取会员卡
     */
    @RequestMapping("/auth/member/register")
    @ResponseBody
    public ResponseEntity<?> memberRegister() {

        Member member = getObject( Member.class);
        //默认状态为 可用。
        member.setStatus(MchConstant.PUB_YES);
        //性别空  则加默认值
        if (member.getSex() == null)member.setSex(MchConstant.SEX_UNKNOWN);

        //同一商户下，会员手机号唯一
        if (member.getTel() != null && rpcCommonService.rpcMemberService.getByMchIdAndTel(member) != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        int updateCount = rpcCommonService.rpcMemberService.initMember(member, 0L, 0L, null, null);
        if (updateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        //登录
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
     * 代小程序实现获取openId和session_key
     */
    @RequestMapping("/auth/code2Session")
    @ResponseBody
    public ResponseEntity<?> code2Session() {

        String code = getValStringRequired("code");
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");//openid来源，1-餐饮小程序
        String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);//前端传来token，可能为空

        JSONObject respJSON = new JSONObject();
        try {
            //authToken无效，需重新登录
            if(StringUtils.isBlank(authToken) || StringUtils.isBlank(stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + authToken))
                || JwtToken.parseToken(authToken, mainConfig.getJwtSecret()) == null){

                //获取session_key和openid
                JSONObject sessionJSON = xxPayWxComponentService.getSessionKey(mchId, authFrom, code);
                String openid = sessionJSON.getString("openid");
                String sessionKey = sessionJSON.getString("session_key");

                //通过openid和authFrom查找用户
                MemberOpenidRela openidRela = rpcCommonService.rpcMemberOpenidRelaService.getOne(new QueryWrapper<MemberOpenidRela>().lambda()
                        .eq(MemberOpenidRela::getWxOpenId, openid)
                        .eq(MemberOpenidRela::getWxOpenIdFrom, authFrom)
                        .eq(MemberOpenidRela::getMchId, mchId)
                );

                //未查询到用户，session_key存入redis，等待用户获取手机号注册
                if (openidRela == null) {
                    //session_key存入redis，有效期一天  key: $prefix + authFrom + openid
                    stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid, sessionKey,
                            MchConstant.CACHEKEY_SESSIONKEY_TIMEOUT, TimeUnit.MINUTES);

                    respJSON.put("openidData", MyAES.getInstance().encrypt(openid));
                }else {
                    //查询到用户，若用户未登录，则登录
                    Member member = rpcCommonService.rpcMemberService.getById(openidRela.getMemberId());
                    if (member != null && member.getStatus() == MchConstant.PUB_YES){
                        //生成token，存入redis
                        String token = userService.login(member.getMemberId());

                        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR + token, "1",
                                MchConstant.APP_CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);

                        respJSON.put("access_token", token);
                    }
                }
            }else {
                //authToken有效，无需执行登录流程，但需校验该用户有没有openId信息（小程序重新授权时会删除openId信息）
                Map<String, Object> map = JwtToken.parseToken(authToken, mainConfig.getJwtSecret());  //反解析token信息
                if (map == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

                //判断openId有没有，没有：注册， 有：登录
                Long memberId = (Long) map.get("memberId");
                //查找用户的openId
                MemberOpenidRela openidRela = rpcCommonService.rpcMemberOpenidRelaService.getOne(new QueryWrapper<MemberOpenidRela>().lambda()
                        .eq(MemberOpenidRela::getMemberId, memberId)
                        .eq(MemberOpenidRela::getWxOpenIdFrom, authFrom)
                        .eq(MemberOpenidRela::getMchId, mchId)
                );

                //openId信息为空
                if (openidRela == null || StringUtils.isBlank(openidRela.getWxOpenId())) {
                    //获取session_key和openid
                    JSONObject sessionJSON = xxPayWxComponentService.getSessionKey(mchId, authFrom, code);
                    String openid = sessionJSON.getString("openid");
                    String sessionKey = sessionJSON.getString("session_key");

                    //session_key存入redis，有效期一天  key: $prefix + authFrom + openid
                    stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid, sessionKey,
                            MchConstant.CACHEKEY_SESSIONKEY_TIMEOUT, TimeUnit.MINUTES);

                    respJSON.put("openidData", MyAES.getInstance().encrypt(openid));
                }else {
                    respJSON.put("access_token", authToken);
                }
            }

            return ResponseEntity.ok(XxPayResponse.buildSuccess(respJSON));
        } catch (Exception e) {
            _log.error("获取session_key失败", e);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
    }

    /**
     * 小程序获取手机号并登陆
     */
    @RequestMapping("/auth/getPhoneNum")
    @ResponseBody
    public ResponseEntity<?> getPhoneNum() {

        Long mchId = getValLongRequired("mchId");
        String openidData = getValStringRequired("openidData");
        String encrypdata = getValStringRequired("encrypdata");
        String ivdata = getValStringRequired("ivdata");
        Byte authFrom = getValByteRequired("authFrom");//openid来源，1-餐饮小程序

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        if (mchInfo == null) {
            _log.error("=================商户：{}不存在=================", mchId);
            throw new ServiceException(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
        }

        try {
            //解密openid，获取sessionkey，若sessionkey失效，应重新wx.login
            String openid = MyAES.getInstance().decrypt(openidData);
            String sessionKey = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_SESSIONKEY_PREFIX + authFrom + "_" + openid);
            if (StringUtils.isBlank(sessionKey)) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
            }

            //解密数据，获取手机号
            String result = xxPayWxComponentService.decrypt(encrypdata, ivdata, sessionKey);
            _log.info("=================解密手机号结果：{}", result);

            JSONObject resultJSON = (JSONObject) JSONObject.parse(result);
            if (StringUtils.isBlank(resultJSON.getString("purePhoneNumber"))) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GET_TEL_ERROR));
            }

            //登录
            String token;

            //根据商户mchId和手机号查找会员，存在则登录，不存在新增
            Member member = new Member();
            member.setMchId(mchId);
            member.setTel(resultJSON.getString("purePhoneNumber"));

            //根据商户mchId和手机号查找会员
            Member hasMember = rpcCommonService.rpcMemberService.getByMchIdAndTel(member);

            //查到会员，再查询是否在当前小程序下已注册：即已保存openId，再执行登录
            if (hasMember != null && hasMember.getStatus() == MchConstant.PUB_YES) {
                int saveOrUpdateCount = rpcCommonService.rpcMemberOpenidRelaService.saveOrUpdateRecord(hasMember.getMemberId(), openid, authFrom, mchId);
                if (saveOrUpdateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

                token = userService.login(hasMember.getMemberId());
            } else if (hasMember == null) {
                //注册会员
                member.setMemberName(resultJSON.getString("purePhoneNumber"));
                //默认状态为 可用。
                member.setStatus(MchConstant.PUB_YES);
                //性别空  则加默认值
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
     * 小程序获取access_token
     */
    @RequestMapping("/auth/getAccessToken")
    @ResponseBody
    public ResponseEntity<?> getAccessToken() {

        try {
            JSONArray roomInfo = wxAuthService.getLiveInfo(getPageIndex(), getPageSize());

            return ResponseEntity.ok(XxPayPageRes.buildSuccess(roomInfo, roomInfo.size()));
        } catch (Exception e) {
            logger.error(e, "请求失败：{}");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
    }

}
