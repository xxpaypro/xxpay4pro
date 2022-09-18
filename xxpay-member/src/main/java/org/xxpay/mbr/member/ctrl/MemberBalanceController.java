package org.xxpay.mbr.member.ctrl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MemberBalance;
import org.xxpay.core.entity.MemberBalanceHistory;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_balance")
public class MemberBalanceController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员储值余额
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        MemberBalance memberBalance = rpcCommonService.rpcMemberBalanceService.getById(getUser().getMemberId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(memberBalance));
    }

    /**
     * 会员储值明细列表
     */
    @RequestMapping("/history_list")
    @ResponseBody
    public ResponseEntity<?> historyList( Integer page, Integer limit) {


        MemberBalanceHistory balanceHistory = new MemberBalanceHistory();
        balanceHistory.setMemberId(getUser().getMemberId());
        balanceHistory.setMchId(getUser().getMchId());

        IPage<MemberBalanceHistory> list = rpcCommonService.rpcMemberBalanceHistoryService.selectPage(new Page<>(page, limit), balanceHistory);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 储值详情
     */
    @RequestMapping("/history_get")
    @ResponseBody
    public ResponseEntity<?> historyGet() {

        Long id = getValLongRequired( "balanceHistoryId");
        MemberBalanceHistory balanceHistory = rpcCommonService.rpcMemberBalanceHistoryService.getById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(balanceHistory));
    }

}
