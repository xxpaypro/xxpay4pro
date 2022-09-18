package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.UserAccountLog;
import org.xxpay.core.entity.UserAccountLogExample;

import java.util.List;

public interface UserAccountLogMapper extends BaseMapper<UserAccountLog> {
    int countByExample(UserAccountLogExample example);

    int deleteByExample(UserAccountLogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(UserAccountLog record);

    int insertSelective(UserAccountLog record);

    List<UserAccountLog> selectByExample(UserAccountLogExample example);

    UserAccountLog selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("record") UserAccountLog record, @Param("example") UserAccountLogExample example);

    int updateByExample(@Param("record") UserAccountLog record, @Param("example") UserAccountLogExample example);

    int updateByPrimaryKeySelective(UserAccountLog record);

    int updateByPrimaryKey(UserAccountLog record);

    int insertBatch(List<UserAccountLog> records);

}
