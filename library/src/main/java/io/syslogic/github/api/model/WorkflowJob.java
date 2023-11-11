package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Model: Workflow Job
 *
 * @see <a href="https://docs.github.com/en/rest/actions/workflow-jobs#about-the-workflow-jobs-api">Workflow jobs</a>
 * @author Martin Zeitler
 */
public class WorkflowJob {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("conclusion")
    private String conclusion;

    @SerializedName("run_id")
    private Long runId;

    @SerializedName("run_url")
    private String runUrl;

    @SerializedName("node_id")
    private String nodeId;

    @SerializedName("head_sha")
    private String headSha;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("started_at")
    private Date startedAt;

    @SerializedName("completed_at")
    private Date completedAt;

    @SerializedName("steps")
    private ArrayList<WorkflowStep> steps;

    /**
     * Setter for id.
     * @param value the ID of the job.
     */
    public void setId(@NonNull Long value) {
        this.id = value;
    }

    /**
     * Setter for name.
     * @param value the name of the job.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for status.
     * @param value the status of the job.
     */
    public void setStatus(@NonNull String value) {
        this.status = value;
    }

    /**
     * Setter for conclusion.
     * @param value the conclusion of the job.
     */
    public void setConclusion(@NonNull String value) {
        this.conclusion = value;
    }

    /**
     * Setter for runId.
     * @param value the runId of the job.
     */
    public void setRunId(@NonNull Long value) {
        this.runId = value;
    }

    /**
     * Setter for runUrl.
     * @param value the runUrl of the job.
     */
    public void setRunUrl(@NonNull String value) {
        this.runUrl = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the job.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for headSha.
     * @param value the headSha of the job.
     */
    public void setHeadSha(@NonNull String value) {
        this.headSha = value;
    }

    /**
     * Setter for url.
     * @param value the url of the job.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the job.
     */
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    /**
     * Setter for startedAt.
     * @param value the startedAt of the job.
     */
    public void setStartedAt(@NonNull Date value) {
        this.startedAt = value;
    }

    /**
     * Setter for completedAt.
     * @param value the completedAt of the job.
     */
    public void setCompletedAt(@NonNull Date value) {
        this.completedAt = value;
    }

    /**
     * Setter for steps.
     * @param value the steps of the job.
     */
    public void setSteps(@NonNull ArrayList<WorkflowStep> value) {
        this.steps = value;
    }

    /**
     * Getter for id.
     * @return the id of the job.
     */
    @NonNull
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for name.
     * @return the name of the job.
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Getter for status.
     * @return the status of the job.
     */
    @NonNull
    public String getStatus() {
        return this.status;
    }

    /**
     * Getter for conclusion.
     * @return the conclusion of the job.
     */
    @NonNull
    public String getConclusion() {
        return this.conclusion;
    }

    /**
     * Getter for runId.
     * @return the runId of the job.
     */
    @NonNull
    public Long getRunId() {
        return this.runId;
    }

    /**
     * Getter for runUrl.
     * @return the runUrl of the job.
     */
    @NonNull
    public String getRunUrl() {
        return this.runUrl;
    }

    /**
     * Getter for nodeId.
     * @return the nodeId of the job.
     */
    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for headSha.
     * @return the headSha of the job.
     */
    @NonNull
    public String getHeadSha() {
        return this.headSha;
    }

    /**
     * Getter for url.
     * @return the url of the job.
     */
    @NonNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Getter for htmlUrl.
     * @return the htmlUrl of the job.
     */
    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Getter for startedAt.
     * @return the startedAt of the job.
     */
    @NonNull
    public Date getStartedAt() {
        return this.startedAt;
    }

    /**
     * Getter for completedAt.
     * @return the completedAt of the job.
     */
    @NonNull
    public Date getCompletedAt() {
        return this.completedAt;
    }

    /**
     * Getter for steps.
     * @return the steps of the job.
     */
    @NonNull
    public ArrayList<WorkflowStep> getSteps() {
        return this.steps;
    }
}
