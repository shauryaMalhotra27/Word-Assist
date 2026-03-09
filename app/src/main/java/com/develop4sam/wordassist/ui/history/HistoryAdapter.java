package com.develop4sam.wordassist.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.develop4sam.wordassist.R;
import com.develop4sam.wordassist.search.data.db.WordEntity;

import java.util.List;
import java.util.Set;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<WordEntity> wordList;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedIds;

    public HistoryAdapter(List<WordEntity> wordList, boolean isSelectionMode, Set<Integer> selectedIds) {
        this.wordList = wordList;
        this.isSelectionMode = isSelectionMode;
        this.selectedIds = selectedIds;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.isSelectionMode = selectionMode;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WordEntity word = wordList.get(position);
        holder.txtWord.setText(word.word);
        holder.txtPhonetic.setText(word.phonetic != null ? word.phonetic : "");
        holder.txtMeaning.setText(word.fullMeaning != null ? word.fullMeaning : "");

        if (isSelectionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(selectedIds.contains(word.id));
            holder.itemView.setOnClickListener(v -> {
                if (selectedIds.contains(word.id)) {
                    selectedIds.remove(word.id);
                } else {
                    selectedIds.add(word.id);
                }
                notifyItemChanged(position);
            });
            holder.itemView.setOnLongClickListener(null);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                if (holder.txtMeaning.getVisibility() == View.VISIBLE) {
                    holder.txtMeaning.setVisibility(View.GONE);
                } else {
                    holder.txtMeaning.setVisibility(View.VISIBLE);
                }
            });
            holder.itemView.setOnLongClickListener(v -> {
                // Enter selection mode and select this item
                ((HistoryActivity) holder.itemView.getContext()).enterSelectionMode();
                selectedIds.add(word.id);
                notifyDataSetChanged();
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtWord, txtPhonetic, txtMeaning;
        CheckBox checkBox;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWord = itemView.findViewById(R.id.txtWord);
            txtPhonetic = itemView.findViewById(R.id.txtPhonetic);
            txtMeaning = itemView.findViewById(R.id.txtMeaning);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}