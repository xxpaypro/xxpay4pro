package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.RechargeOrder;
import org.xxpay.core.entity.RechargeOrderExample;

import java.util.List;

public interface RechargeOrderMapper extends BaseMapper<RechargeOrder> {
    int countByExample(RechargeOrderExample example);

    int deleteByExample(RechargeOrderExample example);

    int deleteByPrimaryKey(String rechargeOrderId);

    int insert(RechargeOrder record);

    int insertSelective(RechargeOrder record);

    List<RechargeOrder> selectByExample(RechargeOrderExample example);

    RechargeOrder selectByPrimaryKey(String rechargeOrderId);

    int updateByExampleSelective(@Param("record") RechargeOrder record, @Param("example") RechargeOrderExample example);

    int updateByExample(@Param("record") RechargeOrder record, @Param("example") RechargeOrderExample example);

    int updateByPrimaryKeySelective(RechargeOrder record);

    int updateByPrimaryKey(RechargeOrder record);
}
