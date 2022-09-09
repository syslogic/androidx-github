package io.syslogic.github.model;

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
    private Integer totalCount;

    @SerializedName("jobs")
    private ArrayList<WorkflowJob> jobs;

    /** Setters... */
    public void setTotalCount(@NonNull Integer value) {
        this.totalCount = value;
    }

    public void setJobs(@NonNull ArrayList<WorkflowJob> value) {
        this.jobs = value;
    }

    /** Getters... */
    @NonNull
    public Integer getId() {
        return this.totalCount;
    }

    @NonNull
    public ArrayList<WorkflowJob> getName() {
        return this.jobs;
    }
}
