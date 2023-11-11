package io.syslogic.github.api.model;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.syslogic.github.api.Constants;

/**
 * Model: Workflow Run
 *
 * @author Martin Zeitler
 */
@Entity(tableName = Constants.TABLE_WORKFLOW_RUNS)
public class WorkflowRun extends BaseModel implements IContentProvider {

    /** ID */
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    /** WorkflowId */
    // foreign key
    @ColumnInfo(name = "workflow_id")
    @SerializedName("workflow_id")
    private long workflowId;

    /** RunNumber */
    @ColumnInfo(name = "run_number")
    @SerializedName("run_number")
    private long runNumber;

    /** CheckSuiteId */
    @ColumnInfo(name = "check_suite_id")
    @SerializedName("check_suite_id")
    private long checkSuiteId;

    /** CheckSuiteNodeId */
    @ColumnInfo(name = "check_suite_node_id")
    @SerializedName("check_suite_node_id")
    private String checkSuiteNodeId;

    /** NodeId */
    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    /** Event */
    @ColumnInfo(name = "event")
    @SerializedName("event")
    private String event;

    /** Status */
    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    /** Conclusion */
    @ColumnInfo(name = "conclusion")
    @SerializedName("conclusion")
    private String conclusion;

    /** Name */
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    /** HeadBranch */
    @ColumnInfo(name = "head_branch")
    @SerializedName("head_branch")
    private String headBranch;

    /** HeadSha */
    @ColumnInfo(name = "head_sha")
    @SerializedName("head_sha")
    private String headSha;

    /** DisplayTitle */
    @ColumnInfo(name = "display_title")
    @SerializedName("display_title")
    private String displayTitle;

    /** Url */
    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    /** HtmlUrl */
    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    /** CreatedAt */
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;

    /** UpdatedAt */
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    /** Actor */
    @Ignore
    @SerializedName("actor")
    private User actor;

    /** TriggeringActor */
    @Ignore
    @SerializedName("triggering_actor")
    private User triggeringActor;

    /** RunStartedAt */
    @ColumnInfo(name = "run_started_at")
    @SerializedName("run_started_at")
    private Date runStartedAt;

    /** RunAttempt */
    @ColumnInfo(name = "run_attempt")
    @SerializedName("run_attempt")
    private long runAttempt;

    /**
     * Setter for id.
     * @param value the ID of the workflow run.
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Setter for workflowId.
     * @param value the workflowId of the workflow run.
     */
    public void setWorkflowId(long value) {
        this.workflowId = value;
    }

    /**
     * Setter for runNumber.
     * @param value the runNumber of the workflow run.
     */
    public void setRunNumber(long value) {
        this.runNumber = value;
    }

    /**
     * Setter for checkSuiteId.
     * @param value the checkSuiteId of the workflow run.
     */
    public void setCheckSuiteId(long value) {
        this.checkSuiteId = value;
    }

    /**
     * Setter for checkSuiteNodeId.
     * @param value the checkSuiteNodeId of the workflow run.
     */
    public void setCheckSuiteNodeId(@NonNull String value) {
        this.checkSuiteNodeId = value;
    }

    /**
     * Setter for name.
     * @param value the name of the workflow run.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for headBranch.
     * @param value the headBranch of the workflow run.
     */
    public void setHeadBranch(@NonNull String value) {
        this.headBranch = value;
    }

    /**
     * Setter for displayTitle.
     * @param value the displayTitle of the workflow run.
     */
    public void setDisplayTitle(@NonNull String value) {
        this.displayTitle = value;
    }

    /**
     * Setter for event.
     * @param value the event of the workflow run.
     */
    public void setEvent(@NonNull String value) {
        this.event = value;
    }

    /**
     * Setter for status.
     * @param value the status of the workflow run.
     */
    public void setStatus(@NonNull String value) {
        this.status = value;
    }

    /**
     * Setter for conclusion.
     * @param value the conclusion of the workflow run.
     */
    public void setConclusion(@NonNull String value) {
        this.conclusion = value;
    }

    /**
     * Setter for nodeId.
     * @param value the nodeId of the workflow run.
     */
    public void setNodeId(@NonNull String value) {
        this.nodeId = value;
    }

    /**
     * Setter for headSha.
     * @param value the headSha of the workflow run.
     */
    public void setHeadSha(@NonNull String value) {
        this.headSha = value;
    }

    /**
     * Setter for url.
     * @param value the url of the workflow run.
     */
    public void setUrl(@NonNull String value) {
        this.url = value;
    }

    /**
     * Setter for htmlUrl.
     * @param value the htmlUrl of the workflow run.
     */
    public void setHtmlUrl(@NonNull String value) {
        this.htmlUrl = value;
    }

    /**
     * Setter for createdAt.
     * @param value the createdAt of the workflow run.
     */
    public void setCreatedAt(@NonNull Date value) {
        this.createdAt = value;
    }

    /**
     * Setter for updatedAt.
     * @param value the updatedAt of the workflow run.
     */
    public void setUpdatedAt(@NonNull Date value) {
        this.updatedAt = value;
    }

    /**
     * Setter for actor.
     * @param value the actor of the workflow run.
     */
    public void setActor(@NonNull User value) {
        this.actor = value;
    }

    /**
     * Setter for triggeringActor.
     * @param value the triggeringActor of the workflow run.
     */
    public void setTriggeringActor(@NonNull User value) {
        this.triggeringActor = value;
    }

    /**
     * Getter for id.
     * @return the id of the workflow run.
     */
    @Bindable
    public long getId() {
        return this.id;
    }

    /**
     * Getter for workflowId.
     * @return the workflowId of the workflow run.
     */
    @Bindable
    public long getWorkflowId() {
        return this.workflowId;
    }

    /**
     * Getter for runNumber.
     * @return the runNumber of the workflow run.
     */
    @Bindable
    public long getRunNumber() {
        return this.runNumber;
    }

    /**
     * Getter for checkSuiteId.
     * @return the checkSuiteId of the workflow run.
     */
    @Bindable
    public long getCheckSuiteId() {
        return this.checkSuiteId;
    }

    /**
     * Getter for checkSuiteNodeId.
     * @return the checkSuiteNodeId of the workflow run.
     */
    @NonNull
    @Bindable
    public String getCheckSuiteNodeId() {
        return this.checkSuiteNodeId;
    }

    /**
     * Getter for name.
     * @return the name of the workflow run.
     */
    @NonNull
    @Bindable
    public String getName() {
        return this.name;
    }

    /**
     * Getter for headBranch.
     * @return the head branch of the workflow run.
     */
    @NonNull
    @Bindable
    public String getHeadBranch() {
        return this.headBranch;
    }

    /**
     * Getter for displayTitle.
     * @return the display title of the workflow run.
     */
    @NonNull
    @Bindable
    public String getDisplayTitle() {
        return this.displayTitle;
    }

    /**
     * Getter for event.
     * @return the event of the workflow run.
     */
    @NonNull
    @Bindable
    public String getEvent() {
        return this.event;
    }

    /**
     * Getter for status.
     * @return the status of the workflow run.
     */
    @NonNull
    @Bindable
    public String getStatus() {
        return this.status;
    }

    /**
     * Getter for conclusion.
     * @return the conclusion of the workflow run.
     */
    @NonNull
    @Bindable
    public String getConclusion() {
        return this.conclusion;
    }

    /**
     * Getter for name.
     * @return the name of the workflow run.
     */
    @NonNull
    @Bindable
    public String getNodeId() {
        return this.nodeId;
    }

    /**
     * Getter for headSha.
     * @return the headSha of the workflow run.
     */
    @NonNull
    @Bindable
    public String getHeadSha() {
        return this.headSha;
    }

    /**
     * Getter for url.
     * @return the url of the workflow run.
     */
    @NonNull
    @Bindable
    public String getUrl() {
        return this.url;
    }

    /**
     * Getter for htmlUrl.
     * @return the htmlUrl of the workflow run.
     */
    @NonNull
    @Bindable
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    /**
     * Getter for createdAt.
     * @return the createdAt of the workflow run.
     */
    @NonNull
    @Bindable
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Getter for updatedAt.
     * @return the updatedAt of the workflow run.
     */
    @NonNull
    @Bindable
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Getter for actor.
     * @return the actor of the workflow run.
     */
    @NonNull
    @Bindable
    public User getActor() {
        return this.actor;
    }

    /**
     * Getter for triggeringActor.
     * @return the triggering actor of the workflow run.
     */
    @NonNull
    @Bindable
    public User getTriggeringActor() {
        return this.triggeringActor;
    }

    /**
     * Getter for runStartedAt.
     * @return the runStartedAt of the workflow run.
     */
    @NonNull
    @Bindable
    public Date getRunStartedAt() {
        return this.runStartedAt;
    }

    /**
     * Getter for runAttempt.
     * @return the runAttempt of the workflow run.
     */
    @Bindable
    public long getRunAttempt() {
        return this.runAttempt;
    }

    @NonNull
    @Override
    public BaseModel fromContentValues(@NonNull ContentValues values) {
        return null;
    }

    @NonNull
    @Override
    public ContentValues toContentValues() {
        return null;
    }
}
