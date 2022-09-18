package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.FileUtils;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.BankCardBin;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.common.util.POIUtil;
import sun.swing.BakedArrayList;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/07/22
 * @description: 银行卡bin
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/card_bin")
public class BankCardBinController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(BankCardBinController.class);

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        BankCardBin bankCardBin = getObject( BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.count(bankCardBin);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<BankCardBin> bankCardBinList = rpcCommonService.rpcBankCardBinService.select((getPageIndex() -1) * getPageSize(), getPageSize(), bankCardBin);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(bankCardBinList, count));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        BankCardBin bankCardBin = rpcCommonService.rpcBankCardBinService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(bankCardBin));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改卡BIN信息" )
    public ResponseEntity<?> update() {

        BankCardBin bankCardBin = getObject( BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.update(bankCardBin);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增卡BIN信息" )
    public ResponseEntity<?> add() {

        BankCardBin bankCardBin = getObject( BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.add(bankCardBin);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 批量导入卡bin
     * @param request
     * @param upFile
     * @return
     */
    @RequestMapping("/import")
    public Object importCardBin( @RequestParam(value = "upFile", required = true) MultipartFile upFile){
        Long startTime = System.currentTimeMillis();
        if(upFile.isEmpty()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        try {
            String fileName = upFile.getOriginalFilename();
            List<List<String>> list = new ArrayList<>();
            if(fileName.endsWith(".csv")) {
                InputStreamReader fr = new InputStreamReader(upFile.getInputStream());
                list = FileUtils.readCSVFile(fr, "GBK");
            }else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                list = POIUtil.readExcel(upFile);
            }
            if(CollectionUtils.isEmpty(list)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
            }

            List<BankCardBin> bankCardBinList = new LinkedList<>();
            int num = 0;
            // 记录文件中总金额
            for(int i = 0; i < list.size(); i++) {
                List<String> strList = list.get(i);
                try {
                    if(i > 3) {
                        BankCardBin bankCardBin = new BankCardBin();
                        String str0 = strList.get(0).trim();    // 发卡行名称及机构代码,如:中国银联支付标记(00010030)
                        String bankName = str0.substring(0, str0.indexOf("("));
                        String bankCode = str0.substring(str0.indexOf("(")+1, str0.indexOf(")"));
                        bankCardBin.setBankName(bankName);                  // 银行名称
                        bankCardBin.setBankCode(bankCode);                  // 银行编码
                        bankCardBin.setCardName(strList.get(1).trim());     // 卡名
                        bankCardBin.setCardType(strList.get(15).trim());    // 银行卡类型
                        bankCardBin.setCardLength(Integer.parseInt(strList.get(8)));    // 卡长度
                        bankCardBin.setCardBin(strList.get(13).trim());     // 卡bin
                        bankCardBinList.add(bankCardBin);
                    }
                }catch (Exception e) {
                    _log.error(e, "第" + i + "行数据处理异常");
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
                }
                // 100个提交批量插入
                if(bankCardBinList.size() > 100) {
                    rpcCommonService.rpcBankCardBinService.insertBatch(bankCardBinList);
                    num+= bankCardBinList.size();
                    bankCardBinList = new LinkedList<>();
                }
            }
            if(bankCardBinList.size() > 0) {
                rpcCommonService.rpcBankCardBinService.insertBatch(bankCardBinList);
                num+= bankCardBinList.size();
            }
            // 返回数据
            JSONObject retObj = new JSONObject();
            retObj.put("batchInertCount", num);
            retObj.put("costTime", System.currentTimeMillis() - startTime); // 记录服务端耗时
            return ResponseEntity.ok(XxPayResponse.buildSuccess(retObj));
        } catch (IOException e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }


}
