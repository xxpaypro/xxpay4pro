package org.paydemo.ctrl;

import org.paydemo.utils.HttpUtil;
import org.paydemo.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/refund")
public class RefundController {
	
	@Value("${config.mchId}")
	private Long mchId;
	
	@Value("${config.mchKey}")
	private String mchKey;
	
	@Value("${config.payHost}")
	private String payHost;

	@Autowired
	private HttpServletRequest request;
	
	 /** 退款申请 */
    @RequestMapping(value = "/apply")
    public String apply() throws IOException {
    	
    	String amount = request.getParameter("amount");  //退款金额
    	String mchOrderNo = request.getParameter("mchOrderNo");  //原始 退款订单号
    	String mchRefundNo = request.getParameter("mchRefundNo");  //退款商户单号


    	if(StringUtils.isEmpty(amount) || StringUtils.isEmpty(mchRefundNo) || StringUtils.isEmpty(mchOrderNo)){
    		return "参数丢失！";
    	}

    	//金额转换为  分 为单位
    	String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("mchOrderNo", mchOrderNo);   //支付订单-商户订单号
		params.put("mchRefundNo", mchRefundNo);   //商户退款单号
    	params.put("amount", amountParam);   //退款金额
		params.put("currency", "cny");   //币种
		params.put("clientIp", "210.73.10.148");   //客户端IP
    	params.put("device", "ios10.3.1");   //客户端设备
		params.put("extra", "");  //附加参数
		params.put("param1", "");   //扩展参数1
		params.put("param2", "");   //扩展参数2

		//如果notifyUrl 不为空表示异步退款，具体退款结果以退款通知为准
		params.put("notifyUrl", "");   //同步处理
//		params.put("notifyUrl", "http://localhost:8080/api/refund/notify");   //异步处理

		params.put("channelUser", "");   //渠道用户标识,如微信openId,支付宝账号
		params.put("userName", "");   //用户姓名
		params.put("remarkInfo", "用户退款");   //备注

    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);
    	
    	return  HttpUtil.post(payHost + "/api/refund/create_order", genUrlParams(params));
    	
    }
    
	/** 查询订单 */
    @RequestMapping(value = "/queryOrder")
    public String queryOrder(HttpServletRequest request) throws IOException {
    	
    	String mchRefundOrder = request.getParameter("mchRefundOrder");
    	
    	if(StringUtils.isEmpty(mchRefundOrder)){
    		return "参数丢失！";
    	}

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("appId", "");  //应用ID
    	params.put("mchRefundNo", mchRefundOrder);   //商户退款单号
    	params.put("refundOrderId", "");   //支付中心退款订单号
    	params.put("executeNotify", "false");   //是否执行回调

    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);
    	
    	return  HttpUtil.post(payHost + "/api/refund/query_order", genUrlParams(params));
    }
    
	/** 接收支付网关的异步通知 */
    @RequestMapping(value = "/notify")
    public String notify(HttpServletRequest request) throws IOException {
    	
    	if(StringUtils.isEmpty(request.getParameter("sign"))){   //sign参数 不存在
    		return "fail(sign not exists)";
    	}
    	
    	String resSign = request.getParameter("sign");  //接口返回sign参数值
    	
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	paramsMap.put("refundOrderId", request.getParameter("refundOrderId"));
    	paramsMap.put("mchId", request.getParameter("mchId"));
    	paramsMap.put("appId", request.getParameter("appId"));
    	paramsMap.put("mchRefundNo", request.getParameter("mchRefundNo"));
    	paramsMap.put("refundAmount", request.getParameter("refundAmount"));
    	paramsMap.put("status", request.getParameter("status"));
    	paramsMap.put("channelOrderNo", request.getParameter("channelOrderNo"));
    	paramsMap.put("param1", request.getParameter("param1"));
    	paramsMap.put("param2", request.getParameter("param2"));
    	paramsMap.put("refundSuccTime", request.getParameter("refundSuccTime"));
    	paramsMap.put("backType", request.getParameter("backType"));

    	String sign = SignUtil.getSign(paramsMap, mchKey);   //根据返回数据 和商户key 生成sign
    	
    	//验签
    	if(!resSign.equals(sign)){
    		return "fail(verify fail)";
    	}
    	
    	//处理业务...
    	return "success";
    }
    
    /** map 转换为  url参数 **/
    private static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=").append(paraMap.get(key));
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

}
