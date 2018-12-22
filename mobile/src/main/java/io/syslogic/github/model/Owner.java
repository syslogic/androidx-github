package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("id")
    private long id;

    @SerializedName("login")
    private String login;

    public long getId() {
        return this.id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setId(long value) {
        this.id = value;
    }

    public void setLogin(String value) {
        this.login = value;
    }

}
