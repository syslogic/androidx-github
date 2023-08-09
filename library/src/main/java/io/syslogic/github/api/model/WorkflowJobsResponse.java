package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Model: Workflow Jobs Response
 *
 * @author Martin Zeitler
 */
public class WorkflowJobsResponse {
    @SerializedName("total_count")
    private Long totalCount;
    @SerializedName("jobs")
    private ArrayList<WorkflowJob> jobs;

    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }
    @SuppressWarnings("unused")
    public void setJobs(@NonNull ArrayList<WorkflowJob> value) {
        this.jobs = value;
    }
    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }
    @Nullable
    public ArrayList<WorkflowJob> getJobs() {
        return this.jobs;
    }
}
