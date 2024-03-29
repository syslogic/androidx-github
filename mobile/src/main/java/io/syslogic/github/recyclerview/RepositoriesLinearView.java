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
 * Repositories {@link RecyclerView}
 *
 * @author Martin Zeitler
 */
public class RepositoriesLinearView extends RecyclerView {

    ScrollListener scrollListener;

    /** Constructor */
    public RepositoriesLinearView(@NonNull Context context) {
        super(context);
    }

    /** Constructor */
    public RepositoriesLinearView(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        this.setLayoutManager(layoutManager);

        this.scrollListener = new ScrollListener(layoutManager) {
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

    @SuppressLint("NotifyDataSetChanged")
    public void clearAdapter() {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();
        }
    }

    public void setRepositoryType(@NonNull String value) {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {adapter.setRepositoryType(value);}
    }

    public void setSortField(String value) {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {adapter.setSortField(value);}
    }

    public void setSortOrder(String value) {
        RepositoriesAdapter adapter = ((RepositoriesAdapter) getAdapter());
        if (adapter != null) {adapter.setSortOrder(value);}
    }

    @NonNull
    public ScrollListener getOnScrollListener() {
        return this.scrollListener;
    }
}
