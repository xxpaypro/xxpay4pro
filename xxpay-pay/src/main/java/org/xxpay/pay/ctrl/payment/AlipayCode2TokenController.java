package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.pay.channel.alipay.AlipayApiService;
import org.xxpay.pay.channel.alipay.AlipayConfig;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.RpcCommonService;

/** 支付宝[authCode换取authToken] Controller **/
@Controller
public class AlipayCode2TokenController extends BaseController {

    private final MyLog _log = MyLog.getLog(AlipayCode2TokenController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private AlipayApiService alipayApiService;

    @RequestMapping(value = "/api/alipay/code2token")
    public String code2token() {
        _log.info("###### 开始接收支付宝code2Token接口请求  ######");

        boolean isSuccess = true;  //是否授权成功
        String authType = ""; //授权模式， alipayInner - 支付宝系统授权，  codeImg - 本系统二维码授权
        String errMsg = ""; //错误信息

        try {

            JSONObject reqParam = getJsonParam();
            Long mchId = getLong(reqParam, "mchId");
            if(mchId == null){  //支付宝原生系统， 商户授权通过后的业务
                authType = "alipayInner";

            }else{   //本系统生成授权二维码链接地址， 商户授权通过后的业务

                authType = "codeImg";
                String appAuthCode =  getStringRequired(reqParam, "app_auth_code"); //支付宝请求的authCode
                String ifTypeCode = "alipay";

                //根据商户查询 服务商ID
                MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
                if(mchInfo == null) throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
                if(MchConstant.MCH_TYPE_PRIVATE == mchInfo.getType()){  //私有商户
                    throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL); //私有商户无需授权
                }

                //查询商户的通道信息
                PayPassage mchPassage = rpcCommonService.rpcPayPassageService.selectByMch(mchId, ifTypeCode);
                if(mchPassage != null && mchPassage.getStatus() == MchConstant.PUB_NO){ //通道状态为关闭， 提示授权失败
                    throw ServiceException.build(RetEnum.RET_MCH_STATUS_CLOSE);
                }

                //查询服务商的配置信息
                PayPassage isvPayPassage = rpcCommonService.rpcPayPassageService.selectByIsv(mchInfo.getIsvId(), ifTypeCode);
                AlipayConfig alipayConfig = new AlipayConfig(isvPayPassage.getIsvParam());

                //请求换取token
                JSONObject mchParam = alipayApiService.getOrRefreshToken(alipayConfig, appAuthCode, null);

                //更新参数
                if(mchPassage == null){ //如果为空，则初始化商户配置信息

                    mchPassage = new PayPassage();
                    mchPassage.setBelongInfoId(mchId);
                    mchPassage.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
                    PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
                    mchPassage.setPassageName(payInterfaceType.getIfTypeName());
                    mchPassage.setIfTypeCode(ifTypeCode);
                    mchPassage.setStatus(MchConstant.PUB_YES); //默认开启
                    mchPassage.setRiskStatus(MchConstant.PUB_NO); //风控状态： 关闭
                    mchPassage.setContractStatus(MchConstant.PUB_YES); //签约状态： true
                    mchPassage.setStatus(MchConstant.PUB_YES); //默认开启
                    mchPassage.setMchParam(mchParam.toJSONString());
                    rpcCommonService.rpcPayPassageService.save(mchPassage);

                }else{

                    PayPassage updateRecord = new PayPassage();
                    updateRecord.setId(mchPassage.getId());
                    updateRecord.setMchParam(mchParam.toJSONString());
                    rpcCommonService.rpcPayPassageService.updateById(updateRecord);
                }
            }

        }catch (ServiceException e) {

            isSuccess = false;
            errMsg = e.getErrMsg();
        }catch (Exception e) {
            _log.error(e, "");
            isSuccess = false;
            errMsg = "系统异常，请重新扫码！";
        }

        request.setAttribute("isSuccess", isSuccess);
        request.setAttribute("authType", authType);
        request.setAttribute("errMsg", errMsg);
        return "payment/alipay/code2token";
    }
}
