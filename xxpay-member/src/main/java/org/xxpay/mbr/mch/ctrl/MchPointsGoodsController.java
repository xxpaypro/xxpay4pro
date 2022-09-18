package org.xxpay.mbr.mch.ctrl;

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
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MchPointsGoodsStoreRela;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MemberGoodsExchange;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: zx
 * @date: 19/09/18
 * @description: 商户积分商品
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/points_goods")
public class MchPointsGoodsController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchPointsGoodsController.class);

    /**
     * 积分商品列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list( Integer page, Integer limit) {

        MchPointsGoods mchPointsGoods = new MchPointsGoods();
        mchPointsGoods.setMchId(getUser().getMchId());
        mchPointsGoods.setStatus(MchConstant.PUB_YES);

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
                .eq(MchPointsGoods::getMchId, getUser().getMchId())
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

}
