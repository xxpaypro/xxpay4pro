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
@RequestMapping("/api/pay")
public class PaydemoController {
	
	@Value("${config.mchId}")
	private Long mchId;
	
	@Value("${config.mchKey}")
	private String mchKey;
	
	@Value("${config.payHost}")
	private String payHost;

	@Autowired
	private HttpServletRequest request;
	
	 /** 创建支付订单 */
    @RequestMapping(value = "/createOrder")
    public String createOrder() throws IOException {
    	
    	String amount = request.getParameter("amount");
    	String productId = request.getParameter("productId");
    	String mchOrderNo = request.getParameter("mchOrderNo");
    	String appId = request.getParameter("appId");
		String extra = request.getParameter("extra");  //扩展参数
		String channelUserId = request.getParameter("channelUserId");  //渠道用户ID
    	
    	if(StringUtils.isEmpty(amount) || StringUtils.isEmpty(productId) || StringUtils.isEmpty(mchOrderNo)){
    		return "参数丢失！";
    	}
    	
    	//金额转换为  分 为单位
    	String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("appId", appId);  // 当调用微信小程序支付时,该appId必传，为小程序的appId
    	params.put("productId", productId);  //支付产品ID
    	params.put("mchOrderNo", mchOrderNo);   //商户订单号
    	params.put("currency", "cny");   //币种
    	params.put("amount", amountParam);   //支付金额
    	params.put("clientIp", "210.73.10.148");   //客户端IP
    	params.put("device", "ios10.3.1");   //客户端设备
    	params.put("returnUrl", "http://localhost:8080/return_page.html");   //支付结果前端跳转URL
    	params.put("notifyUrl", "http://localhost:8080/api/pay/notify");   //支付结果后台回调URL
    	params.put("subject", "网络购物");  //商品主题
    	params.put("body", "网络购物");   //商品描述信息
    	params.put("param1", "");   //扩展参数1
    	params.put("param2", "");   //扩展参数2
		params.put("channelUserId", channelUserId);   //渠道用户ID,小程序支付时传openId
    	params.put("extra", extra);  //附加参数
    	
    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);
    	
    	return  HttpUtil.post(payHost + "/api/pay/create_order", genUrlParams(params));
    	
    }
    
	/** 查询订单 */
    @RequestMapping(value = "/queryOrder")
    public String queryOrder(HttpServletRequest request) throws IOException {
    	
    	String mchOrderNo = request.getParameter("mchOrderNo");
    	
    	if(StringUtils.isEmpty(mchOrderNo)){
    		return "参数丢失！";
    	}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("payOrderId", "");   //支付中心生成的订单号，与mchOrderNo二者传一即可
    	params.put("mchOrderNo", mchOrderNo);   //商户生成的订单号，与payOrderId二者传一即可
    	params.put("executeNotify", "false");   //是否执行回调
    	
    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);
    	
    	return  HttpUtil.post(payHost + "/api/pay/query_order", genUrlParams(params));
    }

	/** 关闭订单 */
	@RequestMapping(value = "/closeOrder")
	public String closeOrder(HttpServletRequest request) throws IOException {

		String mchOrderNo = request.getParameter("mchOrderNo");

		if(StringUtils.isEmpty(mchOrderNo)){
			return "参数丢失！";
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mchId", mchId + "");  //商户ID
		params.put("payOrderId", "");   //支付中心生成的订单号，与mchOrderNo二者传一即可
		params.put("mchOrderNo", mchOrderNo);   //商户生成的订单号，与payOrderId二者传一即可

		String sign = SignUtil.getSign(params, mchKey);  //签名
		params.put("sign", sign);

		return  HttpUtil.post(payHost + "/api/pay/close_order", genUrlParams(params));
	}
    
	/** 接收支付网关的异步通知 */
    @RequestMapping(value = "/notify")
    public String notify(HttpServletRequest request) throws IOException {
    	
    	if(StringUtils.isEmpty(request.getParameter("sign"))){   //sign参数 不存在
    		return "fail(sign not exists)";
    	}
    	
    	String resSign = request.getParameter("sign");  //接口返回sign参数值
    	
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	paramsMap.put("payOrderId", request.getParameter("payOrderId"));
    	paramsMap.put("mchId", request.getParameter("mchId"));
    	paramsMap.put("appId", request.getParameter("appId"));
    	paramsMap.put("productId", request.getParameter("productId"));
    	paramsMap.put("mchOrderNo", request.getParameter("mchOrderNo"));
    	paramsMap.put("amount", request.getParameter("amount"));
    	paramsMap.put("status", request.getParameter("status"));
    	paramsMap.put("channelOrderNo", request.getParameter("channelOrderNo"));
    	paramsMap.put("channelAttach", request.getParameter("channelAttach"));
    	paramsMap.put("param1", request.getParameter("param1"));
    	paramsMap.put("param2", request.getParameter("param2"));
    	paramsMap.put("paySuccTime", request.getParameter("paySuccTime"));
    	paramsMap.put("backType", request.getParameter("backType"));
    	paramsMap.put("income", request.getParameter("income"));
    	
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
