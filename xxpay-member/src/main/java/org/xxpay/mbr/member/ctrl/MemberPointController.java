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
import org.xxpay.core.entity.MemberPoints;
import org.xxpay.core.entity.MemberPointsHistory;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_point")
public class MemberPointController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员积分余额
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long memberId = getUser().getMemberId();
        MemberPoints memberPoints = rpcCommonService.rpcMemberPointsService.getById(memberId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(memberPoints));
    }

    /**
     * 会员积分明细列表
     */
    @RequestMapping("/history_list")
    @ResponseBody
    public ResponseEntity<?> historyList( Integer page, Integer limit) {


        MemberPointsHistory pointsHistory = new MemberPointsHistory();
        pointsHistory.setMemberId(getUser().getMemberId());
        pointsHistory.setMchId(getUser().getMchId());

        IPage<MemberPointsHistory> list = rpcCommonService.rpcMemberPointsHistoryService.selectPage(new Page<>(page, limit), pointsHistory);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 积分详情
     */
    @RequestMapping("/history_get")
    @ResponseBody
    public ResponseEntity<?> historyGet() {

        Long id = getValLongRequired( "pointsHistoryId");
        MemberPointsHistory pointsHistory = rpcCommonService.rpcMemberPointsHistoryService.getById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(pointsHistory));
    }

}
