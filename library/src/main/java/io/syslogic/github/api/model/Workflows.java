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
public class Workflows {

    /** Items */
    @SerializedName("workflows")
    private ArrayList<Workflow> workflows;

    /** Item Count */
    @SerializedName("total_count")
    private Long totalCount = 0L;

    /**
     * Setter for totalCount.
     * @param value the total count of workflows.
     */
    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    /**
     * Setter for workflows.
     * @param items the workflows.
     */
    public void setWorkflows(@NonNull ArrayList<Workflow> items) {
        this.workflows = items;
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
     * Getter for workflows.
     * @return the workflows.
     */
    @Nullable
    public ArrayList<Workflow> getWorkflows() {
        return this.workflows;
    }
}
