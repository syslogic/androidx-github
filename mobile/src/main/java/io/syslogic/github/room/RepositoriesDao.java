package io.syslogic.github.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.Repository;

/**
 * {@link Repository} {@link Dao} interface
 * Note: currently not in use.
 * @author Martin Zeitler
 */
@Dao
public interface RepositoriesDao {

    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    List<Repository> getItems();

    @Insert()
    Long insert(Repository item);

    @Update()
    void update(Repository item);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES)
    void clear();
}
