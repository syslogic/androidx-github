package io.syslogic.github.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.Topic;

/**
 * {@link Topic} {@link Dao} interface
 * @author Martin Zeitler
 */
@Dao
public interface TopicsDao {

    @Query("SELECT * FROM " + Constants.TABLE_TOPICS)
    List<Topic> getItems();

    @Query("SELECT * FROM " + Constants.TABLE_TOPICS + " WHERE id = :itemId")
    Topic getItem(Long itemId);

    /* for ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_TOPICS)
    Cursor selectAll();

    @Insert()
    Long insert(Topic item);

    @Update()
    void update(Topic item);

    @Query("DELETE FROM " + Constants.TABLE_TOPICS + " WHERE id = :itemId")
    void deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_TOPICS)
    void clear();
}
