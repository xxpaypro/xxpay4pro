package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayInterfaceTypeExample;

import java.util.List;

public interface PayInterfaceTypeMapper extends BaseMapper<PayInterfaceType> {
    int countByExample(PayInterfaceTypeExample example);

    int deleteByExample(PayInterfaceTypeExample example);

    int deleteByPrimaryKey(String ifTypeCode);

    int insert(PayInterfaceType record);

    int insertSelective(PayInterfaceType record);

    List<PayInterfaceType> selectByExample(PayInterfaceTypeExample example);

    PayInterfaceType selectByPrimaryKey(String ifTypeCode);

    int updateByExampleSelective(@Param("record") PayInterfaceType record, @Param("example") PayInterfaceTypeExample example);

    int updateByExample(@Param("record") PayInterfaceType record, @Param("example") PayInterfaceTypeExample example);

    int updateByPrimaryKeySelective(PayInterfaceType record);

    int updateByPrimaryKey(PayInterfaceType record);
}
