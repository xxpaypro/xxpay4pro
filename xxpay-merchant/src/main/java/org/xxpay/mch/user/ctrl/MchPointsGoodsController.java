package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MchPointsGoodsStoreRela;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MemberGoodsExchange;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: pangxiaoyu
 * @date: 19/08/22
 * @description: 商户积分商品
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/points_goods")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchPointsGoodsController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchPointsGoodsController.class);

    /**
     * 积分商品列表
     * @return
     */
    @MethodLog( remark = "积分商品列表" )
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list( Integer page, Integer limit) {

        MchPointsGoods mchPointsGoods = getObject( MchPointsGoods.class);
        mchPointsGoods.setMchId(getUser().getBelongInfoId());
        IPage<MchPointsGoods> mchPointsGoodsList = rpcCommonService.rpcMchPointsGoodsService.list(mchPointsGoods, new Page<>(page, limit));
        return ResponseEntity.ok(PageRes.buildSuccess(mchPointsGoodsList));
    }

    /**
     * 积分商品信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long goodsId = getValLongRequired( "goodsId");
        //获取商品信息
        MchPointsGoods pointsGoods = rpcCommonService.rpcMchPointsGoodsService.getOne(
                new QueryWrapper<MchPointsGoods>().lambda()
                .eq(MchPointsGoods::getMchId, getUser().getBelongInfoId())
                .eq(MchPointsGoods::getGoodsId, goodsId)
        );
        //转换成json
        JSONObject json = JsonUtil.getJSONObjectFromObj(pointsGoods);
        String storeIds = "";
        String storeNames = "";
        json.put("storeIds", storeIds);
        json.put("storeNames", storeNames);
        if (pointsGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES) {
            //获取设置门店对应的门店ID、门店名称
            ArrayList<Long> list = new ArrayList<>();
            list.add(goodsId);
            Collection<MchPointsGoodsStoreRela> listByIds = rpcCommonService.rpcMchPointsGoodsStoreRelaService.listByIds(list);
            if (listByIds.size() != 0){
                for (MchPointsGoodsStoreRela rela: listByIds) {
                    storeIds += rela.getStoreId().toString() + ",";
                    MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(rela.getStoreId());
                    storeNames += mchStore.getStoreName() + ",";
                }
                json.put("storeIds", storeIds.substring(0, storeIds.length() - 1));
                json.put("storeNames", storeNames.substring(0, storeNames.length() - 1));
            }
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));
    }

    /**
     * 新增积分商品
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        MchPointsGoods mchPointsGoods = getObject( MchPointsGoods.class);
        //判断是否限制库存，如果限制库存，库存数量是否填写
        if (mchPointsGoods.getStockLimitType() != null && mchPointsGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES) {
            if (mchPointsGoods.getStockNum() == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_LIMIT_STOCK_NUM_ERROR));
            }
        }
        //所属商户ID
        mchPointsGoods.setMchId(getUser().getBelongInfoId());
        boolean save = rpcCommonService.rpcMchPointsGoodsService.save(mchPointsGoods);
        //如果限制门店增加门店与商品关联
        if (mchPointsGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            String storeIds = getValStringRequired( "storeIds");
            String[] split = storeIds.split(",");
            MchPointsGoodsStoreRela rela = new MchPointsGoodsStoreRela();
            rela.setGoodsId(mchPointsGoods.getGoodsId());
            for (String s : split) {
                rela.setStoreId(Long.valueOf(s));
                rpcCommonService.rpcMchPointsGoodsStoreRelaService.save(rela);
            }
        }
        if(save) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 积分商品修改
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchPointsGoods mchPointsGoods = getObject( MchPointsGoods.class);
        //当前操作人与积分商品所属商户ID是否相同
        MchPointsGoods pointsGoods = rpcCommonService.rpcMchPointsGoodsService.getById(mchPointsGoods.getGoodsId());
        if (!getUser().getBelongInfoId().equals(pointsGoods.getMchId())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        //判断是否限制库存，如果限制库存，库存数量是否填写
        if (mchPointsGoods.getStockLimitType() != null && mchPointsGoods.getStockLimitType() == MchConstant.MCH_POINTS_GOODS_STOCK_LIMIT_TYPE_YES) {
            if (mchPointsGoods.getStockNum() == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_LIMIT_STOCK_NUM_ERROR));
            }
        }
        //如果限制门店则执行门店与商品关联；如果不限制则删除关联执行更新
        Boolean result;
        if (mchPointsGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            String storeIds = getValStringRequired( "storeIds");
            result = rpcCommonService.rpcMchPointsGoodsService.updatePointsGoods(mchPointsGoods, storeIds);
        }else {
            //移除关联
            rpcCommonService.rpcMchPointsGoodsStoreRelaService.removeById(pointsGoods.getGoodsId());
            result = rpcCommonService.rpcMchPointsGoodsService.updateById(mchPointsGoods);
        }

        if(result) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 积分商品核销
     * @return
     */
    @RequestMapping("/checkPointGoods")
    @ResponseBody
    public ResponseEntity<?> checkPointGoods() {

        String exchangeNo = getValStringRequired( "exchangeNo");

        //根据会员ID和积分商品ID，查询兑换记录是否已存在
        MemberGoodsExchange exchange = rpcCommonService.rpcMemberGoodsExchangeService.getOne(
                new QueryWrapper<MemberGoodsExchange>().lambda()
                        .eq(MemberGoodsExchange::getExchangeNo, exchangeNo)
        );

        if (exchange == null){
            _log.info("兑换记录不存在，exchangeNo={}", exchangeNo);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if (exchange.getStatus() != MchConstant.INTEGRAL_COMMODITY_NOT_EXCHANGE){
            _log.info("兑换状态错误，status={}", exchange.getStatus());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MEMBER_GOODS_EXCHANGE_STATUS_ERROR));
        }

        //操作员名称
        boolean result = rpcCommonService.rpcMchPointsGoodsService.checkPointGoods(exchange, getUser().getUserId(), getUser().getLoginUserName());
        if (result) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 积分商品领取记录
     */
    @RequestMapping("/goodsRecord")
    @ResponseBody
    public ResponseEntity<?> goodsRecord( Integer page, Integer limit) {

        MemberGoodsExchange goodsExchange = getObject( MemberGoodsExchange.class);

        LambdaQueryWrapper<MemberGoodsExchange> lambda = new QueryWrapper<MemberGoodsExchange>().lambda();
        lambda.eq(MemberGoodsExchange::getMchId, getUser().getBelongInfoId());
        if (goodsExchange.getGoodsId() != null) lambda.eq(MemberGoodsExchange::getGoodsId, goodsExchange.getGoodsId());
        if (goodsExchange.getStatus() != null) lambda.eq(MemberGoodsExchange::getStatus, goodsExchange.getStatus());
        if (goodsExchange.getMemberId() != null) lambda.eq(MemberGoodsExchange::getMemberId, goodsExchange.getMemberId());
        if (goodsExchange.getMemberNo() != null) lambda.eq(MemberGoodsExchange::getMemberNo, goodsExchange.getMemberNo());
        lambda.orderByDesc(MemberGoodsExchange::getCreateTime);

        IPage<MemberGoodsExchange> list = rpcCommonService.rpcMemberGoodsExchangeService.page(new Page<>(page, limit), lambda);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }
}
