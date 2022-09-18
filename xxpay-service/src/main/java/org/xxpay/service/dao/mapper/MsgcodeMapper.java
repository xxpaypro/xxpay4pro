package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.Msgcode;
import org.xxpay.core.entity.MsgcodeExample;

public interface MsgcodeMapper extends BaseMapper<Msgcode> {
    int countByExample(MsgcodeExample example);

    int deleteByExample(MsgcodeExample example);

    int deleteByPrimaryKey(Long ID);

    int insert(Msgcode record);

    int insertSelective(Msgcode record);

    List<Msgcode> selectByExample(MsgcodeExample example);

    Msgcode selectByPrimaryKey(Long ID);

    int updateByExampleSelective(@Param("record") Msgcode record, @Param("example") MsgcodeExample example);

    int updateByExample(@Param("record") Msgcode record, @Param("example") MsgcodeExample example);

    int updateByPrimaryKeySelective(Msgcode record);

    int updateByPrimaryKey(Msgcode record);
    
    int addCode(@Param("phoneNo") String phoneNo, @Param("code") String code, @Param("bizType") Byte bizType, 
    		@Param("expTime") Integer expTime, @Param("todayLimit") Integer todayLimit);
    
    int verifyCode(@Param("phoneNo") String phoneNo, @Param("code") String code, @Param("bizType") Byte bizType);
}
