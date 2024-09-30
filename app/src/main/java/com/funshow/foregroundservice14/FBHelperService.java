package com.funshow.foregroundservice14;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.UUID;
public class FBHelperService extends Service {
    private static final String CHANNEL_ID_MEDIA_PROJECTION = "CHANNEL_ID_MEDIA_PROJECTION";
    private static final String CHANNEL_NAME_MEDIA_PROJECTION = "屏幕录制";
    private static final int NOTIFICATION_ID_MEDIA_PROJECTION = 1;
    private NotificationManager NOTIFICATION_MANAGER ;
    public static boolean running = false;

    public void onCreate() {
        Log.i("APPLifeCycle" , "FBHelperService onCreate");
        super.onCreate();
        try {
            NOTIFICATION_MANAGER = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("服务已启动");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_MEDIA_PROJECTION, CHANNEL_NAME_MEDIA_PROJECTION, NotificationManager.IMPORTANCE_HIGH);
                NOTIFICATION_MANAGER.createNotificationChannel(channel);

                notificationBuilder.setChannelId(CHANNEL_ID_MEDIA_PROJECTION);
            }
            Notification notification = notificationBuilder.build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID_MEDIA_PROJECTION, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
            } else {
                startForeground(NOTIFICATION_ID_MEDIA_PROJECTION, notification);
            }
        } catch (Exception e) {
            Log.i("FunshowError" , "e = " + e);
        }
        running = true;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }
}