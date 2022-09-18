package org.xxpay.manage.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sett")
public class SettController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(SettController.class);

    /**
     * 结算列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SettRecord settRecord = getObject( SettRecord.class);
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex()-1) * getPageSize(), getPageSize(), settRecord, getQueryObj());
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 审核列表查询
     * @return
     */
    @RequestMapping("/audit_list")
    @ResponseBody
    public ResponseEntity<?> auditList() {

        SettRecord settRecord = getObject( SettRecord.class);
        List<Byte> settStatusList = new LinkedList<>();
        settStatusList.add(MchConstant.SETT_STATUS_AUDIT_ING); // 审核中
        int count = rpcCommonService.rpcSettRecordService.count(settStatusList, settRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex()-1) * getPageSize(), getPageSize(), settStatusList, settRecord, getQueryObj());
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 打款列表查询
     * @return
     */
    @RequestMapping("/remit_list")
    @ResponseBody
    public ResponseEntity<?> remitList() {

        SettRecord settRecord = getObject( SettRecord.class);
        List<Byte> settStatusList = new LinkedList<>();
        settStatusList.add(MchConstant.SETT_STATUS_AUDIT_OK);   // 已审核
        settStatusList.add(MchConstant.SETT_STATUS_REMIT_ING);  // 打款中
        int count = rpcCommonService.rpcSettRecordService.count(settStatusList, settRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex()-1) * getPageSize(), getPageSize(), settStatusList, settRecord, getQueryObj());
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 结算查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(settRecord));
    }

    /**
     * 审核
     * @return
     */
    @RequestMapping("/audit")
    @ResponseBody
    @MethodLog( remark = "结算审核" )
    public ResponseEntity<?> audit() {

        Long id = getValLongRequired( "id");
        Byte status = getValByteRequired( "status");
        String remark = getValString( "remark");
        // 状态必须为审核通过或审核不通过
        if(status.byteValue() != MchConstant.SETT_STATUS_AUDIT_OK &&
                status.byteValue() != MchConstant.SETT_STATUS_AUDIT_NOT) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_STATUS_ERROR));
        }
        try {
            int count = rpcCommonService.rpcSettRecordService.auditSett(id, status, remark);
            if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 打款(改状态)
     * @return
     */
    @RequestMapping("/remit")
    @ResponseBody
    @MethodLog( remark = "结算打款" )
    public ResponseEntity<?> remit() {

        Long id = getValLongRequired( "id");
        Byte status = getValByteRequired( "status");
        String remark = getValString( "remark");
        String remitRemark = getValString( "remitRemark");
        // 状态必须为打款中|打款成功|打款失败
        if(status.byteValue() != MchConstant.SETT_STATUS_REMIT_ING
                && status.byteValue() != MchConstant.SETT_STATUS_REMIT_SUCCESS
                && status.byteValue() != MchConstant.SETT_STATUS_REMIT_FAIL) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_STATUS_ERROR));
        }
        try {
            int count = rpcCommonService.rpcSettRecordService.remit(id, status, remark, remitRemark, null, null);
            if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 打款(转账+改状态)
     * @return
     */
    @RequestMapping("/remit2")
    @ResponseBody
    @MethodLog( remark = "结算打款+转账" )
    public ResponseEntity<?> remit2() {

        Long id = getValLongRequired( "id");
        String remark = getValString( "remark");
        String remitRemark = getValString( "remitRemark");

        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        Integer agentpayPassageAccountId = getValIntegerRequired( "agentpayPassageAccountId");

        // 判断代付通道
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null || agentpayPassage.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_AGENTPAY_PASSAGE_NOT_EXIST));
        }

        // 判断代付通道子账户
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(agentpayPassageAccountId);
        if(agentpayPassageAccount == null || agentpayPassageAccount.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_AGENTPAY_PASSAGE_ACCOUNT_NOT_EXIST));
        }

        // 查询结算记录
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.findById(id);

        TransOrder transOrder = new TransOrder();
        transOrder.setInfoId(settRecord.getInfoId());
        transOrder.setInfoType(MchConstant.INFO_TYPE_MCH);
        transOrder.setMchType(MchConstant.MCH_TYPE_PLATFORM);
        transOrder.setMchTransNo(settRecord.getSettOrderId());
        transOrder.setChannelType(agentpayPassage.getIfTypeCode());
        transOrder.setChannelId(agentpayPassage.getIfCode());
        transOrder.setPassageId(agentpayPassage.getId());
        transOrder.setPassageAccountId(agentpayPassageAccount.getId());
        transOrder.setAmount(settRecord.getRemitAmount());
        transOrder.setAccountAttr(settRecord.getAccountAttr());
        transOrder.setAccountType(settRecord.getAccountType());
        transOrder.setAccountName(settRecord.getAccountName());
        transOrder.setAccountNo(settRecord.getAccountNo());
        transOrder.setProvince(settRecord.getProvince());
        transOrder.setCity(settRecord.getCity());
        transOrder.setBankName(settRecord.getBankName());
        transOrder.setChannelMchId(agentpayPassageAccount.getPassageMchId());
        transOrder.setNotifyUrl(mainConfig.getSettNotifyUrl());
        BigDecimal channelRate = null;  // 渠道费率
        Long channelFeeEvery = null;    // 渠道每笔费用
        Long channelCost = null;        // 渠道成本
        if(agentpayPassage.getFeeType() == 1) {   // 百分比收费
            channelRate = agentpayPassage.getFeeRate();
            channelCost = new BigDecimal(settRecord.getRemitAmount()).multiply(channelRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP).longValue();
        }else if(agentpayPassage.getFeeType() == 2) { // 固定收费
            channelFeeEvery = agentpayPassage.getFeeEvery();
            channelCost = channelFeeEvery;
        }
        
        //微信结算账户   需要录入 transOrder.setChannelUser("openID");
        if(transOrder.getAccountType() == 2){
        	transOrder.setChannelUser(settRecord.getAccountNo());
        }

        transOrder.setChannelRate(channelRate);
        transOrder.setChannelFeeEvery(channelFeeEvery);
        transOrder.setChannelCost(channelCost);

        // 先更新为打款中
        int count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_ING, remark, remitRemark, null, null);
        if(count != 1) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        // 调用转账接口,进行实时转账
        String transStr;
        try {
            String transOrderId = rpcCommonService.rpcXxPayTransService.executeTrans(transOrder);
            transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            if(transOrder == null) {
                return ResponseEntity.ok(BizResponse.build("转账失败,调用支付中心异常"));
            }

            Byte transStatus = transOrder.getStatus();
            if(transStatus == null || PayConstant.REFUND_STATUS_FAIL == transStatus) {  // 明确转账失败
                // 更新打款失败
                transStr = "转账失败";
                count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_FAIL, remark, remitRemark, transOrderId, transOrder.getChannelErrMsg());
            }else if(PayConstant.TRANS_STATUS_SUCCESS == transStatus ) { // 明确转成成功
                transStr = "转账成功";
                // 更新打款成功
                count = rpcCommonService.rpcSettRecordService.remit(id, MchConstant.SETT_STATUS_REMIT_SUCCESS, remark, remitRemark, transOrderId, null);
            }else if(PayConstant.TRANS_STATUS_TRANING == transStatus) {
                transStr = "转账处理中";
                count = rpcCommonService.rpcSettRecordService.updateTrans(id, transOrderId, null);
            }else {
                transStr = "转账处理中";
                count = rpcCommonService.rpcSettRecordService.updateTrans(id, transOrderId, null);
            }
            if(count != 1) {
                return ResponseEntity.ok(BizResponse.build(transStr + ",操作账户失败"));
            }
        }catch (Exception e) {
            transStr = "转账异常";
            _log.error(e, "");
        }
        return ResponseEntity.ok(BizResponse.build(transStr));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long infoId = getValLong( "infoId");
        String settOrderId = getValString( "settOrderId");
        String accountName = getValString( "accountName");
        Byte settStatus = getValByte( "settStatus");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcSettRecordService.count4All(infoId, accountName, settOrderId, settStatus, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }

    /**
     * 批量账户记录查询
     * @return
     */
    @RequestMapping("/batch_record_list")
    @ResponseBody
    public ResponseEntity<?> batchRecordList() {

        Long settRecordId = getValLongRequired( "id");   // 结算记录ID
        MchSettBatchRecord mchSettBatchRecord = new MchSettBatchRecord();
        mchSettBatchRecord.setSettRecordId(settRecordId);
        int count = rpcCommonService.rpcMchSettBatchRecordService.count(mchSettBatchRecord);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchSettBatchRecord> mchSettBatchRecordList = rpcCommonService.rpcMchSettBatchRecordService.select((getPageIndex()-1) * getPageSize(), getPageSize(), mchSettBatchRecord);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchSettBatchRecordList, count));
    }



    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        SettRecord settRecord = getObject( SettRecord.class);
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj());
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select(0, MchConstant.MAX_EXPORT_ROW, settRecord, getQueryObj());

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"结算单号", "结算商类型", "结算商ID", "账户名", "结算金额", "手续费", "打款金额", "结算状态", "结算日期"});
        excelData.add(header);
        for(SettRecord record : settRecordList){
            List rowData = new ArrayList<>();
            rowData.add(record.getSettOrderId());
            rowData.add(record.getInfoType() == MchConstant.INFO_TYPE_MCH ? "商户" : "代理商");
            rowData.add(record.getInfoId());
            rowData.add(record.getAccountName());
            rowData.add(AmountUtil.convertCent2Dollar(record.getSettAmount()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getSettFee()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getRemitAmount()));
            switch (record.getSettStatus()){
                case MchConstant.SETT_STATUS_AUDIT_ING : rowData.add("等待审核"); break;
                case MchConstant.SETT_STATUS_AUDIT_OK : rowData.add("已审核"); break;
                case MchConstant.SETT_STATUS_AUDIT_NOT : rowData.add("审核不通过"); break;
                case MchConstant.SETT_STATUS_REMIT_ING : rowData.add("打款中"); break;
                case MchConstant.SETT_STATUS_REMIT_SUCCESS : rowData.add("打款成功"); break;
                case MchConstant.SETT_STATUS_REMIT_FAIL : rowData.add("打款失败"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add(DateUtil.date2Str(record.getSettDate()));
            excelData.add(rowData);
        }

        super.writeExcelStream("结算记录", excelData);
        return null;
    }

}
