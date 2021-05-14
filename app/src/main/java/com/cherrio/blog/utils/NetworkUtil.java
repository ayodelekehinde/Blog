package com.cherrio.blog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.net.ConnectivityManagerCompat;

public class NetworkUtil {
    private final ConnectivityManager cm;
    private final NetworkInfo info;

    public NetworkUtil(Context context){
        cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = cm.getActiveNetworkInfo();
    }
    public boolean isConnected(){
        return info != null && info.isConnectedOrConnecting();
    }
    public boolean isWifi(){
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
