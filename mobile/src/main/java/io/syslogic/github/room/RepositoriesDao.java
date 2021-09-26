package io.syslogic.github.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.syslogic.github.constants.Constants;
import io.syslogic.github.model.Repository;

/**
 * {@link  Repository} {@link Dao} interface
 * @author Martin Zeitler
 */
@Dao
public interface RepositoriesDao {

    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    List<Repository> getItems();

    @Insert()
    Long insert(Repository item);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES)
    void clear();
}
