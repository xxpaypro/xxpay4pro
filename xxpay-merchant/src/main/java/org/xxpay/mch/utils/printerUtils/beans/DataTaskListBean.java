package org.xxpay.mch.utils.printerUtils.beans;

import java.util.List;

public class DataTaskListBean {
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<TaskList> getList() {
        return list;
    }

    public void setList(List<TaskList> list) {
        this.list = list;
    }

    private Integer total;
    private List<TaskList> list;
}
