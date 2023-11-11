package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.syslogic.github.api.Constants;

/**
 * Model: License
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_LICENSES)
public class License {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("key")
    private String key;

    @SerializedName("spdx_id")
    private String spdxId;

    @SerializedName("name")
    private String title;

    @SerializedName("url")
    private String url;

    /**
     * Getter for id.
     * @return the id of the license.
     */
    @NonNull
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for nodeId.
     * @return the nodeId of the license.
     */
    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for key.
     * @return the key of the license.
     */
    @NonNull
    public String getKey() {
        return this.key;
    }

    /**
     * Getter for spdxId.
     * @return the spdxId of the license.
     */
    @NonNull
    public String getSpdxId() {
        return this.spdxId;
    }

    /**
     * Getter for title.
     * @return the title of the license.
     */
    @NonNull
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter for url.
     * @return the url of the license.
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for id.
     * @param value the id of the license.
     */
    public void setId(@NonNull Long value) {
        this.id = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the license.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for key.
     * @param value the key of the license.
     */
    public void setKey(@NonNull String value) {
        this.key = value;
    }

    /**
     * Setter for spdxId.
     * @param value the spdxId of the license.
     */
    public void setSpdxId(@NonNull String value) {
        this.spdxId = value;
    }

    /**
     * Setter for title.
     * @param value the title of the license.
     */
    public void setTitle(@NonNull String value) {
        this.title = value;
    }

    /**
     * Setter for url.
     * @param value the url of the license.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }
}
