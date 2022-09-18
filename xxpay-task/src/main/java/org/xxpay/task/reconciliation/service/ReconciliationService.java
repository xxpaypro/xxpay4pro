package org.xxpay.task.reconciliation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.CheckBatch;
import org.xxpay.core.entity.CheckMistake;
import org.xxpay.core.entity.CheckMistakePool;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.task.common.service.RpcCommonService;
import org.xxpay.task.reconciliation.entity.ReconciliationEntity;
import org.xxpay.task.reconciliation.enums.BatchStatusEnum;
import org.xxpay.task.reconciliation.enums.MistakeHandleStatusEnum;
import org.xxpay.task.reconciliation.enums.MistakeTypeEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/20
 * @description:
 */
@Service
public class ReconciliationService {

    private static final MyLog _log = MyLog.getLog(ReconciliationService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 对账核心逻辑
     * @param bankList
     * @param channelMchId
     * @param batch
     */
    public void check(List<ReconciliationEntity> bankList, String channelMchId, CheckBatch batch) {

        // 判断bankList是否为空
        if (bankList == null) {
            bankList = new ArrayList<ReconciliationEntity>();
        }

        String billDate = DateUtil.date2Str(batch.getBillDate(), DateUtil.FORMAT_YYYY_MM_DD);
        // 3.查询平台订单数据
        List<Byte> statusList = new LinkedList<>();
        statusList.add(PayConstant.PAY_STATUS_SUCCESS);
        // 账期内成功订单
        List<PayOrder> platSucessDateList = rpcCommonService.rpcPayOrderService.select(channelMchId, billDate, statusList);
        // 账期内所有订单
        List<PayOrder> platAllDateList = rpcCommonService.rpcPayOrderService.select(channelMchId, billDate, null);

        // 查询平台缓冲池中所有的数据
        List<CheckMistakePool> platScreatchRecordList = rpcCommonService.rpcCheckService.selectAllMistakePool();

        // 差错list
        List<CheckMistake> mistakeList = new ArrayList<CheckMistake>();

        // 需要放入缓冲池中平台长款list
        List<CheckMistakePool> insertScreatchRecordList = new ArrayList<CheckMistakePool>();

        // 需要从缓冲池中移除的数据
        List<CheckMistakePool> removeScreatchRecordList = new ArrayList<CheckMistakePool>();

        _log.info("  开始以平台的数据为准对账,平台长款记入缓冲池");
        base4PlatForm(platSucessDateList, bankList, mistakeList, insertScreatchRecordList, batch);
        _log.info("结束以平台的数据为准对账");

        _log.info("  开始以银行通道的数据为准对账");
        base4Bank(platAllDateList, bankList, platScreatchRecordList, mistakeList, batch, removeScreatchRecordList);
        _log.info(" 结束以银行通道的数据为准对账");

        // 保存数据
        rpcCommonService.rpcCheckService.saveData(batch, mistakeList, insertScreatchRecordList, removeScreatchRecordList);

    }


    /**
     * 以平台为主对账
     * @param platformDateList
     * @param bankList
     * @param mistakeList
     * @param screatchRecordList
     * @param batch
     */
    private void base4PlatForm(List<PayOrder> platformDateList, List<ReconciliationEntity> bankList, List<CheckMistake> mistakeList, List<CheckMistakePool> screatchRecordList, CheckBatch batch) {
        Long platTradeAmount = 0l;// 平台交易总金额
        Long platFee = 0l;// 平台总手续费
        Integer tradeCount = 0;// 平台订单总数
        Integer mistakeCount = 0;

        for (PayOrder record : platformDateList) {
            Boolean flag = false;// 用于标记是否有匹配
            // 累计平台交易总金额和总手续费
            platTradeAmount = platTradeAmount + record.getAmount();
            
            OrderProfitDetail platDetail = rpcCommonService.rpcOrderProfitDetailService.findOne(
            			MchConstant.BIZ_TYPE_TRANSACT, record.getPayOrderId(), MchConstant.INFO_TYPE_PLAT);
            platFee = platFee + (platDetail == null ? 0l : platDetail.getIncomeAmount());
            tradeCount++;
            for (ReconciliationEntity bankRecord : bankList) {
                // 如果银行账单中有匹配数据：进行金额，手续费校验
                if (bankRecord.getBankOrderNo().equalsIgnoreCase(record.getChannelOrderNo())) {
                    flag = true;// 标记已经找到匹配

                    // * step1:匹配订单金额 *
                    // 平台金额多
                    if (record.getAmount().compareTo(bankRecord.getBankAmount()) == 1) {
                        // 金额不匹配，创建差错记录
                        CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.PLATFORM_OVER_CASH_MISMATCH, batch);
                        mistakeList.add(misktake);
                        mistakeCount++;
                        break;
                    }
                    // 平台金额少
                    else if (record.getAmount().compareTo(bankRecord.getBankAmount()) == -1) {
                        // 金额不匹配，创建差错记录
                        CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.PLATFORM_SHORT_CASH_MISMATCH, batch);
                        mistakeList.add(misktake);
                        mistakeCount++;
                        break;
                    }

                    // * step2:匹配订单手续费 *
                    /*if (record.getPlatCost().compareTo(bankRecord.getBankFee()) != 0) {
                        // 金额不匹配，创建差错记录
                        CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.FEE_MISMATCH, batch);
                        mistakeList.add(misktake);
                        mistakeCount++;
                        break;
                    }*/

                }
            }
            // 没有找到匹配的记录，把这个订单记录到缓冲池中
            if (!flag) {
                CheckMistakePool screatchRecord = getScratchRecord(record, batch);
                screatchRecordList.add(screatchRecord);
            }
        }

        // 统计数据保存
        batch.setTradeAmount(platTradeAmount);
        batch.setTradeCount(tradeCount);
        batch.setFee(platFee);
        batch.setMistakeCount(mistakeCount);
    }

    /**
     * 以银行为准对账
     * @param platAllDateList
     * @param bankList
     * @param platScreatchRecordList
     * @param mistakeList
     * @param batch
     * @param removeScreatchRecordList
     */
    private void base4Bank(List<PayOrder> platAllDateList, List<ReconciliationEntity> bankList, List<CheckMistakePool> platScreatchRecordList, List<CheckMistake> mistakeList, CheckBatch batch, List<CheckMistakePool> removeScreatchRecordList) {
        Long platTradeAmount = 0l;// 平台交易总金额
        Long platFee = 0l;// 平台总手续费
        Integer tradeCount = 0;// 平台订单总数
        Integer mistakeCount = 0;
        // 拿银行数据去对账
        for (ReconciliationEntity bankRecord : bankList) {

            boolean flag = false;// 用于标记是否有匹配
            for (PayOrder record : platAllDateList) {
                // * step1 检查有匹配的数据 *
                if (bankRecord.getBankOrderNo().equals(record.getChannelOrderNo())) {
                    flag = true;
                    //* step2： 判断平台状态是否匹配 *
                    //* 注意：状态匹配不需要做金额和手续费验证，以平台数据为基准对账已经做了验证 *
                    // 不匹配记录差错。
                    if (PayConstant.PAY_STATUS_SUCCESS != record.getStatus() ) {
                        CheckMistake misktake1 = createMisktake(null, record, bankRecord, MistakeTypeEnum.PLATFORM_SHORT_STATUS_MISMATCH, batch);
                        mistakeList.add(misktake1);
                        mistakeCount++;
                        // break;

                        //* 订单状态不匹配验证完之后，在验证金额和手续费，差错处理必须先处理状态不符的情况 *
                        // 验证金额和手续费
                        //* step1:匹配订单金额 *
                        // 平台金额多
                        if (record.getAmount().compareTo(bankRecord.getBankAmount()) == 1) {
                            // 金额不匹配，创建差错记录
                            CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.PLATFORM_OVER_CASH_MISMATCH, batch);
                            mistakeList.add(misktake);
                            mistakeCount++;
                            break;
                        }
                        // 平台金额少
                        else if (record.getAmount().compareTo(bankRecord.getBankAmount()) == -1) {
                            // 金额不匹配，创建差错记录
                            CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.PLATFORM_SHORT_CASH_MISMATCH, batch);
                            mistakeList.add(misktake);
                            mistakeCount++;
                            break;
                        }

                        //* step2:匹配订单手续费 *
                        /*if (record.getPlatCost().compareTo(bankRecord.getBankFee()) != 0) {
                            // 金额不匹配，创建差错记录
                            CheckMistake misktake = createMisktake(null, record, bankRecord, MistakeTypeEnum.FEE_MISMATCH, batch);
                            mistakeList.add(misktake);
                            mistakeCount++;
                            break;
                        }*/

                    }
                }
            }

            //* step3： 如果没有匹配的数据，去缓冲池中查找对账，如果没有记录差错 *
            if (!flag) {
                // 去缓冲池中查找对账(前提是缓冲池里面有数据)
                if (platScreatchRecordList != null)
                    for (CheckMistakePool scratchRecord : platScreatchRecordList) {

                        // 找到匹配的
                        if (scratchRecord.getBankOrderNo().equals(bankRecord.getBankOrderNo())) {
                            // 累计平台交易总金额和总手续费
                            platTradeAmount = platTradeAmount + scratchRecord.getOrderAmount();
                            //platFee = platFee.add(scratchRecord.getPlatCost() == null ? BigDecimal.ZERO : scratchRecord.getPlatCost());
                            tradeCount++;
                            flag = true;

                            // 验证金额和手续费
                            //* step1:匹配订单金额 *
                            // 平台金额多
                            if (scratchRecord.getOrderAmount().compareTo(bankRecord.getBankAmount()) == 1) {
                                // 金额不匹配，创建差错记录
                                CheckMistake misktake = createMisktake(scratchRecord, null, bankRecord, MistakeTypeEnum.PLATFORM_OVER_CASH_MISMATCH, batch);
                                mistakeList.add(misktake);
                                mistakeCount++;
                                break;
                            }
                            // 平台金额少
                            else if (scratchRecord.getOrderAmount().compareTo(bankRecord.getBankAmount()) == -1) {
                                // 金额不匹配，创建差错记录
                                CheckMistake misktake = createMisktake(scratchRecord, null, bankRecord, MistakeTypeEnum.PLATFORM_SHORT_CASH_MISMATCH, batch);
                                mistakeList.add(misktake);
                                mistakeCount++;
                                break;
                            }

                            //* step2:匹配订单手续费 *
                            if (scratchRecord.getPlatCost().compareTo(bankRecord.getBankFee()) != 0) {
                                // 金额不匹配，创建差错记录
                                CheckMistake misktake = createMisktake(scratchRecord, null, bankRecord, MistakeTypeEnum.FEE_MISMATCH, batch);
                                mistakeList.add(misktake);
                                mistakeCount++;
                                break;
                            }

                            //* step3:把缓存池中匹配的记录删除掉 *
                            removeScreatchRecordList.add(scratchRecord);
                        }
                    }
            }

            // 缓冲池中还是没有这条记录,直接记录差错，差错类型为 PLATFORM_MISS("平台漏单")
            if (!flag) {
                CheckMistake misktake = createMisktake(null, null, bankRecord, MistakeTypeEnum.PLATFORM_MISS, batch);
                mistakeList.add(misktake);
                mistakeCount++;
            }
        }

        // 统计数据保存
        batch.setTradeAmount(batch.getTradeAmount() + (platTradeAmount));
        batch.setTradeCount(batch.getTradeCount() + tradeCount);
        batch.setFee(batch.getFee()+ (platFee));
        batch.setMistakeCount(batch.getMistakeCount() + mistakeCount);
    }

    /**
     * 创建差错记录
     * @param scratchRecord
     * @param record
     * @param bankRecord
     * @param mistakeType
     * @param batch
     * @return
     */
    private CheckMistake createMisktake(CheckMistakePool scratchRecord, PayOrder record, ReconciliationEntity bankRecord, MistakeTypeEnum mistakeType, CheckBatch batch) {

        CheckMistake mistake = new CheckMistake();
        mistake.setBatchNo(batch.getBatchNo());
        mistake.setBillDate(batch.getBillDate());
        mistake.setErrType(mistakeType.getCode());
        mistake.setHandleStatus(MistakeHandleStatusEnum.NOHANDLE.getCode());
        mistake.setBankType(batch.getBankType());
        mistake.setChannelMchId(batch.getChannelMchId());
        if (record != null) {
            mistake.setMchId(record.getMchId());
            mistake.setMchName(null);
            mistake.setMchOrderNo(record.getMchOrderNo());
            mistake.setOrderId((record.getPayOrderId()));
            mistake.setTradeTime(record.getCreateTime());
            mistake.setBatchNo(record.getChannelOrderNo());
            mistake.setOrderAmount(record.getAmount());
            mistake.setRefundAmount(null);
            mistake.setOrderStatus((record.getStatus()));
            mistake.setFee(null);
        }

        if (scratchRecord != null) {
            mistake.setOrderId(scratchRecord.getOrderId());
            mistake.setTradeTime(scratchRecord.getPaySuccessTime());
            mistake.setBatchNo(scratchRecord.getBatchNo());
            mistake.setOrderAmount(scratchRecord.getOrderAmount());
            mistake.setRefundAmount(scratchRecord.getSuccessRefundAmount());
            mistake.setOrderStatus(scratchRecord.getStatus());
            mistake.setFee(scratchRecord.getPlatCost());
        }

        if (bankRecord != null) {
            mistake.setBankAmount(bankRecord.getBankAmount());
            mistake.setBankFee(bankRecord.getBankFee());
            mistake.setBankOrderNo(bankRecord.getBankOrderNo());
            mistake.setBankRefundAmount(bankRecord.getBankRefundAmount());
            mistake.setBankOrderStatus((bankRecord.getBankTradeStatus()));
            mistake.setBankTradeTime(bankRecord.getBankTradeTime());
            mistake.setBatchNo(bankRecord.getBankTrxNo());
        }
        return mistake;
    }

    /**
     * 得到缓存记录：用于放入缓冲池
     * @param record
     * @param batch
     * @return
     */
    private CheckMistakePool getScratchRecord(PayOrder record, CheckBatch batch) {

        CheckMistakePool scratchRecord = new CheckMistakePool();
        scratchRecord.setBankOrderNo(record.getChannelOrderNo());
        //scratchRecord.setBankTrxNo(record.getBankTrxNo());
        scratchRecord.setMchOrderNo(record.getMchOrderNo());
        scratchRecord.setOrderId(record.getPayOrderId());
        if(record.getPaySuccTime() != null) scratchRecord.setCompleteTime(record.getPaySuccTime());
        if(record.getPaySuccTime() != null) scratchRecord.setPaySuccessTime(record.getPaySuccTime());
        scratchRecord.setMchOrderNo(record.getMchOrderNo());
        scratchRecord.setOrderAmount(record.getAmount());
        scratchRecord.setPlatCost(null);
        scratchRecord.setChannelType(record.getChannelType());
        scratchRecord.setChannelId(record.getChannelId());
        scratchRecord.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        scratchRecord.setBatchNo(batch.getBatchNo());
        scratchRecord.setBillDate(batch.getBillDate());
        return scratchRecord;
    }

    /**
     * 如果缓冲池中有三天前的数据就清理掉并记录差错
     */
    public void validateScratchPool() {
        List<CheckMistakePool> list = rpcCommonService.rpcCheckService.selectScratchPoolRecord(DateUtil.addDay(new Date(), -3));
        List<CheckMistake> mistakeList = null;
        // 如果有数据
        if (!list.isEmpty()) {
            mistakeList = new ArrayList<CheckMistake>();
            for (CheckMistakePool scratchRecord : list) {
                // 创建差错记录
                CheckMistake mistake = new CheckMistake();
                mistake.setBatchNo(scratchRecord.getBatchNo());
                mistake.setBillDate(scratchRecord.getBillDate());
                mistake.setErrType(MistakeTypeEnum.BANK_MISS.getCode());
                mistake.setHandleStatus(MistakeHandleStatusEnum.NOHANDLE.getCode());
                mistake.setBankType(null);

                mistake.setOrderId(scratchRecord.getOrderId());
                mistake.setTradeTime(scratchRecord.getPaySuccessTime());
                mistake.setBankOrderNo(scratchRecord.getBankOrderNo());
                mistake.setOrderAmount(scratchRecord.getOrderAmount());
                mistake.setRefundAmount(scratchRecord.getSuccessRefundAmount());
                mistake.setOrderStatus(scratchRecord.getStatus());
                mistake.setFee(scratchRecord.getPlatCost());
                mistakeList.add(mistake);
            }

            rpcCommonService.rpcCheckService.removeDateFromPool(list, mistakeList);

        }

    }


    /**
     * 是否对过账
     * @param channelMchId
     * @param billDate
     * @return
     */
    public Boolean isChecked(String channelMchId, Date billDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String billDateStr = sdf.format(billDate);
        _log.info("检查,支付方式[" + channelMchId + "],订单日期[" + billDateStr + "]");

        CheckBatch checkBatch = new CheckBatch();
        checkBatch.setBillDate(billDate);
        checkBatch.setChannelMchId(channelMchId);
        List<CheckBatch> checkBatchList = rpcCommonService.rpcCheckService.selectCheckBatch(checkBatch);
        // 没有记录,则需要对账
        if (checkBatchList.isEmpty()) {
            return false;
        }
        // 如果有失败的,重新对账
        for(CheckBatch batch : checkBatchList) {
            if(batch.getReleaseStatus().byteValue() == BatchStatusEnum.ERROR.getCode().byteValue() ||
                    batch.getReleaseStatus().byteValue() == BatchStatusEnum.FAIL.getCode().byteValue()) {
                return false;
            }
        }
        // 已经对过账
        return true;
    }


}
