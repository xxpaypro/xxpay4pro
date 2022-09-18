package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.core.entity.SettBankAccountExample;

public interface SettBankAccountMapper extends BaseMapper<SettBankAccount> {
    int countByExample(SettBankAccountExample example);

    int deleteByExample(SettBankAccountExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SettBankAccount record);

    int insertSelective(SettBankAccount record);

    List<SettBankAccount> selectByExample(SettBankAccountExample example);

    SettBankAccount selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SettBankAccount record, @Param("example") SettBankAccountExample example);

    int updateByExample(@Param("record") SettBankAccount record, @Param("example") SettBankAccountExample example);

    int updateByPrimaryKeySelective(SettBankAccount record);

    int updateByPrimaryKey(SettBankAccount record);
}
