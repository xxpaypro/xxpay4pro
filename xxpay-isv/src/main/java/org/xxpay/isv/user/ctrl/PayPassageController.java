package org.xxpay.isv.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayInterface;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 该服务商支付通道相关查询及配置ctrl  **/
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/pay_passage")
@PreAuthorize("hasRole('"+ MchConstant.ISV_ROLE_NORMAL+"')")
public class PayPassageController extends BaseController {

    private final static MyLog _log = MyLog.getLog(PayPassageController.class);

    @Autowired
    private RpcCommonService rpc;

    /** 查询当前服务商下所有可用 接口类型 **/
    @RequestMapping("/list")
    public XxPayResponse list() {

        Long currentIsvId = getUser().getBelongInfoId();
        LambdaQueryWrapper<PayPassage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoId, currentIsvId);
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_ISV);
        lambdaQueryWrapper.eq(PayPassage::getStatus, MchConstant.PUB_YES); //仅查询 可用 接口类型

        Integer queryPayType = getValInteger("queryPayType"); //根据payType查询
        if(queryPayType != null){
            List<PayInterface> payInterfaceList = rpc.rpcPayInterfaceService.list(new QueryWrapper<PayInterface>().lambda().eq(PayInterface::getPayType, queryPayType));
            List<String> ifTypeCodeList = new ArrayList<>();
            payInterfaceList.stream().forEach(payInterface -> ifTypeCodeList.add(payInterface.getIfTypeCode()));

            //没有查询到任何数据 直接返回空数据集合
            if(ifTypeCodeList.isEmpty()) return PageRes.buildSuccess(Arrays.asList());
            lambdaQueryWrapper.in(PayPassage::getIfTypeCode, ifTypeCodeList);
        }

        IPage page = getIPage(); //允许查询全部数据
        IPage<PayPassage> list = rpc.rpcPayPassageService.page(page, lambdaQueryWrapper);
        return PageRes.buildSuccess(list);
    }

    /** 查询通道配置信息 **/
    @RequestMapping("/get")
    public XxPayResponse get() {

        Long id = getValLongRequired( "id");

        Long currentIsvId = getUser().getBelongInfoId();
        LambdaQueryWrapper<PayPassage> lambdaQueryWrapper = new QueryWrapper<PayPassage>().lambda();
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoId, currentIsvId);
        lambdaQueryWrapper.eq(PayPassage::getBelongInfoType, MchConstant.INFO_TYPE_ISV);
        lambdaQueryWrapper.eq(PayPassage::getStatus, MchConstant.PUB_YES); //仅查询 可用 接口类型
        lambdaQueryWrapper.eq(PayPassage::getId, id);
        PayPassage payPassage = rpc.rpcPayPassageService.getOne(lambdaQueryWrapper);

        if(payPassage == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        ///封装接口信息
        PayInterfaceType ifType = rpc.rpcPayInterfaceTypeService.findByCode(payPassage.getIfTypeCode());
        payPassage.setPsVal("ifTypeObj", ifType);
        return PageRes.buildSuccess(payPassage);
    }


    /** 更新通道配置信息 **/
    @RequestMapping("/update")
    public XxPayResponse update() {

        Integer id = getValIntegerRequired( "id");
        String isvParam = getValStringRequired( "isvParam");

        PayPassage updateRecord = new PayPassage();
        updateRecord.setId(id);
        updateRecord.setIsvParam(isvParam);
        if(rpc.rpcPayPassageService.updateById(updateRecord)){
            return PageRes.buildSuccess();
        }

        throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
    }

    /** 获取当前服务商的授权地址 **/
    @RequestMapping("/alipay_auth_url")
    public XxPayResponse alipayAuthUrl() {

        Long mchId = getValLongRequired( "mchId");

        //查询当前服务商的配置信息
        PayPassage isvPassage = rpc.rpcPayPassageService.selectByIsv(getUser().getBelongInfoId(), PayConstant.CHANNEL_NAME_ALIPAY);
        JSONObject isvParam = JSON.parseObject(isvPassage.getIsvParam());
        String appId = isvParam.getString("appId");

        //支付宝回调xxpay支付中心地址 （需通过授权地址设置）
        String redirectUrl = mainConfig.getPayUrl() + "/alipay/code2token?mchId=" + mchId;

        //商户可直接访问的授权地址链接
        String mchAuthUrl = XXPayUtil.getAlipayUrl4env(3, isvParam) + "?app_id="+appId+"&redirect_uri=" + redirectUrl;

        //二维码图片地址
        String imgUrl =  mainConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(mchAuthUrl);

        JSONObject result = new JSONObject();
        result.put("mchAuthUrl", mchAuthUrl);
        result.put("imgUrl", imgUrl);
        return XxPayResponse.buildSuccess(result);
    }



}