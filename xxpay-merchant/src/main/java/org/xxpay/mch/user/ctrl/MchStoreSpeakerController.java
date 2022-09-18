package org.xxpay.mch.user.ctrl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.utils.SpeakerUtil;

/**
 * <p>
 * 门店与云喇叭关联表 前端控制器
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-09
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_store_speaker")
public class MchStoreSpeakerController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private final static MyLog _log = MyLog.getLog(MchStoreSpeakerController.class);

    /**
     * 获取绑定信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long storeId = getValLongRequired( "storeId");
        MchStoreSpeaker speaker = rpcCommonService.rpcMchStoreSpeakerService.getById(storeId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(speaker));
    }


    /**
     * 门店申请绑定或解绑云喇叭
     * @return
     */
    @RequestMapping("/binding_speaker")
    @ResponseBody
    public ResponseEntity<?> bindingSpeaker() {
        //获取验证金额
        String codeMoney = getValStringRequired("codeMoney");
        Long storeId = getValLongRequired("storeId");
        String relieve = getValStringRequired("relieve");
        String cent = AmountUtil.convertDollar2Cent(codeMoney);
        Long moneyCode = Long.valueOf(cent);
        Long userId = getUser().getBelongInfoId();
        MchInfo mchUser = rpcCommonService.rpcMchInfoService.getById(userId);

        //用户是否存在
        if (mchUser == null) {
            _log.info("用户不存在userId={}", userId);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        //用户状态是否正常
        if (mchUser.getStatus() != MchConstant.PUB_YES) {
            _log.info("用户状态已禁用user={}", mchUser);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STATUS_CLOSE));
        }
        //根据所属商户创建者ID查询是否为服务商创建
        IsvInfo isvInfo = rpcCommonService.rpcIsvInfoService.getById(mchUser.getIsvId());
        if (isvInfo == null) {
            _log.info("该用户非服务商创建user={}", mchUser);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_ISV_NOT_EXIST));
        }
        //服务商是否已配置云喇叭配置信息
        IsvSpeakerConfig config = rpcCommonService.rpcIsvSpeakerConfigService.getOne(
                new QueryWrapper<IsvSpeakerConfig>().lambda()
                        .eq(IsvSpeakerConfig::getIsvId, isvInfo.getIsvId())
        );
        if (config == null) {
            _log.info("服务商未配置云喇叭信息");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_ISV_SPEAKER_CONFIG_ERROR));
        }
        if (config.getStatus() == MchConstant.PUB_NO) {
            _log.info("服务商配置已关闭");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_ISV_SPEAKER_CONFIG_ERROR));
        }
        //查询门店信息
        MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(storeId);
        if (mchStore == null) {
            _log.info("门店不存在");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
        }
        //查询门店是否已绑定
        MchStoreSpeaker storeSpeaker = rpcCommonService.rpcMchStoreSpeakerService.getById(mchStore.getStoreId());
        if (storeSpeaker != null) {
            if (storeSpeaker.getStatus() == MchConstant.MCH_STORE_SPEAKER_STATUS_USED && "1".equals(relieve)){
                _log.info("设备已绑定");
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_SPEAKER_STATUS_ERROR));
            }
        }
        if (!storeSpeaker.getMoneyCode().equals(moneyCode)) {
            _log.info("绑定/解绑失败，验证金额错误");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_SPEAKER_CODE_ERROR));
        }

        JSONObject json = SpeakerUtil.bindOrRelieve(config.getToken(), relieve, storeSpeaker.getSpeakerId(), storeSpeaker.getStoreId() + "");
        String code = json.getString("code");
        if ("WG10005".equals(code)){
            getSpeakerJson(config);
            json = SpeakerUtil.bindOrRelieve(config.getToken(), relieve, storeSpeaker.getSpeakerId(), storeSpeaker.getStoreId() + "");
            code = json.getString("code");
        }
        if (!"0".equals(code)){
            _log.info("绑定失败", json.getString("msg"));
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL,json.getString("msg")));
        }
        if ("0".equals(relieve)){
            //更新为已解绑
            storeSpeaker.setStatus(MchConstant.MCH_STORE_SPEAKER_STATUS_RELIEVE);

        }else if ("1".equals(relieve)){
            //更新为绑定
            storeSpeaker.setStatus(MchConstant.MCH_STORE_SPEAKER_STATUS_USED);
        }
        boolean result = rpcCommonService.rpcMchStoreSpeakerService.updateById(storeSpeaker);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 门店申请绑定云喇叭发送验证
     * @return
     */
    @RequestMapping("/confirm_speaker")
    @ResponseBody
    public ResponseEntity<?> confirmSpeaker() {
        //获取喇叭ID、门店ID
        Long speakerId = getValLongRequired("speakerId");
        Long storeId = getValLongRequired("storeId");
        Long userId = getUser().getBelongInfoId();
        MchInfo mchUser = rpcCommonService.rpcMchInfoService.getById(userId);

        //用户是否存在
        if (mchUser == null) {
            _log.info("用户不存在userId={}", userId);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        //用户状态是否正常
        if (mchUser.getStatus() != MchConstant.PUB_YES) {
            _log.info("用户状态已禁用user={}", mchUser);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STATUS_CLOSE));
        }
        //根据所属商户创建者ID查询是否为服务商创建
        IsvInfo isvInfo = rpcCommonService.rpcIsvInfoService.getById(mchUser.getIsvId());
        if (isvInfo == null) {
            _log.info("该用户非服务商创建user={}", mchUser);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_ISV_NOT_EXIST));
        }
        //服务商是否已配置云喇叭配置信息
        IsvSpeakerConfig config = rpcCommonService.rpcIsvSpeakerConfigService.getOne(
                new QueryWrapper<IsvSpeakerConfig>().lambda()
                        .eq(IsvSpeakerConfig::getIsvId, isvInfo.getIsvId())
        );
        if (config == null) {
            _log.info("服务商未配置云喇叭信息");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_ISV_SPEAKER_CONFIG_ERROR));
        }
        //查询门店信息
        MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(storeId);
        if (mchStore == null) {
            _log.info("门店不存在");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
        }
        //生成验证金额，存入门店关联信息
        Long codeMoney = rpcCommonService.rpcMchStoreSpeakerService.creatCodeMoney(mchStore.getStoreId(), speakerId);
        String signature = null;
        JSONObject result = null;
        String code = null;
        //获取云喇叭服务器token
        if (config.getToken() == null) {
            //获取token并存入失效时间
            getSpeakerJson(config);
        }else {
            Integer expireTime = config.getTokenExpire();
            if (expireTime < 10) {
                //获取token并存入失效时间
                getSpeakerJson(config);
            }
            signature = config.getToken();
        }
        result = SpeakerUtil.postMsg(signature, speakerId, codeMoney, 1);
        code = result.getString("code");
        //如果为验签失败
        if ("WG10005".equals(code)) {
            getSpeakerJson(config);
            signature = config.getToken();
            result = SpeakerUtil.postMsg(signature, speakerId, codeMoney, 1);
            code = result.getString("code");
        }
        if (!"0".equals(code)){
            _log.info("发送消息失败{}", result.getString("msg"));
            return ResponseEntity.ok(BizResponse.build((RetEnum.RET_COMM_OPERATION_FAIL), result.getString("msg")));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(codeMoney));
    }

    private void getSpeakerJson(IsvSpeakerConfig config){
        JSONObject json = SpeakerUtil.getSignature(config.getUserId(), config.getUserPassword());
        config.setToken(json.getString("signature"));
        config.setTokenExpire(json.getInteger("remainTime"));
        rpcCommonService.rpcIsvSpeakerConfigService.updateById(config);
    }

}
