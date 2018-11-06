package io.syslogic.github.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.activity.DetailActivity;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.RepositoryViewHolderBinding;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.retrofit.GithubService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoriesAdapter extends RecyclerView.Adapter {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoriesAdapter.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    private ArrayList<Repository> mItems = new ArrayList<>();

    private long totalItemCount = 0;

    private Context mContext;

    private String queryString = "language\\:android";

    public RepositoriesAdapter(Context context, int pageNumber) {

        this.mContext = context;
        this.fetchPage(pageNumber);

        String defaultQuery = context.getResources().getStringArray(R.array.queryStrings)[0];
        this.setQueryString(defaultQuery);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepositoryViewHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_repository, parent, false);
        View layout = binding.getRoot();
        layout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Repository item = getItem(position);
        // if(mDebug) {Log.d(LOG_TAG, item.getFullName() + ": " + item.getUrl());}
        ((ViewHolder) viewHolder).getDataBinding().setRepository(item);
        ((ViewHolder) viewHolder).setId(item.getId());
        ((ViewHolder) viewHolder).setTag(item);
    }

    private Repository getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }


    private void addAll(ArrayList<Repository> items) {
        this.mItems.addAll(items);
    }

    private String getQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -90);
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return queryString + "+pushed:>" + isodate;
    }

    public void fetchPage(int pageNumber) {

        GithubService service = GithubClient.getService();
        Call<Repositories> api = service.getRepositories(getQueryString(),"stars","desc", pageNumber);
        if(mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<Repositories>() {

            @Override
            public void onResponse(@NonNull Call<Repositories> call, @NonNull Response<Repositories> response) {
                switch(response.code()) {

                    case 200: {
                        if (response.body() != null) {
                            Repositories items = response.body();
                            if (mDebug) {Log.d(LOG_TAG, "loaded " + getItemCount() + " / " + items.getCount());}
                            setTotalItemCount(items.getCount());
                            int positionStart = getItemCount();
                            addAll(items.getRepositories());
                            notifyItemRangeChanged(positionStart, getItemCount());
                        }
                        break;
                    }

                    case 403: {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = (new JsonParser()).parse(errors).getAsJsonObject();
                                String message = jsonObject.get("message").toString();
                                if(mDebug) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                    Log.e(LOG_TAG, message);
                                }
                            } catch (IOException e) {
                                if(mDebug) {Log.e(LOG_TAG, e.getMessage());}
                            }
                            /* TODO: reset the scrollListener. */
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Repositories> call, @NonNull Throwable t) {
                if(mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }

    public void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    /** Setters */
    private void setTotalItemCount(long value) {
        this.totalItemCount = value;
    }
    void setQueryString(String value) {
        this.queryString = value;
    }

    /** Getters */
    private long getTotalItemCount() {
        return this.totalItemCount;
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Repository}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RepositoryViewHolderBinding mDataBinding;
        private RepositoriesLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(RepositoryViewHolderBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView((CardView) layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(View viewHolder) {

            this.mRecyclerView = (RepositoriesLinearView) viewHolder.getParent();
            AppCompatActivity activity = (AppCompatActivity) this.mRecyclerView.getContext();
            Repository item = (Repository) viewHolder.getTag();

            Bundle extras = new Bundle();
            extras.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtras(extras);
            activity.startActivity(intent);
        }

        /** Setters */
        public void setId(long value) {
            this.itemId = value;
        }
        void setCardView(CardView view) {
            this.cardView = view;
        }
        void setTag(Repository item) {
            this.cardView.setTag(item);
        }

        /** Getters */
        public long getId() {
            return this.itemId;
        }
        RepositoryViewHolderBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
