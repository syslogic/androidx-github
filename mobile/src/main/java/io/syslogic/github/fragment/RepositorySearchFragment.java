package io.syslogic.github.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.adapter.QueryStringAdapter;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.model.SpinnerItem;
import io.syslogic.github.api.model.User;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.QueryStringsDao;
import io.syslogic.github.databinding.FragmentRepositorySearchBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.provider.RepositorySearchMenuProvider;
import io.syslogic.github.recyclerview.RepositorySearchAdapter;
import io.syslogic.github.recyclerview.RepositorySearchLinearView;
import io.syslogic.github.recyclerview.ScrollListener;

/**
 * Repository Search {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class RepositorySearchFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RepositorySearchFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_repository_search;

    /** Data-Binding */
    private FragmentRepositorySearchBinding mDataBinding;

    /** Preference: PREFERENCE_KEY_SHOW_REPOSITORY_TOPICS */
    boolean showRepositoryTopics = false;

    /** Constructor */
    public RepositorySearchFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentRepositorySearchBinding.inflate(inflater, container, false));
        this.getDataBinding().setPagerState(new PagerState());

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new RepositorySearchMenuProvider(activity));

        /* the SpinnerItem has the same ID as the QueryString. */
        activity.setSupportActionBar(this.getDataBinding().toolbarRepositories.toolbarRepositories);
        this.mDataBinding.toolbarRepositories.home.setOnClickListener(view -> activity.onBackPressed());

        AppCompatSpinner spinner = this.getDataBinding().toolbarRepositories.spinnerQueryString;
        spinner.setAdapter(new QueryStringAdapter(requireContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                if (count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    ScrollListener.setPageNumber(1);
                    String queryString = item.getValue();
                    RepositorySearchLinearView recyclerview = getDataBinding().recyclerviewRepositories;
                    recyclerview.setQueryString(queryString);
                    PagerState pagerState = getDataBinding().getPagerState();
                    if (pagerState != null) {
                        pagerState.setQueryString(queryString);
                        getDataBinding().setPagerState(pagerState);
                    }
                    if (recyclerview.getAdapter() != null) {
                        recyclerview.clearAdapter();
                        ((RepositorySearchAdapter) recyclerview.getAdapter()).fetchPage(1);
                    }
                }
                count++;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /* It is quicker to query Room, because the QueryStringAdapter is populating too slow. */
        assert this.prefs != null;
        showRepositoryTopics = this.prefs.getBoolean(Constants.PREFERENCE_KEY_SHOW_REPOSITORY_TOPICS, false);
        if (this.getDataBinding().recyclerviewRepositories.getAdapter() == null) {
            if (isNetworkAvailable(requireContext())) {
                QueryStringsDao dao = Abstraction.getInstance(requireContext()).queryStringsDao();
                Abstraction.executorService.execute(() -> {
                    try {
                        assert dao != null;
                        List<QueryString> items = dao.getItems();
                        if (items.size() > 0) {
                            String queryString = items.get(0).toQueryString();
                            requireActivity().runOnUiThread(() -> {
                                RepositorySearchAdapter adapter = new RepositorySearchAdapter(requireContext(), queryString, showRepositoryTopics, 1);
                                getDataBinding().recyclerviewRepositories.setAdapter(adapter);
                                PagerState pagerState = getDataBinding().getPagerState();
                                if (pagerState != null) {
                                    pagerState.setQueryString(queryString);
                                    getDataBinding().setPagerState(pagerState);
                                }
                            });
                        } else {
                            if (mDebug) {Log.e(LOG_TAG, "table `query_strings` has no records.");}
                            this.getDataBinding().toolbarRepositories.spinnerQueryString.setVisibility(View.INVISIBLE);
                        }
                    } catch (IllegalStateException e) {
                        if (mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
                    }
                });
            } else {
                this.onNetworkLost();
            }
        }

        return this.getDataBinding().getRoot();
    }

    @NonNull
    public FragmentRepositorySearchBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        if (binding instanceof FragmentRepositorySearchBinding) {
            this.mDataBinding = (FragmentRepositorySearchBinding) binding;
        }
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
            if (pagerState != null) {
                pagerState.setIsOffline(false);
                this.getDataBinding().setPagerState(pagerState);
            }

            /* When being online for the first time, adapter is null. */
            RepositorySearchAdapter adapter = ((RepositorySearchAdapter) this.getDataBinding().recyclerviewRepositories.getAdapter());
            if (adapter == null) {
                /* Needs to run on UiThread */
                requireActivity().runOnUiThread(() -> {
                    String queryString = getDataBinding().recyclerviewRepositories.getQueryString();
                    if (queryString == null) {
                        QueryStringAdapter queryStringArrayAdapter = (QueryStringAdapter) getDataBinding().toolbarRepositories.spinnerQueryString.getAdapter();
                        if (queryStringArrayAdapter != null && queryStringArrayAdapter.getCount() > 0) {
                            queryString = queryStringArrayAdapter.getItem(0).getValue();
                        }
                    }
                    if (queryString != null) {
                        getDataBinding().recyclerviewRepositories.setAdapter(new RepositorySearchAdapter(requireActivity(), queryString, showRepositoryTopics, 1));
                    }
                });
            } else if (adapter.getItemCount() == 0) {
                /* If required, fetch page 1 */
                adapter.fetchPage(1);
            }
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
    public void onLogin(@NonNull User item) {
        this.mDataBinding.setUser(item);
    }
}
