package io.syslogic.github.network;

public interface ConnectivityAware {

    void onNetworkAvailable();

    void onNetworkLost();
}
