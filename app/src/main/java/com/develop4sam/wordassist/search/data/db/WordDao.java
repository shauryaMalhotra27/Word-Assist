package com.develop4sam.wordassist.search.data.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(WordEntity word);

    @Query("SELECT * FROM words ORDER BY timestamp DESC")
    List<WordEntity> getAll();
}
