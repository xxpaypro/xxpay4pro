package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayOrderExample;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.core.entity.TransOrderExample;

import java.util.List;
import java.util.Map;

public interface TransOrderMapper extends BaseMapper<TransOrder> {
    int countByExample(TransOrderExample example);

    int deleteByExample(TransOrderExample example);

    int deleteByPrimaryKey(String transOrderId);

    int insert(TransOrder record);

    int insertSelective(TransOrder record);

    List<TransOrder> selectByExample(TransOrderExample example);

    TransOrder selectByPrimaryKey(String transOrderId);

    int updateByExampleSelective(@Param("record") TransOrder record, @Param("example") TransOrderExample example);

    int updateByExample(@Param("record") TransOrder record, @Param("example") TransOrderExample example);

    int updateByPrimaryKeySelective(TransOrder record);

    int updateByPrimaryKey(TransOrder record);

    // 金额求和
    long sumAmountByExample(TransOrderExample example);

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
}
