package io.syslogic.github.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import io.syslogic.github.BuildConfig;
import io.syslogic.github.R;
import io.syslogic.github.databinding.RepositoriesFragmentBinding;
import io.syslogic.github.model.SpinnerItem;
import io.syslogic.github.spinner.TopicsAdapter;
import io.syslogic.github.recyclerview.RepositoriesAdapter;
import io.syslogic.github.recyclerview.RepositoriesLinearView;
import io.syslogic.github.recyclerview.RepositoriesScrollListener;

public class RepositoriesFragment extends BaseFragment {

    /** {@link Log} Tag */
    private static final String LOG_TAG = RepositoriesFragment.class.getSimpleName();

    /** the {@link ViewDataBinding} */
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mDataBinding = RepositoriesFragmentBinding.inflate(inflater, container, false);
        View layout = this.mDataBinding.getRoot();

        if(this.getContext() != null) {

            this.mRecyclerView = layout.findViewById(R.id.recyclerview_repositories);
            if (this.mRecyclerView.getAdapter() == null) {

                // E/RepositoriesAdapter: Unable to resolve host "api.github.com": No address associated with hostname
                if(isNetworkAvailable(getContext())) {
                    this.mRecyclerView.setAdapter(new RepositoriesAdapter(this.getContext(), 1));
                } else {
                    // E/RecyclerView: No adapter attached; skipping layout
                    this.mRecyclerView.setAdapter(new RepositoriesAdapter(this.getContext(), 0));
                    if(mDebug) {Log.e(LOG_TAG, "isNetworkAvailable() == FALSE");}
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
                        RepositoriesScrollListener.setPageNumber(1);
                        mRecyclerView.setQuery(item.getValue());
                        mRecyclerView.clearAdapter();
                    }
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        return layout;
    }

    private RepositoriesFragmentBinding getDataBinding() {
        return this.mDataBinding;
    }
}
