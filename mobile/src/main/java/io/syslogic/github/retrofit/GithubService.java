package io.syslogic.githubtrends.retrofit;

import io.syslogic.githubtrends.model.Repositories;
import io.syslogic.githubtrends.model.Repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @see <a href="https://developer.github.com/v3/">api v3</>
 * @see <a href="https://developer.github.com/v3/search/#search-repositories">search</>
**/
public interface GithubService {

    @GET("search/repositories")
    Call<Repositories> getRepositories(
        @Query(value = "q") String query,
        @Query(value = "sort") String sort,
        @Query(value = "order") String order,
        @Query(value = "page") int pageNumber
    );

    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @Path(value = "id") long id
    );
}
