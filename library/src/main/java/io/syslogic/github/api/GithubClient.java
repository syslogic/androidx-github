package io.syslogic.github.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import io.syslogic.github.api.model.Branch;
import io.syslogic.github.api.model.RateLimits;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.RepositorySearch;
import io.syslogic.github.api.model.User;
import io.syslogic.github.api.model.WorkflowJobsResponse;
import io.syslogic.github.api.model.WorkflowRun;
import io.syslogic.github.api.model.WorkflowRunsResponse;
import io.syslogic.github.api.model.WorkflowsResponse;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * GitHub API Client
 *
 * @author Martin Zeitler
 */
public class GithubClient {

    private static Retrofit retrofit;

    /** @return an instance of {@link GithubService}. */
    @NonNull private static GithubService getService() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                .setDateFormat(Constants.GITHUB_DATE_FORMAT)
                .create();

            OkHttpClient ok = new OkHttpClient.Builder()
                .followSslRedirects(false)
                .followRedirects(false)
                .build();

            retrofit = new retrofit2.Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.GITHUB_API_BASE_URL)
                .client(ok)
                .build();
        }
        return retrofit.create(GithubService.class);
    }

    /** @return Retrofit2 call. */
    @NonNull public static Call<RateLimits> getRateLimits() {
        return getService().getRateLimits();
    }

    /**
     * @param token the personal access token.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<User> getUser(@NonNull String token) {
        return getService().getUser("token " + token);
    }

    /**
     * @param token the personal access token.
     * @param username the name of the user to get.
     * @return Retrofit2 call.
     */
    @SuppressWarnings("unused")
    @NonNull public static Call<User> getUser(@NonNull String token, @NonNull String username) {
        return getService().getUser("token " + token, username);
    }

    /**
     * Search Repositories.
     * @param token the personal access token.
     * @param queryString the query-string of the repository search.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<RepositorySearch> searchRepositories(@Nullable String token, @NonNull String queryString, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageNumber) {
        return getService().searchRepositories("token " + token, queryString, sortField, sortOrder, pageNumber);
    }

    /**
     * Organization repositories
     * @param token the personal access token.
     * @param org the corresponding organization name.
     * @param type Default: all. Can be one of: all, public, private, forks, sources, member.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageSize The number of results per page (max 100). Default: 30.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
    @SuppressWarnings("unused")
    @NonNull public static Call<ArrayList<Repository>> getOrgRepositories(@Nullable String token, @NonNull String org, @NonNull String type, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageSize, @NonNull Integer pageNumber) {
        return getService().getOrgRepositories("token " + token, org, type, sortField, sortOrder, pageSize, pageNumber);
    }

    /**
     * User repositories
     * @param token the personal access token.
     * @param username the corresponding username.
     * @param type Default: all. Can be one of: all, public, private, forks, sources, member.
     * @param sortField Default: created. Can be one of: created, updated, pushed, full_name.
     * @param sortOrder Default: asc when using full_name, otherwise desc. Can be one of: asc, desc.
     * @param pageSize The number of results per page (max 100). Default: 30.
     * @param pageNumber Page number of the results to fetch. Default: 1.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<ArrayList<Repository>> getUserRepositories(@Nullable String token, @NonNull String username, @NonNull String type, @NonNull String sortField, @NonNull String sortOrder, @NonNull Integer pageSize, @NonNull Integer pageNumber) {
        return getService().getUserRepositories("token " + token, username, type, sortField, sortOrder, pageSize, pageNumber);
    }

    /**
     * One repository.
     * @param itemId the ID of the repository to get.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<Repository> getRepository(@NonNull Long itemId) {
        return getService().getRepository(itemId);
    }

    /**
     * All branches of a repository.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<ArrayList<Branch>> getBranches(@NonNull String owner, @NonNull String repo) {
        return getService().getBranches(owner, repo);
    }

    /**
     * One branch of a repository.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param branch the name of the branch.
     * @return Retrofit2 call.
     */
    @SuppressWarnings("unused")
    @NonNull public static Call<Branch> getBranch(@NonNull String owner, @NonNull String repo, @NonNull String branch) {
        return getService().getBranch(owner, repo, branch);
    }

    /**
     * Obtain the redirect URL to download an archive for a repository.
     * The :archive_format can be either "tarball" or "zipball".
     * The :branch must be a valid Git reference.
     *
     * <p>If you omit :branch, the repository’s default branch (usually master) will be used.
     * Note: for private repositories, the links are temporary and expire after five minutes.</p>
     *
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param format either "tarball" or "zipball".
     * @param branch the ref, which to download.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<ResponseBody> getArchiveLink(@NonNull String token, @NonNull String owner, @NonNull String repo, @NonNull String format, @NonNull String branch) {
        return getService().getArchiveLink("token " + token, owner, repo, format, branch);
    }

    /**
     * It determines the filename to use with the DownloadManager.
     * @param url the url, which to download.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<Void> getHead(@NonNull String url) {
        return getService().getHead(url);
    }

    /**
     * GitHub Actions: Workflows.
     * @see <a href="https://docs.github.com/en/github-ae@latest/rest/actions/workflows#list-repository-workflows">List repository workflows</a>
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<WorkflowsResponse> getWorkflows(@Nullable String token, @NonNull String owner, @NonNull String repo) {
        return getService().getWorkflows("token " + token, owner, repo);
    }

    /**
     * GitHub Actions: Workflow Runs.
     * @see <a href="https://docs.github.com/en/github-ae@latest/rest/actions/workflow-runs">List workflow runs for a repository</a>
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<WorkflowRunsResponse> getWorkflowRuns(@Nullable String token, @NonNull String owner, @NonNull String repo) {
        return getService().getWorkflowRuns("token " + token, owner, repo);
    }

    /**
     * GitHub Actions: Workflow Run.
     * @see <a href="https://docs.github.com/en/github-ae@latest/rest/actions/workflow-runs#get-a-workflow-run">Get a workflow run</a>
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param runId the ID of the run.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<WorkflowRun> getWorkflowRun(@Nullable String token, @NonNull String owner, @NonNull String repo, @NonNull Integer runId) {
        return getService().getWorkflowRun("token " + token, owner, repo, runId);
    }

    /**
     * GitHub Actions: Workflow Jobs.
     * @see <a href="https://docs.github.com/en/github-ae@latest/rest/actions/workflow-jobs#list-jobs-for-a-workflow-run">List jobs for a workflow run</a>
     * @param token the personal access token.
     * @param owner the owner of the repository.
     * @param repo the name of the repository.
     * @param runId the ID of the run to list jobs for.
     * @return Retrofit2 call.
     */
    @NonNull public static Call<WorkflowJobsResponse> getWorkflowJobs(@Nullable String token, @NonNull String owner, @NonNull String repo, @NonNull Integer runId) {
        return getService().getWorkflowJobs("token " + token, owner, repo, runId);
    }
}
