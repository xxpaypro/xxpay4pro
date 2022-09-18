package org.xxpay.agent.settlement.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.AgentInfo;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/agentpay")
@PreAuthorize("hasRole('"+ MchConstant.AGENT_ROLE_NORMAL+"')")
public class AgentpayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AgentpayController.class);

    /**
     * 申请单笔代付
     * @return
     */
    @RequestMapping("/apply")
    @ResponseBody
    @MethodLog( remark = "发起代付" )
    public ResponseEntity<?> apply() {

        long agentId = getUser().getBelongInfoId();
        Long agentpayAmountL = getRequiredAmountL( "agentpayAmount");

        if(agentpayAmountL <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
        }
        String accountName = StringUtils.deleteWhitespace(getValStringRequired( "accountName"));   // 账户名
        String accountNo = StringUtils.deleteWhitespace(getValStringRequired( "accountNo"));       // 账号
        String remark = getValString( "remark");                     // 备注
        // 支付安全验证
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());
        
        String payPassword = getValString( "payPassword");
        String vercode = getValString( "vercode");

        AgentpayRecord agentpayRecord = null;
        // 发起提现申请
        try {
            String agentpayOrderId = MySeq.getAgentpay();
            _log.info("商户后台发起代付,代付ID={}", agentpayOrderId);
            agentpayRecord = new AgentpayRecord();
            agentpayRecord.setAgentpayOrderId(agentpayOrderId);
            agentpayRecord.setSubAmountFrom(MchConstant.AGENTPAY_OUT_PAY_BALANCE); //代理商发起代付仅支持收款账户 出款
            agentpayRecord.setInfoId(getUser().getBelongInfoId());
            agentpayRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
            agentpayRecord.setAccountName(accountName);      // 账户名
            agentpayRecord.setAccountNo(accountNo);          // 账号
            agentpayRecord.setAmount(agentpayAmountL);       // 代付金额
            agentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
            agentpayRecord.setDevice("web");
            agentpayRecord.setClientIp(IPUtility.getClientIp(request));
            int result = rpcCommonService.rpcXxPayAgentpayService.applyAgentpay(agentpayRecord, "");
            if(result == 1) {
                return ResponseEntity.ok(BizResponse.buildSuccess());
            }else {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
            }
        }catch (ServiceException e) {
            _log.error("商户ID:" + getUser().getBelongInfoId() + "," + e.getRetEnum().getMessage());
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum(), e.getExtraMsg()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 批量代付申请
     * @return
     */
    @RequestMapping("/apply_batch")
    @ResponseBody
    @MethodLog( remark = "发起批量代付" )
    public ResponseEntity<?> batchApply(){
        Long startTime = System.currentTimeMillis();

        String nums = getValStringRequired( "nums");     // 存储前端提交的对已经行编号
        String values = getValStringRequired( "values"); // 所有input表单域,根据编号取值,如accountName_0
        // 支付安全验证
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());
        
        String payPassword = StringUtils.isEmpty(values)? "" : JSONObject.parseObject(values).getString("payPassword");
        String vercode = StringUtils.isEmpty(values)? "" : JSONObject.parseObject(values).getString("vercode");

        List<AgentpayRecord> agentpayRecordList = new LinkedList<>();
        try {
            JSONArray numArray = JSONArray.parseArray(nums);
            JSONObject valueObj = JSONObject.parseObject(values);
            for(int i = 0; i < numArray.size(); i++) {
                String accountName = StringUtils.deleteWhitespace(valueObj.getString("accountName_" + numArray.getString(i)));
                String accountNo = StringUtils.deleteWhitespace(valueObj.getString("accountNo_" + numArray.getString(i)));
                String agentpayAmount = StringUtils.deleteWhitespace(valueObj.getString("agentpayAmount_" + numArray.getString(i)));
                Long agentpayAmountL = new BigDecimal(agentpayAmount).multiply(new BigDecimal(100)).longValue(); // // 转成分
                if(agentpayAmountL <= 0) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
                }
                AgentpayRecord agentpayRecord = new AgentpayRecord();
                agentpayRecord.setInfoId(agentInfo.getAgentId());
                agentpayRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
                agentpayRecord.setSubAmountFrom(MchConstant.AGENTPAY_OUT_PAY_BALANCE); //代理商发起代付仅支持收款账户 出款
                agentpayRecord.setAccountName(accountName);  // 账户名
                agentpayRecord.setAccountNo(accountNo);      // 账号
                agentpayRecord.setAmount(agentpayAmountL);       // 代付金额
                agentpayRecord.setRemark("代付:" + agentpayAmount + "元");
                agentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
                agentpayRecord.setClientIp(IPUtility.getClientIp(request));
                agentpayRecord.setDevice("web");
                agentpayRecordList.add(agentpayRecord);
            }
            // 如果账户记录为空
            if(agentpayRecordList.size() == 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY));
            }
            if((agentpayRecordList.size() > 10)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT, "最多10条"));
            }

            // 查看是否有重复代付申请记录
            AgentpayRecord agentpayRecord = isRepeatAgentpay(agentpayRecordList);
            if(agentpayRecord != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "卡号:" + agentpayRecord.getAccountNo() + ",金额:" + agentpayRecord.getAmount()/100.0 + "元"));
            }

            // 批量提交代付
            JSONObject resObj = rpcCommonService.rpcXxPayAgentpayService.batchApplyAgentpay("", agentpayRecordList);
            if(resObj == null || resObj.getIntValue("batchInertCount") <= 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "批量代付失败"));
            }

            // 返回数据
            JSONObject retObj = new JSONObject();
            retObj.put("batchInertCount", resObj.getInteger("batchInertCount"));
            retObj.put("totalAmount", resObj.getLongValue("totalAmount")); //
            retObj.put("totalFee", resObj.getLongValue("totalFee"));
            retObj.put("costTime", System.currentTimeMillis() - startTime); // 记录服务端耗时
            return ResponseEntity.ok(XxPayResponse.buildSuccess(retObj));
        } catch (ServiceException e) {
            _log.error("商户ID:" + getUser().getBelongInfoId() + "," + e.getRetEnum().getMessage());
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        } catch (Exception e) {
            _log.error(e, "");
            // 清除缓存的key
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }


    /**
     * 判断是否有重复的代付申请(卡号+金额),只要有重复即返回
     * @param agentpayRecordList
     * @return
     */
    AgentpayRecord isRepeatAgentpay(List<AgentpayRecord> agentpayRecordList) {
        Map<String, AgentpayRecord> map = new HashMap<>();
        if(agentpayRecordList == null || agentpayRecordList.size() <= 1) return null;
        for(AgentpayRecord record : agentpayRecordList) {
            String key = record.getAccountNo() + record.getAmount();
            if(map.get(key) != null) {
                return map.get(key);
            }
            map.put(key, record);
        }
        return null;
    }

    /**
     * 代付列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        AgentpayRecord agentpayRecord = getObject( AgentpayRecord.class);
        if(agentpayRecord == null) agentpayRecord = new AgentpayRecord();
        agentpayRecord.setInfoId(getUser().getBelongInfoId());
        agentpayRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
        int count = rpcCommonService.rpcAgentpayService.count(agentpayRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentpayRecord> agentpayRecordList = rpcCommonService.rpcAgentpayService.select((getPageIndex()-1) * getPageSize(), getPageSize(), agentpayRecord, getQueryObj());
        for(AgentpayRecord agentpayRecord1 : agentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(agentpayRecord1.getAccountNo(), 4, 4, 4);
            agentpayRecord1.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentpayRecordList, count));
    }

    /**
     * 代付记录查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String agentpayOrderId = getValStringRequired( "agentpayOrderId");
        AgentpayRecord agentpayRecord = new AgentpayRecord();
        agentpayRecord.setInfoId(getUser().getBelongInfoId());
        agentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        agentpayRecord.setAgentpayOrderId(agentpayOrderId);
        agentpayRecord = rpcCommonService.rpcAgentpayService.find(agentpayRecord);
        if(agentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(agentpayRecord.getAccountNo(), 4, 4, 4);
            agentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentpayRecord));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        AgentpayRecord agentpayRecord = getObject( AgentpayRecord.class);
        if(agentpayRecord == null) agentpayRecord = new AgentpayRecord();
        agentpayRecord.setInfoId(getUser().getBelongInfoId());
        agentpayRecord.setInfoType(MchConstant.INFO_TYPE_AGENT);
        int count = rpcCommonService.rpcAgentpayService.count(agentpayRecord, getQueryObj());
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<AgentpayRecord> agentpayRecordList = rpcCommonService.rpcAgentpayService.select(0, MchConstant.MAX_EXPORT_ROW, agentpayRecord, getQueryObj());
        for(AgentpayRecord agentpayRecord1 : agentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(agentpayRecord1.getAccountNo(), 4, 4, 4);
            agentpayRecord1.setAccountNo(accountNo);
        }
        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"转账单号", "账户名", "账号", "代付金额(元)", "手续费(元)", "扣减账户金额(元)", "状态", "代付渠道", "时间"});
        excelData.add(header);
        for(AgentpayRecord record : agentpayRecordList){
            List rowData = new ArrayList<>();
            rowData.add(record.getTransOrderId());
            rowData.add(record.getAccountName());
            rowData.add(record.getAccountNo());
            rowData.add(AmountUtil.convertCent2Dollar(record.getAmount()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getFee()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getSubAmount()));
            switch (record.getStatus()){
                case PayConstant.AGENTPAY_STATUS_INIT : rowData.add("待处理"); break;
                case PayConstant.AGENTPAY_STATUS_ING : rowData.add("处理中"); break;
                case PayConstant.AGENTPAY_STATUS_SUCCESS : rowData.add("成功"); break;
                case PayConstant.AGENTPAY_STATUS_FAIL : rowData.add("失败"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add(record.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_PLAT ? "代理商后台" : "API接口");
            rowData.add(DateUtil.date2Str(record.getCreateTime()));
            excelData.add(rowData);
        }

        super.writeExcelStream("代付列表", excelData);
        return null;
    }

}
