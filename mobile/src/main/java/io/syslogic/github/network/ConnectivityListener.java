package io.syslogic.github.network;

/**
 * Connectivity Listener
 * @author Martin Zeitler
 * @version 1.0.0
**/
public interface ConnectivityListener {

    void onNetworkAvailable();

    void onNetworkLost();
}
