package com.develop4sam.wordassist.search.data.db;

import androidx.room.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class WordDaoTest {

    private AppDatabase db;
    private WordDao wordDao;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(
                RuntimeEnvironment.application,
                AppDatabase.class
        ).build();
        wordDao = db.wordDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetByWord() {
        WordEntity entity = new WordEntity();
        entity.word = "test";
        entity.phonetic = "/tɛst/";
        entity.fullMeaning = "A procedure...";

        wordDao.insert(entity);

        WordEntity retrieved = wordDao.getByWord("test");
        assertNotNull(retrieved);
        assertEquals("test", retrieved.word);
        assertEquals("/tɛst/", retrieved.phonetic);
    }

    @Test
    public void getAll_returnsInDescendingOrder() {
        WordEntity e1 = new WordEntity();
        e1.word = "word1";
        e1.timestamp = 1000;

        WordEntity e2 = new WordEntity();
        e2.word = "word2";
        e2.timestamp = 2000;

        wordDao.insert(e1);
        wordDao.insert(e2);

        List<WordEntity> all = wordDao.getAll();
        assertEquals(2, all.size());
        assertEquals("word2", all.get(0).word); // Newer first
        assertEquals("word1", all.get(1).word);
    }

    @Test
    public void update_updatesEntity() {
        WordEntity entity = new WordEntity();
        entity.word = "test";
        entity.timestamp = 1000;

        wordDao.insert(entity);

        entity.timestamp = 2000;
        wordDao.update(entity);

        WordEntity updated = wordDao.getByWord("test");
        assertEquals(2000, updated.timestamp);
    }

    @Test
    public void deleteById_removesEntity() {
        WordEntity entity = new WordEntity();
        entity.word = "test";

        wordDao.insert(entity);
        int id = wordDao.getByWord("test").id;

        wordDao.deleteById(id);

        assertNull(wordDao.getByWord("test"));
    }
}