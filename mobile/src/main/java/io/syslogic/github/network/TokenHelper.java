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
import io.syslogic.github.Constants;

/**
 * Access Token Helper
 *
 * @author Martin Zeitler
 */
public class TokenHelper {

    /** {@link Log} Tag */
    private static final String LOG_TAG = TokenHelper.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    @Nullable
    public static String getAccessToken(@NonNull Context context) {
        AccountManager accountManager = AccountManager.get(context);
        if (mDebug) {
            /* Attempting to load the personal access token from package meta. */
            return loadPackageMeta(context, accountManager);
        } else {
            /* Attempting to load the personal access token from AccountManager. */
            Account account = getAccount(accountManager, 0);
            if (account != null) {
                return accountManager.getUserData(account, "accessToken");
            } else {
                Log.d(LOG_TAG, "account not found: " + Constants.ACCOUNT_TYPE);
                return null;
            }
        }
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    private static String loadPackageMeta(@NonNull Context context, AccountManager accountManager) {
        String accessToken = null;
        try {
            ApplicationInfo app;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                PackageManager.ApplicationInfoFlags flags = PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA);
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), flags);
            } else {
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            }
            accessToken = app.metaData.getString("com.github.ACCESS_TOKEN");
            if (accessToken != null) {addAccount(accountManager, accessToken);}
        } catch (NullPointerException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    @Nullable
    @SuppressWarnings("SameParameterValue")
    private static Account getAccount(@NonNull AccountManager accountManager, int index) {
        Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
        if (accounts.length <= index) {return null;}
        else {return accounts[ index ];}
    }

    /** It currently adds into the "Personal" accounts (probably depending on the profile). */
    @Nullable
    public static Account addAccount(AccountManager accountManager, String accessToken) {
        if (getAccount(accountManager, 0) == null) {
            Account account = new Account("GitHub API Client", Constants.ACCOUNT_TYPE);
            Bundle extraData = new Bundle();
            extraData.putString("accessToken", accessToken);
            accountManager.addAccountExplicitly(account, accessToken, extraData);
            return account;
        }
        return null;
    }
}
