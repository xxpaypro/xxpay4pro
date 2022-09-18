package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.Member;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dingzhiwei
 * @date: 18/1/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH)
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/statistics/count4income")
    @ResponseBody
    public ResponseEntity<?> count4Income() {
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, null, null);
        JSONObject object = new JSONObject();
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计今昨日数据
     * @return
     */
    @RequestMapping("/statistics/count4dayIncome")
    @ResponseBody
    public ResponseEntity<?> count4DayIncome() {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";
        // 昨日
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String yesterdayStart = yesterday + " 00:00:00";
        String yesterdayEnd = yesterday + " 23:59:59";
        // 今日收款统计
        Map todayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 今日充值统计
        Map todayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_RECHARGE, todayStart, todayEnd);
        // 今日代付统计
        Map todayAgentpayData = rpcCommonService.rpcAgentpayService.count4All(null, null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, null, null, todayStart, todayEnd);
        // 昨日收款统计
        Map yesterdayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, yesterdayStart, yesterdayEnd);
        // 昨日充值统计
        Map yesterdayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_RECHARGE, yesterdayStart, yesterdayEnd);
        // 昨日代付统计
        Map yesterdayAgentpayData = rpcCommonService.rpcAgentpayService.count4All(null, null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null,null, null, yesterdayStart, yesterdayEnd);
        JSONObject object = new JSONObject();
        object.put("todayPayData", doMapEmpty(todayPayData));
        object.put("todayRechargeData", doMapEmpty(todayRechargeData));
        object.put("todayAgentpayData", doMapEmpty(todayAgentpayData));
        object.put("yesterdayPayData", doMapEmpty(yesterdayPayData));
        object.put("yesterdayRechargeData", doMapEmpty(yesterdayRechargeData));
        object.put("yesterdayAgentpayData", doMapEmpty(yesterdayAgentpayData));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }


    /**
     * 统计代理商数据
     * @return
     */
    @RequestMapping("/statistics/count4agent")
    @ResponseBody
    public ResponseEntity<?> count4Agent() {
        // 代理商数据
        Map agentObj = rpcCommonService.rpcAgentInfoService.count4Agent();
        // 代理商分润数据
        Map agentProfitObj = new JSONObject();
        Long rechargeProfit = 0l;
        Long totalProfit = 0l;
        List<Map> mapList = rpcCommonService.rpcAccountHistoryService.count4AgentProfit(null);
        for(Map map : mapList) {
            String bizItem = map.get("bizItem").toString();
            Long profilt = Long.parseLong(map.get("totalProfit").toString());
            switch (bizItem) {
                case MchConstant.BIZ_ITEM_PAY:
                    totalProfit += profilt;
                    agentProfitObj.put("payProfit", profilt);
                    break;
                case MchConstant.BIZ_ITEM_AGENTPAY:
                    totalProfit += profilt;
                    agentProfitObj.put("agentpayProfit", profilt);
                    break;
                case MchConstant.BIZ_ITEM_OFF:
                    totalProfit += profilt;
                    rechargeProfit += profilt;
                    break;
                case MchConstant.BIZ_ITEM_ONLINE:
                    totalProfit += profilt;
                    rechargeProfit += profilt;
                    break;
            }
        }
        agentProfitObj.put("rechargeProfit", rechargeProfit);
        agentProfitObj.put("totalProfit", totalProfit);
        JSONObject object = new JSONObject();
        object.put("agentObj", doMapEmpty(agentObj));
        object.put("agentProfitObj", doMapEmpty(agentProfitObj));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计商户数据
     * @return
     */
    @RequestMapping("/statistics/count4mch")
    @ResponseBody
    public ResponseEntity<?> count4Mch() {
        // 商户数据
        Map mchObj = rpcCommonService.rpcMchInfoService.count4Mch();
        JSONObject object = new JSONObject();
        object.put("mchObj", doMapEmpty(mchObj));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计平台数据
     * @return
     */
    @RequestMapping("/statistics/count4plat")
    @ResponseBody
    public ResponseEntity<?> count4Plat() {
        // 分润数据
        List<Map> payDataObj2 = rpcCommonService.rpcAccountHistoryService.count4Data3();

        JSONObject payDataObj = new JSONObject();
        Long totalProfit = 0l;
        for(Map<String,Object> map : payDataObj2) {
            if((int)map.get("BizType") == 1) {
                payDataObj.put("payDataObj", doMapEmpty(map));
            }
            if((int)map.get("BizType") == 4) {
                payDataObj.put("rechargeDataObj", doMapEmpty(map));
            }
            if((int)map.get("BizType") == 6) {
                payDataObj.put("agentpayDataObj", doMapEmpty(map));
            }
            totalProfit += ((BigDecimal)map.get("totalPlatProfit")).longValue();
        }

        //空值设置0
        if(payDataObj.get("payDataObj")==null) {
            payDataObj.put("payDataObj", doMapEmpty(new HashMap()));
        }if(payDataObj.get("rechargeDataObj")==null) {
            payDataObj.put("rechargeDataObj", doMapEmpty(new HashMap()));
        }
        if(payDataObj.get("agentpayDataObj")==null) {
            payDataObj.put("agentpayDataObj", doMapEmpty(new HashMap()));
        }

        payDataObj.put("totalProfit",totalProfit);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payDataObj));
    }

    /**
     * 统计代付数据
     * @return
     */
    @RequestMapping("/statistics/count4agentpay")
    @ResponseBody
    public ResponseEntity<?> count4Agentpay() {
        // 商户代付数据
        Map agentpayDataObj = rpcCommonService.rpcAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_AGENTPAY);
        // 商户充值数据
        JSONObject object = new JSONObject();
        Map rechargeDataObj = rpcCommonService.rpcAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_RECHARGE);
        object.put("agentpayDataObj", doMapEmpty(agentpayDataObj));
        object.put("rechargeDataObj", doMapEmpty(rechargeDataObj));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 商户充值排行
     * @return
     */
    @RequestMapping("/data/count4MchTop")
    @ResponseBody
    public ResponseEntity<?> count4mchTop() {

        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        Long agentId = getValLong( "agentId");
        Long mchId = getValLong( "mchId");
        Byte productType = getValByte( "productType");
        // 商户充值排行
        List<Map> mchTopList = rpcCommonService.rpcPayOrderService.count4MchTop(agentId, mchId, productType, createTimeStart, createTimeEnd);
        List<Map> mchTopList2 = new LinkedList<>();
        for(Map map : mchTopList) {
            mchTopList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchTopList2));
    }

    /**
     * 代理商分润排行
     * @return
     */
    @RequestMapping("/data/count4AgentTop")
    @ResponseBody
    public ResponseEntity<?> count4AgentTop() {

        Long agentId = getValLong( "agentId");
        String bizType = getValString( "bizType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 代理商分润排行
        List<Map> agentTopList = rpcCommonService.rpcAccountHistoryService.count4AgentTop(agentId, bizType,createTimeStart, createTimeEnd);
        List<Map> agentTopList2 = new LinkedList<>();
        for(Map map : agentTopList) {
            agentTopList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentTopList2));
    }

    /**
     * 支付产品统计
     * @return
     */
    @RequestMapping("/data/count4PayProduct")
    @ResponseBody
    public ResponseEntity<?> count4PayProduct() {

        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 支付产品统计
        List<Map> payProductList = rpcCommonService.rpcPayOrderService.count4PayProduct(createTimeStart, createTimeEnd);
        List<Map> payProductList2 = payProductList.stream().map(this::doMapEmpty).collect(Collectors.toCollection(LinkedList::new));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payProductList2));
    }

    private Map doMapEmpty(Map map) {
        if(map == null) return map;
        if(null == map.get("totalCount")) map.put("totalCount", 0);
        if(null == map.get("totalAmount")) map.put("totalAmount", 0);
        if(null == map.get("totalMchIncome")) map.put("totalMchIncome", 0);
        if(null == map.get("totalAgentProfit")) map.put("totalAgentProfit", 0);
        if(null == map.get("totalPlatProfit")) map.put("totalPlatProfit", 0);
        if(null == map.get("totalChannelCost")) map.put("totalChannelCost", 0);
        if(null == map.get("totalBalance")) map.put("totalBalance", 0);
        if(null == map.get("totalSettAmount")) map.put("totalSettAmount", 0);
        if(null == map.get("payProfit")) map.put("payProfit", 0);
        if(null == map.get("agentpayProfit")) map.put("agentpayProfit", 0);
        if(null == map.get("totalRemitAmount")) map.put("totalRemitAmount", 0);
        return map;
    }

    /**
     * 代理商统计
     * @return
     */
    @RequestMapping("/data/agentStatistics")
    @ResponseBody
    public ResponseEntity<?> agentStatistics() {

        Long agentId = getValLong( "agentId");
        String bizType = getValString( "bizType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");

        // 代理商分润查询
        List<Map> countAgentStatistics = rpcCommonService.rpcOrderProfitDetailService.countAgentStatistics(agentId, bizType, createTimeStart, createTimeEnd);
        Long totalCount = (Long) countAgentStatistics.get(0).get("totalCount");
        int count = totalCount.intValue();
        List<Map> agentStatisticsList = rpcCommonService.rpcOrderProfitDetailService.agentStatistics( (getPageIndex() -1) * getPageSize(), getPageSize(),agentId, bizType,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentStatisticsList2,count));
    }

    @RequestMapping("/data/countAgentStatistics")
    @ResponseBody
    public ResponseEntity<?> countAgentStatistics() {

        Long agentId = getValLong( "agentId");
        String bizType = getValString( "bizType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 代理商分润查询
        List<Map> agentStatisticsList = rpcCommonService.rpcOrderProfitDetailService.countAgentStatistics(agentId, bizType,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentStatisticsList2));
    }

    /**
     * 平台统计
     * @return
     */
    @RequestMapping("/data/platStatistics")
    @ResponseBody
    public ResponseEntity<?> platStatistics() {

        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        String createTimeStart = createTimeStartStr + " 00:00:00";
        String createTimeEnd = createTimeEndStr + " 23:59:59";
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";
        // 昨日
        String yestday = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-24*60*60*1000);
        String yestdayStart = yestday + " 00:00:00";
        String yestdayEnd = yestday + " 23:59:59";
        // 今日收入情况
        Map todayIncome = rpcCommonService.rpcOrderProfitDetailService.platStatistics(todayStart, todayEnd);
        // 昨日收入情况
        Map yestdayIncome = rpcCommonService.rpcOrderProfitDetailService.platStatistics(yestdayStart, yestdayEnd);
        // 自定义时间收入情况
        Map Income = rpcCommonService.rpcOrderProfitDetailService.platStatistics(createTimeStart, createTimeEnd);
        // 全部收入情况
        Map totalIncome = rpcCommonService.rpcOrderProfitDetailService.platStatistics(null, null);
        JSONObject object = new JSONObject();
        object.put("todayIncome", doMapEmpty(todayIncome));
        object.put("yestdayIncome", doMapEmpty(yestdayIncome));
        object.put("Income", doMapEmpty(Income));
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 通道统计
     * @return
     */
    @RequestMapping("/data/channelStatistics")
    @ResponseBody
    public ResponseEntity<?> channelStatistics() {

        Long productId = getValLong( "productId");
        String ifCode = getValString( "ifCode");
        String payType = getValString( "payType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        //查询通道ID
        List<Map> channelMap = rpcCommonService.rpcPayOrderService.getAllChannel();
        List<Map> agentStatisticsList = null;
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for (Map map1 : channelMap) {
            // 通道统计查询
            agentStatisticsList = rpcCommonService.rpcPayOrderService.channelStatistics(map1.get("passageId"), productId, ifCode, payType, createTimeStart, createTimeEnd);
            for(Map map : agentStatisticsList) {
                agentStatisticsList2.add(doMapEmpty(map));
            }
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentStatisticsList2));
    }

    /**
     * 支付统计
     * @return
     */
    @RequestMapping("/data/paymentStatistics")
    @ResponseBody
    public ResponseEntity<?> paymentStatistics() {

        Long mchId = getValLong( "mchId");
        Byte productType = getValByte( "productType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 支付数据查询
        List<Map> countAgentStatistics = rpcCommonService.rpcPayOrderService.count4PaymentStatistics(mchId, productType,createTimeStart, createTimeEnd);
        Long totalCount = (Long) countAgentStatistics.get(0).get("totalMchCount");
        int count = totalCount.intValue();
        List<Map> agentStatisticsList = rpcCommonService.rpcPayOrderService.paymentStatistics( (getPageIndex() -1) * getPageSize(), getPageSize(),mchId, productType,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentStatisticsList2,count));
    }

    /**
     * 支付统计总数
     * @return
     */
    @RequestMapping("/data/count4PaymentStatistics")
    @ResponseBody
    public ResponseEntity<?> count4PaymentStatistics() {

        Long mchId = getValLong( "mchId");
        Byte productType = getValByte( "productType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 支付数据查询
        List<Map> agentStatisticsList = rpcCommonService.rpcPayOrderService.count4PaymentStatistics(mchId, productType,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentStatisticsList2));
    }

    /**
     * 代付统计
     * @return
     */
    @RequestMapping("/data/agentpayStatistics")
    @ResponseBody
    public ResponseEntity<?> agentpayStatistics() {

        Long infoId = getValLong( "infoId");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        Byte infoType = getValByte("infoType");
        // 代付数据查询
        List<Map> countAgentStatistics = rpcCommonService.rpcAgentpayService.countAgentpayStatistics(infoId, infoType, createTimeStart, createTimeEnd);
        Long totalCount = (Long) countAgentStatistics.get(0).get("totalMchCount");
        int count = totalCount.intValue();
        List<Map> agentStatisticsList = rpcCommonService.rpcAgentpayService.agentpayStatistics((getPageIndex() -1) * getPageSize(), getPageSize(),infoId, infoType, createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentStatisticsList2,count));
    }

    @RequestMapping("/data/countAgentpayStatistics")
    @ResponseBody
    public ResponseEntity<?> countAgentpayStatistics() {

        Long infoId = getValLong( "infoId");
        Byte infoType = getValByte("infoType");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 支付数据查询
        List<Map> agentStatisticsList = rpcCommonService.rpcAgentpayService.countAgentpayStatistics(infoId, infoType, createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentStatisticsList2));
    }

    /**
     * 结算统计
     * @return
     */
    @RequestMapping("/data/settStatistics")
    @ResponseBody
    public ResponseEntity<?> settStatistics() {

        Long mchId = getValLong( "mchId");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 结算数据查询
        List<Map> countAgentStatistics = rpcCommonService.rpcSettRecordService.countSettStatistics(mchId,createTimeStart, createTimeEnd);
        Long totalCount = (Long) countAgentStatistics.get(0).get("totalMchCount");
        int count = totalCount.intValue();
        List<Map> agentStatisticsList = rpcCommonService.rpcSettRecordService.settStatistics((getPageIndex() -1) * getPageSize(), getPageSize(),mchId,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentStatisticsList2,count));
    }

    @RequestMapping("/data/countSettStatistics")
    @ResponseBody
    public ResponseEntity<?> countSettStatistics() {

        Long mchId = getValLong( "mchId");
        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        // 结算数据查询
        List<Map> agentStatisticsList = rpcCommonService.rpcSettRecordService.countSettStatistics(mchId,createTimeStart, createTimeEnd);
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentStatisticsList2));
    }

    /**
     * 订单日成功交易金额
     * @return
     */
    @RequestMapping("/data/orderDayAmount")
    @ResponseBody
    public ResponseEntity<?> orderDayAmount() {
        Map object = new LinkedHashMap();
        Map dayAmount = new LinkedHashMap();
        for(int i = 0 ; i < 7 ; i++){
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-24*60*60*1000*i);
            String dayStart = day + " 00:00:00";
            String dayEnd = day + " 23:59:59";
            // 每日交易金额查询
            dayAmount = rpcCommonService.rpcPayOrderService.orderDayAmount(null,dayStart, dayEnd);
            object.put(i,day);
            int y = i + 7;
            object.put(y, doMapEmpty(dayAmount));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 支付产品成功订单数
     * @return
     */
    @RequestMapping("/data/orderProductSuccess")
    @ResponseBody
    public ResponseEntity<?> orderProductSuccess() {
        // 支付产品交易笔数
        List<Map> agentStatisticsList = rpcCommonService.rpcPayOrderService.orderProductSuccess();
        List<Map> agentStatisticsList2 = new LinkedList<>();
        for(Map map : agentStatisticsList) {
            agentStatisticsList2.add(doMapEmpty(map));
        }
        //总笔数数组
        Long[] count = new Long[agentStatisticsList2.size()];
        //支付产品数组
        String[] productName = new String[agentStatisticsList.size()];
        int i = 0;
        for (Map map : agentStatisticsList2){
            count[i] = (Long) map.get("totalCount");
            productName[i] = (String) map.get("ProductName");
            i++;
        }
        Map<String,Object> orderProduct = new HashMap<String,Object>();
        orderProduct.put("totalCount",count);
        orderProduct.put("productName",productName);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(orderProduct));
    }

    /** 数据统计 查询数据 **/
    @RequestMapping("/data/dataStatistics")
    public XxPayResponse dataStatistics() {

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null,  null,null, null, null, null, null, null);

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();

        //订单金额
        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
        resultMap.put("sumTradeAmount", sumTradeAmount);

        //实际收款
        BigDecimal sumRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
        resultMap.put("sumRealAmount", sumRealAmount);

        //退款金额
        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
        resultMap.put("sumRefundAmount", sumRefundAmount);

        //订单笔数
        Long countTrade = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
        resultMap.put("countTrade", countTrade);

        //退款笔数
        Long countRefund = rpcCommonService.rpcMchTradeOrderService.countRefund(commonCondition);
        resultMap.put("countRefund", countRefund);

        //优惠金额
        BigDecimal sumDiscountAmount = rpcCommonService.rpcMchTradeOrderService.sumDiscountAmount(commonCondition);
        resultMap.put("sumDiscountAmount", sumDiscountAmount);


        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 支付类型 饼状图数据 **/
    @RequestMapping("/data/statisticsByProductType")
    public XxPayResponse statisticsByProductType() {

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null,  null,null, null, null, null, null, null);
        List<Map> resultList = rpcCommonService.rpcMchTradeOrderService.countByGroupProductType(commonCondition);
        return XxPayResponse.buildSuccess(resultList);
    }

    /** 用户增长趋势 折线图 **/
    @RequestMapping("/data/statisticsByAllUserCharts")
    public XxPayResponse statisticsByAllUserCharts() {

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        List<String> xTitleArray = new ArrayList<>();  //x轴标题

        List<Integer> countIsvArray = new ArrayList<>();
        List<Integer> countAgent1Array = new ArrayList<>();
        List<Integer> countAgent2Array = new ArrayList<>();
        List<Integer> countAgent3Array = new ArrayList<>();
        List<Integer> countMchArray = new ArrayList<>();
        List<Integer> countMemberArray = new ArrayList<>();

        if(queryStartTime == null && queryStartTime == null){  //统计所有时间段的数据
            this.queryAllUserCount(null, null,
                    countIsvArray, countAgent1Array, countAgent2Array, countAgent3Array, countMchArray, countMemberArray);
        }else{

            //同一天,  2小时/时段
            int splitTime = 2;
            if(DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD).equals(DateUtil.date2Str(queryEndTime, DateUtil.FORMAT_YYYY_MM_DD))){

                String thisQueryDate = DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD);

                String lastTime = "00:00";
                String thisTime = null;
                Date thisStartQueryTime = null;
                Date thisEndQueryTime = null;
                for(int i = 1; i <= (24/splitTime); i++){

                    thisTime = String.format("%02d", i*splitTime) + ":00";
                    xTitleArray.add(lastTime + "~" + thisTime);
                    thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                    thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":59");

                    this.queryAllUserCount(thisStartQueryTime, thisEndQueryTime,
                            countIsvArray, countAgent1Array, countAgent2Array, countAgent3Array, countMchArray, countMemberArray);

                    lastTime = thisTime;
                }
            }else{//遍历日期

                for(int i = 0; i < 31; i++){  //最多31天

                    Date thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    Date thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                    xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                    this.queryAllUserCount(thisStartQueryTime, thisEndQueryTime,
                            countIsvArray, countAgent1Array, countAgent2Array, countAgent3Array, countMchArray, countMemberArray);
                }
            }

        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("countIsvArray", countIsvArray);
        resultMap.put("countAgent1Array", countAgent1Array);
        resultMap.put("countAgent2Array", countAgent2Array);
        resultMap.put("countAgent3Array", countAgent3Array);
        resultMap.put("countMchArray", countMchArray);
        resultMap.put("countMemberArray", countMemberArray);
        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 订单数量 / 销售额 平滑折线图 **/
    @RequestMapping("/data/statisticsByOrderCountAndAmountCharts")
    public XxPayResponse statisticsByOrderCountAndAmountCharts() {

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        if(queryStartTime == null || queryStartTime == null){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR); //不支持全部数据查询
        }

        List<String> xTitleArray = new ArrayList<>();  //x轴标题
        List<String> orderCountArray = new ArrayList<>();  //订单数量数据
        List<String> orderAmountArray = new ArrayList<>();  //订单金额数据

        //同一天,  2小时/时段
        int splitTime = 2;
        if(DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD).equals(DateUtil.date2Str(queryEndTime, DateUtil.FORMAT_YYYY_MM_DD))){

            String thisQueryDate = DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD);

            String lastTime = "00:00";
            String thisTime = null;
            Date thisStartQueryTime = null;
            Date thisEndQueryTime = null;
            for(int i = 1; i <= (24/splitTime); i++){

                thisTime = String.format("%02d", i*splitTime) + ":00";
                xTitleArray.add(lastTime + "~" + thisTime);
                thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":59");

                //公共查询条件
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null,  null,null, null, null, null, null, null);

                //订单数量
                Long orderCount = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
                orderCountArray.add(orderCount + "");

                //订单金额
                BigDecimal orderAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                orderAmountArray.add(AmountUtil.convertCent2Dollar(orderAmount.toString()));
                lastTime = thisTime;
            }
        }else{//遍历日期

            for(int i = 0; i < 31; i++){  //最多31天

                Date thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                    break;
                }
                Date thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                //公共查询条件
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null,  null,null, null, null, null, null, null);

                //订单数量
                Long orderCount = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
                orderCountArray.add(orderCount + "");

                //订单金额
                BigDecimal orderAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                orderAmountArray.add(AmountUtil.convertCent2Dollar(orderAmount.toString()));
            }
        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("orderCountArray", orderCountArray);
        resultMap.put("orderAmountArray", orderAmountArray);
        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 查询新增用户数量 **/
    private void queryAllUserCount(Date beginTime, Date endTime,
                      List<Integer> isvList, List<Integer> agent1List, List<Integer> agent2List,
                      List<Integer> agent3List, List<Integer> mchList, List<Integer> memberList){

        //查询全部 [新增服务商] 数量
        LambdaQueryWrapper<IsvInfo> isvWrapper = new LambdaQueryWrapper();
        isvWrapper.eq(IsvInfo::getStatus, MchConstant.PUB_YES);
        if(beginTime != null) isvWrapper.ge(IsvInfo::getCreateTime, beginTime);
        if(endTime != null) isvWrapper.le(IsvInfo::getCreateTime, endTime);

        isvList.add(rpcCommonService.rpcIsvInfoService.count(isvWrapper));

        //查询全部 [一级代理商] 数量
        LambdaQueryWrapper<AgentInfo> agent1Wrapper = new LambdaQueryWrapper();
        agent1Wrapper.eq(AgentInfo::getStatus, MchConstant.PUB_YES);
        agent1Wrapper.eq(AgentInfo::getAgentLevel, 1);
        if(beginTime != null) agent1Wrapper.ge(AgentInfo::getCreateTime, beginTime);
        if(endTime != null) agent1Wrapper.le(AgentInfo::getCreateTime, endTime);

        agent1List.add(rpcCommonService.rpcAgentInfoService.count(agent1Wrapper));

        //查询全部 [二级代理商] 数量
        LambdaQueryWrapper<AgentInfo> agent2Wrapper = new LambdaQueryWrapper();
        agent2Wrapper.eq(AgentInfo::getStatus, MchConstant.PUB_YES);
        agent2Wrapper.eq(AgentInfo::getAgentLevel, 2);
        if(beginTime != null) agent2Wrapper.ge(AgentInfo::getCreateTime, beginTime);
        if(endTime != null) agent2Wrapper.le(AgentInfo::getCreateTime, endTime);

        agent2List.add(rpcCommonService.rpcAgentInfoService.count(agent2Wrapper));

        //查询全部 [三级代理商] 数量
        LambdaQueryWrapper<AgentInfo> agent3Wrapper = new LambdaQueryWrapper();
        agent3Wrapper.eq(AgentInfo::getStatus, MchConstant.PUB_YES);
        agent3Wrapper.eq(AgentInfo::getAgentLevel, 3);
        if(beginTime != null) agent3Wrapper.ge(AgentInfo::getCreateTime, beginTime);
        if(endTime != null) agent3Wrapper.le(AgentInfo::getCreateTime, endTime);

        agent3List.add(rpcCommonService.rpcAgentInfoService.count(agent3Wrapper));

        //查询全部 [商户] 数量
        LambdaQueryWrapper<MchInfo> mchWrapper = new LambdaQueryWrapper();
        mchWrapper.eq(MchInfo::getStatus, MchConstant.PUB_YES);
        if(beginTime != null) mchWrapper.ge(MchInfo::getCreateTime, beginTime);
        if(endTime != null) mchWrapper.le(MchInfo::getCreateTime, endTime);

        mchList.add(rpcCommonService.rpcMchInfoService.count(mchWrapper));

        //查询全部 [会员] 数量
        LambdaQueryWrapper<Member> memberWrapper = new LambdaQueryWrapper();
        memberWrapper.eq(Member::getStatus, MchConstant.PUB_YES);
        if(beginTime != null) memberWrapper.ge(Member::getCreateTime, beginTime);
        if(endTime != null) memberWrapper.le(Member::getCreateTime, endTime);

        memberList.add(rpcCommonService.rpcMemberService.count(memberWrapper));
    }

}
