package com.develop4sam.wordassist.overlay.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.develop4sam.wordassist.overlay.view.ManualOverlayView;

public class ManualOverlayService extends Service {

    private static final String TAG = "ManualOverlayService";
    private ManualOverlayView view;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!android.provider.Settings.canDrawOverlays(this)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (view == null) {
            view = new ManualOverlayView(this);
            view.show();
        }

        if (intent != null) {
            if (intent.hasExtra("showPhonetic"))
                view.setShowPhonetic(intent.getBooleanExtra("showPhonetic", true));
            if (intent.hasExtra("showExamples"))
                view.setShowExamples(intent.getBooleanExtra("showExamples", true));
            if (intent.hasExtra("showSynonyms"))
                view.setShowSynonyms(intent.getBooleanExtra("showSynonyms", true));
            if (intent.hasExtra("showAntonyms"))
                view.setShowAntonyms(intent.getBooleanExtra("showAntonyms", true));
            if (intent.hasExtra("themeColor"))
                view.setThemeColor(intent.getStringExtra("themeColor"));
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (view != null) view.remove();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}