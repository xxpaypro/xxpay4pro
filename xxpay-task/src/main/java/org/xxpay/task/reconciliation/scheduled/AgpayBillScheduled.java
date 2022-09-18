package org.xxpay.task.reconciliation.scheduled;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.Bill;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.task.common.service.RpcCommonService;

import com.csvreader.CsvWriter;

/**
 * <p><b>Title: </b>MchAgpayBillScheduled.java
 * <p><b>Description: </b>商户 /代理商 代付 对账单生成 task
 * @author terrfly
 * @version V1.0
 * <p>
 */
@Component
public class AgpayBillScheduled {

    private static final MyLog _log = MyLog.getLog(AgpayBillScheduled.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Value("${config.mchAgpayBillPath}")
    private String mchAgpayBillPath;

	@Value("${config.agentAgpayBillPath}")
	private String agentAgpayBillPath;

    @Value("${config.platBillPath}")
    private String platBillPath;


	@Value("${config.downBillUrl}")
	private String downBillUrl;

//    @Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    @Scheduled(cron="0 55 10 ? * *")   // 每日10:55执行
    public void buildMchAgpayBillTask() {
        _log.info("生成商户/代理商代付对账单文件,开始...");

        // 1. 查询代付订单表,查询昨日,按照商户ID升序排列
        Date billDate = DateUtil.addDay(new Date(), -1);
        String billDateStr = DateUtil.date2Str(billDate, DateUtil.FORMAT_YYYY_MM_DD);
        int pageIndex = 1, limit = 100;

		AgentpayRecord selectAgentay = new AgentpayRecord();
		selectAgentay.setStatus(PayConstant.AGENTPAY_STATUS_SUCCESS);
		selectAgentay.setPsVal("startTime", DateUtil.str2date(billDateStr + " 00:00:00"));
		selectAgentay.setPsVal("endTime", DateUtil.str2date(billDateStr + " 23:59:59"));
		selectAgentay.setPsVal("billDate", DateUtil.str2date(billDateStr + " 00:00:00"));
		selectAgentay.setPsVal("billType", MchConstant.BILL_TYPE_AGENTPAY);
        List<AgentpayRecord> recordList = rpcCommonService.rpcAgentpayService.selectAllBill((pageIndex - 1) * limit, limit, selectAgentay);
        Map<Long, CsvWriter> mchCsvWriterMap = new HashMap<>();
        Map<String, Bill> mchBillMap = new HashMap<>();
        while (recordList != null && !recordList.isEmpty()) {
        	
        	for(AgentpayRecord record: recordList){
        		Long infoId = record.getInfoId();
				Byte infoType = record.getInfoType();
				String infoStr = infoType == MchConstant.INFO_TYPE_MCH ? "商户" : "代理商";

				String mapKey  = infoId + "_" + infoType ;
        		try {
					Bill mchBill = mchBillMap.get(mapKey);
					if( mchBill == null ){

						 mchBill = new Bill();
						 mchBill.setInfoId(infoId);
                         mchBill.setInfoType(infoType);

						 if(infoType == MchConstant.INFO_TYPE_MCH){
							 MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(infoId);
							 mchBill.setInfoName(mchInfo.getMchName());
							 mchBill.setMchType(mchInfo.getType());
						 }else{
							 AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(infoId);
							 mchBill.setInfoName(agentInfo.getAgentName());
						 }

					     mchBill.setBillDate(billDate);
					     mchBill.setBillType(MchConstant.BILL_TYPE_AGENTPAY);
					     mchBill.setStatus(MchConstant.MCH_BILL_STATUS_INT);
					     mchBillMap.put(mapKey, mchBill);

					     String subDir = infoType == MchConstant.INFO_TYPE_MCH ? mchAgpayBillPath : agentAgpayBillPath ;
						 mchBill.setBillPath(downBillUrl + subDir + "/" + billDateStr + "/" + record.getInfoId() + ".csv");

					     rpcCommonService.rpcMchBillService.add(mchBill);
					}
					
					//查询商户是否完成
					if(mchBill.getStatus().byteValue() == MchConstant.MCH_BILL_STATUS_COMPLETE ) {
						_log.info("商户/代理商代付对账单状态已完成，continue， infoId={}, infoType ={}", infoId, infoType);
						continue;
					}
					
					CsvWriter csvWriter = mchCsvWriterMap.get(record.getInfoId());
					if(csvWriter == null){

                        String subDir = infoType == MchConstant.INFO_TYPE_MCH ? mchAgpayBillPath : agentAgpayBillPath ;
                        String scvFilePath = platBillPath + subDir + File.separator + billDateStr + File.separator + record.getInfoId() + ".csv";
					    // 创建目录
					    String path = scvFilePath.substring(0, scvFilePath.lastIndexOf(File.separator));
					    File file = new File(path);
					    if (!file.exists()) file.mkdirs();
					    csvWriter = new CsvWriter(scvFilePath,',', Charset.forName("GBK"));
					    String[] headers = {
					    		"代付订单", infoStr + "编号", "申请代付金额", "代付手续费", "账户属性", "账户类型", "账户名", "账户号", "开户行所在省", "开户行所在市",
					    		"开户行名称", "开户行网点名称", "联行号","银行代码", "状态", "转账订单号", "创建时间", "代付渠道", "商户单号" };
					    csvWriter.writeRecord(headers);
					    mchCsvWriterMap.put(record.getInfoId(), csvWriter);
					}
					String[] csvContent = {
							record.getAgentpayOrderId(), record.getInfoId() + "", record.getAmount().toString(), record.getFee().toString(),
							record.getAccountAttr() == 0 ? "对私" : "对公" ,record.getAccountType() == 1 ? "银行卡转账" : record.getAccountType() == 2 ? "微信转账" : "支付宝转账", 
							record.getAccountName(), "'"+record.getAccountNo(), record.getProvince(), record.getCity(), record.getBankName(), record.getBankNetName(),
							StrUtil.toString(record.getBankNumber()), record.getBankCode(), PayConstant.AGENTPAY_STATUS_SUCCESS == record.getStatus() ? "成功" : "" ,
							record.getTransOrderId(), DateUtil.date2Str(record.getCreateTime()), record.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_PLAT ? infoStr+"后台" : "API接口", record.getMchOrderNo()};
					csvWriter.writeRecord(csvContent);
					
				} catch (Exception e) {
					_log.error("error, infoId = {}",e, infoId);
				}
        	}
        	
        	//本次数据处理完成， 更新pageIndex，并进行查询操作。
        	pageIndex++;
        	recordList = rpcCommonService.rpcAgentpayService.selectAllBill((pageIndex - 1) * limit, limit, selectAgentay);
        }
        
        //批量关闭流操作
        for(CsvWriter csvWriter : mchCsvWriterMap.values()){
        	if(csvWriter != null){
        		csvWriter.close();
        		csvWriter = null;
        	}
        }
        
        //批量更新对账单状态
        for(Bill bill : mchBillMap.values()){
        	if(bill != null && MchConstant.MCH_BILL_STATUS_INT == bill.getStatus()){
        		rpcCommonService.rpcMchBillService.updateComplete(bill.getInfoId() , bill.getInfoType() ,bill.getBillDate());
        	}
        }
        
        
        _log.info("生成商户代付对账单文件,结束。");
    }
}
