package io.syslogic.github.api.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.model.QueryString;

/**
 * {@link QueryString} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface QueryStringsDao {

    /** @return a list of {@link QueryString} records. */
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    List<QueryString> getItems();

    /** @return one {@link QueryString} record. */
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    QueryString getItem(Long itemId);

    /**
     * For ContentProvider
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    Cursor selectAll();

    /**
     * @param item the {@link QueryString} to insert.
     * @return the id of the item inserted.
     */
    @Insert()
    Long insert(QueryString item);

    /**
     * @param item the {@link QueryString} to update.
     */
    @Update()
    int update(QueryString item);

    /**
     * @param item the {@link QueryString} to delete.
     */
    @Delete()
    void delete(QueryString item);

    /**
     * Delete one {@link QueryString} record by ID.
     * @return the number of affected records.
     */
    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    int deleteById(Long itemId);

    /** Delete all {@link QueryString} records. */
    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS)
    void clear();
}
