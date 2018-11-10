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

import io.syslogic.github.R;
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
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** ViewDataBinding {@link RepositoryFragmentBinding} */
    private RepositoryFragmentBinding mDataBinding;

    /** {@link WebView} */
    private WebView mWebView;

    private boolean contentLoaded = false;

    private long itemId = 0;

    public RepositoryFragment() {

    }

    public static RepositoryFragment newInstance(long itemId) {
        RepositoryFragment fragment = new RepositoryFragment();
        fragment.setItemId(itemId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mDataBinding = RepositoryFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();
        if(this.getContext() != null) {
            if(! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {
                this.mWebView = layout.findViewById(R.id.webview_preview);
                this.mWebView.getSettings().setJavaScriptEnabled(true);
                this.mWebView.setWebViewClient(new WebViewClient() {
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
                    if (mDebug) {Log.e(LOG_TAG, t.getMessage());}
                }
            });
        }
    }

    private void setItemId(long value) {
        this.itemId = value;
    }

    public long getItemId() {
        return this.itemId;
    }

    private WebView getWebView() {
        return this.mWebView;
    }

    public RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void onNetworkAvailable() {
        if (mDebug) {Log.d(LOG_TAG, "network connection is available.");}
        if(this.mWebView != null && !contentLoaded) {setRepository();}
    }

    @Override
    public void onNetworkLost() {
        if (mDebug) {Log.d(LOG_TAG, "network connection was lost.");}
    }
}
