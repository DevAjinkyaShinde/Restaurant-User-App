package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TakeAwayOrderIdResponse {

    @SerializedName("GetTakeAwayOrder")
    private List<GetTakeAwayOrderId> getTakeAwayOrder = null;

    public List<GetTakeAwayOrderId> getGetTakeAwayOrder() {
        return getTakeAwayOrder;
    }

    public void setGetTakeAwayOrder(List<GetTakeAwayOrderId> getTakeAwayOrder) {
        this.getTakeAwayOrder = getTakeAwayOrder;
    }
}
