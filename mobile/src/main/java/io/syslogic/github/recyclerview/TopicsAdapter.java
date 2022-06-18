package io.syslogic.github.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.CardviewTopicBinding;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.PagerState;

/**
 * Repository Topics {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class TopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mItems;

    public TopicsAdapter(@NonNull List<String> items) {
        this.mItems = items;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewTopicBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_topic, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        String item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setItem(item);
    }

    private String getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link String}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewTopicBinding mDataBinding;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewTopicBinding binding) {
            super(binding.getRoot());
            binding.cardview.setOnClickListener(this);
            this.mDataBinding = binding;
        }

        @Override
        public void onClick(@NonNull View viewHolder) {

            TopicsFlexboxView mRecyclerView = (TopicsFlexboxView) viewHolder.getParent();
            String item = (String) viewHolder.getTag();
            BaseActivity activity = (BaseActivity) mRecyclerView.getContext();
            FragmentRepositoriesBinding databinding = (FragmentRepositoriesBinding) activity.getFragmentDataBinding();

            /* TODO: navigate... */
            if (databinding != null) {
                ScrollListener.setPageNumber(1);

                String queryString = "topic:" + item; // TODO
                databinding.recyclerviewRepositories.setQueryString(queryString);

                PagerState pagerState = databinding.getPagerState();
                if (pagerState != null) {
                    pagerState.setQueryString(queryString);
                    databinding.setPagerState(pagerState);
                }
                databinding.toolbarRepositories.spinnerQueryString.setVisibility(View.INVISIBLE);
                if (databinding.toolbarRepositories.spinnerQueryString.getAdapter() != null) {
                    // databinding.toolbarRepositories.spinnerQueryString.getAdapter()
                }
                if (databinding.recyclerviewRepositories.getAdapter() != null) {
                    databinding.recyclerviewRepositories.clearAdapter();
                    ((RepositoriesAdapter) databinding.recyclerviewRepositories.getAdapter()).fetchPage(1);
                }
            }
        }

        CardviewTopicBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
