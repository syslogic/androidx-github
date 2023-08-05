package io.syslogic.github.retrofit;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.model.Branch;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.RepositorySearch;
import io.syslogic.github.model.Repository;
import io.syslogic.github.model.User;
import io.syslogic.github.model.WorkflowJobs;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * GitHub API Service
 *
 * @see <a href="https://developer.github.com/v3/">api v3</>
 * @see <a href="https://developer.github.com/v3/rate_limit/">rate limit</>
 * @see <a href="https://developer.github.com/v3/search/#search-repositories">search</>
 * @see <a href="https://developer.github.com/v3/repos/branches/">branches</>
 * @author Martin Zeitler
 */
public interface GithubService {

    @NonNull
    @GET("rate_limit")
    Call<RateLimits> getRateLimits();

    /** Search all repositories */
    @NonNull
    @GET("search/repositories")
    Call<RepositorySearch> searchRepositories(
        @NonNull @Header("Authorization")            String token,
        @NonNull @Query(value = "q", encoded = true) String queryString,
        @NonNull @Query(value = "sort")              String sortField,
        @NonNull @Query(value = "order")             String sortOrder,
        @NonNull @Query(value = "page")              Integer pageNumber
    );

    /**
     * Organization repositories
     * @noinspection unused
     */
    @NonNull
    @GET("orgs/{org}/repos")
    Call<ArrayList<Repository>> getOrgRepositories(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "org") String org,
            @NonNull @Query(value = "type") String type, // Default: all. Can be one of: all, public, private, forks, sources, member
            @NonNull @Query(value = "sort") String sortField, // Default: created. Can be one of: created, updated, pushed, full_name
            @NonNull @Query(value = "direction") String sortOrder, // Default: asc when using full_name, otherwise desc. Can be one of: asc, desc
            @NonNull @Query(value = "per_page") Integer pageSize, // The number of results per page (max 100). Default: 30
            @NonNull @Query(value = "page") Integer pageNumber // Page number of the results to fetch. Default: 1
    );

    /** User repositories */
    @NonNull
    @GET("users/{username}/repos")
    Call<ArrayList<Repository>> getUserRepositories(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "username") String username,
            @NonNull @Query(value = "type") String type, // Default: all. Can be one of: all, public, private, forks, sources, member
            @NonNull @Query(value = "sort") String sortField, // Default: created. Can be one of: created, updated, pushed, full_name
            @NonNull @Query(value = "direction") String sortOrder, // Default: asc when using full_name, otherwise desc. Can be one of: asc, desc
            @NonNull @Query(value = "per_page") Integer pageSize, // The number of results per page (max 100). Default: 30
            @NonNull @Query(value = "page") Integer pageNumber // Page number of the results to fetch. Default: 1
    );

    /** One repository */
    @NonNull
    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @NonNull @Path(value = "id") Long id
    );

    /** All branches of a repository */
    @NonNull
    @GET("/repos/{owner}/{repo}/branches")
    Call<ArrayList<Branch>> getBranches(
        @NonNull @Path(value = "owner") String owner,
        @NonNull @Path(value = "repo")  String repo
    );

    /** One branch of a repository */
    @NonNull
    @GET("/repos/{owner}/{repo}/branches/{branch}")
    Call<Branch> getBranch(
        @NonNull @Path(value = "owner")  String owner,
        @NonNull @Path(value = "repo")   String repo,
        @NonNull @Path(value = "branch") String branch
    );

    /**
     * Obtain the redirect URL to download an archive for a repository.
     * The :archive_format can be either "tarball" or "zipball".
     * The :branch must be a valid Git reference.
     * <p>
     * If you omit :branch, the repository’s default branch (usually master) will be used.
     * Note: for private repositories, the links are temporary and expire after five minutes.
     * </p>
     */
    @NonNull
    @Streaming
    @GET("/repos/{owner}/{repo}/{format}/{branch}")
    Call<ResponseBody> getArchiveLink(
        @NonNull @Header("Authorization") String token,
        @NonNull  @Path(value = "owner")  String owner,
        @NonNull  @Path(value = "repo")   String repo,
        @NonNull  @Path(value = "format") String format,
        @Nullable @Path(value = "branch") String branch
    );

    /** It determines the filename to use with the DownloadManager. */
    @NonNull
    @HEAD
    Call<Void> getHead(@NonNull @Url String url);

    /** One user */
    @NonNull
    @GET("user")
    Call<User> getUser(
        @NonNull @Header("Authorization") String token
    );

    /** One user */
    @NonNull
    @GET("user/{username}")
    Call<User> getUser(
        @NonNull @Header("Authorization") String token,
        @NonNull @Path(value = "name")    String username
    );

    /** One user */
    @NonNull
    @SuppressWarnings("unused")
    @GET("/user/repos")
    Call<RepositorySearch> getRepositories(
        @NonNull @Header("Authorization") String token
    );

    /** GitHub Actions: Workflow Runs */
    @NonNull
    @GET("/repos/{owner}/{repo}/actions/jobs/{jobId}")
    Call<WorkflowJobs> getWorkflowRuns(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "owner") String owner,
            @NonNull @Path(value = "repo")  String repo,
            @NonNull @Path(value = "jobId") Integer jobId
    );
}
