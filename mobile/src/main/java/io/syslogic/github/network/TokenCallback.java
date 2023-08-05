package io.syslogic.github.network;

import androidx.annotation.NonNull;

import io.syslogic.github.api.model.User;

/**
 * Token Callback
 *
 * @author Martin Zeitler
 */
public interface TokenCallback {
    void onLogin(@NonNull User item);
}
