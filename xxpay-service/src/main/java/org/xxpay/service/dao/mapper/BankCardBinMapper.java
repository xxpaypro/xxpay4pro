package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.BankCardBin;
import org.xxpay.core.entity.BankCardBinExample;

import java.util.List;

public interface BankCardBinMapper extends BaseMapper<BankCardBin> {
    int countByExample(BankCardBinExample example);

    int deleteByExample(BankCardBinExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BankCardBin record);

    int insertSelective(BankCardBin record);

    List<BankCardBin> selectByExample(BankCardBinExample example);

    BankCardBin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BankCardBin record, @Param("example") BankCardBinExample example);

    int updateByExample(@Param("record") BankCardBin record, @Param("example") BankCardBinExample example);

    int updateByPrimaryKeySelective(BankCardBin record);

    int updateByPrimaryKey(BankCardBin record);

    BankCardBin selectByCardNo(String cardNo);

    int insertBatch(List<BankCardBin> bankCardBinList);
}
