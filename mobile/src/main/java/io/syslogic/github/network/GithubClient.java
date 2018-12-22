package io.syslogic.github.network;

import androidx.annotation.NonNull;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.syslogic.github.constants.Constants;

public class GithubClient {

    private static Retrofit retrofit;

    private static GithubService getService() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Constants.GITHUB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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

    public static @NonNull Call<ResponseBody> getArchiveLink(@NonNull String owner, @NonNull String repo, @NonNull String format, @NonNull String ref) {
        return getService().getArchiveLink(owner, repo, format, ref);
    }
}
