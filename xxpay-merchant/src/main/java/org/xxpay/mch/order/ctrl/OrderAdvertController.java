package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 17/12/27
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/order_advert")
public class OrderAdvertController extends BaseController {

    private static final MyLog _log = MyLog.getLog(OrderAdvertController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /** 查询支付成功订单的返回广告  **/
    @RequestMapping("/get_advert")
    public ResponseEntity<?> orderAdvert() {

        String tradeOrderId = getValStringRequired("tradeOrderId");//订单ID
        Byte showType = getValByteRequired("showType"); //显示位置

        //查询订单信息
        MchTradeOrder tradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(tradeOrderId);
        if (tradeOrder == null) {
            _log.info("订单不存在，tradeOrderId={}", tradeOrderId);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }

        //查询广告配置项
        JSONArray result = rpcCommonService.rpcIsvAdvertConfigService.selectAdListByTradeOrder(showType, tradeOrder);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }
}
