package org.xxpay.mch.statistics.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchTradeOrderBatch;
import org.xxpay.core.entity.MchTradeOrderBatchHour;
import org.xxpay.core.entity.SysUser;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dingzhiwei
 * @date: 18/1/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/data")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 统计数据
     * @return
     */
    @RequestMapping("/7day")
    @ResponseBody
    public ResponseEntity<?> count() {
        Random random = new Random();
        JSONArray array = new JSONArray();
        for(int i=1; i<8; i++) {
            String day = "2018-01-0" + i;
            long value = random.nextInt(10) * i;
            JSONArray dArray = new JSONArray();
            dArray.add(day);
            dArray.add(value);
            array.add(dArray);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(array));
    }

    /**
     * 账户数据
     * @return
     */
    @RequestMapping("/count4Account")
    @ResponseBody
    public ResponseEntity<?> count4Account() {

        Map<String, Object> dataInfo = rpcCommonService.rpcAccountBalanceService.selectLineByInfoId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId());

        JSONObject object = (JSONObject) JSON.toJSON(dataInfo);
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getBelongInfoId(), MchConstant.PRODUCT_TYPE_PAY, null, null);
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/count4dayIncome")
    @ResponseBody
    public ResponseEntity<?> count4DayIncome() {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";
        Byte infoType = 2;
        // 昨日
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String yesterdayStart = yesterday + " 00:00:00";
        String yesterdayEnd = yesterday + " 23:59:59";
        // 今日收款统计
        Map todayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getBelongInfoId(), MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 今日充值统计
        Map todayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getBelongInfoId(), MchConstant.PRODUCT_TYPE_RECHARGE, todayStart, todayEnd);
        // 今日代付统计
        Map todayAgentpayData = rpcCommonService.rpcAgentpayService.count4All(getUser().getBelongInfoId(), null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, infoType, null, todayStart, todayEnd);
        // 昨日收款统计
        Map yesterdayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getBelongInfoId(), MchConstant.PRODUCT_TYPE_PAY, yesterdayStart, yesterdayEnd);
        // 昨日充值统计
        Map yesterdayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getBelongInfoId(), MchConstant.PRODUCT_TYPE_RECHARGE, yesterdayStart, yesterdayEnd);
        // 昨日代付统计
        Map yesterdayAgentpayData = rpcCommonService.rpcAgentpayService.count4All(getUser().getBelongInfoId(), null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null,infoType, null, yesterdayStart, yesterdayEnd);
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
     * 订单日成功交易金额
     * @return
     */
    @RequestMapping("/orderDayAmount")
    @ResponseBody
    public ResponseEntity<?> orderDayAmount() {
        Long mchId = getUser().getBelongInfoId();
        Map object = new LinkedHashMap();
        Map dayAmount = new LinkedHashMap();
        for(int i = 0 ; i < 7 ; i++){
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-24*60*60*1000*i);
            String dayStart = day + " 00:00:00";
            String dayEnd = day + " 23:59:59";
            // 每日交易金额查询
            dayAmount = rpcCommonService.rpcPayOrderService.orderDayAmount(mchId,dayStart, dayEnd);
            object.put(i,day);
            int y = i + 7;
            object.put(y, doMapEmpty(dayAmount));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }


   /** 数据统计 查询数据 **/
    @RequestMapping("/dataStatistics")
    public XxPayResponse dataStatistics() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //查询门店ID
        Long queryStoreId = getValLong("queryStoreId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES){  //非商户本身， 仅查询当前门店， 否则支持切换门店
            queryStoreId = getUser().getStoreId();
        }

        //查询操作员
        Long queryOperatorId = getValLong("queryOperatorId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES || false){  //非商户本身 || //TODO 非店长， 仅查询当前操作员，否则支持切换
            queryOperatorId = getUser().getUserId();
        }

        //根据 产品类型查找
        Byte queryProductType = getValByte("queryProductType");

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, null, queryMchId, queryStoreId, queryOperatorId, queryProductType, null, null);

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();

        //订单金额
        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
        resultMap.put("sumTradeAmount", sumTradeAmount);

        //实际收款
        BigDecimal sumRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
        resultMap.put("sumRealAmount", sumRealAmount);

        //会员消费
        BigDecimal sumMemberAmount = rpcCommonService.rpcMchTradeOrderService.sumMemberAmount(commonCondition);
        resultMap.put("sumMemberAmount", sumMemberAmount);

        //退款金额
        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
        resultMap.put("sumRefundAmount", sumRefundAmount);

        //新增会员
        Integer countAddMember = rpcCommonService.rpcMemberService.countAddMember(queryDate[0], queryDate[1],queryMchId);
        resultMap.put("countAddMember", countAddMember);

        //订单笔数
        Long countTrade = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
        resultMap.put("countTrade", countTrade);

        //优惠金额
        BigDecimal sumDiscountAmount = rpcCommonService.rpcMchTradeOrderService.sumDiscountAmount(commonCondition);
        resultMap.put("sumDiscountAmount", sumDiscountAmount);

        //非会员消费
        BigDecimal sumNotMemberAmount = rpcCommonService.rpcMchTradeOrderService.sumNotMemberAmount(commonCondition);
        resultMap.put("sumNotMemberAmount", sumNotMemberAmount);

        //退款笔数
        Long countRefund = rpcCommonService.rpcMchTradeOrderService.countRefund(commonCondition);
        resultMap.put("countRefund", countRefund);

        //充值笔数
        Long countRecharge = rpcCommonService.rpcMchTradeOrderService.countRecharge(commonCondition);
        resultMap.put("countRecharge", countRecharge);

        //会员充值
        BigDecimal sumRechargeAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeAmount(commonCondition);
        resultMap.put("sumRechargeAmount", sumRechargeAmount);

        //赠送积分
        BigDecimal sumGivePoints = rpcCommonService.rpcMchTradeOrderService.sumGivePoints(commonCondition);
        resultMap.put("sumGivePoints", sumGivePoints);

        //充值赠送金额
        BigDecimal sumRechargeGiveAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeGiveAmount(commonCondition);
        resultMap.put("sumRechargeGiveAmount", sumRechargeGiveAmount);

        //会员卡折扣金额(目前仅赠送积分， 无会员卡折扣金额优惠)
        resultMap.put("sumMemberDiscountAmount", 0L);

        //会员 / 非会员订单数量
        List<Integer> countTradeOrderByMember = rpcCommonService.rpcMchTradeOrderService.countTradeOrderByMember(commonCondition);
        resultMap.put("countMemberTradeOrder", countTradeOrderByMember.get(0));
        resultMap.put("countNotMemberTradeOrder", countTradeOrderByMember.get(1));

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 金额统计图表数据 **/
    @RequestMapping("/statisticsByAmountCharts")
    public XxPayResponse statisticsByAmountCharts() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //查询门店ID
        Long queryStoreId = getValLong("queryStoreId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES){  //非商户本身， 仅查询当前门店， 否则支持切换门店
            queryStoreId = getUser().getStoreId();
        }

        //查询操作员
        Long queryOperatorId = getValLong("queryOperatorId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES || false){  //非商户本身 || //TODO 非店长， 仅查询当前操作员，否则支持切换
            queryOperatorId = getUser().getUserId();
        }

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        List<String> xTitleArray = new ArrayList<>();  //x轴标题
        List<String> tradeAmountArray = new ArrayList<>();  //交易金额数据
        List<String> refundAmountArray = new ArrayList<>();  //退款金额数据

        //同一天,  2小时/时段
        int splitTime = 2;
        //判断登录端只能是web或者uni-app
        String loginType = getUser().getLoginType();
        if (!(MchConstant.LOGIN_TYPE_WEB.equals(loginType) || MchConstant.LOGIN_TYPE_APP.equals(loginType))) {
            return XxPayResponse.build(RetEnum.RET_MCH_LOGIN_TYPE_ERROR);
        }
        if(DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD).equals(DateUtil.date2Str(queryEndTime, DateUtil.FORMAT_YYYY_MM_DD))){

            String thisQueryDate = DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD);

            String lastTime = "00:00";
            String thisTime = null;
            Date thisStartQueryTime = null;
            Date thisEndQueryTime = null;
            for(int i = 1; i <= (24/splitTime); i++){
                if (loginType.equals(MchConstant.LOGIN_TYPE_WEB)){
                    thisTime = String.format("%02d", i*splitTime) + ":00";
                    xTitleArray.add(lastTime + "~" + thisTime);
                    thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                    thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":59");
                }else if (loginType.equals(MchConstant.LOGIN_TYPE_APP)){
                    thisTime = String.format("%02d", i*splitTime) + ":00";
                    if(i%2 == 0){
                        xTitleArray.add(" ");
                    }else{
                        xTitleArray.add(lastTime.substring(0, 2) + "~" + thisTime.substring(0, 2));
                    }
                    thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                    thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":00");
                }

                //公共查询条件
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

                //查询交易金额
                BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                tradeAmountArray.add(AmountUtil.convertCent2Dollar(sumTradeAmount.toString()));

                //退款金额
                BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
                refundAmountArray.add(AmountUtil.convertCent2Dollar(sumRefundAmount.toString()));
                lastTime = thisTime;
            }
        }else{//遍历日期

            if (loginType.equals(MchConstant.LOGIN_TYPE_WEB)){
                for(int i = 0; i < 16; i++){  //最多15天

                    Date thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    Date thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                    xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                //公共查询条件
                Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

                    //查询交易金额
                BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                    tradeAmountArray.add(AmountUtil.convertCent2Dollar(sumTradeAmount.toString()));

                    //退款金额
                BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
                    refundAmountArray.add(AmountUtil.convertCent2Dollar(sumRefundAmount.toString()));
                }
            }else if (loginType.equals(MchConstant.LOGIN_TYPE_APP)){
                //开始和结束时间 相差天数
                long diffDay = DateUtil.diffDay(queryStartTime, queryEndTime) + 1;
                if (diffDay > 30 || diffDay <= 0) return XxPayResponse.build(RetEnum.RET_MCH_DIFF_DAY);//最多三十天

                //查询开始时间
                Date thisStartQueryTime;
                //查询结束时间
                Date thisEndQueryTime;

                if (0 < diffDay && diffDay <= 10) {
                    for(int i = 0; i < diffDay; i++){

                        thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                        if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                            break;
                        }
                        thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                        xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                        //公共查询条件
                        Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

                        //查询交易金额
                        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                        tradeAmountArray.add(AmountUtil.convertCent2Dollar(sumTradeAmount.toString()));

                        //退款金额
                        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
                        refundAmountArray.add(AmountUtil.convertCent2Dollar(sumRefundAmount.toString()));
                    }
                } else if (10 < diffDay && diffDay <= 20) {
                    thisStartQueryTime = queryStartTime;

                    for(int i = 1; i < diffDay/2 + 1; i++){

                        if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                            break;
                        }
                        thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 1)); //查询结束时间
                        if(i%2 == 0){
                            xTitleArray.add(" ");
                        }else{
                            xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                        }

                        //公共查询条件
                        Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

                        //查询交易金额
                        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                        tradeAmountArray.add(AmountUtil.convertCent2Dollar(sumTradeAmount.toString()));

                        //退款金额
                        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
                        refundAmountArray.add(AmountUtil.convertCent2Dollar(sumRefundAmount.toString()));

                        thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                    }
                } else if (20 < diffDay && diffDay <= 30) {
                    thisStartQueryTime = queryStartTime;

                    for(int i = 1; i < diffDay/3 + 1; i++){

                        if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                            break;
                        }
                        thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 2)); //查询结束时间
                        if(i%2 == 0){
                            xTitleArray.add(" ");
                        }else{
                            xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                        }

                        //公共查询条件
                        Map commonCondition = XXPayUtil.packageDataCommonCondition(thisStartQueryTime, thisEndQueryTime, null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

                        //查询交易金额
                        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
                        tradeAmountArray.add(AmountUtil.convertCent2Dollar(sumTradeAmount.toString()));

                        //退款金额
                        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
                        refundAmountArray.add(AmountUtil.convertCent2Dollar(sumRefundAmount.toString()));

                        thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                    }
                }
            }

        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("tradeAmountArray", tradeAmountArray);
        resultMap.put("refundAmountArray", refundAmountArray);

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 支付类型 饼状图数据 **/
    @RequestMapping("/statisticsByProductType")
    public XxPayResponse statisticsByProductType() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //查询门店ID
        Long queryStoreId = getValLong("queryStoreId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES){  //非商户本身， 仅查询当前门店， 否则支持切换门店
            queryStoreId = getUser().getStoreId();
        }

       //查询操作员
        Long queryOperatorId = getValLong("queryOperatorId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES || false){  //非商户本身 || //TODO 非店长， 仅查询当前操作员，否则支持切换
            queryOperatorId = getUser().getUserId();
        }

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

        List<Map> resultList = rpcCommonService.rpcMchTradeOrderService.countByGroupProductType(commonCondition);
        return XxPayResponse.buildSuccess(resultList);
    }

    /** 新增会员和会员总数，会员消费统计 **/
    @RequestMapping("/countMbr")
    public XxPayResponse countMbr() {
        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, null, queryMchId, null, null, null, null, null);

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();

        //新增会员
        Integer countAddMember = rpcCommonService.rpcMemberService.countAddMember(queryDate[0], queryDate[1], queryMchId);
        resultMap.put("countAddMember", countAddMember);

        //会员总数
        Integer countMember = rpcCommonService.rpcMemberService.countMember(queryMchId);
        resultMap.put("countMember", countMember);

        //会员充值赠送金额
        BigDecimal sumRechargeGiveAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeGiveAmount(commonCondition);
        resultMap.put("sumRechargeGiveAmount", sumRechargeGiveAmount);

        //会员消费金额
        BigDecimal sumMbrAmount = rpcCommonService.rpcMchTradeOrderService.sumMbrAmount(commonCondition);
        resultMap.put("sumMbrAmount", sumMbrAmount);

        //会员充值金额
        BigDecimal sumRechargeAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeAmount(commonCondition);
        resultMap.put("sumRechargeAmount", sumRechargeAmount);

        //会员退款金额
        BigDecimal sumMbrRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumMbrRefundAmount(commonCondition);
        resultMap.put("sumMbrRefundAmount", sumMbrRefundAmount);

        //会员消费笔数
        Long countMbrTrade = rpcCommonService.rpcMchTradeOrderService.countMbrTrade(commonCondition);
        resultMap.put("countMbrTrade", countMbrTrade);

        //会员充值笔数
        Long countMbrRecharge = rpcCommonService.rpcMchTradeOrderService.countMbrRecharge(commonCondition);
        resultMap.put("countMbrRecharge", countMbrRecharge);

        //会员退款笔数
        Long countMbrRefund = rpcCommonService.rpcMchTradeOrderService.countMbrRefund(commonCondition);
        resultMap.put("countMbrRefund", countMbrRefund);

        //会员赠送积分
        Long sumGivePoints = rpcCommonService.rpcMemberPointsHistoryService.sumGivePoints(queryDate[0], queryDate[1], queryMchId);
        resultMap.put("sumGivePoints", sumGivePoints);

        //会员消费积分
        Long sumConsumePoints = rpcCommonService.rpcMemberPointsHistoryService.sumConsumePoints(queryDate[0], queryDate[1], queryMchId);
        resultMap.put("sumConsumePoints", sumConsumePoints);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 会员性别统计图表数据 **/
    @RequestMapping("/statisticsByMbrSexCharts")
    public XxPayResponse statisticsByMemberSexCharts() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        List<String> xTitleArray = new ArrayList<>();  //x轴标题
        List<Integer> countMbrUnknown = new ArrayList<>();  //会员性别未知
        List<Integer> countMbrMale = new ArrayList<>();  //会员性别男
        List<Integer> countMbrFemale = new ArrayList<>();  //会员性别女

        //同一天,  2小时/时段
        int splitTime = 2;
        if(DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD).equals(DateUtil.date2Str(queryEndTime, DateUtil.FORMAT_YYYY_MM_DD))){

            String thisQueryDate = DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD);

            String lastTime = "00:00";
            for(int i = 1; i <= (24/splitTime); i++){
                String thisTime = String.format("%02d", i*splitTime) + ":00";
                if(i%2 == 0){
                    xTitleArray.add(" ");
                }else{
                    xTitleArray.add(lastTime.substring(0, 2) + "~" + thisTime.substring(0, 2));
                }
                Date thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                Date thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":00");

                //查询性别统计数据
                int countUnknown = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_UNKNOWN);
                countMbrUnknown.add(countUnknown);

                int countMale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_MALE);
                countMbrMale.add(countMale);

                int countFemale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_FEMALE);
                countMbrFemale.add(countFemale);

                lastTime = thisTime;
            }
        }else{//遍历日期
            //开始和结束时间 相差天数
            long diffDay = DateUtil.diffDay(queryStartTime, queryEndTime) + 1;
            if (diffDay > 30 || diffDay <= 0) return XxPayResponse.build(RetEnum.RET_MCH_DIFF_DAY);//最多三十天

            //查询开始时间
            Date thisStartQueryTime;
            //查询结束时间
            Date thisEndQueryTime;

            if (0 < diffDay && diffDay <= 10) {
                for(int i = 0; i < diffDay; i++){

                    thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                    xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                    //查询性别统计数据
                    int countUnknown = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_UNKNOWN);
                    countMbrUnknown.add(countUnknown);

                    int countMale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_MALE);
                    countMbrMale.add(countMale);

                    int countFemale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_FEMALE);
                    countMbrFemale.add(countFemale);
                }
            } else if (10 < diffDay && diffDay <= 20) {
                thisStartQueryTime = queryStartTime;

                for(int i = 1; i < diffDay/2 + 1; i++){

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 1)); //查询结束时间
                    if(i%2 == 0){
                        xTitleArray.add(" ");
                    }else{
                        xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                    }

                    //查询性别统计数据
                    int countUnknown = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_UNKNOWN);
                    countMbrUnknown.add(countUnknown);

                    int countMale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_MALE);
                    countMbrMale.add(countMale);

                    int countFemale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_FEMALE);
                    countMbrFemale.add(countFemale);

                    thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                }
            } else if (20 < diffDay && diffDay <= 30) {
                thisStartQueryTime = queryStartTime;

                for(int i = 1; i < diffDay/3 + 1; i++){

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 2)); //查询结束时间
                    if(i%2 == 0){
                        xTitleArray.add(" ");
                    }else{
                        xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                    }

                    //查询性别统计数据
                    int countUnknown = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_UNKNOWN);
                    countMbrUnknown.add(countUnknown);

                    int countMale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_MALE);
                    countMbrMale.add(countMale);

                    int countFemale = rpcCommonService.rpcMemberService.countMemberBySex(thisStartQueryTime, thisEndQueryTime, queryMchId, MchConstant.SEX_FEMALE);
                    countMbrFemale.add(countFemale);

                    thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                }
            }

        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("countMbrUnknown", countMbrUnknown);
        resultMap.put("countMbrMale", countMbrMale);
        resultMap.put("countMbrFemale", countMbrFemale);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 会员活跃统计图表数据 **/
    @RequestMapping("/statisticsMbrActiveCharts")
    public XxPayResponse statisticsMbrActiveCharts() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //选择时间范围
        Date[] queryDate = getQueryDateRange();
        Date queryStartTime = queryDate[0];
        Date queryEndTime = queryDate[1];

        List<String> xTitleArray = new ArrayList<>();  //x轴标题
        List<Long> countMbrActive = new ArrayList<>();  //会员活跃数据

        //同一天,  2小时/时段
        int splitTime = 2;
        if(DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD).equals(DateUtil.date2Str(queryEndTime, DateUtil.FORMAT_YYYY_MM_DD))){

            String thisQueryDate = DateUtil.date2Str(queryStartTime, DateUtil.FORMAT_YYYY_MM_DD);

            String lastTime = "00:00";
            for(int i = 1; i <= (24/splitTime); i++){
                String thisTime = String.format("%02d", i*splitTime) + ":00";
                if(i%2 == 0){
                    xTitleArray.add(" ");
                }else{
                    xTitleArray.add(lastTime.substring(0, 2) + "~" + thisTime.substring(0, 2));
                }
                Date thisStartQueryTime =  DateUtil.str2date(thisQueryDate + " " + lastTime + ":00");
                Date thisEndQueryTime = DateUtil.str2date(thisQueryDate + " " + thisTime + ":00");

                //查询会员交易数据
                Long count = rpcCommonService.rpcMchTradeOrderService.countMemberActive(thisStartQueryTime, thisEndQueryTime, queryMchId);
                countMbrActive.add(count);

                lastTime = thisTime;
            }
        }else{//遍历日期
            //开始和结束时间 相差天数
            long diffDay = DateUtil.diffDay(queryStartTime, queryEndTime) + 1;
            if (diffDay > 30 || diffDay <= 0) return XxPayResponse.build(RetEnum.RET_MCH_DIFF_DAY);//最多三十天

            //查询开始时间
            Date thisStartQueryTime;
            //查询结束时间
            Date thisEndQueryTime;

            if (0 < diffDay && diffDay <= 10) {
                for(int i = 0; i < diffDay; i++){

                    thisStartQueryTime = DateUtils.addDays(queryStartTime, i); //时间偏移

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(thisStartQueryTime); //当天的结束时间
                    xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd"));

                    //查询会员交易数据
                    Long count = rpcCommonService.rpcMchTradeOrderService.countMemberActive(thisStartQueryTime, thisEndQueryTime, queryMchId);
                    countMbrActive.add(count);
                }
            } else if (10 < diffDay && diffDay <= 20) {
                thisStartQueryTime = queryStartTime;

                for(int i = 1; i < diffDay/2 + 1; i++){

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 1)); //查询结束时间
                    if(i%2 == 0){
                        xTitleArray.add(" ");
                    }else{
                        xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                    }

                    //查询会员交易数据
                    Long count = rpcCommonService.rpcMchTradeOrderService.countMemberActive(thisStartQueryTime, thisEndQueryTime, queryMchId);
                    countMbrActive.add(count);

                    thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                }
            } else if (20 < diffDay && diffDay <= 30) {
                thisStartQueryTime = queryStartTime;

                for(int i = 1; i < diffDay/3 + 1; i++){

                    if(thisStartQueryTime.compareTo(queryEndTime) >= 0){ //时间超过
                        break;
                    }
                    thisEndQueryTime = DateUtil.getEnd(DateUtils.addDays(thisStartQueryTime, 2)); //查询结束时间
                    if(i%2 == 0){
                        xTitleArray.add(" ");
                    }else{
                        xTitleArray.add(DateUtil.date2Str(thisStartQueryTime, "dd") + "~" + DateUtil.date2Str(thisEndQueryTime, "dd"));
                    }

                    //查询会员交易数据
                    Long count = rpcCommonService.rpcMchTradeOrderService.countMemberActive(thisStartQueryTime, thisEndQueryTime, queryMchId);
                    countMbrActive.add(count);

                    thisStartQueryTime = DateUtil.getBegin(DateUtil.addDay(thisEndQueryTime, 1));
                }
            }

        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xTitleArray", xTitleArray);
        resultMap.put("countMbrActive", countMbrActive);

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 员工统计 查询数据 **/
    @RequestMapping("/operatorStatistics")
    public XxPayResponse operatorStatistics() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //查询门店ID
        Long queryStoreId = getValLong("queryStoreId") == null ? getUser().getStoreId() : getValLong("queryStoreId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES){  //非商户本身， 仅查询当前门店， 否则支持切换门店
            queryStoreId = getUser().getStoreId();
        }

        //非商户查询当前操作员    或    商户可查询门店全部操作员
        List<SysUser> operatorList = null;

        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES || false){  //非商户本身 || //TODO 非店长， 仅查询当前操作员，否则支持切换
            operatorList.add(rpcCommonService.rpcSysService.getById(getUser().getUserId()));
        }else {   //商户本身，根据门店ID查操作员列表
             operatorList = rpcCommonService.rpcSysService.list(
                        new QueryWrapper<SysUser>().lambda()
                        .eq(SysUser::getStoreId, queryStoreId)
                        .eq(SysUser::getBelongInfoId, queryMchId)
                );
        }

        //选择时间范围
        Date[] queryDate = getQueryDateRange();

        //返回的统计数据
        List<Map> resultList = new LinkedList<>();

        for (SysUser sysUser : operatorList) {
            Long queryOperatorId = sysUser.getUserId();
            String operatorName = sysUser.getLoginUserName();

            //公共查询条件
            Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, null, queryMchId, queryStoreId, queryOperatorId, null, null, null);

            //单个操作员的统计数据
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("operatorId", queryOperatorId);
            resultMap.put("operatorName", operatorName);

            //总收款金额
            BigDecimal sumRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("sumRealAmount", sumRealAmount);

            //微信收款金额
            commonCondition.put("productType",  Byte.valueOf("3"));  //产品类型
            BigDecimal wxpayRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("wxpayRealAmount", wxpayRealAmount);

            //支付宝收款金额
            commonCondition.put("productType",  Byte.valueOf("4"));  //产品类型
            BigDecimal alipayRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("alipayRealAmount", alipayRealAmount);

            //云闪付收款金额
            commonCondition.put("productType",  Byte.valueOf("5"));  //产品类型
            BigDecimal unionpayRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("unionpayRealAmount", unionpayRealAmount);

            //会员支付收款金额
            commonCondition.put("productType",  Byte.valueOf("2"));  //产品类型
            BigDecimal mbrpayRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("mbrpayRealAmount", mbrpayRealAmount);

            //现金收款金额
            commonCondition.put("productType",  Byte.valueOf("1"));  //产品类型
            BigDecimal cashpayRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
            resultMap.put("cashpayRealAmount", cashpayRealAmount);

            resultList.add(resultMap);
        }

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


    // ======================================================开始纳呈支付统计 ==========================================================================

    /** 数据统计 查询数据 **/
    @RequestMapping("/nc/dataStatistics")
    public XxPayResponse dataStatisticsForNc() {

        //查询商户ID
        Long queryMchId = getUser().getBelongInfoId();

        //查询门店ID
        Long queryStoreId = null;
        /*
        Long queryStoreId = getValLong("queryStoreId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES){  //非商户本身， 仅查询当前门店， 否则支持切换门店
            queryStoreId = getUser().getStoreId();
        }
        */

        //查询操作员
        Long queryOperatorId = getValLong("queryOperatorId");
        if(getUser().getIsSuperAdmin() != MchConstant.PUB_YES || false){  //非商户本身 || //TODO 非店长， 仅查询当前操作员，否则支持切换
            queryOperatorId = getUser().getUserId();
        }

        //根据 产品类型查找
        Byte queryProductType = getValByte("queryProductType");

        //选择时间范围
        //Date[] queryDate = getQueryDateRange();

        String queryDateParamter = getValString("queryDate"); ////格式： yyyy-MM-dd

        //时间参数为空, 则查询当天的
        Date date = StringUtils.isBlank(queryDateParamter) ? new Date() : DateUtil.str2date(queryDateParamter, DateUtil.FORMAT_YYYY_MM_DD);

        //查询开始时间
        Date resultBeginDateTime = DateUtil.getBegin(date);
        //查询结束时间
        Date resultEndDateTime = DateUtil.getEnd(date);

        Date[] queryDate = new Date[]{resultBeginDateTime, resultEndDateTime};

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(queryDate[0], queryDate[1], null, null, null, queryMchId, queryStoreId, queryOperatorId, queryProductType, null, null);

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();

        //订单金额
        BigDecimal sumTradeAmount = rpcCommonService.rpcMchTradeOrderService.sumTradeAmount(commonCondition);
        resultMap.put("sumTradeAmount", sumTradeAmount);

        //实际收款
        BigDecimal sumRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
        resultMap.put("sumRealAmount", sumRealAmount);

        List<Map> productTypeAmtList= rpcCommonService.rpcMchTradeOrderService.countByGroupProductTypeForNc(commonCondition);
        if (CollectionUtils.isNotEmpty(productTypeAmtList)) {
            for (Map map : productTypeAmtList) {
                int productType = map.get("productType") != null ? Integer.valueOf(map.get("productType").toString()) : null;

                //微信支付方式
                if (productType == 3) {
                    BigDecimal wxSumRealAmount = map.get("totalAmount") != null ? new BigDecimal(map.get("totalAmount").toString()) : BigDecimal.ZERO;
                    BigDecimal wxCuntTrade = map.get("totalCount") != null ? new BigDecimal(map.get("totalCount").toString()) : BigDecimal.ZERO;
                    resultMap.put("wxSumRealAmount", wxSumRealAmount);
                    resultMap.put("wxCuntTrade", wxCuntTrade);
                }
                //支付宝支付方式
                if (productType == 4) {
                    BigDecimal aliPaySumRealAmount = map.get("totalAmount") != null ? new BigDecimal(map.get("totalAmount").toString()) : BigDecimal.ZERO;
                    BigDecimal aliPayCuntTrade = map.get("totalCount") != null ? new BigDecimal(map.get("totalCount").toString()) : BigDecimal.ZERO;
                    resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
                    resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
                }
            }
        }

        //退款金额
        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
        resultMap.put("sumRefundAmount", sumRefundAmount);


        //订单笔数
        Long countTrade = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
        resultMap.put("countTrade", countTrade);


        //退款笔数
        Long countRefund = rpcCommonService.rpcMchTradeOrderService.countRefund(commonCondition);
        resultMap.put("countRefund", countRefund);

        return XxPayResponse.buildSuccess(resultMap);
    }


    /** 按小时数据统计  **/
    @RequestMapping("/nc/hour/dataStatistics")
    public XxPayResponse dataStatisticsForNcHour() {

        String batchDate = getValStringRequired("batchDate");
        Long mchId = getValLong("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("batchDate", batchDate);
        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        List<MchTradeOrderBatchHour> batchHours = rpcCommonService.rpcMchTradeOrderBatchHourService.selectPayTrend(paramMap);

        if (CollectionUtils.isEmpty(batchHours)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_NO_DATA);
        }

        Map<String, MchTradeOrderBatchHour> tempMap = batchHours.stream().collect(Collectors.toMap(MchTradeOrderBatchHour::getHour, MchTradeOrderBatchHour -> MchTradeOrderBatchHour));
        List<Long> wxSumRealAmount = new ArrayList<Long>();   //微信收款集合
        List<Long> wxCuntTrade = new ArrayList<Long>();   //微信收款笔数集合
        List<Long> wxTradePrice = new ArrayList<Long>();   //微信收款单价集合
        List<Long> aliPaySumRealAmount = new ArrayList<Long>();   //支付宝收款集合
        List<Long> aliPayCuntTrade = new ArrayList<Long>();   //支付宝收款笔数集合
        List<Long> aliPayTradePrice = new ArrayList<Long>();   //支付宝收款单价集合
        MchTradeOrderBatchHour hour01 = tempMap.get("01");
        MchTradeOrderBatchHour hour02 = tempMap.get("02");
        MchTradeOrderBatchHour hour03 = tempMap.get("03");
        MchTradeOrderBatchHour hour04 = tempMap.get("04");
        MchTradeOrderBatchHour hour05 = tempMap.get("05");
        MchTradeOrderBatchHour hour06 = tempMap.get("06");
        MchTradeOrderBatchHour hour07 = tempMap.get("07");
        MchTradeOrderBatchHour hour08 = tempMap.get("08");
        MchTradeOrderBatchHour hour09 = tempMap.get("09");
        MchTradeOrderBatchHour hour10 = tempMap.get("10");
        MchTradeOrderBatchHour hour11 = tempMap.get("11");
        MchTradeOrderBatchHour hour12 = tempMap.get("12");
        MchTradeOrderBatchHour hour13 = tempMap.get("13");
        MchTradeOrderBatchHour hour14 = tempMap.get("14");
        MchTradeOrderBatchHour hour15 = tempMap.get("15");
        MchTradeOrderBatchHour hour16 = tempMap.get("16");
        MchTradeOrderBatchHour hour17 = tempMap.get("17");
        MchTradeOrderBatchHour hour18 = tempMap.get("18");
        MchTradeOrderBatchHour hour19 = tempMap.get("19");
        MchTradeOrderBatchHour hour20 = tempMap.get("20");
        MchTradeOrderBatchHour hour21 = tempMap.get("21");
        MchTradeOrderBatchHour hour22 = tempMap.get("22");
        MchTradeOrderBatchHour hour23 = tempMap.get("23");
        MchTradeOrderBatchHour hour24 = tempMap.get("24");

        //微信收款金额(1-24点)
        wxSumRealAmount.add(hour01.getWxSumRealAmount());
        wxSumRealAmount.add(hour02.getWxSumRealAmount());
        wxSumRealAmount.add(hour03.getWxSumRealAmount());
        wxSumRealAmount.add(hour04.getWxSumRealAmount());
        wxSumRealAmount.add(hour05.getWxSumRealAmount());
        wxSumRealAmount.add(hour06.getWxSumRealAmount());
        wxSumRealAmount.add(hour07.getWxSumRealAmount());
        wxSumRealAmount.add(hour08.getWxSumRealAmount());
        wxSumRealAmount.add(hour09.getWxSumRealAmount());
        wxSumRealAmount.add(hour10.getWxSumRealAmount());
        wxSumRealAmount.add(hour11.getWxSumRealAmount());
        wxSumRealAmount.add(hour12.getWxSumRealAmount());
        wxSumRealAmount.add(hour13.getWxSumRealAmount());
        wxSumRealAmount.add(hour14.getWxSumRealAmount());
        wxSumRealAmount.add(hour15.getWxSumRealAmount());
        wxSumRealAmount.add(hour16.getWxSumRealAmount());
        wxSumRealAmount.add(hour17.getWxSumRealAmount());
        wxSumRealAmount.add(hour18.getWxSumRealAmount());
        wxSumRealAmount.add(hour19.getWxSumRealAmount());
        wxSumRealAmount.add(hour20.getWxSumRealAmount());
        wxSumRealAmount.add(hour21.getWxSumRealAmount());
        wxSumRealAmount.add(hour22.getWxSumRealAmount());
        wxSumRealAmount.add(hour23.getWxSumRealAmount());
        wxSumRealAmount.add(hour24.getWxSumRealAmount());

        //微信收款笔数(1-24点)
        wxCuntTrade.add(hour01.getWxCuntTrade());
        wxCuntTrade.add(hour02.getWxCuntTrade());
        wxCuntTrade.add(hour03.getWxCuntTrade());
        wxCuntTrade.add(hour04.getWxCuntTrade());
        wxCuntTrade.add(hour05.getWxCuntTrade());
        wxCuntTrade.add(hour06.getWxCuntTrade());
        wxCuntTrade.add(hour07.getWxCuntTrade());
        wxCuntTrade.add(hour08.getWxCuntTrade());
        wxCuntTrade.add(hour09.getWxCuntTrade());
        wxCuntTrade.add(hour10.getWxCuntTrade());
        wxCuntTrade.add(hour11.getWxCuntTrade());
        wxCuntTrade.add(hour12.getWxCuntTrade());
        wxCuntTrade.add(hour13.getWxCuntTrade());
        wxCuntTrade.add(hour14.getWxCuntTrade());
        wxCuntTrade.add(hour15.getWxCuntTrade());
        wxCuntTrade.add(hour16.getWxCuntTrade());
        wxCuntTrade.add(hour17.getWxCuntTrade());
        wxCuntTrade.add(hour18.getWxCuntTrade());
        wxCuntTrade.add(hour19.getWxCuntTrade());
        wxCuntTrade.add(hour20.getWxCuntTrade());
        wxCuntTrade.add(hour21.getWxCuntTrade());
        wxCuntTrade.add(hour22.getWxCuntTrade());
        wxCuntTrade.add(hour23.getWxCuntTrade());
        wxCuntTrade.add(hour24.getWxCuntTrade());

        //微信收款单价(1-24点)
        wxTradePrice.add(calPrice(hour01.getWxSumRealAmount(), hour01.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour02.getWxSumRealAmount(), hour02.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour03.getWxSumRealAmount(), hour03.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour04.getWxSumRealAmount(), hour04.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour05.getWxSumRealAmount(), hour05.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour06.getWxSumRealAmount(), hour06.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour07.getWxSumRealAmount(), hour07.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour08.getWxSumRealAmount(), hour08.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour09.getWxSumRealAmount(), hour09.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour10.getWxSumRealAmount(), hour10.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour11.getWxSumRealAmount(), hour11.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour12.getWxSumRealAmount(), hour12.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour13.getWxSumRealAmount(), hour13.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour14.getWxSumRealAmount(), hour14.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour15.getWxSumRealAmount(), hour15.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour16.getWxSumRealAmount(), hour16.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour17.getWxSumRealAmount(), hour17.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour18.getWxSumRealAmount(), hour18.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour19.getWxSumRealAmount(), hour19.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour20.getWxSumRealAmount(), hour20.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour21.getWxSumRealAmount(), hour21.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour22.getWxSumRealAmount(), hour22.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour23.getWxSumRealAmount(), hour23.getWxCuntTrade()));
        wxTradePrice.add(calPrice(hour24.getWxSumRealAmount(), hour24.getWxCuntTrade()));

        //支付宝收款金额(1-24点)
        aliPaySumRealAmount.add(hour01.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour02.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour03.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour04.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour05.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour06.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour07.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour08.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour09.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour10.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour11.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour12.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour13.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour14.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour15.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour16.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour17.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour18.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour19.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour20.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour21.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour22.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour23.getAliPaySumRealAmount());
        aliPaySumRealAmount.add(hour24.getAliPaySumRealAmount());

        //支付宝收款笔数(1-24点)
        aliPayCuntTrade.add(hour01.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour02.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour03.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour04.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour05.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour06.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour07.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour08.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour09.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour10.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour11.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour12.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour13.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour14.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour15.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour16.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour17.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour18.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour19.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour20.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour21.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour22.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour23.getAliPayCuntTrade());
        aliPayCuntTrade.add(hour24.getAliPayCuntTrade());

        //支付宝收款单价(1-24点)
        aliPayTradePrice.add(calPrice(hour01.getAliPaySumRealAmount(), hour01.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour02.getAliPaySumRealAmount(), hour02.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour03.getAliPaySumRealAmount(), hour03.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour04.getAliPaySumRealAmount(), hour04.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour05.getAliPaySumRealAmount(), hour05.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour06.getAliPaySumRealAmount(), hour06.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour07.getAliPaySumRealAmount(), hour07.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour08.getAliPaySumRealAmount(), hour08.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour09.getAliPaySumRealAmount(), hour09.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour10.getAliPaySumRealAmount(), hour10.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour11.getAliPaySumRealAmount(), hour11.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour12.getAliPaySumRealAmount(), hour12.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour13.getAliPaySumRealAmount(), hour13.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour14.getAliPaySumRealAmount(), hour14.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour15.getAliPaySumRealAmount(), hour15.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour16.getAliPaySumRealAmount(), hour16.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour17.getAliPaySumRealAmount(), hour17.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour18.getAliPaySumRealAmount(), hour18.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour19.getAliPaySumRealAmount(), hour19.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour20.getAliPaySumRealAmount(), hour20.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour21.getAliPaySumRealAmount(), hour21.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour22.getAliPaySumRealAmount(), hour22.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour23.getAliPaySumRealAmount(), hour23.getAliPayCuntTrade()));
        aliPayTradePrice.add(calPrice(hour24.getAliPaySumRealAmount(), hour24.getAliPayCuntTrade()));

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put("hourArray", Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"));
        resultMap.put("wxSumRealAmount", wxSumRealAmount);
        resultMap.put("wxCuntTrade", wxCuntTrade);
        resultMap.put("wxTradePrice", wxTradePrice);
        resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
        resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
        resultMap.put("aliPayTradePrice", aliPayTradePrice);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 统计30天的数据趋势  **/
    @RequestMapping("/nc/day/dataStatistics")
    public XxPayResponse dataStatisticsForNcDay() {

        String payDate = getValStringRequired("payDate");
        Long mchId = getValLong("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payDate", payDate);
        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        List<MchTradeOrderBatch> batchDay = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrend(paramMap);

        if (CollectionUtils.isEmpty(batchDay)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_NO_DATA);
        }

        Map<String, MchTradeOrderBatch> tempMap = batchDay.stream().collect(Collectors.toMap(MchTradeOrderBatch::getBatchDate, MchTradeOrderBatch -> MchTradeOrderBatch));


        List<Long> wxSumRealAmount = new ArrayList<Long>();   //微信收款集合
        List<Long> wxCuntTrade = new ArrayList<Long>();   //微信收款笔数集合
        List<Long> wxTradePrice = new ArrayList<Long>();   //微信收款单价集合
        List<Long> aliPaySumRealAmount = new ArrayList<Long>();   //支付宝收款集合
        List<Long> aliPayCuntTrade = new ArrayList<Long>();   //支付宝收款笔数集合
        List<Long> aliPayTradePrice = new ArrayList<Long>();   //支付宝收款单价集合

        //获取从昨天开始的前30天集合
        List<String> payDates = listDay(payDate);

        if (CollectionUtils.isEmpty(payDates) || payDates.size() != 30) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_THIRT_DAY_ERR);
        }

        for (String day : payDates) {
            MchTradeOrderBatch dayBatch = tempMap.get(day);
            if (dayBatch == null) {
                wxSumRealAmount.add(0l);
                wxCuntTrade.add(0l);
                wxTradePrice.add(0l);
                aliPaySumRealAmount.add(0l);
                aliPayCuntTrade.add(0l);
                aliPayTradePrice.add(0l);
            }else {
                wxSumRealAmount.add(convertLong(dayBatch.getWxSumRealAmount()));
                wxCuntTrade.add(convertLong(dayBatch.getWxCuntTrade()));
                wxTradePrice.add(calPrice(convertLong(dayBatch.getWxSumRealAmount()), convertLong(dayBatch.getWxCuntTrade())));
                aliPaySumRealAmount.add(convertLong(dayBatch.getAliPaySumRealAmount()));
                aliPayCuntTrade.add(convertLong(dayBatch.getAliPayCuntTrade()));
                aliPayTradePrice.add(calPrice(convertLong(dayBatch.getAliPaySumRealAmount()), convertLong(dayBatch.getAliPayCuntTrade())));
            }
        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("dayArray", payDates);
        resultMap.put("wxSumRealAmount", wxSumRealAmount);
        resultMap.put("wxCuntTrade", wxCuntTrade);
        resultMap.put("wxTradePrice", wxTradePrice);
        resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
        resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
        resultMap.put("aliPayTradePrice", aliPayTradePrice);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 按天统计数据  **/
    @RequestMapping("/nc/day/dataTotal")
    public XxPayResponse dataTotalForNcDay() {

        String payDate = getValStringRequired("payDate");
        Long mchId = getValLong("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payDate", payDate);
        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        //指定日期的统计数据
        MchTradeOrderBatch dayTotal = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendByDay(paramMap);
        //往前推7天的统计数据
        paramMap.put("payDate", preDay(payDate, 7));
        MchTradeOrderBatch pre7DayTotal = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendByDay(paramMap);

        Long wxSumRealAmount = 0l;   //微信收款
        Long wxCuntTrade = 0l;   //微信收款笔数
        Long wxTradePrice = 0l;   //微信收款单价
        Long aliPaySumRealAmount = 0l;   //支付宝收款
        Long aliPayCuntTrade = 0l;   //支付宝收款笔数
        Long aliPayTradePrice = 0l;  //支付宝收款单价

        Long pre7DayWxSumRealAmount = 0l;   //微信收款(前7天)
        Long pre7DayWxCuntTrade = 0l;   //微信收款笔数(前7天)
        Long pre7DayWxTradePrice = 0l;   //微信收款单价(前7天)
        Long pre7DayAliPaySumRealAmount = 0l;   //支付宝收款(前7天)
        Long pre7DayAliPayCuntTrade = 0l;   //支付宝收款笔数(前7天)
        Long pre7DayAliPayTradePrice = 0l;  //支付宝收款单价(前7天)

        if (dayTotal != null) {
            wxSumRealAmount = dayTotal.getWxSumRealAmount();
            wxCuntTrade = dayTotal.getWxCuntTrade();
            wxTradePrice = calPrice(wxSumRealAmount, wxCuntTrade);
            aliPaySumRealAmount = dayTotal.getAliPaySumRealAmount();
            aliPayCuntTrade = dayTotal.getAliPayCuntTrade();
            aliPayTradePrice = calPrice(aliPaySumRealAmount, aliPayCuntTrade);
        }


        if (pre7DayTotal != null) {
            pre7DayWxSumRealAmount = pre7DayTotal.getWxSumRealAmount();
            pre7DayWxCuntTrade = pre7DayTotal.getWxCuntTrade();
            pre7DayWxTradePrice = calPrice(pre7DayWxSumRealAmount, pre7DayWxCuntTrade);
            pre7DayAliPaySumRealAmount = pre7DayTotal.getAliPaySumRealAmount();
            pre7DayAliPayCuntTrade = pre7DayTotal.getAliPayCuntTrade();
            pre7DayAliPayTradePrice = calPrice(pre7DayAliPaySumRealAmount, pre7DayAliPayCuntTrade);
        }


        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("wxSumRealAmount", wxSumRealAmount);
        resultMap.put("wxCuntTrade", wxCuntTrade);
        resultMap.put("wxTradePrice", wxTradePrice);
        resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
        resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
        resultMap.put("aliPayTradePrice", aliPayTradePrice);

        resultMap.put("pre7DayWxSumRealAmount", pre7DayWxSumRealAmount);
        resultMap.put("pre7DayWxCuntTrade", pre7DayWxCuntTrade);
        resultMap.put("pre7DayWxTradePrice", pre7DayWxTradePrice);
        resultMap.put("pre7DayAliPaySumRealAmount", pre7DayAliPaySumRealAmount);
        resultMap.put("pre7DayAliPayCuntTrade", pre7DayAliPayCuntTrade);
        resultMap.put("pre7DayAliPayTradePrice", pre7DayAliPayTradePrice);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 统计指定月份的每天一天数据  **/
    @RequestMapping("/nc/month/dataStatistics")
    public XxPayResponse dataStatisticsForNcMonth() {

        String payMonth = getValStringRequired("payMonth");
        Long mchId = getValLongRequired("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payMonth", payMonth);
        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        List<MchTradeOrderBatch> batchMonth = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendForMonth(paramMap);

        if (CollectionUtils.isEmpty(batchMonth)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_NO_DATA);
        }

        Map<String, MchTradeOrderBatch> tempMap = batchMonth.stream().collect(Collectors.toMap(MchTradeOrderBatch::getBatchDate, MchTradeOrderBatch -> MchTradeOrderBatch));


        List<Long> wxSumRealAmount = new ArrayList<Long>();   //微信收款集合
        List<Long> wxCuntTrade = new ArrayList<Long>();   //微信收款笔数集合
        List<Long> wxTradePrice = new ArrayList<Long>();   //微信收款单价集合
        List<Long> aliPaySumRealAmount = new ArrayList<Long>();   //支付宝收款集合
        List<Long> aliPayCuntTrade = new ArrayList<Long>();   //支付宝收款笔数集合
        List<Long> aliPayTradePrice = new ArrayList<Long>();   //支付宝收款单价集合

        //获取从昨天开始的前30天集合
        List<String> payDates = listDayForMonth(payMonth);

        if (CollectionUtils.isEmpty(payDates)) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_THIRT_MONTH_ERR);
        }

        for (String day : payDates) {
            MchTradeOrderBatch dayBatch = tempMap.get(day);
            if (dayBatch == null) {
                wxSumRealAmount.add(0l);
                wxCuntTrade.add(0l);
                wxTradePrice.add(0l);
                aliPaySumRealAmount.add(0l);
                aliPayCuntTrade.add(0l);
                aliPayTradePrice.add(0l);
            }else {
                wxSumRealAmount.add(convertLong(dayBatch.getWxSumRealAmount()));
                wxCuntTrade.add(convertLong(dayBatch.getWxCuntTrade()));
                wxTradePrice.add(calPrice(convertLong(dayBatch.getWxSumRealAmount()), convertLong(dayBatch.getWxCuntTrade())));
                aliPaySumRealAmount.add(convertLong(dayBatch.getAliPaySumRealAmount()));
                aliPayCuntTrade.add(convertLong(dayBatch.getAliPayCuntTrade()));
                aliPayTradePrice.add(calPrice(convertLong(dayBatch.getAliPaySumRealAmount()), convertLong(dayBatch.getAliPayCuntTrade())));
            }
        }

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //统计数据
        calMap(batchMonth, resultMap);
        resultMap.put("dayArray", payDates);
        resultMap.put("wxSumRealAmount", wxSumRealAmount);
        resultMap.put("wxCuntTrade", wxCuntTrade);
        resultMap.put("wxTradePrice", wxTradePrice);
        resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
        resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
        resultMap.put("aliPayTradePrice", aliPayTradePrice);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /** 按月统计数据  **/
    @RequestMapping("/nc/month/dataTotal")
    public XxPayResponse dataTotalForNcMonth() {

        String payMonth = getValStringRequired("payMonth");
        Long mchId = getValLong("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payMonth", payMonth);
        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        //指定日期的统计数据
        MchTradeOrderBatch monthTotal = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendByMonth(paramMap);
        //往前推7天的统计数据
        paramMap.put("payMonth", preMonth(payMonth, 1));
        MchTradeOrderBatch preMonthTotal = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendByMonth(paramMap);

        Long wxSumRealAmount = 0l;   //微信收款
        Long wxCuntTrade = 0l;   //微信收款笔数
        Long wxTradePrice = 0l;   //微信收款单价
        Long aliPaySumRealAmount = 0l;   //支付宝收款
        Long aliPayCuntTrade = 0l;   //支付宝收款笔数
        Long aliPayTradePrice = 0l;  //支付宝收款单价

        Long preMonthWxSumRealAmount = 0l;   //微信收款(前7天)
        Long preMonthWxCuntTrade = 0l;   //微信收款笔数(前7天)
        Long preMonthWxTradePrice = 0l;   //微信收款单价(前7天)
        Long preMonthAliPaySumRealAmount = 0l;   //支付宝收款(前7天)
        Long preMonthAliPayCuntTrade = 0l;   //支付宝收款笔数(前7天)
        Long preMonthAliPayTradePrice = 0l;  //支付宝收款单价(前7天)

        if (monthTotal != null) {
            wxSumRealAmount = monthTotal.getWxSumRealAmount();
            wxCuntTrade = monthTotal.getWxCuntTrade();
            wxTradePrice = calPrice(wxSumRealAmount, wxCuntTrade);
            aliPaySumRealAmount = monthTotal.getAliPaySumRealAmount();
            aliPayCuntTrade = monthTotal.getAliPayCuntTrade();
            aliPayTradePrice = calPrice(aliPaySumRealAmount, aliPayCuntTrade);
        }


        if (preMonthTotal != null) {
            preMonthWxSumRealAmount = preMonthTotal.getWxSumRealAmount();
            preMonthWxCuntTrade = preMonthTotal.getWxCuntTrade();
            preMonthWxTradePrice = calPrice(preMonthWxSumRealAmount, preMonthWxCuntTrade);
            preMonthAliPaySumRealAmount = preMonthTotal.getAliPaySumRealAmount();
            preMonthAliPayCuntTrade = preMonthTotal.getAliPayCuntTrade();
            preMonthAliPayTradePrice = calPrice(preMonthAliPaySumRealAmount, preMonthAliPayCuntTrade);
        }


        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("wxSumRealAmount", wxSumRealAmount);
        resultMap.put("wxCuntTrade", wxCuntTrade);
        resultMap.put("wxTradePrice", wxTradePrice);
        resultMap.put("aliPaySumRealAmount", aliPaySumRealAmount);
        resultMap.put("aliPayCuntTrade", aliPayCuntTrade);
        resultMap.put("aliPayTradePrice", aliPayTradePrice);

        resultMap.put("preMonthWxSumRealAmount", preMonthWxSumRealAmount);
        resultMap.put("preMonthWxCuntTrade", preMonthWxCuntTrade);
        resultMap.put("preMonthWxTradePrice", preMonthWxTradePrice);
        resultMap.put("preMonthAliPaySumRealAmount", preMonthAliPaySumRealAmount);
        resultMap.put("preMonthAliPayCuntTrade", preMonthAliPayCuntTrade);
        resultMap.put("preMonthAliPayTradePrice", preMonthAliPayTradePrice);

        return XxPayResponse.buildSuccess(resultMap);
    }

    /**  交易分析--日报  **/
    @RequestMapping("/nc/day/daily")
    public XxPayResponse dataTrendDailyPage() {

        Long mchId = getValLongRequired("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("offset", (getPageIndex() -1) * getPageSize());
        paramMap.put("limit", getPageSize());

        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        Long signHosptial = calSignHospital(mchId, mchInfo.getMiniRole());
        List<Map> resultMap = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendDailyPage(paramMap);
        if (resultMap != null && resultMap.size() > 0) {
            for (Map map : resultMap) {
                map.put("noTradeCounter", signHosptial - (map.get("hospitalIds") != null ? Long.valueOf(map.get("hospitalIds").toString()) : 0l));
            }
        }

        return XxPayResponse.buildSuccess(resultMap);
    }


    /**  交易分析--月报  **/
    @RequestMapping("/nc/month/daily")
    public XxPayResponse dataTrendMonthPage() {

        Long mchId = getValLongRequired("mchId");

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.build(RetEnum.RET_HIS_MCHID_REQUIRED);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("offset", (getPageIndex() -1) * getPageSize());
        paramMap.put("limit", getPageSize());

        if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_MCHCHANT_ADMIN) {
            paramMap.put("hospitalId", mchInfo.getHospitalId());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            paramMap.put("areaCode", mchInfo.getAreaCode());
        }else if (mchInfo.getMiniRole() == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            //运营平台管理员查看所有的
        }else {
            return XxPayResponse.build(RetEnum.RET_HIS_MCH_TRADE_ORDER_BATCH_VIEW_ROLE_ERR);
        }

        Long signHosptial = calSignHospital(mchId, mchInfo.getMiniRole());
        List<Map> resultMap = rpcCommonService.rpcMchTradeOrderBatchService.selectDataTrendMonthPage(paramMap);
        return XxPayResponse.buildSuccess(resultMap);
    }

    private void calMap(List<MchTradeOrderBatch> batchs, Map<String, Object> resultMap) {
        //最大微信收款金额对象
        MchTradeOrderBatch wxMaxBatch = batchs.stream().max(Comparator.comparing(MchTradeOrderBatch::getWxSumRealAmount)).get();
        //最小微信收款金额对象
        MchTradeOrderBatch wxMinBatch = batchs.stream().min(Comparator.comparing(MchTradeOrderBatch::getWxSumRealAmount)).get();
        //微信收款有效交易天数
        Long wxEffDays = batchs.stream().filter(c -> c.getWxSumRealAmount() > 0).count();
        //微信收款金额平均值
        double avgWxRealAmt = batchs.stream().mapToLong(c -> c.getWxSumRealAmount()).average().getAsDouble();
        Long avgWxAmount = new Double(avgWxRealAmt).longValue();
        Map<String, Object> wxMaxMap = new HashMap<String, Object>();
        wxMaxMap.put("wxDateMaxBatch", wxMaxBatch.getBatchDate());
        wxMaxMap.put("wxAmountMaxBatch", wxMaxBatch.getWxSumRealAmount());
        Map<String, Object> wxMinMap = new HashMap<String, Object>();
        wxMinMap.put("wxDateMinBatch", wxMaxBatch.getBatchDate());
        wxMinMap.put("wxAmountMinBatch", wxMinBatch.getWxSumRealAmount());
        resultMap.put("wxMaxBatch", wxMaxMap);
        resultMap.put("wxMinBatch", wxMinMap);
        resultMap.put("avgWxAmount", avgWxAmount);
        resultMap.put("wxEffDays", wxEffDays);

        //最大支付宝收款金额对象
        MchTradeOrderBatch aliPayMaxBatch = batchs.stream().max(Comparator.comparing(MchTradeOrderBatch::getAliPaySumRealAmount)).get();
        //最小支付宝收款金额对象
        MchTradeOrderBatch aliPayMinBatch = batchs.stream().min(Comparator.comparing(MchTradeOrderBatch::getAliPaySumRealAmount)).get();
        //支付宝有效交易天数(指定月份)
        Long aliPayEffDays = batchs.stream().filter(c -> c.getAliPaySumRealAmount() > 0).count();
        //支付宝收款金额平均值
        double avgAliPayAmt = batchs.stream().mapToLong(c -> c.getAliPaySumRealAmount()).average().getAsDouble();
        Long avgAliPayAmount = new Double(avgAliPayAmt).longValue();
        Map<String, Object> aliPayMaxMap = new HashMap<String, Object>();
        aliPayMaxMap.put("aliPayDateMaxBatch", aliPayMaxBatch.getBatchDate());
        aliPayMaxMap.put("aliPayAmountMaxBatch", aliPayMaxBatch.getAliPaySumRealAmount());
        Map<String, Object> aliPayMinMap = new HashMap<String, Object>();
        aliPayMinMap.put("aliPayDateMinBatch", aliPayMinBatch.getBatchDate());
        aliPayMinMap.put("aliPayAmountMinBatch", aliPayMinBatch.getAliPaySumRealAmount());
        resultMap.put("aliPayMaxBatch", aliPayMaxMap);
        resultMap.put("aliPayMinBatch", aliPayMinMap);
        resultMap.put("avgAliPayAmount", avgAliPayAmount);
        resultMap.put("aliPayEffDays", aliPayEffDays);
    }

    //计算单价
    private Long calPrice(Long sumRealAmount, Long tradeCnt) {
        if (sumRealAmount == null || sumRealAmount == 0 || tradeCnt == null || tradeCnt == 0l) {
            return 0l;
        }
        return BigDecimal.valueOf(sumRealAmount).divide(BigDecimal.valueOf(tradeCnt), 0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 获取从昨天开始的前30天日期
     * @param day
     * @return
     */
    private List<String> listDay(String day) {
        try {
            List<String> result = new ArrayList<String>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(day);
            Calendar calc =Calendar.getInstance();
            for(int i=0;i < 30; i++){
                calc.setTime(date);
                calc.add(calc.DATE, -(i+1));
                Date minDate = calc.getTime();
                result.add(sdf.format(minDate));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定月份的所有的日期
     * @param monthStr 格式： yyyy-MM
     * @return
     */
    private List<String> listDayForMonth(String monthStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            List<String> list = new ArrayList<String>();
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(monthStr));
            cal.set(Calendar.DATE, 1);

            int month = cal.get(Calendar.MONTH);
            while (cal.get(Calendar.MONTH) == month) {
                list.add(sf.format(cal.getTime()));
                cal.add(Calendar.DATE, 1);
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Long convertLong(Long input) {
        if (input == null) {
            return 0l;
        }
        return input;
    }

    /**
     * 获取指定日期的前几天日期
     * @param payDate
     * @param past
     * @return
     * @throws ParseException
     */
    private String preDay(String payDate, int past) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(payDate));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - past);
            return sdf.format(calendar.getTime());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定月份的前几个月
     * @param payMonth
     * @param past
     * @return
     * @throws ParseException
     */
    private String preMonth(String payMonth, int past) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date month = sdf.parse(payMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(month);
            calendar.add(Calendar.MONTH, - past);
            Date starDate = calendar.getTime();
            return sdf.format(starDate);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 计算签约管辖医院
     * @param mchId
     * @param role
     * @return
     */
    private Long calSignHospital(Long mchId, byte role) {
        LambdaQueryWrapper<MchInfo> lambdaQueryWrapperByMch = new QueryWrapper<MchInfo>().lambda();

        if (role == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            lambdaQueryWrapperByMch.eq(MchInfo::getParentId, mchId);
        }
        lambdaQueryWrapperByMch.isNotNull(MchInfo::getHospitalId);

        List<MchInfo> mchInfos = rpcCommonService.rpcMchInfoService.list(lambdaQueryWrapperByMch);

        //签约管辖医院
        List<String> hospitalIds = mchInfos.stream().map(MchInfo::getHospitalId).distinct().collect(Collectors.toList());
        return CollectionUtils.isEmpty(hospitalIds) ? 0l : hospitalIds.size();
    }


    public static void main(String[] args) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date month = sdf.parse("2021-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        calendar.add(Calendar.MONTH, - 1);
        Date starDate = calendar.getTime();
        System.out.println("============================" +  (2l - 3l));
    }

    // ======================================================结束纳呈支付统计 ==========================================================================

}
