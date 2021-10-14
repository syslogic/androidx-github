package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

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
    private Long id;

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("gravatar_id")
    private String gravatarId;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

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
    public Long getId() {
        return this.id;
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
    public String getUrl() {
        return this.url;
    }

    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
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

    public void setId(@NonNull Long value) {
        this.id = value;
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

    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
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
