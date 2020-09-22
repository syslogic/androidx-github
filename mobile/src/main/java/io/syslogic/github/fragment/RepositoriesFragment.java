package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.ToolbarPagerBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.adapter.TopicAdapter;
import io.syslogic.github.databinding.FragmentRepositoriesBinding;
import io.syslogic.github.model.User;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.ScrollListener;

/**
 * Repositories Fragment
 * @author Martin Zeitler
 * @version 1.0.0
**/
public class RepositoriesFragment extends BaseFragment {

    /** Log Tag */
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentRepositoriesBinding mDataBinding;

    public RepositoriesFragment() {}

    @NonNull
    public static RepositoriesFragment newInstance() {
        return new RepositoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setDataBinding(FragmentRepositoriesBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        if(this.getContext() != null && this.getActivity() != null) {

            this.getDataBinding().setPager(new PagerState());

            /* Setting up the toolbar required in order to show the settings menu. */
            ((BaseActivity) this.getActivity()).setSupportActionBar(this.getDataBinding().toolbarQuery.toolbarQuery);
            this.getDataBinding().toolbarQuery.toolbarQuery.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.menu_action_topics:


                            return true;
                    }
                    return false;
                }
            });

            AppCompatSpinner spinner = this.getDataBinding().toolbarQuery.spinnerTopic;
            spinner.setAdapter(new TopicAdapter(this.getContext()));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                    if (count > 0) {
                        SpinnerItem item = (SpinnerItem) view.getTag();
                        ScrollListener.setPageNumber(1);
                        getDataBinding().recyclerviewRepositories.setQueryString(item.getValue());
                        if (getDataBinding().recyclerviewRepositories.getAdapter() != null) {
                            getDataBinding().recyclerviewRepositories.clearAdapter();
                            ((RepositoriesAdapter) getDataBinding().recyclerviewRepositories.getAdapter()).fetchPage(1);
                        }
                        if(mDebug) {
                            String text = getDataBinding().recyclerviewRepositories.getQueryString();
                            getDataBinding().toolbarPager.textQueryString.setText(text);
                        }
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (this.getDataBinding().recyclerviewRepositories.getAdapter() == null) {
                if(isNetworkAvailable(this.getContext())) {
                    this.getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                    if(mDebug) {
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

    @NonNull
    public FragmentRepositoriesBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentRepositoriesBinding) binding;
    }

    @Override
    protected void setCurrentUser(@Nullable User value) {
        this.currentUser = value;
    }

    @Override
    public void onNetworkAvailable() {

        super.onNetworkAvailable();

        if(this.getContext() != null) {

            String token = this.getAccessToken(this.getContext());
            if(getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            if(this.mDataBinding != null) {

                PagerState pagerState = this.getDataBinding().toolbarPager.getPager();
                if (pagerState != null) {
                    pagerState.setIsOffline(false);
                    this.getDataBinding().toolbarPager.setPager(pagerState);
                } else {
                    /* this happens on LOLLIPOP_MR1 */
                }

                /* when online for the first time */
                RepositoriesAdapter adapter = ((RepositoriesAdapter) this.getDataBinding().recyclerviewRepositories.getAdapter());
                if(adapter == null) {

                    /* needs to run on UiThread */
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(getActivity() != null) {
                                    getDataBinding().recyclerviewRepositories.setAdapter(new RepositoriesAdapter(getActivity(), 1));
                                }
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
        if(this.getContext() != null) {
            ToolbarPagerBinding pager = this.getDataBinding().toolbarPager;
            PagerState state;
            if(pager.getPager() == null) {
                state = new PagerState();
                state.setIsOffline(true);
            } else {
                state = pager.getPager();
                state.setIsOffline(true);
            }
            pager.setPager(state);
        }
    }

    @Override
    public void onLogin(@NonNull User item) {

    }
}
