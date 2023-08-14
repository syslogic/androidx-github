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
import io.syslogic.github.databinding.FragmentWorkflowJobsBinding;
import io.syslogic.github.menu.WorkflowsMenuProvider;
import io.syslogic.github.recyclerview.WorkflowStepsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflow Run {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class WorkflowJobsFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowJobsFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_workflow_jobs;

    /** Data-Binding */
    FragmentWorkflowJobsBinding mDataBinding;

    /** The repository's ID. */
    Long repositoryId = -1L;

    /** The repository's owner. */
    private String repositoryOwner;

    /** The repository's name. */
    private String repositoryName;

    /** The workflow run's ID. */
    private Long runId = -1L;

    /** Constructor */
    public WorkflowJobsFragment() {}
    @NonNull
    @SuppressWarnings("unused")
    public static WorkflowJobsFragment newInstance(long repositoryId) {
        WorkflowJobsFragment fragment = new WorkflowJobsFragment();
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
                this.setRunId(args.getLong(Constants.ARGUMENT_RUN_ID));
            } else {
                this.setRepositoryId(args.getLong(Constants.ARGUMENT_REPO_ID));
                this.setRepositoryOwner(args.getString(Constants.ARGUMENT_REPO_OWNER, null));
                this.setRepositoryName(args.getString(Constants.ARGUMENT_REPO_NAME, null));
                this.setRunId(args.getLong(Constants.ARGUMENT_RUN_ID));
            }
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentWorkflowJobsBinding.inflate(inflater, container, false));

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new WorkflowsMenuProvider(activity));

        activity.setSupportActionBar(this.getDataBinding().toolbarWorkflowJobs.toolbarWorkflowJobs);
        this.mDataBinding.toolbarWorkflowJobs.home.setOnClickListener(view -> activity.onBackPressed());

        // Recyclerview.Adapter
        WorkflowStepsAdapter adapter = new WorkflowStepsAdapter(requireContext());
        this.getDataBinding().recyclerviewWorkflowSteps.setAdapter(adapter);

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else if (this.repositoryOwner != null && this.repositoryName != null) {
            /* No need to load the repository, when the owner, name and runId are known. */
            adapter.getWorkflowSteps(getAccessToken(), this.repositoryOwner, this.repositoryName, this.runId);
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

                            WorkflowStepsAdapter adapter = ((WorkflowStepsAdapter) mDataBinding.recyclerviewWorkflowSteps.getAdapter());
                            if (adapter != null) {
                                adapter.getWorkflowSteps(getAccessToken(), repositoryOwner, repositoryName, getRunId());
                            }
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

    @NonNull
    public Long getRunId() {
        return this.runId;
    }

    private void setRepositoryId(@NonNull Long value) {
        this.repositoryId = value;
    }

    private void setRepositoryOwner(@NonNull String value) {
        this.repositoryOwner = value;
    }

    private void setRepositoryName(@NonNull String value) {
        this.repositoryName = value;
    }

    private void setRunId(@NonNull Long value) {
        this.runId = value;
    }

    @NonNull
    public FragmentWorkflowJobsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentWorkflowJobsBinding) binding;
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
