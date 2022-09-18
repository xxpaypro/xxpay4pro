package org.xxpay.core.common.Exception;

import org.apache.commons.lang3.StringUtils;
import org.xxpay.core.common.constant.RetEnum;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
public class ServiceException extends RuntimeException {

    private RetEnum retEnum;

    private String extraMsg;

    public ServiceException(RetEnum retEnum) {
        this.retEnum = retEnum;
    }

    public ServiceException(RetEnum retEnum, String extraMsg) {
        this.retEnum = retEnum;
        this.extraMsg = extraMsg;
    }

    public static ServiceException build(RetEnum retEnum) {
        ServiceException serviceException = new ServiceException(retEnum);
        return serviceException;
    }

    public RetEnum getRetEnum() {
        return retEnum;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public String getErrMsg() {
        String errMsg = retEnum.getMessage();
        if(StringUtils.isNotBlank(extraMsg)) {
            errMsg += "[" + extraMsg + "]";
        }
        return errMsg;
    }

}
