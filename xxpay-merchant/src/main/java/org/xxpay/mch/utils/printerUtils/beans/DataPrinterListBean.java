package org.xxpay.mch.utils.printerUtils.beans;

import java.util.List;

public class DataPrinterListBean {
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<PrinterList> getList() {
        return list;
    }

    public void setList(List<PrinterList> list) {
        this.list = list;
    }

    private Integer total;
    private List<PrinterList> list;
}
