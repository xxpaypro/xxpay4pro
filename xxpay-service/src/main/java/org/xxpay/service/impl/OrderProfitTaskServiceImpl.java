package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.OrderProfitDetailMapper;
import org.xxpay.service.dao.mapper.OrderProfitTaskMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务商结算跑批任务表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-21
 */
@Service
public class OrderProfitTaskServiceImpl extends ServiceImpl<OrderProfitTaskMapper, OrderProfitTask> implements IOrderProfitTaskService {
    
    private static final MyLog logger = MyLog.getLog(OrderProfitTaskServiceImpl.class);
    
    @Autowired
    private IAgentInfoService agentInfoService;

    @Autowired
    private IOrderProfitDetailService orderProfitDetailService;

    @Autowired
    private IMchTradeOrderService mchTradeOrderService;

    @Autowired
    private IIsvSettConfigService isvSettConfigService;

    @Autowired
    private OrderProfitDetailMapper orderProfitDetailMapper;


    /** 执行结算任务 **/
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void calIsvAndAgentsProfit(IsvSettConfig isvSettConfig, Date nowDate0){

        Long isvId = isvSettConfig.getIsvId(); //isvId

        //1. 获取需查询的时间参数 和 结算快照
        Date selectDate = null;   //计算需查询的订单时间
        String settConfigSnapshot = "";  //服务商的结算快照

        if(MchConstant.ISV_SETT_DATE_TYPE_DAY == isvSettConfig.getSettDateType()){  //结算周期类型： 固定天数, 查询订单数据为 (now - T) 以前的数据
            selectDate = DateUtil.addDay(nowDate0, ( 0 - isvSettConfig.getSettSetDay() )); //时间分割点  00:00:00， 应查询该时间以前的数据（小于）
            settConfigSnapshot = "每"+isvSettConfig.getSettSetDay()+"天/结算, 分润计算数据为："+DateUtil.date2Str(selectDate)+" 之前未结算的订单.";

        }else if(MchConstant.ISV_SETT_DATE_TYPE_MONTH == isvSettConfig.getSettDateType()){ //结算周期类型： 指定月的如期， 应该以当前月的1号作为时间节点
            selectDate = DateUtil.str2date(DateUtil.date2Str(nowDate0, "yyyy-MM-01 HH:mm:ss"));
            settConfigSnapshot = "每月"+isvSettConfig.getSettSetDay()+"号结算, 分润计算数据为："+DateUtil.date2Str(selectDate)+" 之前未结算的订单.";
        }else{
            logger.error("服务商配置有误, isvId={}", isvId);  //配置有误
            return ;
        }

        //2. 查询该isv下所有的代理商集合 + 所有下级代理商， 并计算代理商的分润情况
        List<AgentInfo> agentList = agentInfoService.list(new LambdaQueryWrapper<AgentInfo>().eq(AgentInfo::getIsvId, isvId));

        for(AgentInfo agentInfo: agentList){

            Long currentAgentId = agentInfo.getAgentId();  //当前遍历的agentId
            logger.info("处理代理商[{}]的分润情况. ", currentAgentId);

            OrderProfitTask task = new OrderProfitTask();
            task.setIsvId(isvId);
            task.setAgentId(currentAgentId); //agentId
            task.setAgentPid(agentInfo.getPid()); //pid
            task.setAgentName(agentInfo.getAgentName());  //agentName
            task.setSettConfigSnapshot(settConfigSnapshot); //结算周期快照

            //查询所有交易数据(包含支付+退款)：
            queryAndFullCountData(isvId, MchConstant.INFO_TYPE_AGENT, currentAgentId, selectDate, "all", task);

            //查询微信统计数据(包含支付+退款)
            queryAndFullCountData(isvId, MchConstant.INFO_TYPE_AGENT, currentAgentId, selectDate, "wx", task);

            //查询支付宝统计数据(包含支付+退款)
            queryAndFullCountData(isvId, MchConstant.INFO_TYPE_AGENT, currentAgentId, selectDate, "alipay", task);

            //结算状态： 金额大于0 待结算， 否则无需结算
            task.setSettStatus(task.getAllTradeProfitAmount() > 0 ? MchConstant.ISV_SETT_STATUS_WAIT : MchConstant.ISV_SETT_STATUS_NO_NEED);

            //插入任务表
            this.save(task);

            //更新taskId 和 状态
            updateTaskIdAndStatus(isvId, MchConstant.INFO_TYPE_AGENT, currentAgentId, selectDate, task.getTaskId());
        }

        //3. 统计该服务商的分润情况
        OrderProfitTask isvTaskRecord = new OrderProfitTask();
        isvTaskRecord.setIsvId(isvId);
        isvTaskRecord.setSettConfigSnapshot(settConfigSnapshot); //结算周期快照
        isvTaskRecord.setSettStatus(MchConstant.ISV_SETT_STATUS_NO_NEED); //结算状态： 服务商默认 无需结算

        //查询所有交易数据(包含支付+退款)：
        queryAndFullCountData(isvId, MchConstant.INFO_TYPE_ISV, isvId, selectDate, "all", isvTaskRecord);

        //查询微信统计数据(包含支付+退款)
        queryAndFullCountData(isvId, MchConstant.INFO_TYPE_ISV, isvId, selectDate, "wx", isvTaskRecord);

        //查询支付宝统计数据(包含支付+退款)
        queryAndFullCountData(isvId, MchConstant.INFO_TYPE_ISV, isvId, selectDate, "alipay", isvTaskRecord);

        //插入任务表
        this.save(isvTaskRecord);

        //更新taskId 和 状态
        updateTaskIdAndStatus(isvId, MchConstant.INFO_TYPE_ISV, isvId, selectDate, isvTaskRecord.getTaskId());


        //更新订单的结算状态
        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setSettTaskStatus(MchConstant.PUB_YES);  //跑批状态; 已完成

        //update t_mch_trade_order set settTaskStatus = 1 where isvId = 1 and productId !=null and productId != '' and settTaskStatus = 0 and createTime < selectDate
        mchTradeOrderService.update(updateRecord, new UpdateWrapper<MchTradeOrder>().lambda()
        .eq(MchTradeOrder::getIsvId, isvId)
        .isNotNull(MchTradeOrder::getProductId).ne(MchTradeOrder::getProductId, "")
        .eq(MchTradeOrder::getSettTaskStatus, MchConstant.ISV_SETT_STATUS_WAIT)
        .lt(MchTradeOrder::getCreateTime, selectDate) );

        //更新当前服务商的结算配置信息 (更新下次结算周期)
        Date nextProfitDate = XXPayUtil.nextProfitTaskTime(isvSettConfig.getSettDateType(), isvSettConfig.getSettSetDay(), nowDate0);
        IsvSettConfig updateConfig = new IsvSettConfig();
        updateConfig.setIsvId(isvSettConfig.getIsvId());
        updateConfig.setNextSettDate(nextProfitDate);
        updateConfig.setPrevSettDate(new Date());  //上次结算日期，记录为当前时间
        isvSettConfigService.updateById(updateConfig);

    }

    @Override
    public boolean updateSettFinish(Long taskId, String settImgPath) {

        OrderProfitTask updateRecord = new OrderProfitTask();
        updateRecord.setSettStatus(MchConstant.ISV_SETT_STATUS_OK); //状态： 已结算
        updateRecord.setSettDate(new Date()); //结算时间： 当前时间
        updateRecord.setSettImg(settImgPath); //结算凭证

        //更新状态为已结算， 判断条件为 settStatus and taskId
       return this.update(updateRecord, new UpdateWrapper<OrderProfitTask>().lambda()
                .eq(OrderProfitTask::getTaskId, taskId).eq(OrderProfitTask::getSettStatus, MchConstant.ISV_SETT_STATUS_WAIT));
    }

    /** 更新OrderProfitDetail 表中的taskId 和 TaskStatus **/
    private boolean updateTaskIdAndStatus(Long isvId, Byte referInfoType, Long referInfoId, Date selectDate, Long taskId){

        //更新taskId
        OrderProfitDetail updateRecord = new OrderProfitDetail();
        updateRecord.setSettTaskStatus(MchConstant.ISV_SETT_STATUS_OK); //结算任务状态： 已跑批完成
        updateRecord.setSettTaskId(taskId);

        UpdateWrapper<OrderProfitDetail> updateCondition = new UpdateWrapper<>();
        updateCondition.lambda()
                .eq(OrderProfitDetail::getIsvId, isvId)
                .lt(OrderProfitDetail::getBizOrderCreateDate, selectDate)
                .eq(OrderProfitDetail::getSettTaskStatus, MchConstant.ISV_SETT_STATUS_WAIT)
                .eq(OrderProfitDetail::getReferInfoType, referInfoType)
                .eq(OrderProfitDetail::getReferInfoId, referInfoId);
        return orderProfitDetailService.update(updateRecord, updateCondition);
    }


    /** 查询数据并赋值到对象中 **/
    private void queryAndFullCountData(Long isvId, Byte referInfoType, Long referInfoId, Date selectDate, String dataType, OrderProfitTask fullRecord){

        Map<String, Object> queryCountData = null;  //待查询的数据结果集
        BigDecimal profitAmount = null;
        if("all".equals(dataType)){
            queryCountData = orderProfitDetailMapper.selectCountTradeData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, null);

            profitAmount = orderProfitDetailMapper.selectSumProfitData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, null);

        }else if("wx".equals(dataType)){

            queryCountData = orderProfitDetailMapper.selectCountTradeData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, Arrays.asList(PayConstant.PAY_PRODUCT_WX_JSAPI, PayConstant.PAY_PRODUCT_WX_BAR));

            profitAmount = orderProfitDetailMapper.selectSumProfitData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, Arrays.asList(PayConstant.PAY_PRODUCT_WX_JSAPI, PayConstant.PAY_PRODUCT_WX_BAR));

        }else if("alipay".equals(dataType)){

            queryCountData = orderProfitDetailMapper.selectCountTradeData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, Arrays.asList(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI, PayConstant.PAY_PRODUCT_ALIPAY_BAR));

            profitAmount = orderProfitDetailMapper.selectSumProfitData(
                    isvId, referInfoId, referInfoType, selectDate, MchConstant.PUB_NO, Arrays.asList(PayConstant.PAY_PRODUCT_ALIPAY_JSAPI, PayConstant.PAY_PRODUCT_ALIPAY_BAR));
        }

        //转换为JSON格式
        JSONObject countDataJSON = (JSONObject) JSONObject.toJSON(queryCountData);
        Long totalCount = countDataJSON.getLong("totalCount");      //所有交易总数量
        Long totalPayAmount = countDataJSON.getBigDecimal("totalPayAmount").longValue();  //交易总金额
        Date minCreateDate = countDataJSON.getDate("minCreateDate"); //交易最早时间
        Date maxCreateDate = countDataJSON.getDate("maxCreateDate"); //交易最晚时间
        Long totalProfit = profitAmount.longValue();  //待结算金额

        if("all".equals(dataType)){
            fullRecord.setAllTradeCount(totalCount);  //所有交易总数量
            fullRecord.setAllTradeAmount(totalPayAmount); //交易总金额
            fullRecord.setAllTradeProfitAmount(totalProfit); //待结算金额
            fullRecord.setTradeFirstDate(minCreateDate); //交易最早时间
            fullRecord.setTradeLastDate(maxCreateDate); //交易最晚时间
        }else if("wx".equals(dataType)){

            fullRecord.setWxTradeCount(totalCount);  //所有交易总数量
            fullRecord.setWxTradeAmount(totalPayAmount); //交易总金额
            fullRecord.setWxTradeProfitAmount(totalProfit); //待结算金额

        }else if("alipay".equals(dataType)){
            fullRecord.setAlipayTradeCount(totalCount);  //所有交易总数量
            fullRecord.setAlipayTradeAmount(totalPayAmount); //交易总金额
            fullRecord.setAlipayTradeProfitAmount(totalProfit); //待结算金额
        }
    }

}
