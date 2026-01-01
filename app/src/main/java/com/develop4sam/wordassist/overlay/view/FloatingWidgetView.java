package com.develop4sam.wordassist.overlay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.develop4sam.wordassist.R;
import com.develop4sam.wordassist.search.data.api.ApiClient;
import com.develop4sam.wordassist.search.data.model.DictionaryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloatingWidgetView {

    private static final String TAG = "FloatingWidgetView";

    private final Context context;
    private final WindowManager windowManager;

    private View rootView;
    private LinearLayout layoutCollapsed;
    private LinearLayout layoutExpanded;
    private TextView txtWordTitle;
    private TextView txtSubtitle;
    private LinearLayout containerMeanings;
    private ImageView btnClose;
    private WindowManager.LayoutParams params;

    // Configurable display options
    private boolean showPhonetic = true;
    private boolean showPartOfSpeechInSubtitle = true;
    private boolean showExamples = true;
    private boolean showSynonyms = true;
    private boolean showAntonyms = true;
    private int maxDefinitionsPerPos = 2;
    private int maxPartsOfSpeech = 3;
    private boolean autoExpandOnWordUpdate = false;

    private DictionaryResponse lastResponse;

    public FloatingWidgetView(Context context) {
        this.context = context.getApplicationContext();
        this.windowManager = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressLint("InflateParams")
    public void show() {
        if (rootView != null)
            return;

        rootView = LayoutInflater.from(context).inflate(R.layout.view_floating_widget, null);

        layoutCollapsed = rootView.findViewById(R.id.layoutCollapsed);
        layoutExpanded = rootView.findViewById(R.id.layoutExpanded);
        txtWordTitle = rootView.findViewById(R.id.txtWordTitle);
        txtSubtitle = rootView.findViewById(R.id.txtSubtitle);
        containerMeanings = rootView.findViewById(R.id.containerMeanings);
        btnClose = rootView.findViewById(R.id.btnClose);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 300;

        enableDrag(layoutCollapsed);
        enableDrag(layoutExpanded);

        layoutCollapsed.setOnClickListener(v -> expand());
        btnClose.setOnClickListener(v -> collapse());

        windowManager.addView(rootView, params);
        collapse(); // Start collapsed
    }

    /* ---------------- STATE CONTROL ---------------- */

    private void expand() {
        layoutCollapsed.setVisibility(View.GONE);
        layoutExpanded.setVisibility(View.VISIBLE);
    }

    private void collapse() {
        layoutExpanded.setVisibility(View.GONE);
        layoutCollapsed.setVisibility(View.VISIBLE);
    }

    /* ---------------- DATA ---------------- */

    public void updateWord(String word) {
        txtWordTitle.setText(word);
        txtSubtitle.setText("");
        containerMeanings.removeAllViews();
        addMeaningText("Loading definition...");

        if (autoExpandOnWordUpdate) {
            expand(); // Only expand if enabled
        }

        fetchMeaning(word);
    }

    private void fetchMeaning(String word) {
        ApiClient.getApi().getMeaning(word).enqueue(new Callback<DictionaryResponse>() {

            @Override
            public void onResponse(Call<DictionaryResponse> call, Response<DictionaryResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    setErrorState("No meaning found");
                    return;
                }
                updateData(response.body());
            }

            @Override
            public void onFailure(Call<DictionaryResponse> call, Throwable t) {
                Log.e(TAG, "API error", t);
                setErrorState("Error loading meaning");
            }
        });
    }

    private void updateData(DictionaryResponse data) {
        this.lastResponse = data;
        txtWordTitle.setText(data.word);
        containerMeanings.removeAllViews();

        if (data.meanings == null || data.meanings.isEmpty()) {
            txtSubtitle.setText("");
            addMeaningText("No definitions available");
            return;
        }

        // Subtitle: phonetic | first part of speech
        StringBuilder subtitle = new StringBuilder();
        String phonetic = "";
        String firstPos = data.meanings.get(0).partOfSpeech;

        if (showPhonetic && data.phonetics != null && !data.phonetics.isEmpty()) {
            for (DictionaryResponse.Phonetic p : data.phonetics) {
                if (p.text != null && !p.text.isEmpty()) {
                    phonetic = p.text.trim();
                    break;
                }
            }
        }

        if (!phonetic.isEmpty()) {
            subtitle.append(phonetic);
        }

        txtSubtitle.setText(subtitle.toString());

        // Populate definitions
        int posCount = 0;
        for (DictionaryResponse.Meaning meaning : data.meanings) {
            if (posCount >= maxPartsOfSpeech)
                break;
            posCount++;

            TextView posView = createTextView(meaning.partOfSpeech, 14f, "#FFFFFF", true);
            posView.setPadding(0, 20, 0, 8);
            containerMeanings.addView(posView);

            int defCount = 0;
            for (DictionaryResponse.Definition def : meaning.definitions) {
                if (defCount >= maxDefinitionsPerPos)
                    break;
                defCount++;

                containerMeanings.addView(createTextView(
                        "• " + def.definition.trim(), 14f, "#E6FFFFFF"));

                if (showExamples && def.example != null && !def.example.isEmpty()) {
                    containerMeanings.addView(createTextView(
                            "   ↳ " + def.example.trim(),
                            13f, "#B0FFFFFF", false, true));
                }

                if (showSynonyms && def.synonyms != null && !def.synonyms.isEmpty()) {
                    LinearLayout synLayout = new LinearLayout(context);
                    synLayout.setOrientation(LinearLayout.HORIZONTAL);
                    synLayout.addView(createTextView("   Synonyms: ", 12f, "#DDDDDD"));
                    synLayout.addView(createTextView(String.join(", ", def.synonyms),
                            12f, "#FFFFFF", true));
                    containerMeanings.addView(synLayout);
                }

                if (showAntonyms && def.antonyms != null && !def.antonyms.isEmpty()
                        && !"none".equalsIgnoreCase(def.antonyms.get(0).trim())) {
                    LinearLayout antLayout = new LinearLayout(context);
                    antLayout.setOrientation(LinearLayout.HORIZONTAL);
                    antLayout.addView(createTextView("   Antonyms: ", 12f, "#DDDDDD"));
                    antLayout.addView(createTextView(String.join(", ", def.antonyms),
                            12f, "#FFFFFF", true));
                    containerMeanings.addView(antLayout);
                }

                View spacer = new View(context);
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 16));
                containerMeanings.addView(spacer);
            }
        }
    }

    private void setErrorState(String message) {
        txtSubtitle.setText("");
        containerMeanings.removeAllViews();
        addMeaningText(message);
    }

    private void addMeaningText(String text) {
        containerMeanings.addView(createTextView(text, 16f, "#E6FFFFFF"));
    }

    private TextView createTextView(String text, float textSize, String colorHex) {
        return createTextView(text, textSize, colorHex, false, false);
    }

    private TextView createTextView(String text, float textSize, String colorHex, boolean bold) {
        return createTextView(text, textSize, colorHex, bold, false);
    }

    private TextView createTextView(String text, float textSize, String colorHex, boolean bold, boolean italic) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(textSize);
        tv.setTextColor(android.graphics.Color.parseColor(colorHex));
        if (bold)
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        if (italic)
            tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
        tv.setLineSpacing(6f, 1f);
        return tv;
    }

    /* ---------------- CONFIGURATION SETTERS ---------------- */

    public void setShowPhonetic(boolean show) {
        this.showPhonetic = show;
        refresh();
    }

    public void setShowPartOfSpeechInSubtitle(boolean show) {
        this.showPartOfSpeechInSubtitle = show;
        refresh();
    }

    public void setShowExamples(boolean show) {
        this.showExamples = show;
        refresh();
    }

    public void setShowSynonyms(boolean show) {
        this.showSynonyms = show;
        refresh();
    }

    public void setShowAntonyms(boolean show) {
        this.showAntonyms = show;
        refresh();
    }

    public void setMaxDefinitionsPerPos(int max) {
        this.maxDefinitionsPerPos = max;
        refresh();
    }

    public void setMaxPartsOfSpeech(int max) {
        this.maxPartsOfSpeech = max;
        refresh();
    }

    public void setAutoExpandOnWordUpdate(boolean autoExpand) {
        this.autoExpandOnWordUpdate = autoExpand;
    }

    private void refresh() {
        if (lastResponse != null) {
            updateData(lastResponse);
        }
    }

    /* ---------------- DRAG ---------------- */

    private void enableDrag(View dragView) {
        dragView.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;
            float touchX, touchY;
            boolean isDragging;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = params.x;
                        startY = params.y;
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                        isDragging = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - touchX);
                        int dy = (int) (event.getRawY() - touchY);

                        if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                            isDragging = true;
                            params.x = startX + dx;
                            params.y = startY + dy;
                            windowManager.updateViewLayout(rootView, params);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isDragging) {
                            v.performClick();
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void remove() {
        if (rootView != null && rootView.getParent() != null) {
            windowManager.removeView(rootView);
            rootView = null;
        }
    }
}