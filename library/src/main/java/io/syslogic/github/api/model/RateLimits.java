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

    public void setResources(@NonNull Resources item) {
        this.resources = item;
    }

    public void setRate(@NonNull RateLimit item) {
        this.rates = item;
    }

    @NonNull
    public Resources getResources() {
        return this.resources;
    }

    @NonNull
    public RateLimit getRate() {
        return this.rates;
    }
}
