package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.xxpay.core.common.enumm.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.ObjectValidUtil;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.IUserAccountService;
import org.xxpay.service.common.enumm.AccountChangeTypeEnum;
import org.xxpay.service.common.enumm.AccountTypeEnum;
import org.xxpay.service.common.util.ChecksumUtil;
import org.xxpay.service.common.util.DateUtil;
import org.xxpay.service.dao.mapper.MchInfoMapper;
import org.xxpay.service.dao.mapper.UserAccountChangeDetailMapper;
import org.xxpay.service.dao.mapper.UserAccountLogMapper;
import org.xxpay.service.dao.mapper.UserAccountMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglei
 * Date: 18/3/5 下午4:40
 * Description: 用户账户服务接口实现
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

    private static final MyLog _log = MyLog.getLog(UserAccountServiceImpl.class);

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private UserAccountLogMapper userAccountLogMapper;

    @Autowired
    private UserAccountChangeDetailMapper userAccountChangeDetailMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    /**
     * 用户账户开户
     * @param mchId  商户ID
     * @param userId 用户ID
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public Integer openAccount(Long mchId, String userId) {
        if (ObjectValidUtil.isInvalid(userId, mchId)) {
            _log.warn("用户账户开户失败, {}. userId={}, mchId={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, mchId);
            return 0;
        }
        try {
            // 校验商户是否存在
            MchInfo mchInfo = mchInfoMapper.selectById(mchId);
            if (mchInfo == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户账户开户失败, 商户不存在. userId={}, mchId={}", userId, mchId);
                return 0;
            }
            if (!mchInfo.getStatus().equals((byte) 1)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户账户开户失败, 无效的商户. userId={}, mchId={}, status={}",
                        userId, mchId, mchInfo.getStatus());
                return 0;
            }

            // 校验账户是否存在
            UserAccount userAccount = userAccountMapper.selectByPrimaryKey(userId, mchId);
            if (userAccount != null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户账户开户失败, 账户已存在. userId={}, mchId={}, status={}",
                        userId, mchId, mchInfo.getStatus());
                return 0;
            }

            //添加账户信息
            Long updateTimeJava = System.currentTimeMillis();
            String accountCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, 0L, updateTimeJava);
            userAccount = new UserAccount();
            userAccount.setUserId(userId);
            userAccount.setMchId(mchId);
            userAccount.setCheckSum(accountCheckSum);
            userAccount.setUpdateTimeJava(updateTimeJava);
            userAccount.setUseableCheckSum(accountCheckSum);
            userAccount.setUseableUpdateTimeJava(updateTimeJava);
            int userAccountRetNum = userAccountMapper.insertSelective(userAccount);
            if (userAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户账户开户失败, 添加账户信息失败. userId={}, mchId={}, userAccountRetNum={}",
                        userId, mchId, userAccountRetNum);
                return 0;
            }

            // 记录账户日志信息(账户和可用账户)
            List<UserAccountLog> userAccountLogList = new ArrayList<>(2);
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(0L);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_INIT.getType()); // 初始化
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType()); // 总账户
            userAccountLog.setOldBalance(0L);
            userAccountLog.setOldCheckSum(accountCheckSum);
            userAccountLog.setOldUpdateTimeJava(updateTimeJava);
            userAccountLog.setNewBalance(0L);
            userAccountLog.setNewCheckSum(accountCheckSum);
            userAccountLog.setNewUpdateTimeJava(updateTimeJava);
            userAccountLogList.add(userAccountLog);
            UserAccountLog useableUserAccountLog = new UserAccountLog();
            useableUserAccountLog.setUserId(userId);
            useableUserAccountLog.setMchId(mchId);
            useableUserAccountLog.setChangeAmount(0L);
            useableUserAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_INIT.getType()); // 初始化
            useableUserAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType()); // 可用账户
            useableUserAccountLog.setOldBalance(0L);
            useableUserAccountLog.setOldCheckSum(accountCheckSum);
            useableUserAccountLog.setOldUpdateTimeJava(updateTimeJava);
            useableUserAccountLog.setNewBalance(0L);
            useableUserAccountLog.setNewCheckSum(accountCheckSum);
            useableUserAccountLog.setNewUpdateTimeJava(updateTimeJava);
            userAccountLogList.add(useableUserAccountLog);
            int userAccountLogRetNum = userAccountLogMapper.insertBatch(userAccountLogList);
            if (userAccountLogRetNum != userAccountLogList.size()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户账户开户失败, 记录账户日志信息失败. userId={}, mchId={}, userAccountLogRetNum={}",
                        userId, mchId, userAccountLogRetNum);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("用户账户开户失败, {}. userId={}, mchId={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, e);
            return 0;
        }
    }

    /**
     * 用户资金转入
     * @param mchId        商户ID
     * @param userId       用户ID
     * @param changeAmount 转入金额
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    @Override
    public Integer userAccountRollIn(Long mchId, String userId, Long changeAmount) {
        if (ObjectValidUtil.isInvalid(userId, mchId, changeAmount)) {
            _log.warn("用户资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, mchId, changeAmount);
            return 0;
        }
        try {
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("用户资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), userId, mchId, changeAmount);
                return 0;
            }

            //校验checksum
            String oldCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                    userAccountCheck.getBalance(), userAccountCheck.getUpdateTimeJava());
            if (oldCheckSum == null || !oldCheckSum.equals(userAccountCheck.getCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), userId, mchId, changeAmount);
                return 0;
            }

            // 计算新的checksum
            Long updateTimeJava = System.currentTimeMillis();
            Long newBalance = userAccountCheck.getBalance() + changeAmount;
            String newCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newBalance, updateTimeJava);

            // 更新账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            updateUserAccount.setBalance(newBalance);
            updateUserAccount.setCheckSum(newCheckSum);
            updateUserAccount.setUpdateTimeJava(updateTimeJava);
            updateUserAccount.setTotalRollIn(userAccountCheck.getTotalRollIn() + changeAmount);
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 修改账户信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "updateUserAccountRetNum={}",
                        userId, mchId, changeAmount, updateUserAccountRetNum);
                return 0;
            }

            // 记录账户日志信息
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(changeAmount);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountLog.setOldBalance(userAccountCheck.getBalance());
            userAccountLog.setOldCheckSum(userAccountCheck.getCheckSum());
            userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUpdateTimeJava());
            userAccountLog.setNewBalance(newBalance);
            userAccountLog.setNewCheckSum(newCheckSum);
            userAccountLog.setNewUpdateTimeJava(updateTimeJava);
            int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
            if (userAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 记录账户日志信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "userAccountLogRetNum={}",
                        userId, mchId, changeAmount, userAccountLogRetNum);
                return 0;
            }

            // 插入用户账户明细信息
            UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
            userAccountChangeDetail.setUserId(userId);
            userAccountChangeDetail.setMchId(mchId);
            userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            userAccountChangeDetail.setChangeAmount(changeAmount);
            userAccountChangeDetail.setChangeLogId(userAccountLog.getLogId());
            int changeUserAccountChangeDetailRetNum = userAccountChangeDetailMapper
                    .insertSelective(userAccountChangeDetail);
            if (changeUserAccountChangeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 插入用户账户明细信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "changeUserAccountChangeDetailRetNum={}",
                        userId, mchId, changeAmount, changeUserAccountChangeDetailRetNum);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("用户资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, changeAmount, e);
            return 0;
        }
    }

    /**
     * 用户资金转出
     * @param mchId        商户ID
     * @param userId       用户ID
     * @param changeAmount 转出金额
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = java.lang.Exception.class)
    @Override
    public Integer userAccountRollOut(Long mchId, String userId, Long changeAmount) {
        if (ObjectValidUtil.isInvalid(userId, mchId, changeAmount)) {
            _log.warn("用户资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, mchId, changeAmount);
            return 0;
        }
        try {
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("用户资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), userId, mchId, changeAmount);
                return 0;
            }

            // 校验账户余额
            if (userAccountCheck.getBalance().compareTo(changeAmount) < 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_BALANCE_NOT_ENOUGH.getMessage(), userId, mchId, changeAmount);
                return 0;
            }

            //校验checksum
            String oldCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                    userAccountCheck.getBalance(), userAccountCheck.getUpdateTimeJava());
            if (oldCheckSum == null || !oldCheckSum.equals(userAccountCheck.getCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), userId, mchId, changeAmount);
                return 0;
            }

            // 计算新的checksum
            Long updateTimeJava = System.currentTimeMillis();
            Long newBalance = userAccountCheck.getBalance() - changeAmount;
            String newCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newBalance, updateTimeJava);

            // 更新账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            updateUserAccount.setBalance(newBalance);
            updateUserAccount.setCheckSum(newCheckSum);
            updateUserAccount.setUpdateTimeJava(updateTimeJava);
            updateUserAccount.setTotalRollOut(userAccountCheck.getTotalRollOut() + changeAmount);
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 修改账户信息失败. userId={}, mchId={}, changeAmount={}",
                        userId, mchId, changeAmount);
                return 0;
            }

            // 记录账户日志信息
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(changeAmount);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountLog.setOldBalance(userAccountCheck.getBalance());
            userAccountLog.setOldCheckSum(userAccountCheck.getCheckSum());
            userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUpdateTimeJava());
            userAccountLog.setNewBalance(newBalance);
            userAccountLog.setNewCheckSum(newCheckSum);
            userAccountLog.setNewUpdateTimeJava(updateTimeJava);
            int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
            if (userAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 记录账户日志信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "userAccountLogRetNum={}",
                        userId, mchId, changeAmount, userAccountLogRetNum);
                return 0;
            }

            // 插入用户账户明细信息
            UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
            userAccountChangeDetail.setUserId(userId);
            userAccountChangeDetail.setMchId(mchId);
            userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            userAccountChangeDetail.setChangeAmount(changeAmount);
            userAccountChangeDetail.setChangeLogId(userAccountLog.getLogId());
            int changeUserAccountChangeDetailRetNum = userAccountChangeDetailMapper
                    .insertSelective(userAccountChangeDetail);
            if (changeUserAccountChangeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 插入用户账户明细信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "changeUserAccountChangeDetailRetNum={}",
                        userId, mchId, changeAmount, changeUserAccountChangeDetailRetNum);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("用户资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, changeAmount, e);
            return 0;
        }
    }

    /**
     * 查询用户账户信息
     * @param mchId  商户ID
     * @param userId 用户ID
     * @return
     */
    @Override
    public UserAccount getUserAccount(Long mchId, String userId) {
        Map<String, Object> checkMap = checkMchAndAccount(userId, mchId);
        RetEnum checkRetEnum = (RetEnum) checkMap.get("retEnum");
        if (checkRetEnum != null && !RetEnum.RET_SUCCESS.equals(checkRetEnum)) {
            _log.warn("查询用户账户信息失败, {}. userId={}, mchId={}", checkRetEnum.getMessage(), userId, mchId);
            return null;
        }
        return (UserAccount) checkMap.get("account");
    }

    /**
     * 查询用户账户明细列表
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeDay 账户变动日期
     * @param changeType 变动类型 0:转入 1:转出 默认为0
     * @param accountType 账户类型 0:账户 1:可用账户 默认为0
     * @param curPage 当前页数
     * @param viewNumber 每页显示条数
     * @return
     */
    @Override
    public List<UserAccountChangeDetail> getUserAccountDetailList(Long mchId, String userId, Integer changeDay,
                                                                  Short changeType, Short accountType, Integer curPage, Integer viewNumber) {
        /*if (curPage == null) curPage = 0;
        if (viewNumber == null) viewNumber = 20;
        if (ObjectValidUtil.isInvalidCurPage(curPage)) {
            _log.warn("查询用户账户明细列表失败, {}. userId={}, mchId={}, changeDay={}, curPage={}, viewNumber={}",
                    RetEnum.RET_CURRENT_PAGE_INVALID.getMessage(), userId, mchId, changeDay, curPage, viewNumber);
            return null;
        }
        if (ObjectValidUtil.isInvalidViewNumber(viewNumber)) {
            _log.warn("查询用户账户明细列表失败, {}. userId={}, mchId={}, changeDay={}, curPage={}, viewNumber={}",
                    RetEnum.RET_VIEW_NUMBER_INVALID.getMessage(), userId, mchId, changeDay, curPage, viewNumber);
            return null;
        }*/
        Map<String, Object> checkMap = checkMchAndAccount(userId, mchId);
        RetEnum checkRetEnum = (RetEnum) checkMap.get("retEnum");
        if (checkRetEnum != null && !RetEnum.RET_SUCCESS.equals(checkRetEnum)) {
            _log.warn("查询用户账户明细列表失败, {}. userId={}, mchId={}, changeDay={}",
                    checkRetEnum.getMessage(), userId, mchId, changeDay);
            return null;
        }

        return userAccountChangeDetailMapper.selectListByPage(
                userId, mchId, changeDay, changeType, (accountType == null || -99 == accountType) ? null : accountType, curPage, viewNumber);
    }

    /**
     * 查询用户账户明细总数
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeDay 账户变动日期
     * @param changeType 变动类型 0:转入 1:转出
     * @param accountType 账户类型 0:账户 1:可用账户
     * @return
     */
    @Override
    public Integer getUserAccountDetailTotalCount(Long mchId, String userId, Integer changeDay, Short changeType, Short accountType) {
        Map<String, Object> checkMap = checkMchAndAccount(userId, mchId);
        RetEnum checkRetEnum = (RetEnum) checkMap.get("retEnum");
        if (checkRetEnum != null && !RetEnum.RET_SUCCESS.equals(checkRetEnum)) {
            _log.warn("查询用户账户明细总数失败, {}. userId={}, mchId={}, changeDay={}",
                    checkRetEnum.getMessage(), userId, mchId, changeDay);
            return 0;
        }
        UserAccountChangeDetailExample example = new UserAccountChangeDetailExample();
        UserAccountChangeDetailExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andMchIdEqualTo(mchId);
        if (accountType != null) {
            criteria.andAccountTypeEqualTo(accountType);
        }
        if (changeType != null) {
            criteria.andChangeTypeEqualTo(changeType);
        }
        if (changeDay != null) {
            criteria.andChangeDayEqualTo(changeDay);
        }
        return userAccountChangeDetailMapper.countByExample(example);
    }

    private Map<String, Object> checkMchAndAccount(String userId, Long mchId) {
        Map<String, Object> resultMap = new HashMap<>(2);
        if (ObjectValidUtil.isInvalid(userId, mchId)) {
            resultMap.put("retEnum", RetEnum.RET_PARAM_INVALID);
            return resultMap;
        }
        UserAccount userAccount = userAccountMapper.selectByPrimaryKey(userId, mchId);
        if (userAccount == null) {
            resultMap.put("retEnum", RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST);
            return resultMap;
        }
        if (userAccount.getState().equals((short) 0)) {
            resultMap.put("retEnum", RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN);
            return resultMap;
        }
        resultMap.put("account", userAccount);
        return resultMap;
    }

    /**
     * 给他人转账
     * @param mchId        商户ID
     * @param userId       转出用户ID
     * @param toUserId     接收用户ID
     * @param changeAmount 转账金额
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = java.lang.Exception.class)
    @Override
    public Integer transferAccount(Long mchId, String userId, String toUserId, Long changeAmount) {
        if (ObjectValidUtil.isInvalid(userId, toUserId, mchId, changeAmount)) {
            _log.warn("给他人转账失败, {}. userId={}, toUserId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, toUserId, mchId, changeAmount);
            return 0;
        }
        if (userId.equals(toUserId)) {
            _log.warn("给他人转账失败, {}. userId={}, toUserId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_BIZ_ACCOUNT_CANT_TRANSGER_TO_SELF.getMessage(), userId, toUserId, mchId, changeAmount);
            return 0;
        }
        // 校验商户是否存在
        MchInfo mchInfo = mchInfoMapper.selectById(mchId);
        if (mchInfo == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.warn("给他人转账失败, 商户不存在. userId={}, mchId={}", userId, mchId);
            return 0;
        }
        if (!mchInfo.getStatus().equals((byte) 1)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.warn("给他人转账失败, 无效的商户. userId={}, mchId={}, status={}",
                    userId, mchId, mchInfo.getStatus());
            return 0;
        }
        try {
            // 校验账户是否存在
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("给他人转账失败, {}. userId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            if (userAccountCheck.getBalance().compareTo(changeAmount) < 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_BALANCE_NOT_ENOUGH;
                _log.warn("给他人转账失败, {}. userId={}, mchId={}, changeAmount={}, balance={}",
                        retCode.getMessage(), userId, mchId, changeAmount, userAccountCheck.getBalance());
                return 0;
            }
            UserAccount receiveUserAccountCheck = userAccountMapper.checkUserAccountExistLock(toUserId, mchId);
            if (receiveUserAccountCheck == null || receiveUserAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = receiveUserAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("给他人转账失败, {}. toUserId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), toUserId, mchId, changeAmount);
                return 0;
            }
            /*************** 转账用户相关 begin **************/
            // 校验checksum
            String oldCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                    userAccountCheck.getBalance(), userAccountCheck.getUpdateTimeJava());
            if (oldCheckSum == null || !oldCheckSum.equals(userAccountCheck.getCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("给他人转账失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            // 计算新的checksum
            Long updateTimeJava = System.currentTimeMillis();
            Long newBalance = userAccountCheck.getBalance() - changeAmount;
            String newCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newBalance, updateTimeJava);
            // 更新账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            updateUserAccount.setBalance(newBalance);
            updateUserAccount.setCheckSum(newCheckSum);
            updateUserAccount.setUpdateTimeJava(updateTimeJava);
            updateUserAccount.setTotalRollOut(userAccountCheck.getTotalRollOut() + changeAmount);
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("给他人转账失败, 修改账户信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "updateUserAccountRetNum={}",
                        userId, mchId, changeAmount, updateUserAccountRetNum);
                return 0;
            }
            // 记录账户日志信息
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(changeAmount);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountLog.setOldBalance(userAccountCheck.getBalance());
            userAccountLog.setOldCheckSum(userAccountCheck.getCheckSum());
            userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUpdateTimeJava());
            userAccountLog.setNewBalance(newBalance);
            userAccountLog.setNewCheckSum(newCheckSum);
            userAccountLog.setNewUpdateTimeJava(updateTimeJava);
            int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
            if (userAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("给他人转账失败, 记录账户日志信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "userAccountLogRetNum={}",
                        userId, mchId, changeAmount, userAccountLogRetNum);
                return 0;
            }
            // 插入用户账户明细信息
            UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
            userAccountChangeDetail.setUserId(userId);
            userAccountChangeDetail.setMchId(mchId);
            userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            userAccountChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            userAccountChangeDetail.setChangeAmount(changeAmount);
            userAccountChangeDetail.setChangeLogId(userAccountLog.getLogId());
            int changeDetailRetNum = userAccountChangeDetailMapper
                    .insertSelective(userAccountChangeDetail);
            if (changeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 插入用户账户明细信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "changeDetailRetNum={}",
                        userId, mchId, changeAmount, changeDetailRetNum);
                return 0;
            }
            /*************** 转账用户相关 end **************/
            /*************** 接收用户相关 begin **************/
            // 校验checksum
            String receiveOldCheckSum = ChecksumUtil.getInstance().generateChecksum(receiveUserAccountCheck.getUserId(),
                    receiveUserAccountCheck.getBalance(), receiveUserAccountCheck.getUpdateTimeJava());
            if (receiveOldCheckSum == null || !receiveOldCheckSum.equals(receiveUserAccountCheck.getCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("给他人转账失败, {}. toUserId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), toUserId, mchId, changeAmount);
                return 0;
            }
            // 计算新的checksum
            Long receiveUpdateTimeJava = System.currentTimeMillis();
            Long receiveNewBalance = receiveUserAccountCheck.getBalance() + changeAmount;
            String receiveNewCheckSum = ChecksumUtil.getInstance().generateChecksum(toUserId, receiveNewBalance, receiveUpdateTimeJava);
            // 更新账户信息
            UserAccount receiveUpdateUserAccount = new UserAccount();
            receiveUpdateUserAccount.setUserId(toUserId);
            receiveUpdateUserAccount.setMchId(mchId);
            receiveUpdateUserAccount.setBalance(receiveNewBalance);
            receiveUpdateUserAccount.setCheckSum(receiveNewCheckSum);
            receiveUpdateUserAccount.setUpdateTimeJava(receiveUpdateTimeJava);
            receiveUpdateUserAccount.setTotalRollIn(receiveUserAccountCheck.getTotalRollIn() + changeAmount);
            int receiveUpdateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(receiveUpdateUserAccount);
            if (receiveUpdateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 修改账户信息失败. toUserId={}, mchId={}, changeAmount={}, " +
                                "receiveUpdateUserAccountRetNum={}",
                        toUserId, mchId, changeAmount, receiveUpdateUserAccountRetNum);
                return 0;
            }
            // 记录账户日志信息
            UserAccountLog receiveUserAccountLog = new UserAccountLog();
            receiveUserAccountLog.setUserId(toUserId);
            receiveUserAccountLog.setMchId(mchId);
            receiveUserAccountLog.setChangeAmount(changeAmount);
            receiveUserAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            receiveUserAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            receiveUserAccountLog.setOldBalance(receiveUserAccountCheck.getBalance());
            receiveUserAccountLog.setOldCheckSum(receiveUserAccountCheck.getCheckSum());
            receiveUserAccountLog.setOldUpdateTimeJava(receiveUserAccountCheck.getUpdateTimeJava());
            receiveUserAccountLog.setNewBalance(receiveNewBalance);
            receiveUserAccountLog.setNewCheckSum(receiveNewCheckSum);
            receiveUserAccountLog.setNewUpdateTimeJava(receiveUpdateTimeJava);
            int receiveUserAccountLogRetNum = userAccountLogMapper.insertSelective(receiveUserAccountLog);
            if (receiveUserAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 记录账户日志信息失败. toUserId={}, mchId={}, changeAmount={}, " +
                                "receiveUserAccountLogRetNum={}",
                        toUserId, mchId, changeAmount, receiveUserAccountLogRetNum);
                return 0;
            }
            // 插入用户账户明细信息
            UserAccountChangeDetail receiveChangeDetail = new UserAccountChangeDetail();
            receiveChangeDetail.setUserId(toUserId);
            receiveChangeDetail.setMchId(mchId);
            receiveChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            receiveChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
            receiveChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            receiveChangeDetail.setChangeAmount(changeAmount);
            receiveChangeDetail.setChangeLogId(receiveUserAccountLog.getLogId());
            int receiveChangeDetailRetNum = userAccountChangeDetailMapper.insertSelective(receiveChangeDetail);
            if (receiveChangeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转入失败, 插入用户账户明细信息失败. toUserId={}, mchId={}, changeAmount={}, " +
                                "receiveChangeDetailRetNum={}",
                        toUserId, mchId, changeAmount, receiveChangeDetailRetNum);
                return 0;
            }
            /*************** 接收用户相关 end **************/
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("给他人转账失败, {}. userId={}, mchId={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, e);
            return 0;
        }
    }

    /**
     * 用户可用资金转入
     * @param mchId        商户ID
     * @param userId       用户ID
     * @param changeAmount 可用账户变动金额
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = java.lang.Exception.class)
    @Override
    public Integer userUseableAccountRollIn(Long mchId, String userId, Long changeAmount) {
        if (ObjectValidUtil.isInvalid(userId, mchId, changeAmount)) {
            _log.warn("用户可用资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, mchId, changeAmount);
            return 0;
        }
        try {
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("用户可用资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            //校验可用账户checksum
            String oldUseableCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                    userAccountCheck.getUseableBalance(), userAccountCheck.getUseableUpdateTimeJava());
            if (oldUseableCheckSum == null || !oldUseableCheckSum.equals(userAccountCheck.getUseableCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            // 计算新的可用账户checksum
            Long useableUpdateTimeJava = System.currentTimeMillis();
            Long newUseableBalance = userAccountCheck.getUseableBalance() + changeAmount;
            String newUseableCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newUseableBalance, useableUpdateTimeJava);
            // 更新可用账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            updateUserAccount.setUseableBalance(newUseableBalance);
            updateUserAccount.setUseableCheckSum(newUseableCheckSum);
            updateUserAccount.setUseableUpdateTimeJava(useableUpdateTimeJava);
            updateUserAccount.setUseableRollIn(userAccountCheck.getUseableRollIn() + changeAmount);
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转入失败, 修改账户信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "updateUserAccountRetNum={}",
                        userId, mchId, changeAmount, updateUserAccountRetNum);
                return 0;
            }
            // 记录可用账户日志信息
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(changeAmount);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
            userAccountLog.setOldBalance(userAccountCheck.getUseableBalance());
            userAccountLog.setOldCheckSum(userAccountCheck.getUseableCheckSum());
            userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUseableUpdateTimeJava());
            userAccountLog.setNewBalance(newUseableBalance);
            userAccountLog.setNewCheckSum(newUseableCheckSum);
            userAccountLog.setNewUpdateTimeJava(useableUpdateTimeJava);
            int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
            if (userAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转入失败, 记录账户日志信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "userAccountLogRetNum={}",
                        userId, mchId, changeAmount, userAccountLogRetNum);
                return 0;
            }
            // 插入用户可用账户明细信息
            UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
            userAccountChangeDetail.setUserId(userId);
            userAccountChangeDetail.setMchId(mchId);
            userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
            userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
            userAccountChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            userAccountChangeDetail.setChangeAmount(changeAmount);
            userAccountChangeDetail.setChangeLogId(userAccountLog.getLogId());
            int changeUserAccountChangeDetailRetNum = userAccountChangeDetailMapper
                    .insertSelective(userAccountChangeDetail);
            if (changeUserAccountChangeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转入失败, 插入用户账户明细信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "changeUserAccountChangeDetailRetNum={}",
                        userId, mchId, changeAmount, changeUserAccountChangeDetailRetNum);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("用户可用资金转入失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, changeAmount, e);
            return 0;
        }
    }

    /**
     * 用户可用资金转出
     * @param mchId        商户ID
     * @param userId       用户ID
     * @param changeAmount 可用账户变动金额
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = java.lang.Exception.class)
    @Override
    public Integer userUseableAccountRollOut(Long mchId, String userId, Long changeAmount) {
        if (ObjectValidUtil.isInvalid(userId, mchId, changeAmount)) {
            _log.warn("用户可用资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), userId, mchId, changeAmount);
            return 0;
        }
        try {
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("用户可用资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        retCode.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            // 校验可用账户余额
            if (userAccountCheck.getUseableBalance().compareTo(changeAmount) < 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_USEABLE_BALANCE_NOT_ENOUGH.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            //校验可用账户checksum
            String oldUseableCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                    userAccountCheck.getUseableBalance(), userAccountCheck.getUseableUpdateTimeJava());
            if (oldUseableCheckSum == null || !oldUseableCheckSum.equals(userAccountCheck.getUseableCheckSum())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户可用资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                        RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), userId, mchId, changeAmount);
                return 0;
            }
            // 计算新的可用账户checksum
            Long useableUpdateTimeJava = System.currentTimeMillis();
            Long newUseableBalance = userAccountCheck.getUseableBalance() - changeAmount;
            String newUseableCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newUseableBalance, useableUpdateTimeJava);
            // 更新可用账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            updateUserAccount.setUseableBalance(newUseableBalance);
            updateUserAccount.setUseableCheckSum(newUseableCheckSum);
            updateUserAccount.setUseableUpdateTimeJava(useableUpdateTimeJava);
            updateUserAccount.setUseableRollOut(userAccountCheck.getUseableRollOut() + changeAmount);
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 修改账户信息失败. userId={}, mchId={}, changeAmount={}",
                        userId, mchId, changeAmount);
                return 0;
            }
            // 记录可用账户日志信息
            UserAccountLog userAccountLog = new UserAccountLog();
            userAccountLog.setUserId(userId);
            userAccountLog.setMchId(mchId);
            userAccountLog.setChangeAmount(changeAmount);
            userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
            userAccountLog.setOldBalance(userAccountCheck.getUseableBalance());
            userAccountLog.setOldCheckSum(userAccountCheck.getUseableCheckSum());
            userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUseableUpdateTimeJava());
            userAccountLog.setNewBalance(newUseableBalance);
            userAccountLog.setNewCheckSum(newUseableCheckSum);
            userAccountLog.setNewUpdateTimeJava(useableUpdateTimeJava);
            int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
            if (userAccountLogRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 记录账户日志信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "userAccountLogRetNum={}",
                        userId, mchId, changeAmount, userAccountLogRetNum);
                return 0;
            }
            // 插入用户账户明细信息
            UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
            userAccountChangeDetail.setUserId(userId);
            userAccountChangeDetail.setMchId(mchId);
            userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
            userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
            userAccountChangeDetail.setChangeDay(DateUtil.getCurrentDay());
            userAccountChangeDetail.setChangeAmount(changeAmount);
            userAccountChangeDetail.setChangeLogId(userAccountLog.getLogId());
            int changeUserAccountChangeDetailRetNum = userAccountChangeDetailMapper
                    .insertSelective(userAccountChangeDetail);
            if (changeUserAccountChangeDetailRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("用户资金转出失败, 插入用户账户明细信息失败. userId={}, mchId={}, changeAmount={}, " +
                                "changeUserAccountChangeDetailRetNum={}",
                        userId, mchId, changeAmount, changeUserAccountChangeDetailRetNum);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("用户可用资金转出失败, {}. userId={}, mchId={}, changeAmount={}",
                    RetEnum.RET_DB_FAIL.getMessage(), userId, mchId, changeAmount, e);
            return 0;
        }
    }

    /**
     * 查询账户列表记录
     * @param pageIndex
     * @param pageSize
     * @param userAccount
     * @return
     */
    public List<UserAccount> selectUserAccount(int pageIndex, int pageSize, UserAccount userAccount) {
        UserAccountExample example = new UserAccountExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(pageIndex);
        example.setLimit(pageSize);
        UserAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, userAccount);
        return userAccountMapper.selectByExample(example);
    }

    /**
     * 账户记录数
     * @param userAccount
     * @return
     */
    public int countUserAccount(UserAccount userAccount) {
        UserAccountExample example = new UserAccountExample();
        UserAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, userAccount);
        return userAccountMapper.countByExample(example);
    }

    void setCriteria(UserAccountExample.Criteria criteria, UserAccount userAccount) {
        if(userAccount != null) {
            if(userAccount.getMchId() != null) criteria.andMchIdEqualTo(userAccount.getMchId());
            if(StringUtils.isNotBlank(userAccount.getUserId())) criteria.andUserIdEqualTo(userAccount.getUserId());
            if(userAccount.getState() != null && userAccount.getState() != -99) criteria.andStateEqualTo(userAccount.getState());

        }
    }

    /**
     * 修改账户余额及可用余额
     * @param mchId
     * @param userId
     * @param changeAmount
     * @param changeType 1:增加,-1:减少
     * @param useableChangeAmount
     * @param useableChangeType 1:增加,-1:减少
     * @return
     */
    @Transactional(transactionManager = "transactionManager", rollbackFor = java.lang.Exception.class)
    @Override
    public Integer updateUserAccount(Long mchId, String userId, Long changeAmount, Short changeType,
                                     Long useableChangeAmount, Short useableChangeType) {
        if (ObjectValidUtil.isInvalid(userId, mchId)) {
            _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, changeType={}, " +
                            "useableChangeAmount={}, useableChangeType={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), mchId, userId, changeAmount, changeType,
                    useableChangeAmount, useableChangeType);
            return 0;
        }
        if (ObjectValidUtil.isInvalid(changeAmount) && ObjectValidUtil.isInvalid(useableChangeAmount)) {
            _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, changeType={}, " +
                            "useableChangeAmount={}, useableChangeType={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), mchId, userId, changeAmount, changeType,
                    useableChangeAmount, useableChangeType);
            return 0;
        }
        if (ObjectValidUtil.isValid(changeAmount) && (changeType == null
                || (changeType.shortValue() != -1 && changeType.shortValue() != 1))) {
            _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, changeType={}, " +
                            "useableChangeAmount={}, useableChangeType={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), mchId, userId, changeAmount, changeType,
                    useableChangeAmount, useableChangeType);
            return 0;
        }
        if (ObjectValidUtil.isValid(useableChangeAmount) && (useableChangeType == null ||
                (useableChangeType.shortValue() != -1 && useableChangeType.shortValue() != 1))) {
            _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, changeType={}, " +
                            "useableChangeAmount={}, useableChangeType={}",
                    RetEnum.RET_PARAM_INVALID.getMessage(), mchId, userId, changeAmount, changeType,
                    useableChangeAmount, useableChangeType);
            return 0;
        }
        try {
            UserAccount userAccountCheck = userAccountMapper.checkUserAccountExistLock(userId, mchId);
            if (userAccountCheck == null || userAccountCheck.getState().equals((short) 0)) {//帐户不存在或已被冻结
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                RetEnum retCode = userAccountCheck == null ? RetEnum.RET_BIZ_ACCOUNT_NOT_EXIST :
                        RetEnum.RET_BIZ_ACCOUNT_ALLREADY_FROZEN;
                _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, changeType={}, " +
                                "useableChangeAmount={}, useableChangeType={}",
                        retCode.getMessage(), mchId, userId, changeAmount, changeType,
                        useableChangeAmount, useableChangeType);
                return 0;
            }

            Long updateTimeJava = System.currentTimeMillis();
            Long newBalance = null;
            String newCheckSum = null;
            Long useableNewBalance = null;
            String useableNewCheckSum = null;
            if (ObjectValidUtil.isValid(changeAmount)) {
                // 如果是减少金额,需要判断余额
                if (changeType.intValue() == -1 && userAccountCheck.getBalance().compareTo(changeAmount) < 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, {}. userId={}, mchId={}, changeAmount={}",
                            RetEnum.RET_BIZ_ACCOUNT_BALANCE_NOT_ENOUGH.getMessage(), userId, mchId, changeAmount);
                    return 0;
                }
                // 校验账户余额checksum
                String oldCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                        userAccountCheck.getBalance(), userAccountCheck.getUpdateTimeJava());
                if (oldCheckSum == null || !oldCheckSum.equals(userAccountCheck.getCheckSum())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, " +
                                    "changeType={}, useableChangeAmount={}, useableChangeType={}",
                            RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), mchId, userId, changeAmount, changeType,
                            useableChangeAmount, useableChangeType);
                    return 0;
                }
                // 计算新的账户余额checksum
                newBalance = changeType.shortValue() == 1 ? userAccountCheck.getBalance() + changeAmount :
                        userAccountCheck.getBalance() - changeAmount;
                newCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, newBalance, updateTimeJava);
            }

            if (ObjectValidUtil.isValid(useableChangeAmount)) {
                // 如果是减少金额,需要判断余额
                if (useableChangeType.intValue() == -1 && userAccountCheck.getUseableBalance().compareTo(useableChangeAmount) < 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, {}. userId={}, mchId={}, changeAmount={}",
                            RetEnum.RET_BIZ_ACCOUNT_USEABLE_BALANCE_NOT_ENOUGH.getMessage(), userId, mchId, changeAmount);
                    return 0;
                }
                //校验可用账户余额checksum
                String oldUseableCheckSum = ChecksumUtil.getInstance().generateChecksum(userAccountCheck.getUserId(),
                        userAccountCheck.getUseableBalance(), userAccountCheck.getUseableUpdateTimeJava());
                if (oldUseableCheckSum == null || !oldUseableCheckSum.equals(userAccountCheck.getUseableCheckSum())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, " +
                                    "changeType={}, useableChangeAmount={}, useableChangeType={}",
                            RetEnum.RET_BIZ_ACCOUNT_CHECKSUM_ERROR.getMessage(), mchId, userId, changeAmount, changeType,
                            useableChangeAmount, useableChangeType);
                    return 0;
                }
                // 计算新的账户余额checksum
                useableNewBalance = useableChangeType.shortValue() == 1 ?
                        userAccountCheck.getUseableBalance() + useableChangeAmount :
                        userAccountCheck.getUseableBalance() - useableChangeAmount;
                useableNewCheckSum = ChecksumUtil.getInstance().generateChecksum(userId, useableNewBalance, updateTimeJava);
            }

            // 更新账户信息
            UserAccount updateUserAccount = new UserAccount();
            updateUserAccount.setUserId(userId);
            updateUserAccount.setMchId(mchId);
            if (ObjectValidUtil.isValid(newBalance, newCheckSum)) {
                updateUserAccount.setBalance(newBalance);
                updateUserAccount.setCheckSum(newCheckSum);
                updateUserAccount.setUpdateTimeJava(updateTimeJava);
                if (changeType.intValue() == 1) {
                    updateUserAccount.setTotalRollIn(userAccountCheck.getTotalRollIn() + changeAmount);
                } else {
                    updateUserAccount.setTotalRollOut(userAccountCheck.getTotalRollOut() + changeAmount);
                }
            }
            if (ObjectValidUtil.isValid(useableNewBalance, useableNewCheckSum)) {
                updateUserAccount.setUseableBalance(useableNewBalance);
                updateUserAccount.setUseableCheckSum(useableNewCheckSum);
                updateUserAccount.setUseableUpdateTimeJava(updateTimeJava);
                if (useableChangeType.intValue() == 1) {
                    updateUserAccount.setUseableRollIn(userAccountCheck.getUseableRollIn() + useableChangeAmount);
                } else {
                    updateUserAccount.setUseableRollOut(userAccountCheck.getUseableRollOut() + useableChangeAmount);
                }
            }
            int updateUserAccountRetNum = userAccountMapper.updateByPrimaryKeySelective(updateUserAccount);
            if (updateUserAccountRetNum != 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("修改账户余额及可用余额失败, 修改账户信息失败. mchId={}, userId={}, changeAmount={}, " +
                                "changeType={}, useableChangeAmount={}, useableChangeType={}",
                        mchId, userId, changeAmount, changeType, useableChangeAmount, useableChangeType);
                return 0;
            }

            Long changeLogId = null;
            Long useableChangeLogId = null;
            // 记录账户日志信息
            if (ObjectValidUtil.isValid(newBalance, newCheckSum)) {
                UserAccountLog userAccountLog = new UserAccountLog();
                userAccountLog.setUserId(userId);
                userAccountLog.setMchId(mchId);
                userAccountLog.setChangeAmount(changeAmount);
                if (changeType.intValue() == 1) {
                    userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
                } else {
                    userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
                }
                userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
                userAccountLog.setOldBalance(userAccountCheck.getBalance());
                userAccountLog.setOldCheckSum(userAccountCheck.getCheckSum());
                userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUpdateTimeJava());
                userAccountLog.setNewBalance(newBalance);
                userAccountLog.setNewCheckSum(newCheckSum);
                userAccountLog.setNewUpdateTimeJava(updateTimeJava);
                int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
                if (userAccountLogRetNum != 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, 记录账户日志信息失败. mchId={}, userId={}, changeAmount={}, " +
                                    "changeType={}, useableChangeAmount={}, useableChangeType={}",
                            RetEnum.RET_DB_FAIL.getMessage(), mchId, userId, changeAmount, changeType,
                            useableChangeAmount, useableChangeType);
                    return 0;
                }
                changeLogId = userAccountLog.getLogId();
            }
            // 记录可用账户日志信息
            if (ObjectValidUtil.isValid(useableNewBalance, useableNewCheckSum)) {
                UserAccountLog userAccountLog = new UserAccountLog();
                userAccountLog.setUserId(userId);
                userAccountLog.setMchId(mchId);
                userAccountLog.setChangeAmount(useableChangeAmount);
                if (useableChangeType.intValue() == 1) {
                    userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
                } else {
                    userAccountLog.setType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
                }
                userAccountLog.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
                userAccountLog.setOldBalance(userAccountCheck.getUseableBalance());
                userAccountLog.setOldCheckSum(userAccountCheck.getUseableCheckSum());
                userAccountLog.setOldUpdateTimeJava(userAccountCheck.getUseableUpdateTimeJava());
                userAccountLog.setNewBalance(useableNewBalance);
                userAccountLog.setNewCheckSum(useableNewCheckSum);
                userAccountLog.setNewUpdateTimeJava(updateTimeJava);
                int userAccountLogRetNum = userAccountLogMapper.insertSelective(userAccountLog);
                if (userAccountLogRetNum != 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                    _log.warn("修改账户余额及可用余额失败, 记录可用账户日志信息失败. mchId={}, userId={}, changeAmount={}, " +
                                    "changeType={}, useableChangeAmount={}, useableChangeType={}",
                            RetEnum.RET_DB_FAIL.getMessage(), mchId, userId, changeAmount, changeType,
                            useableChangeAmount, useableChangeType);
                    return 0;
                }
                useableChangeLogId = userAccountLog.getLogId();
            }

            // 批量插入用户账户明细信息
            Integer currentDay = DateUtil.getCurrentDay();
            List<UserAccountChangeDetail> userAccountChangeDetailList = new ArrayList<>(2);
            if (ObjectValidUtil.isValid(newBalance, newCheckSum)) {
                UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
                userAccountChangeDetail.setUserId(userId);
                userAccountChangeDetail.setMchId(mchId);
                if (changeType.intValue() == 1) {
                    userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
                } else {
                    userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
                }
                userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_TOTAL.getType());
                userAccountChangeDetail.setChangeDay(currentDay);
                userAccountChangeDetail.setChangeAmount(changeAmount);
                userAccountChangeDetail.setChangeLogId(changeLogId);
                userAccountChangeDetailList.add(userAccountChangeDetail);
            }
            if (ObjectValidUtil.isValid(useableNewBalance, useableNewCheckSum)) {
                UserAccountChangeDetail userAccountChangeDetail = new UserAccountChangeDetail();
                userAccountChangeDetail.setUserId(userId);
                userAccountChangeDetail.setMchId(mchId);
                if (useableChangeType.intValue() == 1) {
                    userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_IN.getType());
                } else {
                    userAccountChangeDetail.setChangeType(AccountChangeTypeEnum.ACCOUNT_CHANGE_TYPE_OUT.getType());
                }
                userAccountChangeDetail.setAccountType(AccountTypeEnum.ACCOUNT_TYPE_USEABLE.getType());
                userAccountChangeDetail.setChangeDay(currentDay);
                userAccountChangeDetail.setChangeAmount(useableChangeAmount);
                userAccountChangeDetail.setChangeLogId(useableChangeLogId);
                userAccountChangeDetailList.add(userAccountChangeDetail);
            }
            int changeUserAccountChangeDetailRetNum = userAccountChangeDetailMapper
                    .insertBatch(userAccountChangeDetailList);
            if (changeUserAccountChangeDetailRetNum != userAccountChangeDetailList.size()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                _log.warn("修改账户余额及可用余额失败, 插入用户账户明细信息失败. mchId={}, userId={}, changeAmount={}, " +
                                "changeType={}, useableChangeAmount={}, useableChangeType={}",
                        RetEnum.RET_DB_FAIL.getMessage(), mchId, userId, changeAmount, changeType,
                        useableChangeAmount, useableChangeType);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
            _log.error("修改账户余额及可用余额失败, {}. mchId={}, userId={}, changeAmount={}, " +
                            "changeType={}, useableChangeAmount={}, useableChangeType={}",
                    RetEnum.RET_DB_FAIL.getMessage(), mchId, userId, changeAmount, changeType,
                    useableChangeAmount, useableChangeType, e);
            return 0;
        }
    }

}
