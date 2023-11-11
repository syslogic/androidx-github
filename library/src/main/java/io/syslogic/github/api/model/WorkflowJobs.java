package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Model: Workflow Jobs
 *
 * @see <a href="https://docs.github.com/en/rest/actions/workflow-jobs#about-the-workflow-jobs-api">Workflow jobs</a>
 * @author Martin Zeitler
 */
public class WorkflowJobs {

    @SerializedName("total_count")
    private Long totalCount = 0L;

    @SerializedName("jobs")
    private ArrayList<WorkflowJob> jobs;

    /**
     * Setter for totalCount.
     * @param value total count of jobs in the workflow
     */
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    /**
     * Setter for jobs.
     * @param value the jobs of the workflow.
     */
    public void setJobs(@NonNull ArrayList<WorkflowJob> value) {
        this.jobs = value;
    }

    /**
     * Getter for jobs.
     * @return the jobs of the workflow.
     */
    @NonNull
    public ArrayList<WorkflowJob> getJobs() {
        return this.jobs;
    }

    /**
     * Getter for totalCount.
     * @return total count of jobs in the workflow.
     */
    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }
}
