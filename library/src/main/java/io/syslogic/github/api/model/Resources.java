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

    /**
     * Setter for core {@link RateLimit}.
     * @param value the rate limit.
     */
    public void setCore(@NonNull RateLimit value) {
        this.core = value;
    }

    /**
     * Setter for search {@link RateLimit}.
     * @param value the rate limit.
     */
    public void setSearch(@NonNull RateLimit value) {
        this.search = value;
    }

    /**
     * Setter for graphql {@link RateLimit}.
     * @param value the rate limit.
     */
    public void setGraphql(@NonNull RateLimit value) {
        this.graphql = value;
    }

    /**
     * Getter for core {@link RateLimit}.
     * @return the rate limit.
     */
    @NonNull
    public RateLimit getCore() {
        return this.core;
    }

    /**
     * Getter for search {@link RateLimit}.
     * @return the rate limit.
     */
    @NonNull
    public RateLimit getSearch() {
        return this.search;
    }

    /**
     * Getter for graphql {@link RateLimit}.
     * @return the rate limit.
     */
    @NonNull
    public RateLimit getGraphql() {
        return this.graphql;
    }
}
