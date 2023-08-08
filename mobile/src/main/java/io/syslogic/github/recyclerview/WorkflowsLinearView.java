package io.syslogic.github.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Workflows {@link RecyclerView}
 *
 * @author Martin Zeitler
 */
public class WorkflowsLinearView extends RecyclerView {

    LinearLayoutManager mLinearLayoutManager;

    /** Constructor */
    public WorkflowsLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public WorkflowsLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(this.mLinearLayoutManager);
        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapter() {
        WorkflowsAdapter adapter = ((WorkflowsAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }
}
