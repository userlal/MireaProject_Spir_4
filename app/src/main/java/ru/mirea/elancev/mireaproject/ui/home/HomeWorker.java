package ru.mirea.elancev.mireaproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class HomeWorker extends Worker {
    static final String TAG = "HomeWorker";
    public HomeWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");
        try {
            Intent serviceIntent = new Intent(HomeWorker.this, HomeService.class);
            ContextCompat.startForegroundService(HomeWorker.this, serviceIntent);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doWork: end");
        return Result.success();
    }
}
