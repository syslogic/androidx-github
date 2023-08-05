package io.syslogic.github.recyclerview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.CardviewWorkflowBinding;
import io.syslogic.github.databinding.FragmentWorkflowsBinding;
import io.syslogic.github.model.Repository;
import io.syslogic.github.model.Workflow;
import io.syslogic.github.model.WorkflowsResponse;
import io.syslogic.github.network.TokenHelper;
import io.syslogic.github.retrofit.GithubClient;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflows {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class WorkflowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = WorkflowsAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private final ArrayList<Repository> mItems = new ArrayList<>();

    private WeakReference<Context> mContext;

    public WorkflowsAdapter(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = new WeakReference<>(recyclerView.getContext());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewWorkflowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_workflow, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new WorkflowsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Repository item = getItem(position);
        CardviewWorkflowBinding binding = ((ViewHolder) viewHolder).getDataBinding();
        binding.setItem(item);
    }

    public void fetchPage(final int pageNumber) {

        /* It may add the account and therefore must be called first */
        String accessToken = getAccessToken();

        /*
         * This only returns a value on the second attempt, because the account
         * from which it gets the cached username is being added asynchronously).
         */
        String username = getUsername();
        if (username == null) {
            // TODO: try again with a delay?
            return;
        }

        Call<ArrayList<Repository>> api = GithubClient.getUserRepositories(accessToken, username,"owner", "full_name","desc", 100, pageNumber);
        if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Repository>> call, @NonNull Response<ArrayList<Repository>> response) {
                int positionStart = getItemCount();
                if (response.code() == 200) { // OK
                    if (response.body() != null) {

                        ArrayList<Repository> items = response.body();
                        for (Repository item : items) {

                            Call<WorkflowsResponse> api2 = GithubClient.getWorkflows(accessToken, username,item.getName());
                            // if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api2.request().url() + "");}
                            api2.enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<WorkflowsResponse> call, @NonNull Response<WorkflowsResponse> response) {
                                    if (response.code() == 200) { // OK
                                        if (response.body() != null) {
                                            WorkflowsResponse items = response.body();
                                            assert items.getWorkflows() != null;
                                            for (Workflow item : items.getWorkflows()) {
                                                if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "has workflow: " + item.getName());}
                                            }
                                        }
                                    } else {
                                        /* "bad credentials" means that the provided access-token is invalid. */
                                        if (response.errorBody() != null) {
                                            logError(response.errorBody());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<WorkflowsResponse> call, @NonNull Throwable t) {
                                    if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "onFailure: " + t.getMessage());}
                                }
                            });
                        }

                        getItems().addAll(items);
                        notifyItemRangeChanged(positionStart, getItemCount());
                    }
                } else {
                    /* "bad credentials" means that the provided access-token is invalid. */
                    if (response.errorBody() != null) {
                        logError(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Repository>> call, @NonNull Throwable t) {
                if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
    }

    /** Getters */
    private Repository getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    ArrayList<Repository> getItems() {
        return this.mItems;
    }

    /** The username is now being stored along with the token */
    @Nullable
    private String getUsername() {
        return TokenHelper.getUsername(getContext());
    }

    @Nullable
    private String getAccessToken() {
        Activity activity = (Activity) getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER) == PackageManager.PERMISSION_GRANTED) {
                return TokenHelper.getAccessToken(activity);
            } else {
                /* For testing purposes only: */
                if (mDebug) {return TokenHelper.getAccessToken(activity);}
                else {return null;}
            }
        } else {
            return TokenHelper.getAccessToken(activity);
        }
    }

    @NonNull
    protected Context getContext() {
        return this.mContext.get();
    }

    void logError(@NonNull ResponseBody responseBody) {
        try {
            String errors = responseBody.string();
            JsonObject jsonObject = JsonParser.parseString(errors).getAsJsonObject();
            Toast.makeText(getContext(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, jsonObject.get("message").toString());}
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + e.getMessage());}
        }
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Workflow}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewWorkflowBinding mDataBinding;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewWorkflowBinding binding) {
            super(binding.getRoot());
            binding.cardview.setOnClickListener(this);
            this.mDataBinding = binding;
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            WorkflowsLinearView mRecyclerView = (WorkflowsLinearView) viewHolder.getParent();
            BaseActivity activity = (BaseActivity) mRecyclerView.getContext();
            FragmentWorkflowsBinding databinding = (FragmentWorkflowsBinding) activity.getFragmentDataBinding();
            if (databinding != null) {
                View layout = databinding.getRoot();
                Repository item = getDataBinding().getItem();
                NavController controller = Navigation.findNavController(layout);
                Bundle args = new Bundle();
                args.putString(Constants.ARGUMENT_ITEM_NAME, item.getName());
                controller.navigate(R.id.action_workflowsFragment_to_workflowFragment, args);
            }
        }

        /** Getters */
        CardviewWorkflowBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
