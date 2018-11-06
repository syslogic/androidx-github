package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RateLimits {

    @SerializedName("resources")
    private Resources resources;

    @SerializedName("rate")
    private RateLimit rates;

    public void setResources(Resources item) {
        this.resources = item;
    }

    public Resources getResources() {
        return this.resources;
    }

    public void setRate(RateLimit item) {
        this.rates = item;
    }

    public RateLimit getRate() {
        return this.rates;
    }
}
