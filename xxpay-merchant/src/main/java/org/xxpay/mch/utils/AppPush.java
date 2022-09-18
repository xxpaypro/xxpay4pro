package org.xxpay.mch.utils;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppPush {

    private static final String url = "http://sdk.open.api.igexin.com/apiex.htm";


    /**
     *    1. 第一个参数为消息内容
     *    2. 第二个参数为设备号
     */
    public static String pushMessageToSingle(String content, String cId, String appId, String appKey, String masterSecret) {

        IGtPush push = new IGtPush(url, appKey, masterSecret);

        //获取透传消息模板
        TransmissionTemplate template = getTemplate(content, appId, appKey);

        // 定义"SingleMessage"类型消息对象，设置消息内容模板、是否支持离线发送、以及离线消息有效期(单位毫秒)
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 60 * 60 *24);

        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cId);

        //消息推送：单推

        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            return ret.getResponse().get("result").toString();
        } else {
            return null;
        }
    }

    //透传消息模板
    public static TransmissionTemplate getTemplate(String content, String appId, String appKey) {

        //1当content格式为  {"title": "xxx","content": "xxx","payload": "xxx"}  时，消息展示在手机通知栏，不会触发app语音阅读
        //2.当content格式为  {"title": "xxx","content": "xxx"}  时，触发app语音阅读，消息不会展示在手机通知栏
        JSONObject obj = new JSONObject();
        obj.put("title", "收款");
        obj.put("content", content);
        //obj.put("payload", content);

        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(obj.toJSONString());
        template.setTransmissionType(2);

        Notify notify = new Notify();
        notify.setTitle("收款");
        notify.setContent(obj.toJSONString());
        notify.setIntent("intent:#Intent;launchFlags=0x14000000;action=android.intent.action.oppopush;package=vip.xxpay.app;component=vip.xxpay.app/io.dcloud.PandoraEntry;S.UP-OL-SU=true;i.parm1=12;end");
        notify.setType(GtReq.NotifyInfo.Type._intent);
        template.set3rdNotifyInfo(notify);//设置第三方通知

        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
        payload.addCustomMsg("payload", "payload");

        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("收款"));

        //字典模式使用APNPayload.DictionaryAlertMsg
        //payload.setAlertMsg(getDictionaryAlertMsg(msg));

        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
        payload.setVoicePlayType(2);
        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
        payload.setVoicePlayMessage("定义内容");

        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String msg){
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(msg);
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle("Title");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }

}