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
public class WorkflowRunsResponse {
    @SerializedName("total_count")
    private Long totalCount;
    @SerializedName("workflows")
    private ArrayList<WorkflowRun> workflowRuns;

    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }
    public void setWorkflowRuns(@NonNull ArrayList<WorkflowRun> value) {
        this.workflowRuns = value;
    }
    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }
    @Nullable
    public ArrayList<WorkflowRun> getWorkflowRuns() {
        return this.workflowRuns;
    }
}
