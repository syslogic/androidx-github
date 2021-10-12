package io.syslogic.github.room;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.constants.Constants;
import io.syslogic.github.model.SpinnerItem;
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

    @Query("SELECT id, title AS name, query_string AS value FROM " + Constants.TABLE_TOPICS)
    List<SpinnerItem> getSpinnerItems();

    @Insert()
    Long insert(Topic item);

    @Update()
    void update(Topic item);

    @Query("DELETE FROM " + Constants.TABLE_TOPICS)
    void clear();
}
