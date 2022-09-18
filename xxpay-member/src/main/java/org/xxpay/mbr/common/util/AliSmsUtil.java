package org.xxpay.mbr.common.util;

import org.apache.commons.lang3.StringUtils;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author: dingzhiwei
 * @date: 18/3/19
 * @description: 阿里云短信工具类
 */
public class AliSmsUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    
    private static final MyLog _log = MyLog.getLog(AliSmsUtil.class);
    
    /**
     * <p><b>Description: </b>调用阿里大于短信服务
     * <p>2018年10月22日 下午6:15:35
     * @author terrfly
     * @param phoneNumer 手机号
     * @param code 验证码
     * @param bizType 业务类型
     * @param sysConfigStr 系统配置信息
     * @return 是否成功
     * @throws ClientException
     */
    public static boolean sendSms(Long phoneNumber, String code, Byte bizType, String sysConfigStr){

        if(StringUtils.isEmpty(sysConfigStr)){
        	 _log.error("调用阿里短信服务失败. 获取配置信息失败, phoneNumber={}, bizType={}",phoneNumber, bizType);
        	return false;
        }
        
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        
        //{accessKeyId:"", accessKeySecret : "", templats:[{'bizType':1, signName :"", templateCode: ""}]}
        JSONObject configJSON = (JSONObject)JSONObject.parse(sysConfigStr);
        JSONObject templatJSON = null;
        JSONArray tempArr = configJSON.getJSONArray("templats");
        
        String signName = configJSON.getString("signName");
        
        for(Object obj: tempArr){
        	JSONObject jsonObj = (JSONObject)obj;
        	if(jsonObj.getByte("bizType") == bizType){
        		templatJSON = jsonObj;
        		break;
        	}
        }
        if(templatJSON == null){
        	_log.error("调用阿里短信服务失败. 获取配置信息失败bizType={},configJSON={}", bizType, configJSON);
        	return false;
        }
        
        if(StringUtils.isNotEmpty(templatJSON.getString("signName"))){
        	signName = templatJSON.getString("signName"); //支持业务自定义签名
        }
        
        try {

	        //初始化acsClient,暂不支持region化
	        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", configJSON.getString("accessKeyId"),
	        		configJSON.getString("accessKeySecret"));
	        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);
	
	        //组装请求对象-具体描述见控制台-文档部分内容
	        SendSmsRequest request = new SendSmsRequest();
	        //必填:待发送手机号
	        request.setPhoneNumbers(phoneNumber + "");
	        //必填:短信签名-可在短信控制台中找到
	        request.setSignName(signName);
	        //必填:短信模板-可在短信控制台中找到
	        request.setTemplateCode(templatJSON.getString("templateCode"));
	        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
	        request.setTemplateParam( templatJSON.getString("templateParam").replace(MchConstant.SYSCONFIG_SMS_PLACEHOLDER, code));
	
	        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	        request.setOutId("yourOutId");
		
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			_log.info("调用阿里短信服务完成.mobile={},code={},message={},requestId={},bizId={}", phoneNumber,
	        		sendSmsResponse.getCode(), sendSmsResponse.getMessage(), sendSmsResponse.getRequestId(), sendSmsResponse.getBizId());
			return true;
		} catch (Exception e) {
			 _log.error("调用阿里短信服务失败., phoneNumber={}, bizType={}",phoneNumber, bizType, e);
		}
        
		return false;
    }

}
