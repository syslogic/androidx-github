package io.syslogic.github.recyclerview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.R;
import io.syslogic.github.BuildConfig;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.activity.RepositoryActivity;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.databinding.CardviewRepositoryBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.RateLimit;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.Repositories;
import io.syslogic.github.model.Repository;
import io.syslogic.github.retrofit.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import io.syslogic.github.network.TokenHelper;

/**
 * Repositories RecyclerView Adapter
 * @author Martin Zeitler
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = RepositoriesAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private ArrayList<Repository> mItems = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private long totalItemCount = 0;

    private final Context mContext;

    private String queryString = "topic:android";

    public RepositoriesAdapter(@NonNull Context context, @NonNull Integer pageNumber) {
        this.mContext = context;
        String defaultQuery = context.getResources().getStringArray(R.array.topic_values)[0];
        this.setQueryString(defaultQuery);
        this.fetchPage(pageNumber);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewRepositoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_repository, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        final Repository item = getItem(position);
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

    String getQueryString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -Constants.PARAMETER_PUSHED_WITHIN_LAST_DAYS);
        String isodate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        return queryString + "+pushed:>" + isodate;
    }

    public void fetchPage(final int pageNumber) {

        final int pageSize = 30;
        String accessToken = getAccessToken();

        Call<Repositories> api = GithubClient.getRepositories(accessToken, getQueryString(),"stars","desc", pageNumber);
        if(BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}

        /* updating the pager data-binding */
        setPagerState(pageNumber, true, null);

        api.enqueue(new Callback<Repositories>() {

            @Override
            public void onResponse(@NonNull Call<Repositories> call, @NonNull Response<Repositories> response) {
                switch(response.code()) {

                    // OK
                    case 200: {
                        if (response.body() != null) {

                            Repositories items = response.body();
                            if (BuildConfig.DEBUG) {Log.d(LOG_TAG, "loaded " + (getItemCount() + pageSize) + " / " + items.getCount());}
                            setTotalItemCount(items.getCount());
                            int positionStart = getItemCount();
                            getItems().addAll(items.getRepositories());
                            notifyItemRangeChanged(positionStart, getItemCount());

                            /* updating the pager data-binding */
                            setPagerState(pageNumber, false, items.getCount());
                        }
                        break;
                    }

                    case 401: {
                        /* "bad credentials" means that the provided access-token is invalid. */
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = new JsonParser().parse(errors).getAsJsonObject();
                                if(BuildConfig.DEBUG) {Log.e(LOG_TAG, jsonObject.get("message").toString());}
                            } catch (IOException e) {
                                if(BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + e.getMessage());}
                            }
                        }

                        /* updating the pager data-binding */
                        setPagerState(pageNumber, false, null);

                        break;
                    }

                    case 403: {
                        if (response.errorBody() != null) {
                            try {
                                String errors = response.errorBody().string();
                                JsonObject jsonObject = new JsonParser().parse(errors).getAsJsonObject();
                                if(BuildConfig.DEBUG) {Log.e(LOG_TAG, jsonObject.get("message").toString());}
                            } catch (IOException e) {
                                if(BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + e.getMessage());}
                            }

                            /* updating the pager data-binding */
                            setPagerState(pageNumber, false, null);

                            resetOnScollListener();
                            getRateLimit("search");
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Repositories> call, @NonNull Throwable t) {
                if(BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
    }

    ArrayList<Repository> getItems() {
        return this.mItems;
    }

    private String getAccessToken() {
        Activity activity = (Activity) getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER) == PackageManager.PERMISSION_GRANTED) {
                return TokenHelper.getAccessToken(activity);
            } else {
                /* for testing purposes only: */
                if(mDebug) {return TokenHelper.getAccessToken(activity);}
                else {return null;}
            }
        } else {
            return TokenHelper.getAccessToken(activity);
        }
    }

    void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    /** reset the scroll listener. */
    protected void resetOnScollListener() {
        if(this.mRecyclerView.getAdapter() != null) {
            ScrollListener listener = ((RepositoriesLinearView) mRecyclerView).getOnScrollListener();
            listener.setIsLoading(false);
        }
    }

    protected void setPagerState(int pageNumber, boolean isLoading, @Nullable Long itemCount) {
        FragmentRepositoriesBinding databinding = (FragmentRepositoriesBinding) ((BaseActivity) mContext).getFragmentDataBinding();
        if(databinding != null) {
            PagerState state = databinding.getPager();
            state.setIsLoading(isLoading);
            state.setPageNumber(pageNumber);
            if (itemCount != null) {
                state.setPageCount((int) Math.ceil(itemCount / state.getItemsPerPage()));
                state.setItemCount(itemCount);
            }
            databinding.setPager(state);
        }
    }

    protected void getRateLimit(@NonNull final String resourceName) {

        Call<RateLimits> api = GithubClient.getRateLimits();
        if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<RateLimits>() {

            @Override
            public void onResponse(@NonNull Call<RateLimits> call, @NonNull Response<RateLimits> response) {
                switch(response.code()) {
                    case 200: {
                        if (response.body() != null) {
                            RateLimits items = response.body();
                            RateLimit limit = null;
                            switch(resourceName) {
                                case "graphql": limit = items.getResources().getGraphql(); break;
                                case "search": limit = items.getResources().getSearch(); break;
                                case "core": limit = items.getResources().getCore(); break;
                            }
                            if(limit != null && BuildConfig.DEBUG) {
                                long seconds = (long) Math.ceil((new Date(limit.getReset() * 1000).getTime() - new Date().getTime()) / 1000);
                                String text = String.format(Locale.getDefault(), "%s quota: %d / %d. reset in %d seconds.", resourceName, limit.getRemaining(), limit.getLimit(), seconds);
                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                            }

                            /* possible border-case: */
                            if(limit != null && limit.getRemaining() > 0) {
                                Toast.makeText(getContext(), "the " + resourceName + " quota was reset already", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateLimits> call, @NonNull Throwable t) {
                if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
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

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Repository}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardviewRepositoryBinding mDataBinding;
        private RepositoriesLinearView mRecyclerView;
        private CardView cardView;
        private long itemId;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(CardviewRepositoryBinding binding) {

            super(binding.getRoot());
            this.mDataBinding = binding;

            View layout = binding.getRoot();
            this.setCardView((CardView) layout.findViewById(R.id.cardview));
            if (this.cardView != null) {this.cardView.setOnClickListener(this);}
        }

        @Override
        public void onClick(View viewHolder) {

            this.mRecyclerView = (RepositoriesLinearView) viewHolder.getParent();
            BaseActivity activity = (BaseActivity) this.mRecyclerView.getContext();
            Repository item = (Repository) viewHolder.getTag();

            if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                FragmentRepositoriesBinding databinding = (FragmentRepositoriesBinding) activity.getFragmentDataBinding();
                //if (databinding.layoutRepository.getRepository() == null || !Objects.equals(databinding.layoutRepository.getRepository().getId(), item.getId())) {
                //    databinding.layoutRepository.setRepository(item);
                //}

            } else {
                Bundle extras = new Bundle();
                extras.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
                Intent intent = new Intent(activity, RepositoryActivity.class);
                intent.putExtras(extras);
                activity.startActivity(intent);
            }
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
        CardviewRepositoryBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
