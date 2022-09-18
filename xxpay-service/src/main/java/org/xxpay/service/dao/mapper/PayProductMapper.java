package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.core.entity.PayProductExample;

import java.util.List;

public interface PayProductMapper extends BaseMapper<PayProduct> {
    int countByExample(PayProductExample example);

    int deleteByExample(PayProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PayProduct record);

    int insertSelective(PayProduct record);

    List<PayProduct> selectByExample(PayProductExample example);

    PayProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PayProduct record, @Param("example") PayProductExample example);

    int updateByExample(@Param("record") PayProduct record, @Param("example") PayProductExample example);

    int updateByPrimaryKeySelective(PayProduct record);

    int updateByPrimaryKey(PayProduct record);
}
