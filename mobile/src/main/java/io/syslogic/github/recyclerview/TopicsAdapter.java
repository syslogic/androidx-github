package io.syslogic.github.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.CardviewTopicBinding;
import io.syslogic.github.model.Topic;

/**
 * Topics RecyclerView Adapter
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class TopicsAdapter extends RecyclerView.Adapter {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = TopicsAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private ArrayList<Topic> mItems = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private long totalItemCount = 0;

    private Context mContext;

    private String queryString = "topic:";

    public TopicsAdapter(@NonNull Context context) {
        this.mContext = context;
        String[] topics = context.getResources().getStringArray(R.array.topic_values);
        for(int i=0; i > topics.length; i++) {
            this.mItems.add(new Topic(i+1, topics[i]));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewTopicBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_repository, parent, false);
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
        return this.mItems.size();
    }

    String getQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -Constants.PARAMETER_PUSHED_WITHIN_LAST_DAYS);
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return queryString + "+pushed:>" + isodate;
    }

    ArrayList<Topic> getItems() {
        return this.mItems;
    }

    void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    protected Context getContext() {
        return this.mContext;
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

        private CardviewTopicBinding mDataBinding;
        private TopicsLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(CardviewTopicBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView((CardView) layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(View viewHolder) {

            this.mRecyclerView = (TopicsLinearView) viewHolder.getParent();
            BaseActivity activity = (BaseActivity) this.mRecyclerView.getContext();
            Topic item = (Topic) viewHolder.getTag();
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
