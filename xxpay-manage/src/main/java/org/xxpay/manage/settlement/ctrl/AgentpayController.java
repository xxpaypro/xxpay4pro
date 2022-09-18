package org.xxpay.manage.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentpayPassageAccount;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description: 代付
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agentpay")
public class AgentpayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 代付列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        AgentpayRecord mchAgentpayRecord = getObject( AgentpayRecord.class);
        if(mchAgentpayRecord == null) mchAgentpayRecord = new AgentpayRecord();
        int count = rpcCommonService.rpcAgentpayService.count(mchAgentpayRecord, getQueryObj());
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.select((getPageIndex()-1) * getPageSize(), getPageSize(), mchAgentpayRecord, getQueryObj());
        for(AgentpayRecord mchAgentpayRecord1 : mchAgentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord1.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord1.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAgentpayRecordList, count));
    }

    /**
     * 代付记录查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String agentpayOrderId = getValStringRequired( "agentpayOrderId");
        AgentpayRecord mchAgentpayRecord = new AgentpayRecord();
        mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
        mchAgentpayRecord = rpcCommonService.rpcAgentpayService.find(mchAgentpayRecord);
        if(mchAgentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayRecord));
    }

    @RequestMapping("/trans_query")
    @ResponseBody
    public ResponseEntity<?> queryTrans() {

        String transOrderId = getValStringRequired( "transOrderId");
        QueryRetMsg resObj = rpcCommonService.rpcXxPayTransService.queryTrans(transOrderId);
        if(resObj != null) {
            return ResponseEntity.ok(XxPayResponse.buildSuccess(resObj.getChannelOriginResponse()));
        }else {
            return ResponseEntity.ok(XxPayResponse.buildSuccess("查询通道异常"));
        }
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long infoId = getValLong( "infoId");
        String agentpayOrderId = getValString( "agentpayOrderId");
        String transOrderId = getValString( "transOrderId");
        String accountName = getValString( "accountName");
        Byte status = getValByte( "status");
        Byte agentpayChannel = getValByte( "agentpayChannel");
        Byte infoType = getValByte( "infoType");
        Byte subAmountFrom = getValByte( "subAmountFrom");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcAgentpayService.count4All(infoId, accountName, agentpayOrderId, transOrderId, status, agentpayChannel, infoType, subAmountFrom, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        obj.put("allTotalSubAmount", allMap.get("totalSubAmount"));                 // 扣减金额
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        AgentpayRecord mchAgentpayRecord = getObject( AgentpayRecord.class);
        if(mchAgentpayRecord == null) mchAgentpayRecord = new AgentpayRecord();
        int count = rpcCommonService.rpcAgentpayService.count(mchAgentpayRecord, getQueryObj());
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<AgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.select(   0, MchConstant.MAX_EXPORT_ROW, mchAgentpayRecord, getQueryObj());
        for(AgentpayRecord mchAgentpayRecord1 : mchAgentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord1.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord1.setAccountNo(accountNo);
        }

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"商户/代理商ID", "发起类型", "转账单号", "账户名", "账号", "代付金额(元)", "手续费(元)", "扣减账户金额", "状态", "代付渠道", "时间"});
        excelData.add(header);
        for(AgentpayRecord record : mchAgentpayRecordList){
            List rowData = new ArrayList<>();
            rowData.add(record.getInfoId());
            rowData.add(record.getInfoType() == MchConstant.INFO_TYPE_MCH ? "商户" : "代理商");
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
            rowData.add(record.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_PLAT ? "后台" : "API接口");
            rowData.add(DateUtil.date2Str(record.getCreateTime()));
            excelData.add(rowData);
        }

        super.writeExcelStream("代付列表", excelData);

        return null;
    }

}
