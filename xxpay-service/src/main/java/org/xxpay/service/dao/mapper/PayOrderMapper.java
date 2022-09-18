package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayOrderExample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PayOrderMapper extends BaseMapper<PayOrder> {
    long countByExample(PayOrderExample example);

    int deleteByExample(PayOrderExample example);

    int deleteByPrimaryKey(String payOrderId);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    List<PayOrder> selectByExample(PayOrderExample example);

    PayOrder selectByPrimaryKey(String payOrderId);

    int updateByExampleSelective(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByExample(@Param("record") PayOrder record, @Param("example") PayOrderExample example);

    int updateByPrimaryKeySelective(PayOrder record);

    int updateByPrimaryKey(PayOrder record);

    // 金额求和
    long sumAmountByExample(PayOrderExample example);

    /**
     * 统计平台的收入情况
     * @param param
     * @return
     */
    Map count4Income(Map param);

    /**
     * 商户充值排行
     * @param param
     * @return
     */
    List<Map> count4MchTop(Map param);

    /**
     * 代理商充值排行
     * @param param
     * @return
     */
    /*List<Map> count4AgentTop(Map param);*/

    /**
     * 支付统计
     * @param param
     * @return
     */
    List<Map> count4Pay(Map param);

    /**
     * 支付产品统计
     * @param param
     * @return
     */
    List<Map> count4PayProduct(Map param);

    /**
     * 统计所有订单
     * @param param
     * @return
     */
    Map count4All(Map param);

    /**
     * 统计成功订单
     * @param param
     * @return
     */
    Map count4Success(Map param);

    /**
     * 统计未付订单
     * @param param
     * @return
     */
    Map count4Fail(Map param);

    List<Map> getAllChannel();

    List<Map> channelStatistics(Map param);

    List<Map> count4PaymentStatistics(Map param);

    List<Map> paymentStatistics(Map param);

    Map orderDayAmount(Map param);

    List<Map> orderProductSuccess();

    BigDecimal selectChannelRateByOrderId(String payOrderId);

}
