package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.pay.channel.TransNotifyInterface;
import org.xxpay.pay.util.SpringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description: 接收转账渠道(异步回调)
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-08-16
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Controller
public class NotifyTransController {

	private static final MyLog _log = MyLog.getLog(NotifyTransController.class);

	TransNotifyInterface transNotifyInterface;

	/**
	 * 转账渠道后台通知响应
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws IOException
     */
	@RequestMapping("/notify/{channel}/trans_notify.htm")
	@ResponseBody
	public String payNotifyRes(HttpServletRequest request, @PathVariable("channel") String channel) throws ServletException, IOException {
		_log.info("====== 开始接收{}转账回调通知 ======", channel);
		try {
			transNotifyInterface = (TransNotifyInterface) SpringUtil.getBean(channel.toLowerCase() +  "TransNotifyService");
		}catch (BeansException e) {
			_log.error(e, "");
			return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "转账渠道类型[channel="+channel+"]实例化异常", null, null));
		}
		JSONObject retObj = transNotifyInterface.doNotify(request);
		String notifyRes = retObj.getString(PayConstant.RESPONSE_RESULT);
		_log.info("响应给{}:{}", channel, notifyRes);
		_log.info("====== 完成接收{}转账回调通知 ======", channel);
		return notifyRes;
	}

}
