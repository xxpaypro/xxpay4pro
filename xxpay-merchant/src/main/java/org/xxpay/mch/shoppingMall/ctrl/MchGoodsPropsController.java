package org.xxpay.mch.shoppingMall.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoodsProps;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

/**
 * <p>
 * 商品属性表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchGoodsProps")
public class MchGoodsPropsController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商品属性列表
     */
    @MethodLog( remark = "商品属性列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long propsCategoryId = getValLongRequired("propsCategoryId");

        LambdaQueryWrapper<MchGoodsProps> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchGoodsProps::getMchId, getUser().getBelongInfoId());
        queryWrapper.eq(MchGoodsProps::getPropsCategoryId, propsCategoryId);

        IPage<MchGoodsProps> mchGoodsPropsList = rpcCommonService.rpcMchGoodsPropsService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsPropsList));
    }

    /**
     * 新增属性
     */
    @MethodLog( remark = "新增属性" )
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchGoodsProps mchGoodsProps = getObject( MchGoodsProps.class);
        //查找该分类下是否存在相同值
        int count = rpcCommonService.rpcMchGoodsPropsService.count(new LambdaQueryWrapper<MchGoodsProps>()
                .eq(MchGoodsProps::getPropsValue, mchGoodsProps.getPropsValue())
                .eq(MchGoodsProps::getPropsCategoryId, mchGoodsProps.getPropsCategoryId())
                .eq(MchGoodsProps::getMchId, getUser().getBelongInfoId()));
        if (count > 0){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_PROPS_VALUE_DUPLICATE));
        }

        mchGoodsProps.setMchId(getUser().getBelongInfoId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        mchGoodsProps.setIsvId(mchInfo.getIsvId());

        boolean result = rpcCommonService.rpcMchGoodsPropsService.save(mchGoodsProps);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 属性详情
     */
    @MethodLog( remark = "属性详情" )
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long propsId = getValLongRequired("propsId");

        MchGoodsProps mchGoodsProps = rpcCommonService.rpcMchGoodsPropsService.getOne(
                new QueryWrapper<MchGoodsProps>().lambda()
                        .eq(MchGoodsProps::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsProps::getPropsId, propsId)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchGoodsProps));
    }

    /**
     * 修改属性
     */
    @MethodLog( remark = "修改属性" )
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchGoodsProps mchGoodsProps = getObject(MchGoodsProps.class);

        MchGoodsProps dbGoodsCategory = rpcCommonService.rpcMchGoodsPropsService.getById(mchGoodsProps.getPropsId());
        //当前操作人ID与分类所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(dbGoodsCategory.getMchId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        if (dbGoodsCategory.getPropsCategoryId().equals(mchGoodsProps.getPropsCategoryId())
                && dbGoodsCategory.getPropsValue().equals(mchGoodsProps.getPropsValue())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_PROPS_VALUE_DUPLICATE));
        }

        boolean result = rpcCommonService.rpcMchGoodsPropsService.updateById(mchGoodsProps);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除属性
     */
    @MethodLog( remark = "删除属性" )
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long propsId = getValLongRequired("propsId");

        boolean result = rpcCommonService.rpcMchGoodsPropsService.remove(
                new QueryWrapper<MchGoodsProps>().lambda()
                        .eq(MchGoodsProps::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsProps::getPropsId, propsId)
        );

        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

}
