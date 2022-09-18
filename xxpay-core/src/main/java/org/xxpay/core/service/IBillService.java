package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.Bill;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/02/06
 * @description: 商户对账单
 */
public interface IBillService extends IService<Bill> {

    Bill findById(Long id);

    public Bill findByInfoIdAndId(Long infoId, Byte infoType, Long id) ;

    List<Bill> select(int offset, int limit, Bill mchBill);

    Integer count(Bill mchBill);

    public Bill findByInfoIdAndBillDate(Long infoId, Byte infoType, Date billDate, Byte billType) ;

    int add(Bill mchBill);

    public int updateComplete(Long infoId, Byte infoType, Date billDate);
    
    List<Long> findMchIds(Bill condition);

    public Integer countBySub(Bill mchBill) ;

    public List<Bill> selectBySub(int offset, int limit, Bill mchBill) ;

}
