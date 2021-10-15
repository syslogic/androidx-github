package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import io.syslogic.github.Constants;

/**
 * Model: License
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_LICENSES)
public class License {

    @Ignore
    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("key")
    private String key;

    @SerializedName("spdx_id")
    private String spdxId;

    @SerializedName("name")
    private String title;

    @SerializedName("url")
    private String licenseUrl;

    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    @NonNull
    public String getKey() {
        return this.key;
    }

    @NonNull
    public String getSpdxId() {
        return this.spdxId;
    }

    @NonNull
    public String getTitle() {
        return this.title;
    }

    @NonNull
    public String getLicenseUrl() {
        return this.licenseUrl;
    }

    public void setSpdxId(@NonNull String value) {
        this.spdxId = value;
    }

    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    public void setKey(@NonNull String value) {
        this.key = value;
    }

    public void setTitle(@NonNull String value) {
        this.title = value;
    }

    public void setLicenseUrl(@NonNull String value) {
        this.licenseUrl = value;
    }
}
