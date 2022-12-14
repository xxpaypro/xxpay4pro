package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.AccountBalanceMapper;
import org.xxpay.service.dao.mapper.AccountHistoryMapper;
import org.xxpay.service.dao.mapper.MchInfoMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @Author terrfly
 * @Date 2018/11/26 11:51
 */
@Service
public class AccountBalanceServiceImpl extends ServiceImpl<AccountBalanceMapper, AccountBalance> implements IAccountBalanceService {


    private static final MyLog _log = MyLog.getLog(AccountBalanceServiceImpl.class);

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;

    @Autowired
    private AccountHistoryMapper accountHistoryMapper;

    @Autowired
    private IAgentpayPassageService agentpayPassageService;

    @Autowired
    private IOrderProfitDetailService orderProfitDetailService;

    @Autowired
    private IPayPassageService payPassageService;

    @Autowired
    private IFeeScaleService feeScaleService;


    @Override
    public Map<String, Object> selectLineByInfoId(byte infoType, long infoId){
        return accountBalanceMapper.selectLineByInfoId(infoType, infoId);
    }


    @Override
    public List<AccountBalance> listAll(int offset, int limit) {

        AccountBalanceExample exa = new AccountBalanceExample();
        exa.setLimit(limit);
        exa.setOffset(offset);
        return accountBalanceMapper.selectByExample(exa);
    }

    @Override
    public int initAccount(Byte infoType, Long infoId, String infoName, byte... accountTypes){

        for(byte accountType: accountTypes){

            AccountBalance insertRecord = new AccountBalance();
            insertRecord.setInfoType(infoType);
            insertRecord.setInfoId(infoId);
            insertRecord.setInfoName(infoName);
            insertRecord.setAccountType(accountType);
            insertRecord.setStatus(MchConstant.PUB_YES); //??????
            insertRecord.setAmount(0L);
            insertRecord.setUnAmount(0L);
            insertRecord.setFrozenAmount(0L);
            insertRecord.setSettAmount(0L);
            insertRecord.setTotalAddAmount(0L);
            insertRecord.setTotalSubAmount(0L);
            insertRecord.setSafeKey(XXPayUtil.genAccountSafeKey(insertRecord));
            accountBalanceMapper.insertSelective(insertRecord);
        }

        return accountTypes.length;

    }

    /**
     * ????????????????????????
     * @param isCheck0 ??????????????????????????????????????????0??? true: ???????????????????????????????????????0???????????? false: ????????????????????????????????????0?????????
     * @param balanceRecord  ????????????????????????????????????
     * @param addAmount   amount???????????????+??? ??????-???
     * @param addUnAmount unAmount???????????????+??? ??????-???
     * @param addFrozenAmount frozenAmount???????????????+??? ??????-???
     * @param addSettAmount settAmount???????????????+??? ??????-???
     * @return
     */
    private boolean updateAccountBalanceAmount(boolean isCheck0, AccountBalance balanceRecord,
                                          Long addAmount, Long addUnAmount, Long addFrozenAmount, Long addSettAmount) {


        //??????????????????
        balanceRecord.setAmount(balanceRecord.getAmount() + (addAmount == null ? 0L : addAmount) );
        balanceRecord.setUnAmount(balanceRecord.getUnAmount() + (addUnAmount == null ? 0L : addUnAmount) );
        balanceRecord.setFrozenAmount(balanceRecord.getFrozenAmount() + (addFrozenAmount == null ? 0L : addFrozenAmount) );
        balanceRecord.setSettAmount(balanceRecord.getSettAmount() + (addSettAmount == null ? 0L : addSettAmount) );

        if(isCheck0){
            //????????????????????????
            if(balanceRecord.getAmount() < 0 ) throw new ServiceException(RetEnum.RET_SERVICE_ACCOUNT_AMOUNT_UPDATE_FAIL);
            if(balanceRecord.getUnAmount() < 0 ) throw new ServiceException(RetEnum.RET_SERVICE_FREEZE_AMOUNT_OUT_LIMIT);
            if(balanceRecord.getSettAmount() < 0 ) throw new ServiceException(RetEnum.RET_SERVICE_ACCOUNT_BALANCE_OUT_LIMIT);
            if(balanceRecord.getFrozenAmount() < 0 ) throw new ServiceException(RetEnum.RET_SERVICE_UN_FREEZE_AMOUNT_OUT_LIMIT);
            if(balanceRecord.getAmount() - balanceRecord.getUnAmount() - balanceRecord.getFrozenAmount() < 0 ){
                throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);
            }
        }

        //??????amount??????????????? ???total????????????
        if(addAmount != null && addAmount > 0 ){
            balanceRecord.setTotalAddAmount(balanceRecord.getTotalAddAmount() + addAmount);
        }
        if(addAmount != null && addAmount < 0 ){
            balanceRecord.setTotalSubAmount(balanceRecord.getTotalSubAmount() + (0-addAmount));
        }

        String oldSafeKey = balanceRecord.getSafeKey();   //??? ????????????key (???????????????)
        balanceRecord.setSafeKey(XXPayUtil.genAccountSafeKey(balanceRecord)); //????????????????????????key
        balanceRecord.setUpdateTime(new Date());

        AccountBalanceExample exa = new AccountBalanceExample();
        exa.createCriteria().andIdEqualTo(balanceRecord.getId()).andSafeKeyEqualTo(oldSafeKey);
        int updateRow = accountBalanceMapper.updateByExampleSelective(balanceRecord, exa);

        return updateRow > 0 ;
    }

    @Override
    public AccountBalance findOne(byte infoType, long infoId, byte accountType){
        AccountBalanceExample exa = new AccountBalanceExample();
        exa.createCriteria().andInfoTypeEqualTo(infoType)
        .andInfoIdEqualTo(infoId)
        .andAccountTypeEqualTo(accountType);

        List<AccountBalance> result = accountBalanceMapper.selectByExample(exa);
        if (result == null || result.isEmpty()) {
            throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_NOT_EXIST);
        }

        AccountBalance record = result.get(0);
        if(record.getStatus() != MchConstant.PUB_YES) {
            throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_DISABLED);
        }

        if(!XXPayUtil.checkAccountSafe(record)){  // ????????????????????????
            throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_SAFE_ERROR);
        }

        return result.get(0);
    }

    @Override
    public void changeUnAmountOnly(AccountBalance balanceRecord, Long changeUnAmount) {

        boolean isTrue = updateAccountBalanceAmount(true, balanceRecord, null, changeUnAmount,null, null );
        if(!isTrue) throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_AMOUNT_UPDATE_FAIL);
    }

    @Override
    public void changeUnAmountOnly(byte infoType, long infoId, byte accountType, Long changeUnAmount){
        changeUnAmountOnly(findOne(infoType, infoId, accountType), changeUnAmount);
    }


    @Override
    public void changeSettAmountOnly(AccountBalance balanceRecord, Long changeSettAmount) {

        boolean isTrue = updateAccountBalanceAmount(true, balanceRecord, null, null,null, changeSettAmount );
        if(!isTrue) throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_AMOUNT_UPDATE_FAIL);
    }

    @Override
    public void changeSettAmountOnly(byte infoType, long infoId, byte accountType, Long changeSettAmount){
        changeSettAmountOnly(findOne(infoType, infoId, accountType), changeSettAmount);
    }


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void changeAmountAndInsertHistory(byte infoType, long infoId, Byte bizType, String bizItem, Long changeAmount, String remark) {

        changeAmountAndInsertHistory( infoType, infoId, bizType, bizItem, changeAmount, remark,
                                        null, null, null, null);

    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void changeAmountAndInsertHistory(byte infoType, long infoId, Byte bizType, String bizItem, Long changeAmount, String remark,
                                             String bizOrderId, String bizChannelOrderNo, Long bizOrderAmount, Long bizOrderFee) {

        // ??????????????????
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setInfoType(infoType);
        accountHistory.setInfoId(infoId);
        accountHistory.setChangeAmount( Math.abs(changeAmount));  //?????????????????????
        accountHistory.setBizType(bizType);
        accountHistory.setBizItem(bizItem);
        accountHistory.setFundDirection(changeAmount > 0 ? MchConstant.FUND_DIRECTION_ADD : MchConstant.FUND_DIRECTION_SUB);
        accountHistory.setRemark(remark);
        accountHistory.setBizOrderId(bizOrderId);
        accountHistory.setBizChannelOrderNo(bizChannelOrderNo);
        accountHistory.setBizOrderAmount(bizOrderAmount);
        accountHistory.setBizOrderFee(bizOrderFee);

        Byte accountType = XXPayUtil.getAccountTypeByBizType(bizType, bizItem); //?????????????????? ?????????????????????????????????
        accountHistory.setChangeAccountType(accountType);

        //????????????????????????????????????
        AccountBalance balanceRecord = findOne(infoType, infoId, accountType);

        //??????history???????????????????????????
        Map<String, Long> changeAmountMap = XXPayUtil.fillHistoryAndReturnChangeAmount(bizType, bizItem, changeAmount, balanceRecord, accountHistory);

        boolean isCheck0 = true;
        //???????????????????????? ?????????????????????????????? ??????????????????
        if(infoType == MchConstant.INFO_TYPE_MCH && mchInfoMapper.selectById(infoId).getType() == MchConstant.MCH_TYPE_PRIVATE){
            changeAmountMap = new HashMap<>(); changeAmountMap.put("addAmount", changeAmount);
            isCheck0 = false;
        }

        boolean isTrue = updateAccountBalanceAmount(isCheck0, balanceRecord, changeAmountMap.get("addAmount"), changeAmountMap.get("addUnAmount"),
                changeAmountMap.get("addFrozenAmount"), changeAmountMap.get("addSettAmount") );

        if(!isTrue) throw ServiceException.build(RetEnum.RET_SERVICE_ACCOUNT_AMOUNT_UPDATE_FAIL);
        accountHistoryMapper.insertSelective(accountHistory);

    }


}
