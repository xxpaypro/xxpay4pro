package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.common.vo.WxAuthVO;
import org.xxpay.core.entity.MchMiniVersion;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.isv.common.service.XxPayWxComponentService;
import org.xxpay.isv.merchant.service.WxOpenMiniService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx_mini")
public class WxMiniController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxMiniController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    @Autowired
    private WxOpenMiniService wxOpenMiniService;

    /**
     * ?????????????????????
     * @return
     */
    @RequestMapping("/fast_register_weapp")
    public XxPayResponse fastRegisterWeapp() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        Byte authType = getValByteRequired("authType");
        String name = getValStringRequired("name");
        String code = getValStringRequired("code");
        String codeType = getValStringRequired("code_type");
        String legalPersonaWechat = getValStringRequired("legal_persona_wechat");
        String legalPersonaName = getValStringRequired("legal_persona_name");
        String componentPhone = getValString("component_phone");

        MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
        if (mchWxauthInfo != null && mchWxauthInfo.getAuthStatus() == MchConstant.MCH_WXAUTH_AUTHSTATUS_FAIL){
            boolean delResult = rpc.rpcMchWxauthInfoService.removeById(mchWxauthInfo.getId());
            if (!delResult) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        MchWxauthInfo mchWxauthInfo2 = rpc.rpcMchWxauthInfoService.getOne(new QueryWrapper<MchWxauthInfo>().lambda()
            .eq(MchWxauthInfo::getLegalPersonaName, legalPersonaName)
            .eq(MchWxauthInfo::getLegalPersonaWechat, legalPersonaWechat)
            .eq(MchWxauthInfo::getBussinessCode, code)
            .eq(MchWxauthInfo::getBussinessName, name)
            .eq(MchWxauthInfo::getAuthStatus, MchConstant.MCH_WXAUTH_AUTHSTATUS_REGISTER)
            .ge(MchWxauthInfo::getCreateTime, DateUtil.addDay(new Date(), -1))
        );
        if (mchWxauthInfo2 != null){
            return XxPayResponse.build(RetEnum.RET_ISV_MINI_IS_APPLET_ING);
        }

        JSONObject reqParams = new JSONObject();
        reqParams.put("name", name);
        reqParams.put("code", code);
        reqParams.put("code_type", codeType);
        reqParams.put("legal_persona_wechat", legalPersonaWechat);
        reqParams.put("legal_persona_name", legalPersonaName);
        reqParams.put("component_phone", componentPhone);

        try {
            JSONObject respJSON = wxOpenMiniService.fastRegisterWeapp(getUser().getBelongInfoId(), mchId, authFrom, authType, reqParams);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

    }

    /**
     * ???????????????????????????
     * @return
     */
    @RequestMapping("/fast_register_weapp_search")
    public XxPayResponse fastRegisterWeappSearch() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.fastRegisterWeappSearch(getUser().getBelongInfoId(), mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            if (wxError.getError().getErrorCode() == 89250 || wxError.getError().getErrorCode() == 89247 || wxError.getError().getErrorCode() == 89253){
                MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
                mchWxauthInfo.setAuthStatus(MchConstant.MCH_WXAUTH_AUTHSTATUS_FAIL);
                rpc.rpcMchWxauthInfoService.updateById(mchWxauthInfo);
            }

            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (ServiceException se) {
            logger.error("???????????????{}", se);
            return XxPayResponse.buildErr(se.getErrMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ?????????
     * @return
     */
    @RequestMapping("/miniTemplateDraftList")
    public XxPayResponse miniTemplateDraftList() {

        try {
            Long currentIsvId = getUser().getBelongInfoId();

            JSONObject respJSON = xxPayWxComponentService.reqWXByIsv(currentIsvId, XxPayWxComponentService.GET_TEMPLATE_DRAFT_LIST_URL, new JSONObject());
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);

            JSONArray jsonArray = JSONArray.parseArray(respJSON.getString("draft_list"));

            return XxPayPageRes.buildSuccess(jsonArray, jsonArray.size());

        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ???????????????????????????
     * @return
     */
    @RequestMapping("/add2Template")
    public XxPayResponse add2Template() {

        Long currentIsvId = getUser().getBelongInfoId();
        String draftId = getValStringRequired("draftId");

        JSONObject reqParam = new JSONObject();
        reqParam.put("draft_id", draftId);

        try {
            JSONObject respJSON = xxPayWxComponentService.reqWXByIsv(currentIsvId, XxPayWxComponentService.ADD_TO_TEMPLATE_URL, reqParam);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

    }

    /**
     * ?????????????????????
     * @return
     */
    @RequestMapping("/miniTemplateList")
    public XxPayResponse miniTemplateList() {
        try {
            Long currentIsvId = getUser().getBelongInfoId();

            JSONObject respJSON = xxPayWxComponentService.reqWXByIsv(currentIsvId, XxPayWxComponentService.GET_TEMPLATE_LIST_URL, new JSONObject());
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);

            JSONArray jsonArray = JSONArray.parseArray(respJSON.getString("template_list"));

            return XxPayPageRes.buildSuccess(jsonArray, jsonArray.size());

        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????
     * @return
     */
    @RequestMapping("/delTemplate")
    public XxPayResponse delTemplate() {

        Long currentIsvId = getUser().getBelongInfoId();
        String templateId = getValStringRequired("templateId");

        JSONObject reqParam = new JSONObject();
        reqParam.put("template_id", templateId);

        try {
            JSONObject respJSON = xxPayWxComponentService.reqWXByIsv(currentIsvId, XxPayWxComponentService.DELETE_TEMPLATE_URL, reqParam);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

    }

    /**
     * ????????????
     * @return
     */
    @RequestMapping("/miniCommit")
    public XxPayResponse miniCommit() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        JSONObject versionJSON = (JSONObject) JSONObject.parse(getValStringRequired("versionData"));

        try {
            int code = wxOpenMiniService.miniCommit(getUser().getBelongInfoId(), mchId, authFrom, versionJSON);
            if (code != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (ServiceException se) {
            logger.error("???????????????{}", se);
            return XxPayResponse.buildErr(se.getErrMsg());
        }  catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

    }

    /**
     * ????????????????????????
     * @return
     */
    @GetMapping(value = "/miniGetQrcode", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] miniGetQrcode() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
        String appid = mchWxauthInfo.getAuthAppId();

        String name = mchId + "_" + appid + ".png";

        try {
            File file = new File(mainConfig.getMiniExperienceQrcodeDir() + name);

            logger.info("????????????:" + mainConfig.getMiniExperienceQrcodeDir() + name);

            //??????????????????????????????????????????
            File dir = file.getParentFile();
            if(!dir.exists()) dir.mkdirs();

            if (!file.exists()){
                WxAuthVO wxAuthVO = null;
                try {
                    wxAuthVO = xxPayWxComponentService.getMchAccessToken(mchId, authFrom);
                }catch (Exception e) {
                    logger.error(e, "");
                }

                String reqUrl = String.format("https://api.weixin.qq.com/wxa/get_qrcode?access_token=%s", wxAuthVO.getAccessToken());
                logger.info("?????????????????????????????????url???{}", reqUrl);

                HttpGet httpGet = new HttpGet(reqUrl);
                httpGet.addHeader("Content-Type", "application/json");
                httpGet.addHeader("Accept", "application/json");

                CloseableHttpClient httpClient = HttpClients.createDefault();

                CloseableHttpResponse response = httpClient.execute(httpGet);
                InputStream inputStream = response.getEntity().getContent();

                saveToImgByInputStream(inputStream, mainConfig.getMiniExperienceQrcodeDir(), name);  //????????????

                file = new File(mainConfig.getMiniExperienceQrcodeDir() + name);
            }

            FileInputStream stream = new FileInputStream(file);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes, 0, stream.available());
            return bytes;
        } catch (Exception e) {
            logger.error(e, "");
            return null;
        }
    }

    /**
     * ?????????????????????
     * @return
     */
    @RequestMapping("/modify_domain")
    public XxPayResponse modifyDomain() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        String domin = getValStringRequired("domin");

        String[] requestdomainList = domin.split(";");

        try {
            int code = wxOpenMiniService.modifyDomain(mchId, authFrom, "set", requestdomainList, requestdomainList, requestdomainList, requestdomainList);
            if (code != 0) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ?????????????????????????????????
     */
    @RequestMapping("/get_miniVersion")
    public XxPayResponse getMiniVersion() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        Byte versionStatus = getValByteRequired("versionStatus");

        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, versionStatus);

        return XxPayResponse.buildSuccess(mchMiniVersion);
    }

    /**
     * ?????????????????????????????????
     */
    @RequestMapping("/delCommitVersion")
    public XxPayResponse delCommitVersion() {

        Long id = getValLongRequired("id");
        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getById(id);
        if (mchMiniVersion == null || mchMiniVersion.getVersionStatus() != MchConstant.MCH_MINI_VERSION_COMMIT){
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        boolean result = rpc.rpcMchMiniVersionService.removeById(id);
        if (!result) return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);

        return XxPayResponse.buildSuccess();
    }

    /**
     * ????????????
     * @return
     */
    @RequestMapping("/submit_audit")
    public XxPayResponse submitAudit() {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        String type = getValStringRequired("type");
        String videoIdList = getValString("videoIdList");//????????????
        String picIdList = getValString("picIdList");//????????????
        String versionDesc = getValString("versionDesc");//????????????????????????????????????
        String feedbackInfo = getValString("feedbackInfo");
        String feedbackStuff = getValString("feedbackStuff");

        JSONObject reqParam = new JSONObject();

        //????????????
        JSONObject previewInfo = new JSONObject();
        if (StringUtils.isNotBlank(picIdList)){
            previewInfo.put("pic_id_list", picIdList.split(","));
        }
        if (StringUtils.isNotBlank(videoIdList)){
            previewInfo.put("video_id_list", videoIdList.split(","));
        }
        reqParam.put("preview_info", previewInfo);
        reqParam.put("version_desc", versionDesc);

        //????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(feedbackInfo)){
            reqParam.put("feedback_info", feedbackInfo);
        }
        if (StringUtils.isNotBlank(feedbackStuff)){
            reqParam.put("feedback_stuff", feedbackStuff.replace(",", "|"));
        }
        logger.info("?????????????????????{}", reqParam);

        try {
            JSONObject respJSON = wxOpenMiniService.submitAudit(mchId, authFrom, type, reqParam);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON.get("auditid"));
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????????????????
     * @return
     */
    @RequestMapping("/get_latest_auditstatus")
    public XxPayResponse getLatestAuditstatus() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.getLatestAuditstatus(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ????????????
     * @return
     */
    @RequestMapping("/undocode_audit")
    public XxPayResponse undocodeAudit() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.undocodeAudit(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (ServiceException se) {
            logger.error("???????????????{}", se);
            return XxPayResponse.buildErr(se.getErrMsg());
        }  catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ??????????????????????????????
     * @return
     */
    @RequestMapping("/release")
    public XxPayResponse release() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.release(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (ServiceException se) {
            logger.error("???????????????{}", se);
            return XxPayResponse.buildErr(se.getErrMsg());
        }  catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ?????????????????????
     * @return
     */
    @RequestMapping("/revert_code_release")
    public XxPayResponse revertCodeRelease() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.revertCodeRelease(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** ??????????????????????????? */
    @RequestMapping("/mini_pic")
    public XxPayResponse miniPic(@RequestParam("file") MultipartFile file) {

        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        if( file == null ) return XxPayResponse.build(RetEnum.RET_SERVICE_UPLOAD_FILE_NOT_EXISTS);

        try {
            //?????????????????????????????? & ?????????????????????
            String suffix = XXPayUtil.getImgSuffix(file.getOriginalFilename());
            String saveFileName = UUID.randomUUID() + "." + suffix;

            String savePath = mainConfig.getUploadMchStaticDir() + File.separator + MchConstant.MCH_IMG_SUB_DIR_WX_MINI_SCREENSHOT + File.separator + saveFileName;
            File saveFile = new File(savePath);

            //??????????????????????????????????????????
            File dir = saveFile.getParentFile();
            if(!dir.exists()) dir.mkdirs();
            file.transferTo(saveFile);

            JSONObject respJSON = wxOpenMiniService.uploadMedia(mchId, authFrom, savePath);

            saveFile.delete();

            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));
            return XxPayResponse.buildSuccess(respJSON.getString("mediaid"));

        } catch (Exception e) {
            logger.error("upload error, fileName = {}", file == null ? null :file.getOriginalFilename(), e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ???????????????????????????????????????quota??????????????????
     * @return
     */
    @RequestMapping("/query_quota")
    public XxPayResponse queryQuota() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.queryQuota(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ????????????
     * @return
     */
    @RequestMapping("/speedup_audit")
    public XxPayResponse speedupAudit() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.speedupAudit(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ?????????????????????????????????
     * @return
     */
    @RequestMapping("/qrcode_jump_get")
    public XxPayResponse qrcodeJumpGet() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.qrcodeJumpGet(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            JSONObject respJSON2 = new JSONObject();
            respJSON2.put("qrcodejump_pub_quota", respJSON.getString("qrcodejump_pub_quota"));

            return XxPayPageRes.buildSuccessExtData(respJSON.get("rule_list"), respJSON.getInteger("list_size"), respJSON2);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ??????????????????????????????
     * @return
     */
    @RequestMapping("/qrcode_jump_add")
    public XxPayResponse qrcodeJumpAdd() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        String path = getValStringRequired("path");
        Byte openVersion = getValByteRequired("openVersion");
        Integer isEdit = getValIntegerRequired("isEdit");
        String debugUrl = getValString("debugUrl");

        String prefix = null;
        if (authFrom == 1){
            prefix = String.format("%s%s/%s/%s/", mainConfig.getMbrUrl(), MchConstant.MBR_QRCODE_CHECKFILE_PATH_FOOD, mchId, authFrom);
        }

        //????????????
        JSONObject reqParams = new JSONObject();
        reqParams.put("prefix", prefix);
        reqParams.put("permit_sub_rule", MchConstant.PUB_YES);
        reqParams.put("path", path);
        reqParams.put("open_version", openVersion);
        reqParams.put("is_edit", isEdit);

        if (StringUtils.isNotBlank(debugUrl)){
            reqParams.put("debug_url", debugUrl.split(","));
        }

        try {
            JSONObject respJSON = wxOpenMiniService.qrcodeJumpAdd(mchId, authFrom, reqParams);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ?????????????????????
     * @return
     */
    @RequestMapping("/getRule")
    public XxPayResponse getRule() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        String prefix = null;
        if (authFrom == 1){
            prefix = String.format("%s%s/%s/%s/", mainConfig.getMbrUrl(), MchConstant.MBR_QRCODE_CHECKFILE_PATH_FOOD, mchId, authFrom);
        }

        return XxPayResponse.buildSuccess(prefix);
    }

    /**
     *  ?????????????????????
     * @return
     */
    @RequestMapping("/qrcode_jump_publish")
    public XxPayResponse qrcodeJumpPublish() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        String prefix = getValStringRequired("prefix");

        MchMiniVersion mchMiniVersion = rpc.rpcMchMiniVersionService.getByMchIdAndVersionstatus(mchId, authFrom, MchConstant.MCH_MINI_VERSION_RELEASE);
        if (mchMiniVersion == null) {
            return XxPayResponse.build(RetEnum.RET_ISV_MINI_NOT_RELEASE);
        }

        JSONObject reqParams = new JSONObject();
        reqParams.put("prefix", prefix);

        try {
            JSONObject respJSON = wxOpenMiniService.qrcodeJumpPublish(mchId, authFrom, reqParams);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ?????????????????????
     * @return
     */
    @RequestMapping("/qrcode_jump_delete")
    public XxPayResponse qrcodeJumpDelete() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");
        String prefix = getValStringRequired("prefix");

        JSONObject reqParams = new JSONObject();
        reqParams.put("prefix", prefix);

        try {
            JSONObject respJSON = wxOpenMiniService.qrcodeJumpDelete(mchId, authFrom, reqParams);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess(respJSON);
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     *  ???????????????????????????????????????
     * @return
     */
    @RequestMapping("/get_category")
    public XxPayResponse getCategory() {
        Long mchId = getValLongRequired("mchId");
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.getCategory(mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildErr(respJSON.getString("errmsg"));

            return XxPayResponse.buildSuccess();
        } catch (WxErrorException wxError) {
            logger.error("???????????????{}", wxError);
            return XxPayResponse.buildErr(wxError.getError().getErrorMsg());
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ?????????????????????????????????
     * @param instreams ????????????
     * @param imgPath ?????????????????????
     * @param imgName ???????????????
     * @return
     *      1???????????????
     *      0???????????????
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath,imgName);//???????????????????????????.jpg,.png???
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }



}