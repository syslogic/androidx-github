package io.syslogic.github.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * TODO: Token Service
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class TokenService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
