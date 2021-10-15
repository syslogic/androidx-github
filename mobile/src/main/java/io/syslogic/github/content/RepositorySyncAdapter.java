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

import java.util.ArrayList;
import java.util.List;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.model.QueryString;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;
import io.syslogic.github.model.User;
import io.syslogic.github.network.TokenHelper;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.QueryStringsDao;
import io.syslogic.github.room.RepositoriesDao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository {@link AbstractThreadedSyncAdapter}
 * @author Martin Zeitler
 */
public class RepositorySyncAdapter extends AbstractThreadedSyncAdapter {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = RepositorySyncAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private final String accessToken;
    private final SharedPreferences prefs;
    private final QueryStringsDao queryStringsDao;
    private final RepositoriesDao repositoriesDao;

    private ArrayList<Repository> repositories = new ArrayList<>();
    private List<QueryString> queryStrings;
    private User user;

    /** Constructor. */
    public RepositorySyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.accessToken = TokenHelper.getAccessToken(context);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.repositoriesDao = Abstraction.getInstance(context).repositoriesDao();
        this.queryStringsDao = Abstraction.getInstance(context).queryStringsDao();
        if (mDebug && this.prefs.getBoolean(Constants.PREFERENCE_KEY_DEBUG_LOGGING, false)) {
            Log.d(LOG_TAG, "Verbose logging is active.");
        }
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
        if (this.accessToken != null) {
            Call<User> api = GithubClient.getUser(this.accessToken);
            this.log("" + api.request().url());
            api.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.body() != null) {
                        onUser(response.body());
                        Abstraction.executorService.execute(() -> onQueryStrings(queryStringsDao.getItems()));
                    }
                }
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    private void onUser(@NonNull User item) {
        this.log("onUser: " + item.getLogin());
        this.user = item;
    }

    private void onQueryStrings(@NonNull List<QueryString> items) {
        this.log("onQueryStrings: " + items.size());
        this.queryStrings = items;
        for (QueryString item: items) {
            Call<Repositories> api = GithubClient.getRepositories(this.accessToken, item.toQueryString(), "forks", "desc", 1);
            this.log("Building content cache for query-string: " + item.toQueryString());
            this.log("" + api.request().url());
            api.enqueue(new Callback<Repositories>() {
                @Override
                public void onResponse(@NonNull Call<Repositories> call, @NonNull Response<Repositories> response) {
                    if (response.body() != null) {onRepositories(response.body());}
                }
                @Override
                public void onFailure(@NonNull Call<Repositories> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    private void onRepositories(@NonNull Repositories item) {
        this.log("onRepositories: " + item.getRepositories().size());
        for (Repository repository: item.getRepositories()) {
            this.log("Repo: " + repository.getUrl());
            this.repositories.add(repository);
        }
        this.log("expected item count: " + item.getTotalCount());
        this.log("expected page count: " + item.getPageCount());
        this.log("loaded so far: " + this.repositories.size());
    }

    private void log(String message) {
        if (mDebug && this.prefs.getBoolean(Constants.PREFERENCE_KEY_DEBUG_LOGGING, false)) {
            Log.d(LOG_TAG, message);
        }
    }
}