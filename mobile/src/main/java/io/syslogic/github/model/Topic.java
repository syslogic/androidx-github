package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Model: Topic
 * @author Martin Zeitler
 */
public class Topic {

    private long id;

    private String queryString = null;

    public Topic() {}

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
