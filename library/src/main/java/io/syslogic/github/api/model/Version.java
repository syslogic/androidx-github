package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model: Version
 * @author Martin Zeitler
 */
public class Version extends BaseModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("license")
    private String license;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("package_html_url")
    private String packageHtmlUrl;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("metadata")
    private MetaData metadata;

    @NonNull
    public Long getId() {
        return this.id;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getLicense() {
        return this.license;
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
    public String getPackageHtmlUrl() {
        return this.packageHtmlUrl;
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
    public MetaData getMetadata() {
        return this.metadata;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public String toString() {
        return "Version {"+
            "id=" + this.id + ", " +
            "name=\"" + this.name + "\", " +
            "url=\"" + this.htmlUrl + "\"" +
        "}";
    }
}
