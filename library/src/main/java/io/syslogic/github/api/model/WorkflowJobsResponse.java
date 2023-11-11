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
    private Long totalCount = 0L;

    @SerializedName("jobs")
    private ArrayList<WorkflowJob> jobs;

    /**
     * Setter for totalCount.
     * @param value the total count of workflow jobs.
     */
    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    /**
     * Setter for workflow jobs.
     * @param items the workflow jobs.
     */
    @SuppressWarnings("unused")
    public void setJobs(@NonNull ArrayList<WorkflowJob> items) {
        this.jobs = items;
    }

    /**
     * Getter for totalCount.
     * @return the total count of workflow jobs.
     */
    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }

    /**
     * Getter for workflow jobs.
     * @return the workflow jobs.
     */
    @Nullable
    public ArrayList<WorkflowJob> getJobs() {
        return this.jobs;
    }
}
