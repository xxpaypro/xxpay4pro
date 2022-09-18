package org.xxpay.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.ICheckService;
import org.xxpay.service.dao.mapper.CheckBatchMapper;
import org.xxpay.service.dao.mapper.CheckMistakeMapper;
import org.xxpay.service.dao.mapper.CheckMistakePoolMapper;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/21
 * @description:
 */
@Service
public class CheckServiceImpl implements ICheckService {

    @Autowired
    private CheckBatchMapper checkBatchMapper;

    @Autowired
    private CheckMistakeMapper checkMistakeMapper;

    @Autowired
    private CheckMistakePoolMapper checkMistakePoolMapper;

    @Override
    public List<CheckMistakePool> selectAllMistakePool() {
        CheckMistakePoolExample example = new CheckMistakePoolExample();
        return checkMistakePoolMapper.selectByExample(example);
    }

    @Override
    public void saveData(CheckBatch batch, List<CheckMistake> mistakeList, List<CheckMistakePool> insertScreatchRecordList, List<CheckMistakePool> removeScreatchRecordList) {

        // TODO 保证事务
        // 插入批次
        checkBatchMapper.insertSelective(batch);
        // 插入差错
        if(CollectionUtils.isNotEmpty(mistakeList)) {
            for(CheckMistake checkMistake : mistakeList) {
                checkMistakeMapper.insertSelective(checkMistake);
            }
        }
        // 插入缓冲池数据
        if(CollectionUtils.isNotEmpty(insertScreatchRecordList)) {
            for(CheckMistakePool checkMistakePool : insertScreatchRecordList) {
                checkMistakePoolMapper.insertSelective(checkMistakePool);
            }
        }
        // 删除需要删除的缓冲池数据
        if(CollectionUtils.isNotEmpty(removeScreatchRecordList)) {
            for(CheckMistakePool checkMistakePool : removeScreatchRecordList) {
                checkMistakePoolMapper.deleteByPrimaryKey(checkMistakePool.getId());
            }
        }
    }

    @Override
    public List<CheckMistakePool> selectScratchPoolRecord(Date date) {
        CheckMistakePoolExample example = new CheckMistakePoolExample();
        CheckMistakePoolExample.Criteria criteria = example.createCriteria();
        criteria.andCreateTimeLessThan(date);
        return checkMistakePoolMapper.selectByExample(example);
    }

    @Override
    public void removeDateFromPool(List<CheckMistakePool> list, List<CheckMistake> mistakeList) {

        // 保存差错
        if(CollectionUtils.isNotEmpty(mistakeList)) {
            for(CheckMistake checkMistake : mistakeList) {
                checkMistakeMapper.insertSelective(checkMistake);
            }
        }

        // 删除缓冲池数据
        if(CollectionUtils.isNotEmpty(list)) {
            for(CheckMistakePool checkMistakePool : list) {
                checkMistakePoolMapper.deleteByPrimaryKey(checkMistakePool.getId());
            }
        }
    }

    @Override
    public List<CheckBatch> selectCheckBatch(CheckBatch batch) {
        CheckBatchExample example = new CheckBatchExample();
        CheckBatchExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckBatch(criteria, batch);
        return checkBatchMapper.selectByExample(example);
    }

    @Override
    public List<CheckBatch> selectCheckBatch(int offset, int limit, CheckBatch batch) {
        CheckBatchExample example = new CheckBatchExample();
        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause("createTime desc");
        CheckBatchExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckBatch(criteria, batch);
        return checkBatchMapper.selectByExample(example);
    }

    @Override
    public List<CheckMistake> selectCheckMistake(int offset, int limit, CheckMistake checkMistake) {
        CheckMistakeExample example = new CheckMistakeExample();
        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause("createTime desc");
        CheckMistakeExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckMistake(criteria, checkMistake);
        return checkMistakeMapper.selectByExample(example);
    }

    @Override
    public List<CheckMistakePool> selectCheckMistakePool(int offset, int limit, CheckMistakePool checkMistakePool) {
        CheckMistakePoolExample example = new CheckMistakePoolExample();
        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause("createTime desc");
        CheckMistakePoolExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckMistakePool(criteria, checkMistakePool);
        return checkMistakePoolMapper.selectByExample(example);
    }

    @Override
    public Integer countCheckBatch(CheckBatch batch) {
        CheckBatchExample example = new CheckBatchExample();
        CheckBatchExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckBatch(criteria, batch);
        return checkBatchMapper.countByExample(example);
    }

    @Override
    public Integer countCheckMistake(CheckMistake checkMistake) {
        CheckMistakeExample example = new CheckMistakeExample();
        CheckMistakeExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckMistake(criteria, checkMistake);
        return checkMistakeMapper.countByExample(example);
    }

    @Override
    public Integer countCheckMistakePool(CheckMistakePool checkMistakePool) {
        CheckMistakePoolExample example = new CheckMistakePoolExample();
        CheckMistakePoolExample.Criteria criteria = example.createCriteria();
        setCriteria4CheckMistakePool(criteria, checkMistakePool);
        return checkMistakePoolMapper.countByExample(example);
    }

    @Override
    public Integer handleCheckMistake(Long id, String handleType, String handleRemark) {
        // 根据id查询
        /*RpAccountCheckMistake mistake = rpAccountCheckMistakeService.getDataById(id);
        mistake.setHandleStatus(MistakeHandleStatusEnum.HANDLED.name());
        mistake.setHandleRemark(handleRemark);
        // 修改差错记录
        rpAccountCheckMistakeService.updateData(mistake);

        Boolean bank = false;
        if ("bank".equals(handleType.trim())) {
            mistake.setHandleValue("以银行为准");
            bank = true;
        }
        // 以平台数据为准：只需修改差错记录
        if (!bank) {
            return;
        }

        switch (mistake.getErrType()) {

            case "BANK_MISS":// 银行不存在该订单
                // 以银行为准
                if (bank) {
                    // 把订单改为失败，减款
                    String trxNo = mistake.getTrxNo();
                    rpTradeReconciliationService.bankMissOrBankFailBaseBank(trxNo);
                }

                break;

            case "PLATFORM_SHORT_STATUS_MISMATCH":// 银行支付成功，平台支付不成功,默认以银行为准
                // 以银行为准
                if (bank) {
                    String trxNo = mistake.getTrxNo();
                    String bankTrxNo = mistake.getBankTrxNo();
                    rpTradeReconciliationService.platFailBankSuccess(trxNo, bankTrxNo);
                }
                break;

            case "PLATFORM_SHORT_CASH_MISMATCH":// 平台需支付金额比银行实际支付金额少
                // 以银行为准
                if (bank) {
                    // 累加金额
                    rpTradeReconciliationService.handleAmountMistake(mistake, true);
                }
                break;

            case "PLATFORM_OVER_CASH_MISMATCH":// 银行实际支付金额比平台需支付金额少
                // 以银行为准
                if (bank) {
                    // 支付记录减款
                    rpTradeReconciliationService.handleAmountMistake(mistake, false);
                }
                break;

            case "PLATFORM_OVER_STATUS_MISMATCH":// 平台支付成功，银行支付不成功(和银行漏单一致)
                // 以银行为准
                if (bank) {
                    // 把订单改为失败，减款
                    String trxNo = mistake.getTrxNo();
                    rpTradeReconciliationService.bankMissOrBankFailBaseBank(trxNo);
                }
                break;

            case "FEE_MISMATCH":// 手续费不匹配
                // 以银行为准
                if (bank) {
                    rpTradeReconciliationService.handleFeeMistake(mistake);
                }
                break;

            case "PLATFORM_MISS":// 平台不存在该订单(暂时不考虑这种情况)
                break;

            default:
                break;
        }*/
        return 1;
    }

    @Override
    public CheckBatch findByBatchId(long batchId) {
        return checkBatchMapper.selectByPrimaryKey(batchId);
    }

    @Override
    public CheckMistake findByMistakeId(Long mistakeId) {
        return checkMistakeMapper.selectByPrimaryKey(mistakeId);
    }

    @Override
    public CheckMistakePool findByMistakePoolId(Long mistakePoolId) {
        return checkMistakePoolMapper.selectByPrimaryKey(mistakePoolId);
    }

    void setCriteria4CheckBatch(CheckBatchExample.Criteria criteria, CheckBatch batch) {
        if(batch != null) {
            if(batch.getChannelMchId() != null) criteria.andChannelMchIdEqualTo(batch.getChannelMchId());
            if(batch.getBillDate() != null) criteria.andBillDateEqualTo(batch.getBillDate());
        }
    }

    void setCriteria4CheckMistake(CheckMistakeExample.Criteria criteria, CheckMistake checkMistake) {
        if(checkMistake != null) {
            if(checkMistake.getChannelMchId() != null) criteria.andChannelMchIdEqualTo(checkMistake.getChannelMchId());
            if(checkMistake.getBillDate() != null) criteria.andBillDateEqualTo(checkMistake.getBillDate());
        }
    }

    void setCriteria4CheckMistakePool(CheckMistakePoolExample.Criteria criteria, CheckMistakePool checkMistakePool) {
        if(checkMistakePool != null) {
        }
    }

}
