package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetExistingOrderDetailResponse {
    @SerializedName("GetExistingOrderDetails")
    private List<GetExistingOrderDetail> getExistingOrderDetails = null;

    public List<GetExistingOrderDetail> getGetExistingOrderDetails() {
        return getExistingOrderDetails;
    }

    public void setGetExistingOrderDetails(List<GetExistingOrderDetail> getExistingOrderDetails) {
        this.getExistingOrderDetails = getExistingOrderDetails;
    }
}
