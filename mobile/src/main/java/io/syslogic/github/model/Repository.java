package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

/**
 * Model: Repository
 * @author Martin Zeitler
 */
public class Repository extends BaseModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    private String fileName;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("license")
    private License license;

    @SerializedName("forks_count")
    private Long forkCount = 0L;

    @SerializedName("stargazers_count")
    private Long stargazerCount = 0L;

    @SerializedName("watchers_count")
    private Long watcherCount = 0L;

    @SerializedName("subscribers_count")
    private Long subscriberCount = 0L;

    @SerializedName("network_count")
    private Long networkCount = 0L;

    public Repository(@NonNull Long id, @NonNull String name, @NonNull String url) {
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
    }

    /* Setters */
    public void setId(@NonNull Long value) {
        this.id = value;
    }

    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setFullName(@NonNull  String value) {
        this.fullName = value;
    }

    public void setFileName(@NonNull  String value) {
        this.fileName = value;
    }

    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    public void setOwner(@NonNull Owner value) {
        this.owner = value;
    }

    public void setLicense(@NonNull License value) {
        this.license = value;
    }

    public void setForkCount(@NonNull Long value) {
        this.forkCount = value;
    }

    public void setStargazerCount(@NonNull Long value) {
        this.stargazerCount = value;
    }

    public void setWatcherCount(@NonNull Long value) {
        this.watcherCount = value;
    }

    public void setSubscriberCount(@NonNull Long value) {
        this.subscriberCount = value;
    }

    public void setNetworkCount(@NonNull Long value) {
        this.networkCount = value;
    }

    /* Getters */
    @NonNull
    @Bindable
    public Long getId() {
        return this.id;
    }

    @NonNull
    @Bindable
    public String getNodeId() {
        return this.nodeId;
    }

    @NonNull
    @Bindable
    public String getName() {
        return this.name;
    }

    @NonNull
    @Bindable
    public String getFileName() {
        return this.fileName;
    }

    @NonNull
    @Bindable
    public String getFullName() {
        return this.fullName;
    }

    @NonNull
    @Bindable
    public String getUrl() {
        return this.url;
    }

    @NonNull
    @Bindable
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    @NonNull
    @Bindable
    public Owner getOwner() {
        return this.owner;
    }

    @NonNull
    @Bindable
    public License getLicense() {
        return this.license;
    }

    @NonNull
    @Bindable
    public Long getForkCount() {
        return this.forkCount;
    }

    @NonNull
    @Bindable
    public Long getStargazerCount() {
        return this.stargazerCount;
    }

    @NonNull
    @Bindable
    public Long getWatcherCount() {
        return this.watcherCount;
    }

    @NonNull
    @Bindable
    public Long getSubscriberCount() {
        return this.subscriberCount;
    }

    @NonNull
    @Bindable
    public Long getNetworkCount() {
        return this.networkCount;
    }
}
