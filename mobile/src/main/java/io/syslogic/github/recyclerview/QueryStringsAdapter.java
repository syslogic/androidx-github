package io.syslogic.github.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.databinding.CardviewQueryStringBinding;

/**
 * Query-Strings {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class QueryStringsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    @SuppressWarnings("unused")
    private static final String LOG_TAG = QueryStringsAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private WeakReference<Context> mContext;
    private RecyclerView mRecyclerView;
    private List<QueryString> mItems;

    public QueryStringsAdapter(@NonNull Context context) {
        this.mContext = new WeakReference<>(context);
        Abstraction.executorService.execute(() -> mItems = Abstraction.getInstance(getContext()).queryStringsDao().getItems());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewQueryStringBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_query_string, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        QueryString item = getItem(position);
        ((ViewHolder) viewHolder).getDataBinding().setItem(item);
        ((ViewHolder) viewHolder).setId(item.getId());
    }

    private QueryString getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        if (this.mItems == null) {return 0;}
        return this.mItems.size();
    }

    List<QueryString> getItems() {
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

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link QueryString}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewQueryStringBinding mDataBinding;
        private QueryStringsLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewQueryStringBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView(layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(@NonNull View viewHolder) {
            this.mRecyclerView = (QueryStringsLinearView) viewHolder.getParent();
            QueryString item = (QueryString) viewHolder.getTag();
            BaseActivity activity = (BaseActivity) this.mRecyclerView.getContext();
            ViewDataBinding databinding = activity.getFragmentDataBinding();
            if (databinding != null) {
                Bundle args = new Bundle();
                args.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
                NavController controller = Navigation.findNavController(databinding.getRoot());
                controller.navigate(R.id.action_queryStringsFragment_to_queryStringFragment, args);
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
        CardviewQueryStringBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
