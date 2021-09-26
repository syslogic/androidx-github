package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Model: Topic
 * @author Martin Zeitler
 */
@Entity
public class Topic {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "query_string")
    private String queryString = null;

    /** Constructor */
    public Topic() {}

    /** Constructor */
    @Ignore
    public Topic(long id, @NonNull String value) {
        this.setId(id);
        this.setQueryString(value);
    }

    public long getId() {
        return this.id;
    }

    @Nullable public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(@Nullable String value) {
        this.queryString = value;
    }

    public void setId(long value) {
        this.id = value;
    }
}
