package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class MenuDetail {
    @SerializedName("Cat_Name")
    private String catName;
    @SerializedName("Menu_Id")
    private Integer menuId;
    @SerializedName("Menu_Type")
    private String menuType;
    @SerializedName("Menu_Name")
    private String menuName;
    @SerializedName("Price")
    private Integer price;
    @SerializedName("Res_Id")
    private Integer resId;
    @SerializedName("Cat_Id")
    private Integer catId;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }
}
