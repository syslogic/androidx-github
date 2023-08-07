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
import io.syslogic.github.databinding.FragmentQueryStringsBinding;
import io.syslogic.github.provider.QueryStringsMenuProvider;
import io.syslogic.github.recyclerview.QueryStringsAdapter;

/**
 * Query-Strings {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class QueryStringsFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused") private static final String LOG_TAG = QueryStringsFragment.class.getSimpleName();

    /** Layout resource ID kept for reference. */
    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_query_strings;

    /** Data-Binding */
    private FragmentQueryStringsBinding mDataBinding;

    /** Constructor */
    public QueryStringsFragment() {}

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseActivity activity = ((BaseActivity) this.requireActivity());
        this.setDataBinding(FragmentQueryStringsBinding.inflate(inflater, container, false));

        /* Setting up the toolbar, in order to show the topics editor. */
        activity.setSupportActionBar(this.getDataBinding().toolbarQueryStrings.toolbarQueryStrings);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            // this.getDataBinding().toolbarQueryStrings.toolbarQueryStrings.setOnMenuItemClickListener(this);
            actionbar.setHomeButtonEnabled(true);
            actionbar.setTitle(R.string.text_bookmarks);
        }

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new QueryStringsMenuProvider(activity));

        this.getDataBinding().toolbarQueryStrings.home.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(getDataBinding().getRoot());
            controller.navigateUp();
        });

        if (this.getDataBinding().recyclerviewQueryStrings.getAdapter() == null) {
            this.getDataBinding().recyclerviewQueryStrings.setAdapter(new QueryStringsAdapter(requireContext()));
        }

        return this.getDataBinding().getRoot();
    }

    @NonNull
    public FragmentQueryStringsBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentQueryStringsBinding) binding;
    }
}
