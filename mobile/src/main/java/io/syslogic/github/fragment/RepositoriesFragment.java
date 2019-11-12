package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;

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

        if(this.getContext() != null) {

            this.mDataBinding.setPager(new PagerState());

            AppCompatSpinner spinner = this.mDataBinding.toolbarQuery.spinnerTopic;
            spinner.setAdapter(new TopicAdapter(this.getContext()));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                    if (count > 0) {
                        SpinnerItem item = (SpinnerItem) view.getTag();
                        ScrollListener.setPageNumber(1);
                        mDataBinding.recyclerviewRepositories.setQueryString(item.getValue());
                        if (mDataBinding.recyclerviewRepositories.getAdapter() != null) {
                            mDataBinding.recyclerviewRepositories.clearAdapter();
                            ((RepositoriesAdapter) mDataBinding.recyclerviewRepositories.getAdapter()).fetchPage(1);
                        }
                        if(mDebug) {
                            String text = mDataBinding.recyclerviewRepositories.getQueryString();
                            getDataBinding().toolbarPager.textQueryString.setText(text);
                        }
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (this.mDataBinding.recyclerviewRepositories.getAdapter() == null) {
                if(isNetworkAvailable(this.getContext())) {
                    this.mDataBinding.recyclerviewRepositories.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                    if(mDebug) {
                        String text = this.mDataBinding.recyclerviewRepositories.getQueryString();
                        this.mDataBinding.toolbarPager.textQueryString.setText(text);
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
        if(this.getContext() != null && this.mDataBinding != null) {

            String token = this.getAccessToken(this.getContext());
            if(getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            PagerState state = this.mDataBinding.toolbarPager.getPager();
            if(state != null) {
                state.setIsOffline(false);
                this.mDataBinding.toolbarPager.setPager(state);
            } else {
                /* this happens on LOLLIPOP_MR1 */
            }

            /* when online for the first time */
            RepositoriesAdapter adapter = ((RepositoriesAdapter) this.mDataBinding.recyclerviewRepositories.getAdapter());
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

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
        if(this.getContext() != null && this.mDataBinding != null) {
            PagerState state;
            if(this.mDataBinding.toolbarPager.getPager() == null) {
                state = new PagerState();
                state.setIsOffline(true);
                this.mDataBinding.toolbarPager.setPager(state);
            } else {
                state = this.mDataBinding.toolbarPager.getPager();
                state.setIsOffline(true);
            }
            this.mDataBinding.toolbarPager.setPager(state);
        }
    }

    @Override
    public void onLogin(@NonNull User item) {

    }
}
