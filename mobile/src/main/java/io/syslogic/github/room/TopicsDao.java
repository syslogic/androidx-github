package io.syslogic.github.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.syslogic.github.constants.Constants;
import io.syslogic.github.model.Topic;

/**
 * {@link Topic} {@link Dao} interface
 * @author Martin Zeitler
 */
@Dao
public interface TopicsDao {

    @Query("SELECT * FROM " + Constants.TABLE_TOPICS)
    List<Topic> getItems();

    @Insert()
    Long insert(Topic item);

    @Query("DELETE FROM " + Constants.TABLE_TOPICS)
    void clear();
}
