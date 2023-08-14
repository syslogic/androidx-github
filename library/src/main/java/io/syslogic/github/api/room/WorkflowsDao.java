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
import io.syslogic.github.api.model.Workflow;

/**
 * {@link Workflow} {@link Dao} interface
 *
 * @author Martin Zeitler
 */
@Dao
public interface WorkflowsDao {

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    List<Workflow> getItems();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS)
    Cursor selectAll();

    /* For ContentProvider */
    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Cursor getCursor(@NonNull Long itemId);

    @NonNull
    @Query("SELECT * FROM " + Constants.TABLE_WORKFLOWS + " WHERE id LIKE :itemId LIMIT 1")
    Workflow getItem(@NonNull Long itemId);

    @NonNull
    @Insert()
    Long insert(@NonNull Workflow item);

    @Update()
    int update(@NonNull Workflow item);

    @Delete()
    void delete(@NonNull Workflow item);

    @SuppressWarnings("unused")
    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS + " WHERE id = :itemId")
    int deleteById(@NonNull Long itemId);

    @Query("DELETE FROM " + Constants.TABLE_WORKFLOWS)
    void clear();
}
