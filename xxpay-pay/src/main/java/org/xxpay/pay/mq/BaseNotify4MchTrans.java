package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchNotify;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.service.RpcCommonService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 商户转账通知处理基类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-11-01
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class BaseNotify4MchTrans {

	private static final MyLog _log = MyLog.getLog(BaseNotify4MchTrans.class);

	@Autowired
	private Mq4MchTransNotify mq4MchTransNotify;

	@Autowired
	private RpcCommonService rpcCommonService;

	/**
	 * 创建响应URL
	 * @param transOrder
	 * @return
	 */
	public String createNotifyUrl(TransOrder transOrder) {
		Long mchId = transOrder.getInfoId();
		MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
		String key = null;
		if(mchInfo != null) {
			key = mchInfo.getPrivateKey();
		}else if(mchId.toString().startsWith("1")) { // TODO 临时处理,判断为代理商
			AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(mchId);
		}
		if(key == null) {
			throw new RuntimeException("获取privateKey异常");
		}

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("transOrderId", ObjectUtils.defaultIfNull(transOrder.getTransOrderId(), ""));           	// 转账订单号
		paramMap.put("mchId", ObjectUtils.defaultIfNull(transOrder.getInfoId(), ""));                      	 	// 商户ID
		paramMap.put("appId", ObjectUtils.defaultIfNull(transOrder.getAppId(), ""));							// 应用ID
		paramMap.put("mchOrderNo", ObjectUtils.defaultIfNull(transOrder.getMchTransNo(), ""));       		 	// 商户订单号
		paramMap.put("amount", ObjectUtils.defaultIfNull(transOrder.getAmount(), ""));                      	// 支付金额
		paramMap.put("status", ObjectUtils.defaultIfNull(transOrder.getStatus(), ""));               			// 转账状态
		paramMap.put("result", ObjectUtils.defaultIfNull(transOrder.getResult(), ""));               			// 转账结果
		paramMap.put("channelOrderNo", ObjectUtils.defaultIfNull(transOrder.getChannelOrderNo(), "")); 			// 渠道订单号
		paramMap.put("channelErrMsg", ObjectUtils.defaultIfNull(transOrder.getChannelErrMsg(), ""));			// 渠道错误描述
		paramMap.put("param1", ObjectUtils.defaultIfNull(transOrder.getParam1(), ""));               		   	// 扩展参数1
		paramMap.put("param2", ObjectUtils.defaultIfNull(transOrder.getParam2(), ""));               		   	// 扩展参数2
		paramMap.put("transSuccTime", "".equals(ObjectUtils.defaultIfNull(transOrder.getTransSuccTime(), "")) ? "" : transOrder.getTransSuccTime().getTime()); // 转账成功时间
		// 先对原文签名
		String reqSign = PayDigestUtil.getSign(paramMap, key);
		paramMap.put("sign", reqSign);   // 签名
		// 签名后再对有中文参数编码
		try {
			paramMap.put("param1", URLEncoder.encode(ObjectUtils.defaultIfNull(transOrder.getParam1(), ""), PayConstant.RESP_UTF8));
			paramMap.put("param2", URLEncoder.encode(ObjectUtils.defaultIfNull(transOrder.getParam2(), ""), PayConstant.RESP_UTF8));
		}catch (UnsupportedEncodingException e) {
			_log.error("URL Encode exception.", e);
			return null;
		}
		String param = XXPayUtil.genUrlParams(paramMap);
		StringBuffer sb = new StringBuffer();
		sb.append(transOrder.getNotifyUrl()).append("?").append(param);
		return sb.toString();
	}

	/**
	 * 处理商户转账后台服务器通知
	 */
	public void doNotify(TransOrder transOrder, boolean isFirst) {
		_log.info(">>>>>> TRANS开始回调通知业务系统 <<<<<<");
		// 发起后台通知业务系统
		// 如果通知地址为空,则不发送
		if(StringUtils.isBlank(transOrder.getNotifyUrl())) {
			_log.info("transOrderId={},notfiy地址为空,不处理回调通知", transOrder.getTransOrderId());
			return;
		}
		JSONObject object = createNotifyInfo(transOrder, isFirst);
		try {
			mq4MchTransNotify.send(object.toJSONString());
		} catch (Exception e) {
			_log.error(e, "transOrderId=%s,sendMessage error.", ObjectUtils.defaultIfNull(transOrder.getTransOrderId(), ""));
		}
		_log.info(">>>>>> TRANS回调通知业务系统完成 <<<<<<");
	}

	public JSONObject createNotifyInfo(TransOrder transOrder, boolean isFirst) {
		String url = createNotifyUrl(transOrder);
		if(isFirst) {
			int result = rpcCommonService.rpcMchNotifyService.insertSelectiveOnDuplicateKeyUpdate(transOrder.getTransOrderId(), transOrder.getInfoId(), null, transOrder.getAppId(), transOrder.getMchTransNo(), PayConstant.MCH_NOTIFY_TYPE_TRANS, url);
			_log.info("增加商户通知记录,orderId={},result:{}", transOrder.getTransOrderId(), result);
		}
		int count = 0;
		if(!isFirst) {
			MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(transOrder.getTransOrderId());
			if(mchNotify != null) count = mchNotify.getNotifyCount();
		}
		JSONObject object = new JSONObject();
		object.put("method", "GET");
		object.put("url", url);
		object.put("orderId", transOrder.getTransOrderId());
		object.put("count", count);
		object.put("createTime", System.currentTimeMillis());
		return object;
	}

}
