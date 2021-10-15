package io.syslogic.github.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.License;

/**
 * {@link License} {@link Dao} interface
 * @author Martin Zeitler
 */
@Dao
public interface LicensesDao {

    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    List<License> getItems();

    /* for ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_LICENSES)
    Cursor selectAll();

    @Insert()
    Long insert(License item);

    @Update()
    void update(License item);

    @Delete()
    void delete(License item);

    // @Query("DELETE FROM " + Constants.TABLE_LICENSES + " WHERE id = :itemId")
    // void deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_LICENSES)
    void clear();
}
