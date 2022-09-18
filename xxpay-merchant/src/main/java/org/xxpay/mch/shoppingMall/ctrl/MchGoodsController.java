package org.xxpay.mch.shoppingMall.ctrl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户商品表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mchGoods")
public class MchGoodsController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 商品列表
     */
    @MethodLog( remark = "商品列表" )
    @RequestMapping(value = "/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long outGoodsId = getValLong("outGoodsId");
        MchGoods mchGoods = getObject( MchGoods.class);

        //当前登录商户ID
        mchGoods.setMchId(getUser().getBelongInfoId());
        IPage<MchGoods> mchGoodsList = rpcCommonService.rpcMchGoodsService.list(mchGoods, outGoodsId, getIPage(), null, null, null, null, null, null);
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsList));
    }


    /**
     * 商品详情
     */
    @MethodLog( remark = "商品详情" )
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long goodsId = getValLongRequired("goodsId");

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getOne(
                new QueryWrapper<MchGoods>().lambda()
                        .eq(MchGoods::getMchId, getUser().getBelongInfoId())
                        .eq(MchGoods::getGoodsId, goodsId)
        );
        if (mchGoods == null || !mchGoods.getMchId().equals(getUser().getBelongInfoId())) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));

        JSONObject object = (JSONObject) JSON.toJSON(mchGoods);

        //获取限制店铺ID和名称
        List storeIds = new LinkedList();
        List storeNames = new LinkedList();
        if (mchGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            List<Map> list = rpcCommonService.rpcMchGoodsService.storesList(goodsId);
            for (Map map : list){
                storeIds.add(map.get("storeId"));
                storeNames.add(map.get("storeName"));
            }
            object.put("storeIds", storeIds);
            object.put("storeNames", storeNames);
        }

        //获取属性分类ID
        List goodsPropsIds = new LinkedList();
        List goodsPropsNames = new LinkedList();
        if (mchGoods.getGoodsPropsType() == MchConstant.MCH_GOODS_PROPS_TYPE_YES){
            List<Map> list = rpcCommonService.rpcMchGoodsService.propsCategoryList(goodsId);
            for (Map map : list){
                goodsPropsIds.add(map.get("propsCategoryId"));
                goodsPropsNames.add(map.get("propsCategoryName"));
            }
            object.put("goodsPropsIds", goodsPropsIds);
            object.put("goodsPropsNames", goodsPropsNames);
        }

        // 获取商品图片
        LinkedList imgList = new LinkedList();

        imgList.add(0, mchGoods.getImgPathMain());

        String imgPathMore = mchGoods.getImgPathMore();
        if (StringUtils.isNotBlank(imgPathMore)){
            String[] split = imgPathMore.split(",");
            for (int i = 0; i < split.length; i++) {
                imgList.add(split[i]);
            }
        }
        object.put("imgList", imgList);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }


    /**
     * 添加商品
     */
    @MethodLog( remark = "添加商品" )
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        String storeIds = getValString( "storeIds");
        String propsCategoryIds = getValString( "propsCategoryIds");
        MchGoods mchGoods = getObject( MchGoods.class);

        //设置商户 服务商ID
        mchGoods.setMchId(getUser().getBelongInfoId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        mchGoods.setIsvId(mchInfo.getIsvId());

        //设置商品分类名称
        if (mchGoods.getCategoryId() == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_SELECT_GOODS_CATEGORY));
        MchGoodsCategory mchGoodsCategory = rpcCommonService.rpcMchGoodsCategoryService.getById(mchGoods.getCategoryId());
        mchGoods.setCategoryName(mchGoodsCategory.getCategoryName());
        mchGoods.setIndustryType(mchGoodsCategory.getAuthFrom());
        //设置售价 进价 会员价
        mchGoods.setCellingPrice(getValLongRequired("cellingPrice"));
        mchGoods.setBuyingPrice(getValLong("buyingPrice"));
        mchGoods.setMemberPrice(getValLong("memberPrice"));

        //实际销量  浏览量  评价数设置初始值  状态
        mchGoods.setActualSaleNum(0L);
        mchGoods.setBrowseNumber(0);
        mchGoods.setEvaluationNumber(0);
        mchGoods.setStatus(MchConstant.PUB_YES);

        boolean saveGoodsResult = rpcCommonService.rpcMchGoodsService.saveMchGoods(mchGoods, storeIds, propsCategoryIds);
        if (!saveGoodsResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 修改商品
     */
    @MethodLog( remark = "修改商品" )
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        String storeIds = getValString( "storeIds");
        String propsCategoryIds = getValString( "propsCategoryIds");
        MchGoods mchGoods = getObject( MchGoods.class);

        MchGoods dbGoods = rpcCommonService.rpcMchGoodsService.getById(mchGoods.getGoodsId());
        if (dbGoods == null || !dbGoods.getMchId().equals(getUser().getBelongInfoId())) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));

        //设置商品分类名称
        if (mchGoods.getCategoryId() == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_SELECT_GOODS_CATEGORY));
        MchGoodsCategory mchGoodsCategory = rpcCommonService.rpcMchGoodsCategoryService.getById(mchGoods.getCategoryId());
        mchGoods.setCategoryName(mchGoodsCategory.getCategoryName());
        mchGoods.setIndustryType(mchGoodsCategory.getAuthFrom());

        //设置售价 进价 会员价
        mchGoods.setCellingPrice(getValLongRequired("cellingPrice"));
        mchGoods.setBuyingPrice(getValLong("buyingPrice"));
        mchGoods.setMemberPrice(getValLong("memberPrice"));

        boolean result = rpcCommonService.rpcMchGoodsService.updateMchGoods(mchGoods, storeIds, propsCategoryIds);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除商品
     */
    @MethodLog( remark = "删除商品" )
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long goodsId = getValLongRequired("goodsId");
        boolean result = rpcCommonService.rpcMchGoodsService.remove(new QueryWrapper<MchGoods>().lambda()
                .eq(MchGoods::getGoodsId, goodsId)
                .eq(MchGoods::getMchId, getUser().getBelongInfoId())
        );

        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 批量设置精品推荐商品
     */
    @MethodLog( remark = "批量设置商品为精品推荐" )
    @RequestMapping("/recommend")
    @ResponseBody
    public ResponseEntity<?> recommend() {

        String[] goodsIdArr = getValStringRequired("goodsId").split(",");
        Integer recommendSort = getValInteger("recommendSort");
        Byte isRecommend = getValByte("isRecommend");
        List idList = Arrays.asList(goodsIdArr);

        LambdaUpdateWrapper<MchGoods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(MchGoods::getGoodsId, idList);
        updateWrapper.eq(MchGoods::getMchId, getUser().getBelongInfoId());
        updateWrapper.set(MchGoods::getIsRecommend, MchConstant.PUB_YES);
        if (recommendSort != null) updateWrapper.set(MchGoods::getRecommendSort, recommendSort);
        if (isRecommend != null) {
            updateWrapper.set(MchGoods::getIsRecommend, isRecommend);
            if (isRecommend == 0) {
                updateWrapper.set(MchGoods::getRecommendSort, MchConstant.PUB_NO);
            }
        }

        boolean result = rpcCommonService.rpcMchGoodsService.update(updateWrapper);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

}
