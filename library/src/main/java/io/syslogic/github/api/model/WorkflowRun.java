package io.syslogic.github.api.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.syslogic.github.api.Constants;

/**
 * Model: Workflow Run
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_WORKFLOW_RUNS)
public class WorkflowRun extends BaseModel implements IContentProvider {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    // foreign key
    @ColumnInfo(name = "workflow_id")
    private long workflowId;

    public void setId(long value) {
        this.id = value;
    }
    public void setWorkflowId(long value) {
        this.workflowId = value;
    }

    public long getId() {
        return this.id;
    }
    public long getWorkflowId() {
        return this.workflowId;
    }

    @NonNull
    @Override
    public BaseModel fromContentValues(@NonNull ContentValues values) {
        return null;
    }

    @NonNull
    @Override
    public ContentValues toContentValues() {
        return null;
    }
}
