package io.syslogic.githubtrends.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Repositories {

    @SerializedName("total_count")
    private long count;

    @SerializedName("items")
    private ArrayList<Repository> mItems;

    public long getCount() {
        return this.count;
    }

    public ArrayList<Repository> getRepositories() {
        return this.mItems;
    }

    public void setRepositories(ArrayList<Repository> items) {
        this.mItems = items;
    }

    public void setCount(long value) {
        this.count = value;
    }
}
