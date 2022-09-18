package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/account")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class AccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Map<String, Object> accountMap = rpcCommonService.rpcAccountBalanceService.selectLineByInfoId(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(accountMap));
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
        mchAccountHistory.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcAccountHistoryService.count(mchAccountHistory);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AccountHistory> mchAccountHistoryList = rpcCommonService.rpcAccountHistoryService
                .select((getPageIndex() - 1) * getPageSize(), getPageSize(), mchAccountHistory);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAccountHistoryList, count));
    }

    /**
     * 查询资金流水列表
     * @return
     */
    @RequestMapping("/history_get")
    @ResponseBody
    public ResponseEntity<?> historyGet() {

        Long id = getValLongRequired( "id");
        AccountHistory mchAccountHistory = rpcCommonService.rpcAccountHistoryService
                .findById(MchConstant.INFO_TYPE_MCH, getUser().getBelongInfoId(), id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAccountHistory));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        AccountHistory mchAccountHistory = getObject( AccountHistory.class);
        mchAccountHistory.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchAccountHistory.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcAccountHistoryService.count(mchAccountHistory);
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<AccountHistory> mchAccountHistoryList = rpcCommonService.rpcAccountHistoryService.select(0, MchConstant.MAX_EXPORT_ROW, mchAccountHistory);
        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"流水号", "变更前金额（元）", "变更金额(元)", "变更后余额(元)", "业务类型", "业务订单", "订单金额(元)", "手续费(元)", "时间"});
        excelData.add(header);
        for(AccountHistory record : mchAccountHistoryList){
            List rowData = new ArrayList<>();
            rowData.add(record.getId());
            rowData.add(AmountUtil.convertCent2Dollar(record.getBalance()));
            rowData.add( (record.getFundDirection() == MchConstant.FUND_DIRECTION_ADD ? "+" : "-")  + AmountUtil.convertCent2Dollar(record.getChangeAmount()));
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

        super.writeExcelStream("资金流水", excelData);
        return null;
    }

}
