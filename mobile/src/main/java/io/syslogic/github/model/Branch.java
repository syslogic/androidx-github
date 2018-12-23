package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Branch {

    @SerializedName("name")
    private String name;

    @SerializedName("commit")
    private Commit commit;

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setCommit(@NonNull Commit item) {
        this.commit = item;
    }

    @NonNull
    public Commit getCommit() {
        return this.commit;
    }

    @NonNull
    public String getName() {
        return this.name;
    }
}
