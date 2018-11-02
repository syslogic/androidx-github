package io.syslogic.githubtrends.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.githubtrends.BuildConfig;
import io.syslogic.githubtrends.R;
import io.syslogic.githubtrends.activity.DetailActivity;
import io.syslogic.githubtrends.constants.Constants;
import io.syslogic.githubtrends.databinding.RepositoryViewHolderBinding;
import io.syslogic.githubtrends.model.Repositories;
import io.syslogic.githubtrends.model.Repository;
import io.syslogic.githubtrends.retrofit.GithubClient;
import io.syslogic.githubtrends.retrofit.GithubService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RepositoriesAdapter extends RecyclerView.Adapter {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoriesAdapter.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    /** the {@link CardView} layout to inflate */
    private int resId = R.layout.cardview_repository;

    private ArrayList<Repository> mItems = new ArrayList<>();

    private long totalItemCount = 0;

    private Context mContext;

    public RepositoriesAdapter(Context context, int pageNumber) {
        this.mContext = context;
        this.fetchPage(pageNumber);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepositoryViewHolderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resId, parent, false);
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

    public GithubService getGithubService() {
        Retrofit client = GithubClient.init();
        return client.create(GithubService.class);
    }

    public void addAll(ArrayList<Repository> items) {
        this.mItems.addAll(items);
    }

    public void fetchPage(int pageNumber) {

        GithubService service = this.getGithubService();
        Call<Repositories> api = service.getRepositories("language\\:android","stars","desc", pageNumber);
        if(mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<Repositories>() {

            @Override
            public void onResponse(@NonNull Call<Repositories> call, @NonNull Response<Repositories> response) {
                switch(response.code()) {

                    case 200: {
                        if (response.body() != null) {
                            Repositories items = response.body();
                            if (mDebug) {Log.d(LOG_TAG, "items " + getItemCount() + " / " + items.getCount());}
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
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Repositories> call, @NonNull Throwable t) {
                if(mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }

    private void setTotalItemCount(long value) {
        this.totalItemCount = value;
    }

    /** {@link RecyclerView.ViewHolder} for {@link CardView} of type {@link Repository}. */
    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RepositoryViewHolderBinding mDataBinding = null;
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
        public void setTag(Repository item) {
            this.cardView.setTag(item);
        }

        /** Getters */
        public long getId() {
            return this.itemId;
        }
        public RepositoryViewHolderBinding getDataBinding() {
            return this.mDataBinding;
        }
        public CardView getCardView() {
            return this.cardView;
        }
    }
}
