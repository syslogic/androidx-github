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
import java.util.List;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.model.Workflow;
import io.syslogic.github.api.model.WorkflowRun;
import io.syslogic.github.api.model.WorkflowRunsResponse;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.databinding.CardviewWorkflowRunBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflow Runs {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class WorkflowRunsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowRunsAdapter.class.getSimpleName();
    private WeakReference<Context> mContext;
    private List<WorkflowRun> mItems;

    public WorkflowRunsAdapter(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
        Abstraction.executorService.execute(() -> {
            // mItems = Abstraction.getInstance(getContext()).workflowRunsDao().getItems()
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewWorkflowRunBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_workflow_run, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        WorkflowRun item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setItem(item);
        ((ViewHolder) viewHolder).setId(item.getId());
    }

    private WorkflowRun getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        if (this.mItems == null) {return 0;}
        return this.mItems.size();
    }

    List<WorkflowRun> getItems() {
        return this.mItems;
    }

    void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    protected Context getContext() {
        return this.mContext.get();
    }

    public void getWorkflowRuns(String accessToken, String username, String repositoryName) {

        Call<WorkflowRunsResponse> api = GithubClient.getWorkflowRuns(accessToken, username, repositoryName);
        if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WorkflowRunsResponse> call, @NonNull Response<WorkflowRunsResponse> response) {
                if (response.code() == 200) { // OK
                    if (response.body() != null) {
                        WorkflowRunsResponse items = response.body();
                        if (items.getWorkflowRuns() != null && items.getWorkflowRuns().size() > 0) {
                            mItems = items.getWorkflowRuns();
                            notifyItemRangeChanged(0, mItems.size());
                        }
                    }
                } else {
                    /* "bad credentials" means that the provided access-token is invalid. */
                    if (response.errorBody() != null) {
                        // logError(response.errorBody());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkflowRunsResponse> call, @NonNull Throwable t) {
                if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "onFailure: " + t.getMessage());}
            }
        });
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link QueryString}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewWorkflowRunBinding mDataBinding;
        private WorkflowsLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewWorkflowRunBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView(layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            this.mRecyclerView = (WorkflowsLinearView) viewHolder.getParent();
            Workflow item = (Workflow) viewHolder.getTag();
            BaseActivity activity = (BaseActivity) this.mRecyclerView.getContext();
            ViewDataBinding databinding = activity.getFragmentDataBinding();
            if (databinding != null) {
                Bundle args = new Bundle();
                args.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
                NavController controller = Navigation.findNavController(databinding.getRoot());
                // controller.navigate(R.id.action_workflowFragment_to_someFragment, args);
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
        CardviewWorkflowRunBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
