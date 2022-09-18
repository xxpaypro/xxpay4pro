package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchOperatorHandover;

/**
 * <p>
 * 商户操作员交班记录表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-29
 */
public interface IMchOperatorHandoverService extends IService<MchOperatorHandover> {

    /** 操作员交班操作 **/
    void operatorHandover(MchOperatorHandover record);

}
