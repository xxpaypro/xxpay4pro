package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.PayPassageExample;

import java.math.BigDecimal;
import java.util.List;

public interface PayPassageMapper extends BaseMapper<PayPassage> {
    int countByExample(PayPassageExample example);

    int deleteByExample(PayPassageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PayPassage record);

    int insertSelective(PayPassage record);

    List<PayPassage> selectByExample(PayPassageExample example);

    PayPassage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PayPassage record, @Param("example") PayPassageExample example);

    int updateByExample(@Param("record") PayPassage record, @Param("example") PayPassageExample example);

    int updateByPrimaryKeySelective(PayPassage record);

    int updateByPrimaryKey(PayPassage record);

    BigDecimal selectMaxFeeByExample(PayPassageExample example);

}
