package com.develop4sam.wordassist.search.data.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insert(WordEntity word);

    @Query("SELECT * FROM words WHERE word = :word LIMIT 1")
    WordEntity getByWord(String word);

    @Update
    void update(WordEntity word);

    @Query("SELECT * FROM words ORDER BY timestamp DESC")
    List<WordEntity> getAll();

    @Query("DELETE FROM words WHERE id = :id")
    void deleteById(int id);
}
