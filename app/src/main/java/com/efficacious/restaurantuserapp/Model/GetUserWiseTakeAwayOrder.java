package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class GetUserWiseTakeAwayOrder {
    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("Register_Id")
    private Integer registerId;
    @SerializedName("Res_Id")
    private Integer resId;
    @SerializedName("Status")
    private String status;
    @SerializedName("Total")
    private Integer total;
    @SerializedName("Created_Date")
    private String createdDate;

    public GetUserWiseTakeAwayOrder() {
    }

    public GetUserWiseTakeAwayOrder(Integer orderId, Integer registerId, Integer resId, String status, Integer total, String createdDate) {
        this.orderId = orderId;
        this.registerId = registerId;
        this.resId = resId;
        this.status = status;
        this.total = total;
        this.createdDate = createdDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
