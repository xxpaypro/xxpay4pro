package org.xxpay.pay.ctrl.account;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchApp;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @Description: 转账接口(同一商户下,用户A转钱给用户B操作)
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-03-27
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class TransferController {

    private final MyLog _log = MyLog.getLog(TransferController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 转账
     * @param params
     * @return
     */
    @RequestMapping(value = "/api/account/transfer")
    public String transfer(@RequestParam String params) {
        _log.info("###### 开始接收账户转账请求 ######");
        String logPrefix = "【账户转账】";
        _log.info("{}请求参数:{}", logPrefix, params);
        try {
            JSONObject po = JSONObject.parseObject(params);
            JSONObject payContext = new JSONObject();
            JSONObject obj = null;
            // 验证参数有效性
            Object object = validateParams(po, payContext);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, PayEnum.ERR_0014.getCode(), object.toString()));
            }

            if (object instanceof JSONObject) obj = (JSONObject) object;
            if(obj == null) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "账户转账失败", null, PayEnum.ERR_0010.getCode(), "账户转账失败"));
            Long mchId = obj.getLong("mchId");
            String fromUserId = obj.getString("fromUserId");
            String toUserId = obj.getString("toUserId");
            Long amount = obj.getLong("amount");

            int result = rpcCommonService.rpcUserAccountService.transferAccount(mchId, fromUserId, toUserId, amount);
            _log.info("{}fromUserId={},userId={},mchId={},amount={},结果:{}", logPrefix, fromUserId, toUserId, mchId, amount, result);
            if(result != 1) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心账户转账失败", null, PayEnum.ERR_0010.getCode(), "DB操作失败"));
            }

            JSONObject retObj = new JSONObject();
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            retObj.put("transResult", 1);

            return XXPayUtil.makeRetData(retObj, payContext.getString("key"));

        }catch (Exception e) {
            _log.error(e, "");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, PayEnum.ERR_0010.getCode(), "请联系技术人员查看"));
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String fromUserId = params.getString("fromUserId");     // 来源用户ID
        String toUserId = params.getString("toUserId");         // 目标用户ID
        String amount = params.getString("amount"); 		    // 转账金额（单位分）
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(fromUserId)) {
            errorMessage = "request params[fromUserId] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(toUserId)) {
            errorMessage = "request params[toUserId] error.";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "request params[amount] error.";
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isBlank(sign)) {
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
        // 验证参数通过,返回JSONObject对象
        JSONObject object = new JSONObject();
        object.put("mchId", mchIdL);
        object.put("fromUserId", fromUserId);
        object.put("toUserId", toUserId);
        object.put("amount", amount);

        return object;
    }

}
