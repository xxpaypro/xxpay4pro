package org.xxpay.pay.ctrl.payment;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.IPUtility;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.PayDigestUtil;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @Description: 处理银行数据中心通知
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class BdcNotifyController {

    private final MyLog _log = MyLog.getLog(BdcNotifyController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final String privateKey = "2m9qSngcpainjoGphRqyo7ROcXwi53b6utECWl4wpSUfqEBhK";    // 银行数据中心私钥
    private static final List<String> whiteIpList = new LinkedList<>();                              // 白名单IP
    static {
        whiteIpList.add("127.0.0.1");
        whiteIpList.add("39.106.48.216");
    }

    /**
     * 接收支付中心通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/bdc/notify.htm")
    @ResponseBody
    public String notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        _log.info("====== 开始处理银行数据中心通知 ======");
        String logP = "[处理数据中心通知]";
        // 判断是否在IP白名单中
        String clientIp = IPUtility.getClientIp(request);
        _log.info("{}请求IP:{}", logP, clientIp);
        if(!whiteIpList.contains(clientIp)) {
            _log.warn("{}不在IP白名单", logP);
            return "不在IP白名单";
        }

        // 验证请求参数
        Map<String, Object> paramMap = request2payResponseMap(request, new String[]{
                "loginName", "tradeNo", "tradeDate", "inAmount", "dfName", "dfAccount", "content", "sign"
        });
        _log.info("{}请求参数,paramMap={}", logP, paramMap);
        String errMsg = verifyPayResponse(paramMap);
        if (!"success".equals(errMsg)) {
            _log.warn(errMsg);
            return errMsg;
        }

        // 判断业务是否处理过
        String tradeNo = (String) paramMap.get("tradeNo");
        AccountHistory mchAccountHistory = rpcCommonService.rpcAccountHistoryService.findByOrderId(MchConstant.INFO_TYPE_MCH, tradeNo);
        if(mchAccountHistory != null) {
            _log.warn("{}trandeNo={}该笔业务已经处理过了", logP, tradeNo);
            return "流水号:["+tradeNo+"]在系统中已存在，不允许重复充值!";
        }
        // 1. 通过备注查找商户
        MchInfo mchInfo = null;
        Long rechargeMchId = null;
        String content = (String) paramMap.get("content");
        if(StringUtils.isNotBlank(content)) {
            _log.info("{}trandeNo={}开始通过备注查找商户", logP, tradeNo);
//            mchInfo = rpcCommonService.rpcMchInfoService.findByTag(content);
            _log.info("{}trandeNo={}查找商户信息,mchInfo={}", logP, tradeNo, mchInfo);
            if(mchInfo != null) rechargeMchId = mchInfo.getMchId();
        }
        // 2. 通过账号查找商户
        if(rechargeMchId == null) {
            String dfName = (String) paramMap.get("dfName");
            String dfAccount = (String) paramMap.get("dfAccount");
            _log.info("{}trandeNo={}开始通过账号查找商户.dfName={},dfAccount={}", logP, tradeNo, dfName, dfAccount);
            
            SettBankAccount mchBankAccount = 
            		rpcCommonService.rpcSettBankAccountService.findByAccountNo(dfAccount, MchConstant.INFO_TYPE_MCH);
            
            _log.info("{}trandeNo={}通过账号查找商户.mchBankAccount={}", logP, tradeNo, mchBankAccount);
            if(mchBankAccount != null && mchBankAccount.getAccountName().equals(dfName)) {
                _log.info("{}trandeNo={}通过账号查找商户.与名字一致,能确认唯一商户", logP, tradeNo);
                rechargeMchId = mchBankAccount.getInfoId();
            }
        }
        _log.info("{}trandeNo={}查找商户完毕.rechargeMchId={}", logP, tradeNo, rechargeMchId);
        if(rechargeMchId == null) {
            _log.warn("{}trandeNo={}无法找到对应的商户", logP, tradeNo);
            return "流水号:["+tradeNo+"]无法找到对应的商户!";
        }

        if(mchInfo == null) {
            mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(rechargeMchId);
        }

        String resStr;
        try {
            // 为商户充值
            String inAmount = (String) paramMap.get("inAmount");
            Long inAmountL = Long.parseLong(AmountUtil.convertDollar2Cent(inAmount)); // 元转分,转long类型
            _log.info("{}trandeNo={}开始为商户充值.rechargeMchId={},inAmount={}元", logP, tradeNo, rechargeMchId, inAmount);

            // 调用充值
            Date currentDate = new Date();

            _log.info("{}trandeNo={}商户充值完毕.rechargeMchId={},inAmountL={}", logP, tradeNo, rechargeMchId, inAmountL);
            resStr = "success";
        }catch (Exception e) {
            resStr = "充值失败";
            _log.error("处理通知失败", e);
        }
        _log.info("{}tradeNo={}响应结果:{}", logP, tradeNo, resStr);
        _log.info("====== 银行数据中心通知处理完成 ======");
        return resStr;
    }

    public Map<String, Object> request2payResponseMap(HttpServletRequest request, String[] paramArray) {
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

    public String verifyPayResponse(Map<String, Object> map) {
        String tradeNo = (String) map.get("tradeNo");
        String inAmount = (String) map.get("inAmount");
        String dfName =  (String) map.get("dfName");
        String dfAccount =  (String) map.get("dfAccount");
        String sign = (String) map.get("sign");

        if (StringUtils.isEmpty(tradeNo)) {
            return "参数tradeNo为空";
        }
        if (StringUtils.isEmpty(inAmount)) {
            return "参数inAmount为空";
        }
        if (StringUtils.isEmpty(dfName)) {
            return "参数dfName为空";
        }
        if (StringUtils.isEmpty(dfAccount)) {
            return "参数dfAccount为空";
        }
        if (StringUtils.isEmpty(sign)) {
            return "参数sign为空";
        }

        // 验证签名
        if (!verifySign(map)) {
            return "验证签名不通过";
        }
        return "success";
    }

    public boolean verifySign(Map<String, Object> map) {
        String localSign = PayDigestUtil.getSign(map, this.privateKey, "sign");
        String sign = (String) map.get("sign");
        return localSign.equalsIgnoreCase(sign);
    }


}
