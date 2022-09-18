package org.xxpay.mbr.member.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.*;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_goods")
public class MemberPointsGoodsController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MemberPointsGoodsController.class);

    /**
     * 兑换积分商品
     */
    @RequestMapping("/addGoodsExchange")
    @ResponseBody
    public ResponseEntity<?> addGoodsExchange() {

        Long goodsId = getValLongRequired("goodsId");
        Long memberId = getUser().getMemberId();
        String memberNo = getUser().getMemberNo();

        //商户设置的积分商品
        MchPointsGoods mchPointsGoods = rpcCommonService.rpcMchPointsGoodsService.getById(goodsId);
        if (mchPointsGoods == null || mchPointsGoods.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_GOODS_NOT_EXISTS));
        }

        //获取当前登录人商户ID
        Long mchId = getUser().getMchId();
        //判断当前积分商品与当前会员所属商户是否一致
        if (!mchPointsGoods.getMchId().equals(mchId)) {
            _log.info("该积分商品非{}商户下的，mchPointsGoods={}", mchId, mchPointsGoods);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        //判断库存
        if (mchPointsGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES && mchPointsGoods.getStockNum() <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_GOODS_GET_OVER));
        }

        //单用户兑换数量
        if (mchPointsGoods.getSingleMemberLimit() != MchConstant.MCH_COUPON_NO_SINGLEUSERLIMIT) {
            int goodsCount = rpcCommonService.rpcMemberGoodsExchangeService.count(new QueryWrapper<MemberGoodsExchange>().lambda()
                    .eq(MemberGoodsExchange::getGoodsId, goodsId)
                    .eq(MemberGoodsExchange::getMemberId, memberId));
            if (mchPointsGoods.getSingleMemberLimit() <= goodsCount) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_GOODS_HAS_GET));
            }
        }

        //判断积分是否足够
        MemberPoints memberPoints = rpcCommonService.rpcMemberPointsService.getById(memberId);
        if (memberPoints.getPoints() < mchPointsGoods.getPoints()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_NOT_ENOUGH));
        }

        int count = rpcCommonService.rpcMemberGoodsExchangeService.addMbrGoods(mchPointsGoods, memberId, memberNo, mchId);
        if (count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 会员已兑换的积分商品列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> memberGetCouponList( Integer page, Integer limit) {

        Long memberId = getUser().getMemberId();
        Byte status = getValByteRequired("status");

        //查询会员积分商品兑换记录
        LambdaQueryWrapper<MemberGoodsExchange> lambda = new QueryWrapper<MemberGoodsExchange>().lambda();
        lambda.eq(MemberGoodsExchange::getMemberId, memberId);
        lambda.eq(MemberGoodsExchange::getStatus, status);
        lambda.eq(MemberGoodsExchange::getMchId, getUser().getMchId());
        IPage<MemberGoodsExchange> list = rpcCommonService.rpcMemberGoodsExchangeService.page(new Page<>(page, limit), lambda);

        //查询会员已领取的积分商品信息
        List goodsList = new LinkedList();
        for (MemberGoodsExchange memberGoodsExchange : list.getRecords()) {
            MchPointsGoods mchPointsGoods = rpcCommonService.rpcMchPointsGoodsService.getById(memberGoodsExchange.getGoodsId());
            if (mchPointsGoods != null){
                JSONObject object = (JSONObject) JSONObject.toJSON(mchPointsGoods);
                object.put("exchangeNo", memberGoodsExchange.getExchangeNo());
                goodsList.add(object);
            }
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(goodsList));
    }

    /**
     * 会员已兑换的积分商品详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get( ) {

        String exchangeNo = getValStringRequired("exchangeNo");
        //会员积分商品兑换记录
        MemberGoodsExchange goodsExchange = rpcCommonService.rpcMemberGoodsExchangeService.getOne(
                        new QueryWrapper<MemberGoodsExchange>().lambda().eq(MemberGoodsExchange::getExchangeNo, exchangeNo));
        if (goodsExchange == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_GOODS_NOT_EXISTS));
        }

        //商户积分商品记录
        MchPointsGoods pointsGoods = rpcCommonService.rpcMchPointsGoodsService.getById(goodsExchange.getGoodsId());
        if (pointsGoods == null || pointsGoods.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_POINTS_GOODS_NOT_EXISTS));
        }

        //优惠券所属门店
        String storeNames = "";
        if (pointsGoods.getStoreLimitType() != MchConstant.MCH_STORE_LIMIT_TYPE_NO) {
            Collection<MchPointsGoodsStoreRela> listByIds = rpcCommonService.rpcMchPointsGoodsStoreRelaService.list(
                    new QueryWrapper<MchPointsGoodsStoreRela>().lambda().eq(MchPointsGoodsStoreRela::getGoodsId, pointsGoods.getGoodsId()));
            List goodsIdList = new LinkedList();
            if (listByIds.size() != 0){
                for (MchPointsGoodsStoreRela rela: listByIds) {
                    goodsIdList.add(rela.getStoreId());
                }

                //添加门店名称
                List<MchStore> mchStoreList = rpcCommonService.rpcMchStoreService.list(new QueryWrapper<MchStore>().lambda().
                        in(MchStore::getStoreId, goodsIdList));
                for (MchStore store : mchStoreList) {
                    storeNames += store.getStoreName() + "，";
                }
                storeNames = storeNames.substring(0, storeNames.length()-1);
            }
        }

        //前端返回
        JSONObject object = (JSONObject) JSONObject.toJSON(pointsGoods);
        object.put("storeNames", storeNames);

        return ResponseEntity.ok(PageRes.buildSuccess(object));
    }

}
