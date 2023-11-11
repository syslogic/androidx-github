package io.syslogic.github.api.room;

import android.database.Cursor;

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

    /** @return a list of {@link Repository} records. */
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    List<Repository> getItems();

    /**
     * @param itemId the id of the {@link Repository}.
     * @return one {@link Repository} record.
     */
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES + " WHERE id LIKE :itemId LIMIT 1")
    Repository getItem(Long itemId);

    /**
     * For ContentProvider
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES)
    Cursor selectAll();

    /**
     * For ContentProvider
     * @param itemId the id of the record.
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_REPOSITORIES + " WHERE id LIKE :itemId LIMIT 1")
    Cursor getCursor(Long itemId);

    /**
     * @param item the {@link Repository} to insert.
     * @return the id of the item inserted.
     */
    @Insert()
    Long insert(Repository item);

    /**
     * @param item the {@link Repository} to update.
     * @return the number of affected records.
     */
    @Update()
    int update(Repository item);

    /**
     * @param item the {@link Repository} to delete.
     */
    @Delete()
    void delete(Repository item);

    /**
     * Delete one {@link Repository} record by ID.
     * @param itemId the id of the {@link Repository}.
     * @return the number of affected records.
     */
    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES + " WHERE id = :itemId")
    int deleteById(Long itemId);

    /** Delete all {@link Repository} records. */
    @Query("DELETE FROM " + Constants.TABLE_REPOSITORIES)
    void clear();
}
