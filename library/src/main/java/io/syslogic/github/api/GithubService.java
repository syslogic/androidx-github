package io.syslogic.github.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import io.syslogic.github.api.model.Branch;
import io.syslogic.github.api.model.RateLimits;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.RepositorySearch;
import io.syslogic.github.api.model.User;

import io.syslogic.github.api.model.WorkflowJobsResponse;
import io.syslogic.github.api.model.WorkflowRunsResponse;
import io.syslogic.github.api.model.WorkflowsResponse;

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
 * @see <a href="https://developer.github.com/v3/">api v3</a>
 * @see <a href="https://developer.github.com/v3/rate_limit/">rate limit</a>
 * @see <a href="https://developer.github.com/v3/search/#search-repositories">search</a>
 * @see <a href="https://developer.github.com/v3/repos/branches/">branches</a>
 * @author Martin Zeitler
 */
public interface GithubService {

    /**
     * Get rate limits.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("rate_limit")
    Call<RateLimits> getRateLimits();

    /**
     * Search all Repositories.
     * @param token the personal access token.
     * @param queryString the query-string of the repository search.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
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
     * Organization repositories.
     * @param token the personal access token.
     * @param org the corresponding organization name.
     * @param type Default: all. Can be one of: all, public, private, forks, sources, member.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageSize The number of results per page (max 100). Default: 30.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
    @NonNull
    @SuppressWarnings("unused")
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

    /**
     * User repositories.
     * @param token the personal access token.
     * @param username the corresponding username.
     * @param type Default: all. Can be one of: all, public, private, forks, sources, member.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageSize The number of results per page (max 100). Default: 30.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("users/{username}/repos")
    Call<ArrayList<Repository>> getUserRepositories(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "username") String username,
            @NonNull @Query(value = "type") String type,
            @NonNull @Query(value = "sort") String sortField,
            @NonNull @Query(value = "direction") String sortOrder,
            @NonNull @Query(value = "per_page") Integer pageSize,
            @NonNull @Query(value = "page") Integer pageNumber
    );

    /**
     * One repository.
     * @param id the ID of the repository to get.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("repositories/{id}")
    Call<Repository> getRepository(
        @NonNull @Path(value = "id") Long id
    );

    /**
     * All branches of a repository.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("/repos/{owner}/{repo}/branches")
    Call<ArrayList<Branch>> getBranches(
        @NonNull @Path(value = "owner") String owner,
        @NonNull @Path(value = "repo")  String repo
    );

    /**
     * One branch of a repository.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param branch the name of the branch.
     * @return Retrofit2 call.
     */
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
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param format either zip or tar.
     * @param branch the ref, which to download.
     * @return Retrofit2 call.
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

    /**
     * It determines the filename to use with the DownloadManager.
     * @param url the url, which to download.
     * @return Retrofit2 call.
     */
    @NonNull
    @HEAD
    Call<Void> getHead(@NonNull @Url String url);

    /**
     * One user.
     * @param token the personal access token.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("user")
    Call<User> getUser(
        @NonNull @Header("Authorization") String token
    );

    /**
     * One user.
     * @param token the personal access token.
     * @param username the name of the user.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("user/{username}")
    Call<User> getUser(
        @NonNull @Header("Authorization") String token,
        @NonNull @Path(value = "name")    String username
    );

    /**
     * One user.
     * @param token the personal access token.
     * @return Retrofit2 call.
     */
    @NonNull
    @SuppressWarnings("unused")
    @GET("/user/repos")
    Call<RepositorySearch> getRepositories(
        @NonNull @Header("Authorization") String token
    );

    /**
     * GitHub Actions: Workflows per repository.
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("/repos/{owner}/{repo}/actions/workflows")
    Call<WorkflowsResponse> getWorkflows(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "owner") String owner,
            @NonNull @Path(value = "repo") String repo
    );

    /**
     * GitHub Actions: Workflow Runs.
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull
    @GET("/repos/{owner}/{repo}/actions/runs")
    Call<WorkflowRunsResponse> getWorkflowRuns(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "owner") String owner,
            @NonNull @Path(value = "repo") String repo
    );

    @NonNull
    @GET("/repos/{owner}/{repo}/actions/runs/{runId}/jobs")
    Call<WorkflowJobsResponse> getWorkflowJobs(
            @NonNull @Header("Authorization") String token,
            @NonNull @Path(value = "owner") String owner,
            @NonNull @Path(value = "repo") String repo,
            @NonNull @Path(value = "runId") Integer runId
    );
}
