package io.syslogic.github.api.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.model.Owner;

/**
 * {@link Owner} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface OwnersDao {

    /** @return a list of {@link Owner} records. */
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    List<Owner> getItems();

    /** @return one {@link Owner} record. */
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS + " WHERE id LIKE :itemId LIMIT 1")
    Owner getItem(Long itemId);

    /**
     * For ContentProvider
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    Cursor selectAll();

    /**
     * @param item the {@link Owner} to insert.
     * @return the id of the item inserted.
     */
    @Insert()
    Long insert(Owner item);

    /**
     * @param item the {@link Owner} to update.
     */
    @Update()
    void update(Owner item);

    /**
     * @param item the {@link Owner} to delete.
     */
    @Delete()
    void delete(Owner item);

    /**
     * Delete one {@link Owner} record by ID.
     * @return the number of affected records.
     */
    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_OWNERS + " WHERE id = :itemId")
    void deleteById(Long itemId);

    /** Delete all {@link Owner} records. */
    @Query("DELETE FROM " + Constants.TABLE_OWNERS)
    void clear();
}
