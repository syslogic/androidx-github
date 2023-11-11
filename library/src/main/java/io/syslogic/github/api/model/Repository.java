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

import java.util.ArrayList;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.utils.StringArrayConverter;

/**
 * Model: Repository
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_REPOSITORIES)
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Repository extends BaseModel implements IContentProvider {

    /** ID */
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private Long id;

    /** NodeId */
    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    /** Name */
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    /** Full Name */
    @ColumnInfo(name = "full_name")
    @SerializedName("full_name")
    private String fullName;

    /** File-Name */
    @Ignore
    private String fileName;

    /** Url */
    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    /** HtmlUrl */
    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    /** Owner */
    @Ignore
    @SerializedName("owner")
    private Owner owner;

    /** OwnerId */
    @ColumnInfo(name = "owner_id")
    private Long ownerId = 0L;

    /** License */
    @Ignore
    @SerializedName("license")
    private License license;

    /** LicenseId */
    @SerializedName("license_id")
    @ColumnInfo(name = "license_id")
    private Long licenseId = 0L;

    /** Forks */
    @SerializedName("forks_count")
    @ColumnInfo(name = "forks")
    private Long forkCount = 0L;

    /** Stargazers */
    @SerializedName("stargazers_count")
    @ColumnInfo(name = "stargazers")
    private Long stargazerCount = 0L;

    /** Watchers */
    @SerializedName("watchers_count")
    @ColumnInfo(name = "watchers")
    private Long watcherCount = 0L;

    /** Subscribers */
    @SerializedName("subscribers_count")
    @ColumnInfo(name = "subscribers")
    private Long subscriberCount = 0L;

    /** Network */
    @SerializedName("network_count")
    @ColumnInfo(name = "network")
    private Long networkCount = 0L;

    /** Topics */
    @SerializedName("topics")
    @TypeConverters(StringArrayConverter.class)
    private String[] topics;

    /** Workflows are being populated asynchronously. */
    @Ignore
    private ArrayList<Workflow> workflows = new ArrayList<>();

    /** Constructor */
    public Repository() {}

    /**
     * Constructor
     * @param id the id of the repository.
     * @param name the title of the repository.
     * @param url the URL of the repository.
     */
    @Ignore
    public Repository(@NonNull Long id, @NonNull String name, @NonNull String url) {
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
    }

    /**
     * Setter for id.
     * @param value the id of the repository.
     */
    public void setId(@NonNull Long value) {
        this.id = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the repository.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for name.
     * @param value the name of the repository.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for fullName.
     * @param value the fullName of the repository.
     */
    public void setFullName(@NonNull  String value) {
        this.fullName = value;
    }

    /**
     * Setter for fileName.
     * @param value the file-name of the repository.
     */
    public void setFileName(@NonNull  String value) {
        this.fileName = value;
    }

    /**
     * Setter for url.
     * @param value the url of the repository.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the repository.
     */
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    /**
     * Setter for owner.
     * @param value the owner of the repository.
     */
    public void setOwner(@NonNull Owner value) {
        this.owner = value;
    }

    /**
     * Setter for ownerId.
     * @param value the ownerId of the repository.
     */
    public void setOwnerId(@NonNull Long value) {
        this.ownerId = value;
    }

    /**
     * Setter for license.
     * @param value the license of the repository.
     */
    public void setLicense(@NonNull License value) {
        this.license = value;
    }

    /**
     * Setter for licenseId.
     * @param value the licenseId of the repository.
     */
    public void setLicenseId(@NonNull Long value) {
        this.licenseId = value;
    }

    /**
     * Setter for forkCount.
     * @param value the fork-count of the repository.
     */
    public void setForkCount(@NonNull Long value) {
        this.forkCount = value;
    }

    /**
     * Setter for stargazerCount.
     * @param value the stargazer-count of the repository.
     */
    public void setStargazerCount(@NonNull Long value) {
        this.stargazerCount = value;
    }

    /**
     * Setter for watcherCount.
     * @param value the watcher-count of the repository.
     */
    public void setWatcherCount(@NonNull Long value) {
        this.watcherCount = value;
    }

    /**
     * Setter for subscriberCount.
     * @param value the subscriber-count of the repository.
     */
    public void setSubscriberCount(@NonNull Long value) {
        this.subscriberCount = value;
    }

    /**
     * Setter for networkCount.
     * @param value the network-count of the repository.
     */
    public void setNetworkCount(@NonNull Long value) {
        this.networkCount = value;
    }

    /**
     * Setter for topics.
     * @param value the topics of the repository.
     */
    public void setTopics(@NonNull String[] value) {
        this.topics = value;
    }

    /**
     * Setter for workflows.
     * @param value the workflows of the repository.
     */
    public void setWorkflows(@NonNull ArrayList<Workflow> value) {
        this.workflows = value;
    }

    /**
     * Getter for id.
     * @return the ID of the repository.
     */
    @NonNull
    @Bindable
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for nodeId.
     * @return the nodeId of the repository.
     */
    @NonNull
    @Bindable
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for name.
     * @return the name of the repository.
     */
    @NonNull
    @Bindable
    public String getName() {
        return this.name;
    }

    /**
     * Getter for fileName.
     * @return the file-name of the repository.
     */
    @NonNull
    @Bindable
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Getter for fullName.
     * @return the full name of the repository.
     */
    @NonNull
    @Bindable
    public String getFullName() {
        return this.fullName;
    }

    /**
     * Getter for url.
     * @return the url of the repository.
     */
    @NonNull
    @Bindable
    public String getUrl() {
        return this.url;
    }

    /**
     * Getter for htmlUrl.
     * @return the htmlUrl of the repository.
     */
    @NonNull
    @Bindable
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Getter for owner.
     * @return the owner of the repository.
     */
    @NonNull
    @Bindable
    public Owner getOwner() {
        return this.owner;
    }

    /**
     * Getter for ownerId.
     * @return the ownerId of the repository.
     */
    @NonNull
    public Long getOwnerId() {
        return this.ownerId;
    }

    /**
     * Getter for license.
     * @return the license of the repository.
     */
    @NonNull
    @Bindable
    public License getLicense() {
        return this.license;
    }

    /**
     * Getter for licenseId.
     * @return the licenseId of the repository.
     */
    @NonNull
    public Long getLicenseId() {
        return this.licenseId;
    }

    /**
     * Getter for forkCount.
     * @return the fork-count of the repository.
     */
    @NonNull
    @Bindable
    public Long getForkCount() {
        return this.forkCount;
    }

    /**
     * Getter for forkCount.
     * @return the fork-count of the repository.
     */
    @NonNull
    @Bindable
    public Long getStargazerCount() {
        return this.stargazerCount;
    }

    /**
     * Getter for forkCount.
     * @return the fork-count of the repository.
     */
    @NonNull
    @Bindable
    public Long getWatcherCount() {
        return this.watcherCount;
    }

    /**
     * Getter for forkCount.
     * @return the fork-count of the repository.
     */
    @NonNull
    @Bindable
    public Long getSubscriberCount() {
        return this.subscriberCount;
    }

    /**
     * Getter for forkCount.
     * @return the fork-count of the repository.
     */
    @NonNull
    @Bindable
    public Long getNetworkCount() {
        return this.networkCount;
    }

    /**
     * Getter for topics.
     * @return the topics of the repository.
     */
    @NonNull
    @Bindable
    public String[] getTopics() {
        return this.topics;
    }

    /**
     * Getter for workflows.
     * @return the workflows of the repository.
     */
    @NonNull
    public ArrayList<Workflow> getWorkflows() {
        return this.workflows;
    }

    /**
     * Getter for workflowCount.
     * @return the total count of workflows in the repository.
     */
    @Bindable
    public int getWorkflowCount() {
        return this.workflows.size();
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
