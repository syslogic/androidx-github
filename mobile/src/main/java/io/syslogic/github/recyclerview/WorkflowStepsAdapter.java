package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.api.GithubClient;
import io.syslogic.github.api.model.WorkflowJobsResponse;
import io.syslogic.github.api.model.WorkflowStep;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.databinding.CardviewWorkflowStepBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Workflow Steps {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class WorkflowStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull @SuppressWarnings("unused") private static final String LOG_TAG = WorkflowStepsAdapter.class.getSimpleName();
    private static WeakReference<Context> mContext;
    List<WorkflowStep> mItems = new ArrayList<>();

    public WorkflowStepsAdapter(@NonNull Context context) {
        mContext = new WeakReference<>(context);
        Abstraction.executorService.execute(() -> {
            // mItems = Abstraction.getInstance(getContext()).workflowStepsDao().getItems()
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewWorkflowStepBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_workflow_step, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        WorkflowStep item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setItem(item);
        ((ViewHolder) viewHolder).setId(item.getNumber());
    }

    private WorkflowStep getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        if (this.mItems == null) {return 0;}
        return this.mItems.size();
    }

    List<WorkflowStep> getItems() {
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

    public void getWorkflowSteps(String accessToken, String username, String repositoryName, Long runId) {

        Call<WorkflowJobsResponse> api = GithubClient.getWorkflowJobs(accessToken, username, repositoryName, runId);
        GithubClient.logUrl(LOG_TAG, api);

        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WorkflowJobsResponse> call, @NonNull Response<WorkflowJobsResponse> response) {
                if (response.code() == 200) { // OK
                    if (response.body() != null) {
                        WorkflowJobsResponse items = response.body();
                        if (items.getJobs() != null && items.getJobs().get(0).getSteps().size() > 0) {
                            setItems(items.getJobs().get(0).getSteps());
                            notifyItemRangeChanged(0, mItems.size());
                        }
                    }
                } else {
                    GithubClient.logError(LOG_TAG, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WorkflowJobsResponse> call, @NonNull Throwable t) {
                if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "onFailure: " + t.getMessage());}
            }
        });
    }

    private void setItems(@NonNull ArrayList<WorkflowStep> items) {
        this.mItems = items;
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link WorkflowStep}. */
    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardviewWorkflowStepBinding mDataBinding;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewWorkflowStepBinding binding) {
            super(binding.getRoot());
            this.mDataBinding = binding;
        }

        /** Setters */
        public void setId(long value) {
            this.itemId = value;
        }

        /** Getters */
        public long getId() {
            return this.itemId;
        }
        CardviewWorkflowStepBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
