package io.syslogic.github.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import io.syslogic.github.BuildConfig;

public class TokenHelper {

    /** {@link Log} Tag */
    private static final String LOG_TAG = TokenHelper.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    public static String getAccessToken(Context context) {
        String accessToken = "";
        if (mDebug) {

            try {
                ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                accessToken = app.metaData.getString("com.github.ACCESS_TOKEN");
                Log.d(LOG_TAG, "com.github.ACCESS_TOKEN: " + accessToken);
            } catch (NullPointerException | PackageManager.NameNotFoundException ignore) {}

        } else {

            AccountManager accountManager = AccountManager.get(context);
            Account[] accounts = accountManager.getAccountsByType("com.github");
            if (accounts.length > 0) {
                Log.d(LOG_TAG, "device tokens: " + accounts.length);
                accessToken = "TODO: use AccountManager";
            } else {
                Log.d(LOG_TAG, "no device tokens");
            }

        }
        return accessToken;
    }
}
