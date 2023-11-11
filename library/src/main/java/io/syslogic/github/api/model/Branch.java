package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Branch
 *
 * @author Martin Zeitler
 */
public class Branch {

    @SerializedName("name")
    private String name;

    @SerializedName("commit")
    private Commit commit;

    /**
     * Setter for name.
     * @param value the name of the branch.
     */
    public void setName(@NonNull String value) {
        this.name = value;
    }

    /**
     * Setter for commit.
     * @param item the commit to set.
     */
    public void setCommit(@NonNull Commit item) {
        this.commit = item;
    }

    /**
     * Getter for commit.
     * @return instance of {@link Commit}.
     */
    @NonNull
    public Commit getCommit() {
        return this.commit;
    }

    /**
     * Getter for name.
     * @return the name of the branch.
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String toString() {
        return this.name;
    }
}
