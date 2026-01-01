package com.develop4sam.wordassist.overlay.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import com.develop4sam.wordassist.overlay.view.FloatingWidgetView;

public class OverlayService extends Service {

    private FloatingWidgetView widget;
    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener clipListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!Settings.canDrawOverlays(this)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (widget == null) {
            widget = new FloatingWidgetView(this);
            widget.show();
        }

        if (widget == null) {
            widget = new FloatingWidgetView(this);
            widget.show();
        }

        if (intent != null && intent.hasExtra("text")) {
            widget.updateWord(intent.getStringExtra("text"));
        }

        if (clipboardManager == null) {

            clipboardManager =
                    (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            clipListener = () -> {
                ClipData clip = clipboardManager.getPrimaryClip();
                if (clip != null && clip.getItemCount() > 0) {
                    CharSequence text = clip.getItemAt(0).getText();
                    if (text != null && text.length() > 1) {
                        widget.updateWord(text.toString().trim());
                    }
                }
            };

            clipboardManager.addPrimaryClipChangedListener(clipListener);
        }

        if (intent != null) {
            if (widget != null) {
                // Update display settings even if widget already exists
                if (intent.hasExtra("showPhonetic"))
                    widget.setShowPhonetic(intent.getBooleanExtra("showPhonetic", true));
                if (intent.hasExtra("showExamples"))
                    widget.setShowExamples(intent.getBooleanExtra("showExamples", true));
                if (intent.hasExtra("showSynonyms"))
                    widget.setShowSynonyms(intent.getBooleanExtra("showSynonyms", true));
                if (intent.hasExtra("showAntonyms"))
                    widget.setShowAntonyms(intent.getBooleanExtra("showAntonyms", true));
                if (intent.hasExtra("autoExpand"))
                    widget.setAutoExpandOnWordUpdate(intent.getBooleanExtra("autoExpand", false));
            }

            if (intent.hasExtra("text")) {
                widget.updateWord(intent.getStringExtra("text"));
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        // ✅ Cleanup clipboard listener
        if (clipboardManager != null && clipListener != null) {
            clipboardManager.removePrimaryClipChangedListener(clipListener);
        }

        if (widget != null) widget.remove();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
