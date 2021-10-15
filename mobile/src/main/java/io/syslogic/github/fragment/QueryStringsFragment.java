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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.databinding.FragmentQueryStringsBinding;
import io.syslogic.github.recyclerview.QueryStringsAdapter;

/**
 * QueryStrings Fragment
 * @author Martin Zeitler
 */
public class QueryStringsFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_query_strings;

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = QueryStringsFragment.class.getSimpleName();

    /** Data Binding */
    private FragmentQueryStringsBinding mDataBinding;

    /** Constructor */
    public QueryStringsFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);
        this.setDataBinding(FragmentQueryStringsBinding.inflate(inflater, container, false));
        View layout = this.getDataBinding().getRoot();

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarQueryStrings.toolbarQueryStrings);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            this.getDataBinding().toolbarQueryStrings.toolbarQueryStrings.setOnMenuItemClickListener(this);
            actionbar.setHomeButtonEnabled(true);
            actionbar.setTitle(R.string.text_bookmarks);
        }

        this.getDataBinding().toolbarQueryStrings.home.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(getDataBinding().getRoot());
            controller.navigateUp();
        });


        if (this.getDataBinding().recyclerviewQueryStrings.getAdapter() == null) {
            this.getDataBinding().recyclerviewQueryStrings.setAdapter(new QueryStringsAdapter(requireContext()));
        }
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bookmarks, menu);
    }

    @NonNull
    public FragmentQueryStringsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentQueryStringsBinding) binding;
    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
        if (item.getItemId() == R.id.menu_action_add_bookmark) {
            controller.navigate(R.id.action_queryStringsFragment_to_queryStringFragment);
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
