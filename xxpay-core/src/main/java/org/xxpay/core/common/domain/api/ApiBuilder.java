package org.xxpay.core.common.domain.api;

import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.constant.RetEnum;

/**
 * api响应结果构造器
 */
public class ApiBuilder {


    /** 业务处理失败 , 根据枚举值 **/
    public static AbstractRes bizError(PayEnum payEnum){

        PayRes result = new PayRes(); //通信失败, 仅返回retCode 和 retMsg , 实例化任一实现类
        result.setRetCode(payEnum.getCode());
        result.setRetMsg(payEnum.getMessage());
        return result;

    }

    /** 业务处理失败, 自定义错误描述 **/
    public static AbstractRes bizError(String retMsg){

        PayRes result = new PayRes(); //通信失败, 仅返回retCode 和 retMsg , 实例化任一实现类
        result.setRetCode(PayEnum.ERROR_OTHER.getCode());
        result.setRetMsg(retMsg);
        return result;
    }

    /**
     *  纳呈支付添加
     */
    /** 业务处理失败 , 根据枚举值 **/
    public static AbstractRes bizError(RetEnum retEnum){

        PayRes result = new PayRes(); //通信失败, 仅返回retCode 和 retMsg , 实例化任一实现类
        result.setRetCode(String.valueOf(retEnum.getCode()));
        result.setRetMsg(retEnum.getMessage());
        return result;
    }

    public static AbstractRes bizError(RetEnum retEnum, String msg){

        PayRes result = new PayRes(); //通信失败, 仅返回retCode 和 retMsg , 实例化任一实现类
        result.setRetCode(String.valueOf(retEnum.getCode()));
        result.setRetMsg(retEnum.getMessage() + ", param_key=" + msg);
        return result;
    }

    /** 业务处理失败, 上游错误描述 **/
    public static AbstractRes channelError(String channelErrCode, String channelErrMsg){

        PayRes result = new PayRes(); //通信失败, 仅返回retCode 和 retMsg , 实例化任一实现类
        PayEnum channelError = PayEnum.ERROR_CHANNEL_ERROR;
        result.setRetCode(channelError.getCode());
        result.setRetMsg(channelError.getMessage()
                        .replace("${errCode}", channelErrCode)
                        .replace("${errMsg}", channelErrMsg)
        );
        return result;
    }


    /** 构建自定义响应对象, 默认响应成功 **/
    public static <T extends AbstractRes> T buildSuccess(Class<? extends AbstractRes> T){

        try {
            T result = (T)T.newInstance();
            result.setRetCode(PayEnum.OK.getCode());
            return result;

        } catch (Exception e) { return null; }
    }

}
