package io.syslogic.github.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import io.syslogic.github.databinding.FragmentTopicBinding;
import io.syslogic.github.databinding.FragmentTopicsBinding;
import io.syslogic.github.recyclerview.TopicsAdapter;

/**
 * Topic Fragment
 * @author Martin Zeitler
 */
public class TopicFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_topic;

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = TopicFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentTopicBinding mDataBinding;

    public TopicFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentTopicBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarTopic.toolbarTopic);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(R.string.text_topics);
            this.getDataBinding().toolbarTopic.toolbarTopic.setOnMenuItemClickListener(this);
        }
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

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavController controller = Navigation.findNavController(getDataBinding().getRoot());
            controller.navigateUp();
        }
        return false;
    }
}
