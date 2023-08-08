package io.syslogic.github.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.databinding.FragmentWorkflowBinding;
import io.syslogic.github.provider.WorkflowMenuProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflow {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class WorkflowFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_workflow;

    /** Data-Binding */
    private FragmentWorkflowBinding mDataBinding;

    private Long itemId = -1L;

    /** Constructor */
    public WorkflowFragment() {}

    @NonNull
    public static WorkflowFragment newInstance(long itemId) {
        WorkflowFragment fragment = new WorkflowFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARGUMENT_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentWorkflowBinding.inflate(inflater, container, false));

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new WorkflowMenuProvider(activity));

        // the SpinnerItem has the same ID as the QueryString.
        activity.setSupportActionBar(this.getDataBinding().toolbarWorkflow.toolbarWorkflow);
        this.mDataBinding.toolbarWorkflow.home.setOnClickListener(view -> activity.onBackPressed());

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else if (itemId != -1L) {
            this.setRepository();
        }
        return this.getDataBinding().getRoot();
    }

    private void setRepository() {

        if (this.itemId != 0) {

            Call<Repository> api = GithubClient.getRepository(this.itemId);
            if (mDebug) {
                Log.w(LOG_TAG, api.request().url() + "");}

            api.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                    switch (response.code()) {
                        case 200 -> {
                            if (response.body() != null) {
                                Repository item = response.body();
                                mDataBinding.setRepository(item);
                            }
                        }
                        case 403 -> {
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
                                    if (mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {
                        Log.e(LOG_TAG, "" + t.getMessage());
                    }
                }
            });
        }
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public FragmentWorkflowBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentWorkflowBinding) binding;
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
