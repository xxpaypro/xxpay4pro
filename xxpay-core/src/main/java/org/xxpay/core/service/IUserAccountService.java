package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.UserAccount;
import org.xxpay.core.entity.UserAccountChangeDetail;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanglei
 * Date: 18/3/5 下午4:39
 * Description: 用户账户服务接口
 */
public interface IUserAccountService extends IService<UserAccount> {

    /**
     * 用户账户开户
     * @param mchId 商户ID
     * @param userId 用户ID
     * @return
     */
    Integer openAccount(Long mchId, String userId);

    /**
     * 用户资金转入
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeAmount 转入金额
     * @return
     */
    Integer userAccountRollIn(Long mchId, String userId, Long changeAmount);

    /**
     * 用户资金转出
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeAmount 转出金额
     * @return
     */
    Integer userAccountRollOut(Long mchId, String userId, Long changeAmount);

    /**
     * 查询用户账户信息
     * @param mchId 商户ID
     * @param userId 用户ID
     * @return
     */
    UserAccount getUserAccount(Long mchId, String userId);

    /**
     * 查询用户账户明细列表
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeDay 账户变动日期
     * @param changeType 变动类型 0:转入 1:转出
     * @param accountType 账户类型 0:账户 1:可用账户
     * @param curPage 当前页数
     * @param viewNumber 每页显示条数
     * @return
     */
    List<UserAccountChangeDetail> getUserAccountDetailList(Long mchId, String userId, Integer changeDay,
                                                           Short changeType, Short accountType, Integer curPage, Integer viewNumber);

    /**
     * 查询用户账户明细总数
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeDay 账户变动日期
     * @param changeType 变动类型 0:转入 1:转出
     * @param accountType 账户类型 0:账户 1:可用账户
     * @return
     */
    Integer getUserAccountDetailTotalCount(Long mchId, String userId, Integer changeDay, Short changeType, Short accountType);

    /**
     * 给他人转账
     * @param mchId 商户ID
     * @param userId 转出用户ID
     * @param toUserId 接收用户ID
     * @param changeAmount 转账金额
     * @return
     */
    Integer transferAccount(Long mchId, String userId, String toUserId, Long changeAmount);

    /**
     * 用户可用资金转入
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeAmount 可用账户变动金额
     * @return
     */
    Integer userUseableAccountRollIn(Long mchId, String userId, Long changeAmount);

    /**
     * 用户可用资金转出
     * @param mchId 商户ID
     * @param userId 用户ID
     * @param changeAmount 可用账户变动金额
     * @return
     */
    Integer userUseableAccountRollOut(Long mchId, String userId, Long changeAmount);

    /**
     * 查询账户列表记录
     * @param pageIndex
     * @param pageSize
     * @param userAccount
     * @return
     */
    List<UserAccount> selectUserAccount(int pageIndex, int pageSize, UserAccount userAccount);

    /**
     * 账户记录数
     * @param userAccount
     * @return
     */
    int countUserAccount(UserAccount userAccount);

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
    Integer updateUserAccount(Long mchId, String userId, Long changeAmount, Short changeType,
                              Long useableChangeAmount, Short useableChangeType);

}
