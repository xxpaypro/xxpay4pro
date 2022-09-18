package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/trade_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchTradeOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");
        if(StringUtils.isBlank(tradeOrderId)) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(getUser().getBelongInfoId(), tradeOrderId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchTradeOrder));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);
        if(mchTradeOrder == null) mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setMchId(getUser().getBelongInfoId());

        if(getUser().getIsSuperAdmin() == MchConstant.PUB_NO){ //商户子操作员

            MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(getUser().getStoreId());
            mchTradeOrder.setStoreNo(mchStore.getStoreNo());
            mchTradeOrder.setStoreId(mchStore.getStoreId());
        }

        // 订单起止时间
        Date[] queryDate = getQueryDateRange();
        Date createTimeStart = queryDate[0];
        Date createTimeEnd =  queryDate[1];

        long count = rpcCommonService.rpcMchTradeOrderService.count(mchTradeOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.select(
                (getPageIndex() -1) * getPageSize(), getPageSize(), mchTradeOrder, createTimeStart, createTimeEnd);

        List<JSONObject> objects = new LinkedList<>();
        for(MchTradeOrder order : mchTradeOrderList) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            object.put("productName", MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(order.getProductType()));  // 产品名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long mchId = getUser().getBelongInfoId();
        Long storeId = getValLong( "storeId");
        String tradeOrderId = getValString( "tradeOrderId");
        String payOrderId = getValString( "payOrderId");
        Byte tradeType = getValByte("tradeType");
        Byte status = getValByte("status");

        // 订单起止时间
        Date now = new Date();
        String createTimeStartStr = DateUtil.date2Str(now, "yyyy-MM-dd") + " 00:00:00";
        String createTimeEndStr = DateUtil.date2Str(now, "yyyy-MM-dd") + " 23:59:59";
        logger.info(createTimeStartStr + "-----" + createTimeEndStr);
        Map allMap = rpcCommonService.rpcMchTradeOrderService.count4All(mchId, storeId, tradeOrderId, payOrderId, tradeType, status, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                                                 // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                                         // 金额
        obj.put("allTotalDiscountAmount", allMap.get("totalDiscountAmount"));       // 优惠金额
        obj.put("allTotalRefundAmount", allMap.get("totalRefundAmount"));             // 退款金额
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }


    /**
     * 商城或餐饮小程序订单记录列表
     * @return
     */
    @RequestMapping("/mall_list")
    @ResponseBody
    public ResponseEntity<?> mallList() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);

        // 订单起止时间
        Date[] queryDate = getQueryDateRange();
        Date createTimeStart = queryDate[0];
        Date createTimeEnd =  queryDate[1];

        LambdaQueryWrapper<MchTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(MchTradeOrder::getCreateTime);
        queryWrapper.eq(MchTradeOrder::getMchId, getUser().getBelongInfoId());
        if (mchTradeOrder.getIndustryType() != null) {
            queryWrapper.eq(MchTradeOrder::getIndustryType, mchTradeOrder.getIndustryType());
        }else {
            queryWrapper.isNotNull(MchTradeOrder::getIndustryType);
        }

        if (StringUtils.isNotBlank(mchTradeOrder.getTradeOrderId())) queryWrapper.eq(MchTradeOrder::getTradeOrderId, mchTradeOrder.getTradeOrderId());
        if (mchTradeOrder.getPostType() != null) queryWrapper.eq(MchTradeOrder::getPostType, mchTradeOrder.getPostType());
        if (StringUtils.isNotBlank(mchTradeOrder.getReceiveTel())) queryWrapper.eq(MchTradeOrder::getReceiveTel, mchTradeOrder.getReceiveTel());
        if (StringUtils.isNotBlank(mchTradeOrder.getReceiveName())) queryWrapper.like(MchTradeOrder::getReceiveName, "%"+mchTradeOrder.getReceiveName()+"%");
        if (StringUtils.isNotBlank(mchTradeOrder.getTransportNo())) queryWrapper.eq(MchTradeOrder::getTransportNo, mchTradeOrder.getTransportNo());
        if (mchTradeOrder.getPostStatus() != null) queryWrapper.eq(MchTradeOrder::getPostStatus, mchTradeOrder.getPostStatus());
        if(createTimeStart != null) queryWrapper.ge(MchTradeOrder::getCreateTime, createTimeStart);
        if(createTimeEnd != null) queryWrapper.le(MchTradeOrder::getCreateTime, createTimeEnd);

        IPage<MchTradeOrder> list = rpcCommonService.rpcMchTradeOrderService.page(getIPage(), queryWrapper);

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(list.getRecords(), list.getTotal()));
    }

    /**
     * 商城或餐饮小程序订单详情
     * @return
     */
    @RequestMapping("/mall_get")
    @ResponseBody
    public ResponseEntity<?> mallGet() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(tradeOrderId);
        if (mchTradeOrder == null || !mchTradeOrder.getMchId().equals(getUser().getBelongInfoId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }

        JSONObject object = (JSONObject) JSON.toJSON(mchTradeOrder);
        if (mchTradeOrder.getMemberId() != null){
            Member member = rpcCommonService.rpcMemberService.getById(mchTradeOrder.getMemberId());
            object.put("memberName", member.getMemberName());
        }

        List list = new LinkedList();
        list.add(object);

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(list, list.size()));
    }

    /**
     * 商城或餐饮小程序订单--商品详情
     */
    @RequestMapping("/getGoods")
    @ResponseBody
    public ResponseEntity<?> getGoods() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        LambdaQueryWrapper<MchTradeOrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrderDetail::getMchId, getUser().getBelongInfoId());
        queryWrapper.eq(MchTradeOrderDetail::getTradeOrderId, tradeOrderId);

        List<MchTradeOrderDetail> list = rpcCommonService.rpcMchTradeOrderDetailService.list(queryWrapper);

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(list, list.size()));
    }

    /**
     * 商城或餐饮小程序发货
     * @return
     */
    @RequestMapping("/post_start")
    @ResponseBody
    public ResponseEntity<?> postStart() {

        String tradeOrderId = getValStringRequired("tradeOrderId");
        String transportNo = getValStringRequired("transportNo");

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(tradeOrderId);
        if (mchTradeOrder == null || !mchTradeOrder.getMchId().equals(getUser().getBelongInfoId()) || mchTradeOrder.getPostStatus() != MchConstant.TRADE_ORDER_POST_STATUS_INIT){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(tradeOrderId);
        updateRecord.setPostStatus(MchConstant.TRADE_ORDER_POST_STATUS_ING);
        updateRecord.setTransportNo(transportNo);

        boolean result = rpcCommonService.rpcMchTradeOrderService.updateById(updateRecord);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 商城或餐饮小程序确认收货
     * @return
     */
    @RequestMapping("/post_confirm")
    @ResponseBody
    public ResponseEntity<?> postConfirm() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(tradeOrderId);
        if (mchTradeOrder == null || !mchTradeOrder.getMchId().equals(getUser().getBelongInfoId()) || mchTradeOrder.getPostStatus() != MchConstant.TRADE_ORDER_POST_STATUS_ING){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(tradeOrderId);
        updateRecord.setPostStatus(MchConstant.TRADE_ORDER_POST_STATUS_SUCCESS);

        boolean result = rpcCommonService.rpcMchTradeOrderService.updateById(updateRecord);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


    //===================================================开始纳呈支付修改=======================================================================

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/nc/list")
    @ResponseBody
    public ResponseEntity<?> listForNc() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);
        if(mchTradeOrder == null) mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setMchId(getUser().getBelongInfoId());

        if(getUser().getIsSuperAdmin() == MchConstant.PUB_NO){ //商户子操作员

            MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(getUser().getStoreId());
            mchTradeOrder.setStoreNo(mchStore.getStoreNo());
            mchTradeOrder.setStoreId(mchStore.getStoreId());
        }

        // 订单起止时间
        Date[] queryDate = getQueryDateRange();
        Date createTimeStart = queryDate[0];
        Date createTimeEnd =  queryDate[1];

        long count = rpcCommonService.rpcMchTradeOrderService.count(mchTradeOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.selectPlus(
                (getPageIndex() -1) * getPageSize(), getPageSize(), mchTradeOrder, createTimeStart, createTimeEnd);

        List<JSONObject> objects = new LinkedList<>();
        for(MchTradeOrder order : mchTradeOrderList) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            object.put("productName", MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(order.getProductType()));  // 产品名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    /**
     * 订单订单跑批记录列表
     * @return
     */
    @RequestMapping("/nc/listBatch")
    @ResponseBody
    public PageRes listBatchForNc() {
        Long mchId = getValLongRequired( "mchId");
        MchTradeOrderBatch mchTradeOrderBatch = getObject( MchTradeOrderBatch.class);
        if(mchTradeOrderBatch == null) mchTradeOrderBatch = new MchTradeOrderBatch();
        mchTradeOrderBatch.setMchId(mchId);
        IPage<MchTradeOrderBatch> result = rpcCommonService.rpcMchTradeOrderBatchService.selectPage(getIPage(), mchTradeOrderBatch);
        return PageRes.buildSuccess(result);
    }

    /**
     * 结算列表(按日)
     * @return
     */
    @RequestMapping("/nc/addBatch")
    @ResponseBody
    public XxPayResponse addBatchForNc() {

        MchTradeOrderBatch mchTradeOrderBatch = getObject( MchTradeOrderBatch.class);

        //if(mchTradeOrderBatch == null) mchTradeOrderBatch = new MchTradeOrderBatch();

        mchTradeOrderBatch.setBatchTaskStatus(MchConstant.STATUS_OK);

        rpcCommonService.rpcMchTradeOrderBatchService.add(mchTradeOrderBatch);
        return XxPayResponse.buildSuccess();
    }

    /**
     * 结算详情(按日)
     * @return
     */
    @RequestMapping("/nc/selectMchBatch")
    @ResponseBody
    public XxPayResponse selectMchBatch() {

        Long mchId = getValLongRequired( "mchId");
        String batchDate = this.getValStringRequired("batchDate");

        MchTradeOrderBatch mchTradeOrderBatch = rpcCommonService.rpcMchTradeOrderBatchService.getOneMchTradeOrderBatch(batchDate, mchId);

        return XxPayResponse.buildSuccess(mchTradeOrderBatch);
    }

    /**
     * 结算详情(按月)
     * @return
     */
    @RequestMapping("/nc/selectMchBatchMonth")
    @ResponseBody
    public XxPayResponse selectMchBatchMonth() {

        Long mchId = getValLongRequired( "mchId");
        String batchDate = this.getValStringRequired("batchDate");

        MchTradeOrderBatchMonth mchTradeOrderBatchMonth = rpcCommonService.rpcMchTradeOrderBatchMonthService.getOneMchTradeOrderBatchMonth(batchDate, mchId);

        return XxPayResponse.buildSuccess(mchTradeOrderBatchMonth);
    }

    /**
     * 结算列表(按月)
     * @return
     */
    @RequestMapping("/nc/addBatcMonth")
    @ResponseBody
    public XxPayResponse addBatchMonthForNc() {

        MchTradeOrderBatchMonth mchTradeOrderBatchMonth = getObject( MchTradeOrderBatchMonth.class);

        //if(mchTradeOrderBatch == null) mchTradeOrderBatch = new MchTradeOrderBatch();

        mchTradeOrderBatchMonth.setBatchTaskStatus(MchConstant.STATUS_OK);

        rpcCommonService.rpcMchTradeOrderBatchMonthService.add(mchTradeOrderBatchMonth);
        return XxPayResponse.buildSuccess();
    }


    /**
     * 医院列表
     * @return
     */
    @RequestMapping("/nc/listMchInfo")
    @ResponseBody
    public PageRes listMchInfoForNc() {

        MchInfo mchInfo = getObject(MchInfo.class);

        //mchInfo.setIsvId(getUser().getBelongInfoId()); //查找该服务商下的商户

        IPage<MchInfo> result = rpcCommonService.rpcMchInfoService.selectPage(getIPage(), mchInfo);
        List<MchInfo> list = result.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            for (MchInfo mi : list) {
                mi.setPrivateKey("-");
                mi.setRegisterPassword("-");
            }
        }
        return PageRes.buildSuccess(result);
    }

    @RequestMapping("/nc/countMchTrade")
    @ResponseBody
    public XxPayResponse countMchInfoTradeForNc() {

        //获取当前登录商户ID
        Long mchId = getUser().getBelongInfoId();

        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if (mchInfo == null) {
            return XxPayResponse.buildErr("商户不存在");
        }

        //商户角色
        byte role = mchInfo.getMiniRole();
        if (!(role == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION || role == MchConstant.MCH_MINI_ROLE_PLATFORM_OPERATORS)) {
            return XxPayResponse.buildErr("角色为卫健委与平台运行商角色才能查询统计");
        }

        //签约管辖医院数
        Long signCounter = 0l;
        //近3日无交易医院数
        Long noTradeCounter = 0l;
        //产生交易的医院数
        Long tradeHispitalCounter = 0l;


        List<MchInfo> mchInfos = new ArrayList<MchInfo>();
        //卫健委角色
        if (role == MchConstant.MCH_MINI_ROLE_HEALTH_COMMISSION) {
            LambdaQueryWrapper<MchInfo> lambdaQueryWrapperByMch = new QueryWrapper<MchInfo>().lambda();
            lambdaQueryWrapperByMch.eq(MchInfo::getParentId, mchId);
            lambdaQueryWrapperByMch.isNotNull(MchInfo::getHospitalId);
            //查询所有的子节点
            mchInfos = rpcCommonService.rpcMchInfoService.list(lambdaQueryWrapperByMch);
            if (CollectionUtils.isNotEmpty(mchInfos)) {
                //查找当前卫健委下的所有医院管理员和收银员对应的mchId
                List<Long> mchIds = mchInfos.stream().map(MchInfo::getMchId).distinct().collect(Collectors.toList());
                //签约管辖医院
                List<String> hospitalIds = mchInfos.stream().map(MchInfo::getHospitalId).distinct().collect(Collectors.toList());
                signCounter = CollectionUtils.isEmpty(hospitalIds) ? 0l : hospitalIds.size();

                //产生交易医院数
                tradeHispitalCounter =  rpcCommonService.rpcMchTradeOrderService.countMchForTrade(mchIds);
                tradeHispitalCounter = (tradeHispitalCounter == null ? 0l : tradeHispitalCounter);

            }
        }

        //平台运营商
        if (role == MchConstant.MCH_MINI_ROLE_PLATFORM_OPERATORS) {
            LambdaQueryWrapper<MchInfo> lambdaQueryWrapperByMch = new QueryWrapper<MchInfo>().lambda();
            lambdaQueryWrapperByMch.isNotNull(MchInfo::getHospitalId);
            mchInfos = rpcCommonService.rpcMchInfoService.list(lambdaQueryWrapperByMch);
            if (CollectionUtils.isNotEmpty(mchInfos)) {
                //签约管辖医院
                List<String> hospitalIds = mchInfos.stream().map(MchInfo::getHospitalId).distinct().collect(Collectors.toList());
                signCounter = CollectionUtils.isEmpty(hospitalIds) ? 0l : hospitalIds.size();

                //产生交易医院数
                tradeHispitalCounter =  rpcCommonService.rpcMchTradeOrderService.countMchForTrade(null);
                tradeHispitalCounter = (tradeHispitalCounter == null ? 0l : tradeHispitalCounter);

            }
        }

        if (tradeHispitalCounter > signCounter) {
            return XxPayResponse.buildErr("产生交易医院数大于签约医院数, 数据错误");
        }

        //3天之类未发生交易的医院数等于签约医院数减去3天之类发生交易的医院数
        noTradeCounter = signCounter - tradeHispitalCounter;

        //返回的统计数据
        Map<String, Object> resultMap = new HashMap<>();
        //签约医院数
        resultMap.put("signCounter", signCounter);
        //3天未发生交易医院数
        resultMap.put("noTradeCounter", noTradeCounter);

        return XxPayResponse.buildSuccess(resultMap);
    }

    //===================================================结束纳呈支付修改=======================================================================

}
