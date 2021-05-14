package com.cherrio.blog;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class BlogApplication extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }
    public static Context getAppContext(){
        return context;
    }

}
