package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.pay.channel.PayNotifyInterface;
import org.xxpay.pay.util.SpringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 接收支付渠道后端通知
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-03-02
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Controller
public class NotifyPayController {

	private static final MyLog _log = MyLog.getLog(NotifyPayController.class);

	PayNotifyInterface payNotifyInterface;

	/**
	 * 支付渠道后台通知响应
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
     */
	@RequestMapping("/notify/{channel}/notify_res.htm")
	@ResponseBody
	public String payNotifyRes(HttpServletRequest request, @PathVariable("channel") String channel) throws ServletException, IOException {
		_log.info("====== 开始接收{}支付回调通知 ======", channel);
		try {
			payNotifyInterface = (PayNotifyInterface) SpringUtil.getBean(channel.toLowerCase() +  "PayNotifyService");
		}catch (BeansException e) {
			_log.error(e, "");
			return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付渠道类型[channel="+channel+"]实例化异常", null, null));
		}
		JSONObject retObj = payNotifyInterface.doNotify(request);
		String notifyRes = retObj.getString(PayConstant.RESPONSE_RESULT);
		_log.info("响应给{}:{}", channel, notifyRes);
		_log.info("====== 完成接收{}支付回调通知 ======", channel);
		return notifyRes;
	}

	/**
	 * 支付渠道前台通知跳转
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/notify/{channel}/return_res.htm")
	public String payReturnRes(HttpServletRequest request, HttpServletResponse response, ModelMap model, @PathVariable("channel") String channel) throws ServletException, IOException {

		_log.info("====== 开始接收{}支付前端同步跳转 ======", channel);
		try {
			payNotifyInterface = (PayNotifyInterface) SpringUtil.getBean(channel.toLowerCase() +  "PayNotifyService");
		}catch (BeansException e) {
			_log.error(e, "");
			return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付渠道类型[channel="+channel+"]实例化异常", null, null));
		}
		JSONObject retObj = payNotifyInterface.doReturn(request);
		String notifyRes = retObj.getString(PayConstant.RESPONSE_RESULT);
		String jumpUrl = retObj.getString(PayConstant.JUMP_URL);
		_log.info("响应给{}:{}", channel, notifyRes);
		_log.info("====== 完成接收{}支付前端同步跳转 ======", channel);

		if(StringUtils.isNotBlank(jumpUrl)) {
			response.sendRedirect(jumpUrl);
			return null;
		}

		return "notify/show";
	}

}
