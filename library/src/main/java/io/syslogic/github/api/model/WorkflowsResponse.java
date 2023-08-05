package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Model: Workflows Response
 *
 * @author Martin Zeitler
 */
public class WorkflowsResponse {

    @SerializedName("total_count")
    private Long totalCount;

    @SerializedName("workflows")
    private ArrayList<Workflow> workflows;

    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    public void setWorkflows(@NonNull ArrayList<Workflow> value) {
        this.workflows = value;
    }

    @NonNull
    public Long getTotalCount() {
        return this.totalCount;
    }

    @Nullable
    public ArrayList<Workflow> getWorkflows() {
        return this.workflows;
    }
}
