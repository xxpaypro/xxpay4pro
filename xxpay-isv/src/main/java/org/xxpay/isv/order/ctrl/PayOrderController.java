package org.xxpay.isv.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/pay_order")
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
        Long isvId = getUser().getBelongInfoId();
        PayOrder queyrPayOrder = new PayOrder();
        queyrPayOrder.setIsvId(isvId);
        queyrPayOrder.setPayOrderId(payOrderId);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.find(queyrPayOrder);

        //查询商户 费率  和当前服务商的利润数据。
        OrderProfitDetail mchDetail = rpcCommonService.rpcOrderProfitDetailService.findMchDetailForPayOrderId(payOrderId);
        if(mchDetail != null){
            payOrder.setPsVal("mchDetail", mchDetail);
        }
        OrderProfitDetail agentDetail = rpcCommonService.rpcOrderProfitDetailService.findCurrentAgentDetailForPayOrderId(payOrderId, getUser().getBelongInfoId());
        if(agentDetail != null){
            payOrder.setPsVal("agentDetail", agentDetail);
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
        payOrder.setIsvId(getUser().getBelongInfoId());
        // 订单起止时间
        Date[] dateRange = getQueryDateRange();
        Date createTimeStart = dateRange[0];
        Date createTimeEnd = dateRange[1];

        long count = rpcCommonService.rpcPayOrderService.count(payOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select(
                (getPageIndex() -1) * getPageSize(), getPageSize(), payOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(payOrderList, count));
    }

    /**
     * 代付记录查询
     * @return
     */
    @RequestMapping("/getAgentpayRecord")
    @ResponseBody
    public ResponseEntity<?> getAgentpayRecord() {

        String agentpayOrderId = getValStringRequired( "agentpayOrderId");
        AgentpayRecord mchAgentpayRecord = new AgentpayRecord();
        mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
        mchAgentpayRecord = rpcCommonService.rpcAgentpayService.find(mchAgentpayRecord);
        if(mchAgentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayRecord));
    }

    /**
     * 代付订单记录列表
     * @return
     */
    @RequestMapping("/agentpayList")
    @ResponseBody
    public ResponseEntity<?> agentpayList() {

        Long currentAgentId = getUser().getBelongInfoId();
        Long searchInfoId = getValLong("searchInfoId");
        Byte searchInfoType = getValByte("searchInfoType");
        String accountName = getValString( "accountName");
        String agentpayOrderId = getValString( "agentpayOrderId");
        Byte agentpayChannel = getValByte( "agentpayChannel");
        Byte status = getValByte( "status");
        // 订单起止时间
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");

        List<Map> countmchAgentpayRecordList = rpcCommonService.rpcAgentpayService.agentpayCount(currentAgentId, searchInfoId, searchInfoType, accountName, agentpayOrderId, agentpayChannel, status, createTimeStart, createTimeEnd);
        Long totalCount = (Long) countmchAgentpayRecordList.get(0).get("totalCount");
        int count = totalCount.intValue();
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Map> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.agentpaySelect(
                (getPageIndex() -1) * getPageSize(), getPageSize(), currentAgentId, searchInfoId, searchInfoType, accountName, agentpayOrderId, agentpayChannel, status, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAgentpayRecordList, count));
    }

    /**
     * 代付订单记录统计
     * @return
     */
    @RequestMapping("/agentpayCount")
    @ResponseBody
    public ResponseEntity<?> agentpayCount() {

        Long currentAgentId = getUser().getBelongInfoId();
        Long searchInfoId = getValLong("searchInfoId");
        Byte searchInfoType = getValByte("searchInfoType");
        String accountName = getValString( "accountName");
        String agentpayOrderId = getValString( "agentpayOrderId");
        Byte agentpayChannel = getValByte( "agentpayChannel");
        Byte status = getValByte( "status");
        // 订单起止时间
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");

        List<Map> countmchAgentpayRecordList = rpcCommonService.rpcAgentpayService.agentpayCount(currentAgentId, searchInfoId, searchInfoType, accountName, agentpayOrderId, agentpayChannel, status, createTimeStart, createTimeEnd);
        List<Map> countmchAgentpayRecordList2 = new LinkedList<>();
        for(Map map : countmchAgentpayRecordList) {
            countmchAgentpayRecordList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(countmchAgentpayRecordList2));
    }
    private Map doMapEmpty(Map map) {
        if (map == null) return map;
        if (null == map.get("totalCount")) map.put("totalCount", 0);
        if (null == map.get("totalAmount")) map.put("totalAmount", 0);
        if (null == map.get("totalFee")) map.put("totalFee", 0);
        if (null == map.get("totalSubAmount")) map.put("totalSubAmount", 0);
        if (null == map.get("totalPlatProfit")) map.put("totalPlatProfit", 0);
        if (null == map.get("totalChannelCost")) map.put("totalChannelCost", 0);
        if (null == map.get("totalBalance")) map.put("totalBalance", 0);
        if (null == map.get("totalSettAmount")) map.put("totalSettAmount", 0);
        if (null == map.get("payProfit")) map.put("payProfit", 0);
        if (null == map.get("agentpayProfit")) map.put("agentpayProfit", 0);
        if (null == map.get("totalRemitAmount")) map.put("totalRemitAmount", 0);
        return map;
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
        Long mchId = getValLong( "mchId");
        Byte productType = getValByte( "productType");
        Long angentId = getUser().getBelongInfoId();

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcPayOrderService.count4All(angentId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);
        Map successMap = rpcCommonService.rpcPayOrderService.count4Success(angentId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);
        Map failMap = rpcCommonService.rpcPayOrderService.count4Fail(angentId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 总金额
        obj.put("successTotalCount", successMap.get("totalCount"));                 // 成功订单数
        obj.put("successTotalAmount", successMap.get("totalAmount"));               // 成功金额
        obj.put("successTotalMchIncome", successMap.get("totalMchIncome"));         // 成功商户收入
        obj.put("successTotalAgentProfit", successMap.get("totalAgentProfit"));     // 成功服务商利润
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
        payOrder.setAgentId(getUser().getBelongInfoId());
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        long count = rpcCommonService.rpcPayOrderService.count(payOrder, createTimeStart, createTimeEnd);
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select(0, MchConstant.MAX_EXPORT_ROW, payOrder, createTimeStart, createTimeEnd);

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"商户号", "支付单号", "商户单号", "支付金额", "产品类型", "状态", "时间"});
        excelData.add(header);
        for(PayOrder record : payOrderList){
            List rowData = new ArrayList<>();
            rowData.add(record.getMchId());
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

        return null;
    }


    @RequestMapping("/agpay/exportExcel")
    @ResponseBody
    public String exportExcelByAgpay() throws Exception {

        Long currentAgentId = getUser().getBelongInfoId();
        Long searchInfoId = getValLong("searchInfoId");
        Byte searchInfoType = getValByte("searchInfoType");
        String accountName = getValString( "accountName");
        String agentpayOrderId = getValString( "agentpayOrderId");
        Byte agentpayChannel = getValByte( "agentpayChannel");
        Byte status = getValByte( "status");
        // 订单起止时间
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");

        List<Map> countmchAgentpayRecordList = rpcCommonService.rpcAgentpayService.agentpayCount(currentAgentId, searchInfoId, searchInfoType, accountName, agentpayOrderId, agentpayChannel, status, createTimeStart, createTimeEnd);
        Long totalCount = (Long) countmchAgentpayRecordList.get(0).get("totalCount");
        int count = totalCount.intValue();
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();

        ///为啥MAP类型？？？
        List<Map> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.agentpaySelect(0, MchConstant.MAX_EXPORT_ROW, currentAgentId, searchInfoId, searchInfoType, accountName, agentpayOrderId, agentpayChannel, status, createTimeStart, createTimeEnd);
        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"转账单号", "商户/服务商ID", "发起类型", "账户名", "账号", "代付金额(元)", "手续费(元)", "扣减账户金额", "状态", "代付渠道", "时间"});
        excelData.add(header);
        for(Map record : mchAgentpayRecordList){
            List rowData = new ArrayList<>();
            rowData.add(record.get("TransOrderId"));
            rowData.add(record.get("InfoId"));
            rowData.add((Integer)record.get("InfoType") == MchConstant.INFO_TYPE_MCH ? "商户" : "服务商");
            rowData.add(record.get("AccountName"));
            rowData.add(record.get("AccountNo"));
            rowData.add(AmountUtil.convertCent2Dollar((Long)record.get("Amount")));
            rowData.add(AmountUtil.convertCent2Dollar((Long)record.get("Fee")));
            rowData.add(AmountUtil.convertCent2Dollar((Long)record.get("SubAmount")));
            switch (Byte.parseByte(record.get("Status").toString())){
                case PayConstant.AGENTPAY_STATUS_INIT : rowData.add("待处理"); break;
                case PayConstant.AGENTPAY_STATUS_ING : rowData.add("处理中"); break;
                case PayConstant.AGENTPAY_STATUS_SUCCESS : rowData.add("成功"); break;
                case PayConstant.AGENTPAY_STATUS_FAIL : rowData.add("失败"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add((Integer)record.get("AgentpayChannel") == MchConstant.AGENTPAY_CHANNEL_PLAT ? "商户后台" : "API接口");
            rowData.add(DateUtil.date2Str((Date)record.get("CreateTime")));
            excelData.add(rowData);
        }

        return null;
    }

}