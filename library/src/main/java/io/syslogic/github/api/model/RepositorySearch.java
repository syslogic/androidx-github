package io.syslogic.github.api.model;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Model: Repository Search
 *
 * @author Martin Zeitler
 */
public class RepositorySearch extends BaseModel {

    @SerializedName("items")
    private ArrayList<Repository> mItems;

    @SerializedName("total_count")
    private Long totalCount;

    public void setRepositories(@NonNull ArrayList<Repository> items) {
        this.mItems = items;
    }

    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    @NonNull
    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    @NonNull
    @Bindable
    public Long getTotalCount() {
        return this.totalCount;
    }

    @NonNull
    @Bindable
    public Long getPageCount() {
        Long recordsPerPage = 30L;
        return (this.totalCount + recordsPerPage -1) / recordsPerPage;
    }
}