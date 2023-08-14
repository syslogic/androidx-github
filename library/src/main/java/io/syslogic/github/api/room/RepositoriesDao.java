package io.syslogic.github.api.room;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.model.Repository;

/**
 * {@link Repository} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface RepositoriesDao {

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    List<Repository> getItems();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    Cursor selectAll();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES + " WHERE id LIKE :itemId LIMIT 1")
    Cursor getCursor(@NonNull Long itemId);

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES + " WHERE id LIKE :itemId LIMIT 1")
    Repository getItem(@NonNull Long itemId);

    @NonNull
    @Insert()
    Long insert(@NonNull Repository item);

    @Update()
    int update(@NonNull Repository item);

    @Delete()
    void delete(@NonNull Repository item);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES + " WHERE id = :itemId")
    int deleteById(@NonNull Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES)
    void clear();
}
