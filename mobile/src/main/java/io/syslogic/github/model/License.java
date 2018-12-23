package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class License {

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("key")
    private String key;

    @SerializedName("spdx_id")
    private String spdxId;

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;


    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }
    @NonNull
    public String getKey() {
        return this.key;
    }
    @NonNull
    public String getSpdxId() {
        return this.spdxId;
    }
    @NonNull
    public String getName() {
        return this.name;
    }
    @NonNull
    public String getUrl() {
        return this.url;
    }


    public void setSpdxId(@NonNull String value) {
        this.spdxId = value;
    }
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }
    public void setKey(@NonNull String value) {
        this.key = value;
    }
    public void setName(@NonNull String value) {
        this.name = value;
    }
    public void setUrl(@NonNull String value) {
        this.url = value;
    }
}
