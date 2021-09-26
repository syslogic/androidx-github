package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import io.syslogic.github.constants.Constants;

/**
 * Model: Topic
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_TOPICS)
public class Topic extends BaseModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title = null;

    @ColumnInfo(name = "query_string")
    private String queryString = null;

    /** Constructor */
    public Topic() {}

    /** Constructor */
    @Ignore
    public Topic(@NonNull Long id, @NonNull String title, @NonNull String queryString) {
        this.setId(id);
        this.setTitle(title);
        this.setQueryString(queryString);
    }

    @Bindable
    public long getId() {
        return this.id;
    }

    @Bindable
    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Bindable
    @Nullable
    public String getQueryString() {
        return this.queryString;
    }

    @Bindable
    public void setId(long value) {
        this.id = value;
    }

    @Bindable
    public void setTitle(@Nullable String value) {
        this.title = value;
    }

    @Bindable
    public void setQueryString(@Nullable String value) {
        this.queryString = value;
    }
}
