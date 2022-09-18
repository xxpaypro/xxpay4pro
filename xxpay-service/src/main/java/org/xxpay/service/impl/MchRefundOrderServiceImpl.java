package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MchRefundOrderMapper;
import org.xxpay.service.dao.mapper.MchTradeOrderMapper;
import org.xxpay.service.dao.mapper.OrderProfitDetailMapper;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 商户退款表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MchRefundOrderServiceImpl extends ServiceImpl<MchRefundOrderMapper, MchRefundOrder> implements IMchRefundOrderService {

    private static final MyLog logger = MyLog.getLog(MchRefundOrderServiceImpl.class);


    @Autowired
    private IMemberPointsHistoryService memberPointsHistoryService;

    @Autowired
    private IMemberPointsService memberPointsService;

    @Autowired
    private IMemberBalanceService memberBalanceService;

    @Autowired
    private IMchTradeOrderService mchTradeOrderService;

    @Autowired
    private IOrderProfitDetailService orderProfitDetailService;

    @Autowired
    private IAgentInfoService agentInfoService;

    @Autowired
    private OrderProfitDetailMapper orderProfitDetailMapper;

    @Autowired
    private MchTradeOrderMapper mchTradeOrderMapper;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void refundSuccess(MchRefundOrder mchRefundOrder, MchTradeOrder mchTradeOrder){

        String tradeOrderId = mchTradeOrder.getTradeOrderId();       //交易订单号
        Long refundAmount = mchRefundOrder.getRefundAmount();    //退款金额
        Long payAmount = mchTradeOrder.getAmount();    //用户支付金额


        //1. 更新商户退款订单为[退款成功] 状态
        MchRefundOrder updateRefundOrder = new MchRefundOrder();
        updateRefundOrder.setRefundSuccTime(new Date());
        updateRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_SUCCESS); //更新退款单为退款成功状态

        boolean isSuccess = this.update(updateRefundOrder, new UpdateWrapper<MchRefundOrder>().lambda()
        .eq(MchRefundOrder::getMchRefundOrderId, mchRefundOrder.getMchRefundOrderId())
        .eq(MchRefundOrder::getStatus, MchConstant.MCH_REFUND_STATUS_ING));

        if(!isSuccess){
            logger.error("更新退款订单失败, mchRefundOrderId={}", mchRefundOrder.getMchRefundOrderId());
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL); //更新失败
        }

        Long subGivePoints = 0L; //总退还积分

        /**
         *  由于纳呈支付不涉及会员，所以暂时屏蔽掉会员处理
         *
        //2. 处理会员相关信息
        if(mchTradeOrder.getMemberId() != null){

            Long memberId = mchTradeOrder.getMemberId();

            //查询是否赠送积分
            MemberPointsHistory memberPointsHistory = memberPointsHistoryService.getOne(
                    new QueryWrapper<MemberPointsHistory>().lambda()
                            .eq(MemberPointsHistory::getMemberId, memberId)
                            .in(MemberPointsHistory::getBizType, MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_RECHARGE, MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_CONSUMPTION)
                            .eq(MemberPointsHistory::getBizOrderId, tradeOrderId));
            if(memberPointsHistory != null){

                //TODO 计算 需要扣减的积分   公式（退款金额 / 消费金额  * 赠送积分 ）
                Long givePoints = memberPointsHistory.getChangePoints(); //消费/充值 赠送积分
                subGivePoints = new BigDecimal(refundAmount).divide(new BigDecimal(payAmount)).multiply(new BigDecimal(givePoints))
                        .setScale(0, BigDecimal.ROUND_HALF_UP).longValue();  //四舍五入模式

                memberPointsService.refundPoints(memberId, subGivePoints, mchRefundOrder.getMchRefundOrderId());
            }

            //2.2 如果会员卡支付, 需要将金额回充
            if(mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD){
                memberBalanceService.refundAmount(memberId, refundAmount, mchRefundOrder.getMchRefundOrderId(), MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_REFUND);
            }
        }
        */

        //3 更新交易表的状态为退款状态（部分, 全额）
        Long updateRow = mchTradeOrderMapper.updateRefundStatusAndAddTotalAmount(mchTradeOrder.getTradeOrderId(), mchRefundOrder.getRefundAmount(), subGivePoints);
        if(updateRow <= 0){
            logger.error("更新交易为退款状态失败！ tradeOrderId={}", mchTradeOrder.getTradeOrderId());
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        /**
         *  由于纳呈支付不涉及分润和积分, 所以暂时屏蔽掉不使用
         *
        //3. 处理订单退款相关分润数据 (微信、支付宝)
        if(mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_WX ||
                mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_ALIPAY){
            insertOrderProfitDetailByRefund(mchRefundOrder);
        }
         */

    }

    /** 插入订单分润数据（退款） */
    private void insertOrderProfitDetailByRefund(MchRefundOrder mchRefundOrder){

        //查询最新订单数据
        MchTradeOrder dbTradeOrder = mchTradeOrderService.findByTradeOrderId(mchRefundOrder.getTradeOrderId());

        //获取基础信息
        String tradeOrderId = mchRefundOrder.getTradeOrderId();
        Long mchId = mchRefundOrder.getMchId();
        Long isvId = dbTradeOrder.getIsvId();


        //查询下单时的费率快照 （根据分润表数据）
        BigDecimal mchFeeRate = null;
        BigDecimal isvFeeRate = null;
        List<AgentInfo> agentProfitRateList = new ArrayList<>();

        //查询该订单的分润统计情况
        List<OrderProfitDetail> allProfit = orderProfitDetailService.list(new QueryWrapper<OrderProfitDetail>()
                .lambda().eq(OrderProfitDetail::getBizOrderId, tradeOrderId)
                .in(OrderProfitDetail::getBizType, MchConstant.PROFIT_BIZ_TYPE_PAY, MchConstant.PROFIT_BIZ_TYPE_RECHARGE)
        );

        Map<Long, BigDecimal> agentProfitRateMap = new HashMap<>();
        for (OrderProfitDetail detail : allProfit) {
            BigDecimal currentRate = new BigDecimal(detail.getFeeRateSnapshot().replace("%", ""));

            if(detail.getReferInfoType() == MchConstant.INFO_TYPE_MCH){
                mchFeeRate = currentRate;

            }
            else if(detail.getReferInfoType() == MchConstant.INFO_TYPE_ISV){
                isvFeeRate = currentRate;

            }else{
                agentProfitRateMap.put(detail.getReferInfoId(), currentRate);

            }
        }

        if(!agentProfitRateMap.isEmpty()){  //包含代理商

            // select AgentId from t_agent_info where agentId in () order by agentLevel desc;
            agentProfitRateList = agentInfoService.list(new LambdaQueryWrapper<AgentInfo>().
                    select(AgentInfo::getAgentId).in(AgentInfo::getAgentId, agentProfitRateMap.keySet()).orderByDesc(AgentInfo::getAgentLevel)
            );
            agentProfitRateList.stream().forEach(ai -> ai.setProfitRate(agentProfitRateMap.get(ai.getAgentId())));
        }

        //重新计算分润逻辑；
        Map<String, OrderProfitDetail> recalculateDetails = orderProfitDetailService.recalculateTradeOrderProfit(dbTradeOrder, mchFeeRate, isvFeeRate, agentProfitRateList);

        //最新的分润情况 (商户)
        OrderProfitDetail mchDetail = recalculateDetails.get(MchConstant.INFO_TYPE_MCH + "_" + mchId);
        mchDetail.setBizOrderId(mchRefundOrder.getMchRefundOrderId()); //退款订单号
        mchDetail.setBizType(MchConstant.PROFIT_BIZ_TYPE_REFUND); //类型为退款
        mchDetail.setBizOrderCreateDate(mchRefundOrder.getCreateTime()); //退款时间

        // 入账总金额 + X = 重新计算的金额;  求X
        OrderProfitDetail sumMchDetail = orderProfitDetailMapper.selectSumAmountByTradeOrder(mchId, MchConstant.INFO_TYPE_MCH, tradeOrderId);
        mchDetail.setIncomeAmount(mchDetail.getIncomeAmount() - sumMchDetail.getIncomeAmount());
        mchDetail.setFeeAmount(mchDetail.getFeeAmount() - sumMchDetail.getFeeAmount());

        orderProfitDetailService.insert(mchDetail);

        //最新的分润情况 (服务商)
        OrderProfitDetail isvDetail = recalculateDetails.get(MchConstant.INFO_TYPE_ISV + "_" + isvId);
        isvDetail.setBizOrderId(mchRefundOrder.getMchRefundOrderId()); //退款订单号
        isvDetail.setBizType(MchConstant.PROFIT_BIZ_TYPE_REFUND); //类型为退款
        isvDetail.setBizOrderCreateDate(mchRefundOrder.getCreateTime()); //退款时间

        // 入账总金额 + X = 重新计算的金额;  求X
        OrderProfitDetail sumIsvDetail = orderProfitDetailMapper.selectSumAmountByTradeOrder(isvId, MchConstant.INFO_TYPE_ISV, tradeOrderId);
        isvDetail.setIncomeAmount(isvDetail.getIncomeAmount() - sumIsvDetail.getIncomeAmount());
        isvDetail.setFeeAmount(isvDetail.getFeeAmount() - sumIsvDetail.getFeeAmount());

        orderProfitDetailService.insert(isvDetail);


        for(AgentInfo agentInfo : agentProfitRateList){

            //最新的分润情况 (代理商)
            OrderProfitDetail agentDetail = recalculateDetails.get(MchConstant.INFO_TYPE_AGENT + "_" + agentInfo.getAgentId());
            agentDetail.setBizOrderId(mchRefundOrder.getMchRefundOrderId()); //退款订单号
            agentDetail.setBizType(MchConstant.PROFIT_BIZ_TYPE_REFUND); //类型为退款
            agentDetail.setBizOrderCreateDate(mchRefundOrder.getCreateTime()); //退款时间

            // 入账总金额 + X = 重新计算的金额;  求X
            OrderProfitDetail sumAgentDetail = orderProfitDetailMapper.selectSumAmountByTradeOrder(agentInfo.getAgentId(), MchConstant.INFO_TYPE_AGENT, tradeOrderId);
            agentDetail.setIncomeAmount(agentDetail.getIncomeAmount() - sumAgentDetail.getIncomeAmount());
            agentDetail.setFeeAmount(agentDetail.getFeeAmount() - sumAgentDetail.getFeeAmount());

            orderProfitDetailService.insert(agentDetail);
        }
    }



    /** 更新退款单为退款失败  **/
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void refundFail(String mchRefundId){


        //1. 更新商户退款订单为[退款失败] 状态
        MchRefundOrder updateRefundOrder = new MchRefundOrder();
        updateRefundOrder.setRefundSuccTime(new Date());
        updateRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_FAIL); //更新退款单为退款失败

        boolean isSuccess = this.update(updateRefundOrder, new UpdateWrapper<MchRefundOrder>().lambda()
                .eq(MchRefundOrder::getMchRefundOrderId, mchRefundId)
                .eq(MchRefundOrder::getStatus, MchConstant.MCH_REFUND_STATUS_ING));

        if(!isSuccess){
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

}
