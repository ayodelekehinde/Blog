package com.cherrio.blog.views.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.cherrio.blog.MainActivity;
import com.cherrio.blog.R;
import com.cherrio.blog.utils.CheckPermissions;

public class StartActivity extends AppCompatActivity {

    private LottieAnimationView mLoading;

    private final String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static int REQUEST_CODE = 202;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mLoading = findViewById(R.id.loading);
        mLoading.setProgress(0);
        mLoading.playAnimation();

        loadSplash();
    }

   private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && !CheckPermissions.hasPermissions(this, permissionList)) {
            ActivityCompat.requestPermissions(this, permissionList, REQUEST_CODE);

        } else {
            loadTheNext();
        }
    }

    private void loadSplash(){
        new Handler().postDelayed(this::checkPermissions, 2000);
    }

    private void loadTheNext(){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 202:
                if (!CheckPermissions.hasPermissions(this, permissionList)) {
                    finish();
                }else {
                    loadTheNext();
                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }
}
