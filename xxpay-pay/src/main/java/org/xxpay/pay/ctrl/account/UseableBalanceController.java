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
import org.xxpay.core.entity.UserAccount;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @Description: 账户可用余额操作接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-03-27
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class UseableBalanceController {

    private final MyLog _log = MyLog.getLog(UseableBalanceController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 变更账户可用余额
     * @param params
     * @return
     */
    @RequestMapping(value = "/api/account/change_useable_balance")
    public String recharge(@RequestParam String params) {
        _log.info("###### 开始接收账户可用余额变更请求 ######");
        String logPrefix = "【账户可以余额变更】";
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
            if(obj == null) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "账户可用余额变更失败", null, PayEnum.ERR_0010.getCode(), "账户可用余额变更失败"));
            String userId = obj.getString("userId");
            Short changeType = obj.getShort("changeType");
            Long mchId = obj.getLong("mchId");
            Long amount = obj.getLong("amount");
            int result = 0;
            if(changeType == 1) {
                // 可用余额增加
                result = rpcCommonService.rpcUserAccountService.userUseableAccountRollIn(mchId, userId, amount);
                _log.info("{}增加 userId={},mchId={},amount={},结果:{}", logPrefix, userId, mchId, amount, result);
            }else if(changeType == -1) {
                // 可用余额减少
                result = rpcCommonService.rpcUserAccountService.userUseableAccountRollOut(mchId, userId, amount);
                _log.info("{}减少 userId={},mchId={},amount={},结果:{}", logPrefix, userId, mchId, amount, result);
            }
            if(result != 1) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "账户可用余额变更失败", null, PayEnum.ERR_0010.getCode(), "DB操作失败"));
            }

            UserAccount userAccount = rpcCommonService.rpcUserAccountService.getUserAccount(mchId, userId);
            if(userAccount == null) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "查询账户返回为空", null, PayEnum.ERR_0010.getCode(), "查询账户返回为空"));
            }
            // 构建返回对象
            JSONObject accObj = new JSONObject();
            accObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            accObj.put("userId", userAccount.getUserId());
            accObj.put("mchId", userAccount.getMchId());
            accObj.put("balance", userAccount.getBalance());                // 账户余额
            accObj.put("useableBalance", userAccount.getUseableBalance());  // 可用余额
            accObj.put("state", userAccount.getState());                    // 账户状态.0表示账户冻结.1表示正常
            accObj.put("totalRollIn", userAccount.getTotalRollIn());        // 累计收入
            accObj.put("totalRollOut", userAccount.getTotalRollOut());      // 累计支出
            accObj.put("accountOpenTime", userAccount.getCreateTime().getTime());     // 账户开户时间

            return XXPayUtil.makeRetData(accObj, payContext.getString("key"));

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
        String userId = params.getString("userId");             // 用户ID
        Short changeType = params.getShort("changeType");       // 变更类型: 1 增加, -1 减少
        String amount = params.getString("amount"); 		    // 充值金额（单位分）
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        if(StringUtils.isBlank(userId)) {
            errorMessage = "request params[userId] error.";
            return errorMessage;
        }
        if(changeType == null || (changeType != 1 && changeType != -1)) {
            errorMessage = "request params[changeType] error.";
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
        object.put("userId", userId);
        object.put("changeType", changeType);
        object.put("amount", amount);

        return object;
    }

}
