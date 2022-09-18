package org.xxpay.agent.statistics.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AgentInfo;
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
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/data")
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 账户数据
     * @return
     */
    @RequestMapping("/count4Account")
    public ResponseEntity<?> count4Account() {
        AccountBalance agentAccount = rpcCommonService.rpcAccountBalanceService.findOne(MchConstant.INFO_TYPE_AGENT, getUser().getBelongInfoId(), MchConstant.ACCOUNT_TYPE_BALANCE);
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
     * 统计代理商数据
     * @return
     */
    @RequestMapping("/count4agent")
    public ResponseEntity<?> count4Agent() {
        // 代理商分润数据
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

        //当前代理商ID
        Long currentAgentId = getUser().getBelongInfoId();
        Long queryMchId = getValLong("queryMchId"); //查询的商户ID

        Integer queryProvinceCode = getValInteger("queryProvinceCode");  //查询省
        Integer queryCityCode = getValInteger("queryCityCode");  //查询市
        Byte queryProductType = getValByte("queryProductType");  //查询支付产品类型

        //查询当前代理商等级
        AgentInfo currentAgentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(currentAgentId);
        Integer currentAgentLevel = currentAgentInfo.getAgentLevel();

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();

        List<Long> allAgentId = new ArrayList<>();
        List<Long> agentIdListLevel2 = new ArrayList<>(); //二级代理商 agentId
        List<Long> agentIdListLevel3 = new ArrayList<>(); //三级代理商 agentId


        if(currentAgentLevel == 1 || currentAgentLevel == 2 ){ //一级， 二级代理商需要查询子代理商

            List<AgentInfo> allSubList = rpcCommonService.rpcAgentInfoService.queryAllSubAgentBaseInfo(currentAgentId);
            for (AgentInfo agentInfo : allSubList) {
                if(agentInfo.getAgentLevel() == 2) agentIdListLevel2.add(agentInfo.getAgentId());
                if(agentInfo.getAgentLevel() == 3) agentIdListLevel3.add(agentInfo.getAgentId());
                allAgentId.add(agentInfo.getAgentId());
            }
        }else{
            allAgentId.add(currentAgentId);  //三级代理商 仅包含自己
        }

        //当前代理商等级
        resultMap.put("currentAgentLevel", currentAgentLevel);

        if(currentAgentLevel == 1){ //一级代理商

            Long countAgent2 = 0L, countMchByAgent2 = 0L;
            if(agentIdListLevel2.size() > 0){
                countAgent2 = rpcCommonService.rpcAgentInfoService.countByAgentIdAndTime(queryDate[0], queryDate[1], agentIdListLevel2);  //
                countMchByAgent2 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 2, null, agentIdListLevel2);
            }
            resultMap.put("countAgent2", countAgent2);  //统计二级代理商数量
            resultMap.put("countMchByAgent2", countMchByAgent2);  //统计二级商户数量

            Long countAgent3 = 0L, countMchByAgent3 = 0L;
            if(agentIdListLevel3.size() > 0){
                countAgent3 = rpcCommonService.rpcAgentInfoService.countByAgentIdAndTime(queryDate[0], queryDate[1], agentIdListLevel3);
                countMchByAgent3 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 3, null, agentIdListLevel3);
            }
            resultMap.put("countAgent3", countAgent3);  //统计三级代理商数量
            resultMap.put("countMchByAgent3", countMchByAgent3);  //统计三级商户数量

        }else if(currentAgentLevel == 2){ //二级代理商

            Long countAgent3 = 0L, countMchByAgent3 = 0L;
            if(agentIdListLevel3.size() > 0){
                countAgent3 = rpcCommonService.rpcAgentInfoService.countByAgentIdAndTime(queryDate[0], queryDate[1], agentIdListLevel3);
                countMchByAgent3 = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], 3, null, agentIdListLevel3);
            }
            resultMap.put("countAgent3", countAgent3);  //统计三级代理商数量
            resultMap.put("countMchByAgent3", countMchByAgent3);  //统计三级商户数量
        }

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, allAgentId, queryMchId, null, null, queryProductType, queryProvinceCode, queryCityCode);

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

        //充值笔数
        Long countRecharge = rpcCommonService.rpcMchTradeOrderService.countRecharge(commonCondition);
        resultMap.put("countRecharge", countRecharge);

        //赠送积分
        BigDecimal sumGivePoints = rpcCommonService.rpcMchTradeOrderService.sumGivePoints(commonCondition);
        resultMap.put("sumGivePoints", sumGivePoints);

        //充值赠送金额
        BigDecimal sumRechargeGiveAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeGiveAmount(commonCondition);
        resultMap.put("sumRechargeGiveAmount", sumRechargeGiveAmount);

        //会员充值金额
        BigDecimal sumRechargeAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeAmount(commonCondition);
        resultMap.put("sumRechargeAmount", sumRechargeAmount);

        //所有商户
        Long countAllMch = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], null, null, allAgentId);
        resultMap.put("countAllMch", countAllMch);

        //子商户数量： 直接子商户
        Long countSubMch = rpcCommonService.rpcMchInfoService.countMchByAgentLevel(queryDate[0], queryDate[1], null, null, Arrays.asList(currentAgentId));
        resultMap.put("countSubMch", countSubMch);

        //佣金金额
        BigDecimal sumProfit = rpcCommonService.rpcOrderProfitDetailService.selectSumProfitDataByFinishTask(queryDate[0], queryDate[1], currentAgentId, MchConstant.INFO_TYPE_AGENT);
        resultMap.put("sumProfit", sumProfit);

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 佣金统计图表数据 **/
    @RequestMapping("/statisticsByProfitCharts")
    public XxPayResponse statisticsByProfitCharts() {

        //当前 代理商ID
        Long currentAgentId = getUser().getBelongInfoId();

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
            BigDecimal profitAmount = rpcCommonService.rpcOrderProfitDetailService.selectSumProfitDataByFinishTask(thisStartQueryTime, thisEndQueryTime, currentAgentId, MchConstant.INFO_TYPE_AGENT);
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
        Long currentAgentId = getUser().getBelongInfoId();

        //查询所有服务商
        List<AgentInfo> allSubList = rpcCommonService.rpcAgentInfoService.queryAllSubAgentBaseInfo(currentAgentId);
        List<Long> allAgentId = new ArrayList<>();
        for (AgentInfo agentInfo : allSubList) {
            allAgentId.add(agentInfo.getAgentId());
        }

        Integer queryProvinceCode = getValInteger("queryProvinceCode");  //查询省
        Integer queryCityCode = getValInteger("queryCityCode");  //查询市
        Byte queryProductType = getValByte("queryProductType");  //查询支付产品类型

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, allAgentId,null, null, null, queryProductType, queryProvinceCode, queryCityCode);
        List<Map> resultList = rpcCommonService.rpcMchTradeOrderService.countByGroupProductType(commonCondition);
        return XxPayResponse.buildSuccess(resultList);
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
