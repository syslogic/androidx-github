package io.syslogic.github.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Rate Limit
 *
 * @author Martin Zeitler
 */
public class RateLimit {

    @SerializedName("limit")
    private int limit;

    @SerializedName("remaining")
    private int remaining;

    @SerializedName("reset")
    private long reset;

    /**
     * Getter for limit.
     * @return the limit.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * Getter for remaining.
     * @return the remaining.
     */
    public int getRemaining() {
        return this.remaining;
    }

    /**
     * Getter for reset.
     * @return the reset.
     */
    public long getReset() {
        return this.reset;
    }

    /**
     * Setter for limit.
     * @param value the limit.
     */
    public void setLimit(int value) {
        this.limit = value;
    }

    /**
     * Setter for remaining.
     * @param value the remaining.
     */
    public void setRemaining(int value) {
        this.remaining = value;
    }

    /**
     * Setter for reset.
     * @param value the reset.
     */
    public void setReset(long value) {
        this.reset = value;
    }
}
