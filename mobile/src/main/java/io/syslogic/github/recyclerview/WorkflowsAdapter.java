package io.syslogic.github.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.Workflow;
import io.syslogic.github.api.model.WorkflowsResponse;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.databinding.CardviewWorkflowBinding;

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
    @NonNull @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowsAdapter.class.getSimpleName();
    static WeakReference<Context> mContext;
    List<Workflow> mItems = new ArrayList<>();
    Long repositoryId;

    public WorkflowsAdapter(@NonNull Context context) {
        mContext = new WeakReference<>(context);
        Abstraction.executorService.execute(() -> {
            // mItems = Abstraction.getInstance(getContext()).workflowsDao().getItems()
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewWorkflowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_workflow, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        Workflow item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setItem(item);
        ((ViewHolder) viewHolder).setId(item.getId());
    }

    private Workflow getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        if (this.mItems == null) {return 0;}
        return this.mItems.size();
    }

    List<Workflow> getItems() {
        return this.mItems;
    }

    void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    protected Context getContext() {
        return mContext.get();
    }

    public void setRepositoryId(Long value) {
        this.repositoryId = value;
    }

    public void getWorkflows(String accessToken, String username, String repositoryName) {

        Call<WorkflowsResponse> api = GithubClient.getWorkflows(accessToken, username, repositoryName);
        GithubClient.logUrl(LOG_TAG, api);

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WorkflowsResponse> call, @NonNull Response<WorkflowsResponse> response) {
                if (response.code() == 200) { // OK
                    if (response.body() != null) {
                        WorkflowsResponse items = response.body();
                        if (items.getWorkflows() != null && items.getWorkflows().size() > 0) {
                            mItems = items.getWorkflows();
                            for (Workflow item : mItems) {item.setRepositoryId(repositoryId);}
                            notifyItemRangeChanged(0, mItems.size());
                        }
                    }
                } else {
                    GithubClient.logError(LOG_TAG, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkflowsResponse> call, @NonNull Throwable t) {
                if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "onFailure: " + t.getMessage());}
            }
        });
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Workflow}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewWorkflowBinding mDataBinding;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewWorkflowBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView(layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            Workflow item = getDataBinding().getItem();
            BaseActivity activity = (BaseActivity) mContext.get();
            ViewDataBinding databinding = activity.getFragmentDataBinding();
            if (databinding != null) {
                Bundle args = new Bundle();
                args.putLong(Constants.ARGUMENT_REPO_ID, item.getRepositoryId());
                NavController controller = Navigation.findNavController(databinding.getRoot());
                controller.navigate(R.id.action_workflowFragment_to_workflowRunsFragment, args);
            }
        }

        /** Setters */
        public void setId(long value) {
            this.itemId = value;
        }
        void setCardView(CardView view) {
            this.cardView = view;
        }

        /** Getters */
        public long getId() {
            return this.itemId;
        }
        CardviewWorkflowBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
