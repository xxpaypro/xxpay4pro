package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx_auth")
public class WxAuthController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxAuthController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    /**
     * 公众号/小程序授权
     * authType: 1-维信公众号  2-小程序
     * authFrom: 1-餐饮小程序
     * @return
     */
    @RequestMapping("/wxAuth")
    public XxPayResponse wxAuth() {
        try {
            Long mchId = getValLongRequired("mchId");
            Byte authType = getValByteRequired("authType");
            Byte authFrom = getValByteRequired("authFrom");
            Long currentIsvId = getUser().getBelongInfoId();

            if (authType != MchConstant.WX_AUTH_TYPE_MP && authType != MchConstant.WX_AUTH_TYPE_MINI){
                return XxPayResponse.build(RetEnum.RET_ISV_NOT_SUPPORT_WX_ACCOUNT);
            }

            if (authFrom != MchConstant.MEMBER_OPENID_FROM_MINI_FOOD && authFrom != MchConstant.MEMBER_OPENID_FROM_MINI_MALL
                    && authFrom != MchConstant.MEMBER_OPENID_FROM_MINI_PAY ){
                return XxPayResponse.build(RetEnum.RET_ISV_NOT_SUPPORT_WX_AUTHFROM);
            }

            if (rpc.rpcMchInfoService.getById(mchId) == null){
                return XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST);
            }

            //拼接授权微信回调地址 参数： isvHttpHost, isvId, mchId
            String redirectUrl = String.format("%s/wxCallback/mchAuth/%s/%s/%s/%s", mainConfig.getIsvApiUrl(), authFrom, authType, currentIsvId, mchId);
            logger.info("授权回调地址：{}", redirectUrl);
            String authUrl;

            WxOpenComponentService service = xxPayWxComponentService.getBinarywangWxOpenComponentService(currentIsvId);

            if (MchConstant.WX_AUTH_TYPE_MP == authType) {//公众号授权
                authUrl = service.getMobilePreAuthUrl(redirectUrl, authType.toString(), "");    // authType: 1.公众号, 2.小程序, 3.全部
            }else if (MchConstant.WX_AUTH_TYPE_MINI == authType){//小程序授权
                authUrl = service.getMobilePreAuthUrl(redirectUrl, authType.toString(), "");    // authType: 1.公众号, 2.小程序, 3.全部
            }else {
                return XxPayResponse.build(RetEnum.RET_ISV_NOT_SUPPORT_WX_ACCOUNT);
            }

            String url = mainConfig.getPayUrl() +  "/qrcode_img_get?url=" + URLEncoder.encode(authUrl) + "&widht=200&height=200";
            return XxPayResponse.buildSuccess(url);

        }catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        } catch (WxErrorException e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }  catch (Exception e) {
            logger.error("error", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询商户的授权列表  ***/
    @RequestMapping("/list")
    public XxPayResponse list() {

        Long mchId = getValLongRequired("mchId");

        List<MchWxauthInfo> list = rpc.rpcMchWxauthInfoService.list(new QueryWrapper<MchWxauthInfo>().lambda()
            .eq(MchWxauthInfo::getMchId, mchId));

        if (list == null || list.isEmpty()){
            return XxPayResponse.buildSuccess();
        }

        for(int i = 0 ; i < list.size() ; i++) {
            if (list.get(i).getAuthStatus() == MchConstant.MCH_WXAUTH_AUTHSTATUS_REGISTER  &&
                    list.get(i).getCreateTime().getTime() < DateUtil.addDay(new Date(), -1).getTime()) {
                list.remove(i);
                i--;
            }
        }

        return XxPayResponse.buildSuccess(list);
    }


    /** 查询商户的授权信息  ***/
    @RequestMapping("/getMchWxAuthInfo")
    public XxPayResponse getMchWxAuthInfo() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
        return XxPayResponse.buildSuccess(mchWxauthInfo);
    }



    /** 更新开放平台 ID   ***/
    @RequestMapping("/updateOpenAppId")
    public XxPayResponse updateOpenAppId() {

        Long mchId = getValLongRequired("mchId");
        String openAppId = getValStringRequired("openAppId");

        MchWxauthInfo updateRecord = new MchWxauthInfo();
        updateRecord.setMchId(mchId);
        updateRecord.setOpenAppId(openAppId);
        if(rpc.rpcMchWxauthInfoService.updateById(updateRecord)){
            return XxPayResponse.buildSuccess();
        }
        return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }


    /**查询openAppId （查询微信接口最新信息）  ***/
    @RequestMapping("/wx/queryOpenAppId")
    public XxPayResponse wxQueryOpenAppId() {

        try {
            Byte authFrom = getValByteRequired("authFrom");

            JSONObject reqParam = new JSONObject();
            reqParam.put("appid", getValStringRequired("authAppId"));
            JSONObject responseJSON = xxPayWxComponentService.reqWXByMch(getValLongRequired("mchId"), authFrom,
                                                    XxPayWxComponentService.API_COMPONENT_OPEN_GET, reqParam);
            return XxPayResponse.buildSuccess(responseJSON.getString("open_appid"));

        } catch (WxErrorException e) {

            if(e.getError().getErrorCode() == 89002){ //该账号没有绑定任何开放平台
                return XxPayResponse.buildSuccess();
            }else{
                logger.error("调起微信接口失败！error: ", e);
                throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
            }

        } catch (Exception e) {
            logger.error("系统异常！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 创建开放平台  ***/
    @RequestMapping("/wx/createOpenApp")
    public XxPayResponse createOpenApp() {

        try {

            Byte authFrom = getValByteRequired("authFrom");

            JSONObject reqParam = new JSONObject();
            reqParam.put("appid", getValStringRequired("authAppId"));
            JSONObject responseJSON = xxPayWxComponentService.reqWXByMch(getValLongRequired("mchId"), authFrom,
                    XxPayWxComponentService.API_COMPONENT_OPEN_CREATE, reqParam);
            return XxPayResponse.buildSuccess(responseJSON.getString("open_appid"));

        } catch (WxErrorException e) {
            logger.error("调起微信接口失败！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (Exception e) {
            logger.error("系统异常！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 绑定到开放平台  ***/
    @RequestMapping("/wx/bind2OpenApp")
    public XxPayResponse bind2OpenApp() {

        try {

            Byte authFrom = getValByteRequired("authFrom");

            JSONObject reqParam = new JSONObject();
            reqParam.put("appid", getValStringRequired("authAppId"));
            reqParam.put("open_appid", getValStringRequired("openAppId"));
            xxPayWxComponentService.reqWXByMch(getValLongRequired("mchId"), authFrom,
                    XxPayWxComponentService.API_COMPONENT_OPEN_BIND, reqParam);
            return XxPayResponse.buildSuccess();

        } catch (WxErrorException e) {
            logger.error("调起微信接口失败！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (Exception e) {
            logger.error("系统异常！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 解绑开放平台  ***/
    @RequestMapping("/wx/unbind")
    public XxPayResponse unbind() {

        try {

            Byte authFrom = getValByteRequired("authFrom");

            JSONObject reqParam = new JSONObject();
            reqParam.put("appid", getValStringRequired("authAppId"));
            reqParam.put("open_appid", getValStringRequired("openAppId"));
            xxPayWxComponentService.reqWXByMch(getValLongRequired("mchId"), authFrom,
                    XxPayWxComponentService.API_COMPONENT_OPEN_UNBIND, reqParam);
            return XxPayResponse.buildSuccess();

        } catch (WxErrorException e) {
            logger.error("调起微信接口失败！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (Exception e) {
            logger.error("系统异常！error: ", e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}