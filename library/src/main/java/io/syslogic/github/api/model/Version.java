package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model: Version
 * @author Martin Zeitler
 */
@SuppressWarnings("unused")
public class Version extends BaseModel {

    /** ID */
    @SerializedName("id")
    private Long id;

    /** Name */
    @SerializedName("name")
    private String name;

    /** License */
    @SerializedName("license")
    private String license;

    /** URL */
    @SerializedName("url")
    private String url;

    /** HTML URL */
    @SerializedName("html_url")
    private String htmlUrl;

    /** Package HTML URL */
    @SerializedName("package_html_url")
    private String packageHtmlUrl;

    /** Date created at */
    @SerializedName("created_at")
    private Date createdAt;

    /** Date last updated at */
    @SerializedName("updated_at")
    private Date updatedAt;

    /** Meta-data */
    @SerializedName("metadata")
    private MetaData metadata;

    /**
     * Get ID.
     * @return version ID
     */
    @NonNull
    public Long getId() {
        return this.id;
    }

    /**
     * Get Name.
     * @return version name
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Get License.
     * @return license
     */
    @NonNull
    public String getLicense() {
        return this.license;
    }

    /**
     * Get URL.
     * @return URL
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Get HTML URL.
     * @return HTML URL
     */
    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Get package HTML URL.
     * @return package HTML URL
     */
    @NonNull
    public String getPackageHtmlUrl() {
        return this.packageHtmlUrl;
    }

    /**
     * Get version created at.
     * @return version created at
     */
    @NonNull
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Get version last updated at.
     * @return version last updated at
     */
    @NonNull
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Get version meta-data.
     * @return version meta-data
     */
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
