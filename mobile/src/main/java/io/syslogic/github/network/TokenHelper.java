package io.syslogic.github.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import io.syslogic.github.BuildConfig;

public class TokenHelper {

    /** {@link Log} Tag */
    private static final String LOG_TAG = TokenHelper.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    public static String getAccessToken(Context context) {

        String accessToken = null;

        if (! mDebug) {

            try {
                ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                accessToken = app.metaData.getString("com.github.ACCESS_TOKEN");
                Log.d(LOG_TAG, "com.github.ACCESS_TOKEN: " + accessToken);
                Bundle extras = new Bundle();



            } catch (NullPointerException | PackageManager.NameNotFoundException ignore) {}

        } else {
            Account account = getAccount(context);

        }
        return accessToken;
    }

    private static Account getAccount(Context context) {

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("io.syslogic.github");

        if (accounts.length == 0) {
            Log.d(LOG_TAG, "no device tokens");
            return  null;
        } else {
            Log.d(LOG_TAG, "device tokens: " + accounts.length);
            return accounts[0];
        }
    }

    private static boolean addAccount(Context context, String accessToken) {

        AccountManager accountManager = AccountManager.get(context);
        Account account = new Account("accessToken", "io.syslogic.github");
        Bundle extraData = new Bundle();
        extraData.putString("accessToken", accessToken);
        return accountManager.addAccountExplicitly(account, accessToken, extraData);
    }
}
