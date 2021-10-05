package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.databinding.FragmentTopicBinding;

/**
 * Topic Fragment
 * @author Martin Zeitler
 */
public class TopicFragment extends BaseFragment {

    /** Log Tag */
    private static final String LOG_TAG = TopicFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentTopicBinding mDataBinding;

    public TopicFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setDataBinding(FragmentTopicBinding.inflate(inflater, container, false));
        return this.getDataBinding().getRoot();
    }

    @NonNull
    public FragmentTopicBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentTopicBinding) binding;
    }
}
