package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

public class Repository implements Observable {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("forks_count")
    private long forkCount;

    @SerializedName("stargazers_count")
    private long stargazerCount;

    @SerializedName("watchers_count")
    private long watcherCount;

    @SerializedName("subscribers_count")
    private long subscriberCount;

    @SerializedName("network_count")
    private long networkCount;

    public Repository(long id, String name, String url) {
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
    }

    /* Setters */
    public void setId(long value) {
        this.id = value;
    }

    public void setName(String value) {
        this.name = value;
    }

    public void setFullName(String value) {
        this.fullName = value;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public void setHtmlUrl(String value) {
        this.htmlUrl = value;
    }

    public void setForkCount(long value) {
        this.forkCount = value;
    }

    public void setStargazerCount(long value) {
        this.stargazerCount = value;
    }

    public void setWatcherCount(long value) {
        this.watcherCount = value;
    }

    public void setSubscriberCount(long value) {
        this.subscriberCount = value;
    }

    public void setNetworkCount(long value) {
        this.networkCount = value;
    }

    /* Getters */
    @Bindable
    public long getId() {
        return this.id;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    @Bindable
    public String getFullName() {
        return this.fullName;
    }

    @Bindable
    public String getUrl() {
        return this.url;
    }

    @Bindable
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    @Bindable
    public long getForkCount() {
        return this.forkCount;
    }

    @Bindable
    public long getStargazerCount() {
        return this.stargazerCount;
    }

    @Bindable
    public long getWatcherCount() {
        return this.watcherCount;
    }

    @Bindable
    public long getSubscriberCount() {
        return this.subscriberCount;
    }

    @Bindable
    public long getNetworkCount() {
        return this.networkCount;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }
}
