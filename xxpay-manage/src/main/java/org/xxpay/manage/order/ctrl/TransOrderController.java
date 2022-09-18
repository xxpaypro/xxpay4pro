package org.xxpay.manage.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.MchNotify;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/trans_order")
public class TransOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条转账记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String transOrderId = getValStringRequired( "transOrderId");
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(transOrder));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        TransOrder transOrder = getObject( TransOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcTransOrderService.count(transOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((getPageIndex() -1) * getPageSize(),
                getPageSize(), transOrder, createTimeStart, createTimeEnd);

        for(TransOrder order: transOrderList){
            MchNotify mchNotfiy = rpcCommonService.rpcMchNotifyService.findByOrderId(order.getTransOrderId());
            order.setPsVal("notifyStatus", mchNotfiy == null ? "" : mchNotfiy.getStatus());
        }

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(transOrderList, count));
    }

    /**
     * 查询订单统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        String transOrderId = getValString( "transOrderId");
        String mchTransNo = getValString( "mchTransNo");
        Long mchId = getValLong( "mchId");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcTransOrderService.count4All(mchId, transOrderId, mchTransNo, createTimeStartStr, createTimeEndStr);
        Map successMap = rpcCommonService.rpcTransOrderService.count4Success(mchId, transOrderId, mchTransNo, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 总金额
        obj.put("successTotalCount", successMap.get("totalCount"));                 // 成功订单数
        obj.put("successTotalAmount", successMap.get("totalAmount"));               // 成功金额
        obj.put("successTotalChannelCost", successMap.get("totalChannelCost"));     // 成功渠道成本
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }


    @RequestMapping("/exportExcel")
    @ResponseBody
    public String exportExcel() throws Exception {


        TransOrder transOrder = getObject( TransOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcTransOrderService.count(transOrder, createTimeStart, createTimeEnd);
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select(0, MchConstant.MAX_EXPORT_ROW, transOrder, createTimeStart, createTimeEnd);

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"转账单号", "商户单号", "商户ID", "转账金额", "转账状态", "转账结果", "创建时间"});
        excelData.add(header);
        for(TransOrder record : transOrderList){
            List rowData = new ArrayList<>();
            rowData.add(record.getTransOrderId());
            rowData.add(record.getMchTransNo());
            rowData.add(record.getInfoId());
            rowData.add(AmountUtil.convertCent2Dollar(record.getAmount()));
            switch (record.getStatus()){
                case PayConstant.TRANS_STATUS_INIT : rowData.add("初始"); break;
                case PayConstant.TRANS_STATUS_TRANING : rowData.add("转账中"); break;
                case PayConstant.TRANS_STATUS_SUCCESS : rowData.add("转账成功"); break;
                case PayConstant.TRANS_STATUS_FAIL : rowData.add("转账失败"); break;
                default: rowData.add("未知"); break;
            }
            switch (record.getResult()){
                case PayConstant.TRANS_RESULT_INIT : rowData.add("不确认结果"); break;
                case PayConstant.TRANS_RESULT_REFUNDING : rowData.add("等待处理"); break;
                case PayConstant.TRANS_RESULT_SUCCESS : rowData.add("确认成功"); break;
                case PayConstant.TRANS_RESULT_FAIL : rowData.add("确认失败"); break;
                default: rowData.add("未知"); break;
            }
            rowData.add(DateUtil.date2Str(record.getCreateTime()));

            excelData.add(rowData);
        }

        super.writeExcelStream("转账订单", excelData);

        return null;
    }






}
