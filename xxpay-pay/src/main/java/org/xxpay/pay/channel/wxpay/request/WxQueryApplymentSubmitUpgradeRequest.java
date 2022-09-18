package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 查询微信升级审核状态  **/
@XStreamAlias("xml")
public class WxQueryApplymentSubmitUpgradeRequest extends BaseWxPayRequest {


    /** 接口版本号 */
    @XStreamAlias("version")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    protected void checkConstraints() throws WxPayException {

    }
}
