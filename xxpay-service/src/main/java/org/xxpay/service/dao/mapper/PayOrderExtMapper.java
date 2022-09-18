package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayOrderExt;
import org.xxpay.core.entity.PayOrderExtExample;

public interface PayOrderExtMapper extends BaseMapper<PayOrderExt> {
    int countByExample(PayOrderExtExample example);

    int deleteByExample(PayOrderExtExample example);

    int deleteByPrimaryKey(String payOrderId);

    int insert(PayOrderExt record);

    int insertSelective(PayOrderExt record);

    List<PayOrderExt> selectByExample(PayOrderExtExample example);

    PayOrderExt selectByPrimaryKey(String payOrderId);

    int updateByExampleSelective(@Param("record") PayOrderExt record, @Param("example") PayOrderExtExample example);

    int updateByExample(@Param("record") PayOrderExt record, @Param("example") PayOrderExtExample example);

    int updateByPrimaryKeySelective(PayOrderExt record);

    int updateByPrimaryKey(PayOrderExt record);
    
    String queryCanRepeatPlaceOrderExt(@Param("mchId")Long mchId, @Param("mchOrderNo")String mchOrderNo);
}
