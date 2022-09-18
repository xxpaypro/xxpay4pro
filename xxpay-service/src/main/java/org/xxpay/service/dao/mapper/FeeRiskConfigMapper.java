package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.FeeRiskConfig;
import org.xxpay.core.entity.FeeRiskConfigExample;

import java.util.List;

public interface FeeRiskConfigMapper extends BaseMapper<FeeRiskConfig> {
    int countByExample(FeeRiskConfigExample example);

    int deleteByExample(FeeRiskConfigExample example);

    int deleteByPrimaryKey(Integer feeScaleId);

    int insert(FeeRiskConfig record);

    int insertSelective(FeeRiskConfig record);

    List<FeeRiskConfig> selectByExample(FeeRiskConfigExample example);

    FeeRiskConfig selectByPrimaryKey(Integer feeScaleId);

    int updateByExampleSelective(@Param("record") FeeRiskConfig record, @Param("example") FeeRiskConfigExample example);

    int updateByExample(@Param("record") FeeRiskConfig record, @Param("example") FeeRiskConfigExample example);

    int updateByPrimaryKeySelective(FeeRiskConfig record);

    int updateByPrimaryKey(FeeRiskConfig record);
}
