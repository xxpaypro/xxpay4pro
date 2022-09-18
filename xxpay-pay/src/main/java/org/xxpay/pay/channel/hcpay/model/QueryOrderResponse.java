package org.xxpay.pay.channel.hcpay.model;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/8/9
 * @description:
 */
public class QueryOrderResponse {

    public String merCode;
    public String beginTime;
    public String endTime;
    public String pageIndex;
    public String resultCount;
    public String pageSize;
    public String resultCode;
    public List<Order> list;

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public class Order {

        public String orderAmount;
        public String orderDate;
        public String orderNumber;
        public String orderStatus;
        public String gouduiStatus;
        public String refundStatus;

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getGouduiStatus() {
            return gouduiStatus;
        }

        public void setGouduiStatus(String gouduiStatus) {
            this.gouduiStatus = gouduiStatus;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }
    }

    @Override
    public String toString() {
        return "QueryOrderResponse{" +
                "merCode='" + merCode + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", pageIndex='" + pageIndex + '\'' +
                ", resultCount='" + resultCount + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", list=" + list +
                '}';
    }
}
