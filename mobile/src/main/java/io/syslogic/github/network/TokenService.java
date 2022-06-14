package io.syslogic.github.network;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

/**
 * Token Service
 *
 * @author Martin Zeitler
 */
public class TokenService extends Service {

    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        synchronized (TokenService.this) {
            if (this.mAuthenticator == null) {
                this.mAuthenticator = new AccountAuthenticator(this);
            }
        }

    }

    @NonNull
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return this.mAuthenticator.getIBinder();
    }

    @NonNull
    @SuppressWarnings("unused")
    public IBinder getIBinder() {
        return this.mAuthenticator.getIBinder();
    }

    @NonNull
    @SuppressWarnings("unused")
    public static Account getAccount(@NonNull String accountName, @NonNull String accountType) {
        return new Account(accountName, accountType);
    }
}
