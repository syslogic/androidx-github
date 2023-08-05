package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model: Workflow Step
 *
 * @see <a href="https://docs.github.com/en/rest/actions/workflow-jobs#about-the-workflow-jobs-api">Workflow jobs</a>
 * @author Martin Zeitler
 */
public class WorkflowStep {

    @SerializedName("number")
    private Integer number;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("conclusion")
    private String conclusion;

    @SerializedName("started_at")
    private Date startedAt;

    @SerializedName("completed_at")
    private Date completedAt;

    /** Setters... */
    public void setNumber(@NonNull Integer value) {
        this.number = value;
    }

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setStatus(@NonNull String value) {
        this.status = value;
    }

    public void setConclusion(@NonNull String value) {
        this.conclusion = value;
    }

    public void setStartedAt(@NonNull Date value) {
        this.startedAt = value;
    }

    public void setCompletedAt(@NonNull Date item) {
        this.completedAt = item;
    }

    /** Getters... */
    @NonNull
    public Integer getNumber() {
        return this.number;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getStatus() {
        return this.status;
    }

    @NonNull
    public String getConclusion() {
        return this.conclusion;
    }

    @NonNull
    public Date getStartedAt() {
        return this.startedAt;
    }

    @NonNull
    public Date getCompletedAt() {
        return this.completedAt;
    }
}
