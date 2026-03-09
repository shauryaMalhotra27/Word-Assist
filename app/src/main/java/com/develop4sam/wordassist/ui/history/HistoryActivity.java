package com.develop4sam.wordassist.ui.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.develop4sam.wordassist.R;
import com.develop4sam.wordassist.search.data.db.AppDatabase;
import com.develop4sam.wordassist.search.data.db.WordEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<WordEntity> wordList;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();
    }

    private void loadHistory() {
        new LoadHistoryTask().execute();
    }

    private class LoadHistoryTask extends AsyncTask<Void, Void, List<WordEntity>> {
        @Override
        protected List<WordEntity> doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.get(HistoryActivity.this);
            return db.wordDao().getAll();
        }

        @Override
        protected void onPostExecute(List<WordEntity> result) {
            wordList = result;
            if (wordList.isEmpty()) {
                findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                findViewById(R.id.emptyView).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new HistoryAdapter(wordList, isSelectionMode, selectedIds);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isSelectionMode) {
                exitSelectionMode();
            } else {
                finish();
            }
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            if (isSelectionMode) {
                deleteSelected();
            } else {
                enterSelectionMode();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enterSelectionMode() {
        isSelectionMode = true;
        selectedIds.clear();
        invalidateOptionsMenu();
        adapter.setSelectionMode(true);
        adapter.notifyDataSetChanged();
    }

    private void exitSelectionMode() {
        isSelectionMode = false;
        selectedIds.clear();
        invalidateOptionsMenu();
        adapter.setSelectionMode(false);
        adapter.notifyDataSetChanged();
    }

    private void deleteSelected() {
        if (selectedIds.isEmpty()) return;
        new DeleteSelectedTask().execute();
    }

    private class DeleteSelectedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = AppDatabase.get(HistoryActivity.this);
            for (Integer id : selectedIds) {
                db.wordDao().deleteById(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            exitSelectionMode();
            loadHistory(); // Refresh list
        }
    }
}