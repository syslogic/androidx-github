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

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.adapter.TopicAdapter;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.RepositoriesLinearView;
import io.syslogic.github.recyclerview.ScrollListener;

/**
 * Repositories Fragment
 * @author Martin Zeitler
 */
public class RepositoriesFragment extends BaseFragment implements TokenCallback {

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_repositories;

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentRepositoriesBinding mDataBinding;

    /** Constructor */
    public RepositoriesFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentRepositoriesBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        if (this.getContext() != null && this.getActivity() != null) {

            this.getDataBinding().setPagerState(new PagerState());

            /* Setting up the toolbar, in order to show the topics editor. */
            ((BaseActivity) this.requireActivity()).setSupportActionBar(this.getDataBinding().toolbarQuery.toolbarQuery);
            this.getDataBinding().toolbarQuery.toolbarQuery.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_action_edit_topics) {
                NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
                controller.navigate(R.id.action_repositoriesFragment_to_topicsGraph);
                return false;
            }  else {
                return super.onOptionsItemSelected(item);
            }});

            AppCompatSpinner spinner = this.getDataBinding().toolbarQuery.spinnerTopic;
            spinner.setAdapter(new TopicAdapter(this.getContext()));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                    if (count > 0) {
                        SpinnerItem item = (SpinnerItem) view.getTag();
                        ScrollListener.setPageNumber(1);

                        // the SpinnerItem has the same ID as the Topic.
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

            if (this.getDataBinding().recyclerviewRepositories.getAdapter() == null) {
                if (isNetworkAvailable(requireContext())) {
                    /* The TopicAdapter takes a while to populate. */
                    TopicAdapter topicAdapter = (TopicAdapter) getDataBinding().toolbarQuery.spinnerTopic.getAdapter();
                    if (topicAdapter != null) {
                        if (topicAdapter.getCount() == 0) {
                            if (mDebug) {Log.w(LOG_TAG, "TopicAdapter.getCount() is 0");}
                            while (topicAdapter.getCount() == 0) {
                                if (mDebug) {Log.w(LOG_TAG, "TopicAdapter.getCount() is still 0");}
                            }
                        }

                        if (topicAdapter.getCount() > 0) {
                            String queryString = topicAdapter.getItem(0).getValue();
                            this.getDataBinding().recyclerviewRepositories.setAdapter(
                                    new RepositoriesAdapter(requireContext(), queryString, 1)
                            );
                            PagerState pagerState = this.getDataBinding().getPagerState();
                            if (pagerState != null) {
                                pagerState.setQueryString(queryString);
                                this.getDataBinding().setPagerState(pagerState);
                            }
                        } else {
                            /* topics would need to be added */
                            // NavController controller = Navigation.findNavController(layout);
                            // controller.navigate(R.id.action_repositoriesFragment_to_topicsGraph);
                        }
                    }
                } else {
                    this.onNetworkLost();
                }
            }
        }
        return layout;
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
                        TopicAdapter topicAdapter = (TopicAdapter) getDataBinding().toolbarQuery.spinnerTopic.getAdapter();
                        if (topicAdapter != null && topicAdapter.getCount() > 0) {
                            queryString = topicAdapter.getItem(0).getValue();
                        }
                    }
                    if (queryString != null) {
                        getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(requireActivity(), queryString, 1));
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
