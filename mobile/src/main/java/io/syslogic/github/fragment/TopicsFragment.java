package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.FragmentTopicsBinding;
import io.syslogic.github.recyclerview.TopicsAdapter;

/**
 * Topics Fragment
 * @author Martin Zeitler
 */
public class TopicsFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_topics;

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = TopicsFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentTopicsBinding mDataBinding;

    public TopicsFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentTopicsBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarTopics.toolbarTopics);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(R.string.text_topics);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            this.getDataBinding().toolbarTopics.toolbarTopics.setOnMenuItemClickListener(item -> {
                NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
                if (item.getItemId() == R.id.menu_action_add_topic) {
                    controller.navigate(R.id.action_topicsFragment_to_topicFragment);
                    return false;
                } else {
                    return super.onOptionsItemSelected(item);
                }});

            this.getDataBinding().toolbarTopics.toolbarTopics.setNavigationOnClickListener(view -> {
                NavController controller = Navigation.findNavController(getDataBinding().getRoot());
                controller.navigateUp();
            });

            if (this.getDataBinding().recyclerviewTopics.getAdapter() == null) {
                this.getDataBinding().recyclerviewTopics.setAdapter(new TopicsAdapter(requireContext()));
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
