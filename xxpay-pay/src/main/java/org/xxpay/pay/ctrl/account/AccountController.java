package org.xxpay.pay.ctrl.account;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.UserAccount;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @Description: 账户操作接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-04-10
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class AccountController {

    private final MyLog _log = MyLog.getLog(AccountController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 变更账户可用余额
     * @param params
     * @return
     */
    @RequestMapping(value = "/api/account/update_amount")
    public String amountUpdate(@RequestParam String params) {
        _log.info("###### 开始接收账户金额修改请求 ######");
        String logPrefix = "【账户金额修改】";
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
            if(obj == null) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "账户金额修改失败", null, PayEnum.ERR_0010.getCode(), "账户金额修改失败"));
            Long mchId = obj.getLong("mchId");
            String userId = obj.getString("userId");
            Short changeType = obj.getShort("changeType");
            Long amount = obj.getLong("amount");
            Short useableChangeType = obj.getShort("useableChangeType");
            Long useableAmount = obj.getLong("useableAmount");
            int result = 0;

            if(changeType == 0) {
                if(useableChangeType == 1) {
                    // 增加可用余额
                    result = rpcCommonService.rpcUserAccountService.userUseableAccountRollIn(mchId, userId, useableAmount);
                }else if(useableChangeType == -1) {
                    // 减少可用余额
                    result = rpcCommonService.rpcUserAccountService.userUseableAccountRollOut(mchId, userId, useableAmount);
                }
            }else if(useableChangeType == 0) {
                if(changeType == 1) {
                    // 增加账户余额
                    result = rpcCommonService.rpcUserAccountService.userAccountRollIn(mchId, userId, amount);
                }else if(changeType == -1) {
                    // 减少账户余额
                    result = rpcCommonService.rpcUserAccountService.userAccountRollOut(mchId, userId, amount);
                }
            }else {
                result = rpcCommonService.rpcUserAccountService.updateUserAccount(mchId, userId, amount, changeType, useableAmount, useableChangeType);
            }

            if(result != 1) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "账户金额修改失败", null, PayEnum.ERR_0010.getCode(), "DB操作失败"));
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

        }catch (ServiceException e) {
            _log.error(e, "");
            return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, e.getRetEnum().getCode()+"", "请联系技术人员查看"));
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
        Short changeType = params.getShort("changeType");       // 账户余额变更类型: 1 增加, -1 减少, 0 不变
        String amount = params.getString("amount"); 		    // 账户金额（单位分）
        Short useableChangeType = params.getShort("useableChangeType");       // 可用余额变更类型: 1 增加, -1 减少, 0 不变
        String useableAmount = params.getString("useableAmount"); 		        // 可用余额（单位分）
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
        // 判断修改余额类型
        if(changeType == null || (changeType != 1
                && changeType != -1
                && changeType != 0)) {
            errorMessage = "request params[changeType] error.";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "request params[amount] error.";
            return errorMessage;
        }
        // 判断修改可用余额类型
        if(useableChangeType == null || (useableChangeType != 1
                && useableChangeType != -1
                && useableChangeType != 0)) {
            errorMessage = "request params[useableChangeType] error.";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(useableAmount)) {
            errorMessage = "request params[useableAmount] error.";
            return errorMessage;
        }
        // 如果余额变更和可用余额变更都为0,表示都不修改,则不允许
        if("0".equals(changeType) && "0".equals(useableChangeType)) {
            errorMessage = "request params[changeType,useableChangeType] error.";
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
        object.put("useableChangeType", useableChangeType);
        object.put("useableAmount", useableAmount);

        return object;
    }

}
