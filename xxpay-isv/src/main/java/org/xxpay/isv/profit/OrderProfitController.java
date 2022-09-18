package org.xxpay.isv.profit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.OrderProfitDetail;
import org.xxpay.core.entity.OrderProfitTask;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.Date;

/** 订单分润ctrl **/
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sub_order_profit")
public class OrderProfitController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    @RequestMapping("/list")
    public XxPayResponse list() {

        OrderProfitTask orderProfitTask = getObject(OrderProfitTask.class);
        orderProfitTask.setIsvId(getUser().getBelongInfoId());  //仅查询当前服务商的
        orderProfitTask.setAgentPid(0L);  //仅查询当前服务商的直接下属代理商（一级代理商）

        IPage<OrderProfitController> result = rpc.rpcOrderProfitTaskService.page(getIPage(), new QueryWrapper<OrderProfitTask>(orderProfitTask));
        return PageRes.buildSuccess(result);
    }


    /** 查询分润明细 **/
    @RequestMapping("/profit_detail_list")
    public XxPayResponse profitDetailList() {

        Long taskId = getValLongRequired("taskId");
        getAvailableSettTask(); //查询分润任务， 并判断是否越权查询

        IPage<OrderProfitDetail> result = rpc.rpcOrderProfitDetailService.page(getIPage(),
                new QueryWrapper<OrderProfitDetail>().lambda().eq(OrderProfitDetail::getSettTaskId, taskId));
        return PageRes.buildSuccess(result);
    }


    /** 查询单条记录 **/
    @RequestMapping("/get")
    public XxPayResponse get() {

        OrderProfitTask orderProfitTask = getAvailableSettTask(); //查询分润任务， 并判断是否越权查询
        return XxPayResponse.buildSuccess(orderProfitTask);
    }

    /** 发起结算 **/
    @RequestMapping("/settApply")
    public XxPayResponse settApply() {

        String settImg = getValStringRequired("settImg");
        OrderProfitTask dbRecord = getAvailableSettTask(); //查询分润任务， 并判断是否越权查询

        if(dbRecord.getSettStatus() != MchConstant.ISV_SETT_STATUS_WAIT){ //不等于 未结算 状态时
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        if(!rpc.rpcOrderProfitTaskService.updateSettFinish(dbRecord.getTaskId(), settImg)){
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);  //更新失败！
        }
        return XxPayResponse.buildSuccess();
    }


    /** 查询分润任务 （自动判断是否权限是否符合） **/
    private OrderProfitTask getAvailableSettTask(){

        Long taskId = getValLongRequired( "taskId");
        OrderProfitTask orderProfitTask = rpc.rpcOrderProfitTaskService.getById(taskId);
        if(orderProfitTask == null) throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        //禁止越权查询
        if(!getUser().getBelongInfoId().equals(orderProfitTask.getIsvId())){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        if(0 != orderProfitTask.getAgentPid()){  //仅查询一级代理商的分润情况
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }
        return orderProfitTask;
    }

}
