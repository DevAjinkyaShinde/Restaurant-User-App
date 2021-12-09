package com.efficacious.restaurantuserapp.Model;

import com.google.gson.annotations.SerializedName;

public class RegisterUserDetails {
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("MiddleName")
    private String middleName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("MobileNo")
    private String mobileNo;
    @SerializedName("EmailId")
    private String emailId;
    @SerializedName("Address1")
    private String address1;
    @SerializedName("Address2")
    private String address2;
    @SerializedName("Address3")
    private String address3;
    @SerializedName("ResId")
    private Integer resId;
    @SerializedName("ImeiNo")
    private String imeiNo;
    @SerializedName("Gcm")
    private String gcm;

    public RegisterUserDetails(String firstName, String middleName, String lastName, String mobileNo, String emailId, String address1, String address2, String address3, Integer resId, String imeiNo, String gcm) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.resId = resId;
        this.imeiNo = imeiNo;
        this.gcm = gcm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getGcm() {
        return gcm;
    }

    public void setGcm(String gcm) {
        this.gcm = gcm;
    }

}
