package org.xxpay.mch.user.ctrl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.utils.printerUtils.PrintApisUtil;
import org.xxpay.mch.utils.printerUtils.PrintUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * <p>
 * 门店与打印机关联表 前端控制器
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_store_printer")
public class MchStorePrinterController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private final static MyLog _log = MyLog.getLog(MchStorePrinterController.class);

    /**
     * 获取门店与打印机信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long storeId = getValLongRequired("storeId");
        MchStorePrinter printer = rpcCommonService.rpcMchStorePrinterService.getById(storeId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(printer));
    }

    /**
     * 门店关联打印机
     * @return
     */
    @RequestMapping("/relation_printer")
    @ResponseBody
    public ResponseEntity<?> relationSrinter() {
        //获取打印机ID、门店ID
        MchStorePrinter printer = getObject(MchStorePrinter.class);
        boolean result = rpcCommonService.rpcMchStorePrinterService.saveOrUpdate(printer);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 云打印机
     */
    @RequestMapping("/pushmsgToPrinter")
    @ResponseBody
    private ResponseEntity<?> pushmsgToPrinter() {
        String tradeOrderId = getValString("tradeOrderId");
        _log.info("待打印订单：{}", tradeOrderId);
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(tradeOrderId);
        if (mchTradeOrder == null) {
            _log.info("流水不存在：{}", tradeOrderId);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }
        //获取商户ID
        Long mchId = mchTradeOrder.getMchId();
        //获取商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);
        //根据所属服务商ID获取配置信息
        IsvPrinterConfig config = rpcCommonService.rpcIsvPrinterConfigService.getById(mchInfo.getIsvId());
        if (config == null) {
            _log.info("服务商未配置云打印信息");
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_ISV_PRINTER_CONFIG_NULL));
        }
        if (config.getStatus() == MchConstant.PUB_NO) {
            _log.info("服务商配置状态关闭{}", config.getStatus());
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_ISV_PRINTER_CONFIG_OFF));
        }
        //获取门店信息
        MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(mchTradeOrder.getStoreId());
        //判断门店信息是否存在
        if (mchStore == null) {
            _log.info("门店信息不存在{}", mchStore);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
        }
        //获取门店配置云打印信息
        MchStorePrinter storePrinter = rpcCommonService.rpcMchStorePrinterService.getById(mchTradeOrder.getStoreId());
        //判断配置是否存在
        if (storePrinter == null) {
            _log.info("门店未配置云打印{}", mchStore);
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_CONFIG_NULL));
        }
        //判断状态是否正常使用
        if (storePrinter.getStatus() == MchConstant.PUB_NO) {
            _log.info("门店云打印已关闭{}", storePrinter.getStatus());
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_CONFIG_OFF));
        }
        //获取服务商配置token
        String token = "";
        //判断是否存在，若不存在，获取新的token，存入超时时间
        if (StringUtils.isEmpty(config.getToken())) {
        token = PrintApisUtil.GetToken(config.getAccessKey(), config.getAccessSecret());
            config.setToken(token);
            Date now = new Date();
            config.setTokenExpire(new Date(now.getTime() + 60*5*1000));
            rpcCommonService.rpcIsvPrinterConfigService.updateById(config);
        }else {
            //若果存在，判断超时时间，若超时则获取新的token，存入超时时间
            long nowTime = new Date().getTime();
            long expireTime = config.getTokenExpire().getTime();
            if (expireTime - nowTime < 0) {
                //获取token并存入失效时间
                token = PrintApisUtil.GetToken(config.getAccessKey(), config.getAccessSecret());
                config.setToken(token);
                Date now = new Date();
                config.setTokenExpire(new Date(now.getTime() + 60*5*1000));
                rpcCommonService.rpcIsvPrinterConfigService.updateById(config);
            }
            token = config.getToken();
        }
        try {
            //调整打印文本
            byte[] contentPrinter = PrintApisUtil.contentPrinter(mchTradeOrder);
            //调用云打印
            String string = PrintApisUtil.PrintString(contentPrinter, storePrinter.getPrinterId(), null, PrintUtils.MakeBussid(), token);
            //接受返回状态，判断是否成功
            _log.info("打印ID：{}", string);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
