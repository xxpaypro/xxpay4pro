package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.SysLog;
import java.util.List;

public interface ISysLogService extends IService<SysLog> {

    int add(SysLog record);

    List<SysLog> select(int offset, int limit, SysLog record);

    Integer count(SysLog record);

}
