package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

/**
 * Model: Repositories
 * @author Martin Zeitler
 */
public class Repositories extends BaseModel {

    @SerializedName("items")
    private ArrayList<Repository> mItems;

    @SerializedName("total_count")
    private Long count;

    public void setRepositories(@NonNull ArrayList<Repository> items) {
        this.mItems = items;
    }

    public void setCount(@NonNull Long value) {
        this.count = value;
    }

    @NonNull
    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    @NonNull
    @Bindable
    public Long getCount() {
        return this.count;
    }

}
