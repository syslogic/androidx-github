package io.syslogic.github.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.syslogic.github.model.Branch;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;

import io.syslogic.github.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.syslogic.github.constants.Constants;

/**
 * Github Client
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class GithubClient {

    private static Retrofit retrofit;

    private static GithubService getService() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Constants.GITHUB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        }
        return retrofit.create(GithubService.class);
    }

    public static @NonNull Call<RateLimits> getRateLimits() {
        return getService().getRateLimits();
    }

    public static @NonNull Call<Repositories> getRepositories(@NonNull String queryString, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageNumber) {
        return getService().getRepositories(queryString, sortField, sortOrder, pageNumber);
    }

    public static @NonNull Call<Repository> getRepository(@NonNull Long itemId) {
        return getService().getRepository(itemId);
    }

    public static @NonNull Call<ArrayList<Branch>> getBranches(@NonNull String owner, @NonNull String repo) {
        return getService().getBranches(owner, repo);
    }

    public static @NonNull Call<Branch> getBranch(@NonNull String owner, @NonNull String repo, @NonNull String branch) {
        return getService().getBranch(owner, repo, branch);
    }

    public static @NonNull Call<ResponseBody> getArchiveLink(@NonNull String owner, @NonNull String repo, @NonNull String format, @NonNull String ref) {
        return getService().getArchiveLink(owner, repo, format, ref);
    }

    public static @NonNull Call<User> getUser(@NonNull String token) {
        return getService().getUser(token);
    }

    public static @NonNull Call<User> getUser(@NonNull String user, @NonNull String token) {
        return getService().getUser(user, token);
    }
}
