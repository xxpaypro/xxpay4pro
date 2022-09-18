package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.PayOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/9/8
 * @description:
 */
public interface IPayOrderService extends IService<PayOrder> {

    PayOrder find(PayOrder payOrder);

    PayOrder findByPayOrderId(String payOrderId);

    PayOrder findByMchIdAndPayOrderId(Long mchId, String payOrderId);

    PayOrder findByMchOrderNo(String mchOrderNo);

    List<PayOrder> select(Long mchId, int offset, int limit, PayOrder payOrder, Date createTimeStart, Date createTimeEnd);

    List<PayOrder> select(int offset, int limit, PayOrder payOrder, Date createTimeStart, Date createTimeEnd);

    Long count(Long mchId, PayOrder payOrder, Date createTimeStart, Date createTimeEnd);

    Long count(PayOrder payOrder, Date createTimeStart, Date createTimeEnd);

    int updateByPayOrderId(String payOrderId, PayOrder payOrder);

    Long sumAmount(PayOrder payOrder, List<Byte> statusList);

    List<PayOrder> select(String channelMchId, String billDate, List<Byte> statusList);

    int updateStatus4Ing(String payOrderId, String channelOrderNo);

    int updateStatus4Success(String payOrderId);

    int updateStatus4Fail(String payOrderId);

    int updateStatus4Success(String payOrderId, String channelOrderNo);

    int updateStatus4Success(String payOrderId, String channelOrderNo, String channelAttach);

    int createPayOrder(PayOrder payOrder);

    int closePayOrder(String payOrderId);

    PayOrder selectPayOrder(String payOrderId);

    PayOrder selectByMchIdAndPayOrderId(Long mchId, String payOrderId);

    PayOrder selectByMchIdAndMchOrderNo(Long mchId, String mchOrderNo);

    // 查询订单数据(用于生成商户对账文件使用)
    List<PayOrder> selectAllBill(int offset, int limit, String billDate);

    /**
     * 收入统计
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Map count4Income(Long agentId, Long mchId, Byte productType, String createTimeStart, String createTimeEnd);

    /**
     * 商户排名统计
     * @param agentId
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    List<Map> count4MchTop(Long agentId, Long mchId, Byte productType, String createTimeStart, String createTimeEnd);

    /**
     * 代理商排名统计
     * @param createTimeStart
     * @param createTimeEnd
     * @param createTimeEnd2 
     * @param createTimeStart2 
     * @return
     */
    /*List<Map> count4AgentTop(String agentId, String bizType, String createTimeStart, String createTimeEnd);*/

    /**
     * 支付统计(idName可以为passageId,productId,channelType,channelId)
     * @param idName
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    List<Map> count4Pay(String idName, String createTimeStart, String createTimeEnd);

    List<Map> count4PayProduct(String createTimeStart, String createTimeEnd);

    /**
     * 得到某个子账户的交易金额
     * @param payPassageAccountId
     * @param creatTimeStart
     * @param createTimeEnd
     * @return
     */
    Long sumAmount4PayPassageAccount(int payPassageAccountId, Date creatTimeStart, Date createTimeEnd);

    Map count4All(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd);

    Map count4Success(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd);

    Map count4Fail(Long agentId, Long mchId, Long productId, String payOrderId, String mchOrderNo, Byte productType, String createTimeStart, String createTimeEnd);

    List<Map> getAllChannel();

    List<Map> channelStatistics(Object passageId, Long productId, String ifCode, String payType, String createTimeStart, String createTimeEnd);

    List<Map> count4PaymentStatistics(Long mchId, Byte productType, String createTimeStart, String createTimeEnd);

    List<Map> paymentStatistics(int offset, int limit, Long mchId, Byte productType, String createTimeStart, String createTimeEnd);
    
    /**
     * <p><b>Description: </b>判断订单  存在&&已过期 
     * <p>2018年10月31日 上午10:35:05
     * @author terrfly
     * @param mchId
     * @param mchOrderNo
     * @return
     */
    public boolean hasOrderAndExpire(Long mchId, String mchOrderNo);

    Map orderDayAmount(Long mchId, String dayStart, String dayEnd);

    List<Map> orderProductSuccess();

    public int updateStatus4Expired(String payOrderId);

    /** 更新订单状态为 押金未结算状态； + 买家用户 */
    int updateStatus4depositIng(String payOrderId, String channelOrderNo, String channelUser);
}
