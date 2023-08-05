package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Resources
 *
 * @author Martin Zeitler
 */
public class Resources {

    @SerializedName("core")
    private RateLimit core;

    @SerializedName("search")
    private RateLimit search;

    @SerializedName("graphql")
    private RateLimit graphql;

    public void setSearch(@NonNull RateLimit value) {
        this.search = value;
    }

    public void setCore(@NonNull RateLimit value) {
        this.core = value;
    }

    public void setGraphql(@NonNull RateLimit value) {
        this.graphql = value;
    }

    @NonNull
    public RateLimit getCore() {
        return this.core;
    }

    @NonNull
    public RateLimit getSearch() {
        return this.search;
    }

    @NonNull
    public RateLimit getGraphql() {
        return this.graphql;
    }
}
