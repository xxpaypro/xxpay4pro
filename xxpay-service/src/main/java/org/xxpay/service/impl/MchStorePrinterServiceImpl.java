package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchStorePrinter;
import org.xxpay.core.service.IMchStorePrinterService;
import org.xxpay.service.dao.mapper.MchStorePrinterMapper;

/**
 * <p>
 * 门店与打印机关联表 服务实现类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@Service
public class MchStorePrinterServiceImpl extends ServiceImpl<MchStorePrinterMapper, MchStorePrinter> implements IMchStorePrinterService {

}
