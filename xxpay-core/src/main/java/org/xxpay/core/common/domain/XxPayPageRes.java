package org.xxpay.core.common.domain;

import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;

import java.io.Serializable;

/**
 * @author: dingzhiwei
 * @date: 17/11/29
 * @description:
 */
public class XxPayPageRes extends XxPayResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private static final MyLog _log = MyLog.getLog(XxPayPageRes.class);

    public long count;     // 记录总数

    public Object extData;    // 返回数据

    public XxPayPageRes(RetEnum retEnum, Object data, long count) {
        super(retEnum, data);
        this.count = count;
        _log.info(this.toString());
    }

    public XxPayPageRes(RetEnum retEnum, Object data, long count, Object extData) {
        super(retEnum, data);
        this.count = count;
        this.extData = extData;
        _log.info(this.toString());
    }

    public static XxPayPageRes buildSuccess(Object data, long count) {
        XxPayPageRes xxPayResponse = new XxPayPageRes(RetEnum.RET_COMM_SUCCESS, data, count);
        return xxPayResponse;
    }

    public static XxPayPageRes buildSuccessExtData(Object data, long count, Object extData) {
        XxPayPageRes xxPayResponse = new XxPayPageRes(RetEnum.RET_COMM_SUCCESS, data, count, extData);
        return xxPayResponse;
    }


    public static XxPayPageRes buildSuccess() {
        XxPayPageRes xxPayResponse = new XxPayPageRes(RetEnum.RET_COMM_SUCCESS, null, 0);
        return xxPayResponse;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public XxPayResponse setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "XxPayPageRes{" +
                "count=" + count +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
