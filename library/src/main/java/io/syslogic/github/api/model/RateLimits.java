package io.syslogic.github.api.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Model: Rate Limits
 *
 * @author Martin Zeitler
 */
public class RateLimits {

    @SerializedName("resources")
    private Resources resources;

    @SerializedName("rate")
    private RateLimit rates;

    /**
     * Setter for {@link Resources}.
     * @param item the resources.
     */
    public void setResources(@NonNull Resources item) {
        this.resources = item;
    }

    /**
     * Setter for {@link RateLimit}.
     * @param item the rates.
     */
    public void setRate(@NonNull RateLimit item) {
        this.rates = item;
    }

    /**
     * Getter for {@link Resources}.
     * @return the resources.
     */
    @NonNull
    public Resources getResources() {
        return this.resources;
    }

    /**
     * Getter for {@link RateLimit}.
     * @return the rates.
     */
    @NonNull
    public RateLimit getRate() {
        return this.rates;
    }
}
