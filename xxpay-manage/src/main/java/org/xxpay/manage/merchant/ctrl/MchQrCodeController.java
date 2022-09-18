package org.xxpay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchQrCode;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_qrcode")
public class MchQrCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增二维码" )
    public ResponseEntity<?> add() {

        MchQrCode mchQrCode = getObject( MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.add(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchQrCode mchQrCode = getObject( MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.count(mchQrCode);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchQrCode> mchQrCodeList = rpcCommonService.rpcMchQrCodeService.select((getPageIndex() -1) * getPageSize(), getPageSize(), mchQrCode);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchQrCodeList, count));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/view_code")
    @ResponseBody
    public ResponseEntity<?> viewCode() {

        Long id = getValLongRequired( "id");
        MchQrCode mchQrCode = rpcCommonService.rpcMchQrCodeService.findById(id);
        if(mchQrCode == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_QR_CODE_STOP));
        }
        if(MchConstant.PUB_YES != mchQrCode.getStatus().byteValue()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        StringBuilder sb = new StringBuilder(mainConfig.getPayUrl() + "/qrcode/toQRCodePage?");
        sb.append("mchId=").append(mchQrCode.getMchId())
                .append("&qrcodeId=").append(mchQrCode.getId());

        JSONObject data = new JSONObject();
        data.put("codeUrl", sb.toString());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        MchQrCode mchQrCode = rpcCommonService.rpcMchQrCodeService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchQrCode));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改二维码" )
    public ResponseEntity<?> update() {

        handleParamAmount( "minAmount", "maxAmount");
        MchQrCode mchQrCode = getObject( MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.update(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
