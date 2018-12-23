package io.syslogic.github.network;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.model.Branch;
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
 * @see <a href="https://developer.github.com/v3/repos/branches/">branches</>
**/
public interface GithubService {

    @NonNull
    @GET("rate_limit")
    Call<RateLimits> getRateLimits();

    /** all repositories. */
    @NonNull
    @GET("search/repositories")
    Call<Repositories> getRepositories(
        @NonNull @Query(value = "q", encoded = true) String queryString,
        @NonNull @Query(value = "sort")  String sortField,
        @NonNull @Query(value = "order") String sortOrder,
        @NonNull @Query(value = "page")  Integer pageNumber
    );

    /** one repository. */
    @NonNull
    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @NonNull @Path(value = "id") Long id
    );

    /** all branches of a repository. */
    @NonNull
    @GET("/repos/{owner}/{repo}/branches")
    Call<ArrayList<Branch>> getBranches(
        @NonNull @Path(value = "owner") String owner,
        @NonNull @Path(value = "repo")  String repo
    );


    /** one branch of a repository. */
    @NonNull
    @GET("/repos/{owner}/{repo}/branches/{repo}")
    Call<Branch> getBranch(
        @NonNull @Path(value = "owner")  String owner,
        @NonNull @Path(value = "repo")   String repo,
        @NonNull @Path(value = "branch") String branch
    );

    /**
     * gets the redirect URL to download an archive for a repository.
     * the :archive_format can be either "tarball" or "zipball".
     * the :branch must be a valid Git reference.
     * if you omit :branch, the repository’s default branch (usually master) will be used.
     * note: for private repositories, these links are temporary and expire after five minutes.
    **/
    @NonNull
    @Streaming
    @GET("/repos/{owner}/{repo}/{format}/{branch}")
    Call<ResponseBody> getArchiveLink(
        @NonNull @Path(value = "owner")   String owner,
        @NonNull @Path(value = "repo")    String repo,
        @NonNull @Path(value = "format")  String format,
        @Nullable @Path(value = "branch") String branch
    );
}
