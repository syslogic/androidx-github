package io.syslogic.github.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.List;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.QueryString;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.adapter.QueryStringAdapter;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.RepositoriesLinearView;
import io.syslogic.github.recyclerview.ScrollListener;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.QueryStringsDao;

/**
 * Repositories Fragment
 *
 * @author Martin Zeitler
 */
public class RepositoriesFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_repositories;

    /** Data Binding */
    private FragmentRepositoriesBinding mDataBinding;

    boolean showTopics = false;

    /** Constructor */
    public RepositoriesFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            this.setDataBinding(FragmentRepositoriesBinding.inflate(inflater, container, false));
            this.getDataBinding().setPagerState(new PagerState());
            this.setHasOptionsMenu(true);

            /* Setting up the toolbar, in order to show the topics editor. */
            ((BaseActivity) this.requireActivity()).setSupportActionBar(this.getDataBinding().toolbarRepositories.toolbarQuery);
            this.getDataBinding().toolbarRepositories.toolbarQuery.setOnMenuItemClickListener(item -> {
                NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
                if (item.getItemId() == R.id.menu_action_bookmarks) {
                    controller.navigate(R.id.action_repositoriesFragment_to_queryStringsGraph);
                    return false;
                } else if (item.getItemId() == R.id.menu_action_preferences) {
                    controller.navigate(R.id.action_repositoriesFragment_to_preferencesFragment);
                    return false;
                } else {
                    return super.onOptionsItemSelected(item);
                }});

            // the SpinnerItem has the same ID as the QueryString.
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
                        RepositoriesLinearView recyclerview = getDataBinding().recyclerviewRepositories;
                        recyclerview.setQueryString(queryString);
                        PagerState pagerState = getDataBinding().getPagerState();
                        if (pagerState != null) {
                            pagerState.setQueryString(queryString);
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

            /* It is quicker to query Room, because the QueryStringAdapter is populating too slow. */
            showTopics = this.prefs.getBoolean(Constants.PREFERENCE_KEY_REPOSITORY_TOPICS, false);
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
                                    RepositoriesAdapter adapter = new RepositoriesAdapter(requireContext(), queryString, showTopics, 1);
                                    getDataBinding().recyclerviewRepositories.setAdapter(adapter);
                                    PagerState pagerState = getDataBinding().getPagerState();
                                    if (pagerState != null) {
                                        pagerState.setQueryString(queryString);
                                        getDataBinding().setPagerState(pagerState);
                                    }
                                });
                            } else {
                                if (mDebug) {Log.e(LOG_TAG, "table `" + Constants.TABLE_QUERY_STRINGS +"` has no records.");}
                                this.getDataBinding().toolbarRepositories.spinnerQueryString.setVisibility(View.INVISIBLE);
                            }
                        } catch (IllegalStateException e) {
                            if (mDebug) {Log.e(LOG_TAG, e.getMessage());}
                        }
                    });
                } else {
                    this.onNetworkLost();
                }
            }
        }
        return this.getDataBinding().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings, menu);
    }

    @NonNull
    public FragmentRepositoriesBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        if (binding instanceof FragmentRepositoriesBinding) {
            this.mDataBinding = (FragmentRepositoriesBinding) binding;
        }
    }

    @Override
    public void onNetworkAvailable() {

        super.onNetworkAvailable();

        String token = this.getAccessToken(requireContext());
        if (getCurrentUser() == null && token != null) {
            this.setUser(token, this);
        }

        if (mDataBinding != null) {
          PagerState pagerState = this.getDataBinding().getPagerState();
            if (pagerState != null) {
                pagerState.setIsOffline(false);
                this.getDataBinding().setPagerState(pagerState);
            }

            /* when being online for the first time, adapter is null. */
            RepositoriesAdapter adapter = ((RepositoriesAdapter) this.getDataBinding().recyclerviewRepositories.getAdapter());
            if (adapter == null) {
                /* needs to run on UiThread */
                requireActivity().runOnUiThread(() -> {
                    String queryString = getDataBinding().recyclerviewRepositories.getQueryString();
                    if (queryString == null) {
                        QueryStringAdapter queryStringArrayAdapter = (QueryStringAdapter) getDataBinding().toolbarRepositories.spinnerQueryString.getAdapter();
                        if (queryStringArrayAdapter != null && queryStringArrayAdapter.getCount() > 0) {
                            queryString = queryStringArrayAdapter.getItem(0).getValue();
                        }
                    }
                    if (queryString != null) {
                        getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(requireActivity(), queryString, showTopics, 1));
                    }
                });
            } else if (adapter.getItemCount() == 0) {
                /* if required, fetch page 1 */
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

    }
}
