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
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("conclusion")
    private String conclusion;

    @SerializedName("run_id")
    private Integer runId;

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

    /** Setters... */
    public void setId(@NonNull Integer value) {
        this.id = value;
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

    public void setRunId(@NonNull Integer value) {
        this.runId = value;
    }

    public void setRunUrl(@NonNull String value) {
        this.runUrl = value;
    }

    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    public void setHeadSha(@NonNull String value) {
        this.headSha = value;
    }

    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    public void setStartedAt(@NonNull Date value) {
        this.startedAt = value;
    }

    public void setCompletedAt(@NonNull Date value) {
        this.completedAt = value;
    }

    public void setSteps(@NonNull ArrayList<WorkflowStep> value) {
        this.steps = value;
    }

    /** Getters... */
    @NonNull
    public Integer getId() {
        return this.id;
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
    public Integer getRunId() {
        return this.runId;
    }

    @NonNull
    public String getRunUrl() {
        return this.runUrl;
    }

    @NonNull
    public String getNodeId() {
        return this.nodeId;
    }

    @NonNull
    public String getHeadSha() {
        return this.headSha;
    }

    @NonNull
    public String getUrl() {
        return this.url;
    }

    @NonNull
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    @NonNull
    public Date getStartedAt() {
        return this.startedAt;
    }

    @NonNull
    public Date getCompletedAt() {
        return this.completedAt;
    }

    @NonNull
    public ArrayList<WorkflowStep> getSteps() {
        return this.steps;
    }
}
