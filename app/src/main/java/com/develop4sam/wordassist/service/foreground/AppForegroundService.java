package com.develop4sam.wordassist.service.foreground;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.develop4sam.wordassist.R;

public class AppForegroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, "wordassist")
                .setContentTitle("WordAssist running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

