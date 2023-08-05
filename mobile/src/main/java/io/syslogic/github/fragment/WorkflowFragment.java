package io.syslogic.github.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.ViewDataBinding;

import io.syslogic.github.Constants;
import io.syslogic.github.R;
import io.syslogic.github.activity.NavHostActivity;
import io.syslogic.github.databinding.FragmentWorkflowBinding;
import io.syslogic.github.api.model.Workflow;
import io.syslogic.github.provider.WorkflowMenuProvider;

/**
 * Workflow {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
public class WorkflowFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = WorkflowFragment.class.getSimpleName();

    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_workflow;

    /** Data Binding */
    private FragmentWorkflowBinding mDataBinding;

    private Long itemId = -1L;

    /** Constructor */
    public WorkflowFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (itemId == 0 && args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        NavHostActivity activity = ((NavHostActivity) this.requireActivity());
        this.setDataBinding(FragmentWorkflowBinding.inflate(inflater, container, false));

        /* It removes & adds {@link BaseMenuProvider} */
        activity.setMenuProvider(new WorkflowMenuProvider(activity));

        // the SpinnerItem has the same ID as the QueryString.
        activity.setSupportActionBar(this.getDataBinding().toolbarWorkflow.toolbarWorkflow);
        this.mDataBinding.toolbarWorkflow.home.setOnClickListener(view -> activity.onBackPressed());

        if (! isNetworkAvailable(this.requireContext())) {
            this.onNetworkLost();
        } else {

            /* TODO: load from API? */
            if (itemId != -1L) {
                Workflow item = new Workflow();
                this.getDataBinding().setWorkflow(item);
            }
        }
        return this.getDataBinding().getRoot();
    }

    @NonNull
    public Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public FragmentWorkflowBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentWorkflowBinding) binding;
    }

    @Override
    public void onNetworkAvailable() {
        super.onNetworkAvailable();
    }

    @Override
    public void onNetworkLost() {
        super.onNetworkLost();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
    }
}
