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
import io.syslogic.github.Constants;
import io.syslogic.github.databinding.FragmentTopicBinding;
import io.syslogic.github.model.Topic;
import io.syslogic.github.room.Abstraction;
import io.syslogic.github.room.TopicsDao;

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

    private Long itemId = 0L;

    /** Constructor */
    public TopicFragment() {}

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

        this.setDataBinding(FragmentTopicBinding.inflate(inflater, container, false));
        this.getDataBinding().setLifecycleOwner(this);

        BaseActivity activity = ((BaseActivity) this.requireActivity());
        activity.setSupportActionBar(this.getDataBinding().toolbarTopic.toolbarTopic);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
        }

        this.getDataBinding().toolbarTopic.home.setOnClickListener(view -> {
            NavController controller = Navigation.findNavController(getDataBinding().getRoot());
            controller.navigateUp();
        });

        this.getDataBinding().buttonSave.setOnClickListener(view -> {
            TopicsDao dao = Abstraction.getInstance(requireContext()).topicsDao();
            Topic item = this.getDataBinding().getTopic();
            Abstraction.executorService.execute(() -> {
                if (itemId == -1L) {
                    itemId = dao.insert(item);
                } else {
                    dao.update(item);
                }
                requireActivity().runOnUiThread(() -> {
                    NavController controller = Navigation.findNavController(getDataBinding().getRoot());
                    controller.navigateUp();
                });
            });
        });

        if (this.itemId == 0) {
            this.getDataBinding().setTopic(new Topic());
        } else {
            TopicsDao dao = Abstraction.getInstance(requireContext()).topicsDao();
            Abstraction.executorService.execute(() -> {
                Topic item = dao.getItem(this.itemId);
                this.getDataBinding().setTopic(item);
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
    public FragmentTopicBinding getDataBinding() {
        return this.mDataBinding;
    }

    @Override
    protected void setDataBinding(@NonNull ViewDataBinding binding) {
        this.mDataBinding = (FragmentTopicBinding) binding;
    }
}
