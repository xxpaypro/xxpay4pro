package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.IsvPrinterConfig;
import org.xxpay.core.service.IIsvPrinterConfigService;
import org.xxpay.service.dao.mapper.IsvPrinterConfigMapper;

/**
 * <p>
 * 服务商与打印机配置表 服务实现类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@Service
public class IsvPrinterConfigServiceImpl extends ServiceImpl<IsvPrinterConfigMapper, IsvPrinterConfig> implements IIsvPrinterConfigService {

}
