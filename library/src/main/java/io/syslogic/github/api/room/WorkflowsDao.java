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

    /** @return a list of {@link Workflow} records. */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    List<Workflow> getItems();

    /**
     * @param itemId the id of the {@link Workflow}.
     * @return one {@link Workflow} record.
     */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Workflow getItem(Long itemId);

    /**
     * For ContentProvider
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    Cursor selectAll();

    /**
     * For ContentProvider
     * @param itemId the id of the record.
     * @return a cursor.
     */
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Cursor getCursor(Long itemId);

    /**
     * @param item the {@link Workflow} to insert.
     * @return the id of the item inserted.
     */
    @Insert()
    Long insert(Workflow item);

    /**
     * @param item the {@link Workflow} to update.
     * @return the number of affected records.
     */
    @Update()
    int update(Workflow item);

    /**
     * @param item the {@link Workflow} to delete.
     * @return the number of affected records.
     */
    @Delete()
    int delete(Workflow item);

    /**
     * Delete one {@link Workflow} record by ID.
     * @param itemId the id of the {@link Workflow}.
     * @return the number of affected records.
     */
    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS + " WHERE id = :itemId")
    int deleteById(Long itemId);

    /** Delete all {@link Workflow} records. */
    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS)
    void clear();
}
