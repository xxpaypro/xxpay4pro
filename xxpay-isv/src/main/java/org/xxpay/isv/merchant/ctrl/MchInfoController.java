package org.xxpay.isv.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/mch_info")
public class MchInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired("mchId");
        MchInfo dbRecord = rpc.rpcMchInfoService.getOneMch(mchId, getUser().getBelongInfoId(), null);
        if(dbRecord == null) throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(dbRecord));
    }

    /** 新增商户信息 */
    @RequestMapping("/add")
    @MethodLog( remark = "新增商户" )
    public XxPayResponse add() {

        //获取请求参数
        MchInfo mchInfo = getObject(MchInfo.class);
        mchInfo.setIsvId(getUser().getBelongInfoId());
        mchInfo.setStatus(MchConstant.STATUS_OK); //商户状态： 服务商新增状态默认开启

        // 持久化操作
        rpc.rpcMchInfoService.add(mchInfo);

        return XxPayResponse.buildSuccess();
    }


    /** 更新商户信息  */
    @RequestMapping("/update")
    @MethodLog( remark = "更新商户信息" )
    public ResponseEntity<?> update() {

        //获取请求参数并转换为对象类型
        MchInfo updateRecord = getObject(MchInfo.class);
        Long updatedMchId = updateRecord.getMchId();  //需更新的mchId

        MchInfo dbRecord = rpc.rpcMchInfoService.findByMchId(updatedMchId); //需更新的数据库信息
        if(dbRecord == null || !dbRecord.getIsvId().equals(getUser().getBelongInfoId())){ //越权操作
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        //更新操作
        rpc.rpcMchInfoService.updateMch(updateRecord);

        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    /** 审核商户信息  */
    @RequestMapping("/audit")
    @MethodLog( remark = "审核商户信息" )
    public XxPayResponse audit() {

        Long mchId = getValLong("mchId");
        Byte status = getValByte("status");
        MchInfo mchInfo  = rpc.rpcMchInfoService.findByMchId(mchId);

        //校验越权操作
        if(mchInfo == null || !mchInfo.getIsvId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        if(mchInfo.getStatus() != MchConstant.STATUS_AUDIT_ING){ ///商户状态非待审核状态
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        rpc.rpcMchInfoService.auditMch(mchId, status);
        return XxPayResponse.buildSuccess();
    }

    /** 签约商户  */
    @RequestMapping("/sign")
    @MethodLog( remark = "签约商户" )
    public XxPayResponse sign() {

        Long mchId = getValLong("mchId");
        Byte signStatus = getValByte("signStatus");
        MchInfo mchInfo  = rpc.rpcMchInfoService.findByMchId(mchId);

        //校验越权操作
        if(mchInfo == null || !mchInfo.getIsvId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        if(mchInfo.getSignStatus() != MchConstant.MCH_SIGN_STATUS_WAIT_FILL_INFO
        && mchInfo.getSignStatus() != MchConstant.MCH_SIGN_STATUS_WAIT_SIGN){ ///商户状态非待签约状态 || 非补充资料状态
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        MchInfo updateRecord = new MchInfo();
        updateRecord.setMchId(mchId);
        updateRecord.setSignStatus(signStatus);

        if(rpc.rpcMchInfoService.updateById(updateRecord)){
            return XxPayResponse.buildSuccess();
        }
        return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }

    @RequestMapping("/list")
    public PageRes list() {

        MchInfo mchInfo = getObject(MchInfo.class);
        mchInfo.setIsvId(getUser().getBelongInfoId()); //查找该服务商下的商户

        IPage<MchInfo> result = rpc.rpcMchInfoService.selectPage(getIPage(), mchInfo);
        return PageRes.buildSuccess(result);
    }

    @RequestMapping("/list_snapshot")
    public ResponseEntity<?> listSnapshot() {
        MchInfo mchInfo = getObject(MchInfo.class);
        Byte applyStatus = getValByte("applyStatus");
        Byte  applyType = getValByte("applyType");

        mchInfo.setIsvId(getUser().getBelongInfoId()); //查找该服务商下的商户
        int count = rpc.rpcMchInfoService.countSnashot(mchInfo, applyStatus, applyType);
        List<MchInfo> mchInfoList = rpc.rpcMchInfoService.selectSnashotPage((getPageIndex() -1) * getPageSize(), getPageSize(), mchInfo, applyStatus, applyType);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchInfoList, count));
    }


    /** 查询当前服务商下的所有商家集合（基本信息） **/
    @RequestMapping("/listAll")
    public XxPayResponse listAll() {

        LambdaQueryWrapper<MchInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchInfo::getIsvId, getUser().getBelongInfoId());
        queryWrapper.select(MchInfo::getMchId, MchInfo::getMchName);

        List<MchInfo> mchInfoList = rpc.rpcMchInfoService.list(queryWrapper);
        return XxPayResponse.buildSuccess(mchInfoList);
    }



    /** 先查询服务商的所有支付通道， 根据ifTypeCode查询商户的配置信息 **/
    @RequestMapping("/pay_passage_list")
    public ResponseEntity<?> payPassageList() {

        Long mchId = getValLongRequired( "mchId");

        //查询服务商可见支付通道列表
        IPage page = getIPage();
        Long currentIsvId = getUser().getBelongInfoId();
        LambdaQueryWrapper<PayPassage> lambdaQueryWrapper = new QueryWrapper<PayPassage>().lambda();
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoId, currentIsvId);
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_ISV);
        lambdaQueryWrapper.eq(PayPassage::getStatus, MchConstant.PUB_YES); //仅查询 可用 接口类型
        IPage<PayPassage> isvPassageList = rpc.rpcPayPassageService.page(page, lambdaQueryWrapper);

        //查询所有商户配置的通道信息
        LambdaQueryWrapper<PayPassage> lambdaQueryWrapperByMch = new QueryWrapper<PayPassage>().lambda();
        lambdaQueryWrapperByMch.eq(PayPassage::getBelongInfoId, mchId);
        lambdaQueryWrapperByMch.eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_MCH);
        List<PayPassage> mchPassageList = rpc.rpcPayPassageService.list(lambdaQueryWrapperByMch);

        //list 转换为map
        Map<String, PayPassage> mchPassageMap = mchPassageList.stream().collect(Collectors.toMap(PayPassage::getIfTypeCode, Function.identity()));

        //放置商户信息
        isvPassageList.getRecords().stream().forEach(isvPassage -> isvPassage.setPsVal("mchPassage", mchPassageMap.get(isvPassage.getIfTypeCode())));

        //返回对象信息
        return ResponseEntity.ok(PageRes.buildSuccess(isvPassageList));
    }


    /** 获取商户的通道信息 **/
    /** 查询商户的配置信息， 查询该通道的配置信息mchParam, 然后查询目前商户的配置参数并传送ID  **/
    @RequestMapping("/pay_passage_get")
    public ResponseEntity<?> payPassageGet() {
        JSONObject param = getReqParamJSON();
        Long mchId = getValLongRequired( "mchId");
        String ifTypeCode = getValStringRequired( "ifTypeCode");

        PayInterfaceType payInterfaceType = rpc.rpcPayInterfaceTypeService.findByCode(ifTypeCode);

        //商户已经配置的通道
        PayPassage mchPassage = rpc.rpcPayPassageService.getOne(
                new QueryWrapper<PayPassage>().lambda()
                        .eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_MCH)
                        .eq(PayPassage::getBelongInfoId, mchId)
                        .eq(PayPassage::getIfTypeCode, ifTypeCode)
        );

        payInterfaceType.setPsVal("mchPassage", mchPassage);

        //返回对象信息
        return ResponseEntity.ok(PageRes.buildSuccess(payInterfaceType));
    }

    /** 更新记录 **/
    @RequestMapping("/pay_passage_save")
    public ResponseEntity<?> payPassageSave() {

        PayPassage isvPassage = getObject( PayPassage.class); //更新的服务商信息

        if(isvPassage.getId() == null){ //新增
            isvPassage.setRiskStatus(MchConstant.PUB_NO); //风控状态： 0-关闭
            isvPassage.setContractStatus(MchConstant.PUB_NO); //签约状态： 0-未开通
        }

        boolean isTrue = rpc.rpcPayPassageService.saveOrUpdate(isvPassage);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(isTrue));
    }

    /** 重置登录密码 **/
    @MethodLog( remark = "重置商户登录密码" )
    @RequestMapping("/resetLoginPwd")
    public XxPayResponse resetLoginPwd() {

        Long mchId = getValLongRequired( "mchId");

        //判断越权操作
        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null || !mchInfo.getIsvId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }

        if(!rpc.rpcSysService.resetLoginPwd(MchConstant.INFO_TYPE_MCH, mchId)){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }
        return XxPayResponse.buildSuccess();
    }

}