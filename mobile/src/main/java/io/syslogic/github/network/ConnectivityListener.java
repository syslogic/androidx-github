package io.syslogic.github.network;

public interface ConnectivityListener {

    void onNetworkAvailable();

    void onNetworkLost();
}
