package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
public class TopicsFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

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
            this.getDataBinding().toolbarTopics.toolbarTopics.setOnMenuItemClickListener(this);

            if (this.getDataBinding().recyclerviewTopics.getAdapter() == null) {
                this.getDataBinding().recyclerviewTopics.setAdapter(new TopicsAdapter(requireContext()));
            }
        }
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.topics, menu);
    }

    @NonNull
    public FragmentTopicsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentTopicsBinding) binding;
    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
        if (item.getItemId() == R.id.menu_action_add_topic) {
            controller.navigate(R.id.action_topicsFragment_to_topicFragment);
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
