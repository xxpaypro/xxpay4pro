package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.MchApp;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/13
 * @description:
 */
public interface IMchAppService extends IService<MchApp> {

    List<MchApp> select(int pageIndex, int pageSize, MchApp mchApp);

    int count(MchApp mchApp);

    MchApp findById(String appId);

    MchApp findByMchIdAndAppId(Long mchId, String appId);

    int add(MchApp mchApp);

    int update(MchApp mchApp);

    int updateByMchIdAndAppId(Long mchId, String appId, MchApp mchApp);

    int delete(String  appId);

}
