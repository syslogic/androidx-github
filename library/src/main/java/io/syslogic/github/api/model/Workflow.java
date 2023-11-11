package io.syslogic.github.api.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.syslogic.github.api.Constants;
import io.syslogic.github.api.utils.DateConverter;

/**
 * Model: Workflow
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_WORKFLOWS)
@TypeConverters(DateConverter.class)
public class Workflow extends BaseModel implements IContentProvider {

    /** ID */
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    /** RepositoryId */
    // foreign key
    @ColumnInfo(name = "repo_id")
    private long repositoryId;

    /** NodeId */
    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    /** Name */
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    /** Path */
    @ColumnInfo(name = "path")
    @SerializedName("path")
    private String path;

    /** State */
    @ColumnInfo(name = "state")
    @SerializedName("state")
    private String state;

    /** CreatedAt */
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;

    /** UpdatedAt */
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    /** Url */
    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    /** HtmlUrl */
    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    /** BadgeUrl */
    @ColumnInfo(name = "badge_url")
    @SerializedName("badge_url")
    private String badgeUrl;

    /**
     * Setter for id.
     * @param value the id of the workflow.
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Setter for repositoryId.
     * @param value the repositoryId of the workflow.
     */
    public void setRepositoryId(long value) {
        this.repositoryId = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the workflow.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for name.
     * @param value the name of the workflow.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for path.
     * @param value the path of the workflow.
     */
    public void setPath(@NonNull String value) {
        this.path = value;
    }

    /**
     * Setter for state.
     * @param value the state of the workflow.
     */
    public void setState(@NonNull String value) {
        this.state = value;
    }

    /**
     * Setter for createdAt.
     * @param value the createdAt of the workflow.
     */
    public void setCreatedAt(@NonNull Date value) {
        this.createdAt = value;
    }

    /**
     * Setter for updatedAt.
     * @param value the updatedAt of the workflow.
     */
    public void setUpdatedAt(@NonNull Date value) {
        this.updatedAt = value;
    }

    /**
     * Setter for url.
     * @param value the url of the workflow.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the workflow.
     */
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    /**
     * Setter for badgeUrl.
     * @param value the badgeUrl of the workflow.
     */
    public void setBadgeUrl(@NonNull String value) {
        this.badgeUrl = value;
    }

    /**
     * Getter for id.
     * @return the id of the workflow.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Getter for repositoryId.
     * @return the repositoryId of the workflow.
     */
    public long getRepositoryId() {
        return this.repositoryId;
    }

    /**
     * Getter for nodeId.
     * @return the nodeId of the workflow.
     */
    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for name.
     * @return the name of the workflow.
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Getter for path.
     * @return the path of the workflow.
     */
    @NonNull
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for state.
     * @return the state of the workflow.
     */
    @NonNull
    public String getState() {
        return this.state;
    }

    /**
     * Getter for createdAt.
     * @return the createdAt of the workflow.
     */
    @NonNull
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Getter for updatedAt.
     * @return the updatedAt of the workflow.
     */
    @NonNull
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Getter for url.
     * @return the url of the workflow.
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Getter for htmlUrl.
     * @return the htmlUrl of the workflow.
     */
    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Getter for badgeUrl.
     * @return the badgeUrl of the workflow.
     */
    @NonNull
    public String getBadgeUrl() {
        return this.badgeUrl;
    }

    @NonNull
    @Override
    public BaseModel fromContentValues(@NonNull ContentValues values) {
        return null;
    }

    @NonNull
    @Override
    public ContentValues toContentValues() {
        return null;
    }
}
