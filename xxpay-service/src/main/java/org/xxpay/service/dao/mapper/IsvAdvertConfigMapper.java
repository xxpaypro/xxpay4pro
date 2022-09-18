package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.IsvAdvertConfig;
import org.xxpay.core.entity.MchTradeOrder;

import java.util.List;

/**
 * <p>
 * 服务商广告配置表 Mapper 接口
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-26
 */
public interface IsvAdvertConfigMapper extends BaseMapper<IsvAdvertConfig> {

    /** 根据条件查询广告列表 **/
    List<IsvAdvertConfig> selectAdList(@Param("showType") Byte showType, @Param("isvId") Long isvId, @Param("agentId") Long agentId,
                                                   @Param("provinceCode") Integer provinceCode, @Param("cityCode") Integer cityCode, @Param("areaCode") Integer areaCode);

}
