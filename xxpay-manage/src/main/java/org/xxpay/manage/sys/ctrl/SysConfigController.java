package org.xxpay.manage.sys.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysConfig;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import java.util.Map;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/config")
public class SysConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询结算配置信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        String type = getValStringRequired( "type");
        JSONObject obj = rpcCommonService.rpcSysConfigService.getSysConfigObj(type);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }

    /**
     * 修改配置信息
     * @return
     */
    @RequestMapping("/update")
    @MethodLog( remark = "修改配置信息" )
    public ResponseEntity<?> update() {

        JSONObject param = getReqParamJSON();
        String type = request.getParameter("type");
        if("sett".equals(type)) {
            // 将金额元转成分
            handleParamAmount( "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount", "feeLevel", "drawFeeLimit");
        }

        rpcCommonService.rpcSysConfigService.update(param);

        if("baiduOCR".equals(type)) {  //百度OCR, 需要清空accessToken
            stringRedisTemplate.delete(MchConstant.CACHEKEY_BAIDU_OCR_ACCESS_TOKEN);
        }
        if("baiduBCE".equals(type)) {  //百度BCE, 需要清空accessToken
            stringRedisTemplate.delete(MchConstant.CACHEKEY_UNIAPP_BAIDU_TOKEN);
            stringRedisTemplate.delete(MchConstant.CACHEKEY_FACEAPP_BAIDU_TOKEN);
        }

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查询短信通道配置信息
     * @return
     */
    @RequestMapping("/getSmsConfig")
    public ResponseEntity<?> getSmsConfig() {

        String code = getValStringRequired( "code");
        SysConfig sysConfig = rpcCommonService.rpcSysConfigService.selectByPrimaryKey(code);

        //value数据转JSONObject
        String smsConfigValue = sysConfig.getValue();
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(smsConfigValue)){
            jsonObject = JSON.parseObject(smsConfigValue);
        }

        //value数据里的templats转JSONArray
        String str = jsonObject.getString("templats");
        JSONArray smsConfig = new JSONArray();
        if (StringUtils.isNotBlank(str)){
            smsConfig = JSONArray.parseArray(str);
        }

        sysConfig.setAccessKeyId(jsonObject.getString("accessKeyId"));
        sysConfig.setAccessKeySecret(jsonObject.getString("accessKeySecret"));
        sysConfig.setSignName(jsonObject.getString("signName"));
        sysConfig.setPsVal("smsConfig",smsConfig);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysConfig));
    }

    /**
     * 修改短信通道配置信息
     * @return
     */
    @RequestMapping("/updateSmsConfig")
    @MethodLog( remark = "修改短信配置信息" )
    public ResponseEntity<?> updateSmsConfig() {

        String code = getValStringRequired( "code");
        String type = getValStringRequired( "type");
        String accessKeyId = getValStringRequired( "accessKeyId");
        String accessKeySecret = getValStringRequired( "accessKeySecret");
        String signName = getValStringRequired( "signName");
        String templats = getValStringRequired("templats");

        //templats转JSONArray
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isNotBlank(templats)){
            jsonArray = JSONArray.parseArray(templats);
        }

        //拼装JSONObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accessKeyId",accessKeyId);
        jsonObject.put("accessKeySecret",accessKeySecret);
        jsonObject.put("signName",signName);
        jsonObject.put("templats",jsonArray);

        String value = JSONObject.toJSONString(jsonObject);

        SysConfig sysConfig = new SysConfig();
        sysConfig.setCode(code);
        sysConfig.setValue(value);
        int result = rpcCommonService.rpcSysConfigService.updateSmsConfig(sysConfig);
        if (result == 0) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


    /** 系统设置 - 其他设置页面， 查询所有待修改的记录  **/
    @RequestMapping("/getSystemConfig")
    public ResponseEntity<?> getSystemConfig() {

        Map<String,String> result = rpcCommonService.rpcSysConfigService.selectByCodes(
                "agentMaxlevel", //代理商设置
                "uploadFileSaveType", "ossEndpoint", "ossBucketName", "ossAccessKeyId", "ossAccessKeySecret",  //上传设置
                "baiduOcrClientId", "baiduOcrClientSecret", "baiduOcrType",  //百度OCR
                "baiduBceAppId2Mch", "baiduBceAppKey2Mch", "baiduBceAppSecret2Mch",  //百度BCE
                "baiduBceAppId2Face", "baiduBceAppKey2Face", "baiduBceAppSecret2Face",  //百度BCE
                "uniPushAppId", "uniPushAppKey", "uniPushMasterSecret"  //uniPush消息推送
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

}