package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Model: Workflow Runs Response
 *
 * @author Martin Zeitler
 */
public class WorkflowRuns {

    @SerializedName("workflow_runs")
    private ArrayList<WorkflowRun> workflowRuns;

    @SerializedName("total_count")
    private Long totalCount = 0L;

    /**
     * Setter for totalCount.
     * @param value the total count of workflow runs.
     */
    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    /**
     * Setter for workflow runs.
     * @param items the workflows.
     */
    public void setWorkflowRuns(@NonNull ArrayList<WorkflowRun> items) {
        this.workflowRuns = items;
    }

    /**
     * Getter for totalCount.
     * @return the total count of workflows.
     */
    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }

    /**
     * Getter for workflow runs.
     * @return the workflow runs.
     */
    @Nullable
    public ArrayList<WorkflowRun> getWorkflowRuns() {
        return this.workflowRuns;
    }
}
