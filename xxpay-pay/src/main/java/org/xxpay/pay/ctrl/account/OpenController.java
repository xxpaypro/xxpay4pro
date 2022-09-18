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
 * @Description: 开户接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2018-03-27
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class OpenController {

    private final MyLog _log = MyLog.getLog(OpenController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 开户接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/api/account/open")
    public String open(@RequestParam String params) {
        _log.info("###### 开始接收开户请求 ######");
        String logPrefix = "【开户】";
        _log.info("{}请求参数:{}", logPrefix, params);
        try {
            JSONObject po = JSONObject.parseObject(params);
            JSONObject payContext = new JSONObject();
            JSONObject userObj = null;
            // 验证参数有效性
            Object object = validateParams(po, payContext);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, PayEnum.ERR_0014.getCode(), object.toString()));
            }

            if (object instanceof JSONObject) userObj = (JSONObject) object;
            if(userObj == null) return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心开户失败", null, PayEnum.ERR_0010.getCode(), "开户失败"));
            Long mchId = userObj.getLong("mchId");
            String userId = userObj.getString("userId");

            int result = rpcCommonService.rpcUserAccountService.openAccount(mchId, userId);
            _log.info("{}创建账户,结果:{}", logPrefix, result);
            if(result != 1) {
                return XXPayUtil.makeRetFail(XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心开户失败", null, PayEnum.ERR_0010.getCode(), "DB插入账户记录失败"));
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

        return object;
    }

}
