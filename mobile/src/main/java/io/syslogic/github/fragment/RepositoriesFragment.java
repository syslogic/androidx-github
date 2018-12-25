package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.databinding.ViewDataBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.adapter.TopicAdapter;
import io.syslogic.github.databinding.RepositoriesFragmentBinding;
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
    private RepositoriesFragmentBinding mDataBinding;

    public RepositoriesFragment() {

    }

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

        this.setDataBinding(RepositoriesFragmentBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        if(this.getContext() != null) {

            this.mDataBinding.setPager(new PagerState());

            this.mDataBinding.toolbarQuery.spinnerTopic.setAdapter(new TopicAdapter(this.getContext()));
            this.mDataBinding.toolbarQuery.spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long resId) {
                    if (count > 0) {
                        SpinnerItem item = (SpinnerItem) view.getTag();
                        ScrollListener.setPageNumber(1);
                        getDataBinding().recyclerview.setQueryString(item.getValue());
                        if (getDataBinding().recyclerview.getAdapter() != null) {
                            getDataBinding().recyclerview.clearAdapter();
                            ((RepositoriesAdapter) getDataBinding().recyclerview.getAdapter()).fetchPage(1);
                        }
                        if(mDebug) {
                            String text = getDataBinding().recyclerview.getQueryString();
                            getDataBinding().toolbarPager.textQueryString.setText(text);
                        }
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            if (this.mDataBinding.recyclerview.getAdapter() == null) {
                if(isNetworkAvailable(this.getContext())) {
                    this.mDataBinding.recyclerview.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                    if(mDebug) {
                        String text = this.mDataBinding.recyclerview.getQueryString();
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
    public RepositoriesFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void setDataBinding(@NonNull ViewDataBinding dataBinding) {
        this.mDataBinding = (RepositoriesFragmentBinding) dataBinding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        if(this.getContext() != null && this.mDataBinding != null) {

            String token = this.getAccessToken(this.getContext());
            if(getCurrentUser() == null && token != null) {
                this.setUser(token, this);
            }

            if(this.mDataBinding.toolbarPager != null) {
                PagerState state = this.mDataBinding.toolbarPager.getPager();
                state.setIsOffline(false);
                this.mDataBinding.toolbarPager.setPager(state);
            }

            if(this.mDataBinding.recyclerview != null) {

                RepositoriesAdapter adapter = ((RepositoriesAdapter) this.mDataBinding.recyclerview.getAdapter());

                /* when online for the first time */
                if(adapter == null) {

                    /* needs to run on UiThread */
                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDataBinding.recyclerview.setAdapter(new RepositoriesAdapter(getContext(), 1));
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
        if(this.getContext() != null && this.mDataBinding != null) {

            if(this.mDataBinding.toolbarPager != null) {
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
    }

    @Override
    public void onLogin(@NonNull User item) {

    }
}
