package io.syslogic.github.network;

/**
 * Connectivity Listener
 * @author Martin Zeitler
 */
public interface ConnectivityListener {
    void onNetworkAvailable();
    void onNetworkLost();
}
