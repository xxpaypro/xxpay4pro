package org.xxpay.pay.channel.suixingpay.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.apache.log4j.lf5.viewer.LogFactor5Dialog;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.suixingpay.SuixingpayConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @Author terrfly
 * @Date 2020/8/25 16:35
 * @Description TODO
 **/
public class SuixingpayUtil {

    private static final MyLog _log = MyLog.getLog(SuixingpayUtil.class);


    /** 通用请求  **/
    public static JSONObject req(String urlSuffix, PayOrder payOrder, JSONObject mainParam, JSONObject reqData) throws Exception{

        String logPrefix = "[发起随行付请求]";

        String privateKey = mainParam.getString("privateKey"); //合作方私钥(替换成自己的)
        String sxfPublic = mainParam.getString("sxfPublic");  //随行付公钥

        ApiRequestBean<JSONObject> reqBean = new ApiRequestBean<>();

        reqBean.setOrgId(mainParam.getString("orgId")); //合作结构ID
        reqBean.setReqId(MySeq.getUUID()); //reqId, 每次请求的唯一值
        reqBean.setSignType("RSA"); //默认只支持 RSA
        reqBean.setTimestamp(DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS)); //yyyyMMddHHmmss 格式
        reqBean.setVersion("1.0");  //格式：1.0
        reqBean.setReqData(reqData); //业务参数

        String req = JSONObject.toJSONString(reqBean);

        //此处不要改变reqData里面值的顺序用LinkedHashMap
        HashMap reqMap = JSON.parseObject(req, LinkedHashMap.class, Feature.OrderedField);
        //组装加密串
        String signContent = RSASignature.getOrderContent(reqMap);

        //sign
        String sign = RSASignature.encryptBASE64(RSASignature.sign(signContent, privateKey));
        reqMap.put("sign", sign);

        _log.info("{}请求参数：{}" , logPrefix, reqMap);

        //发起HTTP请求
        String resultJsonStr = HttpUtils.connectPostUrl(SuixingpayConfig.REQ_URL + urlSuffix, JSON.toJSONString(reqMap));
        _log.info("{}返回信息：{}" , logPrefix, resultJsonStr);

        //不要对reqData排序 所以用LinkedHashMap
        HashMap<String, Object> result = JSON.parseObject(resultJsonStr, LinkedHashMap.class, Feature.OrderedField);

        if (!"0000".equals(result.get("code"))) {

            _log.info("{}异常！：resultJsonStr={}" , logPrefix, resultJsonStr);
            throw new Exception("接口响应异常");
        }

        //验签
        String signResult = result.get("sign").toString();
        result.remove("sign");  //去除sign参数

        //组装加密串
        if (! RSASignature.doCheck(RSASignature.getOrderContent(result), signResult, sxfPublic)) {

            _log.info("{}验签失败！：resultJsonStr={}" , logPrefix, resultJsonStr);
            throw new Exception("验签失败");
        }

        //以下为验签成功处理业务逻辑
        return new JSONObject(result).getJSONObject("respData");
    }

}
