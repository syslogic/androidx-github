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

    /**
     * Setter for number.
     * @param value the number of the workflow step.
     */
    public void setNumber(@NonNull Integer value) {
        this.number = value;
    }

    /**
     * Setter for name.
     * @param value the name of the workflow step.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for status.
     * @param value the status of the workflow step.
     */
    public void setStatus(@NonNull String value) {
        this.status = value;
    }

    /**
     * Setter for conclusion.
     * @param value the conclusion of the workflow step.
     */
    public void setConclusion(@NonNull String value) {
        this.conclusion = value;
    }

    /**
     * Setter for startedAt.
     * @param value the startedAt of the workflow step.
     */
    public void setStartedAt(@NonNull Date value) {
        this.startedAt = value;
    }

    /**
     * Setter for completedAt.
     * @param value the completedAt of the workflow step.
     */
    public void setCompletedAt(@NonNull Date value) {
        this.completedAt = value;
    }

    /**
     * Getter for number.
     * @return the number of the workflow step.
     */
    @NonNull
    public Integer getNumber() {
        return this.number;
    }

    /**
     * Getter for name.
     * @return the name of the workflow step.
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Getter for status.
     * @return the status of the workflow step.
     */
    @NonNull
    public String getStatus() {
        return this.status;
    }

    /**
     * Getter for conclusion.
     * @return the conclusion of the workflow step.
     */
    @NonNull
    public String getConclusion() {
        return this.conclusion;
    }

    /**
     * Getter for startedAt.
     * @return the startedAt of the workflow step.
     */
    @NonNull
    public Date getStartedAt() {
        return this.startedAt;
    }

    /**
     * Getter for completedAt.
     * @return the completedAt of the workflow step.
     */
    @NonNull
    public Date getCompletedAt() {
        return this.completedAt;
    }
}
