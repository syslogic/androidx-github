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
import io.syslogic.github.api.model.License;

/**
 * {@link License} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface LicensesDao {

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    List<License> getItems();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    Cursor selectAll();

    @NonNull
    @Insert()
    Long insert(@NonNull License item);

    @Update()
    void update(@NonNull License item);

    @Delete()
    void delete(@NonNull License item);

    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_LICENSES + " WHERE id = :itemId")
    void deleteById(@NonNull Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_LICENSES)
    void clear();
}
