package io.syslogic.github.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.QueryString;

/**
 * {@link QueryString} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface QueryStringsDao {

    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    List<QueryString> getItems();

    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    QueryString getItem(Long itemId);

    /* for ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    Cursor selectAll();

    @Insert()
    Long insert(QueryString item);

    @Update()
    void update(QueryString item);

    @Delete()
    void delete(QueryString item);

    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    void deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS)
    void clear();
}
