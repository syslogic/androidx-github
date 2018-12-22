package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Commit {

    @SerializedName("sha")
    private String sha;

    @SerializedName("url")
    private String url;

    public void setSha(String value) {
        this.sha = value;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    @NonNull
    public String getSha() {
        return this.sha;
    }

    @NonNull
    public String getUrl() {
        return this.url;
    }
}