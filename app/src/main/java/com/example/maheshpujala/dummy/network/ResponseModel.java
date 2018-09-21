package com.example.maheshpujala.dummy.network;

public class ResponseModel {

    String payload;
    boolean success;
    int statusCode;
    int requestCallBack;

    public ResponseModel() {
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getRequestCallBack() {
        return requestCallBack;
    }

    public void setRequestCallBack(int requestCallBack) {
        this.requestCallBack = requestCallBack;
    }
}
