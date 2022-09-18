package org.xxpay.manage.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchNotify;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_notify")
public class MchNotifyController extends BaseController {

    private static final MyLog _log = MyLog.getLog(MchNotifyController.class);
    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条商户通知记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String orderId = getValStringRequired( "orderId");
        MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(orderId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchNotify));
    }

    /**
     * 商户通知记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchNotify mchNotify = getObject( MchNotify.class);
        int count = rpcCommonService.rpcMchNotifyService.count(mchNotify);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchNotify> mchNotifyList = rpcCommonService.rpcMchNotifyService.select(
                (getPageIndex() -1) * getPageSize(), getPageSize(), mchNotify);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchNotifyList, count));
    }

    /**
     * 重发通知
     * @return
     */
    @RequestMapping("/resend")
    @ResponseBody
    @MethodLog( remark = "重发商户通知" )
    public ResponseEntity<?> resend() {

        String orderIdsParam = getValStringRequired( "orderIds");
        JSONArray orderIds = JSON.parseArray(orderIdsParam);

        JSONObject resultJSON = new JSONObject();
        if(orderIds.size() <= 0 ){
            resultJSON.put("errMsg", "请选择重发订单！");
            return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJSON));
        }

        if(orderIds.size() > 10 ){
            resultJSON.put("errMsg", "批量重发商户通知个数不得大于10个！");
            return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJSON));
        }

        int sendCount = 0 ;
        for(Object id: orderIds){
            MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(id.toString());
            if(mchNotify.getStatus() == PayConstant.MCH_NOTIFY_STATUS_SUCCESS || StringUtils.isBlank(mchNotify.getNotifyUrl())) continue;

            try {
                byte updateCount = (byte) (mchNotify.getNotifyCount() + 1);
                String result = XXPayUtil.call4Post(mchNotify.getNotifyUrl());
                if("success".equalsIgnoreCase(result)){
                    rpcCommonService.rpcMchNotifyService.updateMchNotifySuccess(mchNotify.getOrderId(), result, updateCount);
                }else{
                    rpcCommonService.rpcMchNotifyService.updateMchNotifyFail(mchNotify.getOrderId(), result, updateCount);
                }
                sendCount ++;
            } catch (Exception e) {
                _log.error("重发失败 id={}", id, e);
            }
        }
        resultJSON.put("sendCount", sendCount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJSON));
    }

}