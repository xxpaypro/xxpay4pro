package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/mch_pay_product")
@PreAuthorize("hasRole('"+ MchConstant.ISV_ROLE_NORMAL+"')")
public class MchPayProductController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchPayProductController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    public ResponseEntity<?> list() {
        //当前商户id
        Integer mchId = getValIntegerRequired( "mchId");
        Long currentIsvId = getUser().getBelongInfoId(); //当前isvId
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.list();
        List<FeeScale> mchFeeList = rpcCommonService.rpcFeeScaleService.list(new QueryWrapper<FeeScale>().lambda().eq(FeeScale::getInfoId, mchId));

        Map<Integer, PayProduct> productMap = payProductList.stream().collect(Collectors.toMap(PayProduct::getId, Function.identity()));

        IPage page = getIPage();
        LambdaQueryWrapper<FeeScale> lambda = new QueryWrapper<FeeScale>().lambda()
        .eq(FeeScale::getInfoId, currentIsvId)
        .eq(FeeScale::getInfoType, MchConstant.INFO_TYPE_ISV);

        IPage<FeeScale> list = rpcCommonService.rpcFeeScaleService.page(page, lambda);
        list.getRecords().stream().forEach(feeScale ->
        {
            feeScale.setPsVal("product", productMap.get(feeScale.getRefProductId()));
            //如果产品id相同将下级商户的信息存入
            for (FeeScale fee: mchFeeList) {
                if (feeScale.getRefProductId().equals(fee.getRefProductId())) {
                    feeScale.setPsVal("mchFee", fee);
                }
            }
        });

        return ResponseEntity.ok(PageRes.buildSuccess(list));

    }

    /** 查询通道配置信息 **/
    @RequestMapping("/get")
    public ResponseEntity<?> get() {


        Integer productId = getValIntegerRequired( "productId");
        Long mchId = getValLongRequired( "mchId");

        //查询该产品信息
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);

        //查询该服务商下级商户的配置信息
        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchId, productId);
        payProduct.setPsVal("feeScale", feeScale);

        return ResponseEntity.ok(PageRes.buildSuccess(payProduct));
    }

    /** 更新商户产品配置信息 **/
    @RequestMapping("/update")
    public ResponseEntity<?> update() {
        FeeScale feeScale = getObject( FeeScale.class);
        //获取商户
        Long currentIsvId = getUser().getBelongInfoId();
        //查询服务商配置信息
        FeeScale isvFeeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByIsv(currentIsvId, feeScale.getRefProductId());
        if (isvFeeScale == null) {
            ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        }
        //判断当前修改费率是否高于服务商费率
        Boolean result = false;
        if (isvFeeScale.getFee().compareTo(feeScale.getFee()) < 1) {
            result = rpcCommonService.rpcFeeScaleService.updateMchFeeScale(feeScale, isvFeeScale);
        }else {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_ISV_FEE_ERROR));
        }
        if (!result) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(PageRes.buildSuccess());
    }

}
