package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchOperatorHandover;
import org.xxpay.core.entity.SysUser;
import org.xxpay.core.service.IMchOperatorHandoverService;
import org.xxpay.core.service.ISysService;
import org.xxpay.service.dao.mapper.MchOperatorHandoverMapper;

/**
 * <p>
 * 商户操作员交班记录表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-29
 */
@Service
public class MchOperatorHandoverServiceImpl extends ServiceImpl<MchOperatorHandoverMapper, MchOperatorHandover> implements IMchOperatorHandoverService {

    @Autowired
    private ISysService sysService;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void operatorHandover(MchOperatorHandover record){

        //更新当前操作员工作状态为： 非工作状态
        SysUser sysUser = new SysUser();
        sysUser.setUserId(record.getUserId());
        sysUser.setWorkStatus(MchConstant.PUB_NO); //未工作状态
        sysService.update(sysUser);

        save(record);
    }



}
