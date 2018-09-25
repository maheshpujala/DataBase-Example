package com.example.maheshpujala.dummy.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.maheshpujala.dummy.App;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maheshpujala on 23,July,2018
 */
public class ServerRequest {
    private ProgressDialog progressDialog;
    private Activity activity;
    private ResponseModel serverResponse;

    public ServerRequest(Activity activity) {
        serverResponse = new ResponseModel();
        this.activity = activity;

    }
    public void sendRequest(final RequestModel requestModel,final int requestCallBack,boolean progressBarVisibility, String progressText) {
        if(App.isNetworkAvailable(activity)){
            if(progressBarVisibility){
                progressDialog = new ProgressDialog(activity);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(progressText);
                progressDialog.show();
            }
            Log.e("sendRequest Type:"+ requestModel.getRequestType(),"requestModel.getURL()"+ requestModel.getURL());

            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (requestModel.getRequestType(), requestModel.getURL(), null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e("onResponse"+serverResponse.getStatusCode(),"response=="+response);
                            if(response != null){
                                serverResponse.setPayload(response.toString());
                            }else{
                                serverResponse.setPayload("");
                            }
                            serverResponse.setSuccess(true);
                            serverResponse.setRequestCallBack(requestCallBack);
                            EventBus.getDefault().post(serverResponse);

                            if(progressDialog != null){
                                // create a handler to post messages to the main thread
                                Handler mHandler = new Handler(activity.getMainLooper());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("onErrorResponse","VolleyError"+error);
                            NetworkResponse networkResponse = error.networkResponse;
                            try {
                                Log.e("onerror response","=="+new String(networkResponse.data));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }

                            if (error instanceof NoConnectionError) {
                                App.showDialog(activity);
                                return;
                            } else if (error instanceof AuthFailureError || error instanceof ServerError) {
                                serverResponse.setPayload(new String(networkResponse.data));
                                serverResponse.setStatusCode(networkResponse.statusCode);
                                serverResponse.setSuccess(false);

                            }else if (error instanceof NetworkError) {
                                serverResponse.setPayload("NetworkError");
                                serverResponse.setSuccess(false);

                            } else if (error instanceof ParseError) {
                                serverResponse.setPayload("ParseError");
                                serverResponse.setSuccess(false);

                            }else if (error instanceof TimeoutError) {
                                serverResponse.setPayload("TimeoutError");
                                serverResponse.setSuccess(false);
                                Toast.makeText(activity, "The operation could not be completed due to a timeout.", Toast.LENGTH_LONG).show();
                                return;

                            }
                            serverResponse.setRequestCallBack(requestCallBack);
//                            serverResponseListener.getResponse(serverResponse,requestTag);
                            EventBus.getDefault().post(serverResponse);

                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    Log.e("getBody","=="+ requestModel.getPayload());
                    try {
                        return new JSONObject(requestModel.getPayload()).toString().getBytes("utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new JSONObject().toString().getBytes();
                    }
                }

                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                        serverResponse.setStatusCode(response.statusCode);
                        JSONArray result = null;

                        if (jsonString.length() > 0)
                            result = new JSONArray(jsonString);

                        return Response.success(result,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException | JSONException e) {
                        return Response.error(new ParseError(e));
                    }
                }


            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            App.getInstance(activity).addToRequestQueue(jsObjRequest,requestModel.getRequestType());
        }else{
            serverResponse.setRequestCallBack(requestCallBack);
            serverResponse.setPayload("[]");
            serverResponse.setStatusCode(555);
            serverResponse.setSuccess(false);
            EventBus.getDefault().post(serverResponse);
//            App.showDialog(activity);
        }

    }
}
