package io.syslogic.github.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.Constants;
import io.syslogic.github.databinding.CardviewTopicBinding;
import io.syslogic.github.model.Topic;
import io.syslogic.github.room.Abstraction;

/**
 * Topics RecyclerView Adapter
 * @author Martin Zeitler
 */
public class TopicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = TopicsAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private WeakReference<Context> mContext;

    private RecyclerView mRecyclerView;

    private long totalItemCount = 0;

    private List<Topic> mItems;

    private String queryString = "topic:";

    public TopicsAdapter(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
        Abstraction.executorService.execute(() -> mItems = Abstraction.getInstance(getContext()).topicsDao().getItems());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewTopicBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_topic, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        final Topic item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setTopic(item);
        ((ViewHolder) viewHolder).setId(item.getId());
        ((ViewHolder) viewHolder).setTag(item);
    }

    private Topic getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        if (this.mItems == null) {return 0;}
        return this.mItems.size();
    }

    List<Topic> getItems() {
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

    /** Setters */
    void setTotalItemCount(long value) {
        this.totalItemCount = value;
    }
    void setQueryString(String value) {
        this.queryString = value;
    }

    /** Getters */
    private long getTotalItemCount() {
        return this.totalItemCount;
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Topic}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewTopicBinding mDataBinding;
        private TopicsLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewTopicBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView((CardView) layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            this.mRecyclerView = (TopicsLinearView) viewHolder.getParent();
            Topic item = (Topic) viewHolder.getTag();
            BaseActivity activity = (BaseActivity) this.mRecyclerView.getContext();
            ViewDataBinding databinding = activity.getFragmentDataBinding();
            if (databinding != null) {
                Bundle args = new Bundle();
                args.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
                NavController controller = Navigation.findNavController(databinding.getRoot());
                controller.navigate(R.id.action_topicsFragment_to_topicFragment, args);
            }
        }

        /** Setters */
        public void setId(long value) {
            this.itemId = value;
        }
        void setCardView(CardView view) {
            this.cardView = view;
        }
        void setTag(Topic item) {
            this.cardView.setTag(item);
        }

        /** Getters */
        public long getId() {
            return this.itemId;
        }
        CardviewTopicBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
