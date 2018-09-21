package com.example.maheshpujala.dummy.network;

import com.android.volley.Request;

/**
 * Created by maheshpujala on 23,July,2018
 */
public class RequestModel {

    String URL;
    String payload;
    int requestType;
    public int GET = Request.Method.GET,
            POST = Request.Method.POST,
            PUT = Request.Method.PUT,
            DELETE = Request.Method.DELETE;

    public RequestModel() {
    }


    public RequestModel(String URL, String payload, int requestType) {
        this.URL = URL;
        this.payload = payload;
        this.requestType = requestType;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }


    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }


}
