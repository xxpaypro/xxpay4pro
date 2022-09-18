package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.FeeScaleExample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FeeScaleMapper extends BaseMapper<FeeScale> {
    int countByExample(FeeScaleExample example);

    int deleteByExample(FeeScaleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeeScale record);

    int insertSelective(FeeScale record);

    List<FeeScale> selectByExample(FeeScaleExample example);

    FeeScale selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeeScale record, @Param("example") FeeScaleExample example);

    int updateByExample(@Param("record") FeeScale record, @Param("example") FeeScaleExample example);

    int updateByPrimaryKeySelective(FeeScale record);

    int updateByPrimaryKey(FeeScale record);

    FeeScale selectAgpayAvailable(@Param("infoType") byte infoType, @Param("infoId") Long infoId);


    List<FeeScale> productLeftJoinFeeScale(@Param("infoType") byte infoType, @Param("infoId") Long infoId);

    List<FeeScale> productInnerJoinFeeScale(@Param("infoType") byte infoType, @Param("infoId") Long infoId);

    List<FeeScale> agpayPassageLeftJoinFeeScale(@Param("infoType") byte infoType, @Param("infoId") Long infoId,
                                                @Param("passageBelongInfoType") byte passageBelongInfoType, @Param("passageBelongInfoId") Long passageBelongInfoId);

    List<FeeScale> agpayPassageInnerJoinFeeScale(@Param("infoType") byte infoType, @Param("infoId") Long infoId,
                                                 @Param("passageBelongInfoType") byte passageBelongInfoType, @Param("passageBelongInfoId") Long passageBelongInfoId);

    /**
     * 查询下级配置的最小 费率/费用
     * @param childInfoType 查询下级的类型 1-商户 2-代理商
     * @param currentAgentId 当前代理商ID
     * @param productType
     * @param refProductId
     * @return
     */
    BigDecimal selectMinChildrenFee(@Param("childInfoType") byte childInfoType, @Param("currentAgentId") Long currentAgentId,
                                    @Param("productType") byte productType, @Param("refProductId") Integer refProductId);

}
