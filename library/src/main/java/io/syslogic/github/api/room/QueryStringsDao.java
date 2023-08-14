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
import io.syslogic.github.api.model.QueryString;

/**
 * {@link QueryString} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface QueryStringsDao {

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    List<QueryString> getItems();

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    QueryString getItem(@NonNull Long itemId);

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_QUERY_STRINGS)
    Cursor selectAll();

    @NonNull
    @Insert()
    Long insert(@NonNull QueryString item);

    @Update()
    int update(@NonNull QueryString item);

    @Delete()
    void delete(@NonNull QueryString item);

    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS + " WHERE id = :itemId")
    int deleteById(@NonNull Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_QUERY_STRINGS)
    void clear();
}
