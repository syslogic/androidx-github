package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("id")
    private long id;

    @SerializedName("login")
    private String name;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long value) {
        this.id = value;
    }

    public void setName(String value) {
        this.name = value;
    }
}
