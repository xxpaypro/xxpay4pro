package org.xxpay.pay.channel.transfarpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.BankCardBin;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.pay.channel.BaseTrans;
import org.xxpay.pay.channel.transfarpay.util.DateUtils;
import org.xxpay.pay.channel.transfarpay.util.HttpClient4Utils;
import org.xxpay.pay.channel.transfarpay.util.ParamUtil;
import org.xxpay.pay.service.RpcCommonService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/07/22
 * @description: 传化代付接口
 */
@Service
public class TransfarpayTransService extends BaseTrans {

    private static final MyLog _log = MyLog.getLog(TransfarpayTransService.class);

    private final static String charset = "UTF-8";

    @Autowired
    protected RpcCommonService rpcCommonService;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_TRANSFARPAY;
    }
    @Override
    public QueryRetMsg trans(TransOrder transOrder) {
        String logPrefix = "【传化代付】";
        try {
            String transOrderId = transOrder.getTransOrderId();
            TransfarpayConfig transfarpayConfig = new TransfarpayConfig(getTransParam(transOrder));
            String url = transfarpayConfig.getReqUrl();	// 接口地址
            Map<String, Object> map = new HashMap<>();
            map.put("service_id", "tf56enterprise.enterprise.payForCustomer");
            map.put("appid", transfarpayConfig.getAppId());
            map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
            map.put("businessnumber", transOrderId);                                // 业务流水号
            map.put("subject", "提现");                                              // 商品名称
            map.put("transactionamount", AmountUtil.convertCent2Dollar(transOrder.getAmount()+"")); // 金额,单位元
            map.put("bankcardnumber", transOrder.getAccountNo());                   // 卡号
            map.put("bankcardname", transOrder.getAccountName());                   // 姓名
            // 通过卡bin查询到银行卡信息
            BankCardBin bankCardBin = rpcCommonService.rpcBankCardBinService.findByCardNo(transOrder.getAccountNo());
            map.put("bankcardtype", "个人");                                         // 银行卡类型
            map.put("bankaccounttype", "储蓄卡");                                    // 银行卡借贷类型
            map.put("bankname", bankCardBin == null ? "" : bankCardBin.getBankName().replace("\n",""));        // 银行名称(非必填)
            map.put("backurl", payConfig.getNotifyTransUrl(getChannelName()));      // 异步通知地址
            map.put("fromaccountnumber", transfarpayConfig.getAccountNumber());     // 支付账号
            map.put("dog_sk", transfarpayConfig.getDogSk());                        // 私钥
            map.put("sign_type", "MD5");                                            // 签名类型

            map.put("tf_sign", ParamUtil.map2MD5(map));
            map.remove("dog_sk");

            _log.info("{}发起转账,请求参数:{}", logPrefix, map);
            String responseData = HttpClient4Utils.sendHttpRequest(url, map, charset, false);
            _log.info("{}转账完成,返回结果:{}", logPrefix, responseData);
            JSONObject resObj = JSONObject.parseObject(responseData);
            // success：请求授理成功，不代表交易成功 error：业务失败 exception：网络异常失败，不代表交易失败
            String result = resObj.getString("result");
            // 错误编码
            String code = resObj.getString("code");
            // 错误描述
            String msg = resObj.getString("msg");
            if("success".equalsIgnoreCase(result)) {
                JSONObject data = resObj.getJSONObject("data");
                // 成功  失败  处理中  已退票
                if("成功".equals(data.getString("status"))) {   // 成功
                    _log.info("{} >>> 转账成功", logPrefix);
                    return QueryRetMsg.confirmSuccess(data.getString("businessrecordnumber"));
                }else if("处理中".equals(data.getString("status"))) {
                    // 同步返回处理中,等待通道的异步通知回调继续处理
                    _log.info("{} >>> 转账处理中", logPrefix);
                    return QueryRetMsg.waiting();

                }else if("失败".equals(data.getString("status")) ||"已退票".equals(data.getString("status"))){ // 失败
                    // 处理失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    return QueryRetMsg.confirmFail(null, code, msg);
                }else {
                    // 失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    return QueryRetMsg.confirmFail(null, code, msg);
                }
            }else if("exception".equalsIgnoreCase(result)) {
                _log.info("{} >>> 返回网络异常失败,系统认为转账处理中...", logPrefix);
                return QueryRetMsg.waiting();
            }else if("error".equalsIgnoreCase(result)) { // 明确失败
                _log.info("{} >>> 转账失败", logPrefix);
                return QueryRetMsg.confirmFail(null, code, msg);
            }else{
                return QueryRetMsg.waiting(); //状态码未知
            }

        }catch (Exception e) {
            _log.error(e, "转账异常");
            return QueryRetMsg.sysError();
        }
    }

    public QueryRetMsg query(TransOrder transOrder) {
        String logPrefix = "【传化代付查询】";

        QueryRetMsg retObj ;

        try{

            TransfarpayConfig transfarpayConfig = new TransfarpayConfig(getTransParam(transOrder));
            Map<String, Object> map = new HashMap<>();
            map.put("service_id", "tf56pay.enterprise.queryTradeStatus");
            map.put("appid", transfarpayConfig.getAppId());
            map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
            map.put("businessnumber", transOrder.getTransOrderId());
            map.put("dog_sk", transfarpayConfig.getDogSk());
            map.put("sign_type", "MD5");

            map.put("tf_sign", ParamUtil.map2MD5(map));
            map.remove("dog_sk");

            _log.info("{}发起查询,请求参数:{}", logPrefix, map);
            String responseData = HttpClient4Utils.sendHttpRequest(transfarpayConfig.getReqUrl(), map, charset, false);
            _log.info("{}查询完成,返回结果:{}", logPrefix, responseData);

            JSONObject resObj = JSONObject.parseObject(responseData);
            // success：请求授理成功，不代表交易成功 error：业务失败 exception：网络异常失败，不代表交易失败
            String result = resObj.getString("result");
            // 错误编码
            String code = resObj.getString("code");
            // 错误描述
            String msg = resObj.getString("msg");

            if("success".equalsIgnoreCase(result)) {
                JSONObject data = resObj.getJSONObject("data");
                String status = data.getString("status");
                // 成功  失败  处理中  已退票
                if("成功".equals(status)) {   // 成功
                    _log.info("{} >>> 转账成功", logPrefix);
                    retObj = QueryRetMsg.confirmSuccess(data.getString("businessrecordnumber"));
                    retObj.setChannelOriginResponse(resObj.toJSONString());
                    return retObj;


                }else if("处理中".equals(data.getString("status"))) {
                    // 同步返回处理中,等待通道的异步通知回调继续处理
                    _log.info("{} >>> 转账处理中", logPrefix);
                    retObj = QueryRetMsg.waiting();
                    retObj.setChannelOriginResponse(resObj.toJSONString());
                    return retObj;


                }else if("失败".equals(data.getString("status")) ||"已退票".equals(data.getString("status"))){ // 失败
                    // 处理失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    retObj = QueryRetMsg.confirmFail(null, code, msg);
                    retObj.setChannelOriginResponse(resObj.toJSONString());
                    return retObj;
                }else {
                    // 失败
                    _log.info("{} >>> 返回结果与接口描述不一致,认为订单为处理中", logPrefix);
                    retObj = QueryRetMsg.unknown();
                    retObj.setChannelOriginResponse(resObj.toJSONString());
                    return retObj;
                }
            }else if("exception".equalsIgnoreCase(result)) {
                _log.info("{} >>> 返回网络异常失败", logPrefix);
                retObj = QueryRetMsg.unknown();
                retObj.setChannelOriginResponse(resObj.toJSONString());
                return retObj;
            }else if("error".equalsIgnoreCase(result)) { // 明确失败
                _log.info("{} >>> 返回业务失败", logPrefix);
                retObj = QueryRetMsg.confirmFail(null, code, msg);
                retObj.setChannelOriginResponse(resObj.toJSONString());
                return retObj;
            }else{ //未知状态

                retObj = QueryRetMsg.unknown();
                retObj.setChannelOriginResponse(resObj.toJSONString());
                return retObj;

            }
        }catch (Exception e) {
            _log.error(e, "传化代付查询异常");
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AgentPayBalanceRetMsg balance(String payParam) {
        String logPrefix = "【传化余额查询】";
        try {

            TransfarpayConfig transfarpayConfig = new TransfarpayConfig(payParam);
            String url = transfarpayConfig.getReqUrl();	// 接口地址
            Map<String, Object> map = new HashMap<>();
            map.put("service_id", "tf56pay.enterprise.queryEnterpriseAccountBanlance");
            map.put("appid", transfarpayConfig.getAppId());
            map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
            map.put("accountnumber", transfarpayConfig.getAccountNumber());     // 支付账号
            map.put("dog_sk", transfarpayConfig.getDogSk());                    // 私钥
            map.put("sign_type", "MD5");                                        // 签名类型
            map.put("tf_sign", ParamUtil.map2MD5(map));
            map.remove("dog_sk");
            _log.info("{}请求参数:{}", logPrefix, map);
            String responseData = HttpClient4Utils.sendHttpRequest(url, map, charset, false);
            _log.info("{}返回结果:{}", logPrefix, responseData);
            JSONObject resObj = JSONObject.parseObject(responseData);
            // success：请求授理成功，不代表交易成功 error：业务失败 exception：网络异常失败，不代表交易失败
            String result = resObj.getString("result");
            if("success".equalsIgnoreCase(result)) {
                JSONObject data = resObj.getJSONObject("data");
                return AgentPayBalanceRetMsg.success(
                        Long.parseLong(AmountUtil.convertDollar2Cent(data.getString("balance"))), // 现金余额
                        Long.parseLong(AmountUtil.convertDollar2Cent(data.getString("usableBalance"))) // 代付可用余额
                );
            }

            return AgentPayBalanceRetMsg.fail();
        }catch (Exception e) {
            _log.error(e, "查询头寸异常");
            return AgentPayBalanceRetMsg.fail();
        }
    }

    public static void main(String[] args) {

        System.out.println(String.format("%12d", 600).replace(" ", "0"));


    }

}
