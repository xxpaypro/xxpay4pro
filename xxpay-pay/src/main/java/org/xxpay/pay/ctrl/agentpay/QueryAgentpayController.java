package org.xxpay.pay.ctrl.agentpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.AgentpayQueryRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.AgentpayService;
import org.xxpay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 代付查询
 * @date 2017-08-31
 * @version V1.0
 */
@RestController
public class QueryAgentpayController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryAgentpayController.class);

    @Autowired
    private AgentpayService agentpayService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询代付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/agentpay/query_order")
    public AbstractRes queryAgentpay(HttpServletRequest request) {
        _log.info("###### 开始接收商户查询代付订单请求 ######");
        String logPrefix = "【商户代付订单查询】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            // 验证参数有效性
            String errorMessage = validateParams(po, payContext);
            if (!"success".equalsIgnoreCase(errorMessage)) {
                _log.warn(errorMessage);
                return ApiBuilder.bizError(errorMessage);
            }
            _log.debug("请求参数及签名校验通过");
            Long mchId = po.getLong("mchId"); 			                    // 商户ID
            String mchOrderNo = po.getString("mchOrderNo"); 	            // 商户订单号
            String agentpayOrderId = po.getString("agentpayOrderId"); 	    // 代付订单号
            Boolean executeNotify = po.getBooleanValue("executeNotify");    // 是否执行回调

            AgentpayRecord mchAgentpayRecord = agentpayService.query(mchId, agentpayOrderId, mchOrderNo, executeNotify);
            _log.info("{}查询代付订单,结果:{}", logPrefix, mchAgentpayRecord);
            if (mchAgentpayRecord == null) {
                return ApiBuilder.bizError("代付订单不存在");
            }


            AgentpayQueryRes agentpayQueryRes = ApiBuilder.buildSuccess(AgentpayQueryRes.class);
            agentpayQueryRes.setByMchAgentpayRecord(mchAgentpayRecord);
            agentpayQueryRes.autoGenSign(payContext.getString("key")); //自动签名
            _log.info("###### 商户查询订单处理完成 ######");
            return agentpayQueryRes;

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
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			            // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	        // 商户订单号
        String agentpayOrderId = params.getString("agentpayOrderId"); 	// 代付订单号
        String reqTime = params.getString("reqTime");           // 请求时间
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        if(StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(agentpayOrderId)) {
            errorMessage = "request params[mchOrderNo or payOrderId] error.";
            return errorMessage;
        }
        if(!DateUtil.isValidDateTime(reqTime)) {
            errorMessage = "参数[reqTime]必填,且格式为yyyyMMddHHmmss";
            return errorMessage;
        }
        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "mchInfo not available [mchId="+mchId+"] record in db.";
            return errorMessage;
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "key is null[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        payContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }

        return "success";
    }

}
