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
import io.syslogic.github.databinding.CardviewRepositoryBinding;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.api.model.Repository;
import io.syslogic.github.api.model.Workflow;
import io.syslogic.github.api.model.WorkflowsResponse;
import io.syslogic.github.network.TokenHelper;
import io.syslogic.github.api.GithubClient;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repositories {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = RepositoriesAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private final ArrayList<Repository> mItems = new ArrayList<>();

    private WeakReference<Context> mContext;

    /** This may add the account in debug mode and therefore must be called first. */
    private String accessToken = null;

    /**
     * This only returns a value on the second attempt, because the account
     * from which it gets the cached username is being added asynchronously).
     */
    private String username = null;

    public RepositoriesAdapter(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
        this.getCredentials();
        this.fetchPage(1);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = new WeakReference<>(recyclerView.getContext());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewRepositoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_repository, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RepositoriesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Repository item = getItem(position);
        CardviewRepositoryBinding binding = ((ViewHolder) viewHolder).getDataBinding();
        binding.setItem(item);
    }

    private boolean getCredentials() {

        /* This may add the account in debug mode and therefore must be called first. */
        this.accessToken = getAccessToken();

        /*
         * This only returns a value on the second attempt, because the account
         * from which it gets the cached username is being added asynchronously).
         */
        this.username = getUsername();

        return this.accessToken != null && this.username != null;
    }

    public void fetchPage(final int pageNumber) {

        Call<ArrayList<Repository>> api = GithubClient.getUserRepositories(accessToken, username,"owner", "full_name","desc", 100, pageNumber);
        if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Repository>> call, @NonNull Response<ArrayList<Repository>> response) {
                int positionStart = getItemCount();
                if (response.code() == 200) { // OK
                    if (response.body() != null) {

                        /* Updating the adapter with the initial response already. */
                        ArrayList<Repository> items = response.body();
                        getItems().addAll(items);
                        notifyItemRangeChanged(positionStart, getItemCount());


                        getWorkflows();
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

    private void getWorkflows() {
        for (Repository item : this.getItems()) {

            Call<WorkflowsResponse> api2 = GithubClient.getWorkflows(accessToken, username,item.getName());
            if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api2.request().url() + "");}

            api2.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<WorkflowsResponse> call, @NonNull Response<WorkflowsResponse> response) {
                    if (response.code() == 200) { // OK
                        if (response.body() != null) {
                            WorkflowsResponse items = response.body();
                            if (BuildConfig.DEBUG) {
                                if (items.getWorkflows() != null && items.getWorkflows().size() > 0) {
                                    for (Workflow item2 : items.getWorkflows()) {
                                        Log.d(LOG_TAG, item.getName() + " has workflow: " + item2.getName());
                                    }
                                }
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

        private final CardviewRepositoryBinding mDataBinding;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewRepositoryBinding binding) {
            super(binding.getRoot());
            binding.cardview.setOnClickListener(this);
            this.mDataBinding = binding;
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            RepositoriesLinearView mRecyclerView = (RepositoriesLinearView) viewHolder.getParent();
            BaseActivity activity = (BaseActivity) mRecyclerView.getContext();
            FragmentRepositoriesBinding databinding = (FragmentRepositoriesBinding) activity.getFragmentDataBinding();
            if (databinding != null) {
                View layout = databinding.getRoot();
                Repository item = getDataBinding().getItem();
                NavController controller = Navigation.findNavController(layout);
                Bundle args = new Bundle();
                args.putString(Constants.ARGUMENT_ITEM_NAME, item.getName());
                controller.navigate(R.id.action_repositoriesFragment_to_workflowFragment, args);
            }
        }

        /** Getters */
        CardviewRepositoryBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
