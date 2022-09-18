package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/pay_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class PayOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条支付记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String payOrderId = getValStringRequired( "payOrderId");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByMchIdAndPayOrderId(getUser().getBelongInfoId(), payOrderId);

        OrderProfitDetail mchDetail = rpcCommonService.rpcOrderProfitDetailService.findMchDetailForPayOrderId(payOrderId);
        if(mchDetail != null){
            payOrder.setPsVal("mchDetail", mchDetail);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(payOrder));
    }

    /**
     * 支付订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayOrder payOrder = getObject( PayOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        long count = rpcCommonService.rpcPayOrderService.count(getUser().getBelongInfoId(), payOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select(getUser().getBelongInfoId(),
                (getPageIndex() -1) * getPageSize(), getPageSize(), payOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(payOrderList, count));
    }

    /**
     * 查询订单统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        String payOrderId = getValString( "payOrderId");
        String mchOrderNo = getValString( "mchOrderNo");
        Long productId = getValLong( "productId");
        Byte productType = getValByte("productType");
        Long mchId = getUser().getBelongInfoId();

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcPayOrderService.count4All(null, mchId, productId, payOrderId, mchOrderNo, productType,createTimeStartStr, createTimeEndStr);
        Map successMap = rpcCommonService.rpcPayOrderService.count4Success(null, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);
        Map failMap = rpcCommonService.rpcPayOrderService.count4Fail(null, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 总金额
        obj.put("successTotalCount", successMap.get("totalCount"));                 // 成功订单数
        obj.put("successTotalAmount", successMap.get("totalAmount"));               // 成功金额
        obj.put("successTotalMchIncome", successMap.get("totalMchIncome"));         // 成功商户收入
        obj.put("failTotalCount", failMap.get("totalCount"));                       // 未完成订单数
        obj.put("failTotalAmount", failMap.get("totalAmount"));                     // 未完成金额
        if (Long.parseLong(allMap.get("totalCount").toString()) == 0l) {
            obj.put("successRate", 1l);
        }else {
            BigDecimal successCount = new BigDecimal((long)successMap.get("totalCount"));
            BigDecimal allCount = new BigDecimal((long)allMap.get("totalCount"));
            obj.put("successRate", successCount.multiply(new BigDecimal(100)).divide(allCount,2,BigDecimal.ROUND_HALF_UP));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        Integer page = getValInteger( "page");
        Integer limit = getValInteger( "limit");
        PayOrder payOrder = getObject( PayOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        long count = rpcCommonService.rpcPayOrderService.count(getUser().getBelongInfoId(), payOrder, createTimeStart, createTimeEnd);
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select(getUser().getBelongInfoId(),
              0, MchConstant.MAX_EXPORT_ROW, payOrder, createTimeStart, createTimeEnd);

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"支付单号", "商户单号", "支付金额", "产品类型", "状态", "创建时间"});
        excelData.add(header);
        for(PayOrder record : payOrderList){
            List rowData = new ArrayList<>();
            rowData.add(record.getPayOrderId());
            rowData.add(record.getMchOrderNo());
            rowData.add(AmountUtil.convertCent2Dollar(record.getAmount()));
            switch (record.getProductType()){
                case MchConstant.PRODUCT_TYPE_PAY : rowData.add("收款"); break;
                case MchConstant.PRODUCT_TYPE_RECHARGE : rowData.add("充值"); break;
                default: rowData.add("未知"); break;
            }
            switch (record.getStatus()){
                case PayConstant.PAY_STATUS_INIT: rowData.add("订单初始"); break;
                case PayConstant.PAY_STATUS_PAYING: rowData.add("支付中"); break;
                case PayConstant.PAY_STATUS_SUCCESS: rowData.add("支付成功"); break;
                case PayConstant.PAY_STATUS_REFUND: rowData.add("已退款"); break;
                case PayConstant.PAY_STATUS_FAILED: rowData.add("支付失败"); break;
                case PayConstant.PAY_STATUS_EXPIRED: rowData.add("订单超时"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add(DateUtil.date2Str(record.getCreateTime()));
            excelData.add(rowData);
        }

        super.writeExcelStream("支付订单", excelData);
        return null;
    }

}