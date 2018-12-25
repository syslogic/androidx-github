package io.syslogic.github.network;

import io.syslogic.github.model.User;

/**
 * Token Callback
 * @author Martin Zeitler
 * @version 1.0.0
**/
public interface TokenCallback {

    void onLogin(User item);
}
