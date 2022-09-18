package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.MchTradeOrderExample;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MchTradeOrderMapper extends BaseMapper<MchTradeOrder> {
    long countByExample(MchTradeOrderExample example);

    int deleteByExample(MchTradeOrderExample example);

    int deleteByPrimaryKey(String tradeOrderId);

    int insert(MchTradeOrder record);

    int insertSelective(MchTradeOrder record);

    List<MchTradeOrder> selectByExample(MchTradeOrderExample example);

    /**  纳呈支付修改  **/
    List<MchTradeOrder> selectPlusByExample(MchTradeOrderExample example);

    List<MchTradeOrder> selectByExampleWithBLOBs(MchTradeOrderExample example);

    MchTradeOrder selectByPrimaryKey(String tradeOrderId);

    int updateByExampleSelective(@Param("record") MchTradeOrder record, @Param("example") MchTradeOrderExample example);

    int updateByExample(@Param("record") MchTradeOrder record, @Param("example") MchTradeOrderExample example);

    int updateByPrimaryKeySelective(MchTradeOrder record);

    int updateByPrimaryKey(MchTradeOrder record);

    /**
     * 统计所有订单
     * @param param
     * @return
     */
    Map count4All(Map param);

    /** 更新订单状态为 退款， 并累加退款金额, 减少赠送积分 **/
    Long updateRefundStatusAndAddTotalAmount(@Param("tradeOrderId") String tradeOrderId, @Param("refundAmount") Long refundAmount, @Param("subPoints") Long subPoints);


    /** 总交易额 **/
    BigDecimal sumTradeAmount(Map condition);

    /** 实收金额 **/
    BigDecimal sumRealAmount(Map condition);

    /** 会员消费金额 **/
    BigDecimal sumMemberAmount(Map condition);

    /** 非会员消费金额 **/
    BigDecimal sumNotMemberAmount(Map condition);

    /** 优惠金额 **/
    BigDecimal sumDiscountAmount(Map condition);

    /** 退款金额 **/
    BigDecimal sumRefundAmount(Map condition);

    /** 会员充值金额 **/
    BigDecimal sumRechargeAmount(Map condition);

    /** 赠送积分 **/
    BigDecimal sumGivePoints(Map condition);

    /** 充值赠送金额 **/
    BigDecimal sumRechargeGiveAmount(Map condition);


    /** 交易笔数 **/
    Long countTrade(Map condition);

    /** 退款笔数 **/
    Long countRefund(Map condition);

    /** 充值笔数 **/
    Long countRecharge(Map condition);

    /** 统计支付方式 **/
    List<Map> countByGroupProductType(Map condition);

    /** 统计会员支付订单数据 / 非会员支付订单数据 **/
    List<Integer> countTradeOrderByMember(Map condition);

    /** 会员交易笔数 **/
    Long countMemberActive(@Param("beginTime") Date beginTime,
                           @Param("endTime") Date endTime, @Param("mchId") Long mchId);


    /** 根据服务商设备信息查询统计信息 **/
    List<Map> selectByIsvDevice(@Param("isvId") Long isvId, @Param("isvDeviceNoList") List<String> isvDeviceNoList);

    /** 会员消费金额 **/
    BigDecimal sumMbrAmount(Map condition);

    /** 会员退款金额 **/
    BigDecimal sumMbrRefundAmount(Map condition);

    /** 会员消费笔数 **/
    Long countMbrTrade(Map condition);

    /** 会员充值笔数 **/
    Long countMbrRecharge(Map condition);

    /** 会员退款笔数 **/
    Long countMbrRefund(Map condition);

    //================================================开始新增修改纳呈支付=============================================================

    /** 统计支付方式 **/
    List<Map> countByGroupProductTypeForNc(Map condition);

    /** 根据商户ID统计产生交易的商户数 **/
    Long countMchForTrade(@Param("mchIds") List<Long> mchIds);

    //================================================结束新增修改纳呈支付=============================================================
}
