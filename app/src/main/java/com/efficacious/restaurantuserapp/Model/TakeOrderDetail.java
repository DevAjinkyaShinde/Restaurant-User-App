package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class TakeOrderDetail {

    @SerializedName("TableName")
    private String tableName;
    @SerializedName("RegisterId")
    private Integer registerId;
    @SerializedName("EmployeeId")
    private Integer employeeId;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("IsActive")
    private Integer isActive;
    @SerializedName("intPersonCOunt")
    private Integer intPersonCOunt;
    @SerializedName("ResId")
    private Integer resId;
    @SerializedName("vchSplitTableName")
    private String vchSplitTableName;
    @SerializedName("vchSplit_status")
    private String vchSplitStatus;
    @SerializedName("Status")
    private String status;

    @SerializedName("TimeStamp")
    private String timeStamp;

    public TakeOrderDetail() {

    }

    public TakeOrderDetail(String tableName, Integer registerId, Integer employeeId, String deviceId, Integer isActive, Integer intPersonCOunt, Integer resId, String vchSplitTableName, String vchSplitStatus, String status, String timeStamp) {
        this.tableName = tableName;
        this.registerId = registerId;
        this.employeeId = employeeId;
        this.deviceId = deviceId;
        this.isActive = isActive;
        this.intPersonCOunt = intPersonCOunt;
        this.resId = resId;
        this.vchSplitTableName = vchSplitTableName;
        this.vchSplitStatus = vchSplitStatus;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIntPersonCOunt() {
        return intPersonCOunt;
    }

    public void setIntPersonCOunt(Integer intPersonCOunt) {
        this.intPersonCOunt = intPersonCOunt;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getVchSplitTableName() {
        return vchSplitTableName;
    }

    public void setVchSplitTableName(String vchSplitTableName) {
        this.vchSplitTableName = vchSplitTableName;
    }

    public String getVchSplitStatus() {
        return vchSplitStatus;
    }

    public void setVchSplitStatus(String vchSplitStatus) {
        this.vchSplitStatus = vchSplitStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
