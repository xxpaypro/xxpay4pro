package org.xxpay.mch.utils.printerUtils.beans;

public class PrinterList {
    private String printerId;
    private String printerKey;
    private String printerName;
    private String status;
    private Integer userId;
    private String wifiVersion;
    private String appVersion;
    private Integer createTime;

    public String getPrinterId() {
        return printerId;
    }
    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }
    public String getPrinterKey() {
        return printerKey;
    }
    public void setPrinterKey(String printerKey) {
        this.printerKey = printerKey;
    }
    public String getPrinterName() {
        return printerName;
    }
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getWifiVersion() {
        return wifiVersion;
    }
    public void setWifiVersion(String wifiVersion) {
        this.wifiVersion = wifiVersion;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public Integer getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
