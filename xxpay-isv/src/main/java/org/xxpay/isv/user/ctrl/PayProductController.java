package org.xxpay.isv.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/pay_product")
@PreAuthorize("hasRole('"+ MchConstant.ISV_ROLE_NORMAL+"')")
public class PayProductController extends BaseController {

    private final static MyLog _log = MyLog.getLog(PayProductController.class);

    @Autowired
    private RpcCommonService rpcCommonService;


    /** 查询所有支付产品类型， 每个服务商拥有查看所有产品的权限 **/
    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        Long currentIsvId = getUser().getBelongInfoId(); //当前isvId


        //根据isvId查询, 当前配置的所有产品信息；
        List<FeeScale> isvFeeScaleList = rpcCommonService.rpcFeeScaleService.list(
                new QueryWrapper<FeeScale>().lambda()
                        .eq(FeeScale::getInfoType, MchConstant.INFO_TYPE_ISV)
                        .eq(FeeScale::getInfoId, currentIsvId)
        );

        //按照productId 转为Map类型
        Map<Integer, FeeScale> feeScaleMap = isvFeeScaleList.stream().collect(Collectors.toMap(FeeScale::getRefProductId, Function.identity()));

        IPage page = getIPage();
        IPage<PayProduct> list = rpcCommonService.rpcPayProductService.page(page, null);
        list.getRecords().stream().forEach(product -> product.setPsVal("feeScale", feeScaleMap.get(product.getId())));
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /** 查询通道配置信息 **/
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long currentIsvId = getUser().getBelongInfoId(); //当前服务商ID

        Integer productId = getValIntegerRequired( "productId");

        //查询该产品信息
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);

        //查询该服务商的配置信息
        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByIsv(currentIsvId, productId);
        payProduct.setPsVal("feeScale", feeScale);

        return ResponseEntity.ok(PageRes.buildSuccess(payProduct));
    }


    /** 更新通道配置信息 **/
    @RequestMapping("/saveOrUpdate")
    public ResponseEntity<?> saveOrUpdate() {

        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoType(MchConstant.INFO_TYPE_ISV);
        feeScale.setInfoId(this.getUser().getBelongInfoId());
        feeScale.setProductType(MchConstant.FEE_SCALE_PTYPE_PAY);  //收费产品类型： 支付
        feeScale.setFeeScale(MchConstant.FEE_SCALE_SINGLE); //每笔收费
        feeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE); //每笔单笔收费
        feeScale.setStatus(MchConstant.PUB_YES); //默认开启
        feeScale.setIsDefault(MchConstant.PUB_YES); //默认

        if(rpcCommonService.rpcFeeScaleService.saveOrUpdate(feeScale)){
            return ResponseEntity.ok(PageRes.buildSuccess());
        }

        throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }

}