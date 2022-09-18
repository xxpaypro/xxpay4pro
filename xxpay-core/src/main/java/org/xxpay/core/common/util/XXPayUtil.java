package org.xxpay.core.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.vo.OrderCostFeeVO;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.core.entity.FeeScale;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description: 支付工具类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class XXPayUtil {

    private static final MyLog _log = MyLog.getLog(XXPayUtil.class);

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, String errCode, String errDesc) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(errCode != null) retMap.put(PayConstant.RESULT_PARAM_ERRCODE, errCode);
        if(errDesc != null) retMap.put(PayConstant.RESULT_PARAM_ERRDES, errDesc);
        return retMap;
    }

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, PayEnum payEnum) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(payEnum != null) {
            retMap.put(PayConstant.RESULT_PARAM_ERRCODE, payEnum.getCode());
            retMap.put(PayConstant.RESULT_PARAM_ERRDES, payEnum.getMessage());
        }
        return retMap;
    }

    public static String makeRetData(Map retMap, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retMap.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retMap, resKey);
            retMap.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }
        _log.info("生成响应数据:{}", retMap);
        return JSON.toJSONString(retMap);
    }

    public static String makeRetData(JSONObject retObj, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retObj.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retObj, resKey);
            retObj.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }

        _log.info("生成响应数据:{}", retObj);
        return JSON.toJSONString(retObj);
    }

    public static String makeRetFail(Map retMap) {
        _log.info("生成响应数据:{}", retMap);
        return JSON.toJSONString(retMap);
    }

    /**
     * 验证支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    /**
     * 验证VV平台支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key, String... noSigns) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        if(noSigns != null && noSigns.length > 0) {
            for (String noSign : noSigns) {
                params.remove(noSign);
            }
        }
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    public static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=");
            if(paraMap.get(key) instanceof String) {
                urlParam.append(URLEncoder.encode((String) paraMap.get(key)));
            }else {
                urlParam.append(paraMap.get(key));
            }
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

    public static String genUrlParams2(Map<String, String> paraMap) {
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

    /**
     * 发起HTTP/HTTPS请求(method=POST)
     * @param url
     * @param timeout 单位： 秒
     * @return
     */
    public static String call4Post(String url, int timeout) {
        try {
            URL url1 = new URL(url);
            if("https".equals(url1.getProtocol())) {

                HttpClient client = new HttpClient(url, "POST", timeout, "UTF-8");
                client.calls();
                return client.getResContent();

            }else if("http".equals(url1.getProtocol())) {

                return HttpClient.callHttpPost(url, timeout); // 默认超时时间60秒

            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断返回结果是否成功
     * @param retMap
     * @return
     */
    public static Boolean isSuccess(Map retMap) {
        if(retMap == null) return false;
        if(retMap.get("retCode") == null) return false;
        return "SUCCESS".equalsIgnoreCase(retMap.get("retCode").toString());
    }

    /**
     * 根据费率类型和金额,返回费用(type )
     * @param type      费用类型 1:费率,2:固定金额
     * @param feeRate   费率
     * @param feeEvery  每笔金额
     * @param amount    需要算的金额
     * @return
     */
    public static Long getFee(Byte type, BigDecimal feeRate, Long feeEvery, Long amount) {
        Long fee = 0l;
        if(type == 1) {
            fee = XXPayUtil.calOrderMultiplyRate(amount, feeRate);
        }else if(type == 2) {
            fee = feeEvery;
        }
        return fee;
    }

    /**
     * 判断IP是否允许
     * @param ip
     * @param whiteIps
     * @param blackIps
     * @return
     */
    public static Boolean ipAllow(String ip, String whiteIps, String blackIps) {
        if(StringUtils.isBlank(ip)) {
            return true;
        }
        String[] whiteIp_s = {};
        if(StringUtils.isNotBlank(whiteIps)) {
            whiteIp_s = whiteIps.split(",");
        }
        String[] blackIp_s = {};
        if(StringUtils.isNotBlank(blackIps)) {
            blackIp_s = blackIps.split(",");
        }
        // 白名单为空,黑名单为空
        if(whiteIp_s.length == 0 && blackIp_s.length == 0) {
            return true;
        }
        // 白名单为空,黑名单不为空
        if(whiteIp_s.length == 0 && blackIp_s.length > 0) {
            return !contain(blackIp_s, ip);
        }
        // 白名单不为空,黑名单为空
        if(whiteIp_s.length > 0 && blackIp_s.length == 0) {
            return contain(whiteIp_s, ip);
        }
        // 白名单不为空,黑名单不为空
        if(whiteIp_s.length > 0 && blackIp_s.length > 0) {
            if(contain(blackIp_s, ip)) {    // 如果在黑名单,则返回false
                return false;
            }
            return contain(whiteIp_s, ip);
        }
        return false;
    }

    /**
     * 判断IP是否允许(强校验)
     * 1. 必须在白名单中
     * 2. 如果在黑名单,则白名单中失效
     * @param ip
     * @param whiteIps
     * @param blackIps
     * @return
     */
    public static Boolean ipAllow4Strong(String ip, String whiteIps, String blackIps) {
        // 没有IP则返回false
        if(StringUtils.isBlank(ip)) {
            return false;
        }
        String[] whiteIp_s = {};
        if(StringUtils.isNotBlank(whiteIps)) {
            whiteIp_s = whiteIps.split(",");
        }
        String[] blackIp_s = {};
        if(StringUtils.isNotBlank(blackIps)) {
            blackIp_s = blackIps.split(",");
        }
        // 白名单为空,返回false
        if(whiteIp_s.length == 0) {
            return false;
        }
        // 如果不在白名单,返回false
        if(!contain(whiteIp_s, ip)) {
            return false;
        }
        // 如果黑名单不为空,则判断是否在黑名单中
        if(blackIp_s.length > 0) {
            return !contain(blackIp_s, ip);
        }
        return true;
    }

    /**
     * 判断是否包含IP
     * @param ips   ip数组
     * @param ip    ip地址
     * @return
     */
    public static boolean contain(String[] ips, String ip) {
        if(ips == null || ips.length == 0) return false;
        if(StringUtils.isBlank(ip)) return false;
        for(String p : ips) {
            if(p.equals(ip)) return true;
        }
        return false;
    }

    /**
     * 判断是否包含IP
     * @param ips   使用半角逗号分隔的ip
     * @param ip    ip地址
     * @return
     */
    public static boolean contain(String ips, String ip) {
        if(StringUtils.isBlank(ips)) return false;
        String[] ip_s = {};
        if(StringUtils.isNotBlank(ips)) {
            ip_s = ips.split(",");
        }
        return contain(ip_s, ip);
    }

    /**
     * @deprecated 20181012 新增多级代理商需求，该函数仅支持一级代理商
     * <p><b>Description: </b>计算订单的分润情况 和 各种费用
     * <p>2018年9月20日 下午4:13:47
     * @author matf
     * @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param channelRate 通道费率   （保持与数据库的格式一致 ，百分比之前的数字，如费率为0.55%，则传入 0.55）
     * @param agentRate 代理商设置费率，说明同上，  如果为null  说明商家没有代理商
     * @param mchRate 商家设置费率，说明同上
     * @return
     */
    public static OrderCostFeeVO calOrderCostFeeAndIncome(Long amount, BigDecimal channelRate, BigDecimal agentRate, BigDecimal mchRate){

        //通道手续费
        Long channelCostFee = calOrderMultiplyRate(amount, channelRate);

        //代理商成本费用   即  ：代理商需要支付给平台的费用
        Long agentCostFee = 0L; //当该商户不存在代理商时 代理商费用为0
        if(agentRate != null){
            agentCostFee = calOrderMultiplyRate(amount, agentRate);
        }

        //商家成本费用  即 ： 商家需要支付代理商或者平台的费用
        Long mchCostFee = calOrderMultiplyRate(amount, mchRate);

        //平台利润  : （代理商费用 - 通道费用）   或者  （商家费用 - 通道费用）
        Long platProfit = ( agentRate != null ? agentCostFee : mchCostFee  ) - channelCostFee;

        //代理商利润 ： (商家费用 - 代理商费用) 或者  0
        Long agentProfit = agentRate != null ? mchCostFee - agentCostFee  : 0L;

        //商户入账金额
        Long mchIncome = amount - mchCostFee;

        //计算结果不允许出现负值
        if(agentProfit < 0) {
            _log.warn("[代理商&商户]费率设置异常:agentProfit={}, amount={}, channelRate={}, agentRate={}, mchRate={}", agentProfit, amount, channelRate, agentRate, mchRate);
            agentProfit = 0L;
        }

        if(platProfit < 0) {
            _log.warn("[代理商&通道]费率设置异常:platProfit={}, amount={}, channelRate={}, agentRate={}, mchRate={}", platProfit, amount, channelRate, agentRate, mchRate);
            platProfit = 0L;
        }

        return new OrderCostFeeVO(channelCostFee, agentCostFee, mchCostFee, platProfit, agentProfit, mchIncome);

    }

    /**
     * <p><b>Description: </b>计算订单的各种费用  （订单金额 * 费率  结果四舍五入并保留0位小数 ）
     * 适用于计算
     * <p>2018年9月20日 下午2:16:34
     * @author matf
     * @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param rate 费率   （保持与数据库的格式一致 ，百分比之前的数字，如费率为0.55%，则传入 0.55）
     * @return
     */
    public static Long calOrderMultiplyRate(Long amount, BigDecimal rate){
        //费率还原 回真实数值即/100, 并乘以订单金额   结果四舍五入并保留0位小数
        return new BigDecimal(amount).multiply(rate).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP).longValue();

    }
    
    
    /**
     * <p><b>Description: </b> 计算订单的费用总和，累加 费率  和 费用
     * <p>2018年10月12日 下午10:15:33
     * @author terrfly
     * @param amount 订单发生额
     * @param rate 费用费率
     * @param fee 费用金额
     * @return
     */
    public static Long calOrderTotalFee(Long amount, BigDecimal rate, Long fee){
    	Long totalAmount = 0L;
    	if(rate != null){
    		totalAmount += calOrderMultiplyRate(amount, rate);
    	}if(fee != null){
    		totalAmount += fee;
    	}
    	return totalAmount ;
    }
    
    /**
     * 计算结算手续费
     * @param settAmount 结算金额
     * @param feeType    结算类型   1-百分比收费,2-固定收费
     * @param feeRate    结算费率
     * @param feeLevel   每笔手续费
     * @param drawFeeLimit  手续费上限
     * @return
     */
    public static long calculationFee(Long settAmount, Byte feeType, BigDecimal feeRate, String feeLevel, Long drawFeeLimit) {
        if(feeType == 1) {
            return NumberUtils.min(new BigDecimal(settAmount).multiply(feeRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).longValue(), drawFeeLimit);
        }else if(feeType == 2) {
            return NumberUtils.min(new BigDecimal(feeLevel).longValue(), drawFeeLimit);
        }
        throw new ServiceException(RetEnum.RET_COMM_UNKNOWN_ERROR);
    }


    /**
     * 生成账户数据保护 签名数据 （根据动态数组）
     * @param fields
     * @return
     */
    public static String genAccountSafeKey(Object ... fields){

        StringBuffer signStr = new StringBuffer();
        for(Object field : fields){
            signStr.append(field).append("_");
        }
        return MD5Util.string2MD5(signStr.append(MchConstant.ACCOUNT_SAFE_SIGN_PWD).toString());
    }

    /**
     * 生成账户数据保护 签名数据(根据数据库对象)
     * @param record
     * @return
     */
    public static String genAccountSafeKey(AccountBalance record){
        return genAccountSafeKey(
                record.getInfoType(), record.getInfoId(), record.getAccountType(),
                record.getAmount(), record.getUnAmount(), record.getFrozenAmount(),
                record.getSettAmount(), record.getTotalAddAmount(), record.getTotalSubAmount()
        );
    }

    /**
     * 验证数据库账户是否被篡改
     * @param record
     * @return 是否通过验证
     */
    public static boolean checkAccountSafe(AccountBalance record){
        if(record == null) return false;
        return genAccountSafeKey(record).equals(record.getSafeKey());
    }

    /**
     * 根据业务类型 获取账户变更类型
     * @param bizType
     * @param bizItem
     * @return
     */
    public static Byte getAccountTypeByBizType(Byte bizType, String bizItem){

        if(MchConstant.BIZ_TYPE_TRANSACT == bizType || MchConstant.BIZ_TYPE_REMIT == bizType ||
                MchConstant.BIZ_TYPE_PROFIT == bizType){  //所有支付、提现、分润 业务类型 = 账户余额
            return MchConstant.ACCOUNT_TYPE_BALANCE;
        }

        if(MchConstant.BIZ_TYPE_CHANGE_BALANCE == bizType){ //bizType = 调账
            //调账 & （余额、冻结金额） = 账户余额
            if(MchConstant.BIZ_ITEM_BALANCE.equals(bizItem) || MchConstant.BIZ_ITEM_FROZEN_MONEY.equals(bizItem)) {
                return MchConstant.ACCOUNT_TYPE_BALANCE;
            }

            if(MchConstant.BIZ_ITEM_AGENTPAY_BALANCE.equals(bizItem)) {  //调账&代付余额 = 代付余额
                return MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE;
            }

            if(MchConstant.BIZ_ITEM_SECURITY_MONEY.equals(bizItem)) {  //调账&保证金 = 保证金
                return MchConstant.ACCOUNT_TYPE_SECURITY_MONEY;
            }
        }

        if(MchConstant.BIZ_TYPE_RECHARGE == bizType ){ //充值业务  = 代付余额
            return MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE;
        }

        if( MchConstant.BIZ_TYPE_AGENTPAY == bizType ){  //代付业务  包含从收款账户出款 和 代付余额出款

            if(MchConstant.BIZ_ITEM_BALANCE.equals(bizItem)){
                return MchConstant.ACCOUNT_TYPE_BALANCE;
            }
            if(MchConstant.BIZ_ITEM_AGENTPAY_BALANCE.equals(bizItem)){
                return MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE;
            }

        }
        throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
    }


    /**
     * 1.将资金变动表根据业务类型进行填充
     * 2.返回需要更新的金额map
     * @param bizType
     * @param bizItem
     * @param changeAmount  变动金额（可为正数 和 负数）
     * @param balanceRecord 查询出的账户信息
     * @param fillHistory  需要填充的资金变动表对象
     * @return
     */
    public static Map<String, Long> fillHistoryAndReturnChangeAmount(byte bizType, String bizItem, Long changeAmount, AccountBalance balanceRecord, AccountHistory fillHistory) {

        Map<String, Long> result = new HashMap<>();  // 变更金额map  key = addAmount, addUnAmount, addFrozenAmount, addSettAmount

        switch (bizType){
            case MchConstant.BIZ_TYPE_TRANSACT : //支付

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY :
                        result.put("addAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            case MchConstant.BIZ_TYPE_REMIT :  //提现

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :
                        result.put("addAmount", changeAmount);    // 负数 ，将余额和不可用余额全部进行减的操作。
                        result.put("addUnAmount", changeAmount );
                        result.put("addSettAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;
                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            case MchConstant.BIZ_TYPE_CHANGE_BALANCE :  //调账

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :
                        result.put("addAmount", changeAmount);
                        result.put("addSettAmount", changeAmount);  //调整&余额 将更新可提现金额。
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;

                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE :
                        result.put("addAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;

                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY :
                        result.put("addFrozenAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getFrozenAmount());
                        fillHistory.setAfterBalance(balanceRecord.getFrozenAmount() + changeAmount);
                        break;
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY :
                        result.put("addAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            case MchConstant.BIZ_TYPE_RECHARGE :  //充值

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF :
                        result.put("addAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE :
                        result.put("addAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            case MchConstant.BIZ_TYPE_ERROR_HANKLE :  //差错处理

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }
                ///break;

            case MchConstant.BIZ_TYPE_AGENTPAY :  //代付

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :  //从账户余额付款
                        result.put("addAmount", changeAmount);
                        result.put("addSettAmount", changeAmount);
                        result.put("addUnAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;

                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE :  //从代付余额付款
                        result.put("addAmount", changeAmount);
                        result.put("addUnAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;

                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            case MchConstant.BIZ_TYPE_PROFIT : //分润

                switch (bizItem){

                    // 余额
                    case MchConstant.BIZ_ITEM_BALANCE :
                        result.put("addAmount", changeAmount);
                        result.put("addSettAmount", changeAmount);
                        fillHistory.setBalance(balanceRecord.getAmount());
                        fillHistory.setAfterBalance(balanceRecord.getAmount() + changeAmount);
                        break;

                    // 代付余额
                    case MchConstant.BIZ_ITEM_AGENTPAY_BALANCE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 冻结金额
                    case MchConstant.BIZ_ITEM_FROZEN_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 保证金
                    case MchConstant.BIZ_ITEM_SECURITY_MONEY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 支付
                    case MchConstant.BIZ_ITEM_PAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 代付
                    case MchConstant.BIZ_ITEM_AGENTPAY : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线下充值
                    case MchConstant.BIZ_ITEM_OFF : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 线上充值
                    case MchConstant.BIZ_ITEM_ONLINE : throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                    // 其他
                    default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                }

                break;

            default: throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        return result;
    }

    /**
     * 根据FeeScale信息， 计算单笔订单费用
     * @param feeScale 收费信息配置
     * @param orderAmount 订单金额
     * @return
     */
    public static Long calSingleOrderFee(FeeScale feeScale, Long orderAmount){

        //按包年，包月等统一付费方式，单笔不扣费。
        if(feeScale.getFeeScale() != MchConstant.FEE_SCALE_SINGLE) {
            return 0L;
        }

        //单笔收费模式
        if(feeScale.getSingleFeeType() == MchConstant.FEE_SCALE_SINGLE_FIX){ //单笔固定收费
            return feeScale.getFee().longValue();
        }else if(feeScale.getSingleFeeType() == MchConstant.FEE_SCALE_SINGLE_RATE){ //单笔费率收费
            return calOrderMultiplyRate(orderAmount, feeScale.getFee());
        }

        return null;
    }


    /**
     * 获取账户可用余额
     * @param balance
     * @param isSettAmount
     * @return
     */
    public static Long getAvailableAmount(AccountBalance balance, boolean isSettAmount) {

        if(balance == null) return null;

        Long minuendAmount = balance.getAmount();
        if(isSettAmount) {
            minuendAmount = balance.getSettAmount();
        }

        return minuendAmount - balance.getUnAmount() - balance.getFrozenAmount();
    }

    /** 计算下一次的分润时间  nowDate 精确到日即可 **/
    public static Date nextProfitTaskTime(Byte settDateType, Integer settSetDay, Date nowDate){

        if(MchConstant.ISV_SETT_DATE_TYPE_DAY == settDateType) {  //结算周期类型： 固定天数, 下次分润计划时间为 当前时间 + 天数的 03:00
            return DateUtil.str2date(DateUtil.date2Str(DateUtil.addDay(nowDate, settSetDay), "yyyy-MM-dd 03:00:00"));
        }else if(MchConstant.ISV_SETT_DATE_TYPE_MONTH == settDateType){ //结算周期类型： 指定月的如期， 应该为下一个月当前天数
            return DateUtil.str2date(DateUtil.date2Str(DateUtil.addMonth(nowDate, 1), "yyyy-MM-"+settSetDay+" 03:00:00"));
        }else{
            return null;
        }
    }


    /** 校验微信/支付宝二维码是否符合规范， 并根据支付类型返回对应的产品ID **/
    public static int getProductIdByBarCode(String barCode){

        if(StringUtils.isEmpty(barCode)) throw new ServiceException(RetEnum.RET_MCH_NOT_SUPPORT_BAR_CODE);

        //微信 ： 用户付款码条形码规则：18位纯数字，以10、11、12、13、14、15开头
        //文档： https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=5_1
        if(barCode.length() == 18 && Pattern.matches("^(10|11|12|13|14|15)(.*)", barCode)){
            return PayConstant.PAY_PRODUCT_WX_BAR;
        }
        //支付宝： 25~30开头的长度为16~24位的数字
        //文档： https://docs.open.alipay.com/api_1/alipay.trade.pay/
        else if(barCode.length() >= 16 && barCode.length() <= 24 && Pattern.matches("^(25|26|27|28|29|30)(.*)", barCode)){
            return PayConstant.PAY_PRODUCT_ALIPAY_BAR;
        }
        //云闪付： 二维码标准： 19位 + 62开头
        //文档：https://wenku.baidu.com/view/b2eddcd09a89680203d8ce2f0066f5335a8167fa.html
        else if(barCode.length() == 19 && Pattern.matches("^(62)(.*)", barCode)){
            return PayConstant.PAY_PRODUCT_UNIONPAY_BAR;
        }
        else{  //暂时不支持的条码类型
            throw new ServiceException(RetEnum.RET_MCH_NOT_SUPPORT_BAR_CODE);
        }
    }

    /** 根据productId 获取tradeOrder表的productType **/
    public static Byte getTradeProductTypeByProductId(Integer productId){

        if(productId == null) return null;
        if(productId == PayConstant.PAY_PRODUCT_WX_JSAPI) return MchConstant.MCH_TRADE_PRODUCT_TYPE_WX;
        if(productId == PayConstant.PAY_PRODUCT_WX_BAR) return MchConstant.MCH_TRADE_PRODUCT_TYPE_WX;
        if(productId == PayConstant.PAY_PRODUCT_ALIPAY_JSAPI) return MchConstant.MCH_TRADE_PRODUCT_TYPE_ALIPAY;
        if(productId == PayConstant.PAY_PRODUCT_ALIPAY_BAR) return MchConstant.MCH_TRADE_PRODUCT_TYPE_ALIPAY;
        if(productId == PayConstant.PAY_PRODUCT_UNIONPAY_BAR) return MchConstant.MCH_TRADE_PRODUCT_TYPE_UNIONPAY;
        if(productId == PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM) return MchConstant.MCH_TRADE_PRODUCT_TYPE_WX;
        return null;
    }

    /** 获取有效的图片格式， 返回null： 不支持的图片类型 **/
    public static String getImgSuffix(String filePath){

        String suffix = FileUtils.getFileSuffix(filePath, false).toLowerCase();
        if(MchConstant.ALLOW_UPLOAD_IMG_SUFFIX.contains(suffix)){
            return suffix;
        }
        throw new ServiceException(RetEnum.RET_SERVICE_NOT_ALLOW_UPLOAD_IMG);
    }


    private static final String SANDBOX_KEY = "xxpaysandbox";

    /** 获取支付宝正式环境/沙箱URL地址 type: 1-支付网关， 2-oauth接口地址，3-商户授权  **/
    public static String getAlipayUrl4env(int type, JSONObject param){

        boolean isSandbox = false;
        if(param != null && SANDBOX_KEY.equals(param.getString("sandboxKey"))){
            isSandbox = true;
        }

        switch (type){
            case 1: return isSandbox ? PayConstant.ALIPAY_GATEWAY_URL_SANDBOX : PayConstant.ALIPAY_GATEWAY_URL;
            case 2: return isSandbox ? PayConstant.ALIPAY_OAUTH2URL_SANDBOX : PayConstant.ALIPAY_OAUTH2URL;
            case 3: return isSandbox ? PayConstant.ALIPAY_MCH_AUTH_URL_SANDBOX : PayConstant.ALIPAY_MCH_AUTH_URL;
            default: return null;
        }
    }

    /** 获取云闪付正式环境/沙箱HOST地址   **/
    public static String getUnionpayHost4env(JSONObject param){

        boolean isSandbox = false;
        if(param != null && SANDBOX_KEY.equals(param.getString("sandboxKey"))){
            isSandbox = true;
        }

        return isSandbox ? PayConstant.UNIONPAY_HOST_TEST : PayConstant.UNIONPAY_HOST;
    }



    /** 封装公共查询条件 (用于统计数据) */
    public static Map packageDataCommonCondition(Date beginTime, Date endTime,
                                                 Long isvId, Long agentId, List<Long> agentIdList, Long mchId,
                                                 Long storeId, Long operatorId, Byte productType, Integer provinceCode, Integer cityCode){
        Map commonCondition = new HashMap();
        commonCondition.put("beginTime", beginTime);  //查询开始时间
        commonCondition.put("endTime", endTime);  //查询结束时间
        commonCondition.put("isvId", isvId);  //服务商ID
        commonCondition.put("agentId", agentId);  //代理商ID
        commonCondition.put("agentIdList", agentIdList);  //代理商ID LIST
        commonCondition.put("mchId", mchId);  //商户ID
        commonCondition.put("storeId", storeId);  //门店ID
        commonCondition.put("operatorId", operatorId);  //操作员ID
        commonCondition.put("productType", productType);  //产品类型
        commonCondition.put("provinceCode", provinceCode);  //查询省
        commonCondition.put("cityCode", cityCode);  //查询市
        return commonCondition;
    }


    /** 检查登录用户名是否合法 **/
    public static boolean checkLoginUserName(String loginName){

        if(StringUtils.isEmpty(loginName)) return false;
        return !loginName.matches("^\\d+$");
    }



}
