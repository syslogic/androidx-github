package io.syslogic.github.api.room;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.model.Workflow;

/**
 * {@link Workflow} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface WorkflowsDao {

    // @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    List<Workflow> getItems();

    /* For ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    Cursor selectAll();

    /* For ContentProvider */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Cursor getCursor(Long itemId);

    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Workflow getItem(Long itemId);

    @Insert()
    Long insert(Workflow item);

    @Update()
    int update(Workflow item);

    @Delete()
    void delete(Workflow item);

    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS + " WHERE id = :itemId")
    int deleteById(Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS)
    void clear();
}
