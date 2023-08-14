package io.syslogic.github.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.WorkflowRun;
import io.syslogic.github.databinding.FragmentWorkflowRunBinding;
import io.syslogic.github.menu.WorkflowsMenuProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflow Run {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class WorkflowRunFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowRunFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_workflow_run;

    /** Data-Binding */
    FragmentWorkflowRunBinding mDataBinding;

    /** The repository's ID. */
    Long repositoryId = -1L;

    /** The repository's owner. */
    private String repositoryOwner;

    /** The repository's name. */
    private String repositoryName;

    /** The workflow run's ID. */
    private Long runId = -1L;

    /** Constructor */
    public WorkflowRunFragment() {}
    @NonNull
    @SuppressWarnings("unused")
    public static WorkflowRunFragment newInstance(long repositoryId) {
        WorkflowRunFragment fragment = new WorkflowRunFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARGUMENT_REPO_ID, repositoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            if (args.keySet().contains("android-support-nav:controller:deepLinkIntent")) {
                //noinspection DataFlowIssue
                this.setRepositoryId(Long.valueOf(args.getString(Constants.ARGUMENT_REPO_ID)));
                //noinspection DataFlowIssue
                this.setRunId(Long.valueOf(args.getString(Constants.ARGUMENT_RUN_ID)));
            } else {
                this.setRepositoryId(args.getLong(Constants.ARGUMENT_REPO_ID));
                this.setRunId(args.getLong(Constants.ARGUMENT_RUN_ID));
            }
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentWorkflowRunBinding.inflate(inflater, container, false));

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new WorkflowsMenuProvider(activity));

        activity.setSupportActionBar(this.getDataBinding().toolbarWorkflowRun.toolbarWorkflowRun);
        this.mDataBinding.toolbarWorkflowRun.home.setOnClickListener(view -> activity.onBackPressed());

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else if (this.repositoryOwner != null && this.repositoryName != null) {
            /* No need to load the repository, when the owner and name are known. */
            this.getWorkflowRun();
        } else if (this.repositoryId != -1L) {
            /* Load the repository, when only the ID is known (eg. when started from deep-link intent). */
            this.getRepository(this.repositoryId);
        }

        return this.getDataBinding().getRoot();
    }

    private void getRepository(Long repositoryId) {
        if (repositoryId != 0) {

            Call<Repository> api = GithubClient.getRepository(repositoryId);
            GithubClient.logUrl(LOG_TAG, api);

            api.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            Repository item = response.body();
                            mDataBinding.setRepository(item);

                            /* Filling in the blanks. */
                            repositoryOwner = item.getOwner().getLogin();
                            repositoryName = item.getName();
                            getWorkflowRun();
                        }
                    } else {
                        GithubClient.logError(LOG_TAG, response);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    private void getWorkflowRun() {

        Call<WorkflowRun> api = GithubClient.getWorkflowRun(getAccessToken(), repositoryOwner, repositoryName, getRunId());
        GithubClient.logUrl(LOG_TAG, api);

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WorkflowRun> call, @NonNull Response<WorkflowRun> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        WorkflowRun item = response.body();
                        mDataBinding.setRun(item);
                    }
                } else {
                    GithubClient.logError(LOG_TAG, response);
                }
            }
            @Override
            public void onFailure(@NonNull Call<WorkflowRun> call, @NonNull Throwable t) {
                if (mDebug) {
                    Log.e(LOG_TAG, "" + t.getMessage());
                }
            }
        });
    }

    @NonNull
    public Long getRunId() {
        return this.runId;
    }

    private void setRepositoryId(@NonNull Long value) {
        this.repositoryId = value;
    }
    private void setRunId(@NonNull Long value) {
        this.runId = value;
    }

    @NonNull
    public FragmentWorkflowRunBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentWorkflowRunBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
    }
}
