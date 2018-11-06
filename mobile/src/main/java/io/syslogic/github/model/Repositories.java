package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

public class Repositories extends BaseModel {

    @SerializedName("items")
    private ArrayList<Repository> mItems;

    @SerializedName("total_count")
    private long count;

    public void setRepositories(ArrayList<Repository> items) {
        this.mItems = items;
    }

    public void setCount(long value) {
        this.count = value;
    }

    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    @Bindable
    public long getCount() {
        return this.count;
    }

}
