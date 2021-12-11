package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerDetailsResponse {
    @SerializedName("GetCustomer")
    private List<GetCustomer> getCustomer = null;

    public List<GetCustomer> getGetCustomer() {
        return getCustomer;
    }

    public void setGetCustomer(List<GetCustomer> getCustomer) {
        this.getCustomer = getCustomer;
    }
}
