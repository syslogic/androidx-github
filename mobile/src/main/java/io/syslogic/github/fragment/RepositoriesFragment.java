package io.syslogic.github.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.R;
import io.syslogic.github.databinding.RepositoriesFragmentBinding;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.network.IConnectivityListener;
import io.syslogic.github.spinner.TopicsAdapter;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.RepositoriesLinearView;
import io.syslogic.github.recyclerview.ScrollListener;

public class RepositoriesFragment extends BaseFragment  implements IConnectivityListener {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** ViewDataBinding {@link RepositoriesFragmentBinding} */
    private RepositoriesFragmentBinding mDataBinding;

    /** the {@link AppCompatSpinner} */
    private AppCompatSpinner mSpinnerTopic;

    /** the {@link RecyclerView} */
    private RepositoriesLinearView mRecyclerView;

    public RepositoriesFragment() {

    }

    public static RepositoriesFragment newInstance() {
        return new RepositoriesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.registerNetworkCallback(this.getContext(), this);
        } else {
            this.registerBroadcastReceiver(this.getContext());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mDataBinding = RepositoriesFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();

        if(this.getContext() != null) {

            this.mRecyclerView = layout.findViewById(R.id.recyclerview_repositories);
            if (this.mRecyclerView.getAdapter() == null) {
                if(isNetworkAvailable(this.getContext())) {
                    this.mRecyclerView.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                } else {
                    this.onNetworkLost();
                }
            }

            this.mSpinnerTopic = layout.findViewById(R.id.spinner_topic);
            this.mSpinnerTopic.setAdapter(new TopicsAdapter(this.getContext()));
            this.mSpinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (count > 0) {
                        SpinnerItem item = (SpinnerItem) view.getTag();
                        ScrollListener.setPageNumber(1);
                        mRecyclerView.setQueryString(item.getValue());
                        if (mRecyclerView.getAdapter() != null) {
                            mRecyclerView.clearAdapter();
                            ((RepositoriesAdapter) mRecyclerView.getAdapter()).fetchPage(1);
                        }
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        return layout;
    }

    public RepositoriesFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    public void onNetworkAvailable() {
        if (mDebug) {Log.d(LOG_TAG, "network connection is available.");}
        if(mRecyclerView != null) {
            RepositoriesAdapter adapter = ((RepositoriesAdapter) mRecyclerView.getAdapter());
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
