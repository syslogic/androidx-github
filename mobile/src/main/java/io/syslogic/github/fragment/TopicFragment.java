package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.FragmentTopicBinding;

/**
 * Topic Fragment
 * @author Martin Zeitler
 */
public class TopicFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_topic;

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = TopicFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentTopicBinding mDataBinding;

    /** Constructor */
    public TopicFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentTopicBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarTopic.toolbarTopic);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            activity.getSupportActionBar().setTitle(R.string.text_edit_topic);
        }

        this.getDataBinding().toolbarTopic.home.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(getDataBinding().getRoot());
            controller.navigateUp();
        });

        return layout;
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
