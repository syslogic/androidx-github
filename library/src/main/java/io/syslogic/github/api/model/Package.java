package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model: Package
 * @author Martin Zeitler
 */
public class Package extends BaseModel {

    /** The package ID. */
    @SerializedName("id")
    private long id;

    /** The package-name. */
    @SerializedName("name")
    private String name;

    /** The package visibility. */
    @SerializedName("visibility")
    private String visibility;

    /** The package URL. */
    @SerializedName("url")
    private String url;

    /** The package-type. */
    @SerializedName("package_type")
    private String packageType;

    /** The version count. */
    @SerializedName("version_count")
    private int versionCount;

    /** The creation date. */
    @SerializedName("created_at")
    private Date createdAt;

    /** The last updated date. */
    @SerializedName("updated_at")
    private Date updatedAt;

    /** Nested node: the package repository. */
    @SerializedName("repository")
    private Repository repository;

    /** Nested node: the package owner. */
    @SerializedName("owner")
    private Owner owner;


    /** @return the package ID. */
    public long getId() {
        return this.id;
    }

    /** @return the package version-count. */
    public int getVersionCount() {
        return this.versionCount;
    }

    /** @return the package-name. */
    public String getName() {
        return this.name;
    }

    /** @return the package-type. */
    public String getPackageType() {
        return this.packageType;
    }

    /** @return the package visibility. */
    public String getVisibility() {
        return this.visibility;
    }

    /** @return the package url. */
    public String getUrl() {
        return this.url;
    }

    /** @return the package {@link Repository}. */
    public Repository getRepository() {
        return this.repository;
    }

    /** @return the package {@link Owner}. */
    public Owner getOwner() {
        return this.owner;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public String toString() {
        return "Package {"+
            "id=" + this.id + ", " +
            "name=\"" + this.name + "\", " +
            "packageType=\"" + this.packageType + "\", " +
            "visibility=\"" + this.visibility + "\", " +
            "versionCount=" + this.versionCount + " " +
            // "url=\"" + this.url + "\"" +
        "}";
    }
}
