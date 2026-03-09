package com.develop4sam.wordassist.overlay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.develop4sam.wordassist.R;
import com.develop4sam.wordassist.search.data.api.ApiClient;
import com.develop4sam.wordassist.search.data.model.DictionaryResponse;
import com.develop4sam.wordassist.search.data.repository.DictionaryRepository;
import com.develop4sam.wordassist.utils.Debouncer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManualOverlayView {

    private static final String TAG = "ManualOverlayView";

    private final Context context;
    private final WindowManager windowManager;
    private final DictionaryRepository repository;

    private View rootView;
    private EditText editSearch;
    private TextView txtMeaning;
    private Button btnClose;
    private WindowManager.LayoutParams params;
    private Debouncer debouncer;

    private String themeColor = "green";

    // Display options
    private boolean showPhonetic = true;
    private boolean showExamples = true;
    private boolean showSynonyms = true;
    private boolean showAntonyms = true;

    public ManualOverlayView(Context context) {
        this.context = context.getApplicationContext();
        this.windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        this.repository = new DictionaryRepository(this.context);
        this.debouncer = new Debouncer();
    }

    @SuppressLint("InflateParams")
    public void show() {
        if (rootView != null)
            return;

        rootView = LayoutInflater.from(context).inflate(R.layout.view_manual_overlay, null);

        editSearch = rootView.findViewById(R.id.editSearch);
        txtMeaning = rootView.findViewById(R.id.txtMeaning);
        btnClose = rootView.findViewById(R.id.btnClose);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                android.graphics.PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;

        btnClose.setOnClickListener(v -> remove());

        editSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                debouncer.debounce(() -> {
                    String word = s.toString().trim();
                    if (!word.isEmpty()) {
                        searchWord(word);
                    } else {
                        txtMeaning.setText("Enter a word above to see its meaning");
                    }
                }, 500);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        windowManager.addView(rootView, params);
        rootView.setBackgroundColor(getThemeColor(themeColor));
    }

    private void searchWord(String word) {
        txtMeaning.setText("Loading definition...");

        ApiClient.getApi().getMeaning(word).enqueue(new Callback<DictionaryResponse>() {
            @Override
            public void onResponse(Call<DictionaryResponse> call, Response<DictionaryResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    txtMeaning.setText("No meaning found");
                    return;
                }
                updateMeaning(response.body());
            }

            @Override
            public void onFailure(Call<DictionaryResponse> call, Throwable t) {
                Log.e(TAG, "API error", t);
                txtMeaning.setText("Error loading meaning");
            }
        });
    }

    private void updateMeaning(DictionaryResponse data) {
        StringBuilder meaning = new StringBuilder();

        // Phonetic
        if (showPhonetic && data.phonetics != null && !data.phonetics.isEmpty()) {
            meaning.append("Phonetic: ").append(data.phonetics.get(0).text).append("\n\n");
        }

        if (data.meanings != null) {
            for (DictionaryResponse.Meaning m : data.meanings) {
                meaning.append(m.partOfSpeech).append(":\n");
                for (DictionaryResponse.Definition def : m.definitions) {
                    meaning.append("• ").append(def.definition).append("\n");
                    if (showExamples && def.example != null && !def.example.isEmpty()) {
                        meaning.append("  Example: ").append(def.example).append("\n");
                    }
                    if (showSynonyms && def.synonyms != null && !def.synonyms.isEmpty()) {
                        meaning.append("  Synonyms: ").append(String.join(", ", def.synonyms)).append("\n");
                    }
                    if (showAntonyms && def.antonyms != null && !def.antonyms.isEmpty()) {
                        meaning.append("  Antonyms: ").append(String.join(", ", def.antonyms)).append("\n");
                    }
                    meaning.append("\n");
                }
                meaning.append("\n");
            }
        }

        txtMeaning.setText(meaning.toString());

        // Save to DB
        String phonetic = data.phonetics != null && !data.phonetics.isEmpty() ? data.phonetics.get(0).text : "";
        repository.save(data.word, meaning.toString(), phonetic);
    }

    public void setThemeColor(String color) {
        this.themeColor = color;
        if (rootView != null) {
            rootView.setBackgroundColor(getThemeColor(color));
        }
    }

    private int getThemeColor(String color) {
        switch (color) {
            case "green": return Color.parseColor("#10B981");
            case "blue": return Color.parseColor("#2196F3");
            case "red": return Color.parseColor("#F44336");
            case "purple": return Color.parseColor("#9C27B0");
            case "orange": return Color.parseColor("#FF9800");
            default: return Color.parseColor("#10B981");
        }
    }

    // Setters for display options
    public void setShowPhonetic(boolean show) {
        this.showPhonetic = show;
    }

    public void setShowExamples(boolean show) {
        this.showExamples = show;
    }

    public void setShowSynonyms(boolean show) {
        this.showSynonyms = show;
    }

    public void setShowAntonyms(boolean show) {
        this.showAntonyms = show;
    }

    public void remove() {
        if (rootView != null && rootView.getParent() != null) {
            windowManager.removeView(rootView);
            rootView = null;
        }
        if (debouncer != null) {
            debouncer.shutdown();
        }
    }
}