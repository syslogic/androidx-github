package io.syslogic.github.fragment;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.constants.Constants;
import io.syslogic.github.databinding.ProfileFragmentBinding;
import io.syslogic.github.model.User;

/**
 * Profile Fragment
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class ProfileFragment extends BaseFragment {

    /** Log Tag */
    private static final String LOG_TAG = ProfileFragment.class.getSimpleName();

    /** Data Binding */
    private ProfileFragmentBinding mDataBinding;

    private Boolean contentLoaded = false;

    private Long itemId = 0L;

    public ProfileFragment() {

    }

    @NonNull
    public static ProfileFragment newInstance(@NonNull Long itemId) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @NonNull
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setDataBinding(ProfileFragmentBinding.inflate(inflater, container, false));
        View layout = this.mDataBinding.getRoot();

        if(this.getContext() != null) {

            if(! isNetworkAvailable(this.getContext())) {
                this.onNetworkLost();
            } else {

                this.mDataBinding.layoutLottieLoading.lottieLoading.enableMergePathsForKitKatAndAbove(true);
                this.mDataBinding.layoutLottieLoading.lottieError.enableMergePathsForKitKatAndAbove(true);

                this.mDataBinding.webview.getSettings().setJavaScriptEnabled(true);
                this.mDataBinding.webview.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageCommitVisible (WebView view, String url) {
                        if(! contentLoaded) {
                            mDataBinding.viewflipperContent.showNext();
                            contentLoaded = true;
                        }
                    }
                });

                String token = this.getAccessToken(this.getContext());
                if (getCurrentUser() == null && token != null) {
                    this.setUser(token, this);
                }
            }
        }
        return layout;
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public ProfileFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void setDataBinding(@NonNull ViewDataBinding dataBinding) {
        this.mDataBinding = (ProfileFragmentBinding) dataBinding;
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
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    public void onLogin(@NonNull User item) {
        if(this.mDataBinding != null && !this.contentLoaded) {
            this.mDataBinding.setProfile(item);
        }
    }
}
