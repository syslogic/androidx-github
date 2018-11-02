package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Repositories {

    @SerializedName("items")
    private ArrayList<Repository> mItems;

    @SerializedName("total_count")
    private long count;

    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    public long getCount() {
        return this.count;
    }

    public void setRepositories(ArrayList<Repository> items) {
        this.mItems = items;
    }

    public void setCount(long value) {
        this.count = value;
    }
}
