package org.xxpay.mbr.ordering.ctrl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchShoppingCart;
import org.xxpay.mbr.common.ctrl.BaseController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/mchShoppingCart")
public class MchShoppingCartController extends BaseController {

    /**
     * 购物车列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Byte industryType = getValByteRequired("industryType");
        Long memberId = getUser().getMemberId();
        Long mchId = getUser().getMchId();

        int count = rpcCommonService.rpcMchShoppingCartService.count(industryType, memberId, mchId);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Map> map = rpcCommonService.rpcMchShoppingCartService.selectCartList((getPageIndex() -1) * getPageSize(), getPageSize(true), industryType, memberId, mchId);



        return ResponseEntity.ok(XxPayPageRes.buildSuccess(map, count));
    }

    /**
     * 加购物车
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchShoppingCart mchShoppingCart = getObject(MchShoppingCart.class);

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getById(getValLongRequired("goodsId"));
        if (mchGoods == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_NO_SUCH_GOODS));
        }

        //设置商户ID、服务商ID和会员ID
        mchShoppingCart.setMemberId(getUser().getMemberId());
        mchShoppingCart.setMchId(getUser().getMchId());
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getMchId());
        mchShoppingCart.setIsvId(mchInfo.getIsvId());

        //查询商品是否已存在
        LambdaQueryWrapper<MchShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchShoppingCart::getGoodsId, mchShoppingCart.getGoodsId());
        queryWrapper.eq(MchShoppingCart::getMemberId, mchShoppingCart.getMemberId());
        queryWrapper.eq(MchShoppingCart::getMchId, mchShoppingCart.getMchId());
        queryWrapper.eq(MchShoppingCart::getGoodsProps, mchShoppingCart.getGoodsProps());
        MchShoppingCart dbShoppingCart = rpcCommonService.rpcMchShoppingCartService.getOne(queryWrapper);

        if(dbShoppingCart == null) {
            //判断库存
            if (mchGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES && mchShoppingCart.getGoodsNum() > mchGoods.getStockNum()) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GOODS_STOCK_NUM_NOT_ENOUGH));
            }

            boolean saveResult = rpcCommonService.rpcMchShoppingCartService.save(mchShoppingCart);
            if(!saveResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }else {
            //记录存在，增加购买数量
            int goodsNum = dbShoppingCart.getGoodsNum() + mchShoppingCart.getGoodsNum();
            //判断库存
            if (mchGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES && goodsNum > mchGoods.getStockNum()) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GOODS_STOCK_NUM_NOT_ENOUGH));
            }

            MchShoppingCart updateRecord = new MchShoppingCart();
            updateRecord.setCartId(dbShoppingCart.getCartId());
            updateRecord.setGoodsNum(goodsNum);
            boolean updateResult = rpcCommonService.rpcMchShoppingCartService.updateById(updateRecord);
            if(!updateResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        return ResponseEntity.ok(PageRes.buildSuccess());
    }

    /**
     * 修改购物车商品数量
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        Byte changeType = getValByteRequired("changeType");
        MchShoppingCart mchShoppingCart = getObject(MchShoppingCart.class);

        MchShoppingCart dbShoppingCart = rpcCommonService.rpcMchShoppingCartService.getById(mchShoppingCart.getCartId());
        if (dbShoppingCart == null){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        MchGoods mchGoods = rpcCommonService.rpcMchGoodsService.getById(dbShoppingCart.getGoodsId());

        int dbNum = dbShoppingCart.getGoodsNum();

        if (changeType == 1) {//增加
            dbNum += 1;
        }else if (changeType == 2) {//减少
            dbNum = dbNum > 0 ? dbNum - 1 : dbNum;
        }

        boolean result;
        if (dbNum > 0){
            //判断库存
            if (mchGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES && dbNum > mchGoods.getStockNum()) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_GOODS_STOCK_NUM_NOT_ENOUGH));
            }

            MchShoppingCart updateRecord = new MchShoppingCart();
            updateRecord.setCartId(mchShoppingCart.getCartId());
            updateRecord.setGoodsNum(dbNum);
            result = rpcCommonService.rpcMchShoppingCartService.updateById(updateRecord);
            if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }else if(dbNum == 0) {
            result = rpcCommonService.rpcMchShoppingCartService.remove(
                    new QueryWrapper<MchShoppingCart>().lambda()
                            .eq(MchShoppingCart::getMemberId, getUser().getMemberId())
                            .eq(MchShoppingCart::getCartId, mchShoppingCart.getCartId())
            );
            if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 删除购物车商品
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> delete() {

        Long cartId = getValLongRequired("cartId");

        boolean result = rpcCommonService.rpcMchShoppingCartService.remove(
                new QueryWrapper<MchShoppingCart>().lambda()
                .eq(MchShoppingCart::getMemberId, getUser().getMemberId())
                .eq(MchShoppingCart::getCartId, cartId)
        );

        if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(PageRes.buildSuccess());
    }

    /**
     * 清空购物车
     */
    @RequestMapping("/deleteAll")
    @ResponseBody
    public ResponseEntity<?> deleteAll() {

        LambdaQueryWrapper<MchShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchShoppingCart::getMemberId, getUser().getMemberId());
        queryWrapper.eq(MchShoppingCart::getMchId, getUser().getMchId());

        boolean result = rpcCommonService.rpcMchShoppingCartService.remove(queryWrapper);
        if(!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(PageRes.buildSuccess());
    }

}
