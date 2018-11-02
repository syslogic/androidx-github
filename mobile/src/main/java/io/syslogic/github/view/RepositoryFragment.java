package io.syslogic.github.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.RepositoryFragmentBinding;
import io.syslogic.github.model.Repository;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.retrofit.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RepositoryFragment extends Fragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** Debug Output */
    private static final boolean mDebug = BuildConfig.DEBUG;

    private RepositoryFragmentBinding mDataBinding;

    /** readme.md {@link WebView} */
    private WebView mWebView;

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
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mDataBinding = RepositoryFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();
        this.mWebView = layout.findViewById(R.id.webview_preview);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.setRepository();
        return layout;
    }

    private GithubService getGithubService() {
        Retrofit client = GithubClient.init();
        return client.create(GithubService.class);
    }

    private void setRepository() {

        if(this.itemId != 0) {

            GithubService service = this.getGithubService();
            Call<Repository> api = service.getRepository(this.itemId);
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
                                if (getActivity() != null) {
                                    getActivity().setTitle(item.getFullName());
                                    getWebView().loadUrl(item.getHtmlUrl());
                                }
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

    private RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    private WebView getWebView() {
        return this.mWebView;
    }
}
