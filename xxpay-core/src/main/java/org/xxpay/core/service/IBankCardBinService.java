package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.BankCardBin;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/07/22
 * @description: 银行卡Bin接口
 */
public interface IBankCardBinService extends IService<BankCardBin> {

    int add(BankCardBin bankCardBin);

    int update(BankCardBin bankCardBin);

    BankCardBin findById(Long id);

    BankCardBin findByCardBin(String cardBin);

    BankCardBin findByCardNo(String cardNo);

    List<BankCardBin> select(int offset, int limit, BankCardBin bankCardBin);

    Integer count(BankCardBin bankCardBin);

    int delete(Long id);

    int delete(List<Long> ids);

    int insertBatch(List<BankCardBin> bankCardBinList);
}
