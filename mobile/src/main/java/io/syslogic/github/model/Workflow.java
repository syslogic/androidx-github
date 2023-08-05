package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Workflow
 *
 * @author Martin Zeitler
 */
public class Workflow {

    @SerializedName("name")
    private String name;

    @SerializedName("jobs") // ?
    private WorkflowJobs jobs;

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setJobs(@NonNull WorkflowJobs value) {
        this.jobs = value;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public WorkflowJobs getJobs() {
        return this.jobs;
    }
}
