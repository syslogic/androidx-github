package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

public class RateLimit {

    @SerializedName("limit")
    private int limit;

    @SerializedName("remaining")
    private int remaining;

    @SerializedName("reset")
    private long reset;

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRemaining() {
        return this.remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public long getReset() {
        return this.reset;
    }

    public void setReset(long reset) {
        this.reset = reset;
    }
}
