package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import org.xxpay.core.entity.PayInterfaceTypeTemplate;

/**
 * @author: dingzhiwei
 * @date: 17/12/4
 * @description: 代理商账户流水记录
 */
public interface IPayInterfaceTypeTemplateService extends IService<PayInterfaceTypeTemplate> {

    List<PayInterfaceTypeTemplate> select(int offset, int limit, PayInterfaceTypeTemplate record);

    int count(PayInterfaceTypeTemplate record);

    PayInterfaceTypeTemplate findById(Long id);
    
    int update(PayInterfaceTypeTemplate record);
    
    int add(PayInterfaceTypeTemplate record);
    
    
}
