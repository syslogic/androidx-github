package io.syslogic.github.retrofit;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.model.Branch;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
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

    /** All repositories */
    @NonNull
    @GET("search/repositories")
    Call<Repositories> getRepositories(
        @NonNull @Header("Authorization")            String token,
        @NonNull @Query(value = "q", encoded = true) String queryString,
        @NonNull @Query(value = "sort")              String sortField,
        @NonNull @Query(value = "order")             String sortOrder,
        @NonNull @Query(value = "page")              Integer pageNumber
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
     *
     * If you omit :branch, the repository’s default branch (usually master) will be used.
     * Note: for private repositories, the links are temporary and expire after five minutes.
    **/
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
    Call<Repositories> getRepositories(
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
