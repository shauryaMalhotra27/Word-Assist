package com.develop4sam.wordassist.search.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String word;

    public String phonetic;        // NEW: pronunciation

    public String fullMeaning;     // RENAMED: richer formatted text

    public long timestamp;

    public WordEntity() {
        this.word = word;
        this.phonetic = phonetic;
        this.fullMeaning = fullMeaning;
        this.timestamp = System.currentTimeMillis();
    }
}