package org.xxpay.pay.channel.maxpay.exception;

/**
 * Created by xiaowei.wang on 2016/4/26.
 */
public class AuthorizationException extends PaymaxException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException() {
    }

    public AuthorizationException(String msg) {
        super(msg);
    }
}
