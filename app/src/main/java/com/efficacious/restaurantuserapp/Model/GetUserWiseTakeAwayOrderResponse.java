package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserWiseTakeAwayOrderResponse {
    @SerializedName("GetUserWiseTakeAwayOrder")
    private List<GetUserWiseTakeAwayOrder> getUserWiseTakeAwayOrder = null;

    public List<GetUserWiseTakeAwayOrder> getGetUserWiseTakeAwayOrder() {
        return getUserWiseTakeAwayOrder;
    }

    public void setGetUserWiseTakeAwayOrder(List<GetUserWiseTakeAwayOrder> getUserWiseTakeAwayOrder) {
        this.getUserWiseTakeAwayOrder = getUserWiseTakeAwayOrder;
    }
}
