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

    /** Setters... */
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    public void setJobs(@NonNull ArrayList<WorkflowJob> value) {
        this.jobs = value;
    }

    /** Getters... */
    @NonNull
    public ArrayList<WorkflowJob> getJobs() {
        return this.jobs;
    }

    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }
}
