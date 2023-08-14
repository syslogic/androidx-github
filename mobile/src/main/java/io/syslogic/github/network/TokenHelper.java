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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.User;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Account account = getAccount(accountManager, 0);
        if (account != null) {
            /* Default: Load the access token from AccountManager. */
            return accountManager.getUserData(account, "token");
        } else if (mDebug) {
            /* Debug: Try to load and validate the access token. */
            return loadTokenFromPackageMeta(context, accountManager);
        } else {
            Log.e(LOG_TAG, "Account not found: " + Constants.ACCOUNT_TYPE);
            return null;
        }
    }

    /** The username is now being stored along with the token */
    @Nullable
    public static String getUsername(@NonNull Context context) {
        AccountManager accountManager = AccountManager.get(context);
        /* Attempting to load the personal access token from AccountManager. */
        Account account = getAccount(accountManager, 0);
        if (account != null) {
            return accountManager.getUserData(account, "username");
        } else {
            Log.e(LOG_TAG, "account not found: " + Constants.ACCOUNT_TYPE);
            return null;
        }
    }

    private static String loadTokenFromPackageMeta(@NonNull Context context, AccountManager accountManager) {
        String token = null;
        try {

            ApplicationInfo app;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                PackageManager.ApplicationInfoFlags flags = PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA);
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), flags);
            } else {
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            }

            if (app.metaData.keySet().contains("com.github.ACCESS_TOKEN")) {

                token = app.metaData.getString("com.github.ACCESS_TOKEN");
                if (token != null && !token.equals("null")) {

                    /* Obtain the username; this also validates the access token. */
                    Call<User> api = GithubClient.getUser(token);
                    GithubClient.logUrl(LOG_TAG, api);
                    String finalToken = token;

                    api.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            if (response.code() == 200) { // OK
                                if (response.body() != null) {
                                    User item = response.body();
                                    Account account = addAccount(accountManager, item.getLogin(), finalToken);
                                    if (mDebug) {
                                        if (account != null) {Log.d(LOG_TAG, "account added");}
                                         else {Log.d(LOG_TAG, "account not added");}
                                    }
                                }
                            } else {
                                /* "bad credentials" means that the provided access-token is invalid. */
                                if (response.errorBody() != null) {
                                    logError(response.errorBody());
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                            if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                        }
                    });
                }
            }
        } catch (NullPointerException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return token;
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
    public static Account addAccount(AccountManager accountManager, String username, String token) {
        if (getAccount(accountManager, 0) == null) {
            Account account = new Account("" + username + "@github.com", Constants.ACCOUNT_TYPE);
            Bundle extraData = new Bundle();
            extraData.putString("username", username);
            extraData.putString("token", token);
            accountManager.addAccountExplicitly(account, token, extraData);
            return account;
        }
        return null;
    }

    static void logError(@NonNull ResponseBody responseBody) {
        try {
            String errors = responseBody.string();
            JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, jsonObject.get("message").toString());}
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + e.getMessage());}
        }
    }
}
