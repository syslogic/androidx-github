package io.syslogic.github.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.Repository;

/**
 * {@link Repository} {@link Dao} interface
 * Note: {@link androidx.room.Relation} depends on {@link androidx.room.Transaction}.
 * @author Martin Zeitler
 */
@Dao
public interface RepositoriesDao {

    // @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    List<Repository> getItems();

    /* for ContentProvider */
    // @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    Cursor selectAll();

    @Insert()
    Long insert(Repository item);

    @Update()
    void update(Repository item);

    @Delete()
    void delete(Repository item);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES + " WHERE id = :itemId")
    void deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES)
    void clear();
}
