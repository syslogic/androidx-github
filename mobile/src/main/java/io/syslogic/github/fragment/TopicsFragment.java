package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.databinding.FragmentTopicsBinding;
import io.syslogic.github.recyclerview.TopicsAdapter;

/**
 * Topics Fragment
 * @author Martin Zeitler
 */
public class TopicsFragment extends BaseFragment {

    /** Log Tag */
    private static final String LOG_TAG = TopicsFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentTopicsBinding mDataBinding;

    public TopicsFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setDataBinding(FragmentTopicsBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();
        if(this.getContext() != null && this.getActivity() != null) {
            if (this.getDataBinding().recyclerviewTopics.getAdapter() == null) {
                this.getDataBinding().recyclerviewTopics.setAdapter(new TopicsAdapter(this.getContext()));
            }
        }
        return layout;
    }

    @NonNull
    public FragmentTopicsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentTopicsBinding) binding;
    }
}
