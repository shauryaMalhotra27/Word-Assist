package com.develop4sam.wordassist.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.develop4sam.wordassist.R;

public class Settings extends AppCompatActivity {

    private static final String PREF_NAME = "wordassist_prefs";
    private static final String KEY_SHOW_PHONETIC = "show_phonetic";
    private static final String KEY_SHOW_EXAMPLES = "show_examples";
    private static final String KEY_SHOW_SYNONYMS = "show_synonyms";
    private static final String KEY_SHOW_ANTONYMS = "show_antonyms";
    private static final String KEY_AUTO_EXPAND = "auto_expand";
    private static final String KEY_OVERLAY_MODE = "overlay_mode";

    private SharedPreferences prefs;
    private SwitchCompat switchPhonetic, switchExamples, switchSynonyms, switchAntonyms, switchAutoExpand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Find switches
        switchPhonetic = findViewById(R.id.switchPhonetic);
        switchExamples = findViewById(R.id.switchExamples);
        switchSynonyms = findViewById(R.id.switchSynonyms);
        switchAntonyms = findViewById(R.id.switchAntonyms);
        switchAutoExpand = findViewById(R.id.switchAutoExpand);

        // Set up listeners and initial states
        setupSwitch(switchPhonetic, KEY_SHOW_PHONETIC);
        setupSwitch(switchExamples, KEY_SHOW_EXAMPLES);
        setupSwitch(switchSynonyms, KEY_SHOW_SYNONYMS);
        setupSwitch(switchAntonyms, KEY_SHOW_ANTONYMS);
        setupSwitch(switchAutoExpand, KEY_AUTO_EXPAND);
    }

    private void setupSwitch(SwitchCompat switchView, String key) {
        // Restore saved value
        boolean value = prefs.getBoolean(key, getDefaultForKey(key));
        switchView.setChecked(value);

        // Set listener
        switchView.setOnCheckedChangeListener((button, isChecked) -> {
            if (button.isPressed()) {
                prefs.edit().putBoolean(key, isChecked).apply();
                // Update running services with new settings
                updateRunningServices();
            }
        });
    }

    private void updateRunningServices() {
        // Update the unified overlay service with new settings
        Intent intent = new Intent(this, com.develop4sam.wordassist.overlay.service.OverlayService.class);
        intent.putExtra("showPhonetic", prefs.getBoolean(KEY_SHOW_PHONETIC, true));
        intent.putExtra("showExamples", prefs.getBoolean(KEY_SHOW_EXAMPLES, true));
        intent.putExtra("showSynonyms", prefs.getBoolean(KEY_SHOW_SYNONYMS, true));
        intent.putExtra("showAntonyms", prefs.getBoolean(KEY_SHOW_ANTONYMS, true));
        intent.putExtra("autoExpand", prefs.getBoolean(KEY_AUTO_EXPAND, false));
        // Keep the current mode
        intent.putExtra("manualMode", prefs.getBoolean(KEY_OVERLAY_MODE, false));
        startService(intent);
    }

    private boolean getDefaultForKey(String key) {
        switch (key) {
            case KEY_SHOW_PHONETIC:
            case KEY_SHOW_EXAMPLES:
            case KEY_SHOW_SYNONYMS:
            case KEY_SHOW_ANTONYMS:
                return true;
            case KEY_AUTO_EXPAND:
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}