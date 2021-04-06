package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * Model: User
 * @author Martin Zeitler
 */
public class User {

    @SerializedName("login")
    private String login;

    @SerializedName("id")
    private long id;

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

    @SerializedName("followers_url")
    private String followersUrl;

    @SerializedName("following_url")
    private String followingUrl;

    @SerializedName("gists_url")
    private String gistsUrl;

    @SerializedName("starred_url")
    private String starredUrl;

    @SerializedName("subscriptions_url")
    private String subscriptionsUrl;

    @SerializedName("organizations_url")
    private String organizationsUrl;

    @SerializedName("repos_url")
    private String reposUrl;

    @SerializedName("events_url")
    private String eventsUrl;

    @SerializedName("received_events_url")
    private String receiverEventsUrl;

    @SerializedName("type")
    private String type;

    @SerializedName("site_admin")
    private Boolean siteAdmin;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("blog")
    private String blog;

    @SerializedName("location")
    private String location;

    @SerializedName("email")
    private String email;

    @SerializedName("hireable")
    private Boolean hireable;

    @SerializedName("bio")
    private String bio;

    @SerializedName("public_repos")
    private Integer publicRepos;

    @SerializedName("public_gists")
    private Integer publicGists;

    @SerializedName("followers")
    private Integer followers;

    @SerializedName("following")
    private Integer following;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

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
    @NonNull
    public String getName() {
        return this.name;
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
    public void setName(@NonNull String value) {
        this.name = value;
    }

    @NonNull
    public String toString() {
        return this.login;
    }
}
