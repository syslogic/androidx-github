package io.syslogic.github.network;

import androidx.annotation.NonNull;

import io.syslogic.github.model.User;

/**
 * Token Callback
 * @author Martin Zeitler
 * @version 1.0.0
**/
public interface TokenCallback {

    void onLogin(@NonNull User item);
}
