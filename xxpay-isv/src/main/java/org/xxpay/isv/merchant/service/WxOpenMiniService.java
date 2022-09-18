package org.xxpay.isv.merchant.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.vo.WxAuthVO;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.core.entity.MchMiniVersion;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;

import java.io.File;
import java.util.Date;

/**
 * <pre>
 *     微信开放平台代小程序实现服务能力
 * </pre>
 *
 */
@Service
public class WxOpenMiniService {

    private static final MyLog logger = MyLog.getLog(WxOpenMiniService.class);

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    @Autowired
    private RpcCommonService rpc;


    /**
     * 快速创建小程序接口.
     */
    String FAST_REGISTER_WEAPP_URL = "https://api.weixin.qq.com/cgi-bin/component/fastregisterweapp?action=create";
    String FAST_REGISTER_WEAPP_SEARCH_URL = "https://api.weixin.qq.com/cgi-bin/component/fastregisterweapp?action=search";

    /**
     * 设置小程序服务器域名.
     *
     * <pre>
     *     授权给第三方的小程序，其服务器域名只可以为第三方的服务器，当小程序通过第三方发布代码上线后，小程序原先自己配置的服务器域名将被删除，
     *     只保留第三方平台的域名，所以第三方平台在代替小程序发布代码之前，需要调用接口为小程序添加第三方自身的域名。
     *     提示：需要先将域名登记到第三方平台的小程序服务器域名中，才可以调用接口进行配置
     * </pre>
     */
    String API_MODIFY_DOMAIN = "https://api.weixin.qq.com/wxa/modify_domain";

    /**
     * 设置小程序业务域名（仅供第三方代小程序调用）
     * <pre>
     *     授权给第三方的小程序，其业务域名只可以为第三方的服务器，当小程序通过第三方发布代码上线后，小程序原先自己配置的业务域名将被删除，
     *     只保留第三方平台的域名，所以第三方平台在代替小程序发布代码之前，需要调用接口为小程序添加业务域名。
     * 提示：
     * 1、需要先将域名登记到第三方平台的小程序业务域名中，才可以调用接口进行配置。
     * 2、为授权的小程序配置域名时支持配置子域名，例如第三方登记的业务域名如为qq.com，则可以直接将qq.com及其子域名（如xxx.qq.com）也配置到授权的小程序中。
     * </pre>
     */
    String API_SET_WEBVIEW_DOMAIN = "https://api.weixin.qq.com/wxa/setwebviewdomain";

    /**
     * 获取帐号基本信息
     * <pre>
     * GET请求
     * 注意：需要使用1.3环节获取到的新创建小程序appid及authorization_code换取authorizer_refresh_token进而得到authorizer_access_token。
     * </pre>
     */
    String API_GET_ACCOUNT_BASICINFO = "https://api.weixin.qq.com/cgi-bin/account/getaccountbasicinfo";

    /**
     * 绑定微信用户为小程序体验者
     */
    String API_BIND_TESTER = "https://api.weixin.qq.com/wxa/bind_tester";


    /**
     * 解除绑定微信用户为小程序体验者
     */
    String API_UNBIND_TESTER = "https://api.weixin.qq.com/wxa/unbind_tester";


    /**
     * 获取体验者列表
     */
    String API_GET_TESTERLIST = "https://api.weixin.qq.com/wxa/memberauth";

    /**
     * 以下接口基础信息设置
     * <p>
     *     https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=21517799059ZSEMr&token=6f965b5daf30a98a6bbd2a386faea5c934e929bf&lang=zh_CN
     * </p>
     */

    /**
     * 1. 设置小程序隐私设置（是否可被搜索）
     */
    String API_CHANGE_WXA_SEARCH_STATUS = "https://api.weixin.qq.com/wxa/changewxasearchstatus";

    /**
     * 2. 查询小程序当前隐私设置（是否可被搜索）
     */
    String API_GET_WXA_SEARCH_STATUS = "https://api.weixin.qq.com/wxa/getwxasearchstatus";

    /**
     * 3.1. 获取展示的公众号信息
     */
    String API_GET_SHOW_WXA_ITEM = "https://api.weixin.qq.com/wxa/getshowwxaitem";

    /**
     * 3.2 设置展示的公众号
     */
    String API_UPDATE_SHOW_WXA_ITEM = "https://api.weixin.qq.com/wxa/updateshowwxaitem";


    /**
     * 以下接口为三方平台代小程序实现的代码管理功能
     * <p>
     *     https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1489140610_Uavc4&token=fe774228c66725425675810097f9e48d0737a4bf&lang=zh_CN
     * </p>
     */

    /**
     * 1. 为授权的小程序帐号上传小程序代码
     */
    String API_CODE_COMMIT = "https://api.weixin.qq.com/wxa/commit";

    /**
     * 2. 获取体验小程序的体验二维码
     */
    String API_TEST_QRCODE = "https://api.weixin.qq.com/wxa/get_qrcode";

    /**
     * 3. 获取授权小程序帐号的可选类目
     */
    String API_GET_CATEGORY = "https://api.weixin.qq.com/wxa/get_category";

    /**
     * 4. 获取小程序的第三方提交代码的页面配置（仅供第三方开发者代小程序调用）
     */
    String API_GET_PAGE = "https://api.weixin.qq.com/wxa/get_page";

    /**
     * 5. 将第三方提交的代码包提交审核（仅供第三方开发者代小程序调用）
     */
    String API_SUBMIT_AUDIT = "https://api.weixin.qq.com/wxa/submit_audit";

    /**
     * 7. 查询某个指定版本的审核状态（仅供第三方代小程序调用）
     */
    String API_GET_AUDIT_STATUS = "https://api.weixin.qq.com/wxa/get_auditstatus";

    /**
     * 8. 查询最新一次提交的审核状态（仅供第三方代小程序调用）
     */
    String API_GET_LATEST_AUDIT_STATUS = "https://api.weixin.qq.com/wxa/get_latest_auditstatus";

    /**
     * 9. 发布已通过审核的小程序（仅供第三方代小程序调用）
     */
    String API_RELEASE = "https://api.weixin.qq.com/wxa/release";

    /**
     * 10. 修改小程序线上代码的可见状态（仅供第三方代小程序调用)
     */
    String API_CHANGE_VISITSTATUS = "https://api.weixin.qq.com/wxa/change_visitstatus";

    /**
     * 11.小程序版本回退（仅供第三方代小程序调用）
     */
    String API_REVERT_CODE_RELEASE = "https://api.weixin.qq.com/wxa/revertcoderelease";

    /**
     * 12.查询当前设置的最低基础库版本及各版本用户占比 （仅供第三方代小程序调用）
     */
    String API_GET_WEAPP_SUPPORT_VERSION = "https://api.weixin.qq.com/cgi-bin/wxopen/getweappsupportversion";

    /**
     * 13.设置最低基础库版本（仅供第三方代小程序调用）
     */
    String API_SET_WEAPP_SUPPORT_VERSION = "https://api.weixin.qq.com/cgi-bin/wxopen/setweappsupportversion";

    /**
     * 14.设置小程序“扫普通链接二维码打开小程序”能力
     *
     * https://mp.weixin.qq.com/debug/wxadoc/introduction/qrcode.html
     */
    /**
     * 14.1 增加或修改二维码规则
     */
    String API_QRCODE_JUMP_ADD = "https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpadd";

    /**
     * 14.2 获取已设置的二维码规则
     */
    String API_QRCODE_JUMP_GET = "https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpget";

    /**
     * 14.3 获取校验文件名称及内容
     */
    String API_QRCODE_JUMP_DOWNLOAD = "https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpdownload";

    /**
     * 14.4 删除已设置的二维码规则
     */
    String API_QRCODE_JUMP_DELETE = "https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpdelete";

    /**
     * 14.5 发布已设置的二维码规则
     */
    String API_QRCODE_JUMP_PUBLISH = "https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumppublish";

    /**
     * 15.小程序审核撤回
     * <p>
     * 单个帐号每天审核撤回次数最多不超过1次，一个月不超过10次。
     * </p>
     */
    String API_UNDO_CODE_AUDIT = "https://api.weixin.qq.com/wxa/undocodeaudit";

    /**
     * 16.1 小程序分阶段发布-分阶段发布接口
     */
    String API_GRAY_RELEASE = "https://api.weixin.qq.com/wxa/grayrelease";

    /**
     * 16.2 小程序分阶段发布-取消分阶段发布
     */
    String API_REVERT_GRAY_RELEASE = "https://api.weixin.qq.com/wxa/revertgrayrelease";

    /**
     * 16.3 小程序分阶段发布-查询当前分阶段发布详情
     */
    String API_GET_GRAY_RELEASE_PLAN = "https://api.weixin.qq.com/wxa/getgrayreleaseplan";


    /**
     * 查询服务商的当月提审限额和加急次数（Quota）
     */
    String API_QUERY_QUOTA = "https://api.weixin.qq.com/wxa/queryquota";

    /**
     * 加急审核申请
     */
    String API_SPEED_AUDIT = "https://api.weixin.qq.com/wxa/speedupaudit";

    /**
     * 快速注册小程序
     * 调微信接口：快速注册小程序
     * 本地：四字段插入商户第三方授权信息表，小程序状态为申请注册
     */
    public JSONObject fastRegisterWeapp(Long isvId, Long mchId, Byte authFrom, Byte authType, JSONObject reqParams) throws Exception {

        String componentAccessToken = xxPayWxComponentService.getIsvAccessToken(isvId).getAccessToken();
        String reqUrl = FAST_REGISTER_WEAPP_URL;
        if(StringUtils.isNotEmpty(componentAccessToken)){
            reqUrl = reqUrl + "&component_access_token=" + componentAccessToken;
        }

        //提交微信  注册小程序
        JSONObject respJSON = XxPayWxComponentService.reqWX(reqUrl, reqParams);

        //商户第三方授权信息表插入法人等数据
        if (respJSON.getInteger("errcode") == 0) {
            MchWxauthInfo mchWxauthInfo = new MchWxauthInfo();
            mchWxauthInfo.setIsvId(isvId);
            mchWxauthInfo.setMchId(mchId);
            mchWxauthInfo.setAuthStatus(MchConstant.MCH_WXAUTH_AUTHSTATUS_REGISTER);
            mchWxauthInfo.setAuthFrom(authFrom);
            mchWxauthInfo.setAuthType(authType);
            mchWxauthInfo.setLegalPersonaName(reqParams.getString("legal_persona_name"));
            mchWxauthInfo.setLegalPersonaWechat(reqParams.getString("legal_persona_wechat"));
            mchWxauthInfo.setBussinessCode(reqParams.getString("code"));
            mchWxauthInfo.setBussinessName(reqParams.getString("name"));
            boolean saveResult = rpc.rpcMchWxauthInfoService.save(mchWxauthInfo);
            if (!saveResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
        return respJSON;
    }

    /**
     * 查询小程序注册状态
     * 调微信接口：查询小程序注册状态
     */
    public JSONObject fastRegisterWeappSearch(Long isvId, Long mchId, Byte authFrom) throws Exception {

        MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
        if (mchWxauthInfo == null || mchWxauthInfo.getAuthStatus() != MchConstant.MCH_WXAUTH_AUTHSTATUS_REGISTER) {
            throw new ServiceException(RetEnum.RET_ISV_MINI_AUTHSTATUS_ERROR);
        }

        String componentAccessToken = xxPayWxComponentService.getIsvAccessToken(isvId).getAccessToken();
        String reqUrl = FAST_REGISTER_WEAPP_SEARCH_URL;
        if(StringUtils.isNotEmpty(componentAccessToken)){
            reqUrl = reqUrl + "&component_access_token=" + componentAccessToken;
        }

        JSONObject reqParams = new JSONObject();
        reqParams.put("name", mchWxauthInfo.getBussinessName());
        reqParams.put("legal_persona_wechat", mchWxauthInfo.getLegalPersonaWechat());
        reqParams.put("legal_persona_name", mchWxauthInfo.getLegalPersonaName());

        //提交微信  注册小程序
        JSONObject respJSON = XxPayWxComponentService.reqWX(reqUrl, reqParams);

        return respJSON;
    }

    /**
     * 上传代码
     * 调微信接口：设置服务器域名--上传代码
     * 本地：插入或更新小程序版本管理记录
     */
    public int miniCommit(Long isvId, Long mchId, Byte authFrom, JSONObject versionJSON) throws Exception {

        //数据库中获取该服务商的配置信息，获取配置的域名
        IsvWx3rdInfo isvWx3rdInfo = rpc.rpcIsvWx3rdInfoService.getById(isvId);

        //设置服务器域名
        if (StringUtils.isBlank(isvWx3rdInfo.getConfigMiniHost())) throw new ServiceException(RetEnum.RET_ISV_MINI_DOMAIN_NOT_EXISTS);
        String[] requestdomainList = isvWx3rdInfo.getConfigMiniHost().split(";");

        int code = this.modifyDomain(mchId, authFrom, "set", requestdomainList, requestdomainList, requestdomainList, requestdomainList);
        if (code != 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //小程序所需第三方配置参数
        JSONObject ext = new JSONObject();
        ext.put("mchId", mchId);

        JSONObject extJson = new JSONObject();
        MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
        extJson.put("extEnable", true);
        extJson.put("extAppid", mchWxauthInfo.getAuthAppId());
        extJson.put("ext", ext);

        JSONObject reqParam = new JSONObject();
        reqParam.put("template_id", versionJSON.getString("template_id"));
        reqParam.put("ext_json", extJson.toJSONString());
        reqParam.put("user_version", versionJSON.getString("user_version"));
        reqParam.put("user_desc", versionJSON.getString("user_desc"));
        logger.info("上传代码 请求参数：{}", reqParam);

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_CODE_COMMIT, reqParam);
        if (response.getInteger("errcode") != 0) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

        //数据库无当前小程序开发版本记录，新增；否则  更新
        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_COMMIT);
        if (mchMiniVersion == null){
            //上传代码后，添加小程序版本管理记录，版本为：开发版本
            MchMiniVersion newRecord = new MchMiniVersion();
            newRecord.setMchId(mchId);
            newRecord.setIsvId(isvId);
            newRecord.setAuthFrom(authFrom);
            newRecord.setAuthAppId(mchWxauthInfo.getAuthAppId());
            newRecord.setMiniVersion(versionJSON.getString("user_version"));//版本号
            newRecord.setMiniDesc(versionJSON.getString("user_desc"));//版本描述
            newRecord.setDevelop(versionJSON.getString("developer"));//开发者
            newRecord.setCommitTime(new Date());//代码提交时间
            newRecord.setVersionStatus(MchConstant.MCH_MINI_VERSION_COMMIT);//开发版本，待提交审核
            boolean saveResult = rpc.rpcMchMiniVersionService.save(newRecord);
            if (!saveResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }else {
            MchMiniVersion updateRecord = new MchMiniVersion();
            updateRecord.setId(mchMiniVersion.getId());
            updateRecord.setMiniVersion(versionJSON.getString("user_version"));//版本号
            updateRecord.setMiniDesc(versionJSON.getString("user_desc"));//版本描述
            updateRecord.setDevelop(versionJSON.getString("developer"));//开发者
            updateRecord.setCommitTime(new Date());//代码提交时间
            boolean updateResult = rpc.rpcMchMiniVersionService.updateById(updateRecord);
            if (!updateResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return 0;
    }

    /**
     * 获得小程序的域名配置信息
     * @return
     */
    public int getDomain(Long mchId, Byte authFrom) throws Exception {
        return modifyDomain(mchId, authFrom,"get", null, null, null, null);
    }

    /**
     * 修改服务器域名
     * @param action    delete删除, set覆盖, get获取
     * @return
     */
    public int modifyDomain(Long mchId, Byte authFrom, String action, String[] requestdomainList, String[] wsrequestdomainList, String[] uploaddomainList, String[] downloaddomainList) throws Exception {

        JSONObject params = new JSONObject();
        params.put("action", action);
        if (!"get".equals(action)) {
            params.put("requestdomain", requestdomainList);
            params.put("wsrequestdomain", wsrequestdomainList);
            params.put("uploaddomain", uploaddomainList);
            params.put("downloaddomain", downloaddomainList);
        }

        logger.info("设置服务器域名 请求参数：{}", params);
        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_MODIFY_DOMAIN, params);
        return response.getInteger("errcode");
    }

    /**
     * 提交审核
     * @return
     * @throws Exception
     */
    public JSONObject submitAudit(Long mchId, Byte authFrom, String type, JSONObject reqParam) throws Exception {

        //查询审核版本
        MchMiniVersion auditRecord = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_AUDIT);
        //存在审核中的版本，则不可提交
        if ((auditRecord != null || auditRecord.getAuditStatus() == null) && auditRecord.getAuditStatus() == MchConstant.MCH_MINI_AUDIT_STATUS_ING) {
            throw new ServiceException(RetEnum.RET_ISV_MINI_AUDITSTATUS_IS_ING);
        }

        //提交微信审核
        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_SUBMIT_AUDIT, reqParam);

        //更新小程序版本
        if (response.getInteger("errcode") == 0){
            String auditid = response.getString("auditid");

            //查询开发版本
            MchMiniVersion commitRecord;

            if ("audit".equals(type)){//提交审核，修改开发版本为审核版本

                //如果存在"非审核中"的审核版本，需先删除
                if (auditRecord != null){
                    boolean removeResult = rpc.rpcMchMiniVersionService.removeById(auditRecord.getId());
                    if (!removeResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                commitRecord = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_COMMIT);

                MchMiniVersion updateRecord = new MchMiniVersion();
                updateRecord.setId(commitRecord.getId());
                updateRecord.setAuditId(auditid);//审核编号
                updateRecord.setAuditTime(new Date());//审核提交时间
                updateRecord.setVersionStatus(MchConstant.MCH_MINI_VERSION_AUDIT);//设置为审核版本
                updateRecord.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_ING);//审核中
                boolean updateResult = rpc.rpcMchMiniVersionService.updateById(updateRecord);
                if(!updateResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

            }else {//重新提交审核，更新当前审核版本状态

                MchMiniVersion updateRecord = new MchMiniVersion();
                updateRecord.setId(auditRecord.getId());
                updateRecord.setAuditId(auditid);//审核编号
                updateRecord.setAuditTime(new Date());//审核提交时间
                updateRecord.setVersionStatus(MchConstant.MCH_MINI_VERSION_AUDIT);//设置为审核版本
                updateRecord.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_ING);//审核中
                boolean updateResult = rpc.rpcMchMiniVersionService.updateById(updateRecord);
                if(!updateResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }
        }

        return response;
    }

    /**
     * 上传素材
     * @return
     * @throws Exception
     */
    public JSONObject uploadMedia(Long mchId, Byte authFrom, String savePath) throws Exception {

        WxAuthVO wxAuthVO = xxPayWxComponentService.getMchAccessToken(mchId, authFrom);
        String accessToken = wxAuthVO.getAccessToken();

        File file = new File(savePath);

        // 组装post请求
        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/uploadmedia?access_token="+accessToken);

        FileBody fileBody = new FileBody(file);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("media", fileBody)
                .build();
        httpPost.setEntity(reqEntity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        String body = EntityUtils.toString(response.getEntity());
        logger.info("上传图片：" + body);

        return (JSONObject) JSON.parse(body);
    }

    /**
     * 获取最新一次提交的审核状态
     * @return
     * @throws Exception
     */
    public JSONObject getLatestAuditstatus(Long mchId, Byte authFrom) throws Exception {

        //获取状态
        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, API_GET_LATEST_AUDIT_STATUS, null);

        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_AUDIT);

        //更新
        if (response.getInteger("errcode") == 0){
            MchMiniVersion updateRecord = new MchMiniVersion();
            updateRecord.setId(mchMiniVersion.getId());

            Byte auditStatus = response.getByte("status");//0-成功  1-拒绝  2-审核中  3-已撤回
            updateRecord.setAuditStatus(auditStatus);

            if (auditStatus == 1){//审核拒绝
                String reason = response.getString("reason");//拒绝原因
                String screenShot = response.getString("ScreenShot");//审核失败小程序截图
                updateRecord.setRefusedReason(reason);
                updateRecord.setRefusedScreenShot(screenShot);
            }
            boolean result = rpc.rpcMchMiniVersionService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return response;
    }

    /**
     * 撤回当前代码审核单
     * @return
     * @throws Exception
     */
    public JSONObject undocodeAudit(Long mchId, Byte authFrom) throws Exception {

        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_AUDIT);
        if (mchMiniVersion.getAuditStatus() != MchConstant.MCH_MINI_AUDIT_STATUS_ING){
            throw new ServiceException(RetEnum.RET_ISV_MINI_AUDITSTATUS_NOT_ING);
        }

        //撤回
        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, API_UNDO_CODE_AUDIT, null);

        //更新
        if (response.getInteger("errcode") == 0){
            MchMiniVersion updateRecord = new MchMiniVersion();
            updateRecord.setId(mchMiniVersion.getId());
            updateRecord.setAuditStatus(MchConstant.MCH_MINI_AUDIT_STATUS_CANCEL);

            boolean result = rpc.rpcMchMiniVersionService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return response;
    }

    /**
     * 发布审核通过的小程序
     * @return
     * @throws Exception
     */
    public JSONObject release(Long mchId, Byte authFrom) throws Exception {
        //验证是否为审核成功状态
        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_AUDIT);
        if (mchMiniVersion.getAuditStatus() != MchConstant.MCH_MINI_AUDIT_STATUS_SUCCESS){
            throw new ServiceException(RetEnum.RET_ISV_MINI_AUDITSTATUS_NOT_ING);
        }

        //发布
        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_RELEASE, new JSONObject());

        //删除上个已发布版本
        MchMiniVersion releaseVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_RELEASE);
        if (releaseVersion != null){
            boolean delResult = rpc.rpcMchMiniVersionService.removeById(releaseVersion.getId());
            if (!delResult) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //更新
        if (response.getInteger("errcode") == 0){
            MchMiniVersion updateRecord = new MchMiniVersion();
            updateRecord.setId(mchMiniVersion.getId());
            updateRecord.setVersionStatus(MchConstant.MCH_MINI_VERSION_RELEASE);//设置为线上版本
            updateRecord.setReleaseTime(new Date());//发布时间

            boolean result = rpc.rpcMchMiniVersionService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return response;
    }

    /**
     * 版本回退
     * @return
     * @throws Exception
     */
    public JSONObject revertCodeRelease(Long mchId, Byte authFrom) throws Exception {
        //版本回退
        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, API_RELEASE, new JSONObject());
        return response;
    }

    /**
     * 获取审核时可填写的类目信息
     * @return
     * @throws Exception
     */
    public JSONObject getCategory(Long mchId, Byte authFrom) throws Exception {

        //获取审核时可填写的类目信息
        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, API_GET_CATEGORY, null);
        return response;
    }

    /**
     *  查询服务商的当月提审限额（quota）和加急次数
     * @return
     */
    public JSONObject queryQuota(Long mchId, Byte authFrom) throws Exception {
        //查询
        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, API_QUERY_QUOTA, null);
        return response;
    }

    /**
     *  加急审核
     * @return
     */
    public JSONObject speedupAudit(Long mchId, Byte authFrom) throws Exception {
        //加急审核
        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_AUDIT);
        if (mchMiniVersion.getAuditStatus() != MchConstant.MCH_MINI_AUDIT_STATUS_ING){
            throw new ServiceException(RetEnum.RET_ISV_MINI_AUDITSTATUS_NOT_ING);
        }

        JSONObject reqParams = new JSONObject();
        reqParams.put("auditid", mchMiniVersion.getAuditId());

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_SPEED_AUDIT, reqParams);
        return response;
    }

    /**
     *  获取已设置的二维码规则
     * @return
     */
    public JSONObject qrcodeJumpGet(Long mchId, Byte authFrom) throws Exception {

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_QRCODE_JUMP_GET, new JSONObject());
        return response;
    }

    /**
     *  增加或修改二维码规则
     * @return
     */
    public JSONObject qrcodeJumpAdd(Long mchId, Byte authFrom, JSONObject reqParams) throws Exception {

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_QRCODE_JUMP_DOWNLOAD, new JSONObject());

        //获取到的文件名和文件内容更新到商户第三方授权信息表
        if (response.getInteger("errcode") == 0){
            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);

            MchWxauthInfo updateRecord = new MchWxauthInfo();
            updateRecord.setId(mchWxauthInfo.getId());
            updateRecord.setWxCheckFileName(response.getString("file_name"));
            updateRecord.setWxCheckFileValue(response.getString("file_content"));

            boolean result = rpc.rpcMchWxauthInfoService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);

            JSONObject response2 = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_QRCODE_JUMP_ADD, reqParams);
            return response2;
        }

        return response;
    }

    /**
     *  发布二维码规则
     * @return
     */
    public JSONObject qrcodeJumpPublish(Long mchId, Byte authFrom, JSONObject reqParams) throws Exception {

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_QRCODE_JUMP_PUBLISH, reqParams);

        //获取到的文件名和文件内容更新到商户第三方授权信息表
        if (response.getInteger("errcode") == 0){
            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);

            MchWxauthInfo updateRecord = new MchWxauthInfo();
            updateRecord.setId(mchWxauthInfo.getId());
            updateRecord.setPrefix(reqParams.getString("prefix"));

            boolean result = rpc.rpcMchWxauthInfoService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return response;
    }

    /**
     *  删除二维码规则
     * @return
     */
    public JSONObject qrcodeJumpDelete(Long mchId, Byte authFrom, JSONObject reqParams) throws Exception {

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, API_QRCODE_JUMP_DELETE, reqParams);

        //获取到的文件名和文件内容更新到商户第三方授权信息表
        if (response.getInteger("errcode") == 0){
            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);

            MchWxauthInfo updateRecord = new MchWxauthInfo();
            updateRecord.setId(mchWxauthInfo.getId());
            updateRecord.setPrefix(" ");

            boolean result = rpc.rpcMchWxauthInfoService.updateById(updateRecord);
            if (!result) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return response;
    }
}
