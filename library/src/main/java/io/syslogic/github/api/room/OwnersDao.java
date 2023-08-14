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
import io.syslogic.github.api.model.Owner;

/**
 * {@link Owner} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface OwnersDao {

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    List<Owner> getItems();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    Cursor selectAll();

    @NonNull
    @Insert()
    Long insert(@NonNull Owner item);

    @Update()
    void update(@NonNull Owner item);

    @Delete()
    void delete(@NonNull Owner item);

    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_OWNERS + " WHERE id = :itemId")
    void deleteById(@NonNull Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_OWNERS)
    void clear();
}
