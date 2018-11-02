package io.syslogic.githubtrends.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.syslogic.githubtrends.BuildConfig;
import io.syslogic.githubtrends.R;
import io.syslogic.githubtrends.constants.Constants;
import io.syslogic.githubtrends.databinding.RepositoryFragmentBinding;
import io.syslogic.githubtrends.model.Repository;
import io.syslogic.githubtrends.retrofit.GithubClient;
import io.syslogic.githubtrends.retrofit.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RepositoryFragment extends Fragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoryFragment.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

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
        this.mWebView = layout.findViewById(R.id.webviewReadme);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.setRepository();
        return layout;
    }

    public GithubService getGithubService() {
        Retrofit client = GithubClient.init();
        return client.create(GithubService.class);
    }

    private void setRepository() {

        GithubService service = this.getGithubService();
        Call<Repository> api = service.getRepository(this.itemId);
        if(mDebug) {Log.w(LOG_TAG, api.request().url() + "");}

        api.enqueue(new Callback<Repository>() {

            @Override
            public void onResponse(@NonNull Call<Repository> call, @NonNull Response<Repository> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {

                        Repository item = response.body();
                        getDataBinding().setRepository(item);
                        getDataBinding().notifyChange();

                        if(getActivity() != null) {
                            getActivity().setTitle(item.getFullName());
                            getWebView().loadUrl(item.getHtmlUrl());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Repository> call, @NonNull Throwable t) {
                if(mDebug) {Log.e(LOG_TAG, t.getMessage());}
            }
        });
    }

    private void setItemId(long value) {
        this.itemId = value;
    }

    public long getItemId() {
        return this.itemId;
    }

    public RepositoryFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    public WebView getWebView() {
        return this.mWebView;
    }
}
