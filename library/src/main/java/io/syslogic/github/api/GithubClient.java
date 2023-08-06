package io.syslogic.github.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.syslogic.github.api.model.Branch;
import io.syslogic.github.api.model.RateLimits;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.RepositorySearch;
import io.syslogic.github.api.model.User;
import io.syslogic.github.api.model.WorkflowsResponse;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * GitHub API Client
 *
 * @author Martin Zeitler
 */
public class GithubClient {

    private static Retrofit retrofit;

    @NonNull
    private static GithubService getService() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                .setDateFormat(Constants.GITHUB_DATE_FORMAT)
                .create();

            OkHttpClient ok = new OkHttpClient.Builder()
                .followSslRedirects(false)
                .followRedirects(false)
                .build();

            retrofit = new retrofit2.Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.GITHUB_API_BASE_URL)
                .client(ok)
                .build();
        }
        return retrofit.create(GithubService.class);
    }

    public static @NonNull Call<RateLimits> getRateLimits() {
        return getService().getRateLimits();
    }

    public static @NonNull Call<User> getUser(@NonNull String token) {
        return getService().getUser("token " + token);
    }

    @SuppressWarnings("unused")
    public static @NonNull Call<User> getUser(@NonNull String username, @NonNull String token) {
        return getService().getUser("token " + token, username);
    }

    public static @NonNull Call<RepositorySearch> searchRepositories(@Nullable String token, @NonNull String queryString, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageNumber) {
        return getService().searchRepositories("token " + token, queryString, sortField, sortOrder, pageNumber);
    }

    @SuppressWarnings("unused")
    public static @NonNull Call<ArrayList<Repository>> getOrgRepositories(@Nullable String token, @NonNull String org, @NonNull String type, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageSize, @NonNull Integer pageNumber) {
        return getService().getOrgRepositories("token " + token, org, type, sortField, sortOrder, pageSize, pageNumber);
    }

    public static @NonNull Call<ArrayList<Repository>> getUserRepositories(@Nullable String token, @NonNull String username, @NonNull String type, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageSize, @NonNull Integer pageNumber) {
        return getService().getUserRepositories("token " + token, username, type, sortField, sortOrder, pageSize, pageNumber);
    }

    public static @NonNull Call<Repository> getRepository(@NonNull Long itemId) {
        return getService().getRepository(itemId);
    }

    public static @NonNull Call<ArrayList<Branch>> getBranches(@NonNull String owner, @NonNull String repo) {
        return getService().getBranches(owner, repo);
    }

    @SuppressWarnings("unused")
    public static @NonNull Call<Branch> getBranch(@NonNull String owner, @NonNull String repo, @NonNull String branch) {
        return getService().getBranch(owner, repo, branch);
    }

    public static @NonNull Call<WorkflowsResponse> getWorkflows(@Nullable String token, @NonNull String owner, @NonNull String repo) {
        return getService().getWorkflows("token " + token, owner, repo);
    }

    public static @NonNull Call<ResponseBody> getArchiveLink(@NonNull String token, @NonNull String owner, @NonNull String repo, @NonNull String format, @NonNull String ref) {
        return getService().getArchiveLink("token " + token, owner, repo, format, ref);
    }

    public static @NonNull Call<Void> getHead(@NonNull String url) {
        return getService().getHead(url);
    }
}
