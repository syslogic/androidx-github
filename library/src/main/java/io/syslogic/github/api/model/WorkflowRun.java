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

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private long id;

    // foreign key
    @ColumnInfo(name = "workflow_id")
    @SerializedName("workflow_id")
    private long workflowId;

    @ColumnInfo(name = "run_number")
    @SerializedName("run_number")
    private long runNumber;

    @ColumnInfo(name = "check_suite_id")
    @SerializedName("check_suite_id")
    private long checkSuiteId;

    @ColumnInfo(name = "check_suite_node_id")
    @SerializedName("check_suite_node_id")
    private String checkSuiteNodeId;

    @ColumnInfo(name = "node_id")
    @SerializedName("node_id")
    private String nodeId;

    @ColumnInfo(name = "event")
    @SerializedName("event")
    private String event;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    @ColumnInfo(name = "conclusion")
    @SerializedName("conclusion")
    private String conclusion;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "head_branch")
    @SerializedName("head_branch")
    private String headBranch;

    @ColumnInfo(name = "head_sha")
    @SerializedName("head_sha")
    private String headSha;

    @ColumnInfo(name = "display_title")
    @SerializedName("display_title")
    private String displayTitle;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    @ColumnInfo(name = "html_url")
    @SerializedName("html_url")
    private String htmlUrl;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;

    @Ignore
    @SerializedName("actor")
    private User actor;

    @Ignore
    @SerializedName("triggering_actor")
    private User triggeringActor;

    @ColumnInfo(name = "run_started_at")
    @SerializedName("run_started_at")
    private Date runStartedAt;

    @ColumnInfo(name = "run_attempt")
    @SerializedName("run_attempt")
    private long runAttempt;


    /** Setters... */
    public void setId(long value) {
        this.id = value;
    }

    public void setWorkflowId(long value) {
        this.workflowId = value;
    }

    public void setRunNumber(long value) {
        this.runNumber = value;
    }

    public void setCheckSuiteId(long value) {
        this.checkSuiteId = value;
    }

    public void setCheckSuiteNodeId(@NonNull String value) {
        this.checkSuiteNodeId = value;
    }

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setHeadBranch(@NonNull String value) {
        this.headBranch = value;
    }

    public void setDisplayTitle(@NonNull String value) {
        this.displayTitle = value;
    }

    public void setEvent(@NonNull String value) {
        this.event = value;
    }

    public void setStatus(@NonNull String value) {
        this.status = value;
    }

    public void setConclusion(@NonNull String value) {
        this.conclusion = value;
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

    public void setCreatedAt(@NonNull Date value) {
        this.createdAt = value;
    }

    public void setUpdatedAt(@NonNull Date value) {
        this.updatedAt = value;
    }

    public void setActor(@NonNull User value) {
        this.actor = value;
    }

    public void setTriggeringActor(@NonNull User value) {
        this.triggeringActor = value;
    }

    /** Getters... */
    @Bindable
    public long getId() {
        return this.id;
    }

    @Bindable
    public long getWorkflowId() {
        return this.workflowId;
    }

    @Bindable
    public long getRunNumber() {
        return this.runNumber;
    }

    @Bindable
    public long getCheckSuiteId() {
        return this.checkSuiteId;
    }

    @NonNull
    @Bindable
    public String getCheckSuiteNodeId() {
        return this.checkSuiteNodeId;
    }

    @NonNull
    @Bindable
    public String getName() {
        return this.name;
    }

    @NonNull
    @Bindable
    public String getHeadBranch() {
        return this.headBranch;
    }

    @NonNull
    @Bindable
    public String getDisplayTitle() {
        return this.displayTitle;
    }

    @NonNull
    @Bindable
    public String getEvent() {
        return this.event;
    }

    @NonNull
    @Bindable
    public String getStatus() {
        return this.status;
    }

    @NonNull
    @Bindable
    public String getConclusion() {
        return this.conclusion;
    }

    @NonNull
    @Bindable
    public String getNodeId() {
        return this.nodeId;
    }

    @NonNull
    @Bindable
    public String getHeadSha() {
        return this.headSha;
    }

    @NonNull
    @Bindable
    public String getUrl() {
        return this.url;
    }

    @NonNull
    @Bindable
    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    @NonNull
    @Bindable
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @NonNull
    @Bindable
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @NonNull
    @Bindable
    public User getActor() {
        return this.actor;
    }

    @NonNull
    @Bindable
    public User getTriggeringActor() {
        return this.triggeringActor;
    }

    @NonNull
    @Bindable
    public Date getRunStartedAt() {
        return this.runStartedAt;
    }

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
