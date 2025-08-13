package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Package Version MetaData
 * @author Martin Zeitler
 */
public class MetaData extends BaseModel {

    /** The package-type. */
    @SerializedName("package_type")
    private String packageType;

    /**
     * Get the package-type.
     * @return the package-type.
     */
    public String getPackageType() {
        return this.packageType;
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public String toString() {
        return "MetaData {"+
            "packageType=\"" + this.packageType + "\"" +
        "}";
    }
}
