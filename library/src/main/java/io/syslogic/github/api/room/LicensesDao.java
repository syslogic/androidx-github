package io.syslogic.github.api.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.model.License;

/**
 * {@link License} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface LicensesDao {

    /** @return a list of {@link License} records. */
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    List<License> getItems();

    /** @return one {@link License} record. */
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES + " WHERE id LIKE :itemId LIMIT 1")
    License getItem(Long itemId);

    /**
     * For ContentProvider
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    Cursor selectAll();

    /**
     * @param item the {@link License} to insert.
     * @return the id of the item inserted.
     */
    @Insert()
    Long insert(License item);

    /**
     * @param item the {@link License} to update.
     */
    @Update()
    void update(License item);

    /**
     * @param item the {@link License} to delete.
     */
    @Delete()
    void delete(License item);

    /**
     * Delete one {@link License} record by ID.
     * @return the number of affected records.
     */
    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_LICENSES + " WHERE id = :itemId")
    int deleteById(Long itemId);

    /** Delete all {@link License} records. */
    @Query("DELETE FROM " + Constants.TABLE_LICENSES)
    void clear();
}
