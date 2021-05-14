package com.cherrio.blog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotifBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(NotificationWorker.class,15, TimeUnit.MINUTES)
                .addTag("work")
                .setInitialDelay(30,TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR,PeriodicWorkRequest.MIN_BACKOFF_MILLIS,TimeUnit.MILLISECONDS);
        PeriodicWorkRequest workRequest = builder.setConstraints(constraints).build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork("work", ExistingPeriodicWorkPolicy.KEEP,
                workRequest);
    }
}
