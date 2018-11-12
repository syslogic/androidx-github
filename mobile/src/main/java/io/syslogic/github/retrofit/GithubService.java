package io.syslogic.github.retrofit;

import androidx.annotation.NonNull;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @see <a href="https://developer.github.com/v3/">api v3</>
 * @see <a href="https://developer.github.com/v3/rate_limit/">rate limit</>
 * @see <a href="https://developer.github.com/v3/search/#search-repositories">search</>
**/
public interface GithubService {

    @NonNull
    @GET("rate_limit")
    Call<RateLimits> getRateLimits();

    @NonNull
    @GET("search/repositories")
    Call<Repositories> getRepositories(
        @NonNull @Query(value = "q", encoded = true) String queryString,
        @NonNull @Query(value = "sort") String sortField,
        @NonNull @Query(value = "order") String sortOrder,
        @NonNull @Query(value = "page") Integer pageNumber
    );

    @NonNull
    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @NonNull @Path(value = "id") Long id
    );
}
