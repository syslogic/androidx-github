package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Commit
 *
 * @author Martin Zeitler
 */
public class Commit {

    @SerializedName("sha")
    private String sha;

    @SerializedName("url")
    private String url;

    /** Setter for sha. */
    public void setSha(@NonNull String value) {
        this.sha = value;
    }

    /** Setter for url. */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /** Getter for sha. */
    @NonNull
    public String getSha() {
        return this.sha;
    }

    /** Getter for url. */
    @NonNull
    public String getUrl() {
        return this.url;
    }
}
