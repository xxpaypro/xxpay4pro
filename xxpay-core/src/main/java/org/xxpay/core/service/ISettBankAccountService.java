package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.SettBankAccount;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
public interface ISettBankAccountService extends IService<SettBankAccount> {

    List<SettBankAccount> select(int offset, int limit, SettBankAccount mchBankAccount);

    int count(SettBankAccount mchBankAccount);

    SettBankAccount find(SettBankAccount mchBankAccount);

    SettBankAccount findById(Long id);

    SettBankAccount findByAccountNo(String accountNo, byte infoType) ;

    int add(SettBankAccount mchBankAccount);

    int update(SettBankAccount mchBankAccount);

    int updateByMchId(SettBankAccount mchBankAccount, Long mchId);

    int delete(Long id, byte infoType);
    
    int delete(Long id);

}
