package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import io.syslogic.github.Constants;

/**
 * Model: Owner
 * @see <a href="https://developer.github.com/v3/repos/#get">repos/#get</>
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_OWNERS)
public class Owner {

    @SerializedName("login")
    private String login;

    @SerializedName("id")
    private Long ownerId;

    @Ignore
    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("gravatar_id")
    private String gravatarId;

    @SerializedName("url")
    private String ownerUrl;

    @SerializedName("html_url")
    private String ownerHtmlUrl;

    @SerializedName("repos_url")
    private String reposUrl;

    @SerializedName("type")
    private String type;

    @SerializedName("site_admin")
    private Boolean siteAdmin;

    /* Getters */
    @NonNull
    public String getLogin() {
        return this.login;
    }

    @NonNull
    public Long getOwnerId() {
        return this.ownerId;
    }

    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    @NonNull
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @NonNull
    public String getGravatarId() {
        return this.gravatarId;
    }

    @NonNull
    public String getOwnerUrl() {
        return this.ownerUrl;
    }

    @NonNull
    public String getOwnerHtmlUrl() {
        return this.ownerHtmlUrl;
    }

    @NonNull
    public String getReposUrl() {
        return this.reposUrl;
    }

    @NonNull
    public String getType() {
        return this.type;
    }

    @NonNull
    public Boolean getSiteAdmin() {
        return this.siteAdmin;
    }

    /* Setters */
    public void setLogin(@NonNull String value) {
        this.login = value;
    }

    public void setOwnerId(@NonNull Long value) {
        this.ownerId = value;
    }

    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    public void setAvatarUrl(@NonNull String value) {
        this.avatarUrl = value;
    }

    public void setGravatarId(@NonNull String value) {
        this.gravatarId = value;
    }

    public void setOwnerUrl(@NonNull String value) {
        this.ownerUrl= value;
    }

    public void setOwnerHtmlUrl(@NonNull String value) {
        this.ownerHtmlUrl = value;
    }

    public void setReposUrl(@NonNull String value) {
        this.reposUrl = value;
    }

    public void setType(@NonNull String value) {
        this.type = value;
    }

    public void setSiteAdmin(@NonNull Boolean value) {
        this.siteAdmin = value;
    }
}
