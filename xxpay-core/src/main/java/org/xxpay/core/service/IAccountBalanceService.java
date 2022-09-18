package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.PayOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: 账户/余额 service
 * @Author terrfly
 * @Date 2018/11/26 11:49
 */
public interface IAccountBalanceService extends IService<AccountBalance> {

    /**
     * 查询所有账户信息（一行显示），并放置在map中
     * @param infoType
     * @param infoId
     * @return
     */
    Map<String, Object> selectLineByInfoId(byte infoType, long infoId);

    List<AccountBalance> listAll(int offset, int limit);


    AccountBalance findOne(byte infoType, long infoId, byte accountType);

    /**
     * 只更新 不可用余额字段。
     * @param balanceRecord 必须先查询记录，并根据查询的记录进行业务判断，然后将查询到的记录传入方法，保证更新的记录与查询的记录一致
     * @param changeUnAmount 需要变动的不可用金额（增加为正数，减少为负数）
     */
    void changeUnAmountOnly(AccountBalance balanceRecord, Long changeUnAmount);

    /**
     * 只更新 不可用余额字段。(业务无需验证资金是否充足的情况，例如解冻金额)
     * @param  infoType
     * @param  infoId
     * @param  accountType
     * @param  changeUnAmount 需要变动的不可用金额（增加为正数，减少为负数）
     */
    void changeUnAmountOnly(byte infoType, long infoId, byte accountType, Long changeUnAmount);


    /**
     * 只更新 可结算余额字段。
     * @param balanceRecord 必须先查询记录，并根据查询的记录进行业务判断，然后将查询到的记录传入方法，保证更新的记录与查询的记录一致
     * @param changeSettAmount 需要变动的可提现金额（增加为正数，减少为负数）
     */
    void changeSettAmountOnly(AccountBalance balanceRecord, Long changeSettAmount);

    /**
     * 只更新 可结算余额字段。(业务无需验证资金是否充足的情况，例如解冻金额)
     * @param  infoType
     * @param  infoId
     * @param  accountType
     * @param  changeSettAmount 需要变动的可提现金额（增加为正数，减少为负数）
     */
    void changeSettAmountOnly(byte infoType, long infoId, byte accountType, Long changeSettAmount);


    /**
     * 初始化 accountBalance 表记录
     * @param infoType
     * @param infoId
     * @param infoName
     * @param accountTypes 类型 动态数组
     */
    public int initAccount(Byte infoType, Long infoId, String infoName, byte... accountTypes);


    /**
     * 更新账户余额并记录 history （不包含biz 订单信息）
     * @param infoType
     * @param infoId
     * @param bizType
     * @param bizItem
     * @param changeAmount 变动金额（增加为正数，减少为负数）
     * @param remark
     */
    void changeAmountAndInsertHistory(byte infoType, long infoId, Byte bizType, String bizItem, Long changeAmount, String remark);


    /**
     * 更新账户余额并记录 history （包含biz 订单信息）
     * @param infoType
     * @param infoId
     * @param bizType
     * @param bizItem
     * @param changeAmount  变动金额（增加为正数，减少为负数）
     * @param remark
     * @param bizOrderId
     * @param bizChannelOrderNo
     * @param bizOrderAmount
     * @param bizOrderFee
     */
    void changeAmountAndInsertHistory(byte infoType, long infoId, Byte bizType, String bizItem, Long changeAmount, String remark,
                                      String bizOrderId, String bizChannelOrderNo, Long bizOrderAmount, Long bizOrderFee);




}
