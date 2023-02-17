package io.syslogic.github.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.model.Owner;

/**
 * {@link Owner} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface OwnersDao {

    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    List<Owner> getItems();

    /* For ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_OWNERS)
    Cursor selectAll();

    @Insert()
    Long insert(Owner item);

    @Update()
    void update(Owner item);

    @Delete()
    void delete(Owner item);

    // @Query("DELETE FROM " + Constants.TABLE_OWNERS + " WHERE id = :itemId")
    // void deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_OWNERS)
    void clear();
}
