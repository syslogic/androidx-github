package io.syslogic.github.model;

import com.google.gson.annotations.SerializedName;

public class Resources {

    @SerializedName("core")
    private RateLimit core;

    @SerializedName("search")
    private RateLimit search;

    @SerializedName("graphql")
    private RateLimit graphql;

    public RateLimit getCore() {
        return core;
    }

    public void setCore(RateLimit core) {
        this.core = core;
    }

    public RateLimit getSearch() {
        return search;
    }

    public void setSearch(RateLimit search) {
        this.search = search;
    }

    public RateLimit getGraphql() {
        return graphql;
    }

    public void setGraphql(RateLimit graphql) {
        this.graphql = graphql;
    }

}