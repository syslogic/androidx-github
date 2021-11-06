package io.syslogic.github.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Repositories {@link RecyclerView}
 *
 * @author Martin Zeitler
 */
public class RepositoriesLinearView extends RecyclerView {

    LinearLayoutManager mLinearLayoutManager;
    ScrollListener scrollListener;

    /** Constructor */
    public RepositoriesLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public RepositoriesLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        this.mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(this.mLinearLayoutManager);

        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        this.scrollListener = new ScrollListener(this.mLinearLayoutManager) {
            @Override
            public boolean onLoadPage(int pageNumber, int totalItemsCount) {
                RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
                if (adapter != null) {
                    adapter.fetchPage(pageNumber);
                    return true;
                }
                return false;
            }
        };
        this.addOnScrollListener(scrollListener);
    }

    public void clearAdapter() {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }

    public void setQueryString(@NonNull String value) {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {adapter.setQueryString(value);}
    }

    @Nullable
    public String getQueryString() {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {return adapter.getQueryString();}
        return null;
    }

    @NonNull
    public ScrollListener getOnScrollListener() {
        return this.scrollListener;
    }
}
