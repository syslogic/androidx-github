package io.syslogic.github.content;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Repository Sync {@link Service}
 * @author Martin Zeitler
 */
public class RepositorySyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static RepositorySyncAdapter sSyncAdapter = null;

    /** Instantiate the sync adapter object. */
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new RepositorySyncAdapter(getApplicationContext(), true, true);
            }
        }
    }

    /** Return an object that allows the system to invoke the sync adapter. */
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
