package io.syslogic.github.network;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.BuildConfig;

/**
 * Token Service
 * @author Martin Zeitler
 */
public class TokenService extends Service {

    /** {@link Log} Tag */
    private static final String LOG_TAG = TokenService.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    private Authenticator mAuthenticator;

    @NonNull
    public static Account getAccount(@NonNull String accountName, @NonNull String accountType) {
        return new Account(accountName, accountType);
    }

    @Override
    public void onCreate() {
        this.mAuthenticator = new Authenticator(this);
    }

    @NonNull
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return this.mAuthenticator.getIBinder();
    }

    public IBinder getIBinder() {
        return this.mAuthenticator.getIBinder();
    }

    public static class Authenticator extends AbstractAccountAuthenticator {

        Authenticator(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public Bundle editProperties(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull String s) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Bundle addAccount(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull String s, @NonNull String s2, @NonNull String[] strings, @NonNull Bundle bundle) {
            return null;
        }

        @Nullable
        @Override
        public Bundle confirmCredentials(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull Account account, @NonNull Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Nullable
        @Override
        public Bundle getAuthToken(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull Account account, @NonNull String s, @NonNull Bundle bundle) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public String getAuthTokenLabel(@NonNull String s) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Bundle updateCredentials(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull Account account, @NonNull String s, @NonNull Bundle bundle) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Bundle hasFeatures(@NonNull AccountAuthenticatorResponse accountAuthenticatorResponse, @NonNull Account account, @NonNull String[] strings) {
            throw new UnsupportedOperationException();
        }
    }
}
