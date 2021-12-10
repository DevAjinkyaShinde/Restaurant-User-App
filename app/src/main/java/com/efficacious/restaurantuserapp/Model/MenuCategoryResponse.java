package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuCategoryResponse {
    @SerializedName("MenuCategoryDetails")
    private List<MenuCategoryDetail> menuCategoryDetails = null;

    public List<MenuCategoryDetail> getMenuCategoryDetails() {
        return menuCategoryDetails;
    }

    public void setMenuCategoryDetails(List<MenuCategoryDetail> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }

}
