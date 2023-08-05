package io.syslogic.github.api.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.content.IContentProvider;
import io.syslogic.github.api.utils.StringArrayConverter;

/**
 * Model: Repository
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_REPOSITORIES)
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Repository extends BaseModel implements IContentProvider {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private Long id;

    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "full_name")
    @SerializedName("full_name")
    private String fullName;

    @Ignore
    private String fileName;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    @Ignore
    @SerializedName("owner")
    private Owner owner;

    @ColumnInfo(name = "owner_id")
    private Long ownerId = 0L;

    @Ignore
    @SerializedName("license")
    private License license;

    @SerializedName("license_id")
    @ColumnInfo(name = "license_id")
    private Long licenseId = 0L;

    @SerializedName("forks_count")
    @ColumnInfo(name = "forks")
    private Long forkCount = 0L;

    @SerializedName("stargazers_count")
    @ColumnInfo(name = "stargazers")
    private Long stargazerCount = 0L;

    @SerializedName("watchers_count")
    @ColumnInfo(name = "watchers")
    private Long watcherCount = 0L;

    @SerializedName("subscribers_count")
    @ColumnInfo(name = "subscribers")
    private Long subscriberCount = 0L;

    @SerializedName("network_count")
    @ColumnInfo(name = "network")
    private Long networkCount = 0L;

    @SerializedName("topics")
    @TypeConverters(StringArrayConverter.class)
    private String[] topics;

    /** Constructor */
    public Repository() {}

    /** Constructor */
    @Ignore
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

    public void setOwnerId(@NonNull Long value) {
        this.ownerId = value;
    }

    public void setLicense(@NonNull License value) {
        this.license = value;
    }

    public void setLicenseId(@NonNull Long value) {
        this.licenseId = value;
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

    public void setTopics(@NonNull String[] value) {
        this.topics = value;
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
    public Long getOwnerId() {
        return this.ownerId;
    }

    @NonNull
    @Bindable
    public License getLicense() {
        return this.license;
    }

    @NonNull
    public Long getLicenseId() {
        return this.licenseId;
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

    @NonNull
    @Bindable
    public String[] getTopics() {
        return this.topics;
    }

    @NonNull
    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.getId());
        values.put("node_id", this.getNodeId());
        values.put("name", this.getName());
        values.put("full_name", this.getFullName());
        values.put("url", this.getUrl());
        values.put("html_url", this.getHtmlUrl());
        return values;
    }

    @NonNull
    @Override
    public Repository fromContentValues(@NonNull ContentValues values) {
        this.setId(values.getAsLong("id"));
        this.setNodeId(values.getAsString("node_id"));
        this.setName(values.getAsString("name"));
        this.setFullName(values.getAsString("full_name"));
        this.setUrl(values.getAsString("url"));
        this.setHtmlUrl(values.getAsString("html_url"));
        return this;
    }
}
