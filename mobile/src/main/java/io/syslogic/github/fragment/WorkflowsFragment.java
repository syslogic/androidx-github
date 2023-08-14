package io.syslogic.github.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.databinding.FragmentWorkflowsBinding;
import io.syslogic.github.menu.WorkflowsMenuProvider;
import io.syslogic.github.recyclerview.WorkflowsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflows {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class WorkflowsFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowsFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_workflows;

    /** Data-Binding */
    FragmentWorkflowsBinding mDataBinding;

    /** The repository's ID. */
    Long repositoryId = -1L;

    /** The repository's owner. */
    private String repositoryOwner;

    /** The repository's name. */
    private String repositoryName;

    /** Constructor */
    public WorkflowsFragment() {}

    @NonNull
    @SuppressWarnings("unused")
    public static WorkflowsFragment newInstance(long repositoryId) {
        WorkflowsFragment fragment = new WorkflowsFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARGUMENT_REPO_ID, repositoryId);
        fragment.setArguments(args);
        return fragment;
    }

    /** Note: when deep-linking, the parameters will be passed as type String. */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            if (args.keySet().contains("android-support-nav:controller:deepLinkIntent")) {
                //noinspection DataFlowIssue
                this.setRepositoryId(Long.valueOf(args.getString(Constants.ARGUMENT_REPO_ID)));
            } else {
                this.setRepositoryId(args.getLong(Constants.ARGUMENT_REPO_ID));
                this.setRepositoryOwner(args.getString(Constants.ARGUMENT_REPO_OWNER, null));
                this.setRepositoryName(args.getString(Constants.ARGUMENT_REPO_NAME, null));
            }
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentWorkflowsBinding.inflate(inflater, container, false));

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new WorkflowsMenuProvider(activity));

        // the SpinnerItem has the same ID as the QueryString.
        activity.setSupportActionBar(this.getDataBinding().toolbarWorkflows.toolbarWorkflows);
        this.mDataBinding.toolbarWorkflows.home.setOnClickListener(view -> activity.onBackPressed());

        // Recyclerview.Adapter
        WorkflowsAdapter adapter = new WorkflowsAdapter(requireContext());
        this.getDataBinding().recyclerviewWorkflows.setAdapter(adapter);

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else if (this.repositoryOwner != null && this.repositoryName != null) {
            /* No need to load the repository, when the owner and name are known. */
            adapter.getWorkflows(getAccessToken(), this.repositoryOwner, this.repositoryName);
        } else if (this.repositoryId != -1L) {
            /* Load the repository, when only the ID is known (eg. when started from deep-link intent). */
            this.getRepository(this.repositoryId);
        }
        return this.getDataBinding().getRoot();
    }

    private void getRepository(Long repositoryId) {
        if (repositoryId != 0L) {

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

                            if (mDataBinding.recyclerviewWorkflows.getAdapter() != null) {
                                WorkflowsAdapter adapter = ((WorkflowsAdapter) mDataBinding.recyclerviewWorkflows.getAdapter());
                                adapter.setRepositoryId(repositoryId);
                                adapter.getWorkflows(getAccessToken(), repositoryOwner, repositoryName);
                            }
                        }
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if (mDebug) {
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if (mDebug) {
                                    Log.e(LOG_TAG, "" + e.getMessage());
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
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

    @NonNull
    public FragmentWorkflowsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentWorkflowsBinding) binding;
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
