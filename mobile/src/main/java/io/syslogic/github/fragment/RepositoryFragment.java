package io.syslogic.github.fragment;

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

import androidx.databinding.ViewDataBinding;
import io.syslogic.github.R;
import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.RepositoryFragmentBinding;
import io.syslogic.github.model.Repository;
import io.syslogic.github.retrofit.GithubClient;
import io.syslogic.github.retrofit.GithubService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryFragment extends BaseFragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** the {@link ViewDataBinding} */
    private RepositoryFragmentBinding mDataBinding;

    /** {@link WebView} */
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mDataBinding = RepositoryFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();
        this.setRepository(layout);
        return layout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setRepository(View view) {

        this.mWebView = view.findViewById(R.id.webview_preview);
        this.mWebView.getSettings().setJavaScriptEnabled(true);

        if(this.itemId != 0) {

            GithubService service = GithubClient.getService();
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

    private WebView getWebView() {
        return this.mWebView;
    }

    private RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }
}
