package io.syslogic.github.room;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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
    @NonNull List<Topic> getItems();

    @Query("SELECT id, title, query_string FROM " + Constants.TABLE_TOPICS)
    @NonNull List<SpinnerItem> getSpinnerItems();

    @Insert()
    @NonNull Long insert(@NonNull Topic item);

    @Query("DELETE FROM " + Constants.TABLE_TOPICS)
    void clear();
}
