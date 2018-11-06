package io.syslogic.github.network;

public interface IConnectivityListener {

    void onNetworkAvailable();

    void onNetworkLost();
}
