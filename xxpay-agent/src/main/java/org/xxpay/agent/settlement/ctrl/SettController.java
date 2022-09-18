package org.xxpay.agent.settlement.ctrl;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.SettRecord;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/sett")
public class SettController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(SettController.class);

    /**
     * 是否允许申请提现
     * @return
     */
    @RequestMapping("/is_allow_apply")
    @ResponseBody
    public ResponseEntity<?> allowApply() {
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());

        return ResponseEntity.ok(BizResponse.build(""));
    }

    /**
     * 申请结算
     * @return
     */
    @RequestMapping("/apply")
    @ResponseBody
    @MethodLog( remark = "申请结算" )
    public ResponseEntity<?> apply() {

        String settAmountJson = getValStringRequired( "settAmountJson");         // 前端填写的为元,可以为小数点2位
        
        String payPassword = getValString( "payPassword");
        String vercode = getValString( "vercode");

        JSONArray arr = (JSONArray)JSONArray.parse(settAmountJson);
        List<Map<String,Long>> settList = new ArrayList<Map<String,Long>>();
        
        for(Object obj : arr){
        	JSONObject jsonObj = (JSONObject)obj;
        	Long accountId = jsonObj.getLong("accountId");
        	BigDecimal settAmount = jsonObj.getBigDecimal("settAmount");
        	Long settAmountL = settAmount.multiply(new BigDecimal(100)).longValue(); // // 转成分
        	if(settAmountL <= 0) {
        		return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR));
        	}
        	
        	Map<String, Long> map = new HashMap<String, Long>();
        	map.put("accountId", accountId);
        	map.put("settAmount", settAmountL);
        	settList.add(map);
        	
        }
        
        // 发起提现申请
    	try {
    		int count = rpcCommonService.rpcSettRecordService.applySett(MchConstant.INFO_TYPE_AGENT, getUser().getBelongInfoId(),settList);
    		if(count > 0){
    			return ResponseEntity.ok(BizResponse.buildSuccess());
    		}
    		return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    	}catch (ServiceException e) {
    		_log.error(e, "");
    		return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
    	}catch (Exception e) {
    		_log.error(e, "");
    		return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
    	}
    }

    /**
     * 结算列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SettRecord settRecord = getObject( SettRecord.class);
        if(settRecord == null) settRecord = new SettRecord();
        settRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
        settRecord.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select((getPageIndex()-1) * getPageSize(), getPageSize(), settRecord, getQueryObj());
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(settRecordList, count));
    }

    /**
     * 结算查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired("id");
        SettRecord querySettRecord = new SettRecord();
        querySettRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
        querySettRecord.setInfoId(getUser().getBelongInfoId());
        querySettRecord.setId(id);
        SettRecord settRecord = rpcCommonService.rpcSettRecordService.find(querySettRecord);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(settRecord));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long agentId = getUser().getBelongInfoId();
        String settOrderId = getValString( "settOrderId");
        String accountName = getValString( "accountName");
        Byte settStatus = getValByte( "settStatus");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcSettRecordService.count4All(agentId, accountName, settOrderId, settStatus, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }
    
    /**
     * 结算手续费计算
     * @return
     */
    @RequestMapping("/calculateSettFee")
    @ResponseBody
    public ResponseEntity<?> calculateSettFee() {

    	
    	// 重新设置代理商结算信息
    	AgentInfo info = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());

        String settAmount = getValStringRequired( "settAmount");
        Long settFee = 0L; //提现手续费
        for(String amountStr : settAmount.split("##")){
        	Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(settFee));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        SettRecord settRecord = getObject( SettRecord.class);
        if(settRecord == null) settRecord = new SettRecord();
        settRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
        settRecord.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSettRecordService.count(settRecord, getQueryObj());
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<SettRecord> settRecordList = rpcCommonService.rpcSettRecordService.select(0, MchConstant.MAX_EXPORT_ROW, settRecord, getQueryObj());
        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"结算单号", "账户名", "结算金额", "手续费", "打款金额", "结算状态", "结算日期"});
        excelData.add(header);
        for(SettRecord record : settRecordList){
            List rowData = new ArrayList<>();
            rowData.add(record.getSettOrderId());
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

        super.writeExcelStream("结算列表", excelData);
        return null;
    }
    
}
