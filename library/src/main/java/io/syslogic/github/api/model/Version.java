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
    private long id;

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

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLicense() {
        return this.license;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public String getPackageHtmlUrl() {
        return this.packageHtmlUrl;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

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
