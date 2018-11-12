package io.syslogic.github.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.RepositoryFragmentBinding;
import io.syslogic.github.model.Repository;
import io.syslogic.github.network.IConnectivityListener;
import io.syslogic.github.retrofit.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryFragment extends BaseFragment implements IConnectivityListener {

    /** {@link Log} Tag */
    @NonNull
    static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** {@link RepositoryFragmentBinding} */
    private RepositoryFragmentBinding mDataBinding;

    Boolean contentLoaded = false;

    private Long itemId = 0L;

    public RepositoryFragment() {

    }

    @NonNull
    public static RepositoryFragment newInstance(@NonNull Long itemId) {
        RepositoryFragment fragment = new RepositoryFragment();
        fragment.setItemId(itemId);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if(itemId == 0 && args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(this.getContext(), this);
        } else {
            this.registerBroadcastReceiver(this.getContext());
        }
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = RepositoryFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();
        if(this.getContext() != null) {
            if(! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {
                this.mDataBinding.webviewPreview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webviewPreview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        contentLoaded = true;
                    }
                });
                this.setRepository();
            }
        }
        return layout;
    }

    private void setRepository() {

        if(this.itemId != 0) {

            Call<Repository> api = GithubClient.getRepository(this.itemId);
            if (mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

            api.enqueue(new Callback<Repository>() {

                @Override
                public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                    switch(response.code()) {

                        case 200: {
                            if (response.body() != null) {
                                Repository item = response.body();
                                getDataBinding().setRepository(item);
                                getDataBinding().notifyChange();
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
                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                        Log.e(LOG_TAG, message);
                                    }
                                } catch (IOException e) {
                                    if(mDebug) {Log.e(LOG_TAG, e.getMessage());}
                                }
                                /* TODO: try to setRepository() in a second. */
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                    if (mDebug) {Log.e(getLogTag(), t.getMessage());}
                }
            });
        }
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    @NonNull
    public RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void onNetworkAvailable() {
        if (mDebug) {Log.d(LOG_TAG, "network connection is available.");}
        if(this.mDataBinding.webviewPreview != null && !contentLoaded) {
            setRepository();
        }
    }

    @Override
    public void onNetworkLost() {
        if (mDebug) {Log.d(LOG_TAG, "network connection was lost.");}
    }
}
