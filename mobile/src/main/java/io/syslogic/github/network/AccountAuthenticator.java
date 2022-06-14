package io.syslogic.github.network;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.syslogic.github.activity.AuthenticatorActivity;

public class AccountAuthenticator extends AbstractAccountAuthenticator {

    @NonNull private final WeakReference<Context> mContext;

    public AccountAuthenticator(@NonNull Context context) {
        super(context);
        this.mContext = new WeakReference<>(context);
    }

    @Nullable
    @Override
    public String getAuthTokenLabel(@NonNull String s) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Bundle addAccount(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull String accountType,
            @NonNull String authTokenType,
            @NonNull String[] requiredFeatures,
            @NonNull Bundle options
    ) {

        Intent intent = new Intent(this.mContext.get(), AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Nullable
    @Override
    public Bundle confirmCredentials(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull Bundle bundle
    ) {
        Intent intent = new Intent(this.mContext.get(), AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Nullable
    @Override
    public Bundle editProperties(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull String s
    ) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Bundle getAuthToken(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String s,
            @NonNull Bundle bundle
    ) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Bundle updateCredentials(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String s,
            @NonNull Bundle bundle
    ) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Bundle hasFeatures(
            @NonNull AccountAuthenticatorResponse response,
            @NonNull Account account,
            @NonNull String[] strings
    ) {
        throw new UnsupportedOperationException();
    }
}
