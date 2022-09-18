package org.xxpay.mch.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:商户中心 设置当前商户（私有账户）的通道配置信息ctrl
 * @Author terrfly
 * @Date 2018/11/21 16:10
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/mch_pay_config")
@PreAuthorize("hasRole('" + MchConstant.MCH_ROLE_NORMAL + "')")
public class MchPayConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));

        Long mchId = getUser().getBelongInfoId();

        //左连接 查询
        List<FeeScale> record = rpcCommonService.rpcFeeScaleService.productJoinFeeScale(MchConstant.INFO_TYPE_MCH, mchId, "leftJoin", false);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(record));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        Integer productId = getValIntegerRequired( "productId");
        Long mchId = getUser().getBelongInfoId();

        //校验信息合法性
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));

        //获取 数据载体对象
        FeeScale mchPayPassage = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchId, productId);
        if(mchPayPassage == null) {
            mchPayPassage = new FeeScale();
        }

        mchPayPassage.setPsVal("productName", payProduct.getProductName());
        mchPayPassage.setPsVal("productType", payProduct.getProductType());

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchPayPassage));
    }


    /**
     * 新增 / 更新  商户支付通道配置
     * @return
     */
    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "私有商户配置支付通道" )
    public ResponseEntity<?> addOrUpd() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoId(getUser().getBelongInfoId());
        feeScale.setInfoType(MchConstant.INFO_TYPE_MCH);  //商户类型
        feeScale.setProductType(MchConstant.FEE_SCALE_PTYPE_PAY); //支付产品收费


        //获取私有账户的全局配置，赋值到对应的收费产品中。
        FeeScale templet = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(getUser().getBelongInfoId(), 0);
        if(templet == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_FEE_TEMPLET_NOT_EXISTS));

        feeScale.setFeeScale(templet.getFeeScale());
        feeScale.setFeeScaleStep(templet.getFeeScaleStep());
        feeScale.setSingleFeeType(templet.getSingleFeeType());
        feeScale.setFee(templet.getFee());

        int count;
        if(feeScale.getId() != null) {
            count = rpcCommonService.rpcFeeScaleService.update(feeScale);
        }else {
            count = rpcCommonService.rpcFeeScaleService.add(feeScale);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }



    /**
     * 查询 商户 /代理商 的所有可配置支付通道
     * @return
     */
    @RequestMapping("/can_set_passage_list")
    @ResponseBody
    public ResponseEntity<?> canSetPassageList() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));

        Long mchId = getUser().getBelongInfoId();


        Integer payProductId = getValIntegerRequired( "payProductId");

        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(payProductId);
        if(payProduct == null) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));

        //查询该平台的全部通道信息
        List<PayPassage> result = rpcCommonService.rpcPayPassageService.selectMchAllByPayType(payProduct.getPayType(), mchId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }

}
