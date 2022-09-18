package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.PayInterfaceTypeTemplate;
import org.xxpay.core.entity.PayInterfaceTypeTemplateExample;

public interface PayInterfaceTypeTemplateMapper extends BaseMapper<PayInterfaceTypeTemplate> {
    int countByExample(PayInterfaceTypeTemplateExample example);

    int deleteByExample(PayInterfaceTypeTemplateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PayInterfaceTypeTemplate record);

    int insertSelective(PayInterfaceTypeTemplate record);

    List<PayInterfaceTypeTemplate> selectByExample(PayInterfaceTypeTemplateExample example);

    PayInterfaceTypeTemplate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PayInterfaceTypeTemplate record, @Param("example") PayInterfaceTypeTemplateExample example);

    int updateByExample(@Param("record") PayInterfaceTypeTemplate record, @Param("example") PayInterfaceTypeTemplateExample example);

    int updateByPrimaryKeySelective(PayInterfaceTypeTemplate record);

    int updateByPrimaryKey(PayInterfaceTypeTemplate record);
}
