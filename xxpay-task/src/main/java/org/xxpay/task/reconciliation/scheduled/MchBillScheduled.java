package org.xxpay.task.reconciliation.scheduled;

import com.csvreader.CsvWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.Bill;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.task.common.service.RpcCommonService;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/02/01
 * @description: 生成商户对账单任务 (优化:只需要遍历一次支付订单表,可生成所有的商户对账文件)
 */
@Component
public class MchBillScheduled {

    private static final MyLog _log = MyLog.getLog(MchBillScheduled.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Value("${config.mchPayBillPath}")
    private String mchPayBillPath;

    @Value("${config.platBillPath}")
    private String platBillPath;

    @Value("${config.downBillUrl}")
    private String downBillUrl;

    //@Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron="0 45 10 ? * *")   // 每日10:45执行
    public void buildMchBillTask() {
        _log.info("生成商户对账文件,开始...");

        Map<String, Bill> handleMap = new HashedMap();
        // 1. 查询支付订单表,查询昨日,按照商户ID升序排列
        Date billDate = DateUtil.addDay(new Date(), -1);
        String billDateStr = DateUtil.date2Str(billDate, DateUtil.FORMAT_YYYY_MM_DD);
        int pageIndex = 1;
        int limit = 100;
        boolean flag = true;
        // 得到有支付产品Map
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll();
        Map<String, String> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product.getProductName());
        }

        CsvWriter csvWriter = null;
        Long lastMchId = null;

        while (flag) {
            List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.selectAllBill((pageIndex - 1) * limit, limit, billDateStr);

            for(PayOrder payOrder : payOrderList) {
                // 第一次进入,或者商户ID切换时,需要生成新的csv对象
                Long mchId = payOrder.getMchId();
                if(lastMchId == null || lastMchId.longValue() != mchId.longValue()) {

                    // 保存csv,更新账单记录为已完成
                    if(csvWriter != null) {
                        csvWriter.close();
                        rpcCommonService.rpcMchBillService.updateComplete(lastMchId, MchConstant.INFO_TYPE_MCH, billDate);
                        _log.info("商户:{},对账文件生成完成", lastMchId);
                    }

                    // 判断该商户是否已经生成过
                    Bill mchBill = rpcCommonService.rpcMchBillService.findByInfoIdAndBillDate(mchId, MchConstant.INFO_TYPE_MCH,  billDate, MchConstant.BILL_TYPE_PAY);
                    if(mchBill == null) {
                        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
                        mchBill = new Bill();
                        mchBill.setInfoId(mchId);
                        mchBill.setInfoType(MchConstant.INFO_TYPE_MCH);
                        mchBill.setInfoName(mchInfo.getMchName());
                        mchBill.setMchType(mchInfo.getType());
                        mchBill.setBillDate(billDate);
                        mchBill.setBillType(MchConstant.BILL_TYPE_PAY);
                        mchBill.setStatus(MchConstant.MCH_BILL_STATUS_INT);
                        mchBill.setBillPath(downBillUrl + mchPayBillPath + "/" + billDateStr + "/" + mchId + ".csv");
                        rpcCommonService.rpcMchBillService.add(mchBill);
                    }else {
                        // 已生成了
                        if(mchBill.getStatus().byteValue() == MchConstant.MCH_BILL_STATUS_COMPLETE) {
                            handleMap.put(mchId + billDateStr, mchBill);
                        }
                    }

                    // 判断商户账单是否生成过, 如果没有生成, 则创建csv文件并创建表头
                    if(handleMap.get(mchId + billDateStr) == null) {

                        String scvFilePath = platBillPath + mchPayBillPath + File.separator + billDateStr + File.separator + payOrder.getMchId() + ".csv";

                        // 创建目录
                        String path = scvFilePath.substring(0, scvFilePath.lastIndexOf(File.separator));
                        File file = new File(path);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        csvWriter = new CsvWriter(scvFilePath,',', Charset.forName("GBK"));
                        //_log.info("开始生成:mchId={}的对账文件", payOrder.getMchId());
                        String[] headers = {"支付订单","商户单号","支付产品","支付金额",
                                "商品标题","商品描述","渠道用户","渠道订单号","商户费率","商户收入","是否退款","成功退款金额","订单时间"};
                        try {
                            csvWriter.writeRecord(headers);
                        }catch (Exception e) {

                        }
                    }
                }

                // 判断商户账单是否生成过, 如果没有生成, 则增加内容
                if(handleMap.get(mchId + billDateStr) == null) {
                    try {
                    	OrderProfitDetail mchDetail = rpcCommonService.rpcOrderProfitDetailService.findMchDetailForPayOrderId(payOrder.getPayOrderId());
                        String[] csvContent = { payOrder.getPayOrderId(), payOrder.getMchOrderNo(), payProductMap.get(payOrder.getProductId()+""), AmountUtil.convertCent2Dollar(StrUtil.toString(payOrder.getAmount())),
                                payOrder.getSubject(), payOrder.getBody(), payOrder.getChannelUser(), payOrder.getChannelOrderNo(), mchDetail.getFeeRateSnapshot() , AmountUtil.convertCent2Dollar(StrUtil.toString(mchDetail.getIncomeAmount())),
                                StrUtil.toString(payOrder.getIsRefund(), "0"), StrUtil.toString(payOrder.getSuccessRefundAmount(), "0"), String.valueOf(payOrder.getCreateTime().getTime()) };
                        csvWriter.writeRecord(csvContent);
                    }catch (Exception e) {
                        _log.error(e, "");
                    }
                }

                lastMchId = mchId;
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(payOrderList) || payOrderList.size() < limit) {
                flag = false;
            }
        }

        if(csvWriter != null) {
            csvWriter.close();
            rpcCommonService.rpcMchBillService.updateComplete(lastMchId, MchConstant.INFO_TYPE_MCH, billDate);
            _log.info("商户:{},对账文件生成完成", lastMchId);
        }

        _log.info("生成商户对账文件,结束。");
    }
}
