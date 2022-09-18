package org.xxpay.mch.member.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.entity.SysUser;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/member_balance")
public class MemberBalanceController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员储值余额
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long memberId = getValLongRequired( "memberId");
        MemberBalance memberBalance = rpcCommonService.rpcMemberBalanceService.getById(memberId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(memberBalance));
    }

    /**
     * 会员储值明细列表
     */
    @RequestMapping("/history_list")
    @ResponseBody
    public ResponseEntity<?> historyList( Integer page, Integer limit) {


        MemberBalanceHistory balanceHistory = getObject( MemberBalanceHistory.class);
        balanceHistory.setMchId(getUser().getBelongInfoId());

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

        String storeName = null;
        if (balanceHistory != null && StringUtils.isNotBlank(balanceHistory.getOperatorId())) {
            SysUser user = rpcCommonService.rpcSysService.findByUserId(Long.valueOf(balanceHistory.getOperatorId()));
            if(user != null) {
                Long storeId = user.getStoreId();
                storeName = rpcCommonService.rpcMchStoreService.getById(storeId).getStoreName();
            }
        }

        JSONObject object = (JSONObject) JSONObject.toJSON(balanceHistory);
        object.put("storeName", storeName);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

}
