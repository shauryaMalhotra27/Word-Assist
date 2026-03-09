package com.develop4sam.wordassist.overlay.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import com.develop4sam.wordassist.overlay.view.FloatingWidgetView;
import com.develop4sam.wordassist.overlay.view.ManualOverlayView;

public class OverlayService extends Service {

    private FloatingWidgetView autoWidget;
    private ManualOverlayView manualWidget;
    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener clipListener;
    private boolean isManualMode = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!Settings.canDrawOverlays(this)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        // Check if mode changed
        boolean newManualMode = intent != null && intent.getBooleanExtra("manualMode", false);
        
        // Initialize widgets if needed
        if (autoWidget == null) {
            autoWidget = new FloatingWidgetView(this);
        }
        if (manualWidget == null) {
            manualWidget = new ManualOverlayView(this);
        }

        // Handle mode switching
        if (intent != null && intent.hasExtra("manualMode")) {
            if (newManualMode != isManualMode) {
                isManualMode = newManualMode;
                
                // Hide current widget and show the appropriate one
                if (isManualMode) {
                    if (autoWidget != null) autoWidget.remove();
                    manualWidget.show();
                } else {
                    if (manualWidget != null) manualWidget.remove();
                    autoWidget.show();
                }
            }
        }

        // Show the appropriate widget based on current mode
        if (isManualMode) {
            if (manualWidget != null) {
                manualWidget.show();
            }
        } else {
            if (autoWidget != null) {
                autoWidget.show();
            }
        }

        if (intent != null && intent.hasExtra("text")) {
            if (!isManualMode && autoWidget != null) {
                autoWidget.updateWord(intent.getStringExtra("text"));
            }
        }

        // Manage clipboard listener for auto mode only
        if (!isManualMode) {
            if (clipboardManager == null) {
                clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                clipListener = () -> {
                    ClipData clip = clipboardManager.getPrimaryClip();
                    if (clip != null && clip.getItemCount() > 0) {
                        CharSequence text = clip.getItemAt(0).getText();
                        if (text != null && text.length() > 1) {
                            if (autoWidget != null) {
                                autoWidget.updateWord(text.toString().trim());
                            }
                        }
                    }
                };

                clipboardManager.addPrimaryClipChangedListener(clipListener);
            }
        } else {
            // In manual mode, cleanup clipboard listener if it exists
            if (clipboardManager != null && clipListener != null) {
                clipboardManager.removePrimaryClipChangedListener(clipListener);
                clipListener = null;
            }
        }

        // Update display settings for the current widget
        if (intent != null) {
            if (!isManualMode && autoWidget != null) {
                // Update auto widget settings
                if (intent.hasExtra("showPhonetic"))
                    autoWidget.setShowPhonetic(intent.getBooleanExtra("showPhonetic", true));
                if (intent.hasExtra("showExamples"))
                    autoWidget.setShowExamples(intent.getBooleanExtra("showExamples", true));
                if (intent.hasExtra("showSynonyms"))
                    autoWidget.setShowSynonyms(intent.getBooleanExtra("showSynonyms", true));
                if (intent.hasExtra("showAntonyms"))
                    autoWidget.setShowAntonyms(intent.getBooleanExtra("showAntonyms", true));
                if (intent.hasExtra("autoExpand"))
                    autoWidget.setAutoExpandOnWordUpdate(intent.getBooleanExtra("autoExpand", false));
            } else if (isManualMode && manualWidget != null) {
                // Update manual widget settings
                if (intent.hasExtra("showPhonetic"))
                    manualWidget.setShowPhonetic(intent.getBooleanExtra("showPhonetic", true));
                if (intent.hasExtra("showExamples"))
                    manualWidget.setShowExamples(intent.getBooleanExtra("showExamples", true));
                if (intent.hasExtra("showSynonyms"))
                    manualWidget.setShowSynonyms(intent.getBooleanExtra("showSynonyms", true));
                if (intent.hasExtra("showAntonyms"))
                    manualWidget.setShowAntonyms(intent.getBooleanExtra("showAntonyms", true));
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

        // Cleanup both widgets
        if (autoWidget != null) autoWidget.remove();
        if (manualWidget != null) manualWidget.remove();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
