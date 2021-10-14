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
 * Access Token Helper
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

        /* Attempting to load the personal access token from package meta. */
        AccountManager accountManager = AccountManager.get(context);
        if (mDebug) {loadPackageMeta(context, accountManager);}

        /* Attempting to load the personal access token from AccountManager. */
        Account account = getAccount(accountManager, 0);
        if (account == null) {
            Log.d(LOG_TAG, "account not found: " + accountType);
        } else {
            return accountManager.getUserData(account, "accessToken");
        }
        return null;
    }

    private static void loadPackageMeta(@NonNull Context context, AccountManager accountManager) {
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String accessToken = app.metaData.getString("com.github.ACCESS_TOKEN");
            if (accessToken != null) {addAccount(accountManager, accessToken);}
        } catch (NullPointerException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static Account getAccount(AccountManager accountManager, int index) {
        Account[] accounts = accountManager.getAccountsByType(accountType);
        if (accounts.length < index) {return null;}
        else {return accounts[index];}
    }

    /** It currently adds into the "Personal" accounts (probably depending on the profile). */
    private static void addAccount(AccountManager accountManager, String accessToken) {
        if (getAccount(accountManager, 0) == null) {
            Account account = new Account("Personal Access Token", accountType);
            Bundle extraData = new Bundle();
            extraData.putString("accessToken", accessToken);
            accountManager.addAccountExplicitly(account, accessToken, extraData);
        }
    }
}
