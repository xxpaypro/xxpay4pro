package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.PayInterface;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/5/3
 * @description: 支付接口
 */
public interface IPayInterfaceService extends IService<PayInterface> {

    int add(PayInterface payInterface);

    int update(PayInterface payInterface);

    PayInterface findByCode(String ifCode);

    /** 根据支付接口类型 和 支付类型获取唯一的一条支付接口记录 **/
    PayInterface findByTypeCodeAndPayType(String ifTypeCode, String payType);

    List<PayInterface> select(int offset, int limit, PayInterface payInterface);

    Integer count(PayInterface payInterface);

    List<PayInterface> selectAll(PayInterface payInterface);

    /**
     * 根据接口类型代码查询接口列表
     * @param ifTypeCode
     * @return
     */
    List<PayInterface> selectAllByTypeCode(String ifTypeCode);

}
