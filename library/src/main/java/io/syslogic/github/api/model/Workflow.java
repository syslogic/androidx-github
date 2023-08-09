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

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    // foreign key
    @ColumnInfo(name = "repo_id")
    private long repositoryId;

    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "path")
    @SerializedName("path")
    private String path;

    @ColumnInfo(name = "state")
    @SerializedName("state")
    private String state;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    @ColumnInfo(name = "badge_url")
    @SerializedName("badge_url")
    private String badgeUrl;

    public void setId(long value) {
        this.id = value;
    }
    public void setRepositoryId(long value) {
        this.repositoryId = value;
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
    public void setCreatedAt(@NonNull Date value) {
        this.createdAt = value;
    }
    public void setUpdatedAt(@NonNull Date value) {
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
    public long getRepositoryId() {
        return this.repositoryId;
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
    public Date getCreatedAt() {
        return this.createdAt;
    }
    @NonNull
    public Date getUpdatedAt() {
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
