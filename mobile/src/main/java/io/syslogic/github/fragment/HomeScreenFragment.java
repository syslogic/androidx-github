package io.syslogic.github.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.api.model.User;
import io.syslogic.github.databinding.FragmentHomeScreenBinding;
import io.syslogic.github.menu.HomeScreenMenuProvider;
import io.syslogic.github.network.TokenCallback;

/**
 * Home Screen {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class HomeScreenFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = HomeScreenFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_home_screen;

    /** Data-Binding */
    private FragmentHomeScreenBinding mDataBinding;

    /** Constructor */
    public HomeScreenFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* It removes & adds {@link BaseMenuProvider} */
        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        activity.setMenuProvider(new HomeScreenMenuProvider(activity));

        this.setDataBinding(inflater, container);
        activity.setSupportActionBar(this.getDataBinding().toolbarHomeScreen.toolbarHomeScreen);

        if (! isNetworkAvailable(activity)) {
            this.onNetworkLost();
        } else {
            String token = this.getAccessToken();
            if (getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }
        }

        /* Navigate to RepositoriesFragment */
        this.mDataBinding.buttonUserRepositories
                .setOnClickListener(view -> activity.getNavController()
                .navigate(R.id.action_homeScreenFragment_to_repositoriesGraph));

        /* Navigate to RepositorySearchFragment */
        this.mDataBinding.buttonRepositorySearch
                .setOnClickListener(view -> activity.getNavController()
                .navigate(R.id.action_homeScreenFragment_to_repositorySearchFragment));

        /* Navigate to GitHub Sponsors */
        this.mDataBinding.textGitHubSponsors
                .setOnClickListener(view -> activity.startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.url_git_hub_sponsors)))
                ));

        return this.mDataBinding.getRoot();
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
    public void onLogin(@NonNull User item) {
        this.mDataBinding.setUser(item);
    }

    @Override
    protected void setDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        this.mDataBinding = FragmentHomeScreenBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    public FragmentHomeScreenBinding getDataBinding() {
        return this.mDataBinding;
    }
}
