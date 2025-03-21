package io.syslogic.github.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.adapter.QueryStringAdapter;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.model.User;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.QueryStringsDao;
import io.syslogic.github.databinding.FragmentRepositorySearchBinding;
import io.syslogic.github.menu.RepositorySearchMenuProvider;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.network.TokenCallback;
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
        this.setDataBinding(inflater, container);
        this.getDataBinding().setPagerState(new PagerState());

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new RepositorySearchMenuProvider(activity));

        /* the SpinnerItem has the same ID as the QueryString. */
        activity.setSupportActionBar(this.getDataBinding().toolbarRepositorySearch.toolbarRepositorySearch);
        this.mDataBinding.toolbarRepositorySearch.home.setOnClickListener(view -> {
            activity.getOnBackPressedDispatcher().onBackPressed();
        });

        AppCompatSpinner spinner = this.getDataBinding().toolbarRepositorySearch.spinnerQueryString;
        spinner.setAdapter(new QueryStringAdapter(requireContext()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                if (count > 0) {
                    SpinnerItem item = (SpinnerItem) view.getTag();
                    ScrollListener.setPageNumber(1);
                    String queryString = item.getValue();
                    RepositorySearchLinearView recyclerview = getDataBinding().recyclerviewRepositorySearch;
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
        if (this.getDataBinding().recyclerviewRepositorySearch.getAdapter() == null) {
            if (isNetworkAvailable(requireContext())) {
                QueryStringsDao dao = Abstraction.getInstance(requireContext()).queryStringsDao();
                Abstraction.executorService.execute(() -> {
                    try {
                        assert dao != null;
                        String queryString = null;
                        List<QueryString> items = dao.getItems();
                        if (! items.isEmpty()) {
                            queryString = items.get(0).toQueryString();

                        } else {
                            this.getDataBinding().toolbarRepositorySearch.spinnerQueryString.setVisibility(View.INVISIBLE);
                            queryString = Constants.DEFAULT_QUERY_STRING;
                            if (mDebug) {
                                Log.w(LOG_TAG, "Table `query_strings` currently has no records.");
                                Log.w(LOG_TAG, "Using thr default value: \"" + Constants.DEFAULT_QUERY_STRING + "\".");
                            }
                        }

                        String finalQueryString = queryString;
                        requireActivity().runOnUiThread(() -> {
                            RepositorySearchAdapter adapter = new RepositorySearchAdapter(requireContext(), finalQueryString, showRepositoryTopics, 1);
                            getDataBinding().recyclerviewRepositorySearch.setAdapter(adapter);
                            PagerState pagerState = getDataBinding().getPagerState();
                            if (pagerState != null) {
                                pagerState.setQueryString(finalQueryString);
                                getDataBinding().setPagerState(pagerState);
                            }
                        });

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
            RepositorySearchAdapter adapter = ((RepositorySearchAdapter) this.getDataBinding().recyclerviewRepositorySearch.getAdapter());
            if (adapter == null) {
                /* Needs to run on UiThread */
                requireActivity().runOnUiThread(() -> {
                    String queryString = getDataBinding().recyclerviewRepositorySearch.getQueryString();
                    if (queryString == null) {
                        QueryStringAdapter queryStringArrayAdapter = (QueryStringAdapter) getDataBinding().toolbarRepositorySearch.spinnerQueryString.getAdapter();
                        if (queryStringArrayAdapter != null && queryStringArrayAdapter.getCount() > 0) {
                            queryString = queryStringArrayAdapter.getItem(0).getValue();
                        }
                    }
                    if (queryString != null) {
                        getDataBinding().recyclerviewRepositorySearch.setAdapter(new RepositorySearchAdapter(requireActivity(), queryString, showRepositoryTopics, 1));
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

    @Override
    protected void setDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        this.mDataBinding = FragmentRepositorySearchBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    public FragmentRepositorySearchBinding getDataBinding() {
        return this.mDataBinding;
    }
}
