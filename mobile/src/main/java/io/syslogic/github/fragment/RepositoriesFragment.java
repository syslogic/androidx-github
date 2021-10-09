package io.syslogic.github.fragment;

import android.os.Bundle;
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
import io.syslogic.github.databinding.ToolbarPagerBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.adapter.TopicAdapter;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.network.TokenCallback;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.ScrollListener;

/**
 * Repositories Fragment
 * @author Martin Zeitler
 */
public class RepositoriesFragment extends BaseFragment implements TokenCallback {

    /** Log Tag */
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentRepositoriesBinding mDataBinding;

    public RepositoriesFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentRepositoriesBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        if (this.getContext() != null && this.getActivity() != null) {

            this.getDataBinding().setPager(new PagerState());

            /* Setting up the toolbar, in order to show the topics editor. */
            ((BaseActivity) this.getActivity()).setSupportActionBar(this.getDataBinding().toolbarQuery.toolbarQuery);
            this.getDataBinding().toolbarQuery.toolbarQuery.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_action_topics) {
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
                        getDataBinding().recyclerviewRepositories.setQueryString(item.getValue());
                        getDataBinding().toolbarPager.textQueryString.setText( getDataBinding().recyclerviewRepositories.getQueryString());

                        if (getDataBinding().recyclerviewRepositories.getAdapter() != null) {
                            getDataBinding().recyclerviewRepositories.clearAdapter();
                            ((RepositoriesAdapter) getDataBinding().recyclerviewRepositories.getAdapter()).fetchPage(1);
                        }
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (this.getDataBinding().recyclerviewRepositories.getAdapter() == null) {
                if (isNetworkAvailable(this.getContext())) {
                    this.getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                    if (mDebug) {
                        String text = this.getDataBinding().recyclerviewRepositories.getQueryString();
                        this.getDataBinding().toolbarPager.textQueryString.setText(text);
                    }
                } else {
                    // this.onNetworkLost();
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
        this.mDataBinding = (FragmentRepositoriesBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {

        super.onNetworkAvailable();

        if (this.getContext() != null) {

            String token = this.getAccessToken(this.getContext());
            if (getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            if (this.mDataBinding != null) {

                PagerState pagerState = this.getDataBinding().toolbarPager.getPager();
                if (pagerState != null) {
                    pagerState.setIsOffline(false);
                    this.getDataBinding().toolbarPager.setPager(pagerState);
                } else {
                    /* this happens on LOLLIPOP_MR1 */
                }

                /* when online for the first time */
                RepositoriesAdapter adapter = ((RepositoriesAdapter) this.getDataBinding().recyclerviewRepositories.getAdapter());
                if (adapter == null) {

                    /* needs to run on UiThread */
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (getActivity() != null) {
                                getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(getActivity(), 1));
                            }
                        });
                    }

                    /* if required, fetch page 1 */
                } else if (adapter.getItemCount() == 0) {
                    adapter.fetchPage(1);
                }
            }
        }
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
        if (this.getContext() != null) {
            ToolbarPagerBinding pager = this.getDataBinding().toolbarPager;
            PagerState state;
            if (pager.getPager() == null) {
                state = new PagerState();
            } else {
                state = pager.getPager();
            }
            state.setIsOffline(true);
            pager.setPager(state);
        }
    }

    @Override
    public void onLogin(@NonNull User item) {

    }
}
