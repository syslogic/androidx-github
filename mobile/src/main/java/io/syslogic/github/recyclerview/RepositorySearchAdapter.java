package io.syslogic.github.recyclerview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.R;
import io.syslogic.github.BuildConfig;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.Constants;
import io.syslogic.github.databinding.CardviewRepositorySearchBinding;
import io.syslogic.github.databinding.FragmentRepositorySearchBinding;
import io.syslogic.github.fragment.RepositoryFragment;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.RateLimit;
import io.syslogic.github.model.RateLimits;
import io.syslogic.github.model.RepositorySearch;
import io.syslogic.github.model.Repository;
import io.syslogic.github.retrofit.GithubClient;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import io.syslogic.github.network.TokenHelper;

/**
 * Repository Search {@link RecyclerView.Adapter}
 *
 * @author Martin Zeitler
 */
public class RepositorySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** Log Tag */
    @NonNull
    private static final String LOG_TAG = RepositorySearchAdapter.class.getSimpleName();

    /** Debug Output */
    static final boolean mDebug = BuildConfig.DEBUG;

    private final ArrayList<Repository> mItems = new ArrayList<>();

    private WeakReference<Context> mContext;

    private RecyclerView mRecyclerView;

    private long totalItemCount = 0;

    private String queryString = "topic:android";

    private final boolean showTopics;

    public RepositorySearchAdapter(@NonNull Context context, @NonNull String queryString, @NonNull Boolean showTopics, @NonNull Integer pageNumber) {
        this.mContext = new WeakReference<>(context);
        this.setQueryString(queryString);
        this.showTopics = showTopics;
        this.fetchPage(pageNumber);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mContext = new WeakReference<>(recyclerView.getContext());
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardviewRepositorySearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cardview_repository_search, parent, false);
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RepositorySearchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Repository item = getItem(position);
        CardviewRepositorySearchBinding binding = ((ViewHolder) viewHolder).getDataBinding();
        binding.setItem(item);
        if (! this.showTopics) {
            binding.recyclerviewTopics.setVisibility(View.GONE);
        } else {
            TopicsAdapter adapter = new TopicsAdapter(List.of(item.getTopics()));
            binding.recyclerviewTopics.setAdapter(adapter);
        }
    }

    private Repository getItem(int index) {
        return this.mItems.get(index);
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    public void fetchPage(final int pageNumber) {

        String accessToken = getAccessToken();
        if (this.getPagerState() != null && !this.getPagerState().getIsOffline()) {

            /* Updating the pager data-binding */
            setPagerState(pageNumber, true, null);

            Call<RepositorySearch> api = GithubClient.searchRepositories(accessToken, this.queryString,"stars","desc", pageNumber);
            if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}
            api.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<RepositorySearch> call, @NonNull Response<RepositorySearch> response) {
                    switch (response.code()) {

                        // OK
                        case 200 -> {
                            if (response.body() != null) {
                                RepositorySearch items = response.body();
                                if (BuildConfig.DEBUG) {
                                    // RECYCLERVIEW_DEFAULT_PAGE_SIZE = 30
                                    int currentPageSize = items.getRepositories().size();
                                    Log.d(LOG_TAG, "loaded " + (getItemCount() + currentPageSize) + " / " + items.getTotalCount());
                                    if (currentPageSize < Constants.RECYCLERVIEW_DEFAULT_PAGE_SIZE) {
                                        Log.w(LOG_TAG, "The last page has " + currentPageSize + " / " + Constants.RECYCLERVIEW_DEFAULT_PAGE_SIZE + " items.");
                                    }
                                }
                                setTotalItemCount(items.getTotalCount());
                                int positionStart = getItemCount();
                                getItems().addAll(items.getRepositories());
                                notifyItemRangeChanged(positionStart, getItemCount());

                                /* Updating the pager data-binding */
                                setPagerState(pageNumber, false, items.getTotalCount());
                            }
                        }
                        case 401 -> {
                            /* "bad credentials" means that the provided access-token is invalid. */
                            if (response.errorBody() != null) {
                                logError(response.errorBody());

                                /* Updating the pager data-binding */
                                setPagerState(pageNumber, false, null);
                            }
                        }
                        case 403 -> {
                            if (response.errorBody() != null) {
                                logError(response.errorBody());

                                /* Updating the pager data-binding */
                                setPagerState(pageNumber, false, null);

                                resetOnScrollListener();
                                getRateLimit("search");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RepositorySearch> call, @NonNull Throwable t) {
                    if (BuildConfig.DEBUG) {Log.e(LOG_TAG, "" + t.getMessage());}
                }
            });
        }
    }

    ArrayList<Repository> getItems() {
        return this.mItems;
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

    void clearItems() {
        this.mItems.clear();
        notifyItemRangeChanged(0, getItemCount());
    }

    /** Reset the scroll listener. */
    protected void resetOnScrollListener() {
        if (this.mRecyclerView.getAdapter() != null) {
            ScrollListener listener = ((RepositorySearchLinearView) this.mRecyclerView).getOnScrollListener();
            listener.setIsLoading(false);
        }
    }

    @Nullable
    protected PagerState getPagerState() {
        if (((BaseActivity) getContext()).getFragmentDataBinding() instanceof FragmentRepositorySearchBinding databinding) {
            return databinding.getPagerState();
        }
        return null;
    }

    protected void setPagerState(int pageNumber, boolean isLoading, @Nullable Long itemCount) {
        if (this.getPagerState() != null) {
            PagerState state = this.getPagerState();
            state.setIsLoading(isLoading);
            state.setPageNumber(pageNumber);
            if (itemCount != null) {
                state.setPageCount((int) Math.ceil((float) itemCount / state.getItemsPerPage()));
                state.setItemCount(itemCount);
            }
            FragmentRepositorySearchBinding databinding = (FragmentRepositorySearchBinding) ((BaseActivity) getContext()).getFragmentDataBinding();
            if (databinding != null) {databinding.setPagerState(state);}
        }
    }

    protected void getRateLimit(@SuppressWarnings("SameParameterValue") @NonNull final String resourceName) {
        Call<RateLimits> api = GithubClient.getRateLimits();
        if (BuildConfig.DEBUG) {Log.w(LOG_TAG, api.request().url() + "");}
        api.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RateLimits> call, @NonNull Response<RateLimits> response) {
                if (response.code() == 200 && response.body() != null) {
                    RateLimits items = response.body();
                    RateLimit limit = switch (resourceName) {
                        case "graphql" -> items.getResources().getGraphql();
                        case "search" -> items.getResources().getSearch();
                        case "core" -> items.getResources().getCore();
                        default -> null;
                    };

                    /* For testing purposes only: */
                    if (limit != null && BuildConfig.DEBUG) {
                        long seconds = (long) Math.ceil((new Date(limit.getReset() * 1000).getTime() - Math.ceil(new Date().getTime()) / 1000));
                        String text = String.format(Locale.getDefault(), "%s quota: %d / %d. reset in %d seconds.", resourceName, limit.getRemaining(), limit.getLimit(), seconds);
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    /* Possible border-case: */
                    if (limit != null && limit.getRemaining() > 0) {
                        Toast.makeText(getContext(), "the " + resourceName + " quota was reset already", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateLimits> call, @NonNull Throwable t) {
                if (mDebug) {Log.e(LOG_TAG, "" + t.getMessage());}
            }
        });
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
    @SuppressWarnings("unused")
    private long getTotalItemCount() {
        return this.totalItemCount;
    }

    public String getQueryString() {
        return this.queryString;
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

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Repository}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardviewRepositorySearchBinding mDataBinding;

        /**
         * ViewHolder Constructor
         * @param binding the item's data-binding
        **/
        ViewHolder(@NonNull CardviewRepositorySearchBinding binding) {
            super(binding.getRoot());
            binding.cardview.setOnClickListener(this);
            this.mDataBinding = binding;
        }

        @SuppressLint("SwitchIntDef")
        @Override
        public void onClick(@NonNull View viewHolder) {
            RepositorySearchLinearView mRecyclerView = (RepositorySearchLinearView) viewHolder.getParent();
            BaseActivity activity = (BaseActivity) mRecyclerView.getContext();
            FragmentRepositorySearchBinding databinding = (FragmentRepositorySearchBinding) activity.getFragmentDataBinding();
            int orientation = activity.getResources().getConfiguration().orientation;
            if (databinding != null) {
                View layout = databinding.getRoot();
                Repository item = getDataBinding().getItem();
                NavController controller = Navigation.findNavController(layout);
                switch (orientation) {
                    case /* Configuration.ORIENTATION_SQUARE */ 3,
                            Configuration.ORIENTATION_UNDEFINED,
                            Configuration.ORIENTATION_PORTRAIT -> {
                        Bundle args = new Bundle();
                        args.putLong(Constants.ARGUMENT_ITEM_ID, item.getId());
                        controller.navigate(R.id.action_repositorySearchFragment_to_repositoryFragment, args);
                    }
                    case Configuration.ORIENTATION_LANDSCAPE -> {
                        int layoutId = databinding.layoutRepository.layoutRepository.getId();
                        RepositoryFragment fragment = RepositoryFragment.newInstance(item.getId());
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        ft.replace(layoutId, fragment);
                        ft.commit();
                    }
                }
            }
        }

        /** Getters */
        CardviewRepositorySearchBinding getDataBinding() {
            return this.mDataBinding;
        }
    }
}
