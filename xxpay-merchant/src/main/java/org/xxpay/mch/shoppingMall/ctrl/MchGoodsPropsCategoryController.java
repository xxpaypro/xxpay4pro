package org.xxpay.mch.shoppingMall.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.entity.MchGoodsPropsCategory;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.List;

/**
 * <p>
 * 商户商品属性分类表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchGoodsProps/category")
public class MchGoodsPropsCategoryController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商品属性分类列表
     */
    @MethodLog( remark = "商品属性分类列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long propsCategoryId = getValLong("propsCategoryId");
        String propsCategoryName = getValString("propsCategoryName");

        LambdaQueryWrapper<MchGoodsPropsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchGoodsPropsCategory::getMchId, getUser().getBelongInfoId());
        if (propsCategoryId != null)queryWrapper.eq(MchGoodsPropsCategory::getPropsCategoryId, propsCategoryId);
        if (StringUtils.isNotBlank(propsCategoryName)) queryWrapper.like(MchGoodsPropsCategory::getPropsCategoryName, "%" + propsCategoryName + "%");

        IPage<MchGoodsPropsCategory> mchGoodsPropsCategoryList = rpcCommonService.rpcMchGoodsPropsCategoryService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsPropsCategoryList));
    }

    /**
     * 新增分类
     */
    @MethodLog( remark = "新增分类" )
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchGoodsPropsCategory mchGoodsPropsCategory = getObject( MchGoodsPropsCategory.class);
        mchGoodsPropsCategory.setMchId(getUser().getBelongInfoId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        mchGoodsPropsCategory.setIsvId(mchInfo.getIsvId());

        boolean result = rpcCommonService.rpcMchGoodsPropsCategoryService.save(mchGoodsPropsCategory);
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

        Long propsCategoryId = getValLongRequired("propsCategoryId");

        MchGoodsPropsCategory mchGoodsPropsCategory = rpcCommonService.rpcMchGoodsPropsCategoryService.getOne(
                new QueryWrapper<MchGoodsPropsCategory>().lambda()
                        .eq(MchGoodsPropsCategory::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsPropsCategory::getPropsCategoryId, propsCategoryId)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchGoodsPropsCategory));
    }

    /**
     * 修改分类
     */
    @MethodLog( remark = "修改分类" )
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchGoodsPropsCategory mchGoodsPropsCategory = getObject(MchGoodsPropsCategory.class);

        MchGoodsPropsCategory dbGoodsCategory = rpcCommonService.rpcMchGoodsPropsCategoryService.getById(mchGoodsPropsCategory.getPropsCategoryId());
        //当前操作人ID与分类所属商户ID是否相同
        if (!getUser().getBelongInfoId().equals(dbGoodsCategory.getMchId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        boolean result = rpcCommonService.rpcMchGoodsPropsCategoryService.updateById(mchGoodsPropsCategory);
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

        Long propsCategoryId = getValLongRequired("propsCategoryId");

        boolean result = rpcCommonService.rpcMchGoodsPropsCategoryService.remove(
                new QueryWrapper<MchGoodsPropsCategory>().lambda()
                        .eq(MchGoodsPropsCategory::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoodsPropsCategory::getPropsCategoryId, propsCategoryId)
        );

        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


}
