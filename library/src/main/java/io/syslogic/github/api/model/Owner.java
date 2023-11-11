package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.syslogic.github.api.Constants;

/**
 * Model: Owner
 *
 * @see <a href="https://developer.github.com/v3/repos/#get">repos/#get</a>
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_OWNERS)
public class Owner {

    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @SerializedName("login")
    private String login;

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

    /**
     * Getter for login.
     * @return the login of the owner.
     */
    @NonNull
    public String getLogin() {
        return this.login;
    }

    /**
     * Getter for id.
     * @return the id of the owner.
     */
    @NonNull
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for nodeId.
     * @return the nodeId of the owner.
     */
    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for avatarUrl.
     * @return the avatarUrl of the owner.
     */
    @NonNull
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    /**
     * Getter for gravatarId.
     * @return the gravatarId of the owner.
     */
    @NonNull
    public String getGravatarId() {
        return this.gravatarId;
    }

    /**
     * Getter for url.
     * @return the url of the owner.
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Getter for htmlUrl.
     * @return the htmlUrl of the owner.
     */
    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Getter for reposUrl.
     * @return the reposUrl of the owner.
     */
    @NonNull
    public String getReposUrl() {
        return this.reposUrl;
    }

    /**
     * Getter for type.
     * @return the type of the owner.
     */
    @NonNull
    public String getType() {
        return this.type;
    }

    /**
     * Getter for siteAdmin.
     * @return is siteAdmin.
     */
    @NonNull
    public Boolean getSiteAdmin() {
        return this.siteAdmin;
    }

    /**
     * Setter for login.
     * @param value the login of the owner.
     */
    public void setLogin(@NonNull String value) {
        this.login = value;
    }

    /**
     * Setter for id.
     * @param value the id of the owner.
     */
    public void setId(@NonNull Long value) {
        this.id = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the owner.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for avatarUrl.
     * @param value the avatarUrl of the owner.
     */
    public void setAvatarUrl(@NonNull String value) {
        this.avatarUrl = value;
    }

    /**
     * Setter for gravatarId.
     * @param value the gravatarId of the owner.
     */
    public void setGravatarId(@NonNull String value) {
        this.gravatarId = value;
    }

    /**
     * Setter for url.
     * @param value the url of the owner.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the owner.
     */
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the owner.
     */
    public void setReposUrl(@NonNull String value) {
        this.reposUrl = value;
    }

    /**
     * Setter for type.
     * @param value the type of the owner.
     */
    public void setType(@NonNull String value) {
        this.type = value;
    }

    /**
     * Setter for siteAdmin.
     * @param value is siteAdmin.
     */
    public void setSiteAdmin(@NonNull Boolean value) {
        this.siteAdmin = value;
    }
}
