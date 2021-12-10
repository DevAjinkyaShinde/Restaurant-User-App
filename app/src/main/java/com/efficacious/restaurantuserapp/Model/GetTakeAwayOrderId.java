package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class GetTakeAwayOrderId {
    @SerializedName("OrderId")
    private Long orderId;
    @SerializedName("Res_Id")
    private Long resId;
    @SerializedName("TimeStamp")
    private String timeStamp;

    public GetTakeAwayOrderId() {
    }

    public GetTakeAwayOrderId(Long orderId, Long resId, String timeStamp) {
        this.orderId = orderId;
        this.resId = resId;
        this.timeStamp = timeStamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
