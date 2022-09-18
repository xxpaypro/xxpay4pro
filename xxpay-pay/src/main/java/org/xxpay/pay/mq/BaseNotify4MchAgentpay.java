package org.xxpay.pay.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchNotify;
import org.xxpay.pay.service.RpcCommonService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 商户代付通知处理基类
 * @date 2018-10-02
 * @version V1.0
 */
@Component
public class BaseNotify4MchAgentpay {

	private static final MyLog _log = MyLog.getLog(BaseNotify4MchAgentpay.class);

	@Autowired
	private Mq4MchAgentpayNotify mq4MchAgentpayNotify;

	@Autowired
	private RpcCommonService rpcCommonService;

	/**
	 * 创建响应URL
	 * @param mchAgentpayRecord
	 * @return
	 */
	public String createNotifyUrl(AgentpayRecord mchAgentpayRecord) {
		Long mchId = mchAgentpayRecord.getInfoId();
		MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
		String key = mchInfo.getPrivateKey();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("agentpayOrderId", ObjectUtils.defaultIfNull(mchAgentpayRecord.getAgentpayOrderId(), ""));
		paramMap.put("status", ObjectUtils.defaultIfNull(mchAgentpayRecord.getStatus(), ""));
		paramMap.put("fee", ObjectUtils.defaultIfNull(mchAgentpayRecord.getFee(), ""));
		paramMap.put("transMsg", ObjectUtils.defaultIfNull(mchAgentpayRecord.getTransMsg(), ""));
		paramMap.put("extra", ObjectUtils.defaultIfNull(mchAgentpayRecord.getExtra(), ""));

		// 签名
		String reqSign = PayDigestUtil.getSign(paramMap, key);
		paramMap.put("sign", reqSign);   // 签名

		String param = XXPayUtil.genUrlParams(paramMap);
		StringBuffer sb = new StringBuffer();
		sb.append(mchAgentpayRecord.getNotifyUrl()).append("?").append(param);
		return sb.toString();
	}

	/**
	 * 处理商户代付通知
	 */
	public void doNotify(AgentpayRecord mchAgentpayRecord, boolean isFirst) {
		_log.info(">>>>>> AGENTPAY开始回调通知业务系统 <<<<<<");
		// 发起后台通知业务系统
		// 如果通知地址为空,则不发送
		if(StringUtils.isBlank(mchAgentpayRecord.getNotifyUrl())) {
			_log.info("agentpayOrderId={},notfiy地址为空,不处理回调通知", mchAgentpayRecord.getAgentpayOrderId());
			return;
		}
		JSONObject object = createNotifyInfo(mchAgentpayRecord, isFirst);
		try {
			mq4MchAgentpayNotify.send(object.toJSONString());
		} catch (Exception e) {
			_log.error(e, "agentpayOrderId=%s,sendMessage error.", ObjectUtils.defaultIfNull(mchAgentpayRecord.getAgentpayOrderId(), ""));
		}
		_log.info(">>>>>> AGENTPAY回调通知业务系统完成 <<<<<<");
	}

	public JSONObject createNotifyInfo(AgentpayRecord mchAgentpayRecord, boolean isFirst) {
		String url = createNotifyUrl(mchAgentpayRecord);
		if(isFirst) {
			int result = rpcCommonService.rpcMchNotifyService.insertSelectiveOnDuplicateKeyUpdate(mchAgentpayRecord.getAgentpayOrderId(),
					mchAgentpayRecord.getInfoId(), null, null, mchAgentpayRecord.getMchOrderNo(), PayConstant.MCH_NOTIFY_TYPE_AGENTPAY, url);
			_log.info("增加商户通知记录,orderId={},result:{}", mchAgentpayRecord.getAgentpayOrderId(), result);
		}
		int count = 0;
		if(!isFirst) {
			MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(mchAgentpayRecord.getAgentpayOrderId());
			if(mchNotify != null) count = mchNotify.getNotifyCount();
		}
		JSONObject object = new JSONObject();
		object.put("method", "GET");
		object.put("url", url);
		object.put("orderId", mchAgentpayRecord.getAgentpayOrderId());
		object.put("count", count);
		object.put("createTime", System.currentTimeMillis());
		return object;
	}

}
