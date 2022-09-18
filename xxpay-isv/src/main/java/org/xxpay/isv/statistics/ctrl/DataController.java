package org.xxpay.isv.statistics.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.MchInfo;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/data")
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 账户数据
     * @return
     */
    @RequestMapping("/count4Account")
    @ResponseBody
    public ResponseEntity<?> count4Account() {
        AccountBalance agentAccount = rpcCommonService.rpcAccountBalanceService.findOne(MchConstant.INFO_TYPE_ISV, getUser().getBelongInfoId(), MchConstant.ACCOUNT_TYPE_BALANCE);
        JSONObject object = (JSONObject) JSON.toJSON(agentAccount);
        // 下级商户数量
        MchInfo queryMchInfo = new MchInfo();
        queryMchInfo.setStatus((byte) 1);
        queryMchInfo.setAgentId(getUser().getBelongInfoId());
        long mchCount = rpcCommonService.rpcMchInfoService.count(queryMchInfo);
        object.put("mchCount", mchCount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/count4Income")
    @ResponseBody
    public ResponseEntity<?> count4Income() {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";

        // 今日收入情况
        Map todayIncome = rpcCommonService.rpcPayOrderService.count4Income(getUser().getBelongInfoId(), null, MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 昨日收入情况
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(getUser().getBelongInfoId(), null, MchConstant.PRODUCT_TYPE_PAY, null, null);
        JSONObject object = new JSONObject();
        object.put("todayIncome", doMapEmpty(todayIncome));
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计服务商数据
     * @return
     */
    @RequestMapping("/count4agent")
    @ResponseBody
    public ResponseEntity<?> count4Agent() {
        // 服务商分润数据
        Map agentProfitObj = new JSONObject();
        Long rechargeProfit = 0l;
        Long totalProfit = 0l;
        List<Map> mapList = rpcCommonService.rpcOrderProfitDetailService.count4AgentProfit(getUser().getBelongInfoId());
        for(Map map : mapList) {
            Byte bizType = (byte)((int)(map.get("BizType")));
            Long profilt = Long.parseLong(map.get("totalAgentProfit").toString());
            switch (bizType) {
                case MchConstant.BIZ_TYPE_TRANSACT:
                    totalProfit += profilt;
                    agentProfitObj.put("payProfit", profilt);
                    break;
                case MchConstant.BIZ_TYPE_AGENTPAY:
                    totalProfit += profilt;
                    agentProfitObj.put("agentpayProfit", profilt);
                    break;
                case MchConstant.BIZ_TYPE_RECHARGE:
                    totalProfit += profilt;
                    agentProfitObj.put("rechargeProfit", rechargeProfit);
                    break;
            }
        }

        if (agentProfitObj.get("payProfit") == null)agentProfitObj.put("payProfit",0);
        if (agentProfitObj.get("agentpayProfit") == null)agentProfitObj.put("agentpayProfit",0);
        if (agentProfitObj.get("rechargeProfit") == null)agentProfitObj.put("rechargeProfit",0);
        agentProfitObj.put("totalProfit", totalProfit);
        JSONObject object = new JSONObject();
        object.put("agentProfitObj", doMapEmpty(agentProfitObj));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 商户充值排行
     * @return
     */
    @RequestMapping("/count4MchTop")
    @ResponseBody
    public ResponseEntity<?> count4mchTop() {

        String createTimeStart = getValString( "createTimeStart");
        String createTimeEnd = getValString( "createTimeEnd");
        Long mchId = getValLong( "mchId");
        Byte productType = getValByte( "productType");
        // 商户充值排行
        List<Map> mchTopList = rpcCommonService.rpcPayOrderService.count4MchTop(getUser().getBelongInfoId(), mchId, productType, createTimeStart, createTimeEnd);
        List<Map> mchTopList2 = mchTopList.stream().map(this::doMapEmpty).collect(Collectors.toCollection(LinkedList::new));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchTopList2));
    }


    /** 数据统计 查询数据 **/
    @RequestMapping("/dataStatistics")
    public XxPayResponse dataStatistics() {

        //当前服务商ID
        Long queryIsvId = getUser().getBelongInfoId();
        Integer queryProvinceCode = getValInteger("queryProvinceCode");  //查询省
        Integer queryCityCode = getValInteger("queryCityCode");  //查询市
        Long queryAgentId = getValLong("queryAgentId");  //查询代理商
        Byte queryProductType = getValByte("queryProductType");  //查询支付产品类型

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], queryIsvId, queryAgentId,  null,null, null, null, queryProductType, queryProvinceCode, queryCityCode);

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

        //一级代理商及下属商户 数量
        Long countAgent1 = rpcCommonService.rpcAgentInfoService.countByAgentLevel(queryDate[0], queryDate[1], 1, queryIsvId, null);
        resultMap.put("countAgent1", countAgent1);

        Long countMchByAgent1 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 1, queryIsvId, null);
        resultMap.put("countMchByAgent1", countMchByAgent1);

        //二级代理商及下属商户 数量
        Long countAgent2 = rpcCommonService.rpcAgentInfoService.countByAgentLevel(queryDate[0], queryDate[1], 2, queryIsvId, null);
        resultMap.put("countAgent2", countAgent2);

        Long countMchByAgent2 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 2, queryIsvId, null);
        resultMap.put("countMchByAgent2", countMchByAgent2);

        //三级代理商及下属商户 数量
        Long countAgent3 = rpcCommonService.rpcAgentInfoService.countByAgentLevel(queryDate[0], queryDate[1], 3, queryIsvId, null);
        resultMap.put("countAgent3", countAgent3);

        Long countMchByAgent3 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 3, queryIsvId, null);
        resultMap.put("countMchByAgent3", countMchByAgent3);

        //所有商户
        Integer countAllMch = rpcCommonService.rpcMchInfoService.countAllMch(queryDate[0], queryDate[1], queryIsvId, false);
        resultMap.put("countAllMch", countAllMch);

        //子商户数量
        Integer countSubMch = rpcCommonService.rpcMchInfoService.countAllMch(queryDate[0], queryDate[1], queryIsvId, true);
        resultMap.put("countSubMch", countSubMch);

        //佣金金额
        BigDecimal sumProfit = rpcCommonService.rpcOrderProfitDetailService.selectSumProfitDataByFinishTask(queryDate[0], queryDate[1], queryIsvId, MchConstant.INFO_TYPE_ISV);
        resultMap.put("sumProfit", sumProfit);

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 佣金统计图表数据 **/
    @RequestMapping("/statisticsByProfitCharts")
    public XxPayResponse statisticsByProfitCharts() {

        //当前服务商ID
        Long currentIsvId = getUser().getBelongInfoId();

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        if(queryStartTime == null || queryStartTime == null){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR); //不支持全部数据查询
        }

        List<String> xTitleArray = new ArrayList<>();  //x轴标题
        List<String> profitArray = new ArrayList<>();  //佣金数据


        for(int i = 0; i < 31; i++){  //最多31天

            Date thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

            if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                break;
            }

            Date thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
            xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

            //佣金
            BigDecimal profitAmount = rpcCommonService.rpcOrderProfitDetailService.selectSumProfitDataByFinishTask(thisStartQueryTime, thisEndQueryTime, currentIsvId, MchConstant.INFO_TYPE_ISV);
            profitArray.add(AmountUtil.convertCent2Dollar(profitAmount.toString()));
        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("profitArray", profitArray);
        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 支付类型 饼状图数据 **/
    @RequestMapping("/statisticsByProductType")
    public XxPayResponse statisticsByProductType() {

        //查询商户ID
        Long currentIsvId = getUser().getBelongInfoId();
        Integer queryProvinceCode = getValInteger("queryProvinceCode");  //查询省
        Integer queryCityCode = getValInteger("queryCityCode");  //查询市
        Long queryAgentId = getValLong("queryAgentId");  //查询代理商
        Byte queryProductType = getValByte("queryProductType");  //查询支付产品类型

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], currentIsvId, queryAgentId,  null,null, null, null, queryProductType, queryProvinceCode, queryCityCode);
        List<Map> resultList = rpcCommonService.rpcMchTradeOrderService.countByGroupProductType(commonCondition);
        return XxPayResponse.buildSuccess(resultList);
    }


    /** 订单数量 / 销售额 平滑折线图 **/
    @RequestMapping("/statisticsByOrderCountAndAmountCharts")
    public XxPayResponse statisticsByOrderCountAndAmountCharts() {

        //当前服务商ID
        Long currentIsvId = getUser().getBelongInfoId();
        Integer queryProvinceCode = getValInteger("queryProvinceCode");  //查询省
        Integer queryCityCode = getValInteger("queryCityCode");  //查询市
        Long queryAgentId = getValLong("queryAgentId");  //查询代理商
        Byte queryProductType = getValByte("queryProductType");  //查询支付产品类型

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
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, currentIsvId, queryAgentId,  null,null, null, null, queryProductType, queryProvinceCode, queryCityCode);

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
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, currentIsvId, queryAgentId,  null,null, null, null, queryProductType, queryProvinceCode, queryCityCode);

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
        return map;
    }

}
