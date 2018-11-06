package io.syslogic.github.retrofit;

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

    @GET("rate_limit")
    Call<RateLimits> getRateLimits();

    @GET("search/repositories")
    Call<Repositories> getRepositories(
        @Query("q")     String query,
        @Query("sort")  String sort,
        @Query("order") String order,
        @Query("page")  int pageNumber
    );

    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @Path(value = "id") long id
    );
}
