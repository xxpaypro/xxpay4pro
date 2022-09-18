package org.xxpay.core.common.domain.api;

/**
 * 退款查单接口api 实体Bean
 */
public class SettRes extends AbstractRes {

    /**  **/
    private String infoId;

    /**  **/
    private String mchOrderNo;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

//    public void setBySettRecord(SettRecord settRecord){
//
//        this.infoId = settRecord.getInfoId() + "";
//        this.mchOrderNo = settRecord.getMchOrderNo();
//
//    }


}


