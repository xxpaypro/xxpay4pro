package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.UserAccount;
import org.xxpay.core.entity.UserAccountExample;

import java.util.List;

public interface UserAccountMapper extends BaseMapper<UserAccount> {
    int countByExample(UserAccountExample example);

    int deleteByExample(UserAccountExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("mchId") Long mchId);

    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    List<UserAccount> selectByExample(UserAccountExample example);

    UserAccount selectByPrimaryKey(@Param("userId") String userId, @Param("mchId") Long mchId);

    int updateByExampleSelective(@Param("record") UserAccount record, @Param("example") UserAccountExample example);

    int updateByExample(@Param("record") UserAccount record, @Param("example") UserAccountExample example);

    int updateByPrimaryKeySelective(UserAccount record);

    int updateByPrimaryKey(UserAccount record);

    UserAccount checkUserAccountExistLock(@Param("userId") String userId, @Param("mchId") Long mchId);
}
