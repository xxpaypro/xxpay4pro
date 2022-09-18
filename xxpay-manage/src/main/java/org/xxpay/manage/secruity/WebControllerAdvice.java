package org.xxpay.manage.secruity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;

/**
 *
 */
@ControllerAdvice
public class WebControllerAdvice {

    private static final MyLog _log = MyLog.getLog(WebControllerAdvice.class);

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> errorHandler(Exception ex) {
        _log.error(ex, "");
        String message = ex.getMessage();
        String eMsg = message;
        if(StringUtils.isNotBlank(message) && message.indexOf(":") > 0) {
            eMsg = message.substring(0, message.indexOf(":"));
        }
        if(eMsg.length() > 200) eMsg = eMsg.substring(0, 200);
        BizResponse bizResponse = new BizResponse(99999, "系统异常[" + eMsg + "]");
        return ResponseEntity.ok(bizResponse);
    }
    
    /**
     * 拦截捕捉自定义异常 ServiceException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<?> myErrorHandler(ServiceException ex) {
        return ResponseEntity.ok(XxPayResponse.build(ex.getRetEnum()));
    }

}