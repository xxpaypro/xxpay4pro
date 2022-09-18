package org.xxpay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/account")
public class AccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AccountController.class);

    /**
     * 查询账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired( "mchId");
        return ResponseEntity.ok(XxPayResponse.buildSuccess(rpcCommonService.rpcAccountBalanceService.selectLineByInfoId(MchConstant.INFO_TYPE_MCH, mchId)));
    }

    /**
     * 商户账户加钱
     * @return
     */
    @RequestMapping("/credit")
    @ResponseBody
    @MethodLog( remark = "商户资金增加" )
    public ResponseEntity<?> credit() {

        Long mchId = getValLongRequired( "mchId");
        String bizItem = getValStringRequired( "bizItem");
        String amount = getValStringRequired( "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getValStringRequired( "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {
            rpcCommonService.rpcAccountBalanceService.changeAmountAndInsertHistory(MchConstant.INFO_TYPE_MCH, mchId,
                                            MchConstant.BIZ_TYPE_CHANGE_BALANCE, bizItem, amountL, "平台调账");

            return ResponseEntity.ok(BizResponse.buildSuccess());
        }catch (ServiceException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 商户账户减钱
     * @return
     */
    @RequestMapping("/debit")
    @ResponseBody
    @MethodLog( remark = "商户资金减少" )
    public ResponseEntity<?> debit() {

        Long mchId = getValLongRequired( "mchId");
        String bizItem = getValStringRequired( "bizItem");
        String amount = getValStringRequired( "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getValStringRequired( "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {

            rpcCommonService.rpcAccountBalanceService.changeAmountAndInsertHistory(MchConstant.INFO_TYPE_MCH, mchId,
                    MchConstant.BIZ_TYPE_CHANGE_BALANCE, bizItem, (0-amountL), "平台调账");

            return ResponseEntity.ok(BizResponse.buildSuccess());
        }catch (ServiceException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 查询资金流水列表
     * @return
     */
    @RequestMapping("/history_list")
    @ResponseBody
    public ResponseEntity<?> historyList() {

        AccountHistory mchAccountHistory = getObject( AccountHistory.class);
        mchAccountHistory.setInfoType(MchConstant.INFO_TYPE_MCH);
        int count = rpcCommonService.rpcAccountHistoryService.count(mchAccountHistory);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AccountHistory> mchAccountHistoryList = rpcCommonService.rpcAccountHistoryService
                .select((getPageIndex() -1) * getPageSize(), getPageSize(), mchAccountHistory);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAccountHistoryList, count));
    }

    /**
     * 查询资金流水
     * @return
     */
    @RequestMapping("/history_get")
    @ResponseBody
    public ResponseEntity<?> historyGet() {

        Long id = getValLongRequired( "id");
        AccountHistory mchAccountHistory = rpcCommonService.rpcAccountHistoryService
                .findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAccountHistory));
    }
    
    /**
     * 统计总数
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        String orderId = getValString( "orderId");
        Long agentId = getValLong( "agentId");
        Long mchId = getValLong( "mchId");
        Byte bizType = getValByte( "bizType");
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcAccountHistoryService.count4Data2(mchId, agentId, orderId, bizType, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("totalCount", allMap.get("totalCount"));                         // 总笔数
        obj.put("totalAmount", allMap.get("totalAmount"));                       //
        obj.put("totalFee", allMap.get("totalFee"));
        obj.put("totalAgentProfit", allMap.get("totalAgentProfit"));          //代理商利润    
        obj.put("totalPlatProfit", allMap.get("totalPlatProfit"));         //总平台利润
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        AccountHistory mchAccountHistory = getObject( AccountHistory.class);
        mchAccountHistory.setInfoType(MchConstant.INFO_TYPE_MCH);
        int count = rpcCommonService.rpcAccountHistoryService.count(mchAccountHistory);
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();

        List<AccountHistory> mchAccountHistoryList = rpcCommonService.rpcAccountHistoryService.select(0, MchConstant.MAX_EXPORT_ROW, mchAccountHistory);

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"商户ID", "变更前余额(元)", "变更金额(元)", "变更后余额(元)", "业务类型", "业务订单", "订单金额(元)", "手续费(元)", "时间"});
        excelData.add(header);
        for(AccountHistory record : mchAccountHistoryList){
            List rowData = new ArrayList<>();
            rowData.add(record.getInfoId());
            rowData.add(AmountUtil.convertCent2Dollar(record.getBalance()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getChangeAmount()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getAfterBalance()));
            switch (record.getBizType()){
                case MchConstant.BIZ_TYPE_TRANSACT : rowData.add("支付"); break;
                case MchConstant.BIZ_TYPE_REMIT : rowData.add("提现"); break;
                case MchConstant.BIZ_TYPE_CHANGE_BALANCE : rowData.add("调账"); break;
                case MchConstant.BIZ_TYPE_RECHARGE : rowData.add("充值"); break;
                case MchConstant.BIZ_TYPE_ERROR_HANKLE : rowData.add("差错处理"); break;
                case MchConstant.BIZ_TYPE_AGENTPAY : rowData.add("代付"); break;
                case MchConstant.BIZ_TYPE_PROFIT : rowData.add("分润"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add(record.getBizOrderId());
            rowData.add(AmountUtil.convertCent2Dollar(record.getBizOrderAmount()));
            rowData.add(AmountUtil.convertCent2Dollar(record.getBizOrderFee()));
            rowData.add(DateUtil.date2Str(record.getCreateTime()));

            excelData.add(rowData);
        }

        super.writeExcelStream("商户资金流水", excelData);

        return null;
    }




}
