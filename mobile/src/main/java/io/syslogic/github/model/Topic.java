package io.syslogic.github.model;


public class Topic {

    private long id;

    private String queryString = null;

    public Topic() {}

    public Topic(long id, String value) {
        this.setId(id);
        this.setQueryString(value);
    }

    public long getId() {
        return this.id;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String value) {
        this.queryString = value;
    }

    public void setId(long value) {
        this.id = value;
    }
}
