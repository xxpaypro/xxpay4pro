package org.xxpay.agent.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SettBankAccount;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p><b>Title: </b>BankAccountController.java
 * <p><b>Description: </b>银行账号ctrl
 * @author terrfly
 * @version V1.0
 * <p>
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/bank_account")
@PreAuthorize("hasRole('"+ MchConstant.AGENT_ROLE_NORMAL+"')")
public class BankAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(BankAccountController.class);

    /**
     * 添加银行账户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增结算账号" )
    public ResponseEntity<?> add() {

        SettBankAccount bankAccount = getObject( SettBankAccount.class);
        bankAccount.setInfoType(MchConstant.INFO_TYPE_AGENT);
        if(bankAccount != null) {
            bankAccount.setInfoId(getUser().getBelongInfoId());
            bankAccount.setName(getUser().getNickName());
        }
        // 判断账号是否被使用
        String accountNo = bankAccount.getAccountNo();
        if(rpcCommonService.rpcSettBankAccountService.findByAccountNo(accountNo, MchConstant.INFO_TYPE_AGENT) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }
        // 如果是默认,先将其他都修改为非默认
        if(bankAccount.getIsDefault() == 1) {
            SettBankAccount updateSettBankAccount = new SettBankAccount();
            updateSettBankAccount.setInfoId(getUser().getBelongInfoId());
            updateSettBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcSettBankAccountService.updateByMchId(updateSettBankAccount, getUser().getBelongInfoId());
        }
        int count = rpcCommonService.rpcSettBankAccountService.add(bankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SettBankAccount bankAccount = getObject( SettBankAccount.class);
        if(bankAccount == null) bankAccount = new SettBankAccount();
        bankAccount.setInfoType(MchConstant.INFO_TYPE_AGENT);
        bankAccount.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSettBankAccountService.count(bankAccount);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettBankAccount> bankAccountList = rpcCommonService.rpcSettBankAccountService
                .select((getPageIndex()-1) * getPageSize(), getPageSize(), bankAccount);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(bankAccountList, count));
    }

    /**
     * 修改银行账户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改结算账号" )
    public ResponseEntity<?> update() {

        SettBankAccount bankAccount = getObject( SettBankAccount.class);
        if(bankAccount != null) bankAccount.setInfoId(getUser().getBelongInfoId());
        // 判断账号是否被使用
        String accountNo = bankAccount.getAccountNo();
        SettBankAccount querySettBankAccount = rpcCommonService.rpcSettBankAccountService.findById(bankAccount.getId());
        if(!querySettBankAccount.getAccountNo().equals(accountNo) && rpcCommonService.rpcSettBankAccountService.findByAccountNo(accountNo, MchConstant.INFO_TYPE_AGENT) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }

        // 如果是默认,先将其他都修改为非默认
        if(bankAccount.getIsDefault() == 1) {
            SettBankAccount updateSettBankAccount = new SettBankAccount();
            updateSettBankAccount.setInfoId(getUser().getBelongInfoId());
            updateSettBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcSettBankAccountService.updateByMchId(updateSettBankAccount, getUser().getBelongInfoId());
        }
        int count = rpcCommonService.rpcSettBankAccountService.update(bankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 删除银行账户信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除结算账号" )
    public ResponseEntity<?> delete() {

        Long id = getValLongRequired("id");
        int count = rpcCommonService.rpcSettBankAccountService.delete(id, MchConstant.INFO_TYPE_AGENT);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired("id");
        SettBankAccount bankAccount = new SettBankAccount();
        bankAccount.setInfoId(getUser().getBelongInfoId());
        bankAccount.setId(id);
        bankAccount.setInfoType(MchConstant.INFO_TYPE_AGENT);
        bankAccount = rpcCommonService.rpcSettBankAccountService.find(bankAccount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(bankAccount));
    }

    /**
     * 查询默认账号
     * @return
     */
    @RequestMapping("/default_get")
    @ResponseBody
    public ResponseEntity<?> defaultAccount() {
        SettBankAccount bankAccount = new SettBankAccount();
        bankAccount.setInfoId(getUser().getBelongInfoId());
        bankAccount.setIsDefault(MchConstant.PUB_YES);
        bankAccount.setInfoType(MchConstant.INFO_TYPE_AGENT);
        bankAccount = rpcCommonService.rpcSettBankAccountService.find(bankAccount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(bankAccount));
    }

    /**
     * 设置默认账号
     * @return
     */
    @RequestMapping("/default_set")
    @ResponseBody
    @MethodLog( remark = "设置默认结算账号" )
    public ResponseEntity<?> setDefault() {

        Long id = getValLongRequired("id");
        // 设置所有状态为非默认
        SettBankAccount bankAccount = new SettBankAccount();
        bankAccount.setInfoId(getUser().getBelongInfoId());
        bankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcSettBankAccountService.update(bankAccount);
        _log.info("set all bankAccount default is no, id={}, result={}", id, count);
        // 设置当前为默认
        SettBankAccount bankAccount2 = new SettBankAccount();
        bankAccount2.setId(id);
        bankAccount2.setIsDefault(MchConstant.PUB_YES);
        count = rpcCommonService.rpcSettBankAccountService.update(bankAccount2);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 取消默认账号
     * @return
     */
    @RequestMapping("/default_cancel")
    @ResponseBody
    @MethodLog( remark = "取消默认结算账号" )
    public ResponseEntity<?> cancelDefault() {

        Long id = getValLongRequired("id");
        // 设置当前为非默认
        SettBankAccount bankAccount = new SettBankAccount();
        bankAccount.setId(id);
        bankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcSettBankAccountService.update(bankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
