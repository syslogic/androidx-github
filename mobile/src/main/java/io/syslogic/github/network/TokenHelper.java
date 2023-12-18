package io.syslogic.github.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.activity.AuthenticatorActivity;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.User;
import io.syslogic.github.fragment.HomeScreenFragment;
import io.syslogic.github.fragment.HomeScreenFragmentDirections;

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
    @NonNull static final String LOG_TAG = TokenHelper.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    @Nullable
    public static String getAccessToken(@NonNull Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getAccount(accountManager, 0);
        if (account != null) {
            /* Default: Load the access token from AccountManager. */
            return accountManager.getUserData(account, "token");
        } else if (mDebug) {
            /* Debug: Try to load and validate the access token. */
            return loadTokenFromPackageMeta(activity, accountManager);
        } else {
            Log.e(LOG_TAG, "Account not found: " + Constants.ACCOUNT_TYPE);
            return null;
        }
    }

    public static void setAccessToken(Activity activity, @Nullable String token) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getAccount(accountManager, 0);
        if (account != null && token == null) {
            accountManager.removeAccount(account, activity, accountManagerFuture -> {
                Log.d(LOG_TAG, "Account removed: " + Constants.ACCOUNT_TYPE);
            }, new Handler(Looper.getMainLooper()));
        } else if (account == null && token == null) {
            /* This maybe happen when the token loaded from package-meta has expired. */
            Intent intent = new Intent(activity, AuthenticatorActivity.class);
            activity.startActivity(intent);
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

    private static String loadTokenFromPackageMeta(@NonNull Activity activity, AccountManager accountManager) {
        String token = null;
        try {

            ApplicationInfo app;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                PackageManager.ApplicationInfoFlags flags = PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA);
                app = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), flags);
            } else {
                app = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            }

            if (mDebug && app.metaData.keySet().contains("com.github.ACCESS_TOKEN")) {
                Log.d(LOG_TAG, "loading access token from meat-data: " + Constants.ACCOUNT_TYPE);
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
                                    if (account != null) {Log.d(LOG_TAG, "account added");}
                                    else {Log.d(LOG_TAG, "account not added");}
                                }
                            } else if (response.errorBody() != null) {
                                try {
                                    String errors = response.errorBody().string();
                                    JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                                    String message = jsonObject.get("message").toString();

                                    /* "Bad credentials" means that the provided access-token is invalid. */
                                    if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "login error: " + message);}
                                    if (message.equals("\"Bad credentials\"")) {

                                        // Remove the token, it is invalid anyway.
                                        TokenHelper.setAccessToken(activity, null);

                                        if (activity instanceof NavHostActivity activity2) {
                                            if (activity2.getCurrentFragment() instanceof HomeScreenFragment) {
                                                activity2.getNavController().navigate(
                                                    HomeScreenFragmentDirections
                                                            .actionHomeScreenFragmentToPreferencesFragment()
                                                );
                                            }
                                        }
                                    }
                                } catch (IOException e) {
                                    if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "IOException: " + e.getMessage());}
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                            Log.e(LOG_TAG, "onFailure: " + t.getMessage());
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
            Account account = new Account(username + "@github.com", Constants.ACCOUNT_TYPE);
            Bundle extraData = new Bundle();
            extraData.putString("username", username);
            extraData.putString("token", token);
            accountManager.addAccountExplicitly(account, token, extraData);
            return account;
        }
        return null;
    }
}