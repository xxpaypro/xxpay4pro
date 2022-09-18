package org.xxpay.isv.components;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.core.entity.MchMiniVersion;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.core.entity.MemberOpenidRela;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** 用于接收微信消息的ctrl */
@Controller
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wxCallback")
public class WxCallbackController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxCallbackController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 描述：  第三方入驻成功后， 接收微信第三方的验证票据（component_verify_ticket）
     * 接口文档： https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/component_verify_ticket.html
     *
     */
    @RequestMapping("/componentTicket/{isvId}")
    @ResponseBody
    public Object componentTicket(@PathVariable("isvId") Long isvId,
                                @RequestBody(required = false) String requestBody, @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        try {
            logger.info("接收微信请求 isvId={}：[signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[{}] ", isvId, signature, encType, msgSignature, timestamp, nonce, requestBody);

            //查询当前服务商的 [第三方] 配置信息
            IsvWx3rdInfo isvWx3rdInfo = rpc.rpcIsvWx3rdInfoService.getById(isvId);
            if(isvWx3rdInfo == null){  //未配置
                logger.error("未查询到服务商第三方配置信息, isvId={}", isvId);
                return "error[isv config not exists]";
            }

            //当存在空值时
            if(StringUtils.isAnyEmpty(isvWx3rdInfo.getComponentAppId(), isvWx3rdInfo.getComponentAppSecret())){
                logger.error("服务商第三方配置信息有误, isvId={}", isvId);
                return "error[isv config is null]";
            }

            WxOpenService wxOpenService = new WxOpenServiceImpl();
            WxOpenInMemoryConfigStorage config = new WxOpenInMemoryConfigStorage();
            config.setComponentAppId(isvWx3rdInfo.getComponentAppId());
            config.setComponentAppSecret(isvWx3rdInfo.getComponentAppSecret());
            config.setComponentToken(isvWx3rdInfo.getConfigMsgToken());
            config.setComponentAesKey(isvWx3rdInfo.getConfigAesKey());
            wxOpenService.setWxOpenConfigStorage(config);
            if (!StringUtils.equalsIgnoreCase("aes", encType) || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
                throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
            }

            // aes解密
            WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
            logger.debug("消息解密后内容为：{} ", inMessage.toString());

            //获取到的component_verify_ticket，存入数据库
            if ("component_verify_ticket".equals(inMessage.getInfoType())){

                String componentVerifyTicket = inMessage.getComponentVerifyTicket();

                //当前账号已停用 || 待录入信息
                if(isvWx3rdInfo.getStatus() == MchConstant.ISV3RD_STATUS_STOP || isvWx3rdInfo.getStatus() == MchConstant.ISV3RD_STATUS_WAIT_UPLOAD_INFO){
                    logger.debug("服务商第三方账号状态不可用 isvId={}, status={}", isvId, isvWx3rdInfo.getStatus());
                    return "error[isv Unavailable]";
                }

                if(isvWx3rdInfo.getStatus() == MchConstant.ISV3RD_STATUS_OK){  //已完成账号验证， 微信再次发送消息

                    //保存最新ticket
                    IsvWx3rdInfo updateRecord = new IsvWx3rdInfo();
                    updateRecord.setIsvId(isvId); updateRecord.setComponentVerifyTicket(componentVerifyTicket);
                    rpc.rpcIsvWx3rdInfoService.updateById(updateRecord);
                    return "success";
                }

                //当前状态状态为：  待验证 || 验证错误
                try {
                    //获取 & 重新设置缓存中的 accessToken
                    xxPayWxComponentService.queryAndSetCacheAccessToken(isvId,
                                            isvWx3rdInfo.getComponentAppId(), isvWx3rdInfo.getComponentAppSecret(), componentVerifyTicket);
                    IsvWx3rdInfo updateRecord = new IsvWx3rdInfo();
                    updateRecord.setIsvId(isvId);
                    updateRecord.setComponentVerifyTicket(componentVerifyTicket);
                    updateRecord.setStatus(MchConstant.ISV3RD_STATUS_OK);  //验证通过
                    rpc.rpcIsvWx3rdInfoService.updateById(updateRecord);

                } catch (Exception e) {
                    logger.error("验证有误！ ", e);
                    IsvWx3rdInfo updateRecord = new IsvWx3rdInfo();
                    updateRecord.setIsvId(isvId); updateRecord.setStatus(MchConstant.ISV3RD_STATUS_ERROR);  //验证信息有误
                    rpc.rpcIsvWx3rdInfoService.updateById(updateRecord);
                }
                return "success";

            //商户小程序创建后的通知推送
            //创建成功后，拿到授权信息，刷新token存入数据库，access_token存入redis
            }else if("notify_third_fasteregister".equals(inMessage.getInfoType()) && inMessage.getStatus() == 0){

                logger.debug("商户小程序创建成功后的的推送, param={}", inMessage.toString());

                String authCode = inMessage.getAuthCode();     //授权Code
                WxOpenComponentService wxService = xxPayWxComponentService.getBinarywangWxOpenComponentService(isvId);

                //查询授权的商户信息
                WxOpenQueryAuthResult result = wxService.getQueryAuth(authCode);
                logger.debug("商户小程序授权通过后结果：{}", result.getAuthorizationInfo().toString());

                //满足条件：
                String mchAuthAppId = result.getAuthorizationInfo().getAuthorizerAppid();  //商户授权账号的 appId
                String mchAuthAccessToken = result.getAuthorizationInfo().getAuthorizerAccessToken(); //授权账号accessToken
                String mchAuthRefreshToken = result.getAuthorizationInfo().getAuthorizerRefreshToken();  //授权账号refreshToken
                Integer mchAuthExpiresIn = result.getAuthorizationInfo().getExpiresIn();  //accessToken的过期时间
                List<Integer> funcList = result.getAuthorizationInfo().getFuncInfo();  //授权集列表

                if (!funcList.contains(17) || !funcList.contains(18) || !funcList.contains(25) ||
                        !funcList.contains(30) || !funcList.contains(37)
                        || !funcList.contains(40) || !funcList.contains(49)) {
                    //权限不满足条件；
                    return "error[mch authlist_not_enough]";
                }

                //根据企业名  企业代码  法人姓名  法人微信查询24h内已提交注册的小程序
                MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOne(new QueryWrapper<MchWxauthInfo>().lambda()
                        .eq(MchWxauthInfo::getLegalPersonaName, inMessage.getInfo().getLegalPersonaName())
                        .eq(MchWxauthInfo::getLegalPersonaWechat, inMessage.getInfo().getLegalPersonaWechat())
                        .eq(MchWxauthInfo::getBussinessCode, inMessage.getInfo().getCode())
                        .eq(MchWxauthInfo::getBussinessName, inMessage.getInfo().getName())
                        .eq(MchWxauthInfo::getAuthStatus, MchConstant.MCH_WXAUTH_AUTHSTATUS_REGISTER)
                        .ge(MchWxauthInfo::getCreateTime, DateUtil.addDay(new Date(), 1))
                );

                if (mchWxauthInfo == null) {
                    logger.error("未查询到商户第三方授权信息, isvId={}, 企业名称={}, 法人姓名={}", isvId, inMessage.getInfo().getName(), inMessage.getInfo().getName(), inMessage.getInfo().getLegalPersonaName());
                    return "error[mch wxauth info not exists]";
                }

                MchWxauthInfo updateRecord = new MchWxauthInfo();
                updateRecord.setId(mchWxauthInfo.getId());
                updateRecord.setAuthAppId(inMessage.getRegistAppId());
                updateRecord.setAuthRefreshToken(mchAuthRefreshToken);
                updateRecord.setAuthFuncInfo(JSONArray.toJSON(funcList).toString());
                updateRecord.setAuthStatus(MchConstant.MCH_WXAUTH_AUTHSTATUS_SUCCESS);
                if (!rpc.rpcMchWxauthInfoService.updateById(updateRecord)){
                    return "error[mch wxauth info update error]";
                }

                //authorizer_access_token放置redis
                Integer redisExpiresIn = mchAuthExpiresIn - 60;  //redis过期时间为 默认时间减去1分钟, 避免接口传输时间导致已失效问题
                stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_WX_AUTH_ACCESS_TOKEN + mchWxauthInfo.getMchId() + "_" + mchWxauthInfo.getAuthFrom() + "_" + mchAuthAppId,
                        mchAuthAccessToken, redisExpiresIn, TimeUnit.SECONDS);

                return "success";
            }else {
                return "error";
            }
        }catch (Exception e){
            logger.error("error:", e);
            return "error["+e.getMessage()+"]";
        }
    }



    /**
     * 描述：  消息接收事件URL
     *
     */
    @RequestMapping("/{appId}/msgCallBack/{isvId}")
    @ResponseBody
    public Object msgCallBack(@PathVariable("isvId") Long isvId, @PathVariable("APPID") String appId,
                                  @RequestBody(required = false) String requestBody, @RequestParam("timestamp") String timestamp,
                                  @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                                  @RequestParam(name = "encrypt_type", required = false) String encType,
                                  @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        try {
            logger.info("接收微信消息事件请求 isvId={}：[signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[{}] ", isvId, signature, encType, msgSignature, timestamp, nonce, requestBody);

            //查询当前服务商的 [第三方] 配置信息
            IsvWx3rdInfo isvWx3rdInfo = rpc.rpcIsvWx3rdInfoService.getById(isvId);
            if(isvWx3rdInfo == null){  //未配置
                logger.error("未查询到服务商第三方配置信息, isvId={}", isvId);
                return "error[isv config not exists]";
            }

            //当存在空值时
            if(StringUtils.isAnyEmpty(isvWx3rdInfo.getComponentAppId(), isvWx3rdInfo.getComponentAppSecret())){
                logger.error("服务商第三方配置信息有误, isvId={}", isvId);
                return "error[isv config is null]";
            }

            WxOpenService wxOpenService = new WxOpenServiceImpl();
            WxOpenInMemoryConfigStorage config = new WxOpenInMemoryConfigStorage();
            config.setComponentAppId(isvWx3rdInfo.getComponentAppId());
            config.setComponentAppSecret(isvWx3rdInfo.getComponentAppSecret());
            config.setComponentToken(isvWx3rdInfo.getConfigMsgToken());
            config.setComponentAesKey(isvWx3rdInfo.getConfigAesKey());
            wxOpenService.setWxOpenConfigStorage(config);
            if (!StringUtils.equalsIgnoreCase("aes", encType) || !wxOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
                throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
            }

            // aes解密
            WxMpXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody, wxOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
            logger.info("消息事件解密后内容为：{} ", inMessage.toString());

            if ("weapp_audit_success".equals(inMessage.getEvent())){
                MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByIsvIdAndAppIdAndAudits(isvId, appId, MchConstant.MCH_MINI_AUDIT_STATUS_ING);
                if (mchMiniVersion == null) return "error[mch version error]";

                mchMiniVersion.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_SUCCESS);
                rpc.rpcMchMiniVersionService.updateById(mchMiniVersion);

            }else if ("weapp_audit_fail".equals(inMessage.getEvent())){
                MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByIsvIdAndAppIdAndAudits(isvId, appId, MchConstant.MCH_MINI_AUDIT_STATUS_ING);
                if (mchMiniVersion == null) return "error[mch version error]";

                mchMiniVersion.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_FAIL);
                mchMiniVersion.setRefusedReason(inMessage.getReason());
                rpc.rpcMchMiniVersionService.updateById(mchMiniVersion);
            }else if ("weapp_audit_delay".equals(inMessage.getEvent())){
                MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByIsvIdAndAppIdAndAudits(isvId, appId, MchConstant.MCH_MINI_AUDIT_STATUS_ING);
                if (mchMiniVersion == null) return "error[mch version error]";

                mchMiniVersion.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_DELAY);
                mchMiniVersion.setRefusedReason(inMessage.getReason());
                rpc.rpcMchMiniVersionService.updateById(mchMiniVersion);
            }

            return "success";
        }catch (Exception e){
            logger.error("error:", e);
            return "error["+e.getMessage()+"]";
        }
    }



    /** 商户授权通过后的回调地址 authType: 1:微信公众号授权， 2小程序 ; authFrom: 1-餐饮小程序**/
    @RequestMapping("/mchAuth/{authFrom}/{authType}/{isvId}/{mchId}")
    public String mchAuth(@PathVariable("authFrom") Byte authFrom, @PathVariable("authType") Byte authType, @PathVariable("isvId") Long isvId, @PathVariable("mchId") Long mchId) {

        try {

            logger.info("商户授权成功后的的请求, isvId={}, mchId={}, param={}", isvId, mchId, getReqParamJSON());

            String authCode = getValStringRequired("auth_code");     //授权Code
            String expiresIn = getValStringRequired("expires_in");  //有效期

            WxOpenComponentService wxOpenService = xxPayWxComponentService.getBinarywangWxOpenComponentService(isvId);

            //查询授权的商户信息
            WxOpenQueryAuthResult result = wxOpenService.getQueryAuth(authCode);
            logger.info("商户授权通过后查询结果：{}", result);

            //满足条件：
            String mchAuthAppId = result.getAuthorizationInfo().getAuthorizerAppid();  //商户授权账号的 appId
            String mchAuthAccessToken = result.getAuthorizationInfo().getAuthorizerAccessToken(); //授权账号accessToken
            String mchAuthRefreshToken = result.getAuthorizationInfo().getAuthorizerRefreshToken();  //授权账号refreshToken
            Integer mchAuthExpiresIn = result.getAuthorizationInfo().getExpiresIn();  //accessToken的过期时间
            List<Integer> funcList = result.getAuthorizationInfo().getFuncInfo();  //授权集列表

            // 判断权限集合 是否满足业务需求：
            //1、消息管理权限 2、用户管理权限 3、帐号服务权限 4、网页服务权限 5、微信小店权限 6、微信多客服权限 7、群发与通知权限
            // 8、微信卡券权限 9、微信扫一扫权限 10、微信连WIFI权限 11、素材管理权限 12、微信摇周边权限 13、微信门店权限
            // 15、自定义菜单权限 16、获取认证状态及信息 17、帐号管理权限（小程序） 18、开发管理与数据分析权限（小程序）
            // 19、客服消息管理权限（小程序） 20、微信登录权限（小程序） 21、数据分析权限（小程序） 22、城市服务接口权限
            // 23、广告管理权限 24、开放平台帐号管理权限 25、 开放平台帐号管理权限（小程序） 26、微信电子发票权限
            // 41、搜索widget的权限
            // 请注意： 1）该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。

            if(authType == MchConstant.WX_AUTH_TYPE_MP){  //公众号授权
                if(!funcList.contains(2) || !funcList.contains(3) || !funcList.contains(4)  || !funcList.contains(8)
                        || !funcList.contains(13) || !funcList.contains(23)|| !funcList.contains(24))
                       {
                    //权限不满足条件；
                    throw ServiceException.build(RetEnum.RET_MCH_AUTHLIST_NOT_ENOUGH);
                }
            }else if(authType == MchConstant.WX_AUTH_TYPE_MINI) {  //小程序授权
                /*if (!funcList.contains(17) || !funcList.contains(18) || !funcList.contains(25) ||
                        !funcList.contains(30) || !funcList.contains(37)
                        || !funcList.contains(40) || !funcList.contains(49)) {
                    //权限不满足条件；
                    throw ServiceException.build(RetEnum.RET_MCH_AUTHLIST_NOT_ENOUGH);
                }*/
            }

            //绑定
            MchWxauthInfo record = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);

            if(record == null ){ //新增授权信息
                MchWxauthInfo mchWxauthInfo = new MchWxauthInfo();
                mchWxauthInfo.setMchId(mchId);
                mchWxauthInfo.setIsvId(isvId);
                mchWxauthInfo.setAuthAppId(mchAuthAppId);
                mchWxauthInfo.setAuthFuncInfo(JSONArray.toJSON(funcList).toString());
                mchWxauthInfo.setAuthRefreshToken(mchAuthRefreshToken);
                mchWxauthInfo.setAuthStatus(MchConstant.MCH_WXAUTH_AUTHSTATUS_SUCCESS);
                mchWxauthInfo.setAuthFrom(authFrom);
                mchWxauthInfo.setAuthType(authType);
                rpc.rpcMchWxauthInfoService.save(mchWxauthInfo);
            }else{
                //更新授权信息
                MchWxauthInfo updateRecord = new MchWxauthInfo();
                updateRecord.setId(record.getId());
                updateRecord.setIsvId(isvId);
                updateRecord.setMchId(mchId);
                updateRecord.setAuthAppId(mchAuthAppId);
                updateRecord.setAuthFuncInfo(JSONArray.toJSON(funcList).toString());
                updateRecord.setAuthRefreshToken(mchAuthRefreshToken);
                updateRecord.setAuthStatus(MchConstant.MCH_WXAUTH_AUTHSTATUS_SUCCESS);
                updateRecord.setAuthFrom(authFrom);
                updateRecord.setAuthType(authType);
                rpc.rpcMchWxauthInfoService.updateById(updateRecord);

                //删除原授权小程序对应的用户openid信息
                rpc.rpcMemberOpenidRelaService.remove(new LambdaQueryWrapper<MemberOpenidRela>()
                        .eq(MemberOpenidRela::getMchId, mchId)
                        .eq(MemberOpenidRela::getWxOpenIdFrom, authFrom)
                );
            }

            //authorizer_access_token放置redis
            Integer redisExpiresIn = mchAuthExpiresIn - 60;  //redis过期时间为 默认时间减去1分钟, 避免接口传输时间导致已失效问题
            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_WX_AUTH_ACCESS_TOKEN + mchId + "_" + authFrom + "_" + mchAuthAppId,
                    mchAuthAccessToken, redisExpiresIn, TimeUnit.SECONDS);

//            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getById(mchId);
//            String mchBindOpenAppId = mchWxauthInfo == null ? null : mchWxauthInfo.getOpenAppId();
//
//
//            //查询当前公众号  / 小程序  是否绑定了开放平台
//            String authAccountOpenAppId = "";
//
//            //商户已关联开放平台 && 授权账号已绑定
//            if(StringUtils.isNotEmpty(mchBindOpenAppId) && StringUtils.isNotEmpty(authAccountOpenAppId)) {
//
//                if (mchBindOpenAppId.equals(authAccountOpenAppId)) {  //如果一致， 不做任何判断
//
//                } else {
//                    //TODO 提示错误信息，  无法继续操作， 请先解除开放平台后再继续
//                }
//            }
//
//            //商户已关联开放平台  && 授权账号未绑定
//            if(StringUtils.isNotEmpty(mchBindOpenAppId) && StringUtils.isEmpty(authAccountOpenAppId)){
//                //TODO 绑定开放平台  调起接口
//            }
//
//
//            //商户未关联开放平台  && 授权账号已绑定
//            if(StringUtils.isEmpty(mchBindOpenAppId) && StringUtils.isNotEmpty(authAccountOpenAppId)) {
//                //TODO 将当前商户关联开放平台 绑定到 该开放平台
//            }
//
//            //商户未关联开放平台 && 授权账号未绑定
//            if(StringUtils.isEmpty(mchBindOpenAppId) && StringUtils.isEmpty(authAccountOpenAppId)) {
//                //TODO 新建开放平台，并绑定
//            }

        }catch (ServiceException e){
            request.setAttribute("errMsg", e.getRetEnum().getMessage());
        }catch (Exception e){
            logger.error("error:", e);
            request.setAttribute("errMsg", e.getMessage());
        }

        return "wx/auth";

    }



}