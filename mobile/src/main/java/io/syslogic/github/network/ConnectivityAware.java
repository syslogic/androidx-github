package io.syslogic.github.network;

public interface IConnectivityAware {

    void onNetworkAvailable();

    void onNetworkLost();
}
