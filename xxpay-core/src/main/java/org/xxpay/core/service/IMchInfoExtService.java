package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchInfoExt;

/**
 * <p>
 * 商户扩展信息表（进件） 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-04
 */
public interface IMchInfoExtService extends IService<MchInfoExt> {

    /** 获取唯一一条记录 **/
    MchInfoExt getOneMchExt(Long mchId, Long isvId, Long agentId);

    /** 更新 / 新增 **/
    boolean saveOrUpdate(MchInfoExt record);

}
