package org.xxpay.mch.settlement.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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
import org.xxpay.core.entity.MchInfo;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.common.util.POIUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 18/04/21
 * @description: 代付操作
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/agentpay")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class AgentpayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AgentpayController.class);

    /**
     * 申请单笔代付
     * @return
     */
    @MethodLog( remark = "单笔代付" )
    @RequestMapping("/apply")
    @ResponseBody
    public ResponseEntity<?> apply() {

        long mchId = getUser().getBelongInfoId();
        Long agentpayAmountL = getRequiredAmountL( "agentpayAmount");

        if(agentpayAmountL <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
        }
        String accountName = StringUtils.deleteWhitespace(getValStringRequired( "accountName"));   // 账户名
        String accountNo = StringUtils.deleteWhitespace(getValStringRequired( "accountNo"));       // 账号
        Byte subAmountFrom = getValByteRequired( "subAmountFrom");       //出款类型
        String remark = getValString( "remark");                     // 备注
        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        
        String payPassword = getValString( "payPassword");
        String vercode = getValString( "vercode");

        AgentpayRecord mchAgentpayRecord = null;
        // 发起提现申请
        try {
            String agentpayOrderId = MySeq.getAgentpay();
            _log.info("商户后台发起代付,代付ID={}", agentpayOrderId);
            mchAgentpayRecord = new AgentpayRecord();
            mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
            mchAgentpayRecord.setSubAmountFrom(subAmountFrom);
            mchAgentpayRecord.setInfoId(getUser().getBelongInfoId());
            mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
            mchAgentpayRecord.setMchType(mchInfo.getType());
            mchAgentpayRecord.setAccountName(accountName);      // 账户名
            mchAgentpayRecord.setAccountNo(accountNo);          // 账号
            mchAgentpayRecord.setAmount(agentpayAmountL);       // 代付金额
            mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
            mchAgentpayRecord.setDevice("web");
            mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
            int result = rpcCommonService.rpcXxPayAgentpayService.applyAgentpay(mchAgentpayRecord, mchInfo.getPrivateKey());
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
    @MethodLog( remark = "批量代付" )
    public ResponseEntity<?> batchApply(){
        Long startTime = System.currentTimeMillis();

        String nums = getValStringRequired( "nums");     // 存储前端提交的对已经行编号
        String values = getValStringRequired( "values"); // 所有input表单域,根据编号取值,如accountName_0
        Byte subAmountFrom = getValByteRequired( "subAmountFrom");
        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());
        
        String payPassword = StringUtils.isEmpty(values)? "" : JSONObject.parseObject(values).getString("payPassword");
        String vercode = StringUtils.isEmpty(values)? "" : JSONObject.parseObject(values).getString("vercode");

        List<AgentpayRecord> mchAgentpayRecordList = new LinkedList<>();
        try {
            // 代付总金额
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
                AgentpayRecord mchAgentpayRecord = new AgentpayRecord();
                mchAgentpayRecord.setInfoId(mchInfo.getMchId());
                mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
                mchAgentpayRecord.setMchType(mchInfo.getType());
                mchAgentpayRecord.setSubAmountFrom(subAmountFrom);
                mchAgentpayRecord.setAccountName(accountName);  // 账户名
                mchAgentpayRecord.setAccountNo(accountNo);      // 账号
                mchAgentpayRecord.setAmount(agentpayAmountL);       // 代付金额
                mchAgentpayRecord.setRemark("代付:" + agentpayAmount + "元");
                mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
                mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
                mchAgentpayRecord.setDevice("web");
                mchAgentpayRecordList.add(mchAgentpayRecord);
            }
            // 如果账户记录为空
            if(mchAgentpayRecordList.size() == 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY));
            }
            if((mchAgentpayRecordList.size() > 10)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT, "最多10条"));
            }

            // 查看是否有重复代付申请记录
            AgentpayRecord mchAgentpayRecord = isRepeatAgentpay(mchAgentpayRecordList);
            if(mchAgentpayRecord != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元"));
            }

            // 批量提交代付
            JSONObject resObj = rpcCommonService.rpcXxPayAgentpayService.batchApplyAgentpay(mchInfo.getPrivateKey(), mchAgentpayRecordList);
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
     * 批量代付申请
     * @param upFile
     * @return
     */
    @MethodLog( remark = "批量代付（上传文件）" )
    @RequestMapping("/batch_upload")
    public Object uploadFile( @RequestParam(value = "upFile", required = true) MultipartFile upFile){
        Long startTime = System.currentTimeMillis();

        Long agentpayAmountL = getRequiredAmountL( "agentpayAmount");         // 代付总金额,转成分
        Integer agentpayNumber = getValIntegerRequired( "agentpayNumber");       // 代付笔数
        Byte subAmountFrom = getValByteRequired( "subAmountFrom");


        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getBelongInfoId());

        String payPassword = getValString( "payPassword");
        String vercode = getValString( "vercode");
        if(upFile.isEmpty()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        List<AgentpayRecord> mchAgentpayRecordList = null;
        try {
            String fileName = upFile.getOriginalFilename();
            List<List<String>> list = null;
            if(fileName.endsWith(".csv")) {
                InputStreamReader fr = new InputStreamReader(upFile.getInputStream());
                list = FileUtils.readCSVFile(fr, "GBK");
            }else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                list = POIUtil.readExcel(upFile);
            }
            if(CollectionUtils.isEmpty(list)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
            }

            mchAgentpayRecordList = new LinkedList<>();
            // 记录文件中总金额
            Long totalAmount= 0l;
            Integer totalNum = 0;
            for(int i = 0; i < list.size(); i++) {
                List<String> strList = list.get(i);
                try {
                    if(i > 0 && StringUtils.isNotBlank(strList.get(0))) {
                        AgentpayRecord mchAgentpayRecord = new AgentpayRecord();
                        mchAgentpayRecord.setInfoId(mchInfo.getMchId());
                        mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
                        mchAgentpayRecord.setMchType(mchInfo.getType());
                        mchAgentpayRecord.setSubAmountFrom(subAmountFrom);
                        mchAgentpayRecord.setAccountName(StringUtils.deleteWhitespace(strList.get(0)));     // 账户名
                        mchAgentpayRecord.setAccountNo(StringUtils.deleteWhitespace(strList.get(1)));       // 账号
                        Long amount = new BigDecimal(StringUtils.deleteWhitespace(strList.get(2))).multiply(new BigDecimal(100)).longValue();
                        if(amount <= 0) {
                            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
                        }
                        mchAgentpayRecord.setAmount(amount);                // 代付金额
                        mchAgentpayRecord.setRemark("代付:" + amount/100 + "元");
                        mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
                        mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
                        mchAgentpayRecord.setDevice("web");
                        totalAmount += amount;
                        totalNum++;
                        mchAgentpayRecordList.add(mchAgentpayRecord);
                    }
                }catch (Exception e) {
                    _log.error(e, "第" + i + "行数据处理异常");
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
                }
            }
            // 如果账户记录为空
            if(mchAgentpayRecordList.size() == 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY));
            }
            if((mchAgentpayRecordList.size() > 1000)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT));
            }
            // 文件中的金额与申请代付金额不符
            if(totalAmount.compareTo(agentpayAmountL) != 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_AMOUNT));
            }
            // 文件中的笔数与申请代付笔数不一致
            if(totalNum.compareTo(agentpayNumber) != 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_NUMBER));
            }
            // 查看是否有重复代付申请记录
            AgentpayRecord mchAgentpayRecord = isRepeatAgentpay(mchAgentpayRecordList);
            if(mchAgentpayRecord != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元"));
            }
            // 批量提交代付
            JSONObject resObj = rpcCommonService.rpcXxPayAgentpayService.batchApplyAgentpay(mchInfo.getPrivateKey(), mchAgentpayRecordList);
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
        } catch (IOException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }


    /**
     * 判断是否有重复的代付申请(卡号+金额),只要有重复即返回
     * @param mchAgentpayRecordList
     * @return
     */
    AgentpayRecord isRepeatAgentpay(List<AgentpayRecord> mchAgentpayRecordList) {
        Map<String, AgentpayRecord> map = new HashMap<>();
        if(mchAgentpayRecordList == null || mchAgentpayRecordList.size() <= 1) return null;
        for(AgentpayRecord record : mchAgentpayRecordList) {
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

        AgentpayRecord mchAgentpayRecord = getObject( AgentpayRecord.class);
        if(mchAgentpayRecord == null) mchAgentpayRecord = new AgentpayRecord();
        mchAgentpayRecord.setInfoId(getUser().getBelongInfoId());
        mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
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
        mchAgentpayRecord.setInfoId(getUser().getBelongInfoId());
        mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
        mchAgentpayRecord = rpcCommonService.rpcAgentpayService.find(mchAgentpayRecord);
        if(mchAgentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayRecord));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long mchId = getUser().getBelongInfoId();
        String agentpayOrderId = getValString( "agentpayOrderId");
        String accountName = getValString( "accountName");
        Byte status = getValByte("status");
        Byte agentpayChannel = getValByte("agentpayChannel");
        Byte subAmountFrom = getValByte("subAmountFrom");
        Byte infoType = 2;
        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcAgentpayService.count4All(mchId, accountName, agentpayOrderId, null, status, agentpayChannel, infoType, subAmountFrom, createTimeStartStr, createTimeEndStr);

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
        mchAgentpayRecord.setInfoId(getUser().getBelongInfoId());
        mchAgentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        int count = rpcCommonService.rpcAgentpayService.count(mchAgentpayRecord, getQueryObj());
        if(count > MchConstant.MAX_EXPORT_ROW) return RetEnum.RET_SERVICE_OUT_OF_RANGE_MAX_EXPORT_ROW.getMessage();
        List<AgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.select(0, MchConstant.MAX_EXPORT_ROW, mchAgentpayRecord, getQueryObj());
        for(AgentpayRecord mchAgentpayRecord1 : mchAgentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord1.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord1.setAccountNo(accountNo);
        }

        List<List> excelData = new ArrayList<>();
        List header = Arrays.asList(new String[]{"转账单号", "账户名", "账号", "代付金额(元)", "手续费(元)", "扣减账户金额(元)", "状态", "代付渠道", "时间"});
        excelData.add(header);
        for(AgentpayRecord record : mchAgentpayRecordList){
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
            rowData.add(record.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_PLAT ? "商户后台" : "API接口");
            rowData.add(DateUtil.date2Str(record.getCreateTime()));
            excelData.add(rowData);
        }

        super.writeExcelStream("代付列表", excelData);
        return null;
    }
}
