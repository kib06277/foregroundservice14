package com.funshow.foregroundservice14;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.UUID;
public class FBHelperService extends Service {
    private static final String CHANNEL_ID = "LiveStreamChannel";
    protected int notificationID = 1;
    protected String ID;
    protected Handler handler;

    public void onCreate() {
        Log.i("APPLifeCycle" , "FBHelperService onCreate");
        super.onCreate();
        this.handler = new Handler();
        this.ID = UUID.randomUUID().toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.i("APPLifeCycle" , "FBHelperService onStartCommand");
            // 建立 NotificationChannel (Android 8.0+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Foreground Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }

            // 建立前景通知
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Service Running")
                    .setContentText("Your service is running in the foreground")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            // 在 5 秒內調用 startForeground() 並顯示通知
            startForeground(999 , notification);
        } catch (Exception e ) {
            Log.i("FunshowError" , "FBHelperService onStartCommand e = " + e);
        }

        // 在此處進行你的其他工作
        return START_NOT_STICKY;
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}