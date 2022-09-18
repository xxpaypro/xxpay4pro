package org.xxpay.core.service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.common.callback.AddTradeOrderCallBack;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.MchTradeOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
public interface IMchTradeOrderService extends IService<MchTradeOrder> {

    List<MchTradeOrder> select(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    /**  纳呈支付修改  **/
    List<MchTradeOrder> selectPlus(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    List<MchTradeOrder> selectByExampleWithBLOBs(int pageIndex, int pageSize, MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    Long count(MchTradeOrder mchTradeOrder, Date createTimeStart, Date createTimeEnd);

    int add(MchTradeOrder mchTradeOrder);

    MchTradeOrder findByTradeOrderId(String tradeOrderId);

    MchTradeOrder findByMchIdAndTradeOrderId(Long mchId, String tradeOrderId);

    MchTradeOrder findByMchIdAndPayOrderId(Long mchId, String payOrderId);

    int updateStatus4Ing(String tradeOrderId);

    int updateStatus4Success(String tradeOrderId, Long income, Long givePoints);

    int updateStatus4Success(String tradeOrderId, String payOrderId, Long income, Long givePoints);

    int updateSuccess4Member(String tradeOrderId, String payOrderId, Long income, String openId);

    int updateSuccess4MemberBalance(String tradeOrderId, Long memberId, String couponNo);

    int updateSuccess4Cod(String tradeOrderId, Long memberId, String couponNo);

    int updateStatus4Fail(String tradeOrderId);

    int update(MchTradeOrder mchTradeOrder);

    Map count4All(Long mchId, Long storeId, String tradeOrderId, String payOrderId, Byte tradeType, Byte status, String createTimeStart, String createTimeEnd);

    int updateSuccess4MemberRecharge(String tradeOrderId, String payOrderId, Long income);

    /** 生成tradeOrder ,并返回tradeOrderId  **/
    MchTradeOrder insertTradeOrder(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                            Byte tradeProductType, Integer productId, String ip, Long operatorId, Long storeId, AddTradeOrderCallBack callBack);

    MchTradeOrder insertTradeOrderHis(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                                             Byte tradeProductType, Integer productId, String ip, String mchOrderNo,
                                             Long operatorId, Long storeId, AddTradeOrderCallBack callback);

    /** 自动获取JWT信息 **/
    MchTradeOrder insertTradeOrderJWT(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount,
                                      Byte tradeProductType, Integer productId, String ip, AddTradeOrderCallBack callBack);

    /** 小程序下单 **/
    MchTradeOrder insertTradeOrderMiniProgram(MchInfo mchInfo, Byte tradeType, Long requiredAmount, Long realAmount, Byte postType, Long postPrice,
                                              Byte tradeProductType, Integer productId, String ip, Long storeId, Long addressId, Long storeAreaId, String goodsDesc, AddTradeOrderCallBack callBack);

    /** 调起上游支付接口 **/
    JSONObject callPayInterface(MchTradeOrder mchTradeOrder, MchInfo mchInfo, String payUrl, String notifyUrl);


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
	
	 /** 会员交易总数 **/
    Long countMemberActive(Date thisStartQueryTime, Date thisEndQueryTime, Long queryMchId);


    /** 根据服务商设备信息查询统计信息
     * key=deviceNo,
     * val index0=订单数量; index1=订单金额
     * **/
    Map<String, Long[]> selectByIsvDevice(Long isvId, List<String> isvDeviceNoList);


    /** 押金消费完成  **/
    void depositConsumeFinish(String mchTradeOrderId, String payOrderId, Long consumeAmount);

    /** 撤销订单  **/
    void depositReverse(String mchTradeOrderId, String payOrderId);

    /** 会员消费金额 **/
    BigDecimal sumMbrAmount(Map commonCondition);

    /** 会员退款金额 **/
    BigDecimal sumMbrRefundAmount(Map commonCondition);

    /** 会员消费笔数 **/
    Long countMbrTrade(Map commonCondition);

    /** 会员充值笔数 **/
    Long countMbrRecharge(Map commonCondition);

    /** 会员退款笔数 **/
    Long countMbrRefund(Map commonCondition);

    /** 根据会员和订单状态查询某小程序订单数量 **/
    int countByMemberAndStatus(Long memberId, Byte authFrom, Byte status, Byte postStatus);



    //===================================================================开始纳呈支付新增=====================================================================

    List<Map> countByGroupProductTypeForNc(Map condition);

    /**  根据商户ID统计产生交易的商户数 **/
    Long countMchForTrade(List<Long> mchIds);

    //===================================================================结束纳呈支付新增=====================================================================
}


