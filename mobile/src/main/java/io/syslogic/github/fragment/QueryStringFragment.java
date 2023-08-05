package io.syslogic.github.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import io.syslogic.github.R;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.Constants;
import io.syslogic.github.api.model.QueryString;
import io.syslogic.github.api.room.Abstraction;
import io.syslogic.github.api.room.QueryStringsDao;
import io.syslogic.github.databinding.FragmentQueryStringBinding;

/**
 * Query-String {@link BaseFragment}
 *
 * @author Martin Zeitler
 */
@BindingMethods({
    @BindingMethod(
            type = com.google.android.material.textfield.TextInputEditText.class,
            attribute = "android:onEditorAction",
            method = "setOnEditorActionListener"
    )
})
public class QueryStringFragment extends BaseFragment {

    /** Log Tag */
    @SuppressWarnings("unused")
    private static final String LOG_TAG = QueryStringFragment.class.getSimpleName();

    @SuppressWarnings("unused") private static final int resId = R.layout.fragment_query_string;

    /** Data Binding */
    private FragmentQueryStringBinding mDataBinding;

    private Long itemId = -1L;

    /** Constructor */
    public QueryStringFragment() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (itemId == -1L && args != null) {
            this.setItemId(args.getLong(Constants.ARGUMENT_ITEM_ID));
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.setDataBinding(FragmentQueryStringBinding.inflate(inflater, container, false));

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarQueryString.toolbarQueryString);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {actionbar.setHomeButtonEnabled(true);}

        this.getDataBinding().toolbarQueryString.home.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(this.getDataBinding().getRoot());
            controller.navigateUp();
        });

        this.getDataBinding().buttonSave.setOnClickListener(view -> {
            QueryStringsDao dao = Abstraction.getInstance(requireContext()).queryStringsDao();
            QueryString item = this.getDataBinding().getItem();
            Abstraction.executorService.execute(() -> {
                assert dao != null;
                if (itemId == -1L) {itemId = dao.insert(item);}
                else {dao.update(item);}
                requireActivity().runOnUiThread(() -> {
                    NavController controller = Navigation.findNavController(getDataBinding().getRoot());
                    controller.navigateUp();
                });
            });
        });

        if (this.itemId == -1L) {
            QueryString item = new QueryString();
            this.getDataBinding().setItem(item);
        } else {
            QueryStringsDao dao = Abstraction.getInstance(requireContext()).queryStringsDao();
            Abstraction.executorService.execute(() -> {
                assert dao != null;
                QueryString item = dao.getItem(getItemId());
                this.getDataBinding().setItem(item);
            });
        }

        return this.getDataBinding().getRoot();
    }

    @NonNull
    private Long getItemId() {
        return this.itemId;
    }

    private void setItemId(@NonNull Long value) {
        this.itemId = value;
    }

    @NonNull
    public FragmentQueryStringBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentQueryStringBinding) binding;
    }
}
