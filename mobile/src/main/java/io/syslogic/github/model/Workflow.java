package io.syslogic.github.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Workflow
 *
 * @author Martin Zeitler
 */
public class Workflow {

    @SerializedName("id")
    private long id;

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("name")
    private String name;

    @SerializedName("path")
    private String path;

    @SerializedName("state")
    private String state;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("badge_url")
    private String badgeUrl;

    public void setId(long value) {
        this.id = value;
    }
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }
    public void setName(@NonNull String value) {
        this.name = value;
    }
    public void setPath(@NonNull String value) {
        this.path = value;
    }
    public void setState(@NonNull String value) {
        this.state = value;
    }
    public void setCreatedAt(@NonNull String value) {
        this.createdAt = value;
    }
    public void setUpdatedAt(@NonNull String value) {
        this.updatedAt = value;
    }
    public void setUrl(@NonNull String value) {
        this.url = value;
    }
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }
    public void setBadgeUrl(@NonNull String value) {
        this.badgeUrl = value;
    }

    public long getId() {
        return this.id;
    }
    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }
    @NonNull
    public String getName() {
        return this.name;
    }
    @NonNull
    public String getPath() {
        return this.path;
    }
    @NonNull
    public String getState() {
        return this.state;
    }
    @NonNull
    public String getCreatedAt() {
        return this.createdAt;
    }
    @NonNull
    public String getUpdatedAt() {
        return this.updatedAt;
    }
    @NonNull
    public String getUrl() {
        return this.url;
    }
    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }
    @NonNull
    public String getBadgeUrl() {
        return this.badgeUrl;
    }
}
