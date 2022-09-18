package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MchInfoMapper extends BaseMapper<MchInfo> {

    /**
     * 统计商户信息
     * @param param
     * @return
     */
    Map count4Mch(Map param);


    /** 统计商户数量：  根据代理商等级查询 **/
    Long countMchByAgentLevel(@Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd,
                                @Param("agentLevel") Integer agentLevel, @Param("isvId") Long isvId, @Param("agentIdList") List<Long> agentIdList);

    /** 代理商下所属商户微信进件信息列表 **/
    List<MchInfo> selectWxSnashotPage(Map param);

    /** 统计代理商下所属商户微信进件数量 **/
    int countWxSnashot(Map param);

    /** 代理商下所属商户支付宝进件信息列表 **/
    List<MchInfo> selectAliSnashotPage(Map param);

    /** 统计代理商下所属商户支付宝进件数量 **/
    int countAliSnashot(Map param);

    /**  通过商户ID集合查询商户 **/
    List<Long> selectMchIdForNc(List<Long> mchIds);
}