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

    /**
     * Setter for sha.
     * @param value the sha of the commit.
     */
    public void setSha(@NonNull String value) {
        this.sha = value;
    }

    /**
     * Setter for url.
     * @param value the url of the commit.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Getter for sha.
     * @return the sha of the commit.
     */
    @NonNull
    public String getSha() {
        return this.sha;
    }

    /**
     * Getter for url.
     * @return the url of the commit.
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }
}
