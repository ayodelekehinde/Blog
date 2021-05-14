package com.cherrio.blog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private NavController mNavigationController;

    @Override
    protected void onStart() {
        super.onStart();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(NotificationWorker.class,15, TimeUnit.MINUTES)
                .addTag("work")
                .setInitialDelay(30,TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR,PeriodicWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS);
        PeriodicWorkRequest workRequest = builder.setConstraints(constraints).build();
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueueUniquePeriodicWork("work", ExistingPeriodicWorkPolicy.KEEP,
                workRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdview = findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        mAdview.loadAd(request);

        mNavigationController = Navigation.findNavController(this, R.id.nav_host_frag);
        NavigationUI.setupActionBarWithNavController(this, mNavigationController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavigationController.navigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
