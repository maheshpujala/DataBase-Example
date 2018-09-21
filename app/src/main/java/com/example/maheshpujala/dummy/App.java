package com.example.maheshpujala.dummy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AlertDialog;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by maheshpujala on 20,September,2018
 */
public class App extends MultiDexApplication {
    public static Context myContext;
    private static AlertDialog alertDialog;
    private static App appVolleyInstance;
    private static RequestQueue appVolleyRequestQueue;
    public static final String TAG = App.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        appVolleyInstance = this;
    }


    public static synchronized App getInstance(Context context) {
        myContext = context;
        return appVolleyInstance;
    }


    public static RequestQueue getRequestQueue() {
        if (appVolleyRequestQueue == null) {
            appVolleyRequestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return appVolleyRequestQueue;
    }


    public <T> void addToRequestQueue(com.android.volley.Request<T> req, int tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (appVolleyRequestQueue != null) {
            appVolleyRequestQueue.cancelAll(tag);
        }
    }





    public static boolean isNetworkAvailable(Context context) {
        myContext = context;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            return true;
        }
        return false;
    }



    public static void showDialog(final Context context){
        if(alertDialog != null && alertDialog.isShowing()){

        }else{
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("You need to be connected to internet to use this app.");
            dialogBuilder.setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                    ((Activity)context).finishAffinity();
                    System.exit(0);
                }
            })
                    .setCancelable(false);

            alertDialog  = dialogBuilder.create();
            try{
                if(!((Activity) context).isFinishing()){
                    alertDialog.show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}

