package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 查询微信审核状态  **/
@XStreamAlias("xml")
public class WxQueryApplymentSubmitRequest extends BaseWxPayRequest {


    /** 接口版本号 */
    @XStreamAlias("version")
    private String version;

    /** 业务申请编号 */
    @XStreamAlias("business_code")
    private String businessCode;

    /** 业务申请编号 */
    @XStreamAlias("applyment_id")
    private String applymentId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getApplymentId() {
        return applymentId;
    }

    public void setApplymentId(String applymentId) {
        this.applymentId = applymentId;
    }

    @Override
    protected void checkConstraints() throws WxPayException {

    }
}
