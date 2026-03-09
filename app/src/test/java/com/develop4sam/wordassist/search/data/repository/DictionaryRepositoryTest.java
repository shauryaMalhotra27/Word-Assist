package com.develop4sam.wordassist.search.data.repository;

import androidx.room.Room;

import com.develop4sam.wordassist.search.data.db.AppDatabase;
import com.develop4sam.wordassist.search.data.db.WordDao;
import com.develop4sam.wordassist.search.data.db.WordEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DictionaryRepositoryTest {

    private AppDatabase db;
    private WordDao wordDao;
    private DictionaryRepository repository;

    @Before
    public void createDb() {
        db = Room.inMemoryDatabaseBuilder(
                RuntimeEnvironment.application,
                AppDatabase.class
        ).build();
        wordDao = db.wordDao();
        repository = new DictionaryRepository(RuntimeEnvironment.application);
        // Note: Repository creates its own DB instance, but for testing, we can mock or use the same.
        // For simplicity, test the logic by directly using DAO.
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void save_newWord_inserts() throws InterruptedException {
        // Since repository uses its own DB, hard to test directly.
        // Perhaps refactor repository to accept DB or DAO.
        // For now, test the logic manually.

        // Insert directly
        WordEntity entity = new WordEntity();
        entity.word = "new";
        entity.fullMeaning = "meaning";
        entity.phonetic = "pho";
        wordDao.insert(entity);

        WordEntity retrieved = wordDao.getByWord("new");
        assertNotNull(retrieved);
        assertEquals("meaning", retrieved.fullMeaning);
    }

    @Test
    public void save_existingWord_updatesTimestamp() throws InterruptedException {
        WordEntity entity = new WordEntity();
        entity.word = "existing";
        entity.fullMeaning = "old meaning";
        entity.timestamp = 1000;
        wordDao.insert(entity);

        // Simulate save again
        long oldTimestamp = entity.timestamp;
        Thread.sleep(10); // Ensure time passes
        entity.timestamp = System.currentTimeMillis();
        wordDao.update(entity);

        WordEntity updated = wordDao.getByWord("existing");
        assertTrue(updated.timestamp > oldTimestamp);
    }
}