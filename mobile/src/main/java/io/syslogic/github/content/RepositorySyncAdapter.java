package io.syslogic.github.content;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;
import io.syslogic.github.model.Topic;
import io.syslogic.github.model.User;
import io.syslogic.github.network.TokenHelper;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.RepositoriesDao;
import io.syslogic.github.room.TopicsDao;
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
    private User user;
    private List<Topic> topics;
    private ArrayList<Repository> repositories = new ArrayList<>();
    private final RepositoriesDao repositoriesDao;
    private final TopicsDao topicsDao;

    /** Constructor. */
    public RepositorySyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.accessToken = TokenHelper.getAccessToken(context);
        this.repositoriesDao = Abstraction.getInstance(context).repositoriesDao();
        this.topicsDao = Abstraction.getInstance(context).topicsDao();
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
            if (mDebug) {Log.d(LOG_TAG, api.request().url() + "");}
            api.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.body() != null) {
                        onUser(response.body());
                        Abstraction.executorService.execute(() -> onTopics(topicsDao.getItems()));
                    }
                }
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    private void onUser(@NonNull User item) {
        if (mDebug) {Log.d(LOG_TAG, "onUser: " + item.getLogin());}
        this.user = item;
    }

    private void onTopics(@NonNull List<Topic> items) {
        if (mDebug) {Log.d(LOG_TAG, "onTopics: " + items.size());}
        this.topics = items;
        for (Topic item: items) {
            if (mDebug) {Log.d(LOG_TAG, "building content cache for query-string parameter: " + item.toQueryString());}
            Call<Repositories> api = GithubClient.getRepositories(this.accessToken, item.toQueryString(), "forks", "desc", 1);
            if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}
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
        for (Repository repository: item.getRepositories()) {
            if (mDebug) {Log.d(LOG_TAG, "Repo: " + repository.getUrl());}
            this.repositories.add(repository);
        }
        if (mDebug) {
            Log.d(LOG_TAG, "expected item count: " + item.getTotalCount());
            Log.d(LOG_TAG, "expected page count: " + item.getPageCount());
            Log.d(LOG_TAG, "loaded so far: " + this.repositories.size());
        }
    }
}