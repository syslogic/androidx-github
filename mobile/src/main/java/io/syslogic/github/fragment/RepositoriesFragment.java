package io.syslogic.github.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import io.syslogic.github.databinding.RepositoriesFragmentBinding;
import io.syslogic.github.model.PagerState;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.network.IConnectivityListener;
import io.syslogic.github.adapter.TopicAdapter;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.ScrollListener;

public class RepositoriesFragment extends BaseFragment implements IConnectivityListener {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** {@link RepositoriesFragmentBinding} */
    private RepositoriesFragmentBinding mDataBinding;

    /** {@link AppCompatTextView}, only visible in debug builds */
    private AppCompatTextView mTextQueryString;

    /** {@link AppCompatSpinner} */
    private AppCompatSpinner mSpinnerTopic;

    /** {@link Toolbar} for the {@link ScrollListener} */
    private Toolbar mToolbarPager;

    public RepositoriesFragment() {

    }

    @NonNull
    public static RepositoriesFragment newInstance() {
        return new RepositoriesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(this.getContext(), this);
        } else {
            this.registerBroadcastReceiver(this.getContext());
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.mDataBinding = RepositoriesFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();

        if(this.getContext() != null) {

            this.mDataBinding.setPager(new PagerState());

            this.mDataBinding.toolbarQuery.spinnerTopic.setAdapter(new TopicAdapter(this.getContext()));
            this.mDataBinding.toolbarQuery.spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                    this.onNetworkLost();
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
    public void onNetworkAvailable() {
        if (mDebug) {Log.d(LOG_TAG, "network connection is available.");}
        if(this.mDataBinding != null && this.mDataBinding.recyclerview != null) {
            RepositoriesAdapter adapter = ((RepositoriesAdapter) this.mDataBinding.recyclerview.getAdapter());
            if (adapter != null && adapter.getItemCount() == 0) {
                adapter.fetchPage(1);
            }
        }
    }

    @Override
    public void onNetworkLost() {
        if (mDebug) {Log.w(LOG_TAG, "network connection was lost.");}
    }
}
