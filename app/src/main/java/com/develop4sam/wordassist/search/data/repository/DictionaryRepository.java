package com.develop4sam.wordassist.search.data.repository;


import android.content.Context;

import com.develop4sam.wordassist.search.data.db.AppDatabase;
import com.develop4sam.wordassist.search.data.db.WordEntity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryRepository {

    private final AppDatabase db;

    public DictionaryRepository(Context context) {
        db = AppDatabase.get(context);
    }

    public void save(String word, String meaning, String phonetic) {
        new Thread(() -> {
            WordEntity entity = new WordEntity();
            entity.word = word;
            entity.fullMeaning = meaning;
            entity.phonetic = phonetic;
            entity.timestamp = System.currentTimeMillis();
            db.wordDao().insert(entity);
        }).start();
    }
}