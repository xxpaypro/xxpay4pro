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
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoodsCategory;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchGoods/category")
public class MchGoodsCategoryController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商品分类列表
     */
    @MethodLog( remark = "商品分类列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchGoodsCategory mchGoodsCategory = getObject( MchGoodsCategory.class);
        //当前登录商户ID
        mchGoodsCategory.setMchId(getUser().getBelongInfoId());
        IPage<MchGoodsCategory> mchGoodsCategoryList = rpcCommonService.rpcMchGoodsCategoryService.list(mchGoodsCategory, getIPage(true));
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsCategoryList));
    }

    /**
     * 一级分类列表
     */
    @MethodLog( remark = "一级分类列表" )
    @RequestMapping("/parent_list")
    @ResponseBody
    public ResponseEntity<?> parentList() {

        LambdaQueryWrapper<MchGoodsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchGoodsCategory::getParentCategoryId, MchConstant.PUB_NO);
        queryWrapper.eq(MchGoodsCategory::getMchId, getUser().getBelongInfoId());
        List<MchGoodsCategory> mchGoodsCategoryList = rpcCommonService.rpcMchGoodsCategoryService.list(queryWrapper);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchGoodsCategoryList));
    }

    /**
     * 新增分类
     */
    @MethodLog( remark = "新增分类" )
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchGoodsCategory mchGoodsCategory = getObject( MchGoodsCategory.class);
        mchGoodsCategory.setMchId(getUser().getBelongInfoId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        mchGoodsCategory.setIsvId(mchInfo.getIsvId());

        boolean result = rpcCommonService.rpcMchGoodsCategoryService.save(mchGoodsCategory);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 分类详情
     */
    @MethodLog( remark = "分类详情" )
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long categoryId = getValLongRequired("categoryId");

        MchGoodsCategory mchGoodsCategory = rpcCommonService.rpcMchGoodsCategoryService.getOne(
                new QueryWrapper<MchGoodsCategory>().lambda()
                        .eq(MchGoodsCategory::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsCategory::getCategoryId, categoryId)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchGoodsCategory));
    }

    /**
     * 修改分类
     */
    @MethodLog( remark = "修改分类" )
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchGoodsCategory mchGoodsCategory = getObject(MchGoodsCategory.class);

        MchGoodsCategory dbGoodsCategory = rpcCommonService.rpcMchGoodsCategoryService.getById(mchGoodsCategory.getCategoryId());
        //当前操作人ID与分类所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(dbGoodsCategory.getMchId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        boolean result = rpcCommonService.rpcMchGoodsCategoryService.updateById(mchGoodsCategory);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除分类
     */
    @MethodLog( remark = "删除分类" )
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long categoryId = getValLongRequired("categoryId");

        boolean result = rpcCommonService.rpcMchGoodsCategoryService.remove(
                new QueryWrapper<MchGoodsCategory>().lambda()
                        .eq(MchGoodsCategory::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsCategory::getCategoryId, categoryId)
        );

        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


}
