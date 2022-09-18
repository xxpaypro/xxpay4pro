package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.net.URLDecoder;

/**
 * 刷脸支付（给商户端提供与微信或支付宝刷脸交互接口）
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/face_pay")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class FacePayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(FacePayController.class);

    /**
     * 支付宝刷脸支付初始化接口
     * @return
     */
    @RequestMapping("/alipay_initialize")
    @ResponseBody
    public ResponseEntity<?> alipayInitialize() {
        Long mchId = getUser().getBelongInfoId();                         // 商户ID
        String storeId = getValStringRequired("storeId");            // 门店ID
        String storeName = getValStringRequired("storeName");        // 门店名称
        String deviceId = getValStringRequired("deviceId");          // 设备
        String metaInfo = getValStringRequired( "metaInfo");        // 刷脸客户端提供的信息
        _log.info("[支付宝刷脸支付初始化]mchId={},storeId={},storeName={},deviceId={},metaInfo={}", mchId, storeId, storeName, deviceId, metaInfo);
        JSONObject resultJson = rpcCommonService.rpcXxPayAlipayApiService.authenticationSmilepayInitialize(mchId, URLDecoder.decode(metaInfo));
        if(resultJson == null) {
            _log.error("调用支付宝刷脸支付初始化失败");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("zimId", resultJson.getString("zimId"));
        jsonObject.put("zimInitClientData", resultJson.getString("zimInitClientData"));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(jsonObject));
    }

    /**
     * 获取微信刷脸调用凭证
     * @return
     */
    @RequestMapping("/get_wxpayface_authinfo")
    @ResponseBody
    public ResponseEntity<?> getWxpayFaceAuthInfo() {
        Long mchId = getUser().getBelongInfoId();                         // 商户ID
        String storeId = getValStringRequired("storeId");            // 门店ID
        String storeName = getValStringRequired("storeName");        // 门店名称
        String deviceId = getValStringRequired("deviceId");          // 设备
        String rawdata = getValStringRequired("rawdata");            // 刷脸数据
        _log.info("[获取微信刷脸调用凭证]mchId={},storeId={},storeName={},deviceId={},rawdata={}", mchId, storeId, storeName, deviceId, rawdata);
        JSONObject resultJson = rpcCommonService.rpcXxPayWxpayApiService.getWxpayFaceAuthInfo(mchId, storeId, storeName, deviceId, rawdata);
        if(resultJson == null) {
            _log.error("调用微信获取刷脸调用凭证失败");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJson));
    }

}