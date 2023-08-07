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
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.RepositorySearch;
import io.syslogic.github.api.model.User;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.QueryStringsDao;
import io.syslogic.github.api.room.RepositoriesDao;
import io.syslogic.github.network.TokenHelper;

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
    @NonNull
    private static final String LOG_TAG = RepositorySyncAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    final QueryStringsDao queryStringsDao;
    final RepositoriesDao repositoriesDao;

    private final String accessToken;
    private final SharedPreferences prefs;

    private ArrayList<Repository> repositories = new ArrayList<>();

    /** Constructor. */
    public RepositorySyncAdapter(@NonNull Context context, boolean autoInitialize, boolean allowParallelSyncs) {
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
                        Abstraction.executorService.execute(() -> {
                            assert queryStringsDao != null;
                            onQueryStrings(queryStringsDao.getItems());
                        });
                    }
                }
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    void onUser(@NonNull User item) {
        this.log("onUser: " + item.getLogin());
    }

    void onQueryStrings(@NonNull List<QueryString> items) {
        this.log("onQueryStrings: " + items.size());
        for (QueryString item: items) {
            Call<RepositorySearch> api = GithubClient.searchRepositories(this.accessToken, item.toQueryString(), "forks", "desc", 1);
            this.log("Building content cache for query-string: " + item.toQueryString());
            this.log("" + api.request().url());
            api.enqueue(new Callback<RepositorySearch>() {
                @Override
                public void onResponse(@NonNull Call<RepositorySearch> call, @NonNull Response<RepositorySearch> response) {
                    if (response.body() != null) {onRepositories(response.body());}
                }
                @Override
                public void onFailure(@NonNull Call<RepositorySearch> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    void onRepositories(@NonNull RepositorySearch item) {
        this.log("onRepositories: " + item.getRepositories().size());
        for (Repository repository: item.getRepositories()) {
            this.log("Repo: " + repository.getUrl());
            this.repositories.add(repository);
        }
        this.log("expected item count: " + item.getTotalCount());
        this.log("expected page count: " + item.getPageCount());
        this.log("loaded so far: " + this.repositories.size());
    }

    void log(String message) {
        if (mDebug && this.prefs.getBoolean(Constants.PREFERENCE_KEY_DEBUG_LOGGING, false)) {
            Log.d(LOG_TAG, message);
        }
    }
}