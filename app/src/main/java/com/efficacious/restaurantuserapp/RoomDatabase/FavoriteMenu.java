package com.efficacious.restaurantuserapp.RoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class FavoriteMenu {

    @PrimaryKey(autoGenerate = true)
    int Id;

    private String catName;
    private Integer menuId;
    private String menuType;
    private String menuName;
    private Integer price;
    private Integer resId;
    private Integer catId;

    public FavoriteMenu(String catName, Integer menuId, String menuType, String menuName, Integer price, Integer resId, Integer catId) {
        this.catName = catName;
        this.menuId = menuId;
        this.menuType = menuType;
        this.menuName = menuName;
        this.price = price;
        this.resId = resId;
        this.catId = catId;
    }

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
