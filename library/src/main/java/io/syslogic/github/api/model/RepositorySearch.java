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

    /** Items */
    @SerializedName("items")
    private ArrayList<Repository> mItems;

    /** Item Count */
    @SerializedName("total_count")
    private Long totalCount;

    /**
     * Setter for repositories.
     * @param items the repositories.
     */
    public void setRepositories(@NonNull ArrayList<Repository> items) {
        this.mItems = items;
    }

    /**
     * Setter for totalCount.
     * @param value the total count of repositories.
     */
    @SuppressWarnings("unused")
    public void setTotalCount(@NonNull Long value) {
        this.totalCount = value;
    }

    /**
     * Getter for repositories.
     * @return the results.
     */
    @NonNull
    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    /**
     * Getter for totalCount.
     * @return total count of results.
     */
    @NonNull
    @Bindable
    public Long getTotalCount() {
        return this.totalCount;
    }

    /**
     * Getter for pageCount.
     * @return the expected page count.
     */
    @NonNull
    @Bindable
    public Long getPageCount() {
        Long recordsPerPage = 30L;
        return (this.totalCount + recordsPerPage -1) / recordsPerPage;
    }
}
