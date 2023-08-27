package io.syslogic.github.content;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.RepositoriesDao;
import io.syslogic.github.network.TokenHelper;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository {@link AbstractThreadedSyncAdapter}
 *
 * @author Martin Zeitler
 */
public class RepositorySyncAdapter extends AbstractThreadedSyncAdapter {

    /** Log Tag */
    @NonNull static final String LOG_TAG = RepositorySyncAdapter.class.getSimpleName();

    private final SharedPreferences prefs;

    private final String accessToken;

    private final String username;

    final RepositoriesDao dao;

    boolean hasRecords = true;

    int pageSize = 100;

    int pageNumber = 1;

    /** Constructor. */
    public RepositorySyncAdapter(@NonNull Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dao = Abstraction.getInstance(context).repositoriesDao();
        this.accessToken = TokenHelper.getAccessToken(context);
        this.username = TokenHelper.getUsername(context);
    }

    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may be specified in extras,
     * which is guaranteed to not be null. Invocations of this method are guaranteed to be serialized.
     *
     * @param account    the account that should be synced
     * @param extras     SyncAdapter-specific parameters
     * @param authority  the authority of this sync request
     * @param provider   a ContentProviderClient that points to the ContentProvider for this authority
     * @param syncResult SyncAdapter-specific parameters
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (this.accessToken != null && this.username != null) {

            while (hasRecords) {

                Call<ArrayList<Repository>> api = GithubClient.getUserRepositories(accessToken, username, "owner", "full_name", "desc", pageSize, pageNumber);
                if (BuildConfig.DEBUG && this.prefs.getBoolean(Constants.PREFERENCE_KEY_DEBUG_LOGGING, false)) {
                    Log.d(LOG_TAG, api.request().url() + "");
                }

                api.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Repository>> call, @NonNull Response<ArrayList<Repository>> response) {
                        if (response.code() == 200) { // OK
                            if (response.body() != null) {

                                ArrayList<Repository> items = response.body();
                                if (items.size() == pageSize) {pageNumber++;}
                                else {hasRecords = false;}

                                for (Repository item : items) {
                                    Abstraction.executorService.execute(() -> {
                                        assert dao != null;
                                        if (dao.getItem(item.getId()) == null) {
                                            dao.insert(item);
                                        } else {
                                            dao.update(item);
                                        }
                                    });
                                }
                            }
                        } else {
                            /* "bad credentials" means that the provided access-token is invalid. */
                            if (response.errorBody() != null) {
                                logError(response.errorBody());
                                hasRecords = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Repository>> call, @NonNull Throwable t) {
                        if (BuildConfig.DEBUG) {
                            Log.e(LOG_TAG, "" + t.getMessage());
                        }
                    }
                });
            }
        }
    }

    void logError(@NonNull ResponseBody responseBody) {
        if (BuildConfig.DEBUG && this.prefs.getBoolean(Constants.PREFERENCE_KEY_DEBUG_LOGGING, false)) {
            try {
                String errors = responseBody.string();
                JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                Log.e(LOG_TAG, jsonObject.get("message").toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "" + e.getMessage());
            }
        }
    }
}