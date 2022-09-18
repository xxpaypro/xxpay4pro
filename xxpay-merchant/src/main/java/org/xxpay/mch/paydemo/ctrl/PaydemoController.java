package org.xxpay.mch.paydemo.ctrl;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.util.HttpClient;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.mch.common.config.MainConfig;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/paydemo")
public class PaydemoController extends BaseController {

    private static final Logger _log = LoggerFactory.getLogger(PaydemoController.class);
    
    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private MainConfig mainConfig;
    
    /**
     * 通道测试- 创建支付订单
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
    @RequestMapping(value = "/create")
    @ResponseBody
    public ResponseEntity<?> create() {
        // type目前两种类型,recharge:api充值,cashier:收银台充值
        String type = request.getParameter("type");
        String productId = request.getParameter("productId");
        String amount = request.getParameter("amount");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
        String notifyUrl = request.getParameter("notifyUrl");
        String extra = request.getParameter("extra");
        String appId = request.getParameter("appId");
        
        BigDecimal amountB = new BigDecimal(amount);
        amountB = amountB.multiply(new BigDecimal("100")).setScale(0);
        
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(this.getUser().getBelongInfoId());
        
        JSONObject retObj = createPayOrder(type, mchInfo.getMchId(), mchInfo.getPrivateKey(), appId, productId, amountB.toString(), subject, body, notifyUrl, extra);
        if(retObj == null) {
            return ResponseEntity.ok(new BizResponse(10001, "创建订单失败,没有返回数据"));
        }
        if(PayEnum.OK.getCode().equals(retObj.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retObj, mchInfo.getPrivateKey(), "sign");
            String retSign = (String) retObj.get("sign");
            if(checkSign.equals(retSign)) {
                return ResponseEntity.ok(XxPayPageRes.buildSuccess(retObj));
            }else {
                return ResponseEntity.ok(new BizResponse(10001, "创建订单失败,验证支付中心返回签名失败"));
            }
        }
        return ResponseEntity.ok(new BizResponse(10001, "创建订单失败," + retObj.getString("retMsg")));
    }

    /**
     * 接收支付中心通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify.htm")
    @ResponseBody
    public String notify( HttpServletResponse response) throws Exception {
        _log.info("====== 开始处理支付中心通知 ======");
        Map<String,Object> paramMap = request2payResponseMap(request, new String[]{
                "payOrderId", "mchId", "appId", "productId", "mchOrderNo", "amount", "status",
                "channelOrderNo", "channelAttach", "param1", "income",
                "param2", "paySuccTime", "backType", "sign"
        });
        
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(Long.parseLong(paramMap.get("mchId").toString()));
        
        _log.info("支付中心通知请求参数,paramMap={}", paramMap);
        if (!verifyPayResponse(paramMap, mchInfo.getPrivateKey())) {
            String errorMessage = "verify request param failed.";
            _log.warn(errorMessage);
            return errorMessage;
        }
        String payOrderId = (String) paramMap.get("payOrderId");
        String mchOrderNo = (String) paramMap.get("mchOrderNo");
        String resStr;
        try {
            // 业务处理代码,根据订单号,得到业务系统交易数据.
            // 对交易数据进行处理,如修改状态,发货等操作
            resStr = "success";
        }catch (Exception e) {
            resStr = "fail";
            _log.error("处理通知失败", e);
        }
        _log.info("响应支付中心通知结果:{},payOrderId={},mchOrderNo={}", resStr, payOrderId, mchOrderNo);
        _log.info("====== 支付中心通知处理完成 ======");
        return resStr;
    }

    /**
     * 调用XxPay支付系统,创建支付订单
     * @param type
     * @param mchId
     * @param key
     * @param appId
     * @param productId
     * @param amount
     * @param subject
     * @param body
     * @param notifyUrl
     * @return
     */
    JSONObject createPayOrder(String type, Long mchId, String key, String appId,
    		String productId, String amount, String subject, String body, String notifyUrl, String extra) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);                               // 商户ID
        paramMap.put("appId", appId);                               // 应用ID
        paramMap.put("mchOrderNo", System.currentTimeMillis());     // 商户订单号
        paramMap.put("productId", productId);                       // 支付产品
        paramMap.put("amount", amount);                             // 支付金额,单位分
        paramMap.put("currency", "cny");                            // 币种, cny-人民币
        paramMap.put("clientIp", "211.94.116.218");                 // 用户地址,微信H5支付时要真实的
        paramMap.put("device", "WEB");                              // 设备
        paramMap.put("subject", subject);                           // 商品标题
        paramMap.put("body", body);                                 // 商品内容
        paramMap.put("notifyUrl", notifyUrl);                       // 回调URL
        paramMap.put("param1", "");                                 // 扩展参数1
        paramMap.put("param2", "");                                 // 扩展参数2
        paramMap.put("extra", extra);                               // 附加参数
        // 生成签名数据
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);                              // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("[xxpay] req:{}", reqData);
        String url = mainConfig.getPayUrl() + "/pay/create_order?";
        // 发起Http请求下单
        String result = call4Post(url + reqData);
        _log.info("[xxpay] res:{}", result);
        JSONObject retObj = JSON.parseObject(result);
        return retObj;
    }

    public Map<String, Object> request2payResponseMap( String[] paramArray) {
        Map<String, Object> responseMap = new HashMap<>();
        for (int i = 0;i < paramArray.length; i++) {
            String key = paramArray[i];
            String v = request.getParameter(key);
            if (v != null) {
                responseMap.put(key, v);
            }
        }
        return responseMap;
    }

    public boolean verifyPayResponse(Map<String,Object> map, String priKey) {
        String mchId = (String) map.get("mchId");
        String payOrderId = (String) map.get("payOrderId");
        String amount = (String) map.get("amount");
        String sign = (String) map.get("sign");

        if (StringUtils.isEmpty(mchId)) {
            _log.warn("Params error. mchId={}", mchId);
            return false;
        }
        if (StringUtils.isEmpty(payOrderId)) {
            _log.warn("Params error. payOrderId={}", payOrderId);
            return false;
        }
        if (StringUtils.isEmpty(amount) || !NumberUtils.isDigits(amount)) {
            _log.warn("Params error. amount={}", amount);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            _log.warn("Params error. sign={}", sign);
            return false;
        }

        // 验证签名
        if (!verifySign(map, priKey)) {
            _log.warn("verify params sign failed. payOrderId={}", payOrderId);
            return false;
        }

        // 此处需要写业务逻辑
        // 校验数据,订单是否一致,金额是否一致

        return true;
    }

    public boolean verifySign(Map<String, Object> map, String privateKey) {
        String localSign = PayDigestUtil.getSign(map, privateKey, "sign");
        String sign = (String) map.get("sign");
        return localSign.equalsIgnoreCase(sign);
    }

    /**
     * 发起HTTP/HTTPS请求(method=POST)
     * @param url
     * @return
     */
    public static String call4Post(String url) {
        try {
            URL url1 = new URL(url);
            if("https".equals(url1.getProtocol())) {
                return HttpClient.callHttpsPost(url);
            }else if("http".equals(url1.getProtocol())) {
                return HttpClient.callHttpPost(url);
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
