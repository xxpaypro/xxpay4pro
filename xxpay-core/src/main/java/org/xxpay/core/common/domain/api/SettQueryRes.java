package org.xxpay.core.common.domain.api;

import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.SettRecord;

/**
 * 退款查单接口api 实体Bean
 */
public class SettQueryRes extends AbstractRes {

    /**  **/
    private String infoId;

    /**  **/
    private String mchOrderNo;

    /**  **/
    private String settAmount;

    /**  **/
    private String settFee;

    /**  **/
    private String accountType;

    /**  **/
    private String accountName;

    /**  **/
    private String accountNo;

    /**  **/
    private String status;

    /**  **/
    private String settDate;

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

    public String getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(String settAmount) {
        this.settAmount = settAmount;
    }

    public String getSettFee() {
        return settFee;
    }

    public void setSettFee(String settFee) {
        this.settFee = settFee;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettDate() {
        return settDate;
    }

    public void setSettDate(String settDate) {
        this.settDate = settDate;
    }


//    public void setBySettRecord(SettRecord settRecord){
//        this.infoId = StrUtil.toString(settRecord.getInfoId());
//        this.mchOrderNo = StrUtil.toString(settRecord.getMchOrderNo());
//        this.settAmount = StrUtil.toString(settRecord.getSettAmount());
//        this.settFee = StrUtil.toString(settRecord.getSettFee());
//        this.accountType = StrUtil.toString(settRecord.getAccountType());
//        this.accountName = StrUtil.toString(settRecord.getAccountName());
//        this.accountNo = StrUtil.toString(settRecord.getAccountNo());
//        this.status = StrUtil.toString(settRecord.getSettStatus());
//        this.settDate = settRecord.getSettDate() == null ? "" : settRecord.getSettDate().getTime() + "";
//    }

}


