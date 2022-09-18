package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.MchQrCode;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_qrcode")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchQrCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增商户二维码" )
    public ResponseEntity<?> add() {
        JSONObject param = getReqParamJSON();
        handleParamAmount( "payAmount");
        MchQrCode mchQrCode = getObject( MchQrCode.class);
        mchQrCode.setMchId(getUser().getBelongInfoId());
        mchQrCode.setCreateOperatorId(getUser().getUserId() + "");

        if(rpcCommonService.rpcMchQrCodeService.save(mchQrCode)){
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        JSONObject param = getReqParamJSON();
        Long payAmountL = getAmountL("payAmount");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getBelongInfoId());
        mchQrCode.setPayAmount(payAmountL);
        int count = rpcCommonService.rpcMchQrCodeService.count(mchQrCode);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchQrCode> mchQrCodeList = rpcCommonService.rpcMchQrCodeService.select((getPageIndex() -1) * getPageSize(), getPageSize(), mchQrCode);
        LinkedList<Object> list = new LinkedList<>();
        for (MchQrCode code: mchQrCodeList) {
            JSONObject json = JsonUtil.getJSONObjectFromObj(code);
            //拼接URL
            StringBuilder sb = new StringBuilder(mainConfig.getPayUrl() + "/qrcode/toQRCodePage?");
            sb.append("mchId=").append(getUser().getBelongInfoId())
                    .append("&storeId=").append(code.getStoreId() != null ? code.getStoreId() : "")
                    .append("&operatorId=").append(code.getOperatorId() != null ? code.getOperatorId() : "")
                    .append("&payAmount=").append(code.getPayAmount() != null ? code.getPayAmount() : "");

            String codeUrl = sb.toString();
            String url = mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(codeUrl) + "&widht=200&height=200";
            json.put("url", url);
            list.add(json);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(list, count));
    }

    /**
     * 查询二维码地址 及 图片
     * @return
     */
    @RequestMapping("/view_code")
    @ResponseBody
    public ResponseEntity<?> viewCode() {
        JSONObject param = getReqParamJSON();
        Long id = getValLongRequired( "id");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getBelongInfoId());
        mchQrCode.setId(id);
        mchQrCode = rpcCommonService.rpcMchQrCodeService.find(mchQrCode);
        if(mchQrCode == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_QR_CODE_STOP));
        }
        if(MchConstant.PUB_YES != mchQrCode.getStatus()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        //拼接URL
        StringBuilder sb = new StringBuilder(mainConfig.getPayUrl() + "/qrcode/toQRCodePage?");
        sb.append("mchId=").append(mchQrCode.getMchId())
                .append("&storeId=").append(mchQrCode.getStoreId() != null ? mchQrCode.getStoreId() : "")
                .append("&operatorId=").append(mchQrCode.getOperatorId() != null ? mchQrCode.getOperatorId() : "")
                .append("&payAmount=").append((mchQrCode.getPayAmount() != null && mchQrCode.getPayAmount() != 0) ? mchQrCode.getPayAmount() : "");

        String codeUrl = sb.toString();

        JSONObject data = new JSONObject();
        data.put("codeUrl", codeUrl);
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(codeUrl) + "&widht=200&height=200");
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }


    /** 根据金额动态生成二维码图片 **/
    @RequestMapping("/view_code_amount")
    @ResponseBody
    public ResponseEntity<?> viewCodeByAmount() {
        JSONObject param = getReqParamJSON();
        Long payAmount = getRequiredAmountL( "payAmount");

        Long mchId = getUser().getBelongInfoId(); //mchId
        Long storeId = getUser().getStoreId();

        String tradeOrderId = MySeq.getTrade();  //可通过该ID进行查单操作

        //拼接URL
        StringBuilder sb = new StringBuilder(mainConfig.getPayUrl() + "/qrcode/toQRCodePage?");
                sb.append("mchId=").append(mchId)
                .append("&storeId=").append(storeId != null ? storeId : "")
                .append("&operatorId=").append(getUser().getIsSuperAdmin() == MchConstant.PUB_YES ? "" : getUser().getUserId())
                .append("&payAmount=").append(payAmount)
                .append("&tradeOrderId=").append(tradeOrderId);

        String codeUrl = sb.toString();

        JSONObject data = new JSONObject();
        data.put("tradeOrderId", tradeOrderId);
        data.put("codeUrl",codeUrl);
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(codeUrl) + "&widht=200&height=200");
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 查询 单个二维码信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        JSONObject param = getReqParamJSON();
        Long id = getValLongRequired( "id");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getBelongInfoId());
        mchQrCode.setId(id);
        mchQrCode = rpcCommonService.rpcMchQrCodeService.find(mchQrCode);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchQrCode));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户二维码" )
    public ResponseEntity<?> update() {
        JSONObject param = getReqParamJSON();
        handleParamAmount( "payAmount");
        MchQrCode mchQrCode = getObject( MchQrCode.class);

        boolean isTrue = rpcCommonService.rpcMchQrCodeService.update(mchQrCode,
                new QueryWrapper<MchQrCode>().lambda()
                        .eq(MchQrCode::getId, mchQrCode.getId())
                        .eq(MchQrCode::getMchId, getUser().getBelongInfoId()
        ));
        if(isTrue) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
