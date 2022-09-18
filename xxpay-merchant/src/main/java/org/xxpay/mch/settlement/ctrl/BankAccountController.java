package org.xxpay.mch.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/bank_account")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
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
    @MethodLog( remark = "添加结算账户" )
    public ResponseEntity<?> add() {

        SettBankAccount mchBankAccount = getObject( SettBankAccount.class);
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
        if(mchBankAccount != null) {
            mchBankAccount.setInfoId(getUser().getBelongInfoId());
            mchBankAccount.setName(getUser().getNickName());
        }
        // 判断账号是否被使用
        String accountNo = mchBankAccount.getAccountNo();
        if(rpcCommonService.rpcSettBankAccountService.findByAccountNo(accountNo ,MchConstant.INFO_TYPE_MCH) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }
        // 如果是默认,先将其他都修改为非默认
        if(mchBankAccount.getIsDefault() == 1) {
            SettBankAccount updateSettBankAccount = new SettBankAccount();
            updateSettBankAccount.setInfoId(getUser().getBelongInfoId());
            updateSettBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcSettBankAccountService.updateByMchId(updateSettBankAccount, getUser().getBelongInfoId());
        }
        int count = rpcCommonService.rpcSettBankAccountService.add(mchBankAccount);
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

        SettBankAccount mchBankAccount = getObject( SettBankAccount.class);
        if(mchBankAccount == null) mchBankAccount = new SettBankAccount();
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchBankAccount.setInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcSettBankAccountService.count(mchBankAccount);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SettBankAccount> mchAccountHistoryList = rpcCommonService.rpcSettBankAccountService
                .select((getPageIndex()-1) * getPageSize(), getPageSize(), mchBankAccount);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchAccountHistoryList, count));
    }

    /**
     * 修改银行账户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改结算账户" )
    public ResponseEntity<?> update() {

        SettBankAccount mchBankAccount = getObject( SettBankAccount.class);
        if(mchBankAccount != null) mchBankAccount.setInfoId(getUser().getBelongInfoId());
        // 判断账号是否被使用
        String accountNo = mchBankAccount.getAccountNo();
        SettBankAccount querySettBankAccount = rpcCommonService.rpcSettBankAccountService.findById(mchBankAccount.getId());
        if(!querySettBankAccount.getAccountNo().equals(accountNo) && rpcCommonService.rpcSettBankAccountService.findByAccountNo(accountNo ,MchConstant.INFO_TYPE_MCH) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }

        // 如果是默认,先将其他都修改为非默认
        if(mchBankAccount.getIsDefault() == 1) {
            SettBankAccount updateSettBankAccount = new SettBankAccount();
            updateSettBankAccount.setInfoId(getUser().getBelongInfoId());
            updateSettBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcSettBankAccountService.updateByMchId(updateSettBankAccount, getUser().getBelongInfoId());
        }
        int count = rpcCommonService.rpcSettBankAccountService.update(mchBankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 删除银行账户信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除结算账户" )
    public ResponseEntity<?> delete() {

        Long id = getValLongRequired( "id");
        int count = rpcCommonService.rpcSettBankAccountService.delete(id, MchConstant.INFO_TYPE_MCH);
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

        Long id = getValLongRequired( "id");
        SettBankAccount mchBankAccount = new SettBankAccount();
        mchBankAccount.setInfoId(getUser().getBelongInfoId());
        mchBankAccount.setId(id);
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchBankAccount = rpcCommonService.rpcSettBankAccountService.find(mchBankAccount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBankAccount));
    }

    /**
     * 查询默认账号
     * @return
     */
    @RequestMapping("/default_get")
    @ResponseBody
    public ResponseEntity<?> defaultAccount() {
        SettBankAccount mchBankAccount = new SettBankAccount();
        mchBankAccount.setInfoId(getUser().getBelongInfoId());
        mchBankAccount.setIsDefault(MchConstant.PUB_YES);
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
        mchBankAccount = rpcCommonService.rpcSettBankAccountService.find(mchBankAccount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBankAccount));
    }

    /**
     * 设置默认账号
     * @return
     */
    @RequestMapping("/default_set")
    @ResponseBody
    @MethodLog( remark = "设置默认结算账户" )
    public ResponseEntity<?> setDefault() {

        Long id = getValLongRequired( "id");
        // 设置所有状态为非默认
        SettBankAccount mchBankAccount = new SettBankAccount();
        mchBankAccount.setInfoId(getUser().getBelongInfoId());
        mchBankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcSettBankAccountService.update(mchBankAccount);
        _log.info("set all bankAccount default is no, id={}, result={}", id, count);
        // 设置当前为默认
        SettBankAccount mchBankAccount2 = new SettBankAccount();
        mchBankAccount2.setId(id);
        mchBankAccount2.setIsDefault(MchConstant.PUB_YES);
        count = rpcCommonService.rpcSettBankAccountService.update(mchBankAccount2);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 取消默认账号
     * @return
     */
    @RequestMapping("/default_cancel")
    @ResponseBody
    @MethodLog( remark = "取消默认结算账户" )
    public ResponseEntity<?> cancelDefault() {

        Long id = getValLongRequired( "id");
        // 设置当前为非默认
        SettBankAccount mchBankAccount = new SettBankAccount();
        mchBankAccount.setId(id);
        mchBankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcSettBankAccountService.update(mchBankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
