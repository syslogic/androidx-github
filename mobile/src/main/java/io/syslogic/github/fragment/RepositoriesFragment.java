package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.model.User;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.provider.RepositoriesMenuProvider;
import io.syslogic.github.recyclerview.RepositoriesAdapter;

/**
 * Repositories {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class RepositoriesFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_repositories;

    /** Data-Binding */
    private FragmentRepositoriesBinding mDataBinding;

    /** Constructor */
    public RepositoriesFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentRepositoriesBinding.inflate(inflater, container, false));
        this.getDataBinding().setPagerState(new PagerState());

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new RepositoriesMenuProvider(activity));

        activity.setSupportActionBar(this.getDataBinding().toolbarRepositories.toolbarRepositories);
        this.mDataBinding.toolbarRepositories.home.setOnClickListener(view -> activity.onBackPressed());

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else {
            RepositoriesAdapter adapter = new RepositoriesAdapter(requireContext());
            this.getDataBinding().recyclerviewRepositories.setAdapter(adapter);
        }
        return this.getDataBinding().getRoot();
    }

    @NonNull
    public FragmentRepositoriesBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentRepositoriesBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        String token = this.getAccessToken();
        if (getCurrentUser() == null && token != null) {
            this.setUser(token, this);
        }

        //noinspection ConstantValue
        if (this.getDataBinding() != null) {
            PagerState pagerState = this.getDataBinding().getPagerState();
            pagerState.setIsOffline(false);
            this.getDataBinding().setPagerState(pagerState);
        }

    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
        PagerState pagerState = this.getDataBinding().getPagerState();
        if (pagerState != null) {
            pagerState.setIsLoading(false);
            pagerState.setIsOffline(true);
            this.getDataBinding().setPagerState(pagerState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
    }

    @Override
    public void onLogin(@NonNull User item) {
        this.mDataBinding.setUser(item);
    }
}
