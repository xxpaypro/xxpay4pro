package org.xxpay.pay.channel.maxpay.exception;

/**
 * Created by xiaowei.wang on 2016/4/26.
 */
public abstract class PaymaxException extends Exception {
    private static final long serialVersionUID = 1L;

    public PaymaxException() {
        super();
    }

    public PaymaxException(String msg) {
        super(msg);
    }

    public PaymaxException(String msg, Throwable th) {
        super(msg, th);
    }

    public PaymaxException(Throwable th) {
        super(th);
    }
}
