package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AccountBalanceExample;

import java.util.List;
import java.util.Map;

public interface AccountBalanceMapper extends BaseMapper<AccountBalance> {
    int countByExample(AccountBalanceExample example);

    int deleteByExample(AccountBalanceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountBalance record);

    int insertSelective(AccountBalance record);

    List<AccountBalance> selectByExample(AccountBalanceExample example);

    AccountBalance selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountBalance record, @Param("example") AccountBalanceExample example);

    int updateByExample(@Param("record") AccountBalance record, @Param("example") AccountBalanceExample example);

    int updateByPrimaryKeySelective(AccountBalance record);

    int updateByPrimaryKey(AccountBalance record);

    /**
     * 查询所有账户信息（一行显示），并放置在map中
     * @param infoType
     * @param infoId
     * @return
     */
    Map<String, Object> selectLineByInfoId(@Param("infoType")byte infoType, @Param("infoId")long infoId);
}
