package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.UserAccountChangeDetail;
import org.xxpay.core.entity.UserAccountChangeDetailExample;

import java.util.List;

public interface UserAccountChangeDetailMapper extends BaseMapper<UserAccountChangeDetail> {
    int countByExample(UserAccountChangeDetailExample example);

    int deleteByExample(UserAccountChangeDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserAccountChangeDetail record);

    int insertSelective(UserAccountChangeDetail record);

    List<UserAccountChangeDetail> selectByExample(UserAccountChangeDetailExample example);

    UserAccountChangeDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserAccountChangeDetail record, @Param("example") UserAccountChangeDetailExample example);

    int updateByExample(@Param("record") UserAccountChangeDetail record, @Param("example") UserAccountChangeDetailExample example);

    int updateByPrimaryKeySelective(UserAccountChangeDetail record);

    int updateByPrimaryKey(UserAccountChangeDetail record);

    UserAccountChangeDetail selectByUniqueKeyForUpdate(@Param("userId") String userID, @Param("mchId") Long mchId,
                                                       @Param("changeDay") Integer changeDay, @Param("changeType") Short changeType);

    List<UserAccountChangeDetail> selectListByPage(@Param("userId") String userId, @Param("mchId") Long mchId,
                                                   @Param("changeDay") Integer changeDay, @Param("changeType") Short changeType,
                                                   @Param("accountType") Short accountType,
                                                   @Param("startNumber") Integer startNumber, @Param("viewNumber") Integer viewNumber);

    int insertBatch(List<UserAccountChangeDetail> userAccountChangeDetailList);
}
