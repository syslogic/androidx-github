package io.syslogic.github.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.Constants;
import io.syslogic.github.databinding.FragmentProfileBinding;
import io.syslogic.github.api.model.User;
import io.syslogic.github.network.TokenCallback;

/**
 * Profile {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class ProfileFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_profile;

    /** Data Binding */
    private FragmentProfileBinding mDataBinding;

    private Long itemId = -1L;

    /** Constructor */
    public ProfileFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (itemId == 0 && args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setDataBinding(FragmentProfileBinding.inflate(inflater, container, false));
        if (this.getContext() != null) {

            if (! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {

                this.mDataBinding.webview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        if (! contentLoaded) {contentLoaded = true;}
                    }
                });

                String token = this.getAccessToken();
                if (getCurrentUser() == null && token != null) {
                    this.setUser(token, this);
                }
            }
        }
        return this.mDataBinding.getRoot();
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public FragmentProfileBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentProfileBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
    }

    @Override
    public void onLogin(@NonNull User item) {
        if (this.mDataBinding != null && !this.contentLoaded) {
            this.mDataBinding.setProfile(item);
        }
    }
}
