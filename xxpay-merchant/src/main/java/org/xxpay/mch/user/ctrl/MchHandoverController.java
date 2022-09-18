package org.xxpay.mch.user.ctrl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.MchOperatorHandover;
import org.xxpay.mch.common.ctrl.BaseController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/** 交班接口 **/
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/handover")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchHandoverController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchHandoverController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** 获取当前人的交班前的统计信息 */
    @RequestMapping(value = "/preInfo")
    public XxPayResponse preInfo() {

        try {

            //获取统计信息
            MchOperatorHandover mchOperatorHandover = getStatisticsData();

            return XxPayResponse.buildSuccess(mchOperatorHandover);

        }catch (ServiceException e){
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e){
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 交班确认 */
    @RequestMapping(value = "/confirm")
    public XxPayResponse handoverConfirm() {

        try {

            //请求数据
            MchOperatorHandover reqRecord = getObject(MchOperatorHandover.class);

            //获取统计信息
            MchOperatorHandover dbData = getStatisticsData();

            //数据对比
            if(!dbData.getCountTotalOrder().equals(reqRecord.getCountTotalOrder())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }else if(!dbData.getSumRechargeAmount().equals(reqRecord.getSumRechargeAmount())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }else if(!dbData.getSumDiscountAmount().equals(reqRecord.getSumDiscountAmount())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }else if(!dbData.getSumCashAmount().equals(reqRecord.getSumCashAmount())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }else if(!dbData.getSumRefundAmount().equals(reqRecord.getSumRefundAmount())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }else if(!dbData.getSumRealAmount().equals(reqRecord.getSumRealAmount())){
                throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_DATA_MODIFY);

            }

            dbData.setRemark(reqRecord.getRemark());
            rpcCommonService.rpcMchOperatorHandoverService.operatorHandover(dbData);

            //将accessToken清除
            String authToken = request.getHeader(MchConstant.USER_TOKEN_KEY);
            if(StringUtils.isEmpty(authToken)){
                authToken = request.getParameter(MchConstant.USER_TOKEN_KEY);
            }
            stringRedisTemplate.delete(MchConstant.CACHEKEY_TOKEN_PREFIX_MCH + authToken);

            return XxPayResponse.buildSuccess();

        }catch (ServiceException e){
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e){
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 获取交接班信息 **/
    private MchOperatorHandover getStatisticsData(){

        if(getUser().getWorkStatus() != MchConstant.PUB_YES){ //当前操作员非‘工作’状态， 无法完成交班
            throw ServiceException.build(RetEnum.RET_MCH_HANDOVER_FINISH);
        }

        MchOperatorHandover mchOperatorHandover = new MchOperatorHandover();

        //数据统计
        Date workStartTime = getUser().getWorkStartTime();
        Date workEndTime = new Date();

        //公共查询条件
        Map commonCondition = XXPayUtil.packageDataCommonCondition(workStartTime, workEndTime, null, null, null, null, null, getUser().getUserId(), null, null, null);

        //订单总数量
        Long countTrade = rpcCommonService.rpcMchTradeOrderService.countTrade(commonCondition);
        mchOperatorHandover.setCountTotalOrder(countTrade);

        //充值金额
        BigDecimal sumRechargeAmount = rpcCommonService.rpcMchTradeOrderService.sumRechargeAmount(commonCondition);
        mchOperatorHandover.setSumRechargeAmount(sumRechargeAmount.longValue());

        //优惠金额
        BigDecimal sumDiscountAmount = rpcCommonService.rpcMchTradeOrderService.sumDiscountAmount(commonCondition);
        mchOperatorHandover.setSumDiscountAmount(sumDiscountAmount.longValue());

        //实收现金
        BigDecimal cashAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(XXPayUtil.packageDataCommonCondition(workStartTime, workEndTime, null, null, null, null, null, getUser().getUserId(), MchConstant.MCH_TRADE_PRODUCT_TYPE_CASH, null, null));
        mchOperatorHandover.setSumCashAmount(cashAmount.longValue());

        //退款金额
        BigDecimal sumRefundAmount = rpcCommonService.rpcMchTradeOrderService.sumRefundAmount(commonCondition);
        mchOperatorHandover.setSumRefundAmount(sumRefundAmount.longValue());

        //实收金额
        BigDecimal sumRealAmount = rpcCommonService.rpcMchTradeOrderService.sumRealAmount(commonCondition);
        mchOperatorHandover.setSumRealAmount(sumRealAmount.longValue());

        //获取当前操作员信息
        mchOperatorHandover.setUserId(getUser().getUserId());
        mchOperatorHandover.setUserName(getUser().getNickName());
        mchOperatorHandover.setStoreId(getUser().getStoreId());
        mchOperatorHandover.setMchId(getUser().getBelongInfoId());

        //交班时间
        mchOperatorHandover.setWorkStartTime(workStartTime);
        mchOperatorHandover.setWorkEndTime(workEndTime);

        return mchOperatorHandover;
    }

}