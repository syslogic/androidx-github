package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.adapter.RepositoryTypeAdapter;
import io.syslogic.github.adapter.SortFieldListingAdapter;
import io.syslogic.github.adapter.SortOrderAdapter;
import io.syslogic.github.api.model.User;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.menu.RepositoriesMenuProvider;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.RepositoriesLinearView;
import io.syslogic.github.recyclerview.ScrollListener;

/**
 * Repositories {@link BaseFragment}
 * @see <a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user">List repositories for a user</a>
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
        this.setDataBinding(inflater, container);
        this.getDataBinding().setPagerState(new PagerState());

        /* It removes & adds {@link BaseMenuProvider} */
        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        activity.setMenuProvider(new RepositoriesMenuProvider(activity));

        activity.setSupportActionBar(this.getDataBinding().toolbarRepositories.toolbarRepositories);
        this.mDataBinding.toolbarRepositories.home.setOnClickListener(view -> {
            activity.getOnBackPressedDispatcher().onBackPressed();
        });

        /* {@link AppCompatSpinner} spinner_repository_type */
        AppCompatSpinner spinnerRepositoryType = this.getDataBinding().toolbarRepositories.spinnerRepositoryType;
        spinnerRepositoryType.setAdapter(new RepositoryTypeAdapter(requireContext()));
        spinnerRepositoryType.setSelection(1);

        spinnerRepositoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                if (count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    ScrollListener.setPageNumber(1);
                    String value = item.getValue();
                    RepositoriesLinearView recyclerview = getDataBinding().recyclerviewRepositories;
                    recyclerview.setRepositoryType(value);
                    PagerState pagerState = getDataBinding().getPagerState();
                    if (pagerState != null) {
                        getDataBinding().setPagerState(pagerState);
                    }
                    if (recyclerview.getAdapter() != null) {
                        recyclerview.clearAdapter();
                        ((RepositoriesAdapter) recyclerview.getAdapter()).fetchPage(1);
                    }
                }
                count++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /* {@link AppCompatSpinner} spinner_sort_field */
        AppCompatSpinner spinnerSortField = this.getDataBinding().toolbarRepositories.spinnerSortField;
        spinnerSortField.setAdapter(new SortFieldListingAdapter(requireContext()));
        spinnerSortField.setSelection(3);

        spinnerSortField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                if (count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    ScrollListener.setPageNumber(1);
                    String value = item.getValue();
                    RepositoriesLinearView recyclerview = getDataBinding().recyclerviewRepositories;
                    recyclerview.setSortField(value);
                    PagerState pagerState = getDataBinding().getPagerState();
                    if (pagerState != null) {
                        getDataBinding().setPagerState(pagerState);
                    }
                    if (recyclerview.getAdapter() != null) {
                        recyclerview.clearAdapter();
                        ((RepositoriesAdapter) recyclerview.getAdapter()).fetchPage(1);
                    }
                }
                count++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /* {@link AppCompatSpinner} spinner_sort_order */
        AppCompatSpinner spinnerSortOrder = this.getDataBinding().toolbarRepositories.spinnerSortOrder;
        spinnerSortOrder.setAdapter(new SortOrderAdapter(requireContext()));
        spinnerSortOrder.setSelection(0);

        spinnerSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                if (count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    ScrollListener.setPageNumber(1);
                    String value = item.getValue();
                    RepositoriesLinearView recyclerview = getDataBinding().recyclerviewRepositories;
                    recyclerview.setSortOrder(value);
                    PagerState pagerState = getDataBinding().getPagerState();
                    if (pagerState != null) {
                        getDataBinding().setPagerState(pagerState);
                    }
                    if (recyclerview.getAdapter() != null) {
                        recyclerview.clearAdapter();
                        ((RepositoriesAdapter) recyclerview.getAdapter()).fetchPage(1);
                    }
                }
                count++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else {
            RepositoriesAdapter adapter = new RepositoriesAdapter(requireContext());
            this.getDataBinding().recyclerviewRepositories.setAdapter(adapter);
        }
        return this.getDataBinding().getRoot();
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
        if (this.mDataBinding != null) {this.mDataBinding.setUser(item);}
    }

    @Override
    protected void setDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        this.mDataBinding = FragmentRepositoriesBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    public FragmentRepositoriesBinding getDataBinding() {
        return this.mDataBinding;
    }
}
