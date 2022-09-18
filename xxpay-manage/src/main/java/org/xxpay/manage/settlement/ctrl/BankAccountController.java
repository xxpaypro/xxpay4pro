package org.xxpay.manage.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.SettBankAccount;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bank_account")
public class BankAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 添加银行账户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增结算账号" )
    public ResponseEntity<?> add() {

        SettBankAccount mchBankAccount = getObject( SettBankAccount.class);
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
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
        mchBankAccount.setInfoType(MchConstant.INFO_TYPE_MCH);
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
    @MethodLog( remark = "修改结算账号" )
    public ResponseEntity<?> update() {

        SettBankAccount mchBankAccount = getObject( SettBankAccount.class);
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
    @MethodLog( remark = "删除结算账号" )
    public ResponseEntity<?> delete() {

        Long id = getValLongRequired( "id");
        int count = rpcCommonService.rpcSettBankAccountService.delete(id, MchConstant.INFO_TYPE_MCH);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询银行账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        SettBankAccount mchBankAccount = rpcCommonService.rpcSettBankAccountService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBankAccount));
    }

}
