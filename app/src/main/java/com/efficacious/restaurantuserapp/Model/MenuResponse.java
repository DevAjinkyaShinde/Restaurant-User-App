package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuResponse {
    @SerializedName("MenuDetails")
    private List<MenuDetail> menuDetails;

    public List<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(List<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }
}
