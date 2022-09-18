package org.xxpay.mbr.ordering.ctrl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchGoodsCategory;
import org.xxpay.core.entity.MchGoodsProps;
import org.xxpay.core.entity.MchGoodsPropsCategoryRela;
import org.xxpay.mbr.common.ctrl.BaseController;

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
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchGoods")
public class MchGoodsController extends BaseController {

    /**
     * 商户商品列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Byte sortColumn = getValByte("sortColumn");
        Byte sortType = getValByte("sortType");
        Long floorPrice = getValLong("floorPrice");
        Long ceilPrice = getValLong("ceilPrice");
        String createTimeStart = getValString("createTimeStart");
        String createTimeEnd = getValString("createTimeEnd");

        MchGoods mchGoods = getObject(MchGoods.class);
        mchGoods.setMchId(getUser().getMchId());
        mchGoods.setStatus(MchConstant.PUB_YES);
        IPage<MchGoods> mchGoodsList = rpcCommonService.rpcMchGoodsService.list(mchGoods, null, getIPage(), sortColumn, sortType, floorPrice, ceilPrice, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsList));
    }

    /**
     * 精品推荐商品列表
     */
    @RequestMapping("/recommend_list")
    @ResponseBody
    public ResponseEntity<?> recommendList() {

        LambdaQueryWrapper<MchGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchGoods::getMchId, getUser().getMchId());
        queryWrapper.eq(MchGoods::getIsRecommend, MchConstant.PUB_YES);
        queryWrapper.orderByAsc(MchGoods::getRecommendSort);
        queryWrapper.orderByDesc(MchGoods::getCreateTime);

        IPage<MchGoods> mchGoodsList = rpcCommonService.rpcMchGoodsService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(mchGoodsList));
    }

    /**
     * 商品详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long goodsId = getValLongRequired("goodsId");
        Long mchId = getValLongRequired("mchId");

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getOne(
                new QueryWrapper<MchGoods>().lambda()
                .eq(MchGoods::getMchId, mchId)
                .eq(MchGoods::getGoodsId, goodsId)
        );

        if (mchGoods == null || !mchGoods.getMchId().equals(getUser().getMchId())) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));

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

        //获取属性
        JSONArray propsArray = new JSONArray();

        if (mchGoods.getGoodsPropsType() == MchConstant.MCH_GOODS_PROPS_TYPE_YES){

            //查询商品关联的属性分类
            List<MchGoodsPropsCategoryRela> gpcRelaList = rpcCommonService.rpcMchGoodsPropsCategoryRelaService.list(new QueryWrapper<MchGoodsPropsCategoryRela>().lambda()
                .eq(MchGoodsPropsCategoryRela::getGoodsId, goodsId)
            );

            //查询商品分类属性下的属性列表
            for(MchGoodsPropsCategoryRela gpcRela : gpcRelaList) {
                JSONObject propsCategory = new JSONObject();

                LambdaQueryWrapper<MchGoodsProps> goodsPropsLambda = new QueryWrapper<MchGoodsProps>().lambda();
                goodsPropsLambda.eq(MchGoodsProps::getPropsCategoryId, gpcRela.getPropsCategoryId());
                List<MchGoodsProps> goodsProps = rpcCommonService.rpcMchGoodsPropsService.list(goodsPropsLambda);
                propsCategory.put("propsCategoryId", gpcRela.getPropsCategoryId());
                propsCategory.put("propsCategoryName", goodsProps.get(0).getPropsName());
                propsCategory.put("props", goodsProps);

                propsArray.add(propsCategory);
            }
        }

        object.put("goodsProps", propsArray);

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
     * 商品相关属性列表
     */
    @RequestMapping("/getGoodsProps")
    @ResponseBody
    public ResponseEntity<?> getGoodsProps() {

        Long goodsId = getValLongRequired("goodsId");

        //获取属性
        JSONArray propsArray = new JSONArray();

        //查询商品关联的属性分类
        List<MchGoodsPropsCategoryRela> gpcRelaList = rpcCommonService.rpcMchGoodsPropsCategoryRelaService.list(new QueryWrapper<MchGoodsPropsCategoryRela>().lambda()
                .eq(MchGoodsPropsCategoryRela::getGoodsId, goodsId)
        );

        //查询商品分类属性下的属性列表
        for(MchGoodsPropsCategoryRela gpcRela : gpcRelaList) {
            JSONObject propsCategory = new JSONObject();

            LambdaQueryWrapper<MchGoodsProps> goodsPropsLambda = new QueryWrapper<MchGoodsProps>().lambda();
            goodsPropsLambda.eq(MchGoodsProps::getPropsCategoryId, gpcRela.getPropsCategoryId());
            List<MchGoodsProps> goodsProps = rpcCommonService.rpcMchGoodsPropsService.list(goodsPropsLambda);
            propsCategory.put("propsCategoryId", gpcRela.getPropsCategoryId());
            propsCategory.put("propsCategoryName", goodsProps.get(0).getPropsName());
            propsCategory.put("props", goodsProps);

            propsArray.add(propsCategory);
        }

        return ResponseEntity.ok(PageRes.buildSuccess(propsArray));
    }

}
