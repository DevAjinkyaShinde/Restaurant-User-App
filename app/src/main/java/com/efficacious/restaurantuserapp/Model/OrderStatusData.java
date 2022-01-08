package com.efficacious.restaurantuserapp.Model;

public class OrderStatusData {
//    int OrderId;
    String Status;
    long TimeStamp;

    public OrderStatusData() {

    }

    public OrderStatusData(String status, long timeStamp) {
        Status = status;
        TimeStamp = timeStamp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
