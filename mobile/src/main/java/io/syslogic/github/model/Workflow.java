package io.syslogic.github.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Workflow
 *
 * @author Martin Zeitler
 */
public class Workflow {

    @SerializedName("name")
    private String name;

    public void setName(@NonNull String value) {
        this.name = value;
    }

    @NonNull
    public String getName() {
        return this.name;
    }
}
