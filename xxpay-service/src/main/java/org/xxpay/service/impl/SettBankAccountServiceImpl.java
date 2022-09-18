package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.core.entity.SettBankAccountExample;
import org.xxpay.core.service.ISettBankAccountService;
import org.xxpay.service.dao.mapper.SettBankAccountMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
@Service
public class SettBankAccountServiceImpl extends ServiceImpl<SettBankAccountMapper, SettBankAccount> implements ISettBankAccountService {

    @Autowired
    private SettBankAccountMapper mchBankAccountMapper;

    @Override
    public List<SettBankAccount> select(int offset, int limit, SettBankAccount mchBankAccount) {
        SettBankAccountExample example = new SettBankAccountExample();
        example.setOffset(offset);
        example.setLimit(limit);
        SettBankAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchBankAccount);
        return mchBankAccountMapper.selectByExample(example);
    }

    @Override
    public int count(SettBankAccount mchBankAccount) {
        SettBankAccountExample example = new SettBankAccountExample();
        SettBankAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchBankAccount);
        return mchBankAccountMapper.countByExample(example);
    }

    @Override
    public SettBankAccount findById(Long id) {
        return mchBankAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public SettBankAccount findByAccountNo(String accountNo, byte infoType) {
        SettBankAccount bankAccount = new SettBankAccount();
        bankAccount.setAccountNo(accountNo);
        bankAccount.setInfoType(infoType);
        return find(bankAccount);
    }

    @Override
    public SettBankAccount find(SettBankAccount mchBankAccount) {
        SettBankAccountExample example = new SettBankAccountExample();
        SettBankAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchBankAccount);
        List<SettBankAccount> mchBankAccountList = mchBankAccountMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(mchBankAccountList)) return null;
        return mchBankAccountList.get(0);
    }

    @Override
    public int add(SettBankAccount mchBankAccount) {
        return mchBankAccountMapper.insertSelective(mchBankAccount);
    }

    @Override
    public int update(SettBankAccount mchBankAccount) {
        return mchBankAccountMapper.updateByPrimaryKeySelective(mchBankAccount);
    }

    @Override
    public int updateByMchId(SettBankAccount updateSettBankAccount, Long mchId) {
        SettBankAccountExample example = new SettBankAccountExample();
        SettBankAccountExample.Criteria criteria = example.createCriteria();
        criteria.andInfoIdEqualTo(mchId);
        criteria.andInfoTypeEqualTo(MchConstant.INFO_TYPE_MCH);
        return mchBankAccountMapper.updateByExampleSelective(updateSettBankAccount, example);
    }

    @Override
    public int delete(Long id, byte infoType) {
    	SettBankAccountExample exa = new SettBankAccountExample();
    	exa.createCriteria().andIdEqualTo(id).andInfoTypeEqualTo(infoType);
    	return mchBankAccountMapper.deleteByExample(exa);
    }
    
    @Override
    public int delete(Long id) {
    	return mchBankAccountMapper.deleteByPrimaryKey(id);
    }

    void setCriteria(SettBankAccountExample.Criteria criteria, SettBankAccount mchBankAccount) {
        if(mchBankAccount != null) {
            if(mchBankAccount.getInfoId() != null) criteria.andInfoIdEqualTo(mchBankAccount.getInfoId());
            if(mchBankAccount.getId() != null) criteria.andIdEqualTo(mchBankAccount.getId());
            if(StringUtils.isNotBlank(mchBankAccount.getAccountNo())) criteria.andAccountNoEqualTo(mchBankAccount.getAccountNo());
            if(mchBankAccount.getIsDefault() != null) criteria.andIsDefaultEqualTo(mchBankAccount.getIsDefault());
            if(mchBankAccount.getAccountType() != null && mchBankAccount.getAccountType() != -99) criteria.andAccountTypeEqualTo(mchBankAccount.getAccountType());
            if(mchBankAccount.getInfoType() != null) criteria.andInfoTypeEqualTo(mchBankAccount.getInfoType());
        }
    }

}
