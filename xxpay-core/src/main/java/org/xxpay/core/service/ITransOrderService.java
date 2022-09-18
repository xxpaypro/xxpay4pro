package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.TransOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/10/26
 * @description: 转账业务
 */
public interface ITransOrderService extends IService<TransOrder> {

    TransOrder findByTransOrderId(String transOrderId);

    List<TransOrder> select(int offset, int limit, TransOrder transOrder, Date createTimeStart, Date createTimeEnd);

    Integer count(TransOrder transOrder, Date createTimeStart, Date createTimeEnd);

    TransOrder find(TransOrder transOrder);

    TransOrder selectByMchIdAndTransOrderId(Long mchId, String transOrderId);

    public TransOrder selectByInfoIdAndMchTransNo(Long infoId, Byte infoType, String mchTransNo);

    int createTransOrder(TransOrder transOrder);

    int updateStatus4Ing(String transOrderId, String channelOrderNo);

    int updateStatus4Success(String transOrderId);

    int updateStatus4Success(String transOrderId, String channelOrderNo);

    int updateStatus4Fail(String transOrderId, String channelErrCode, String channelErrMsg);

    int updateStatus4Fail(String transOrderId, String channelErrCode, String channelErrMsg, String channelOrderNo);

    Long sumAmount4AgentpayPassageAccount(int agentpayPassageAccountId, Date creatTimeStart, Date createTimeEnd);

    /**
     * 统计所有订单
     * @param mchId
     * @param transOrderId
     * @param mchTransNo
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Map count4All(Long mchId, String transOrderId, String mchTransNo, String createTimeStart, String createTimeEnd);

    /**
     * 统计成功订单
     * @param mchId
     * @param transOrderId
     * @param mchTransNo
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    Map count4Success(Long mchId, String transOrderId, String mchTransNo, String createTimeStart, String createTimeEnd);
}
