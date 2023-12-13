package ru.mirea.spiridovich.mireaproject.ui.home;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HomeService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}
