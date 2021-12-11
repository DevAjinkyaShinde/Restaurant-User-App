package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class GetExistingOrderDetail {
    @SerializedName("Id")
    private Integer id;
    @SerializedName("KitchenStatus")
    private String kitchenStatus;
    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("CategoryName")
    private String categoryName;
    @SerializedName("MenuName")
    private String menuName;
    @SerializedName("Price")
    private Float price;
    @SerializedName("Qty")
    private Integer qty;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("EmployeeId")
    private Integer employeeId;
    @SerializedName("vchFoodDescription")
    private String vchFoodDescription;
    @SerializedName("MenuType")
    private String menuType;

    public GetExistingOrderDetail() {
    }

    public GetExistingOrderDetail(Integer id, String kitchenStatus, Integer orderId, String categoryName, String menuName, Float price, Integer qty, String tableName, Integer employeeId, String vchFoodDescription, String menuType) {
        this.id = id;
        this.kitchenStatus = kitchenStatus;
        this.orderId = orderId;
        this.categoryName = categoryName;
        this.menuName = menuName;
        this.price = price;
        this.qty = qty;
        this.tableName = tableName;
        this.employeeId = employeeId;
        this.vchFoodDescription = vchFoodDescription;
        this.menuType = menuType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(String kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getVchFoodDescription() {
        return vchFoodDescription;
    }

    public void setVchFoodDescription(String vchFoodDescription) {
        this.vchFoodDescription = vchFoodDescription;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
