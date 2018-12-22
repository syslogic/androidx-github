package io.syslogic.github.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

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

    /**
     * Gets a redirect URL to download an archive for a repository.
     * The :archive_format can be either tarball or zipball.
     * The :ref must be a valid Git reference.
     * If you omit :ref, the repository’s default branch (usually master) will be used.
     * Please make sure your HTTP framework is configured to follow redirects -
     * or you will need to use the Location header to make a second GET request.
     *
     * Note: For private repositories, these links are temporary and expire after five minutes.
    **/
    @NonNull
    @Streaming
    @GET("/repos/{owner}/{repo}/{format}/{ref}")
    Call<ResponseBody> getArchiveLink(
        @NonNull @Path(value = "owner")  String owner,
        @NonNull @Path(value = "repo")   String repo,
        @NonNull @Path(value = "format") String format,
        @Nullable @Path(value = "ref")   String ref
    );
}
