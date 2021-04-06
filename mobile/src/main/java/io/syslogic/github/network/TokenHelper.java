package io.syslogic.github.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.BuildConfig;

/**
 * Token Helper
 * @author Martin Zeitler
 */
public class TokenHelper {

    /** {@link Log} Tag */
    private static final String LOG_TAG = TokenHelper.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    private static final String accountType = "io.syslogic.github";

    @Nullable
    public static String getAccessToken(@NonNull Context context) {

        AccountManager accountManager = AccountManager.get(context);
        String accessToken = null;

        if (mDebug) {

            try {
                ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                accessToken = app.metaData.getString("com.github.ACCESS_TOKEN");
                // Log.d(LOG_TAG, "com.github.ACCESS_TOKEN: " + accessToken);
                addAccount(accountManager, accessToken);

            } catch (NullPointerException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            Account account = getAccount(accountManager);
            if(account == null) {
                Log.d(LOG_TAG, "account not found: " + accountType);
            } else {
                accessToken = accountManager.getUserData(account, "accessToken");
            }
        }
        return accessToken;
    }

    private static Account getAccount(AccountManager accountManager) {

        Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts.length == 0) {
            Log.d(LOG_TAG, "no device tokens");
            return  null;
        } else {
            Log.d(LOG_TAG, "device tokens: " + accounts.length);
            return accounts[0];
        }
    }

    private static boolean addAccount(AccountManager accountManager, String accessToken) {
        Account account = new Account("GitHub", accountType);
        Bundle extraData = new Bundle();
        extraData.putString("accessToken", accessToken);
        return accountManager.addAccountExplicitly(account, accessToken, extraData);
    }
}
