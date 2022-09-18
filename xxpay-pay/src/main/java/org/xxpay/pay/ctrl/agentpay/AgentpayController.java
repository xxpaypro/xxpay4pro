package org.xxpay.pay.ctrl.agentpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.AgentPayRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.service.IXxPayAgentpayService;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 代付
 * @date 2018-10-02
 * @version V1.0
 */
@RestController
public class AgentpayController extends BaseController {

    private final MyLog _log = MyLog.getLog(AgentpayController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    IXxPayAgentpayService xxPayAgentpayService;

    /**
     * 商户代付接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建商户代付订单
     * 3)发起转账请求
     * 4)返回下单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/agentpay/apply")
    public AbstractRes transOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户代付请求 ######");
        try {
            JSONObject po = getJsonParam(request);

            String logPrefix = "【商户API代付】";
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject agentpayContext = new JSONObject();
            AgentpayRecord mchAgentpayRecord = null;
            // 验证参数有效性
            Object object = validateParams(po, agentpayContext, request);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return ApiBuilder.bizError(object.toString());
            }
            if (object instanceof AgentpayRecord) mchAgentpayRecord = (AgentpayRecord) object;
            if(mchAgentpayRecord == null) return ApiBuilder.bizError("支付网关代付失败");
            int agentPayResult = xxPayAgentpayService.applyAgentpay(mchAgentpayRecord, agentpayContext.getString("key"));

            _log.info("{}创建代付订单,结果:{}", logPrefix, agentPayResult);
            if(agentPayResult != 1) {
                return ApiBuilder.bizError("申请代付失败");
            }
            // 查询最新代付记录
            AgentpayRecord dbRecord = rpcCommonService.rpcAgentpayService.findByAgentpayOrderId(mchAgentpayRecord.getAgentpayOrderId());

            AgentPayRes result = ApiBuilder.buildSuccess(AgentPayRes.class);
            result.setAgentpayOrderId(dbRecord.getAgentpayOrderId());
            result.setStatus(dbRecord.getStatus());
            result.setFee(dbRecord.getFee());
            result.setTransMsg(dbRecord.getTransMsg());
            result.setExtra(dbRecord.getExtra());
            result.autoGenSign(agentpayContext.getString("key"));
            return result;

        }catch (ServiceException e) {
            _log.error(e, "");
            return ApiBuilder.bizError( e.getErrMsg());
        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("支付网关系统异常");
        }
    }


    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject agentpayContext, HttpServletRequest request) {

        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 代付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户代付单号
        String amount = params.getString("amount"); 		    // 代付金额（单位分）
        Byte accountAttr = params.getByteValue("accountAttr");  // 账户属性:0-对私,1-对公,默认对私
        String accountName = params.getString("accountName");   // 收款人账户名
        String accountNo = params.getString("accountNo");       // 收款人账户号
        String province = params.getString("province");         // 开户行所在省份
        String city = params.getString("city");                 // 开户行所在市
        String bankName = params.getString("bankName");         // 开户行名称
        String bankNumber = params.getString("bankNumber");     // 联行号
        String notifyUrl = params.getString("notifyUrl"); 		// 转账结果回调URL
        String remark = params.getString("remark");	            // 备注
        String extra = params.getString("extra");		        // 扩展域
        String reqTime = params.getString("reqTime");           // 请求时间
        Byte subAmountFrom = params.getByte("subAmountFrom"); //扣减资金来源:1:从收款账户出款,2:从代付余额账户出款
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "参数[mchI]d必填,且为数值类型.mchId=" + mchId;
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "商户不存在.mchId=" + mchId;
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户状态不可用.mchId=" + mchId;
            return errorMessage;
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "商户没有配置私钥.mchId=" + mchId;
            return errorMessage;
        }
        agentpayContext.put("key", key);
        agentpayContext.put("mchInfo", mchInfo);

        if(StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "参数[mchOrderNo]必填";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "参数[amount]必填且为数字";
            return errorMessage;
        }
        if(Long.parseLong(amount) <= 0) {
            errorMessage = "参数[amount]必须大于0";
            return errorMessage;
        }
        if(StringUtils.isBlank(accountName)) {
            errorMessage = "参数[accountName]必填";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(accountNo)) {
            errorMessage = "参数[accountNo]必填且为数值";
            return errorMessage;
        }
        if(accountAttr != null && accountAttr == 1) {
            if(StringUtils.isBlank(province)) {
                errorMessage = "对公代付,参数[province]必填";
                return errorMessage;
            }
            if(StringUtils.isBlank(city)) {
                errorMessage = "对公代付,参数[city]必填";
                return errorMessage;
            }
            if(StringUtils.isBlank(bankName)) {
                errorMessage = "对公代付,参数[bankName]必填";
                return errorMessage;
            }
            if(bankNumber == null) {
                errorMessage = "对公代付,参数[bankNumber]必填";
                return errorMessage;
            }
        }
        if(StringUtils.isBlank(remark)) {
            errorMessage = "参数[remark]必填";
            return errorMessage;
        }
        if(!DateUtil.isValidDateTime(reqTime)) {
            errorMessage = "参数[reqTime]必填,且格式为yyyyMMddHHmmss";
            return errorMessage;
        }

        if(subAmountFrom == null || (subAmountFrom != MchConstant.AGENTPAY_OUT_PAY_BALANCE && subAmountFrom != MchConstant.AGENTPAY_OUT_AGENTPAY_BALANCE) ){
            return "参数[subAmountFrom]必填, 1:从收款账户出款,2:从代付余额账户出款";
        }

        // 签名信息
        if (StringUtils.isBlank(sign)) {
            errorMessage = "参数[sign]必填";
            return errorMessage;
        }

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名不通过.";
            return errorMessage;
        }

        // 验证参数通过,AgentpayRecord
        AgentpayRecord mchAgentpayRecord = new AgentpayRecord();
        mchAgentpayRecord.setAgentpayOrderId(MySeq.getAgentpay());
        mchAgentpayRecord.setMchOrderNo(mchOrderNo);
        mchAgentpayRecord.setInfoId(mchIdL);
        mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchAgentpayRecord.setMchType(mchInfo.getType());
        mchAgentpayRecord.setAccountName(accountName);              // 账户名
        mchAgentpayRecord.setAccountNo(accountNo);                  // 账号
        mchAgentpayRecord.setAmount(Long.parseLong(amount));        // 代付金额
        mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_API);    // 设置API代付
        mchAgentpayRecord.setDevice("");
        mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
        mchAgentpayRecord.setAccountAttr(accountAttr);
        mchAgentpayRecord.setProvince(province);
        mchAgentpayRecord.setCity(city);
        mchAgentpayRecord.setBankName(bankName);
        mchAgentpayRecord.setBankNumber(bankNumber);
        mchAgentpayRecord.setNotifyUrl(notifyUrl);
        mchAgentpayRecord.setRemark(remark);
        mchAgentpayRecord.setExtra(extra);
        mchAgentpayRecord.setSubAmountFrom(subAmountFrom);
        return mchAgentpayRecord;
    }

}
