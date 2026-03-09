package com.develop4sam.wordassist.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.develop4sam.wordassist.R;
import com.develop4sam.wordassist.accessibility.WordAssistAccessibilityService;
import com.develop4sam.wordassist.overlay.service.ManualOverlayService;
import com.develop4sam.wordassist.overlay.service.OverlayService;
import com.develop4sam.wordassist.ui.permission.PermissionDialog;
import com.develop4sam.wordassist.ui.permission.PermissionManager;

//public class MainActivity extends AppCompatActivity {
//
//    private static final String PREF_NAME = "wordassist_prefs";
//    private static final String KEY_OVERLAY_ENABLED = "overlay_enabled";
//
//    private TextView txtAccessibilityStatus, txtOverlayStatus, txtRequiredPermissionEnable, txtWordAssistHeading;
//    private com.google.android.material.button.MaterialButton btnAccessibility, btnOverlay;
//    private SwitchCompat switchEnable;
//    private SharedPreferences prefs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//
//        // Views
//        txtAccessibilityStatus = findViewById(R.id.txtAccessibilityStatus);
//        txtOverlayStatus = findViewById(R.id.txtOverlayStatus);
//        btnAccessibility = findViewById(R.id.btn_accessibility);
//        btnOverlay = findViewById(R.id.btn_overlay);
//        switchEnable = findViewById(R.id.switchEnable);
//        txtRequiredPermissionEnable = findViewById(R.id.txtRequiredPermissionEnable);
//        txtWordAssistHeading = findViewById(R.id.txtWordAssistHeading);
//
//        // Accessibility permission button
//        btnAccessibility.setOnClickListener(v ->
//                PermissionDialog.showAccessibilityExplanation(
//                        this,
//                        () -> PermissionManager.openAccessibilitySettings(this)
//                )
//        );
//
//        // Overlay permission button
//        btnOverlay.setOnClickListener(v -> {
//            if (!PermissionManager.hasOverlayPermission(this)) {
//                PermissionDialog.showOverlayExplanation(
//                        this,
//                        () -> PermissionManager.requestOverlayPermission(this)
//                );
//            }
//        });
//
//        // Switch listener - only react to user interaction
//        switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isPressed()) { // Ensures it's a user action, not programmatic
//                prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, isChecked).apply();
//
//                if (isChecked) {
//                    startOverlayService();
//                    txtWordAssistHeading.setText("Stop Word Assist Service");
//                } else {
//                    stopOverlayService();
//                    txtWordAssistHeading.setText("Start Word Assist Service");
//                }
//                txtWordAssistHeading.setText(switchEnable.isChecked() ? "Stop Word Assist Service" : "Start Word Assist Service");
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        updatePermissionUI();
//    }
//
//    private void updatePermissionUI() {
//        boolean accessibilityGranted = PermissionManager.isAccessibilityEnabled(
//                this,
//                WordAssistAccessibilityService.class
//        );
//
//        boolean overlayGranted = Settings.canDrawOverlays(this);
//
//        // Accessibility UI
//        if (accessibilityGranted) {
//            txtAccessibilityStatus.setText("App Accessibility");
//            btnAccessibility.setText("Allowed");
//            btnAccessibility.setEnabled(false);
//            btnAccessibility.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
//            btnAccessibility.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
//            btnAccessibility.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
//        } else {
//            txtAccessibilityStatus.setText("App Accessibility");
//            btnAccessibility.setText("Allow");
//            btnAccessibility.setEnabled(true);
//        }
//
//        // Overlay UI
//        if (overlayGranted) {
//            txtOverlayStatus.setText("Floating Overlay");
//            btnOverlay.setText("Allowed");
//            btnOverlay.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
//            btnOverlay.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
//            btnOverlay.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
//            btnOverlay.setEnabled(false);
//        } else {
//            txtOverlayStatus.setText("Floating Overlay");
//            btnOverlay.setText("Allow");
//            btnOverlay.setEnabled(true);
//        }
//
//        // Show/hide permission warning
//        if (accessibilityGranted && overlayGranted) {
//            txtRequiredPermissionEnable.setVisibility(TextView.GONE);
//            switchEnable.setEnabled(true);
//
//            // Restore previous switch state only when permissions are fully granted
//            boolean wasEnabled = prefs.getBoolean(KEY_OVERLAY_ENABLED, false);
//            switchEnable.setChecked(wasEnabled);
//            updateSwitchText(wasEnabled);
//        } else {
//            txtRequiredPermissionEnable.setVisibility(TextView.VISIBLE);
//            switchEnable.setEnabled(false);
//            switchEnable.setChecked(false);
//            prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, false).apply();
//            updateSwitchText(false);
//        }
//    }
//
//    private void updateSwitchText(boolean isChecked) {
//        switchEnable.setTextOn("Stop Word Assist Service");
//        txtWordAssistHeading.setText("Start Word Assist Service");
//        txtWordAssistHeading.setText(isChecked ? "Stop Word Assist Service" : "Start Word Assist Service");
//    }
//
//    private void startOverlayService() {
//        startService(new Intent(this, OverlayService.class));
//        Toast.makeText(this, "Word Assist Started", Toast.LENGTH_SHORT).show();
//    }
//
//    private void stopOverlayService() {
//        stopService(new Intent(this, OverlayService.class));
//        Toast.makeText(this, "Word Assist Stopped", Toast.LENGTH_SHORT).show();
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    private static final String PREF_NAME = "wordassist_prefs";
//    private static final String KEY_OVERLAY_ENABLED = "overlay_enabled";
//
//    // Preference keys for display settings
//    private static final String KEY_SHOW_PHONETIC = "show_phonetic";
//    private static final String KEY_SHOW_EXAMPLES = "show_examples";
//    private static final String KEY_SHOW_SYNONYMS = "show_synonyms";
//    private static final String KEY_SHOW_ANTONYMS = "show_antonyms";
//    private static final String KEY_AUTO_EXPAND = "auto_expand";
//
//    // Views
//    private TextView txtAccessibilityStatus, txtOverlayStatus, txtRequiredPermissionEnable, txtWordAssistHeading;
//    private com.google.android.material.button.MaterialButton btnAccessibility, btnOverlay;
//    private SwitchCompat switchEnable;
//    private CardView cardDisplaySettings;
//
//    // Display setting switches
//    private SwitchCompat switchPhonetic, switchExamples, switchSynonyms, switchAntonyms, switchAutoExpand;
//
//    private SharedPreferences prefs;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//
//        // Existing views
//        txtAccessibilityStatus = findViewById(R.id.txtAccessibilityStatus);
//        txtOverlayStatus = findViewById(R.id.txtOverlayStatus);
//        btnAccessibility = findViewById(R.id.btn_accessibility);
//        btnOverlay = findViewById(R.id.btn_overlay);
//        switchEnable = findViewById(R.id.switchEnable);
//        txtRequiredPermissionEnable = findViewById(R.id.txtRequiredPermissionEnable);
//        txtWordAssistHeading = findViewById(R.id.txtWordAssistHeading);
//
//        // New: Display settings card and switches
//        cardDisplaySettings = findViewById(R.id.cardDisplaySettings);
//        switchPhonetic = findViewById(R.id.switchPhonetic);
//        switchExamples = findViewById(R.id.switchExamples);
//        switchSynonyms = findViewById(R.id.switchSynonyms);
//        switchAntonyms = findViewById(R.id.switchAntonyms);
//        switchAutoExpand = findViewById(R.id.switchAutoExpand);
//
//        // Permission buttons (unchanged)
//        btnAccessibility.setOnClickListener(v -> PermissionDialog.showAccessibilityExplanation(this,
//                () -> PermissionManager.openAccessibilitySettings(this)));
//
//        btnOverlay.setOnClickListener(v -> {
//            if (!PermissionManager.hasOverlayPermission(this)) {
//                PermissionDialog.showOverlayExplanation(this,
//                        () -> PermissionManager.requestOverlayPermission(this));
//            }
//        });
//
//        // Main service switch
//        switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isPressed()) {
//                prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, isChecked).apply();
//
//                if (isChecked) {
//                    startOverlayService();
//                    txtWordAssistHeading.setText("Stop Word Assist Service");
//                } else {
//                    stopOverlayService();
//                    txtWordAssistHeading.setText("Start Word Assist Service");
//                }
//            }
//        });
//
//        // Display setting switches - save to prefs
//        setupSwitch(switchPhonetic, KEY_SHOW_PHONETIC, true);
//        setupSwitch(switchExamples, KEY_SHOW_EXAMPLES, true);
//        setupSwitch(switchSynonyms, KEY_SHOW_SYNONYMS, true);
//        setupSwitch(switchAntonyms, KEY_SHOW_ANTONYMS, true);
//        setupSwitch(switchAutoExpand, KEY_AUTO_EXPAND, false);
//    }
//
//    private void setupSwitch(SwitchCompat switchView, String key, boolean defaultValue) {
//        boolean value = prefs.getBoolean(key, defaultValue);
//        switchView.setChecked(value);
//
//        switchView.setOnCheckedChangeListener((button, isChecked) -> {
//            if (button.isPressed()) {
//                prefs.edit().putBoolean(key, isChecked).apply();
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        updatePermissionUI();
//    }
//
//    private void updatePermissionUI() {
//        boolean accessibilityGranted = PermissionManager.isAccessibilityEnabled(this, WordAssistAccessibilityService.class);
//        boolean overlayGranted = Settings.canDrawOverlays(this);
//        boolean bothGranted = accessibilityGranted && overlayGranted;
//
//        // Update permission buttons (your existing logic)
//        if (accessibilityGranted) {
//            txtAccessibilityStatus.setText("App Accessibility");
//            btnAccessibility.setText("Allowed");
//            btnAccessibility.setEnabled(false);
//            btnAccessibility.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
//            btnAccessibility.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
//            btnAccessibility.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
//        } else {
//            txtAccessibilityStatus.setText("App Accessibility");
//            btnAccessibility.setText("Allow");
//            btnAccessibility.setEnabled(true);
//        }
//
//        if (overlayGranted) {
//            txtOverlayStatus.setText("Floating Overlay");
//            btnOverlay.setText("Allowed");
//            btnOverlay.setEnabled(false);
//            btnOverlay.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
//            btnOverlay.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
//            btnOverlay.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
//        } else {
//            txtOverlayStatus.setText("Floating Overlay");
//            btnOverlay.setText("Allow");
//            btnOverlay.setEnabled(true);
//        }
//
//        if (bothGranted) {
//            txtRequiredPermissionEnable.setVisibility(View.GONE);
//            switchEnable.setEnabled(true);
//            cardDisplaySettings.setVisibility(View.VISIBLE);
//
//            // Restore main switch state
//            boolean wasEnabled = prefs.getBoolean(KEY_OVERLAY_ENABLED, false);
//            switchEnable.setChecked(wasEnabled);
//            txtWordAssistHeading.setText(wasEnabled ? "Stop Word Assist Service" : "Start Word Assist Service");
//
//            // Restore display toggles
//            switchPhonetic.setChecked(prefs.getBoolean(KEY_SHOW_PHONETIC, true));
//            switchExamples.setChecked(prefs.getBoolean(KEY_SHOW_EXAMPLES, true));
//            switchSynonyms.setChecked(prefs.getBoolean(KEY_SHOW_SYNONYMS, true));
//            switchAntonyms.setChecked(prefs.getBoolean(KEY_SHOW_ANTONYMS, true));
//            switchAutoExpand.setChecked(prefs.getBoolean(KEY_AUTO_EXPAND, false));
//
//        } else {
//            txtRequiredPermissionEnable.setVisibility(View.VISIBLE);
//            switchEnable.setEnabled(false);
//            switchEnable.setChecked(false);
//            cardDisplaySettings.setVisibility(View.GONE);
//            prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, false).apply();
//        }
//    }
//
//    private void startOverlayService() {
//        Intent intent = new Intent(this, OverlayService.class);
//
//        // Pass all current settings to the overlay
//        intent.putExtra("showPhonetic", prefs.getBoolean(KEY_SHOW_PHONETIC, true));
//        intent.putExtra("showExamples", prefs.getBoolean(KEY_SHOW_EXAMPLES, true));
//        intent.putExtra("showSynonyms", prefs.getBoolean(KEY_SHOW_SYNONYMS, true));
//        intent.putExtra("showAntonyms", prefs.getBoolean(KEY_SHOW_ANTONYMS, true));
//        intent.putExtra("autoExpand", prefs.getBoolean(KEY_AUTO_EXPAND, false));
//
//        startService(intent);
//        Toast.makeText(this, "Word Assist Started", Toast.LENGTH_SHORT).show();
//    }
//
//    private void stopOverlayService() {
//        stopService(new Intent(this, OverlayService.class));
//        Toast.makeText(this, "Word Assist Stopped", Toast.LENGTH_SHORT).show();
//    }
//}

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "wordassist_prefs";
    private static final String KEY_OVERLAY_ENABLED = "overlay_enabled";

    private static final String KEY_SHOW_PHONETIC = "show_phonetic";
    private static final String KEY_SHOW_EXAMPLES = "show_examples";
    private static final String KEY_SHOW_SYNONYMS = "show_synonyms";
    private static final String KEY_SHOW_ANTONYMS = "show_antonyms";
    private static final String KEY_AUTO_EXPAND = "auto_expand";
    private static final String KEY_OVERLAY_MODE = "overlay_mode";

    // Views
    private TextView txtAccessibilityStatus, txtOverlayStatus, txtRequiredPermissionEnable, txtWordAssistHeading;
    private com.google.android.material.button.MaterialButton btnAccessibility, btnOverlay;
    private SwitchCompat switchEnable, switchMode;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Find views
        txtAccessibilityStatus = findViewById(R.id.txtAccessibilityStatus);
        txtOverlayStatus = findViewById(R.id.txtOverlayStatus);
        btnAccessibility = findViewById(R.id.btn_accessibility);
        btnOverlay = findViewById(R.id.btn_overlay);
        switchEnable = findViewById(R.id.switchEnable);
        switchMode = findViewById(R.id.switchMode);
        txtRequiredPermissionEnable = findViewById(R.id.txtRequiredPermissionEnable);
        txtWordAssistHeading = findViewById(R.id.txtWordAssistHeading);

        // Permission buttons
        btnAccessibility.setOnClickListener(v -> PermissionDialog.showAccessibilityExplanation(this,
                () -> PermissionManager.openAccessibilitySettings(this)));

        btnOverlay.setOnClickListener(v -> {
            if (!PermissionManager.hasOverlayPermission(this)) {
                PermissionDialog.showOverlayExplanation(this,
                        () -> PermissionManager.requestOverlayPermission(this));
            }
        });

        // Main Word Assist switch
        switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, isChecked).apply();
                if (isChecked) {
                    startOverlayService();
                    txtWordAssistHeading.setText("Disable Floating Word Assist");
                } else {
                    stopOverlayService();
                    txtWordAssistHeading.setText("Enable Floating Word Assist");
                }
                refreshWidgets();
            }
        });

        // Mode switch (Auto/Manual)
        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                prefs.edit().putBoolean(KEY_OVERLAY_MODE, isChecked).apply();
                
                // Update the overlay mode if Word Assist is enabled
                if (switchEnable.isChecked()) { // Only update if Word Assist is enabled
                    Intent intent = new Intent(this, OverlayService.class);
                    intent.putExtra("manualMode", isChecked);
                    startService(intent);
                    
                    String modeName = isChecked ? "Manual Search Mode" : "Auto Word Assist";
                    Toast.makeText(this, "Switched to " + modeName, Toast.LENGTH_SHORT).show();
                } else {
                    // If Word Assist is not enabled, just save the mode preference
                    // The correct mode will be used when Word Assist is enabled
                    String modeName = isChecked ? "Manual" : "Auto";
                    Toast.makeText(this, modeName + " mode selected (enable Word Assist to start)", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        updatePermissionUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, com.develop4sam.wordassist.ui.settings.Settings.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(this, com.develop4sam.wordassist.ui.history.HistoryActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePermissionUI() {
        boolean accessibilityGranted = PermissionManager.isAccessibilityEnabled(this,
                WordAssistAccessibilityService.class);
        boolean overlayGranted = Settings.canDrawOverlays(this);
        boolean bothGranted = accessibilityGranted && overlayGranted;

        // Permission button states
        if (accessibilityGranted) {
            txtAccessibilityStatus.setText("App Accessibility");
            btnAccessibility.setText("Allowed");
            btnAccessibility.setEnabled(false);
            btnAccessibility.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
            btnAccessibility.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
            btnAccessibility.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
        } else {
            txtAccessibilityStatus.setText("App Accessibility");
            btnAccessibility.setText("Allow");
            btnAccessibility.setEnabled(true);
        }

        if (overlayGranted) {
            txtOverlayStatus.setText("Floating Overlay");
            btnOverlay.setText("Allowed");
            btnOverlay.setEnabled(false);
            btnOverlay.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
            btnOverlay.setTextColor(ContextCompat.getColor(this, R.color.dark_green));
            btnOverlay.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_green)));
        } else {
            txtOverlayStatus.setText("Floating Overlay");
            btnOverlay.setText("Allow");
            btnOverlay.setEnabled(true);
        }

        if (bothGranted) {
            txtRequiredPermissionEnable.setVisibility(View.GONE);
            switchEnable.setEnabled(true);
            switchMode.setEnabled(true);

            // Restore main switch
            boolean wasEnabled = prefs.getBoolean(KEY_OVERLAY_ENABLED, false);
            switchEnable.setChecked(wasEnabled);
            txtWordAssistHeading.setText(wasEnabled ? "Disable Word Assist" : "Enable Word Assist");

            // Restore mode switch
            boolean isManualMode = prefs.getBoolean(KEY_OVERLAY_MODE, false);
            switchMode.setChecked(isManualMode);

        } else {
            txtRequiredPermissionEnable.setVisibility(View.VISIBLE);
            switchEnable.setEnabled(false);
            switchEnable.setChecked(false);
            switchMode.setEnabled(false);
            prefs.edit().putBoolean(KEY_OVERLAY_ENABLED, false).apply();
        }
    }

    private void startOverlayService() {
        Intent intent = new Intent(this, OverlayService.class);
        
        // Pass all settings including the mode
        intent.putExtra("showPhonetic", prefs.getBoolean(KEY_SHOW_PHONETIC, true));
        intent.putExtra("showExamples", prefs.getBoolean(KEY_SHOW_EXAMPLES, true));
        intent.putExtra("showSynonyms", prefs.getBoolean(KEY_SHOW_SYNONYMS, true));
        intent.putExtra("showAntonyms", prefs.getBoolean(KEY_SHOW_ANTONYMS, true));
        intent.putExtra("autoExpand", prefs.getBoolean(KEY_AUTO_EXPAND, false));
        intent.putExtra("manualMode", prefs.getBoolean(KEY_OVERLAY_MODE, false));
        
        boolean isManualMode = prefs.getBoolean(KEY_OVERLAY_MODE, false);
        String modeName = isManualMode ? "Manual Search Mode" : "Auto Word Assist";
        Toast.makeText(this, modeName + " Started", Toast.LENGTH_SHORT).show();

        startService(intent);
    }

    private void stopOverlayService() {
        // Stop the overlay service
        stopService(new Intent(this, OverlayService.class));
        Toast.makeText(this, "Word Assist Stopped", Toast.LENGTH_SHORT).show();
    }

    private void refreshWidgets() {
        Intent intent = new Intent(this, com.develop4sam.wordassist.widget.WordAssistWidgetProvider.class);
        intent.setAction(android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = android.appwidget.AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new android.content.ComponentName(getApplication(),
                        com.develop4sam.wordassist.widget.WordAssistWidgetProvider.class));
        intent.putExtra(android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}