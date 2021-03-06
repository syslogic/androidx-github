package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Topics RecyclerView Linear
 * @author Martin Zeitler
**/
public class TopicsLinearView extends RecyclerView {

    LinearLayoutManager mLinearLayoutManager;

    /** Constructor */
    public TopicsLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public TopicsLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(this.mLinearLayoutManager);
        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    public void clearAdapter() {
        TopicsAdapter adapter = ((TopicsAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }
}
