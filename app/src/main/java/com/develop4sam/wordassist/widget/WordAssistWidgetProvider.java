package com.develop4sam.wordassist.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.develop4sam.wordassist.R;

import com.develop4sam.wordassist.overlay.service.OverlayService;

public class WordAssistWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_TOGGLE_SERVICE = "com.develop4sam.wordassist.TOGGLE_FLOATING_SERVICE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_TOGGLE_SERVICE.equals(intent.getAction())) {
            android.content.SharedPreferences prefs = context.getSharedPreferences("wordassist_prefs",
                    Context.MODE_PRIVATE);
            boolean currentlyEnabled = prefs.getBoolean("overlay_enabled", false);
            boolean newState = !currentlyEnabled;

            // Save new state
            prefs.edit().putBoolean("overlay_enabled", newState).apply();

            Intent serviceIntent = new Intent(context, OverlayService.class);
            if (newState) {
                // Add all display settings from preferences
                serviceIntent.putExtra("showPhonetic", prefs.getBoolean("show_phonetic", true));
                serviceIntent.putExtra("showExamples", prefs.getBoolean("show_examples", true));
                serviceIntent.putExtra("showSynonyms", prefs.getBoolean("show_synonyms", true));
                serviceIntent.putExtra("showAntonyms", prefs.getBoolean("show_antonyms", true));
                serviceIntent.putExtra("autoExpand", prefs.getBoolean("auto_expand", false));
                context.startService(serviceIntent);
            } else {
                context.stopService(serviceIntent);
            }

            // Update all widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, WordAssistWidgetProvider.class));
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout_v2);

        android.content.SharedPreferences prefs = context.getSharedPreferences("wordassist_prefs",
                Context.MODE_PRIVATE);
        boolean isEnabled = prefs.getBoolean("overlay_enabled", false);

        // Change switch appearance
        int switchDrawable = isEnabled ? R.drawable.widget_switch_on : R.drawable.widget_switch_off;
        views.setImageViewResource(R.id.widget_toggle_image, switchDrawable);

        // Update status text
        views.setTextViewText(R.id.widget_status_text, isEnabled ? "ON" : "OFF");
        views.setTextColor(R.id.widget_status_text,
                isEnabled ? 0xFF00796B : 0xFF666666);

        // Make the switch clickable
        Intent toggleIntent = new Intent(context, WordAssistWidgetProvider.class);
        toggleIntent.setAction(ACTION_TOGGLE_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.widget_toggle_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}