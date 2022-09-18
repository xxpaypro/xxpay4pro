package org.xxpay.pay.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.common.vo.AgpayExtConfigVO;
import org.xxpay.core.entity.*;
import org.xxpay.pay.channel.TransInterface;
import org.xxpay.pay.mq.BaseNotify4MchTrans;
import org.xxpay.pay.mq.Mq4TransNotify;
import org.xxpay.pay.util.SpringUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/10/30
 * @description:
 */
@Service
public class TransOrderService {

    private static final MyLog _log = MyLog.getLog(TransOrderService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private Mq4TransNotify mq4TransNotify;

    @Autowired
    private BaseNotify4MchTrans baseNotify4MchTrans;

    public void sendTransNotify(String transOrderId, String channelType) {
        JSONObject object = new JSONObject();
        object.put("transOrderId", transOrderId);
        object.put("channelType", channelType);
        mq4TransNotify.send(object.toJSONString());
    }

    public JSONObject query(Long mchId, String transOrderId, String mchTransNo, String executeNotify) {
        TransOrder transOrder;
        if(StringUtils.isNotBlank(transOrderId)) {
            transOrder = rpcCommonService.rpcTransOrderService.selectByMchIdAndTransOrderId(mchId, transOrderId);

        }else {
            transOrder = rpcCommonService.rpcTransOrderService.selectByInfoIdAndMchTransNo(mchId, MchConstant.INFO_TYPE_MCH, mchTransNo);
        }
        if(transOrder == null) return null;
        boolean isNotify = Boolean.parseBoolean(executeNotify);
        if(isNotify) {
            baseNotify4MchTrans.doNotify(transOrder, false);
            _log.info("业务查单完成,并再次发送业务转账通知.发送完成");

        }
        return (JSONObject) JSONObject.toJSON(transOrder);
    }

    /**
     * 创建转账订单
     * @param po
     * @return
     */
    public String createTransOrder(JSONObject po, boolean isMch) {
        String logPrefix = "【商户统一转账】";
        _log.info("{}请求参数:{}", logPrefix, po);
        JSONObject transContext = new JSONObject();
        TransOrder transOrder = null;
        // 验证参数有效性
        Object object = validateParams(po, transContext, isMch);
        if (object instanceof String) {
            _log.info("{}参数校验不通过:{}", logPrefix, object);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
        }
        if (object instanceof TransOrder) transOrder = (TransOrder) object;
        if(transOrder == null) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心转账失败", null, null));
        int result = rpcCommonService.rpcTransOrderService.createTransOrder(transOrder);
        _log.info("{}创建转账订单,结果:{}", logPrefix, result);
        if(result != 1) {
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "创建转账订单失败", null, null));
        }

        Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
        String transOrderId = transOrder.getTransOrderId();
        String channelType =  transOrder.getChannelType();

        // 修改转账状态为转账中
        result = rpcCommonService.rpcTransOrderService.updateStatus4Ing(transOrderId, "");
        if(result != 1) {
            _log.warn("更改转账为转账中({})失败,不能转账.transOrderId={}", PayConstant.TRANS_STATUS_TRANING, transOrderId);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心转账失败", null, PayEnum.ERR_0120));
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("transOrder", transOrder);
        QueryRetMsg retObj;

        try{
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            retObj = transInterface.trans(transOrder);
        }catch (BeansException e) {
            _log.warn("不支持的转账渠道,停止转账处理.transOrderId={},channelType={}", transOrderId, channelType);
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心转账失败", null, PayEnum.ERR_0119));
        }

        // 调用成功
        if(retObj != null) {
            // 判断业务结果

            if(QueryRetMsg.ChannelState.WAITING.equals(retObj.getChannelState()) ) { //支付中
                // 不处理
            }else if(QueryRetMsg.ChannelState.CONFIRM_SUCCESS.equals(retObj.getChannelState())) {
                // 更新转账状态为成功
                String channelOrderNo = retObj.getChannelOrderId();
                result = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, result);
            }else if(QueryRetMsg.ChannelState.CONFIRM_FAIL.equals(retObj.getChannelState())) {
                // 更新转账状态为失败
                String channelErrCode = retObj.getChannelErrCode();
                String channelErrMsg = retObj.getChannelErrMsg();
                result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
                _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
            }
        }else {
            // 更新转账状态为失败
            String channelErrCode = null;
            String channelErrMsg = null;
            result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
            _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
        }

        transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        // 返回的参数
        map.put("mchId", transOrder.getInfoId());
        map.put("appId", transOrder.getAppId());
        map.put("transOrderId", transOrder.getTransOrderId());
        map.put("mchTransNo", transOrder.getMchTransNo());
        map.put("amount", transOrder.getAmount());
        map.put("status", transOrder.getStatus());
        map.put("channelOrderNo", transOrder.getChannelOrderNo());
        map.put("channelErrMsg", transOrder.getChannelErrMsg());
        map.put("transSuccTime", transOrder.getTransSuccTime() == null ? "" : transOrder.getTransSuccTime().getTime());

        return XXPayUtil.makeRetData(map, transContext.getString("key"));
    }

    /**
     * 处理商户转账回调通知(如果notify不为空,则发送)
     * @param transOrder
     * @param isFirst
     */
    void doTransNotify(TransOrder transOrder, boolean isFirst) {
        if(StringUtils.isNotBlank(transOrder.getNotifyUrl())) {
            baseNotify4MchTrans.doNotify(transOrder, true);
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @param isMch 判断发起方是商户还是代理商发起的代付请求， 代理商代付不验签，获取支付账户从代理商表查询。
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject transContext, boolean isMch) {
        String riskLog = "[转账风控]";
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String agentId = params.getString("agentId"); 			    // 代理商ID
        String appId = params.getString("appId");               // 应用ID
        String mchTransNo = params.getString("mchTransNo"); 	// 商户转账单号
        String passageId = params.getString("passageId");       // 通道ID
        String amount = params.getString("amount"); 		    // 转账金额（单位分）
        String currency = params.getString("currency");         // 币种
        String clientIp = params.getString("clientIp");	        // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 转账结果回调URL
        String sign = params.getString("sign"); 				// 签名
        String channelUser = params.getString("channelUser");	// 渠道用户标识,如微信openId,支付宝账号
        Byte accountAttr = params.getByteValue("accountAttr");  // 账户属性:0-对私,1-对公,默认对私
        Byte accountType = params.getByte("accountType");       // 账户类型
        String accountName = params.getString("accountName");   // 账户名
        String accountNo = params.getString("accountNo");       // 账户号
        String province = params.getString("province");         // 开户行所在省份
        String city = params.getString("city");                 // 开户行所在市
        String bankName = params.getString("bankName");         // 开户行名称
        Long bankType = params.getLong("bankType");             // 联行号
        String bankCode = params.getString("bankCode");         // 银行代码


        String remarkInfo = params.getString("remarkInfo");	    // 备注
        // 验证请求参数有效性（必选项）
        Long mchIdL = 0L, agentIdL = 0L;

        if(isMch){
            if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
                errorMessage = "参数[mchI]d必填,且为数值类型.mchId=" + mchId;
                return errorMessage;
            }
            mchIdL = Long.parseLong(mchId);
        }else{
            if(StringUtils.isBlank(agentId) || !NumberUtils.isDigits(agentId)) {
                errorMessage = "参数[agentId]d必填,且为数值类型.agentId=" + agentId;
                return errorMessage;
            }
            agentIdL = Long.parseLong(agentId);

        }

        if(StringUtils.isBlank(mchTransNo)) {
            errorMessage = "参数[mchTrans]N必填";
            return errorMessage;
        }

        Integer passageIdI;
        if(StringUtils.isBlank(passageId) || !NumberUtils.isDigits(passageId)) {
            errorMessage = "参数[passageId]必填,且为数值类型.passageId=" + passageId;
            return errorMessage;
        }
        passageIdI = Integer.parseInt(passageId);
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "参数[amount]必填";
            return errorMessage;
        }
        if(Long.parseLong(amount) <= 0) {
            errorMessage = "参数[amount]必须大于0";
            return errorMessage;
        }
        if(StringUtils.isBlank(currency)) {
            errorMessage = "参数[currency]必填";
            return errorMessage;
        }

        if(StringUtils.isBlank(remarkInfo)) {
            errorMessage = "参数[remarkInfo]必填";
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "参数[sign]必填";
            return errorMessage;
        }
        String key = "";
        MchInfo mchInfo = null;
        if(isMch){
            // 查询商户信息
            mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
            if(mchInfo == null) {
                errorMessage = "商户不存在.mchId=" + mchId;
                return errorMessage;
            }
            if(mchInfo.getStatus() != MchConstant.PUB_YES) {
                errorMessage = "商户状态不可用.mchId=" + mchId;
                return errorMessage;
            }

            // 查询应用信息
            if(StringUtils.isNotBlank(appId)) {
                MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchIdL, appId);
                if(mchApp == null) {
                    errorMessage = "应用不存在.appId=" + appId;
                    return errorMessage;
                }
                if(mchApp.getStatus() != MchConstant.PUB_YES) {
                    errorMessage = "应用状态不可用.appId=" + appId;
                    return errorMessage;
                }
            }

            key = mchInfo.getPrivateKey();

        }else{
            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentIdL);
            if(agentInfo == null) {
                errorMessage = "代理商不存在.agentId=" + agentId;
                return errorMessage;
            }
            if(agentInfo.getStatus() != MchConstant.PUB_YES) {
                errorMessage = "代理商状态不可用.agentId=" + agentId;
                return errorMessage;
            }

        }


        if (StringUtils.isBlank(key)) {
            errorMessage = "商户没有配置私钥.mchId=" + mchId;
            return errorMessage;
        }
        transContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名不通过.";
            return errorMessage;
        }

        // 查询商户对应的代付渠道
        Integer passageAccountId;
        String channelMchId, channelType, channelId;     // 渠道商户ID,渠道接口类型,渠道接口
        BigDecimal channelRate = null;  // 渠道费率
        Long channelFeeEvery = null;    // 渠道每笔费用
        Long channelCost = null;        // 渠道成本


        AgentpayPassageAccount agentpayPassageAccount = null;
        Integer agentpayPassageAccountId = 0;

        byte infoType = isMch ? MchConstant.INFO_TYPE_MCH : MchConstant.INFO_TYPE_AGENT;
        Long infoId = isMch ? mchIdL : agentIdL;

        // 如果通道ID不为空,则根据通道ID查询
        // 如果通道ID为空,则查询该商户默认随机的可用通道

        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.findOne(infoType, infoId, MchConstant.FEE_SCALE_PTYPE_AGPAY, passageIdI );

        if(feeScale == null) {
            errorMessage = "没有配置代付通道.passageId=" + passageId;
            return errorMessage;
        }
        if(feeScale.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "该代付通道已关闭.passageId=" + passageId;
            return errorMessage;
        }
        // 获取代付通道子账户
        AgpayExtConfigVO agpayExtConfigVO = (AgpayExtConfigVO)feeScale.getExtConfigVO();
        if(agpayExtConfigVO == null) return "该代付通道未配置通道信息.passageId=" + passageId;

        if(agpayExtConfigVO.getCurPayPollParam() != null && agpayExtConfigVO.getCurPayPollParam().size() > 0){
            agentpayPassageAccountId = agpayExtConfigVO.getCurPayPollParam().get(0); //获取第一个
        }

        channelFeeEvery = feeScale.getFee().longValue();
        channelCost = channelFeeEvery;


        if(agentpayPassageAccountId != null && agentpayPassageAccountId > 0) {
            agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(agentpayPassageAccountId);
            AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(passageIdI);
            agentpayPassageAccount = getAvailableAPA(riskLog, Long.parseLong(amount), agentpayPassage, agentpayPassageAccount);
        }else {
            Object obj = getAPA(riskLog, passageIdI, Long.parseLong(amount));
            if(obj instanceof String) {
                return obj;
            }
            if(obj instanceof AgentpayPassageAccount) {
                agentpayPassageAccount = (AgentpayPassageAccount) obj;
            }
        }
        // 判断是否取到可用子账户
        if(agentpayPassageAccount == null) {
            errorMessage = "该代付通道没有可用子账户[agentpayPassageId="+passageId+"]";
            return errorMessage;
        }
        passageAccountId = agentpayPassageAccount.getId();
        channelType = agentpayPassageAccount.getIfTypeCode();
        channelId = agentpayPassageAccount.getIfCode();
        channelMchId = agentpayPassageAccount.getPassageMchId();


        if(PayConstant.PAY_CHANNEL_SANDPAY_AGENTPAY.equalsIgnoreCase(channelId)) {
            if(StringUtils.isBlank(accountName)) {
                errorMessage = "参数[accountName]必填";
                return errorMessage;
            }
            if(StringUtils.isBlank(accountNo)) {
                errorMessage = "参数[accountNo]必填";
                return errorMessage;
            }
        }

        // 如果通知地址不为空,则为异步
        if(StringUtils.isNotBlank(notifyUrl)){
            transContext.put("async", true);
        }

        // 验证参数通过,返回TransOrder对象
        TransOrder transOrder = new TransOrder();

        if(isMch){
            transOrder.setInfoId(mchIdL);
            transOrder.setInfoType(MchConstant.INFO_TYPE_MCH);
            transOrder.setAppId(appId);
            transOrder.setMchType(mchInfo.getType());
        }else{
            transOrder.setInfoId(agentIdL);
            transOrder.setInfoType(MchConstant.INFO_TYPE_AGENT);
        }

        transOrder.setTransOrderId(MySeq.getTrans());
        transOrder.setMchTransNo(mchTransNo);
        transOrder.setChannelType(channelType);
        transOrder.setChannelId(channelId);
        transOrder.setPassageId(passageIdI);
        transOrder.setPassageAccountId(passageAccountId);
        transOrder.setAmount(Long.parseLong(amount));
        transOrder.setCurrency(currency);
        transOrder.setClientIp(clientIp);
        transOrder.setDevice(device);
        transOrder.setRemarkInfo(remarkInfo);
        transOrder.setChannelUser(channelUser);
        transOrder.setAccountAttr(accountAttr);
        transOrder.setAccountType(accountType);
        transOrder.setAccountName(accountName);
        transOrder.setAccountNo(accountNo);
        transOrder.setProvince(province);
        transOrder.setCity(city);
        transOrder.setBankName(bankName);
        transOrder.setBankType(bankType);
        transOrder.setBankCode(bankCode);
        transOrder.setChannelMchId(channelMchId);
        transOrder.setChannelRate(channelRate);
        transOrder.setChannelFeeEvery(channelFeeEvery);
        transOrder.setChannelCost(channelCost);
        transOrder.setExtra(extra);
        transOrder.setParam1(param1);
        transOrder.setParam2(param2);
        transOrder.setNotifyUrl(notifyUrl);
        return transOrder;
    }

    /**
     * 根据代付通道ID取子账户
     * @param riskLog
     * @param agentpayPassageId
     * @param amountL
     * @return
     */
    private Object getAPA(String riskLog, int agentpayPassageId, long amountL) {
        // 判断代付通道
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null || agentpayPassage.getStatus() != MchConstant.PUB_YES) {
            return "代付通道不存在或已关闭[agentpayPassageId="+agentpayPassageId+"]";
        }
        // 得到可用的支付通道子账户
        List<AgentpayPassageAccount> agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.selectAllByPassageId(agentpayPassageId);
        if(CollectionUtils.isEmpty(agentpayPassageAccountList)) {
            return "该支付通道没有配置可用子账户[agentpayPassageId="+agentpayPassageId+"]";
        }

        // 需要根据风控规则得到子账户号
        // 随机打乱子账户顺序
        Collections.shuffle(agentpayPassageAccountList);

        for(AgentpayPassageAccount apa : agentpayPassageAccountList) {
            AgentpayPassageAccount availableAgentpayPassageAccount = getAvailableAPA(riskLog, amountL, agentpayPassage, apa);
            if(availableAgentpayPassageAccount == null) continue;
            return availableAgentpayPassageAccount;
        }
        return null;
    }

    /**
     * 获取可用子账户(需判断账户状态,及风控)
     * @param riskLog
     * @param amountL
     * @param apa
     * @return
     */
    private AgentpayPassageAccount getAvailableAPA(String riskLog, long amountL, AgentpayPassage agentpayPassage, AgentpayPassageAccount apa) {
        // 判断账号状态为关闭,则返回空
        if(apa.getStatus() != MchConstant.PUB_YES) {
            return null;
        }
        // 风控继承模式,设置继承的风控信息
        if(apa.getRiskMode() == 1) {
            apa.setRiskStatus(agentpayPassage.getRiskStatus());
            apa.setMaxDayAmount(agentpayPassage.getMaxDayAmount());
            apa.setTradeStartTime(agentpayPassage.getTradeStartTime());
            apa.setTradeEndTime(agentpayPassage.getTradeEndTime());
        }
        // 判断风控状态为关闭,则返回账号对象
        if(apa.getRiskStatus() == MchConstant.PUB_NO) {
            return apa;
        }
        // 下面逻辑判断是否触发风控规则,如果触发则返回空
        // 交易时间是否满足
        String ymd = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD);
        String tradeStartTime = ymd + " " + apa.getTradeStartTime();
        String tradeEndTime = ymd + " " + apa.getTradeEndTime();
        long startTime = DateUtil.str2date(tradeStartTime).getTime();
        long endTime = DateUtil.str2date(tradeEndTime).getTime();
        long currentTime = System.currentTimeMillis();
        if(currentTime < startTime || currentTime > endTime) {
            _log.info("{}交易时间触发风控.agetnpayPassageAccountId={},tradeStartTime={},tradeEndTime={}", riskLog, apa.getId(), tradeStartTime, tradeEndTime);
            return null;
        }
        // 今日总交易金额是否满足
        long maxDayAmount = apa.getMaxDayAmount();
        long dayAmount = rpcCommonService.rpcTransOrderService.sumAmount4AgentpayPassageAccount(apa.getId(),
                DateUtil.str2date(ymd + " 00:00:00"),
                DateUtil.str2date(ymd + " 23:59:59"));
        if(dayAmount > maxDayAmount) {
            _log.info("{}每日交易总额触发风控.agentpayPassageAccountId={},maxDayAmount={},dayAmount={}", riskLog, apa.getId(), maxDayAmount, dayAmount);
            return null;
        }
        return apa;
    }

}
